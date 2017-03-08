package cinnamontoastcrunch.froggergame;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by Weine on 3/6/2017.
 */

public class Vehicle implements GameObject {

    private Rect rectangle;
    private int color;

    public Vehicle(Rect rectangle, int color) {
        this.rectangle = rectangle;
        this.color = color;
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


}
