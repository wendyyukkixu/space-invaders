public class Missile {
    int missile_width;
    int missile_height;
    public double x_position;
    public double y_position;
    public double speed;

    Missile(double x_position, double y_position, double speed, MissileTypes.Types type) {
        switch(type) {
            case SHIP:
                missile_width = Dimensions.SHIP_MISSILE_WIDTH;
                missile_height = Dimensions.SHIP_MISSILE_HEIGHT;
                break;
            case ALIEN1:
                missile_width = Dimensions.ALIEN1_MISSILE_WIDTH;
                missile_height = Dimensions.ALIEN1_MISSILE_HEIGHT;
                break;
            case ALIEN2:
                missile_width = Dimensions.ALIEN2_MISSILE_WIDTH;
                missile_height = Dimensions.ALIEN2_MISSILE_HEIGHT;
                break;
            case ALIEN3:
                missile_width = Dimensions.ALIEN3_MISSILE_WIDTH;
                missile_height = Dimensions.ALIEN3_MISSILE_HEIGHT;
                break;
        }
        this.x_position = x_position;
        this.y_position = y_position;
        this.speed = speed;
    }

    // Moves the missile once by speed
    void move() {
        y_position += speed;
    }

    // Moves the missile off screen
    void moveOffScreen() {
        if (speed < 0) {
            y_position = Dimensions.LINE_Y - missile_height;
        }
        else {
            y_position = Dimensions.SCENE_HEIGHT;
        }
    }

    // Returns true if dimensions collide with missile
    boolean checkCollision(double x, double y, double w, double h) {
        // If one rectangle is beside the other
        if (x_position > x+w || x > x_position+missile_width) {
            return false;
        }

        // If one rectangle is above the other
        else if (y_position > y+h || y > y_position+missile_height) {
            return false;
        }
        return true;
    }
}
