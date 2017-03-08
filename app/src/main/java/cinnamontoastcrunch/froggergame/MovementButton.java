package cinnamontoastcrunch.froggergame;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

/**
 * Created by Weine on 3/6/2017.
 */

public class MovementButton implements GameObject {

    public Rect button;
    private int color;

    public MovementButton(Rect button, int color) {
        this.button = button;
        this.color = color;
    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawRect(button, paint);
    }

    @Override
    public void update() {

    }

    public void update(Point point) {
        button.set(point.x - button.width()/2, point.y - button.height()/2, point.x + button.width()/2, point.y + button.height()/2); // Left, Top, Right, Bottom
    }

}
