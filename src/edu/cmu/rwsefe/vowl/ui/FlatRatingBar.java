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
import android.view.View.MeasureSpec;
import android.widget.RatingBar;

public class FlatRatingBar extends RatingBar {

	protected Bitmap mSampleTile;
	
	public FlatRatingBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs, defStyle);
	}
	
	public FlatRatingBar(Context context, AttributeSet attrs) {
		this(context, attrs, android.R.attr.ratingBarStyle);
	}
	
	protected void init(Context context, AttributeSet attributes, int defStyle) {
		LayerDrawable ratingBarStyle;
		if (defStyle == android.R.attr.ratingBarStyle) {
			ratingBarStyle = (LayerDrawable) getResources().getDrawable(R.drawable.ratingbar_large);
		} else {
			ratingBarStyle = (LayerDrawable) getResources().getDrawable(R.drawable.ratingbar);
		}
		Drawable tiled = tileify(ratingBarStyle, true);
		this.setProgressDrawable(tiled);
	}
	
	/**
	 * Calculates measurement based on a cached tile
	 * @see android.widget.RatingBar
	 */
	@Override
	protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	        
		if (mSampleTile != null) {
			// TODO: Once ProgressBar's TODOs are gone, this can be done more
			// cleanly than mSampleTile
	        final int width = mSampleTile.getWidth() * getNumStars();
	        setMeasuredDimension(resolveSizeAndState(width, widthMeasureSpec, 0),
	                getMeasuredHeight());
	    }
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
            if (mSampleTile == null) {
                mSampleTile = tileBitmap;
            }
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
