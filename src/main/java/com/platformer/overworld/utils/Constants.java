package com.platformer.overworld.utils;

import com.platformer.core.Game;

public class Constants {

    public static final int ANI_SPEED = 25;
    public static final float GRAVITY = 0.04f * Game.SCALE;

    public static class Directions {
        public static final int LEFT = 0;
        public static final int UP = 1;
        public static final int RIGHT = 2;
        public static final int DOWN = 3;
    }

    public static class PlayerConstants {
        public static final int IDLE = 0;
        public static final int RUNNING = 1;
        public static final int JUMP = 2;
        public static final int FALLING = 3;
        public static final int HIT = 4;
        public static final int ATTACK = 5;

        public static int GetSpriteAmount(int playerAction) {
            return switch (playerAction) {
                case RUNNING -> 6;
                case IDLE -> 5;
                case JUMP, ATTACK -> 3;
                case HIT -> 4;
                case FALLING -> 1;
                default -> 1;
            };
        }
    }

    public static class EnemyConstants {
        public static final int CRABBY = 0;
        public static final int PINKSTAR = 1;
        public static final int SHARK = 2;

        public static final int IDLE = 0;
        public static final int RUNNING = 1;
        public static final int ATTACK = 2;
        public static final int HIT = 3;
        public static final int DEAD = 4;

        public static final int CRABBY_WIDTH_DEFAULT = 72;
        public static final int CRABBY_HEIGHT_DEFAULT = 32;
        public static final int CRABBY_WIDTH = (int) (CRABBY_WIDTH_DEFAULT * Game.SCALE);
        public static final int CRABBY_HEIGHT = (int) (CRABBY_HEIGHT_DEFAULT * Game.SCALE);
        public static final int CRABBY_DRAWOFFSET_X = (int) (26 * Game.SCALE);
        public static final int CRABBY_DRAWOFFSET_Y = (int) (9 * Game.SCALE);

        public static final int PINKSTAR_WIDTH_DEFAULT = 34;
        public static final int PINKSTAR_HEIGHT_DEFAULT = 30;
        public static final int PINKSTAR_WIDTH = (int) (PINKSTAR_WIDTH_DEFAULT * Game.SCALE);
        public static final int PINKSTAR_HEIGHT = (int) (PINKSTAR_HEIGHT_DEFAULT * Game.SCALE);
        public static final int PINKSTAR_DRAWOFFSET_X = (int) (9 * Game.SCALE);
        public static final int PINKSTAR_DRAWOFFSET_Y = (int) (7 * Game.SCALE);

        public static final int SHARK_WIDTH_DEFAULT = 34;
        public static final int SHARK_HEIGHT_DEFAULT = 30;
        public static final int SHARK_WIDTH = (int) (SHARK_WIDTH_DEFAULT * Game.SCALE);
        public static final int SHARK_HEIGHT = (int) (SHARK_HEIGHT_DEFAULT * Game.SCALE);
        public static final int SHARK_DRAWOFFSET_X = (int) (8 * Game.SCALE);
        public static final int SHARK_DRAWOFFSET_Y = (int) (6 * Game.SCALE);

        public static int GetSpriteAmount(int enemyType, int enemyState) {
            return switch (enemyType) {
                case CRABBY -> switch (enemyState) {
                    case IDLE -> 9;
                    case RUNNING -> 6;
                    case ATTACK -> 7;
                    case HIT -> 4;
                    case DEAD -> 5;
                    default -> 1;
                };
                case PINKSTAR -> switch (enemyState) {
                    case IDLE -> 8;
                    case RUNNING -> 6;
                    case ATTACK -> 8;
                    case HIT -> 4;
                    case DEAD -> 5;
                    default -> 1;
                };
                case SHARK -> switch (enemyState) {
                    case IDLE -> 8;
                    case RUNNING -> 6;
                    case ATTACK -> 7;
                    case HIT -> 4;
                    case DEAD -> 5;
                    default -> 1;
                };
                default -> 1;
            };
        }

        public static int GetMaxHealth(int enemyType) {
            return switch (enemyType) {
                case CRABBY -> 100;
                case PINKSTAR -> 80;
                case SHARK -> 120;
                default -> 100;
            };
        }

