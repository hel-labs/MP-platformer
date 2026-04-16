# MP-Platformer — Level System & Animation Logic

## Level System

### How Levels Are Encoded
Levels are stored as **PNG images**, not JSON or text files. Each pixel in the image encodes three types of data using its RGB channels:

- **Red channel** → tile index (which tile sprite to render at that grid position)
- **Green channel** → entity spawn (which enemy or player spawn point goes here)
- **Blue channel** → object spawn (potions, boxes, spikes, cannons, trees)

This means the entire level — tiles, enemies, and objects — is packed into a single image file and decoded in one pass.

### Level Loading Flow

```
LoadSave.GetAllLevels()
    → reads all level PNG images from resources
    → each image passed to new Level(img)

Level constructor
    → calls loadLevel() — single loop over all pixels
        → loadLevelData(red, x, y)   — fills int[][] lvlData grid
        → loadEntities(green, x, y)  — spawns enemies + player spawn point
        → loadObjects(blue, x, y)    — spawns potions, spikes, cannons etc.
    → calls calcLvlOffsets()         — computes max camera scroll distance
```

### Tile Rendering (LevelManager)
- The tileset spritesheet is sliced into 48 individual `BufferedImage` tiles (4 rows × 12 columns, each 32×32px).
- On each `draw()` call, `LevelManager` loops over the visible tile grid and draws `levelSprite[index]` at the correct screen position, offset by the camera's `lvlOffset`.
- Tile index `48` renders animated water (cycles through 4 frames), index `49` renders static water bottom.

### Camera / Scroll Offset
- `maxLvlOffsetX = TILES_SIZE × (levelWidthInTiles − screenWidthInTiles)`
- The `Playing` state tracks `xLvlOffset` and clamps it to `maxLvlOffsetX`.
- All entities and tiles subtract `xLvlOffset` from their x position when drawing.

### Multi-Level Support
- `LevelManager` holds an `ArrayList<Level>`.
- `loadNextLevel()` pushes the next level's data to the enemy manager, player, and object manager.
- `lvlIndex` is incremented when the player reaches the level exit.

---

## Animation Logic

### Spritesheet Slicing
In `Player.loadAnimations()`:
```java
animations = new BufferedImage[7][8];
for (int j = 0; j < animations.length; j++)
    for (int i = 0; i < animations[j].length; i++)
        animations[j][i] = img.getSubimage(i * 64, j * 40, 64, 40);
```
- The spritesheet is a grid of 64×40px frames.
- 7 rows = 7 states (IDLE, RUNNING, JUMP, FALLING, ATTACK, HIT, DEAD).
- 8 columns = up to 8 frames per state (not all states use all 8).

### Frame Cycling
In `Entity` (base class), every subclass inherits `aniTick` and `aniIndex`:
```java
aniTick++;
if (aniTick >= ANI_SPEED) {
    aniTick = 0;
    aniIndex++;
    if (aniIndex >= GetSpriteAmount(state))
        aniIndex = 0;
}
```
- `ANI_SPEED` controls how many game ticks each frame is held (lower = faster animation).
- `GetSpriteAmount(state)` returns how many frames that state has, so the cycle wraps correctly per state.

### State-Based Animation
- Each entity has an integer `state` field (maps to a row in the spritesheet).
- `newState(int state)` resets both `aniTick` and `aniIndex` to 0 on state change, preventing mid-cycle glitches.
- `draw()` calls `animations[state][aniIndex]` — so the correct row and frame are always rendered automatically based on current state.

### Horizontal Flip (Direction)
```java
g.drawImage(animations[state][aniIndex],
    (int)(hitbox.x - xDrawOffset) - lvlOffset + flipX,
    (int)(hitbox.y - yDrawOffset + pushDrawOffset),
    width * flipW, height, null);
```
- `flipX` and `flipW` are set based on the player's facing direction.
- `flipW = -1` mirrors the image horizontally (Java's `drawImage` supports negative width for flipping).
- No separate left-facing spritesheet is needed.

### Draw Offset
- The hitbox is smaller than the sprite frame.
- `xDrawOffset` and `yDrawOffset` align the sprite visually over the hitbox without affecting collision logic.