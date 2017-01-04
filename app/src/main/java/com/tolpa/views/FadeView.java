package com.tolpa.views;

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

import com.tolpa.R;


/**
 * Created by ko3a4ok on 02.04.14.
 */
public class FadeView extends View {
    private static final int[] PROMOS = {R.drawable.promo_1, R.drawable.promo_2, R.drawable.promo_3};
//    private static final int[] PROMOS = {android.R.drawable.ic_delete, android.R.drawable.ic_input_add, android.R.drawable.ic_dialog_map};
    private Bitmap[] images = new Bitmap[PROMOS.length];
    private static final Paint ALPHA_PAINT = new Paint();
    private float[] alpha = new float[PROMOS.length];
    public FadeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < PROMOS.length; i++)
                    images[i] = BitmapFactory.decodeResource(getResources(), PROMOS[i]);
            }
        }.start();
        alpha[0] = 1f; // gbcfhtdf
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
            if (images[i] == null) return;
            canvas.drawBitmap(images[i], new Rect(0, 0, images[i].getWidth(), images[i].getHeight()),
                    new Rect(0, 0, getWidth(), getHeight()), ALPHA_PAINT);
        }
    }


}
