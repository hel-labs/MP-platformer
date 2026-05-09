# Change Log (2026-04-27)

## Scope
This document summarizes gameplay, UI, audio, and persistence changes implemented in this session.

## Major Additions

### 1) Player Name Entry Flow
A new screen was added after the title screen to let the player enter/select a name before entering the menu.

- Added new game state: `NAME_ENTRY`
- Added dedicated screen class with typed input, backspace support, and Enter-to-confirm
- Added key event forwarding from game panel to name entry screen

Files:
- src/main/java/com/platformer/gamestate/Gamestate.java
- src/main/java/com/platformer/gamestate/TitleScreen.java
- src/main/java/com/platformer/gamestate/PlayerNameEntryScreen.java (new)
- src/main/java/com/platformer/core/Game.java

### 2) Player Profile Persistence
A profile manager was added to persist and reuse the current player name across sessions.

- Stores current player name in `player_profile.txt`
- Sanitizes name input and enforces max length
- Supports loading saved name automatically

Files:
- src/main/java/com/platformer/utils/PlayerProfileManager.java (new)
- player_profile.txt (new runtime data file)

### 3) Per-Player Leaderboard Model
Leaderboard storage was refactored from anonymous run rows to per-player aggregated records.

- Old model: one row per run (`score|duration|date`)
- New model: one row per player (`playerName|bestScore|totalDuration|lastPlayed`)
- Same player name updates existing entry (case-insensitive)
- Legacy file lines are migrated/loaded with compatibility logic

Files:
- src/main/java/com/platformer/utils/LeaderboardManager.java
- src/main/java/com/platformer/gamestate/Leaderboard.java
- leaderboard.txt (updated data format/content)

### 4) Barrel Point System
Breaking a barrel now awards points in addition to potion drops.

- +1 point per broken barrel
- Points are saved into leaderboard progress for current player

Files:
- src/main/java/com/platformer/overworld/objects/ObjectManager.java
- src/main/java/com/platformer/overworld/states/Playing.java

## Major Behavior Fixes

### 1) Battle Health Persistence Fix
Player health no longer resets to full when a new battle starts.

Root cause fixed:
- Removed forced `setBattleHp(getMaxHp())` before creating battle snapshot

Result:
- Damage taken in one battle now carries into subsequent battles correctly
- Red potion healing is preserved into following battles

File:
- src/main/java/com/platformer/overworld/entities/EnemyManager.java

### 2) Battle Player Attack Sound
Battle now plays player attack SFX when the player attacks (and on enemy defeat by player attack).

- Audio dependency wired into battle state
- Triggered only for player attack outcomes

Files:
- src/main/java/com/platformer/battle/core/BattleManager.java
- src/main/java/com/platformer/battle/core/BattleState.java
- src/main/java/com/platformer/core/Game.java

## Battle UI Changes

### 1) Removed Number Labels on Bars
As requested, numeric HP/stamina text labels were removed from battle status bars.

### 2) Sprite Font Enemy Name
Enemy name rendering now uses sprite-sheet glyphs instead of plain text.

### 3) Different Hostility Bar Visual
Hostility now uses a distinct panel/bar styling from enemy HP bar.

Files:
- src/main/java/com/platformer/battle/ui/BattleUI.java

## Files Added
- src/main/java/com/platformer/gamestate/PlayerNameEntryScreen.java
- src/main/java/com/platformer/utils/PlayerProfileManager.java
- player_profile.txt

## Files Modified
- src/main/java/com/platformer/core/Game.java
- src/main/java/com/platformer/gamestate/Gamestate.java
- src/main/java/com/platformer/gamestate/TitleScreen.java
- src/main/java/com/platformer/gamestate/Leaderboard.java
- src/main/java/com/platformer/battle/core/BattleManager.java
- src/main/java/com/platformer/battle/core/BattleState.java
- src/main/java/com/platformer/battle/ui/BattleUI.java
- src/main/java/com/platformer/overworld/entities/EnemyManager.java
- src/main/java/com/platformer/overworld/objects/ObjectManager.java
- src/main/java/com/platformer/overworld/states/Playing.java
- src/main/java/com/platformer/utils/LeaderboardManager.java
- leaderboard.txt

## Validation Notes
- Static diagnostics on edited Java files: no errors reported.
- Full Gradle compile could not be executed because the wrapper jar is missing:
  - `gradle/wrapper/gradle-wrapper.jar` not found in this repository.

## Non-Gameplay/Environment Files
- `.idea/workspace.xml` changed locally by IDE state and run configuration metadata.
