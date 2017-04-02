package cinnamontoastcrunch.froggergame;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Random;

/**
 * Created by Jpollock on 3/1/2017.
 */

//TODO: Make the player & button size scale with screen size.

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    private MainThread thread;

    private Player player;

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

    Resources res = getResources();
    private Drawable movementImage = res.getDrawable(R.drawable.directions);
    private Drawable actionImage = res.getDrawable(R.drawable.action);

    public static int MOVE_SPEED = 10; // This determines the number of steps before reaching the end of the screen.
    public static int X_SPEED = ((getScreenWidth()/MOVE_SPEED)); // We scale the speed with screen size so bigger screens wont have more space.
    public static int Y_SPEED = ((getScreenWidth()/MOVE_SPEED));

    MediaPlayer randomCroak = MediaPlayer.create(getContext(), R.raw.croak);

    Random rand = new Random();
    int random;

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

        player = new Player(new Rect(100, 100, 175, 175), Color.rgb(0, 255, 0)); // Starting size and color. GREEN!
        playerPoint = new Point(150, 150); // Starting position.

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

        car1 = new Vehicle(new Rect(200, 200, 275, 275), Color.BLACK);
        car1Point = new Point(-100, ((int)(Math.random() * getScreenHeight()))); // Starting position.
        car1.direction = "right";

        car2 = new Vehicle(new Rect(200, 200, 275, 275), Color.MAGENTA);
        car2Point = new Point(-100, ((int)(Math.random() * getScreenHeight()))); // Starting position.
        car2.direction = "right";

        car3 = new Vehicle(new Rect(200, 200, 275, 275), Color.BLACK);
        car3Point = new Point(-100, ((int)(Math.random() * getScreenHeight()))); // Starting position.
        car3.direction = "right";

        car4 = new Vehicle(new Rect(200, 200, 275, 275), Color.BLACK);
        car4Point = new Point(getScreenWidth() + 100, ((int)(Math.random() * getScreenHeight()))); // Starting position.
        car4.direction = "left";

        car5 = new Vehicle(new Rect(200, 200, 275, 275), Color.MAGENTA);
        car5Point = new Point(getScreenWidth() + 100, ((int)(Math.random() * getScreenHeight()))); // Starting position.
        car5.direction = "left";

        car6 = new Vehicle(new Rect(200, 200, 275, 275), Color.BLACK);
        car6Point = new Point(getScreenWidth() + 100, ((int)(Math.random() * getScreenHeight()))); // Starting position.
        car6.direction = "left";

        movementImage.setBounds((leftPoint.x)-50, (upPoint.y)-50, (rightPoint.x)+50, (downPoint.y)+50);
        actionImage.setBounds((actionPoint.x)-100, (actionPoint.y)-100, (actionPoint.x)+100, (actionPoint.y)+100);

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

    /*
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: // If the player presses down on the screen.
            case MotionEvent.ACTION_MOVE:
                    playerPoint.set((int)event.getX(), (int)event.getY()); // Sets the player's point to the position of the event. In this case, the position the player touched the screen.
        }

        return true;
        //return super.onTouchEvent(event);
    }
    */

    @Override
    public boolean onTouchEvent(MotionEvent event) { // This method controls the interactivity with the movement buttons.
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: // If the player presses down on the screen.
            // case MotionEvent.ACTION_MOVE: // If the player moves finger across screen.
            //  playerPoint.set((int)event.getX(), (int)event.getY()); // Sets the player's point to the position of the event. In this case, the position the player touched the screen.

                 if (upButton.button.contains((int)event.getX(), (int)event.getY())) { // Checks if the player touched the button.
                     playerPoint.offset(0, -(Y_SPEED)); // Moves the player object.
                 }

                 if (downButton.button.contains((int)event.getX(), (int)event.getY())) {
                     playerPoint.offset(0, Y_SPEED);
                 }

                 if (leftButton.button.contains((int)event.getX(), (int)event.getY())) {
                     playerPoint.offset(-(X_SPEED), 0);
                 }

                 if (rightButton.button.contains((int)event.getX(), (int)event.getY())) {
                    playerPoint.offset(X_SPEED, 0);
                 }

                if (actionButton.button.contains((int)event.getX(), (int)event.getY())) {
                    // TODO: Add ability trigger here.
                    croak();
                }

        }

        return true;
        //return super.onTouchEvent(event);
    }

    public void update() {
        player.update(playerPoint);
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
    }

    int randomCarSpeed(int min, int max) {
        int range = (max - min) + 1;
        return (int)(Math.random() * range) + min;
    }

    public void moveCars() {

        System.out.println("Moving Cars");

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
            car1Point.set(-100, ((int)(Math.random() * getScreenHeight())));
        }

        if (car2Point.x > getScreenWidth()+100 || car2Point.x < -100) {
            car2Point.set(-100, ((int)(Math.random() * getScreenHeight())));
        }

        if (car3Point.x > getScreenWidth()+100 || car3Point.x < -100) {
            car3Point.set(-100, ((int)(Math.random() * getScreenHeight())));
        }

        if (car4Point.x > getScreenWidth()+100 || car4Point.x < -100) {
            car4Point.set(getScreenWidth() + 100, ((int) (Math.random() * getScreenHeight())));
        }

        if (car5Point.x > getScreenWidth()+100 || car5Point.x < -100) {
            car5Point.set(getScreenWidth() + 100, ((int) (Math.random() * getScreenHeight())));
        }

        if (car6Point.x > getScreenWidth()+100 || car6Point.x < -100) {
            car6Point.set(getScreenWidth() + 100, ((int) (Math.random() * getScreenHeight())));
        }
    }

    public void gameOver() {
        if (car1.playerCollide(player)) {

        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        canvas.drawColor(Color.LTGRAY); // Sets the background color. Instead of using "Color.rgb", we can simply use a color.

        player.draw(canvas); // Draw the player to the screen.

        car1.draw(canvas);
        car2.draw(canvas);
        car3.draw(canvas);
        car4.draw(canvas);
        car5.draw(canvas);
        car6.draw(canvas);

        upButton.draw(canvas); // Draw the up button to the screen.
        downButton.draw(canvas); // Draw the down button to the screen.
        leftButton.draw(canvas); // Draw the left button to the screen.
        rightButton.draw(canvas); // Draw the right button to the screen.
        actionButton.draw(canvas); // Draw the action button to the screen.

        movementImage.draw(canvas);
        actionImage.draw(canvas);

        //System.out.println(getScreenHeight());
        //System.out.println(getScreenWidth());
    }


}
