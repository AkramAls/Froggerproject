package cinnamontoastcrunch.froggergame;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

/**
 * Created by Jpollock on 3/6/2017.
 */

public class Powerup implements GameObject {

    private Rect rectangle;
    private int color;
    String type;

    public Rect getRectangle() {
        return rectangle;
    }

    public Powerup(Rect rectangle, int color, String type) {
        this.rectangle = rectangle;
        this.color = color;
        this.type = type;
    }

    public boolean playerCollide(Player player) {
        if(rectangle.contains(player.getRectangle().left, player.getRectangle().top)
                || rectangle.contains(player.getRectangle().right, player.getRectangle().top)
                || rectangle.contains(player.getRectangle().left, player.getRectangle().bottom)
                || rectangle.contains(player.getRectangle().right, player.getRectangle().bottom)) // Collision detection between player and vehicles.
                return true;
        return false;
    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawRect(rectangle, paint);
    }

    @Override
    public void update() {

    }

    public void update(Point point) {
            rectangle.set(point.x - rectangle.width() / 2, point.y - rectangle.height() / 2, point.x + rectangle.width() / 2, point.y + rectangle.height() / 2); // Left, Top, Right, Bottom
    }


}
