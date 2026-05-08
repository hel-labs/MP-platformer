# MP-Platformer Feature Reference

This document lists the full feature set implemented across the entire codebase (core, overworld, battle, UI, and utilities).

## Core Engine
- Game loop with fixed update + render timing, FPS/UPS tracking, and Swing rendering.
- Resolution scaling and letterboxing via `GamePanel` to preserve aspect ratio.
- Fullscreen toggle at runtime (F11) using `GameWindow`.
- Central state machine (`Gamestate`) with dedicated screens and transitions.
- Central input handler supporting key hold/press/release with consistent polling per tick.

## Game States & Screens
- Title screen with big-text glyph rendering and start prompt.
- Player name entry screen with on-screen prompt and persistent storage.
- Main menu with Play/Options/Credits/Leaderboard/Quit buttons and visual overlays.
- Options screen with audio sliders and mute toggles.
- Leaderboard screen showing persistent top scores with time-played and last-played date.
- Credits screen with scrolling credits image and animated character showcase.

## Overworld Gameplay
- Side-scrolling, tile-based levels built from color-coded level images (RGB encodes tiles, enemies, objects, and spawn).
- Player movement, jumping, gravity, and collision with walls/floor.
- Player combat in overworld with attack boxes for breakables and hit reactions.
- Health and power bars in the overworld HUD.
- Enemy AI for Crabby, Pinkstar, and Shark with states (idle, run, attack, hit, dead).
- Battle trigger on enemy contact, with battle snapshot bridging overworld stats.
- Level progression and completion tracking, with level-complete and game-complete overlays.
- Environmental effects: rain particle system and dialogue pop-ups (question/exclamation).
- Hazard and object system: spikes, cannons, cannonballs, potions, barrels/boxes, background trees, and grass.
- Animated water tiles and background layers (clouds, ship animation).
- Score/points tracking during runs.

## Battle System
- Turn-based battle flow with phases (encounter dialogue → player turn → enemy turn → terminal outcomes).
- Battle actions: Fight, Talk, Spare, and Flee.
- Hostility system with mercy threshold and hostility-based damage bonus.
- Talk options that increase or reduce hostility; calm effects scale with repeated talks.
- Dice-based damage strategies for players and enemies (standard, high variance, escalating, weak steady).
- Battle UI with dialogue box, action menu, hostility meter, and player/enemy status bars.
- Battle animations driven by sprite sheets and animation controllers.
- Battle outcomes: win, flee, or lose, with outcome data fed back to overworld.

## Audio & Options
- Background music for menu/levels/battle and sound effects for actions.
- Sound and music mute toggles.
- Master volume slider with live updates.

## Persistence & Profiles
- Player profile name saved to disk and reused on next launch.
- Persistent leaderboard with best score, total duration, and last played timestamp.

## Utilities & Diagnostics
- Custom exception types for battle/data errors.
- Central logging utility for info/warn/error messages.
- Screen shake utility (available for impact effects).

## Assets & Resources
- Sprite atlases for player/enemies/objects, big-text glyphs, UI buttons, and effects.
- Resource loaders for images and levels, plus audio files under `res/audio`.
