package edu.cmu.rwsefe.vowl.ui;

import edu.cmu.rwsefe.vowl.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.RatingBar;

public class FlatRatingBar extends RatingBar {

	public FlatRatingBar(Context context, AttributeSet attrs) {
		super(context, attrs, android.R.attr.ratingBarStyleSmall);
		init(context, attrs);
	}
	
	protected void init(Context context, AttributeSet attributes) {
		// TODO Make drawable dynamic
		LayerDrawable ratingBarStyle = (LayerDrawable) getResources().getDrawable(R.drawable.ratingbar);		
		Drawable tiled = tileify(ratingBarStyle, true);
		this.setProgressDrawable(tiled);
	}
	
	/**
     * Converts a drawable to a tiled version of itself. It will recursively
     * traverse layer and state list drawables.
     * @see android.widget.RatingBar
     */
    private Drawable tileify(Drawable drawable, boolean clip) {
        
        if (drawable instanceof LayerDrawable) {
            LayerDrawable background = (LayerDrawable) drawable;
            final int N = background.getNumberOfLayers();
            Drawable[] outDrawables = new Drawable[N];
            
            for (int i = 0; i < N; i++) {
                int id = background.getId(i);
                outDrawables[i] = tileify(background.getDrawable(i),
                        (id == android.R.id.progress || id == android.R.id.secondaryProgress));
            }

            LayerDrawable newBg = new LayerDrawable(outDrawables);
            
            for (int i = 0; i < N; i++) {
                newBg.setId(i, background.getId(i));
            }
            
            return newBg;
            
        }  else if (drawable instanceof BitmapDrawable) {
            final Bitmap tileBitmap = ((BitmapDrawable) drawable).getBitmap();
            
            final float[] roundedCorners = new float[] { 5, 5, 5, 5, 5, 5, 5, 5 };
            Shape roundedRect = new RoundRectShape(roundedCorners, null, null);
            final ShapeDrawable shapeDrawable = new ShapeDrawable(roundedRect);

            final BitmapShader bitmapShader = new BitmapShader(tileBitmap,
                    Shader.TileMode.REPEAT, Shader.TileMode.CLAMP);
            shapeDrawable.getPaint().setShader(bitmapShader);

            return (clip) ? new ClipDrawable(shapeDrawable, Gravity.LEFT,
                    ClipDrawable.HORIZONTAL) : shapeDrawable;
        }
        
        return drawable;
    }
}
