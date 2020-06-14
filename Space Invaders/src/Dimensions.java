// Ship, alien, and missile images are all scaled to half their original sizes
// All width/height variables of images are also halved from original dimensions

public class Dimensions {
    // Window dimensions
    public static final int SCENE_WIDTH = 1280;
    public static final int SCENE_HEIGHT = 780;
    public static final int ALIEN_ROWS = 5;
    public static final int ALIEN_COLUMNS = 10;
    public static final int X_PADDING = 10;

    // Title screen
    public static final int TITLE_WIDTH = 491;
    public static final int TITLE_HEIGHT = 211;
    public static final int TITLE_Y_PADDING = 50;

    // Font positions
    public static final double TEXT_Y = SCENE_HEIGHT * 0.04;
    public static final double SCORE_X = SCENE_WIDTH * 0.05;
    public static final double LIVES_X = SCENE_WIDTH * 0.7;
    public static final double LEVEL_X = SCENE_WIDTH * 0.85;
    public static final double LINE_Y = SCENE_HEIGHT * 0.06;

    // End game positions
    public static final double END_GAME_RECT_X = SCENE_WIDTH/4.0;
    public static final double END_GAME_RECT_Y = SCENE_HEIGHT/4.0;
    public static final Double [] END_GAME_RECT_COORDS = new Double[]{
            0.0, 0.0,
            0.0, SCENE_HEIGHT/2.0,
            SCENE_WIDTH/2.0, SCENE_HEIGHT/2.0,
            SCENE_WIDTH/2.0, 0.0
    };

    // Ship dimensions
    public static final int SHIP_WIDTH = 62;
    public static final int SHIP_HEIGHT = 38;
    public static final int SHIP_Y_PADDING = 10;

    // Alien dimensions
    public static final int ALIEN1_WIDTH = 60;
    public static final int ALIEN1_HEIGHT = 40;
    public static final int ALIEN2_WIDTH = 54;
    public static final int ALIEN2_HEIGHT = 41;
    public static final int ALIEN3_WIDTH = 43;
    public static final int ALIEN3_HEIGHT = 41;

    public static final int ALIEN_Y_PADDING = 65;
    public static final int ALIEN_HORIZONTAL_SPACE = 80;
    public static final int ALIEN_VERTICAL_SPACE = 60;

    // Missile dimensions
    public static final int SHIP_MISSILE_WIDTH = 7;
    public static final int SHIP_MISSILE_HEIGHT = 22;

    public static final int ALIEN1_MISSILE_WIDTH = 14;
    public static final int ALIEN1_MISSILE_HEIGHT = 28;
    public static final int ALIEN2_MISSILE_WIDTH = 15;
    public static final int ALIEN2_MISSILE_HEIGHT = 28;
    public static final int ALIEN3_MISSILE_WIDTH = 13;
    public static final int ALIEN3_MISSILE_HEIGHT = 25;
}
