import java.util.ArrayList;
import java.util.List;

public class Ship {
    static final double DEFAULT_SHIP_SPEED = 3;
    static final double DEFAULT_SHIP_MISSILE_SPEED = -6;
    static final int SHIP_WIDTH = Dimensions.SHIP_WIDTH;
    static final int SHIP_HEIGHT = Dimensions.SHIP_HEIGHT;
    static final int MISSILE_WIDTH = Dimensions.SHIP_MISSILE_WIDTH;
    static final int MISSILE_HEIGHT = Dimensions.SHIP_MISSILE_HEIGHT;
    static final int scene_width = Dimensions.SCENE_WIDTH;
    static final int scene_height = Dimensions.SCENE_HEIGHT;

    public double x_position;              // leftmost pixel position of the ship
    public double y_position;              // top most pixel position of the ship
    static double ship_speed;
    static double ship_missile_speed;
    public List<Missile> missiles = new ArrayList<>();

    Ship(int level) {
        ship_speed = DEFAULT_SHIP_SPEED + (level - 1);
        ship_missile_speed = DEFAULT_SHIP_MISSILE_SPEED - 2 * (level - 1);
        resetPosition();
    }

    // Sets the position of the ship to the bottom and middle of the screen
    void resetPosition() {
        x_position = scene_width/2.0 - SHIP_WIDTH/2.0;
        y_position = scene_height-SHIP_HEIGHT-Dimensions.SHIP_Y_PADDING;
    }

    // Moves ship left
    void moveLeft() {
        if (x_position - ship_speed >= Dimensions.X_PADDING) {
            x_position -= ship_speed;
        }
        else {          // Sets position to boundary in the case that ship goes out of bounds
            x_position = Dimensions.X_PADDING;
        }
    }

    // Moves ship left
    void moveRight() {
        if (x_position + SHIP_WIDTH + ship_speed <= scene_width - Dimensions.X_PADDING) {
            x_position += ship_speed;
        }
        else {          // Sets position to boundary in the case that ship goes out of bounds
            x_position = scene_width - SHIP_WIDTH - Dimensions.X_PADDING;
        }
    }

    // Creates a missile from ship's position and returns the Missile object
    Missile shootMissile() {
        Missile missile = new Missile(x_position + SHIP_WIDTH/2.0 - MISSILE_WIDTH/2.0,
                y_position - MISSILE_HEIGHT,
                ship_missile_speed, MissileTypes.Types.SHIP);
        missiles.add(missile);
        return missile;
    }

    // Moves all missiles shot by the ship
    void moveMissiles() {
        for (Missile missile : missiles) {
            missile.move();
        }
    }

    // Returns true if missile collides with ship
    boolean checkMissileCollisions(Missile missile) {
        return missile.checkCollision(x_position, y_position, SHIP_WIDTH, SHIP_HEIGHT);
    }

    // Ships and missiles move faster after a level up
    void level_up(int level) {
        ship_speed = DEFAULT_SHIP_SPEED + (level - 1);
        ship_missile_speed = DEFAULT_SHIP_MISSILE_SPEED - 2 * (level - 1);
    }
}
