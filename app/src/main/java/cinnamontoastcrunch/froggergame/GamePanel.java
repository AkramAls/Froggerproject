package cinnamontoastcrunch.froggergame;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
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

    private MovementButton upButton;
    private MovementButton downButton;
    private MovementButton leftButton;
    private MovementButton rightButton;
    private MovementButton actionButton;

    private Point playerPoint;

    private Point upPoint;
    private Point downPoint;
    private Point leftPoint;
    private Point rightPoint;
    private Point actionPoint;

    private static int MOVE_SPEED = 15; // This determines the number of steps before reaching the end of the screen.
    private static int X_SPEED = ((getScreenWidth()/MOVE_SPEED)); // We scale the speed with screen size so bigger screens wont have more space.
    private static int Y_SPEED = ((getScreenWidth()/MOVE_SPEED));

    MediaPlayer randomCroak = MediaPlayer.create(getContext(), R.raw.croak);

    Random rand = new Random();
    int random;


    private void croak() {

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

        upButton = new MovementButton(new Rect(100, 100, 200, 200), Color.rgb(75, 75, 75));
        upPoint = new Point((175), (getScreenHeight()-275)); // This allows the button to always be located in the same spot, regardless of screen size.

        downButton = new MovementButton(new Rect(100, 100, 200, 200), Color.rgb(75, 75, 75));
        downPoint = new Point((175), (getScreenHeight()-75));

        leftButton = new MovementButton(new Rect(100, 100, 200, 200), Color.rgb(75, 75, 75));
        leftPoint = new Point((75), (getScreenHeight()-175));

        rightButton = new MovementButton(new Rect(100, 100, 200, 200), Color.rgb(75, 75, 75));
        rightPoint = new Point((275), (getScreenHeight()-175));

        actionButton = new MovementButton(new Rect(100, 100, 300, 300), Color.RED);
        actionPoint = new Point((getScreenWidth()-175), getScreenHeight()-175);

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
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        canvas.drawColor(Color.LTGRAY); // Sets the background color. Instead of using "Color.rgb", we can simply use a color.

        player.draw(canvas); // Draw the player to the screen.

        upButton.draw(canvas); // Draw the up button to the screen.
        downButton.draw(canvas); // Draw the down button to the screen.
        leftButton.draw(canvas); // Draw the left button to the screen.
        rightButton.draw(canvas); // Draw the right button to the screen.
        actionButton.draw(canvas); // Draw the action button to the screen.

        //System.out.println(getScreenHeight());
        //System.out.println(getScreenWidth());
    }


}
