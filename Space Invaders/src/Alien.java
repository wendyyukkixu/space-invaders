import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Alien {
    static final double ALIEN_DEFAULT_SPEED = 0.5;
    static final double ALIEN_DEFAULT_MISSILE_SPEED = 2;
    static final int ALIEN_DEFAULT_MISSILE_CHANCE = 5000;
    static double alien_speed;
    static double alien_missile_speed;
    static Directions direction = Directions.RIGHT;
    static double down_distance_travelled = 0.0;                            // distance that group of aliens travelled after direction change to down
    static Directions last_horizontal_direction = Directions.RIGHT;         // direction of aliens before going down
    public static List<Missile> missiles = new ArrayList<>();       // List containing all alien missiles
    static Random rand = new Random();                              // To determine if alien will shoot a missile
    static int missile_chance;

    int alien_width;
    int alien_height;
    public double x_position;               // leftmost pixel position of the alien
    public double y_position;               // top most pixel position of the alien
    public int points;
    int missile_width;
    int missile_height;
    MissileTypes.Types missile_type;

    enum Directions {
        LEFT, RIGHT, DOWN;
    }

    Alien(double x_position, double y_position, AlienTypes.Types type, int level){

        // Aliens and missiles move faster after a level up
        alien_speed = ALIEN_DEFAULT_SPEED + 0.5 * (level - 1);
        alien_missile_speed = ALIEN_DEFAULT_MISSILE_SPEED + level;
        missile_chance = ALIEN_DEFAULT_MISSILE_CHANCE - (level - 1) * 2000;

        switch(type) {
            case ALIEN1:
                alien_width = Dimensions.ALIEN1_WIDTH;
                alien_height = Dimensions.ALIEN1_HEIGHT;
                missile_type = MissileTypes.Types.ALIEN1;
                points = 10;
                missile_width = Dimensions.ALIEN1_MISSILE_WIDTH;
                missile_height = Dimensions.ALIEN1_MISSILE_HEIGHT;
                break;
            case ALIEN2:
                alien_width = Dimensions.ALIEN2_WIDTH;
                alien_height = Dimensions.ALIEN2_HEIGHT;
                missile_type = MissileTypes.Types.ALIEN2;
                points = 20;
                missile_width = Dimensions.ALIEN2_MISSILE_WIDTH;
                missile_height = Dimensions.ALIEN2_MISSILE_HEIGHT;
                break;
            case ALIEN3:
                alien_width = Dimensions.ALIEN3_WIDTH;
                alien_height = Dimensions.ALIEN3_HEIGHT;
                points = 30;
                missile_type = MissileTypes.Types.ALIEN3;
                missile_width = Dimensions.ALIEN3_MISSILE_WIDTH;
                missile_height = Dimensions.ALIEN3_MISSILE_HEIGHT;
                break;
        }
        double centered_offset = (Dimensions.ALIEN_HORIZONTAL_SPACE - alien_width)/2.0;
        this.x_position = x_position + centered_offset;
        this.y_position = y_position;
    }

    // Changes the direction the group of aliens will move in
    static void changeDirections() {
        if (direction == Directions.RIGHT || direction == Directions.LEFT) {
            last_horizontal_direction = direction;
            direction = Directions.DOWN;
        }
        else {
            Alien.down_distance_travelled = 0;
            if (last_horizontal_direction == Directions.RIGHT) {
                direction = Directions.LEFT;
            }
            else {
                direction = Directions.RIGHT;
            }
        }
    }

    // After all aliens in the game have moved down, update how much distance the group has moved down
    static void updateDownDistance() {
        if (direction == Directions.DOWN) {
            down_distance_travelled += alien_speed;
        }
    }

    // When one alien is destroyed, all remaining aliens speed up and have higher chance of shooting missiles
    // to make up for destroyed aliens
    static void speed_up() {
        alien_speed += 0.03;
        missile_chance -= 15;
    }

    // Moves the alien once by speed
    void move() {
        if (direction == Directions.LEFT) {
            x_position -= alien_speed;
        }
        else if (direction == Directions.RIGHT) {
            x_position += alien_speed;
        }
        else {
            y_position += alien_speed;
        }
    }

    // Determines if the alien should fire a missile or not
    boolean willShootMissile() {
        int rand_int = rand.nextInt(missile_chance);
        if (rand_int == 0) {
            return true;
        }
        return false;
    }

    // Creates a missile from alien's position and returns the Missile object
    Missile shootMissile() {
        Missile missile = new Missile(x_position + alien_width/2.0 - missile_width/2.0,
                y_position + alien_height,
                alien_missile_speed, missile_type);
        missiles.add(missile);
        return missile;
    }

    // Moves all missiles shot by aliens
    static void moveMissiles() {
        for (Missile missile : missiles) {
            missile.move();
        }
    }

    // Returns true if missile collides with alien
    boolean checkMissileCollisions(Missile missile) {
        return missile.checkCollision(x_position, y_position, alien_width, alien_height);
    }

    // Resets static variables of the Alien class
    static void reset() {
        missiles.clear();
        last_horizontal_direction = Directions.RIGHT;
        direction = Directions.RIGHT;
        down_distance_travelled = 0.0;
    }
}
