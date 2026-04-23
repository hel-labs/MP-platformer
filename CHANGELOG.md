# Changelog

All notable changes to MP-platformer are documented here.
Entries are sorted newest-first per branch.

---

## [main] — April 2026

### Fixed
- **Audio crash on startup** (`AudioPlayer.java`)
    - Added null-check before loading `.wav` files via `getResource()`.
    - Was the root cause of the `NullPointerException` at `AudioPlayer.getClip()` on launch.

- **Audio clips crashing on null** (`AudioPlayer.java`)
    - `stopSong()`, `toggleSongMute()`, `toggleEffectMute()`, `updateSongVolume()`, and `updateEffectsVolume()` all now skip null clips safely.
    - Previously any missing audio file would crash the entire audio subsystem.

- **Battle music persisting after level transition** (`Playing.java`, `AudioPlayer.java`)
    - `loadNextLevel()` now explicitly calls `setLevelSong()` after loading the next level.
    - `setLevelSong()` simplified — no longer alternates between two tracks, consistently uses `LEVEL_1`.
    - Previously, battle music would stay on indefinitely after the player progressed.

- **White side bars on Windows** (`GamePanel.java`)
    - Panel background set to black.
    - Entire panel is now cleared to black before rendering the scaled game area.
    - Linux was unaffected; Windows defaulted to white for the letterbox area.

### Added
- **ESC to pause** (`Playing.java`)
    - Escape key now toggles pause state during gameplay.
    - Input is correctly blocked while paused, game over, level complete, or player dying.

- **Leaderboard button label overlay** (`Menu.java`)
    - Since the button sprite atlas has no dedicated leaderboard row, the word "LEADERBOARD" is now drawn on top using Big Text glyph PNGs.
    - Text auto-scales to fit the button bounds with nearest-neighbor interpolation to preserve pixel style.
    - Falls back gracefully if atlas has 5+ rows (uses native sprite row instead).

- **Big Text sprite font** (`res/big_text/1.png` – `36.png`)
    - 36 PNG glyphs added: 1–26 map to A–Z, 27–35 map to 1–9, 36 maps to 0.
    - Used by Menu leaderboard label and Title screen renderer.

### Changed
- **Level system simplified** (`LoadSave.java`, `LevelManager.java`)
    - Only `3.png` is kept in `/res/lvls/`. All other level PNGs removed.
    - Level loader updated to support non-sequential filenames (previously required `1.png`, `2.png`, etc.).
    - `LevelManager` now duplicates the single loaded map in memory to provide 2 playable levels.

- **Barrel drop uses named constant** (`ObjectManager.java`)
    - Replaced hardcoded `int type = 1` with `RED_POTION` constant. No behavior change.

- **Window scaling** (`Game.java`)
    - `SCALE` constant at `Game.java:31` controls window size. Currently `1.5f`.
    - `TILES_SIZE`, `GAME_WIDTH`, `GAME_HEIGHT` all derive from `SCALE` automatically.

---

## [battle-rework] — April 2026

> Branch contains the full battle system. Not yet fully merged into main.

### Added
- Turn-based battle system (`BattleUI.java`, `DialogueBox.java`)
    - Action/menu text: `FONT_ACTION` — tunable at `BattleUI.java:30`
    - Hint/description text: `FONT_HINT` — tunable at `BattleUI.java:31`
    - Small labels (turn counter, HP): `FONT_SMALL` — tunable at `BattleUI.java:32`
    - Dialogue typewriter effect: `CHAR_DELAY` — tunable at `DialogueBox.java:11`
    - Dialogue font: tunable at `DialogueBox.java:17`
    - Line spacing: `DialogueBox.java:75`, padding: `DialogueBox.java:13`

- Ending system based on playstyle (Aggressive / Neutral / Passive)
    - Final dialogue differs depending on kill/spare ratio across all levels.

- Persistent local leaderboard (`leaderboard.txt`)
    - Scores sorted by username; points contributed by kills, spares, coins, and levels cleared.

- Inventory and point system (WIP)
    - Affects overworld tile interaction, movement speed, and battle strategy.

- Conversation / dialogue system within battles (WIP)
    - Favorable dialogues lower enemy hostility and damage.
    - Unfavorable dialogues raise hostility significantly.
    - Neutral dialogues have no effect.

### Known Issues (as of last session)
- Camera movement inconsistent.
- Battle music track not wired to a dedicated audio file (uses level track).
- Battle enemy not yet spawned in overworld; contact-based enemy triggers damage instead of battle state switch.

---

## [galib] — April 2026
> See branch directly: https://github.com/hel-labs/MP-platformer/tree/galib

- Overworld and battle logic confirmed working independently.
- State switch between overworld and battle needs wiring.
- Enemy class minor tweaks pending.

---

## [luco] — April 2026
> See branch directly: https://github.com/hel-labs/MP-platformer/tree/luco

- Branch contents to be documented after review/merge.

---

## Notes

- Window starts fullscreen by default (`Game.java:18`). Set to `false` to test windowed scaling.
- Big Text glyph mapping: files `1–26` = A–Z, `27–35` = 1–9, `36` = 0.
- To review any branch's exact changes vs main, use GitHub compare:
  `https://github.com/hel-labs/MP-platformer/compare/main...<branch-name>`