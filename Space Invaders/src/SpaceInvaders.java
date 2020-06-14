import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Line;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class SpaceInvaders extends Application {
    final int scene_width = Dimensions.SCENE_WIDTH;
    final int scene_height = Dimensions.SCENE_HEIGHT;
    final int alien_rows = Dimensions.ALIEN_ROWS;
    final int alien_columns = Dimensions.ALIEN_COLUMNS;

    // Title screen image
    public static Image logo_image = new Image("images/logo.png", 491, 211, true, true);

    // Ship image
    public static Image ship_image = new Image("images/player.png", 124, 75, true, true);

    // Alien images
    public static Image alien1_image = new Image("images/enemy1.png", 119, 81, true, true);
    public static Image alien2_image = new Image("images/enemy2.png", 108, 81, true, true);
    public static Image alien3_image = new Image("images/enemy3.png", 86, 82, true, true);

    // Missile images
    public static Image ship_missile_image = new Image("images/player_bullet.png", 13, 44, true, true);
    public static Image alien1_missile_image = new Image("images/bullet1.png", 28, 55, true, true);
    public static Image alien2_missile_image = new Image("images/bullet2.png", 29, 56, true, true);
    public static Image alien3_missile_image = new Image("images/bullet3.png", 26, 50, true, true);

    // Game sounds
    public String ship_destroyed_sound = getClass().getClassLoader().getResource("sounds/explosion.wav").toString();
    public String alien_1_sound = getClass().getClassLoader().getResource("sounds/fastinvader1.wav").toString();
    public String alien_2_sound = getClass().getClassLoader().getResource("sounds/fastinvader2.wav").toString();
    public String alien_3_sound = getClass().getClassLoader().getResource("sounds/fastinvader3.wav").toString();
    public String alien_4_sound = getClass().getClassLoader().getResource("sounds/fastinvader4.wav").toString();
    public String alien_destroyed_sound = getClass().getClassLoader().getResource("sounds/invaderkilled.wav").toString();
    public String missile_sound = getClass().getClassLoader().getResource("sounds/shoot.wav").toString();
    AudioClip ship_destroyed_clip = new AudioClip(ship_destroyed_sound);
    AudioClip [] alien_audios = new AudioClip[]{
            new AudioClip(alien_1_sound),
            new AudioClip(alien_2_sound),
            new AudioClip(alien_3_sound),
            new AudioClip(alien_4_sound)
    };
    AudioClip alien_destroyed_audio = new AudioClip(alien_destroyed_sound);
    AudioClip missile_audio = new AudioClip(missile_sound);
    int current_alien_audio_index = 0;
    long alien_audio_intervals;             // Time in nanoseconds between alien sounds
    long alien_audio_timer = 0;

    // Main game variables
    Pane game_pane;
    Pane title_screen_pane;
    AnimationTimer timer;
    public int score = 0;
    public int lives = 3;
    public int level = 1;
    long missile_timer = 0;
    public Ship ship;
    public List<Alien> aliens = new ArrayList<>();
    public List<KeyCode> pressed_key_codes = new ArrayList<>();
    boolean game_over = false;

    // Displayed texts
    Text instructions_text;
    Text controls_text;
    Text assignment_text;
    Text score_text;
    Text lives_text;
    Text level_text;
    Text end_game_text;
    Text end_game_info_text;

    // Image views
    ImageView ship_image_view;
    ImageView title_image_view;
    public List<ImageView> alien_image_views = new ArrayList<>();
    public List<ImageView> ship_missile_image_views = new ArrayList<>();
    public List<ImageView> alien_missile_image_views = new ArrayList<>();
    public Polygon end_game_polygon = new Polygon();

    @Override
    public void start(Stage stage) {

        title_screen_pane = new Pane();
        Scene title_screen_scene = new Scene(title_screen_pane, scene_width, scene_height, Color.WHITE);
        game_pane = new Pane();
        Scene game_scene = new Scene(game_pane, scene_width, scene_height, Color.BLACK);

        // Timer ticks every time we want to advance a frame
        // An AnimationTimer runs at 60 FPS
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                handle_animation(now);
            }
        };

        title_screen_setup();

        // Event handlers for title_screen_scene
        title_screen_scene.addEventHandler(KeyEvent.KEY_RELEASED, (key) -> {
            if (key.getCode() == KeyCode.DIGIT1 || key.getCode() == KeyCode.ENTER) {
                level = 1;
            }
            if (key.getCode() == KeyCode.DIGIT2) {
                level = 2;
            }
            else if (key.getCode() == KeyCode.DIGIT3) {
                level = 3;
            }
            if (key.getCode() == KeyCode.DIGIT1 || key.getCode() == KeyCode.DIGIT2
                    || key.getCode() == KeyCode.DIGIT3 || key.getCode() == KeyCode.ENTER ) {
                game_setup();
                stage.setScene(game_scene);
            }
            else if (key.getCode() == KeyCode.Q) {
                System.exit(0);
            }
        });

        // Event handlers for game_scene d
        game_scene.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
            if ((key.getCode() == KeyCode.A || key.getCode() == KeyCode.LEFT)
                    && !pressed_key_codes.contains(KeyCode.A)) {
                pressed_key_codes.add(KeyCode.A);
            }
            else if ((key.getCode() == KeyCode.D || key.getCode() == KeyCode.RIGHT)
                    && !pressed_key_codes.contains(KeyCode.D)) {
                pressed_key_codes.add(KeyCode.D);
            }
            if (key.getCode() == KeyCode.SPACE && !pressed_key_codes.contains(KeyCode.SPACE)) {
                pressed_key_codes.add(KeyCode.SPACE);
            }
        });
        game_scene.addEventHandler(KeyEvent.KEY_RELEASED, (key) -> {
            if (key.getCode() == KeyCode.A || key.getCode() == KeyCode.LEFT) {
                pressed_key_codes.remove(KeyCode.A);
            }
            else if (key.getCode() == KeyCode.D || key.getCode() == KeyCode.RIGHT) {
                pressed_key_codes.remove(KeyCode.D);
            }
            else if (key.getCode() == KeyCode.SPACE) {
                pressed_key_codes.remove(KeyCode.SPACE);
            }
            if (game_over) {
                if (key.getCode() == KeyCode.Q) {
                    System.exit(0);
                }
                if (key.getCode() == KeyCode.DIGIT1 || key.getCode() == KeyCode.ENTER) {
                    level = 1;
                }
                else if (key.getCode() == KeyCode.DIGIT2) {
                    level = 2;
                }
                else if (key.getCode() == KeyCode.DIGIT3) {
                    level = 3;
                }
                if (key.getCode() == KeyCode.DIGIT1 || key.getCode() == KeyCode.DIGIT2
                        || key.getCode() == KeyCode.DIGIT3 || key.getCode() == KeyCode.ENTER ) {
                    reset_game();
                    game_setup();
                    stage.setScene(game_scene);
                }
                else if (key.getCode() == KeyCode.I) {
                    reset_game();
                    stage.setScene(title_screen_scene);
                }
            }
        });

        // Attach the scene to the stage and show it
        stage.setScene(title_screen_scene);
        stage.setTitle("Space Invaders");
        stage.setResizable(false);
        stage.show();
    }

    // Set up title screen
    void title_screen_setup() {
        // Logo setup
        title_image_view = new ImageView(logo_image);
        title_image_view.setX(scene_width/2.0 - Dimensions.TITLE_WIDTH/2.0);
        title_image_view.setY(Dimensions.TITLE_Y_PADDING);
        title_screen_pane.getChildren().add(title_image_view);

        // Text setup
        instructions_text = new Text("Instructions");
        instructions_text.setFont(Font.font ("Verdana", FontWeight.BOLD, 40));
        instructions_text.setTranslateX(scene_width/2.0 - instructions_text.getLayoutBounds().getWidth()/2);
        instructions_text.setTranslateY(title_image_view.getTranslateY() + 2 * Dimensions.TITLE_HEIGHT);
        title_screen_pane.getChildren().add(instructions_text);

        controls_text = new Text("ENTER - Start Game\n" +
                "A or ◄, D or ► - Move ship left or right\n" +
                "SPACE - Fire!\n" +
                "Q - Quit Game\n" +
                "1 or 2 or 3 - Start Game at a specific level");
        controls_text.setTextAlignment(TextAlignment.CENTER);
        controls_text.setFont(Font.font ("Arial", 25));
        controls_text.setTranslateX(scene_width/2.0 - controls_text.getLayoutBounds().getWidth()/2);
        controls_text.setTranslateY(instructions_text.getTranslateY() + controls_text.getLayoutBounds().getHeight()/2);
        title_screen_pane.getChildren().add(controls_text);

        assignment_text = new Text("Implemented by Wendy Xu (20631406) for CS 349, University of Waterloo, W20");
        assignment_text.setFont(Font.font ("Arial", 13));
        assignment_text.setTranslateX(scene_width/2.0 - assignment_text.getLayoutBounds().getWidth()/2);
        assignment_text.setTranslateY(scene_height- assignment_text.getLayoutBounds().getHeight());
        title_screen_pane.getChildren().add(assignment_text);
    }

    // Set up the game
    void game_setup(){
        game_text_setup();
        ship_setup();
        alien_setup();
        timer.start(); // this timer will be stopped automatically by JavaFX when the program terminates
    }

    // Set up game texts
    void game_text_setup() {
        // Game screen
        score_text = new Text("Score: " + score);
        score_text.setFont(Font.font ("Arial", 20));
        score_text.setFill(Color.WHITE);
        score_text.setTranslateX(Dimensions.SCORE_X);
        score_text.setTranslateY(Dimensions.TEXT_Y);
        game_pane.getChildren().add(score_text);

        lives_text = new Text("Lives: " + lives);
        lives_text.setFont(Font.font ("Arial", 20));
        lives_text.setFill(Color.WHITE);
        lives_text.setTranslateX(Dimensions.LIVES_X);
        lives_text.setTranslateY(Dimensions.TEXT_Y);
        game_pane.getChildren().add(lives_text);

        level_text = new Text("Level: " + level);
        level_text.setFont(Font.font ("Arial", 20));
        level_text.setFill(Color.WHITE);
        level_text.setTranslateX(Dimensions.LEVEL_X);
        level_text.setTranslateY(Dimensions.TEXT_Y);
        game_pane.getChildren().add(level_text);

        Line line = new Line(0.0, Dimensions.LINE_Y,
                scene_width, Dimensions.LINE_Y);
        line.setStroke(Color.WHITE);
        game_pane.getChildren().add(line);

        // End game
        end_game_polygon.getPoints().addAll(Dimensions.END_GAME_RECT_COORDS);
        end_game_polygon.setTranslateX(Dimensions.END_GAME_RECT_X);
        end_game_polygon.setTranslateY(Dimensions.END_GAME_RECT_Y);
        end_game_polygon.setStroke(Color.WHITE);
        end_game_polygon.setFill(Color.BLACK);

        end_game_text = new Text("");           // Later set as You win! or Game over!
        end_game_text.setFont(Font.font ("Verdana", 50));

        end_game_info_text = new Text("");      // Later set as score + restart/quit text
        end_game_info_text.setFont(Font.font ("Arial", 30));
        end_game_info_text.setFill(Color.WHITE);
        end_game_info_text.setTextAlignment(TextAlignment.CENTER);
    }

    // Set up player ship
    void ship_setup() {
        ship = new Ship(level);
        ship_image_view = new ImageView(ship_image);
        ship_image_view.setX(ship.x_position);
        ship_image_view.setY(ship.y_position);
        ship_image_view.setFitWidth(Ship.SHIP_WIDTH);
        ship_image_view.setFitHeight(Ship.SHIP_HEIGHT);
        game_pane.getChildren().add(ship_image_view);
    }

    // Set up enemies
    void alien_setup() {
        Alien.direction = Alien.Directions.RIGHT;
        for (int i = 0; i < alien_rows; i ++) {
            for (int j = 0; j < alien_columns; j ++) {
                Alien alien;
                ImageView alien_image_view;
                double x_position = Dimensions.X_PADDING + j*Dimensions.ALIEN_HORIZONTAL_SPACE;
                double y_position = Dimensions.ALIEN_Y_PADDING + i*Dimensions.ALIEN_VERTICAL_SPACE;

                // Alien type 3
                if (i == 0) {
                    alien = new Alien(x_position, y_position, AlienTypes.Types.ALIEN3, level);
                    alien_image_view = new ImageView(alien3_image);
                    alien_image_view.setFitWidth(Dimensions.ALIEN3_WIDTH);
                    alien_image_view.setFitHeight(Dimensions.ALIEN3_HEIGHT);
                }
                // Alien type 2
                else if (i < 3) {
                    alien = new Alien(x_position, y_position, AlienTypes.Types.ALIEN2, level);
                    alien_image_view = new ImageView(alien2_image);
                    alien_image_view.setFitWidth(Dimensions.ALIEN2_WIDTH);
                    alien_image_view.setFitHeight(Dimensions.ALIEN2_HEIGHT);
                }
                // Alien type 1
                else {
                    alien = new Alien(x_position, y_position, AlienTypes.Types.ALIEN1, level);
                    alien_image_view = new ImageView(alien1_image);
                    alien_image_view.setFitWidth(Dimensions.ALIEN1_WIDTH);
                    alien_image_view.setFitHeight(Dimensions.ALIEN1_HEIGHT);
                }
                aliens.add(alien);
                alien_image_view.setX(alien.x_position);
                alien_image_view.setY(alien.y_position);
                alien_image_views.add(alien_image_view);
                game_pane.getChildren().add(alien_image_view);
            }
        }
        alien_audio_intervals = (long) (500000000 - (100000000 * Alien.alien_speed));
    }

    void handle_animation(long now) {
        play_alien_sounds(now);
        move_ship();
        move_aliens();
        shoot_ship_missiles(now);
        shoot_alien_missiles();
        move_missiles();
        check_missile_collisions();
        delete_missiles();
        check_game_state();
    }

    // Plays alien sounds
    void play_alien_sounds(long now) {
        if (now - alien_audio_timer > alien_audio_intervals) {
            alien_audios[current_alien_audio_index].play();
            current_alien_audio_index++;
            if (current_alien_audio_index == alien_audios.length) {
                current_alien_audio_index = 0;
            }
            alien_audio_timer = now;
        }
    }

    // Moves ship left or right depending on keys pressed
    void move_ship() {
        if (pressed_key_codes.contains(KeyCode.A)) {
            ship.moveLeft();
            ship_image_view.setX(ship.x_position);
        }
        if (pressed_key_codes.contains(KeyCode.D)) {
            ship.moveRight();
            ship_image_view.setX(ship.x_position);
        }
    }

    // Moves aliens
    void move_aliens() {
        if (aliens.isEmpty()) {
            return;
        }
        check_alien_direction();

        // Move all aliens
        for (int i = 0; i < aliens.size(); i ++) {
            aliens.get(i).move();
            alien_image_views.get(i).setX(aliens.get(i).x_position);
            alien_image_views.get(i).setY(aliens.get(i).y_position);
        }
        Alien.updateDownDistance();         // Updates distance moving down since last horizontal move if needed
    }

    // Changes alien moving direction if needed
    void check_alien_direction() {
        // Aliens are moving right, check if aliens will move off screen
        if (Alien.direction == Alien.Directions.RIGHT) {
            double max_x = rightmost_alien_x();
            // Alien1 has largest width, so we use that one to calculate if all aliens will fit
            if (max_x + Dimensions.ALIEN1_WIDTH + Dimensions.X_PADDING > scene_width){
                Alien.changeDirections();
            }
        }

        // Aliens are moving left, check if aliens will move off screen
        else if (Alien.direction == Alien.Directions.LEFT) {
            double min_x = leftmost_alien_x();
            if (min_x < Dimensions.X_PADDING){
                Alien.changeDirections();
            }
        }

        // Aliens are moving down, check if they finished moving down
        else if (Alien.direction == Alien.Directions.DOWN &&
                Alien.down_distance_travelled >= Dimensions.ALIEN_VERTICAL_SPACE){
            random_alien_fires();           // After aliens all descend one row, one of the ships fires at the player
            Alien.changeDirections();
        }
    }

    // Determine rightmost x_position between all aliens
    double rightmost_alien_x() {
        double max_x = 0;
        for (Alien alien : aliens) {
            if (alien.x_position > max_x) {
                max_x = alien.x_position;
            }
        }
        return max_x;
    }

    // Determine leftmost x_position between all aliens
    double leftmost_alien_x() {
        double min_x = scene_width;
        for (Alien alien : aliens) {
            if (alien.x_position < min_x) {
                min_x = alien.x_position;
            }
        }
        return min_x;
    }

    // One of the aliens fires at the player
    void random_alien_fires() {
        Random rand = new Random();
        int alien_index = rand.nextInt(aliens.size());
        alien_shoot_missile(aliens.get(alien_index));
    }

    // Parameter alien fires a missile
    void alien_shoot_missile(Alien alien) {
        Missile missile = alien.shootMissile();
        ImageView alien_missile_image_view;
        switch(alien.missile_type) {
            case ALIEN1:
                alien_missile_image_view = new ImageView(alien1_missile_image);
                break;
            case ALIEN2:
                alien_missile_image_view = new ImageView(alien2_missile_image);
                break;
            default:
                alien_missile_image_view = new ImageView(alien3_missile_image);
                break;
        }
        alien_missile_image_view.setX(missile.x_position);
        alien_missile_image_view.setY(missile.y_position);
        alien_missile_image_view.setFitWidth(alien.missile_width);
        alien_missile_image_view.setFitHeight(alien.missile_height);
        alien_missile_image_views.add(alien_missile_image_view);
        game_pane.getChildren().add(alien_missile_image_view);
    }

    // Shoots ship missiles depending on keys pressed
    void shoot_ship_missiles(long now) {
        if (pressed_key_codes.contains(KeyCode.SPACE)) {
            // Rate of fire cannot exceed 2 missiles per second
            if (now - missile_timer > 500000000) {
                missile_audio.play();
                Missile missile = ship.shootMissile();
                ImageView ship_missile_image_view = new ImageView(ship_missile_image);
                ship_missile_image_view.setX(missile.x_position);
                ship_missile_image_view.setY(missile.y_position);
                ship_missile_image_view.setFitWidth(Dimensions.SHIP_MISSILE_WIDTH);
                ship_missile_image_view.setFitHeight(Dimensions.SHIP_MISSILE_HEIGHT);
                ship_missile_image_views.add(ship_missile_image_view);
                game_pane.getChildren().add(ship_missile_image_view);
                missile_timer = now;
            }
        }
    }

    // Shoots alien missiles
    void shoot_alien_missiles() {
        for (Alien alien : aliens) {
            boolean will_shoot = alien.willShootMissile();          // random chance that alien will shoot
            if (will_shoot) {
                missile_audio.play();
                alien_shoot_missile(alien);
            }
        }
    }

    // Moves both ship and alien missiles across the screen
    void move_missiles() {
        // Ship missiles
        ship.moveMissiles();
        for (int i = 0; i < ship_missile_image_views.size(); i++) {
            ship_missile_image_views.get(i).setY(ship.missiles.get(i).y_position);
        }

        // Enemy missiles
        Alien.moveMissiles();
        for (int i = 0; i < alien_missile_image_views.size(); i++) {
            alien_missile_image_views.get(i).setY(Alien.missiles.get(i).y_position);
        }
    }

    void check_missile_collisions() {
        // Check if ship missiles hit any aliens
        for (int i = ship.missiles.size()-1; i >= 0; i --) {
            Missile missile = ship.missiles.get(i);
            for (int j = aliens.size()-1; j >= 0; j --) {
                Alien alien = aliens.get(j);
                boolean hit = alien.checkMissileCollisions(missile);
                if (hit) {
                    alien_destroyed_audio.play();

                    // Move the missile off-screen to be deleted by delete_missiles()
                    missile.moveOffScreen();

                    // Update the score, speed up remaining aliens, remove the alien
                    update_score(alien.points);
                    Alien.speed_up();
                    alien_audio_intervals = (long) (500000000 - (100000000 * Alien.alien_speed));
                    ImageView alien_image_view = alien_image_views.get(j);
                    game_pane.getChildren().remove(alien_image_view);
                    alien_image_views.remove(j);
                    aliens.remove(alien);
                    break;
                }
            }
        }

        // Check if any alien missiles hit the ship
        for (int i = Alien.missiles.size()-1; i >= 0; i --) {
            Missile missile = Alien.missiles.get(i);
            // Only check missiles that have reached the y-coordinate of the player
            if (missile.y_position+missile.missile_height >= ship.y_position) {
                boolean hit = ship.checkMissileCollisions(missile);
                if (hit) {
                    ship_destroyed_clip.play();

                    // Player loses a life
                    lives -= 1;
                    lives_text.setText("Lives: " + lives);

                    // Ship and missiles not respawn/removed for last life so that player can see how they died
                    if (lives > 0){
                        // Move the missile off-screen, deleted by delete_missiles()
                        missile.moveOffScreen();
                        ship_respawn();
                    }
                    break;
                }
            }
        }
    }

    // Updates current score
    void update_score(int points){
        score += points;
        score_text.setText("Score: " + score);
    }

    // Respawn the player ship
    void ship_respawn() {
        ship.resetPosition();
        ship_image_view.setX(ship.x_position);          // Update the ship image position
    }

    // Deletes any off-screen missiles
    void delete_missiles() {
        // Player missiles
        for (int i = ship.missiles.size()-1; i >= 0; i --) {
            Missile missile = ship.missiles.get(i);
            if (missile.y_position <= Dimensions.LINE_Y) {
                ImageView ship_missile_image_view = ship_missile_image_views.get(i);
                game_pane.getChildren().remove(ship_missile_image_view);
                ship_missile_image_views.remove(i);
                ship.missiles.remove(missile);
            }
        }

        // Enemy missiles
        for (int i = Alien.missiles.size()-1; i >= 0; i --) {
            Missile missile = Alien.missiles.get(i);
            if (missile.y_position > scene_height) {
                ImageView alien_missile_image_view = alien_missile_image_views.get(i);
                game_pane.getChildren().remove(alien_missile_image_view);
                alien_missile_image_views.remove(i);
                Alien.missiles.remove(missile);
            }
        }
    }

    // Checks for level up, game over and victory
    void check_game_state() {
        // Game over
        if (lives <= 0){                // No lives left
            end_game();
        }
        for (Alien alien : aliens){     // Aliens reached bottom of screen
            if (alien.y_position + alien.alien_height >= ship.y_position) {
                end_game();
                break;
            }
        }

        // Empty board
        if (aliens.isEmpty()) {         // All aliens defeated
            if (level < 3) {            // Level up
                level_up();
                alien_setup();
            }
            else {                      // Victory
                end_game();
            }
        }
    }

    void level_up(){
        level += 1;
        level_text.setText("Level: " + level);
        ship.level_up(level);

        clear_missiles();
    }

    // Clears both ship and alien missiles
    void clear_missiles() {
        // Remove ship missiles
        ship.missiles.clear();
        for (ImageView ship_missile_image_view : ship_missile_image_views) {
            game_pane.getChildren().remove(ship_missile_image_view);
        }
        ship_missile_image_views.clear();

        // Remove alien missiles
        Alien.missiles.clear();
        for (ImageView alien_missile_image_view : alien_missile_image_views) {
            game_pane.getChildren().remove(alien_missile_image_view);
        }
        alien_missile_image_views.clear();
    }

    // Game is over (both victory and defeat cases)
    void end_game() {
        game_over = true;
        timer.stop();

        // Display end game box
        game_pane.getChildren().add(end_game_polygon);

        if (lives > 0 && level == 3 && aliens.isEmpty()) {      // Victory
            end_game_text.setText("You won!");
            end_game_text.setFill(Color.GREEN);
        }
        else {                                                  // Game over
            end_game_text.setText("Game over!");
            end_game_text.setFill(Color.RED);
        }

        end_game_text.setTranslateX(scene_width/2.0 - end_game_text.getLayoutBounds().getWidth()/2);
        end_game_text.setTranslateY(Dimensions.END_GAME_RECT_Y + 100);

        end_game_info_text.setText("Final score: " + score + "\n\n" +
                "ENTER - Start New Game\n" +
                "I - Back to Instructions\n" +
                "Q - Quit Game\n" +
                "1 or 2 or 3 - Start New Game at a specific level");
        end_game_info_text.setTranslateX(scene_width/2.0 - end_game_info_text.getLayoutBounds().getWidth()/2);
        end_game_info_text.setTranslateY(end_game_polygon.getTranslateY() + end_game_polygon.getLayoutBounds().getHeight()
                - end_game_info_text.getLayoutBounds().getHeight());

        game_pane.getChildren().add(end_game_text);
        game_pane.getChildren().add(end_game_info_text);
    }

    // Resets the game
    void reset_game() {
        // Clear end game pop-up
        game_pane.getChildren().remove(end_game_polygon);
        game_pane.getChildren().remove(end_game_text);
        game_pane.getChildren().remove(end_game_info_text);

        // Clear old aliens
        aliens.clear();
        Alien.reset();

        // Reset game images and variables before setting up the game again
        clear_image_views();
        score = 0;
        lives = 3;
        current_alien_audio_index = 0;
        missile_timer = 0;
        alien_audio_timer = 0;
        game_over = false;
    }

    // Clear image views
    void clear_image_views(){
        // Remove ship image and text
        game_pane.getChildren().remove(ship_image_view);
        game_pane.getChildren().remove(score_text);
        game_pane.getChildren().remove(lives_text);
        game_pane.getChildren().remove(level_text);

        clear_missiles();
        for (ImageView alien_image_view : alien_image_views) {
            game_pane.getChildren().remove(alien_image_view);
        }
        alien_image_views.clear();
    }
}