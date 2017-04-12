package cinnamontoastcrunch.froggergame;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Random;

import static android.graphics.Color.WHITE;
import static android.graphics.Color.YELLOW;
import static android.graphics.Paint.Align.CENTER;

/**
 * Created by Jpollock on 3/1/2017.
 */

//TODO: Make abilities. Make score system. Make death sequence.

    // Screen Height = 1216
    // Screen Width = 800

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {

    private MainThread thread;

    private Player player;

    private Powerup rpg1;
    private Point rpg1Point;
    private Point rpg1BulletPoint;
    private Rect bullet;
    private Point bulletPoint;
    private Rect explosion;
    private Point explosionPoint;
    private int explosionTimer;

    private String currentAbility;

    private Rect playspace = new Rect(0, 0, 800, 750);
    private Rect collisionPreventer = new Rect(-500, 0, getScreenWidth()+500, 10);
    private Paint scorePaint = new Paint();
    private Paint scoreTitlePaint = new Paint();

    private MovementButton upButton; // Movement buttons.
    private MovementButton downButton;
    private MovementButton leftButton;
    private MovementButton rightButton;
    private MovementButton actionButton;

    private Vehicle car1; // Vehicles
    private Vehicle car2;
    private Vehicle car3;
    private Vehicle car4;
    private Vehicle car5;
    private Vehicle car6;

    private Point playerPoint;

    private Point pointChecker = new Point(0, 0);

    private Point carChecker = new Point(0, 0);

    private Point upPoint;
    private Point downPoint;
    private Point leftPoint;
    private Point rightPoint;
    private Point actionPoint;

    private Point car1Point;
    private Point car2Point;
    private Point car3Point;
    private Point car4Point;
    private Point car5Point;
    private Point car6Point;

    private int carSpeed1 = 6;
    private int carSpeed2 = 10;
    private int carSpeed3 = 15;

    private int score = 0;

    Resources res = getResources();
    private Drawable movementImage = res.getDrawable(R.drawable.directions);
    private Drawable actionImage = res.getDrawable(R.drawable.action);
    private Drawable backgroundImage = res.getDrawable(R.drawable.background);
    private Drawable froggerUp = res.getDrawable(R.drawable.froggerup);
    private Drawable froggerLeft = res.getDrawable(R.drawable.froggerleft);
    private Drawable froggerDown = res.getDrawable(R.drawable.froggerdown);
    private Drawable froggerRight = res.getDrawable(R.drawable.froggerright);
    private Drawable car1Image = res.getDrawable(R.drawable.car1);
    private Drawable car2Image = res.getDrawable(R.drawable.car3);
    private Drawable car3Image = res.getDrawable(R.drawable.car2);
    private Drawable car4Image = res.getDrawable(R.drawable.car4);
    private Drawable car5Image = res.getDrawable(R.drawable.car6);
    private Drawable car6Image = res.getDrawable(R.drawable.car5);
    private Drawable pointsImage = res.getDrawable(R.drawable.points);
    private Drawable rpgUp = res.getDrawable(R.drawable.rpgup);
    private Drawable rpgDown = res.getDrawable(R.drawable.rpgdown);
    private Drawable rpgLeft = res.getDrawable(R.drawable.rpgleft);
    private Drawable rpgRight = res.getDrawable(R.drawable.rpgright);
    private Drawable rpgBulletImage = res.getDrawable(R.drawable.bullet);
    private Drawable bulletImage = res.getDrawable(R.drawable.bullet);
    private Drawable explosionImage = res.getDrawable(R.drawable.explosion);
    public Drawable playerShownSprite = froggerUp;
    public Drawable rpgShownSprite = rpgUp;

    private boolean moving = false;
    private boolean shooting = false;
    private boolean goodToGo = false;

    private String bulletDirection = "Up";

    private String abilityPresent = "none";

    public static int MOVE_SPEED = 10; // This determines the number of steps before reaching the end of the screen.
    public static int X_SPEED = ((750/MOVE_SPEED)); // We scale the speed with screen size so bigger screens wont have more space.
    public static int Y_SPEED = ((750/MOVE_SPEED));

    public String playerSprite = "up";

    MediaPlayer randomCroak = MediaPlayer.create(getContext(), R.raw.croak);
    MediaPlayer hop = MediaPlayer.create(getContext(), R.raw.jump);

    Random rand = new Random();
    int random;
    int rpgTicker;

    // private int randomY = ((int)(Math.random() * getScreenHeight())); // Gets random position for car spawning.

    private void croak() {  // This plays a random croaking sound from 3 different sound files.

        randomCroak.release();

        random = (int)(Math.random() * 3 + 1);
        switch (random) {
            case 1: randomCroak = MediaPlayer.create(getContext(), R.raw.croak);
                    randomCroak.start();
                    break;
            case 2: randomCroak = MediaPlayer.create(getContext(), R.raw.ribbit);
                    randomCroak.start();
                    break;
            default: randomCroak = MediaPlayer.create(getContext(), R.raw.ribbit2);
                    randomCroak.start();
                    break;
        }
    }

    private void hopSound() {  // This plays a hopping sound.

        if (hop != null) {
            hop.release();
        }

        hop = MediaPlayer.create(getContext(), R.raw.jump);
        hop.start();

    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }


    public GamePanel(Context context) {
        super(context);

        getHolder().addCallback(this);

        thread = new MainThread(getHolder(), this);

        currentAbility = "none";

        player = new Player(new Rect(25, 25, 100, 100), Color.TRANSPARENT); // Starting size and color. Transparent is a color.
        playerPoint = new Point(62, 37); // Starting position.
        pointChecker = new Point(62, 37);

        rpg1 = new Powerup(new Rect(0, 0, 30, 30), Color.TRANSPARENT, "Launcher"); // Creates a rocket launcher powerup. It is of the launcher powerup type.
        rpg1Point = new Point((int) (Math.random() * 725) + 25, (int) (Math.random() * 675) + 25);
        bullet = new Rect(0, 0, 25, 25);
        bulletPoint = new Point(-10, -10);
        explosion = new Rect(0, 0, 40, 40);
        explosionPoint = new Point(-100, -100);

        upButton = new MovementButton(new Rect(100, 100, 200, 200), Color.TRANSPARENT);
        upPoint = new Point((175), (getScreenHeight()-275)); // This allows the button to always be located in the same spot, regardless of screen size.

        downButton = new MovementButton(new Rect(100, 100, 200, 200), Color.TRANSPARENT);
        downPoint = new Point((175), (getScreenHeight()-75));

        leftButton = new MovementButton(new Rect(100, 100, 200, 200), Color.TRANSPARENT);
        leftPoint = new Point((75), (getScreenHeight()-175));

        rightButton = new MovementButton(new Rect(100, 100, 200, 200), Color.TRANSPARENT);
        rightPoint = new Point((275), (getScreenHeight()-175));

        actionButton = new MovementButton(new Rect(100, 100, 300, 300), Color.TRANSPARENT);
        actionPoint = new Point((getScreenWidth()-175), getScreenHeight()-175);

        car1 = new Vehicle(new Rect(200, 200, 275, 275), Color.TRANSPARENT);
        car1Point = new Point(-100, ((int)(-1)*75)-36); // Starting position.
        car1.direction = "right";

        car2 = new Vehicle(new Rect(200, 200, 350, 275), Color.TRANSPARENT);
        car2Point = new Point(-100, ((int)(-1)*75)-36); // Starting position.
        car2.direction = "right";

        car3 = new Vehicle(new Rect(200, 200, 275, 275), Color.TRANSPARENT);
        car3Point = new Point(-100, ((int)(-1)*75)-36); // Starting position.
        car3.direction = "right";

        car4 = new Vehicle(new Rect(200, 200, 275, 275), Color.TRANSPARENT);
        car4Point = new Point(getScreenWidth() + 100, ((int)(-1)*75)-36); // Starting position.
        car4.direction = "left";

        car5 = new Vehicle(new Rect(200, 200, 350, 275), Color.TRANSPARENT);
        car5Point = new Point(getScreenWidth() + 100, ((int)(-1)*75)-36); // Starting position.
        car5.direction = "left";

        car6 = new Vehicle(new Rect(200, 200, 275, 275), Color.TRANSPARENT);
        car6Point = new Point(getScreenWidth() + 100, ((int)(-1)*75)-36); // Starting position.
        car6.direction = "left";

        backgroundImage.setBounds(0, 0, getScreenWidth(), getScreenHeight());

        movementImage.setBounds((leftPoint.x)-50, (upPoint.y)-50, (rightPoint.x)+50, (downPoint.y)+50);
        actionImage.setBounds((actionPoint.x)-100, (actionPoint.y)-100, (actionPoint.x)+100, (actionPoint.y)+100);

        froggerUp.setBounds((playerPoint.x)-37, (playerPoint.y)-37, (playerPoint.x)+37, (playerPoint.y)+37);
        froggerDown.setBounds((playerPoint.x)-37, (playerPoint.y)-37, (playerPoint.x)+37, (playerPoint.y)+37);
        froggerLeft.setBounds((playerPoint.x)-37, (playerPoint.y)-37, (playerPoint.x)+37, (playerPoint.y)+37);
        froggerRight.setBounds((playerPoint.x)-37, (playerPoint.y)-37, (playerPoint.x)+37, (playerPoint.y)+37);

        rpgBulletImage.setBounds(rpg1.getRectangle());
        rpgUp.setBounds((playerPoint.x)-37, (playerPoint.y)-37, (playerPoint.x)+37, (playerPoint.y)+37);
        rpgDown.setBounds((playerPoint.x)-37, (playerPoint.y)-37, (playerPoint.x)+37, (playerPoint.y)+37);
        rpgLeft.setBounds((playerPoint.x)-37, (playerPoint.y)-37, (playerPoint.x)+37, (playerPoint.y)+37);
        rpgRight.setBounds((playerPoint.x)-37, (playerPoint.y)-37, (playerPoint.x)+37, (playerPoint.y)+37);

        car1Image.setBounds((car1Point.x)-37, (car1Point.y)-37, (car1Point.x)+37, (car1Point.y)+37);
        car2Image.setBounds((car1Point.x)-75, (car1Point.y)-75, (car1Point.x)+75, (car1Point.y)+75); // Truck
        car3Image.setBounds((car1Point.x)-37, (car1Point.y)-37, (car1Point.x)+37, (car1Point.y)+37);
        car4Image.setBounds((car1Point.x)-37, (car1Point.y)-37, (car1Point.x)+37, (car1Point.y)+37);
        car5Image.setBounds((car1Point.x)-75, (car1Point.y)-75, (car1Point.x)+75, (car1Point.y)+75); // Truck
        car6Image.setBounds((car1Point.x)-37, (car1Point.y)-37, (car1Point.x)+37, (car1Point.y)+37);

        scorePaint.setColor(YELLOW);
        scorePaint.setTextSize(30);
        scorePaint.setTextAlign(CENTER);

        scoreTitlePaint.setColor(WHITE);
        scoreTitlePaint.setTextSize(35);
        scoreTitlePaint.setTextAlign(CENTER);

        setFocusable(true);

    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread = new MainThread(getHolder(), this);

        thread.setRunning(true); // Sets the game to running.
        thread.start();
    }


    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while(true) {
            try {
                thread.setRunning(false); // Stops the game.
                thread.join();
            } catch(Exception e) {e.printStackTrace();}
            retry = false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) { // This method controls the interactivity with the movement buttons.
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: // If the player presses down on the screen.
            // case MotionEvent.ACTION_MOVE: // If the player moves finger across screen.
            //  playerPoint.set((int)event.getX(), (int)event.getY()); // Sets the player's point to the position of the event. In this case, the position the player touched the screen.

                //if (hop.isPlaying()) {
                //hop.release();
                //}

                if (moving == false) {

                    if (upButton.button.contains((int) event.getX(), (int) event.getY())) { // Checks if the player touched the button.
                        moving = true;
                        pointChecker = playerPoint;
                        pointChecker.offset(0, -(Y_SPEED));
                        if (playspace.contains(pointChecker.x, pointChecker.y)) {
                            // moving = true;
                            playerPoint = pointChecker; // Moves the player object.
                            //hop = MediaPlayer.create(getContext(), R.raw.hop);
                            //hop.start();
                            playerSprite = "Up";
                            playerShownSprite = froggerUp;
                        }
                        else {
                            pointChecker.offset(0, (Y_SPEED));
                        }
                    }

                    if (downButton.button.contains((int) event.getX(), (int) event.getY())) {
                        moving = true;
                        pointChecker = playerPoint;
                        pointChecker.offset(0, Y_SPEED);
                        if (playspace.contains(pointChecker.x, pointChecker.y)) {
                            // moving = true;
                            playerPoint = pointChecker;
                            //hop = MediaPlayer.create(getContext(), R.raw.hop);
                            //    hop.start();
                            playerSprite = "Down";
                            playerShownSprite = froggerDown;
                        }
                        else {
                            pointChecker.offset(0, -(Y_SPEED));
                        }
                    }

                    if (leftButton.button.contains((int) event.getX(), (int) event.getY())) {
                        moving = true;
                        pointChecker = playerPoint;
                        pointChecker.offset(-(X_SPEED), 0);
                        if (playspace.contains(pointChecker.x, pointChecker.y)) {
                            // moving = true;
                            playerPoint = pointChecker;
                            //hop = MediaPlayer.create(getContext(), R.raw.hop);
                            //hop.start();
                            playerSprite = "Left";
                            playerShownSprite = froggerLeft;
                        }
                        else {
                            pointChecker.offset((X_SPEED), 0);
                        }
                    }

                    if (rightButton.button.contains((int) event.getX(), (int) event.getY())) {
                        moving = true;
                        pointChecker = playerPoint;
                        pointChecker.offset(X_SPEED, 0);
                        if (playspace.contains(pointChecker.x, pointChecker.y)) {
                            // moving = true;
                            playerPoint = pointChecker;
                            //hop = MediaPlayer.create(getContext(), R.raw.hop);
                            //hop.start();
                            playerSprite = "Right";
                            playerShownSprite = froggerRight;
                        }
                        else {
                            pointChecker.offset(-(X_SPEED), 0);
                        }
                    }



                } // end moving

                moving = false;

                if (actionButton.button.contains((int) event.getX(), (int) event.getY())) {

                    croak();

                    if (currentAbility == "Launcher") {

                        if (playerShownSprite == froggerUp) {
                            shootRpg("Up");
                        }

                        if (playerShownSprite == froggerDown) {
                            shootRpg("Down");
                        }

                        if (playerShownSprite == froggerLeft) {
                            shootRpg("Left");
                        }

                        if (playerShownSprite == froggerRight) {
                            shootRpg("Right");
                        }

                    }


                }

        } // end touch event

        return true;
        //return super.onTouchEvent(event);
    }

    public void shootRpg(String direction) { // Controls the shooting of rpg bullets.

        if (shooting == false) {


            bulletPoint.set((playerPoint.x)-12, (playerPoint.y)-12);
            shooting = true;


                if (direction == "Up") {
                    bulletDirection = "Up";
                }

                if (direction == "Down") {
                    bulletDirection = "Down";
                }

                if (direction == "Left") {
                    bulletDirection = "Left";
                }

                if (direction == "Right") {
                    bulletDirection = "Right";
                }

            currentAbility = "none";
            abilityPresent = "none";

        }
    }

    public void update() {

        upButton.update(upPoint);
        downButton.update(downPoint);
        leftButton.update(leftPoint);
        rightButton.update(rightPoint);
        actionButton.update(actionPoint);
        actionButton.update(actionPoint);
        car1.update(car1Point);
        car2.update(car2Point);
        car3.update(car3Point);
        car4.update(car4Point);
        car5.update(car5Point);
        car6.update(car6Point);

        player.update(playerPoint);
        playerShownSprite.setBounds(player.getRectangle());

        rpg1.update(rpg1Point);

        car1Image.setBounds(car1.getRectangle());
        car2Image.setBounds(car2.getRectangle());
        car3Image.setBounds(car3.getRectangle());
        car4Image.setBounds(car4.getRectangle());
        car5Image.setBounds(car5.getRectangle());
        car6Image.setBounds(car6.getRectangle());


        score += 1; // Increases score by one every tick.
        explosionTimer +=1;



        if (shooting == true) { // If a bullet is currently being shot, move it in the correct direction.

            if (bulletDirection == "Up") {
                bulletPoint.offset(0, -25);
            }
            if (bulletDirection == "Down") {
                bulletPoint.offset(0, 25);
            }
            if (bulletDirection == "Left") {
                bulletPoint.offset(-25, 0);
            }
            if (bulletDirection == "Right") {
                bulletPoint.offset(25, 0);
            }
            bulletImage.setBounds(bullet);
        }

        if (abilityPresent == "none") {    // Controls the spawning of powerups on the play area.

            rpgTicker = (int) (Math.random() * 100);

            if (rpgTicker == 1) {

                rpg1Point.set((int) (Math.random() * 725) + 25, (int) (Math.random() * 675) + 25);
                rpgBulletImage.setBounds(rpg1.getRectangle());
                abilityPresent = "rpg";

            }
        }

        if (abilityPresent == "rpg") {
            rpg1.update();

        }

        bullet.offsetTo(bulletPoint.x, bulletPoint.y);

        rpgUp.setBounds(player.getRectangle());
        rpgDown.setBounds(player.getRectangle());
        rpgLeft.setBounds(player.getRectangle());
        rpgRight.setBounds(player.getRectangle());


        if (car1.playerCollide(player)) {
            gameOver();
        }
        if (car2.playerCollide(player)) {
            gameOver();
        }
        if (car3.playerCollide(player)) {
            gameOver();
        }
        if (car4.playerCollide(player)) {
            gameOver();
        }
        if (car5.playerCollide(player)) {
            gameOver();
        }
        if (car6.playerCollide(player)) {
            gameOver();
        }

        if (car1.getRectangle().contains(bullet)) {
            explosionPoint = car1Point;
            explosion.set((car1.getRectangle().left)-20, (car1.getRectangle().top)-20, (car1.getRectangle().right)+20, (car1.getRectangle().bottom)+20);
            explosionImage.setBounds(explosion);
            bulletPoint.offset(1000, 1000);
            car1Point.offset(1000, 0);
            explosionTimer = 0;
            score += 300;
            shooting = false;
        }

        if (car2.getRectangle().contains(bullet)) {
            explosionPoint = car2Point;
            explosion.set((car2.getRectangle().left)-20, (car2.getRectangle().top)-20, (car2.getRectangle().right)+20, (car2.getRectangle().bottom)+20);
            explosionImage.setBounds(explosion);
            bulletPoint.offset(1000, 1000);
            car2Point.offset(1000, 0);
            explosionTimer = 0;
            score += 300;
            shooting = false;
        }

        if (car3.getRectangle().contains(bullet)) {
            explosionPoint = car3Point;
            explosion.set((car3.getRectangle().left)-20, (car3.getRectangle().top)-20, (car3.getRectangle().right)+20, (car3.getRectangle().bottom)+20);
            explosionImage.setBounds(explosion);
            bulletPoint.offset(1000, 1000);
            car3Point.offset(1000, 0);
            explosionTimer = 0;
            score += 300;
            shooting = false;
        }

        if (car4.getRectangle().contains(bullet)) {
            explosionPoint = car4Point;
            explosion.set((car4.getRectangle().left)-20, (car4.getRectangle().top)-20, (car4.getRectangle().right)+20, (car4.getRectangle().bottom)+20);
            explosionImage.setBounds(explosion);
            bulletPoint.offset(1000, 1000);
            car4Point.offset(1000, 0);
            explosionTimer = 0;
            score += 300;
            shooting = false;
        }

        if (car5.getRectangle().contains(bullet)) {
            explosionPoint = car5Point;
            explosion.set((car5.getRectangle().left)-20, (car5.getRectangle().top)-20, (car5.getRectangle().right)+20, (car5.getRectangle().bottom)+20);
            explosionImage.setBounds(explosion);
            bulletPoint.offset(1000, 1000);
            car5Point.offset(1000, 0);
            explosionTimer = 0;
            score += 300;
            shooting = false;
        }

        if (car6.getRectangle().contains(bullet)) {
            explosionPoint = car6Point;
            explosion.set((car6.getRectangle().left)-20, (car6.getRectangle().top)-20, (car6.getRectangle().right)+20, (car6.getRectangle().bottom)+20);
            explosionImage.setBounds(explosion);
            bulletPoint.offset(1000, 1000);
            car6Point.offset(1000, 0);
            explosionTimer = 0;
            score += 300;
            shooting = false;
        }

        if (player.getRectangle().contains(rpg1Point.x, rpg1Point.y)) {
            croak();
            currentAbility = rpg1.type;
            rpg1Point.offset(1000,1000);
            shooting = false;
        }
    }

    int randomCarSpeed(int min, int max) {
        int range = (max - min) + 1;
        return (int)(Math.random() * range) + min;
    }

    public void moveCars() {

        if (car1.direction == "left")
        {
            car1Point.offset(-carSpeed1, 0);
        }
        else
        {
            car1Point.offset(carSpeed1, 0);
        }

        if (car2.direction == "left")
        {
            car2Point.offset(-carSpeed2, 0);
        }
        else
        {
            car2Point.offset(carSpeed2, 0);
        }

        if (car3.direction == "left")
        {
            car3Point.offset(-carSpeed3, 0);
        }
        else
        {
            car3Point.offset(carSpeed3, 0);
        }

        if (car4.direction == "left")
        {
            car4Point.offset(-carSpeed1, 0);
        }
        else
        {
            car4Point.offset(carSpeed1, 0);
        }

        if (car5.direction == "left")
        {
            car5Point.offset(-carSpeed2, 0);
        }
        else
        {
            car5Point.offset(carSpeed2, 0);
        }

        if (car6.direction == "left")
        {
            car6Point.offset(-carSpeed3, 0);
        }
        else
        {
            car6Point.offset(carSpeed3, 0);
        }


        if (car1Point.x > getScreenWidth()+100 || car1Point.x < -100) {
            goodToGo = false;
            while (goodToGo == false){
                carChecker.set(-500, ((int) ((Math.random() * 10) + 1) * 75) - 37);
                collisionPreventer.set(-500, (carChecker.y)-10, (getScreenWidth())+500, (carChecker.y)+10);
                if (collisionPreventer.contains(car1Point.x, car1Point.y) || collisionPreventer.contains(car2Point.x, car2Point.y) ||
                        collisionPreventer.contains(car3Point.x, car3Point.y) || collisionPreventer.contains(car4Point.x, car4Point.y) ||
                        collisionPreventer.contains(car5Point.x, car5Point.y) || collisionPreventer.contains(car6Point.x, car6Point.y)) {
                    goodToGo = false;
                } else {
                    goodToGo = true;
                }
            }
            car1Point.set(-100, carChecker.y);
        }

        if (car2Point.x > getScreenWidth()+100 || car2Point.x < -100) {
            goodToGo = false;
            while (goodToGo == false){
                carChecker.set(-500, ((int) ((Math.random() * 10) + 1) * 75) - 37);
                collisionPreventer.offsetTo(carChecker.x, carChecker.y);
                if (collisionPreventer.contains(car1Point.x, car1Point.y) || collisionPreventer.contains(car2Point.x, car2Point.y) ||
                        collisionPreventer.contains(car3Point.x, car3Point.y) || collisionPreventer.contains(car4Point.x, car4Point.y) ||
                        collisionPreventer.contains(car5Point.x, car5Point.y) || collisionPreventer.contains(car6Point.x, car6Point.y)) {
                    goodToGo = false;
                } else {
                    goodToGo = true;
                }
            }
            car2Point.set(-100, carChecker.y);
        }

        if (car3Point.x > getScreenWidth()+100 || car3Point.x < -100) {
            goodToGo = false;
            while (goodToGo == false){
                carChecker.set(-500, ((int) ((Math.random() * 10) + 1) * 75) - 37);
                collisionPreventer.offsetTo(carChecker.x, carChecker.y);
                if (collisionPreventer.contains(car1Point.x, car1Point.y) || collisionPreventer.contains(car2Point.x, car2Point.y) ||
                        collisionPreventer.contains(car3Point.x, car3Point.y) || collisionPreventer.contains(car4Point.x, car4Point.y) ||
                        collisionPreventer.contains(car5Point.x, car5Point.y) || collisionPreventer.contains(car6Point.x, car6Point.y)) {
                    goodToGo = false;
                } else {
                    goodToGo = true;
                }
            }
            car3Point.set(-100, carChecker.y);
        }

        if (car4Point.x > getScreenWidth()+100 || car4Point.x < -100) {
            goodToGo = false;
            while (goodToGo == false){
                carChecker.set(-500, ((int) ((Math.random() * 10) + 1) * 75) - 37);
                collisionPreventer.offsetTo(carChecker.x, carChecker.y);
                if (collisionPreventer.contains(car1Point.x, car1Point.y) || collisionPreventer.contains(car2Point.x, car2Point.y) ||
                        collisionPreventer.contains(car3Point.x, car3Point.y) || collisionPreventer.contains(car4Point.x, car4Point.y) ||
                        collisionPreventer.contains(car5Point.x, car5Point.y) || collisionPreventer.contains(car6Point.x, car6Point.y)) {
                    goodToGo = false;
                } else {
                    goodToGo = true;
                }
            }
            car4Point.set((getScreenWidth())+100, carChecker.y);
        }

        if (car5Point.x > getScreenWidth()+100 || car5Point.x < -100) {
            goodToGo = false;
            while (goodToGo == false){
                carChecker.set(-500, ((int) ((Math.random() * 10) + 1) * 75) - 37);
                collisionPreventer.offsetTo(carChecker.x, carChecker.y);
                if (collisionPreventer.contains(car1Point.x, car1Point.y) || collisionPreventer.contains(car2Point.x, car2Point.y) ||
                        collisionPreventer.contains(car3Point.x, car3Point.y) || collisionPreventer.contains(car4Point.x, car4Point.y) ||
                        collisionPreventer.contains(car5Point.x, car5Point.y) || collisionPreventer.contains(car6Point.x, car6Point.y)) {
                    goodToGo = false;
                } else {
                    goodToGo = true;
                }
            }
            car5Point.set((getScreenWidth())+100, carChecker.y);
        }

        if (car6Point.x > getScreenWidth()+100 || car6Point.x < -100) {
            goodToGo = false;
            while (goodToGo == false){
                carChecker.set(-500, ((int) ((Math.random() * 10) + 1) * 75) - 37);
                collisionPreventer.offsetTo(carChecker.x, carChecker.y);
                if (collisionPreventer.contains(car1Point.x, car1Point.y) || collisionPreventer.contains(car2Point.x, car2Point.y) ||
                        collisionPreventer.contains(car3Point.x, car3Point.y) || collisionPreventer.contains(car4Point.x, car4Point.y) ||
                        collisionPreventer.contains(car5Point.x, car5Point.y) || collisionPreventer.contains(car6Point.x, car6Point.y)) {
                    goodToGo = false;
                } else {
                    goodToGo = true;
                }
            }
            car6Point.set((getScreenWidth())+100, carChecker.y);
        }

    }

    public void gameOver() {
        System.out.println("SQUISH!");
        croak();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        canvas.drawColor(Color.LTGRAY); // Sets the background color. Instead of using "Color.rgb", we can simply use a color.
        backgroundImage.draw(canvas);



        player.draw(canvas); // Draw the player to the screen.
        playerShownSprite.setBounds(player.getRectangle());
        playerShownSprite.draw(canvas);

        if (shooting == true) {
            bulletImage.draw(canvas);
        }


        if (currentAbility == "Launcher") {

            if (playerShownSprite == froggerUp) {
                rpgUp.draw(canvas);
            }
            else if (playerShownSprite == froggerDown) {
                rpgDown.draw(canvas);
            }
            else if (playerShownSprite == froggerLeft) {
                rpgLeft.draw(canvas);
            }
            else if (playerShownSprite == froggerRight) {
                rpgRight.draw(canvas);
            }

        }



        car1.draw(canvas);
        car2.draw(canvas);
        car3.draw(canvas);
        car4.draw(canvas);
        car5.draw(canvas);
        car6.draw(canvas);
        car1Image.draw(canvas);
        car2Image.draw(canvas);
        car3Image.draw(canvas);
        car4Image.draw(canvas);
        car5Image.draw(canvas);
        car6Image.draw(canvas);

        if (explosionTimer <=30) {
            explosionImage.draw(canvas);
        }

        if (abilityPresent == "rpg") {
            rpg1.draw(canvas);
            rpgBulletImage.setBounds(rpg1.getRectangle());
            rpgBulletImage.draw(canvas);
        }

        upButton.draw(canvas); // Draw the up button to the screen.
        downButton.draw(canvas); // Draw the down button to the screen.
        leftButton.draw(canvas); // Draw the left button to the screen.
        rightButton.draw(canvas); // Draw the right button to the screen.
        actionButton.draw(canvas); // Draw the action button to the screen.

        movementImage.draw(canvas);
        actionImage.draw(canvas);

        canvas.drawText("S C O R E:", (getScreenWidth()/2), 810, scoreTitlePaint);
        canvas.drawText(Integer.toString(score), (getScreenWidth()/2), 845, scorePaint);

        //System.out.println(getScreenHeight());
        //System.out.println(getScreenWidth());
    }


}
