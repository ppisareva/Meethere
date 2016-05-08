package com.example.polina.meethere.views;

/**
 * Created by ko3a4ok on 5/8/16.
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.example.polina.meethere.R;


/**
 * Created by ko3a4ok on 02.04.14.
 */
public class FadeView extends View {
    private static final int[] PROMOS = {R.drawable.promo_1, R.drawable.promo_2, R.drawable.promo_3};
    private Bitmap[] images = new Bitmap[PROMOS.length];
    private static final Paint ALPHA_PAINT = new Paint();
    private float[] alpha = new float[PROMOS.length];
    public FadeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        for (int i = 0; i < PROMOS.length; i++)
            images[i] = BitmapFactory.decodeResource(getResources(), PROMOS[i]);
        alpha[0] = 1f;
    }

    public void setProgress(float alpha, int position) {
        this.alpha[position] = 1f-alpha;
        this.alpha[(position+1)%this.alpha.length] = alpha;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        for (int i = 0; i < PROMOS.length; i++) {
            ALPHA_PAINT.setAlpha((int) (255*alpha[i]));
            canvas.drawBitmap(images[i], new Rect(0, 0, images[i].getWidth(), images[i].getHeight()),
                    new Rect(0, 0, getWidth(), getHeight()), ALPHA_PAINT);
        }
    }


}
