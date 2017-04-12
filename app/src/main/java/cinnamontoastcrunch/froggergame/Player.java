package cinnamontoastcrunch.froggergame;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

/**
 * Created by JPollock on 3/4/2017.
 */

public class Player implements GameObject {

    private Rect rectangle;
    private int color;

    public Rect getRectangle() {
        return rectangle;
    }

    public Player(Rect rectangle, int color) {
        this.rectangle = rectangle;
        this.color = color;
    }


    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint(); // Paint allows us to customize the look of everything.
        paint.setColor(color);     // Here we use it to choose the player's color.
        canvas.drawRect(rectangle, paint);
    }

    @Override
    public void update() {

    }


    public boolean powerupCollide(Powerup powerup) {
        if(rectangle.contains(powerup.getRectangle().left, powerup.getRectangle().top)
                || rectangle.contains(powerup.getRectangle().right, powerup.getRectangle().top)
                || rectangle.contains(powerup.getRectangle().left, powerup.getRectangle().bottom)
                || rectangle.contains(powerup.getRectangle().right, powerup.getRectangle().bottom)) // Collision detection between player and vehicles.
            return true;
        return false;
    }

    public void update(Point point) {
        rectangle.set(point.x - rectangle.width()/2, point.y - rectangle.height()/2, point.x + rectangle.width()/2, point.y + rectangle.height()/2); // Left, Top, Right, Bottom
    }
}