        public static int GetEnemyDmg(int enemyType) {
            return switch (enemyType) {
                case CRABBY -> 15;
                case PINKSTAR -> 10;
                case SHARK -> 20;
                default -> 10;
            };
        }
    }

    public static class ObjectConstants {
        public static final int RED_POTION = 0;
        public static final int BLUE_POTION = 1;
        public static final int BOX = 2;
        public static final int BARREL = 3;
        public static final int CANNON_LEFT = 4;
        public static final int CANNON_RIGHT = 5;
        public static final int SPIKE = 6;

        public static final int RED_POTION_VALUE = 15;
        public static final int BLUE_POTION_VALUE = 20;

        public static final int POTION_WIDTH = (int) (12 * Game.SCALE);
        public static final int POTION_HEIGHT = (int) (16 * Game.SCALE);

        public static final int CONTAINER_WIDTH = (int) (40 * Game.SCALE);
        public static final int CONTAINER_HEIGHT = (int) (30 * Game.SCALE);

        public static final int SPIKE_WIDTH = (int) (32 * Game.SCALE);
        public static final int SPIKE_HEIGHT = (int) (32 * Game.SCALE);

        public static final int CANNON_WIDTH = (int) (40 * Game.SCALE);
        public static final int CANNON_HEIGHT = (int) (26 * Game.SCALE);

        public static int GetSpriteAmount(int objType) {
            return switch (objType) {
                case RED_POTION, BLUE_POTION -> 7;
                case BOX, BARREL -> 8;
                case CANNON_LEFT, CANNON_RIGHT -> 7;
                default -> 1;
            };
        }

        public static int GetTreeWidth(int type) {
            return switch (type) {
                case 7, 8, 9, 10 -> (int) (39 * Game.SCALE);
                default -> (int) (62 * Game.SCALE);
            };
        }

        public static int GetTreeHeight(int type) {
            return switch (type) {
                case 7, 8, 9, 10 -> (int) (92 * Game.SCALE);
                default -> (int) (54 * Game.SCALE);
            };
        }

        public static int GetTreeOffsetX(int type) {
            return 0;
        }

        public static int GetTreeOffsetY(int type) {
            return 0;
        }
    }

    public static class Projectiles {
        public static final int CANNON_BALL_WIDTH = (int) (15 * Game.SCALE);
        public static final int CANNON_BALL_HEIGHT = (int) (15 * Game.SCALE);
        public static final float SPEED = 0.75f * Game.SCALE;
    }

    public static class Dialogue {
        public static final int EXCLAMATION = 0;
        public static final int QUESTION = 1;

        public static int GetSpriteAmount(int dialogueType) {
            return 5;
        }
    }

    public static class UI {
        public static class Buttons {
            public static final int B_WIDTH_DEFAULT = 140;
            public static final int B_HEIGHT_DEFAULT = 56;
            public static final int B_WIDTH = (int) (B_WIDTH_DEFAULT * Game.SCALE);
            public static final int B_HEIGHT = (int) (B_HEIGHT_DEFAULT * Game.SCALE);
        }

        public static class URMButtons {
            public static final int URM_DEFAULT_SIZE = 56;
            public static final int URM_SIZE = (int) (URM_DEFAULT_SIZE * Game.SCALE);
        }

        public static class PauseButtons {
            public static final int SOUND_SIZE_DEFAULT = 42;
            public static final int SOUND_SIZE = (int) (SOUND_SIZE_DEFAULT * Game.SCALE);
        }

        public static class VolumeButtons {
            public static final int VOLUME_DEFAULT_WIDTH = 28;
            public static final int VOLUME_DEFAULT_HEIGHT = 44;
            public static final int VOLUME_WIDTH = (int) (VOLUME_DEFAULT_WIDTH * Game.SCALE);
            public static final int VOLUME_HEIGHT = (int) (VOLUME_DEFAULT_HEIGHT * Game.SCALE);

            public static final int SLIDER_DEFAULT_WIDTH = 215;
            public static final int SLIDER_WIDTH = (int) (SLIDER_DEFAULT_WIDTH * Game.SCALE);
        }
    }
}
