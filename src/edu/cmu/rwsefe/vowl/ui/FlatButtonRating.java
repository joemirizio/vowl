package edu.cmu.rwsefe.vowl.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.RatingBar;
import edu.cmu.rwsefe.vowl.R;

public class FlatButtonRating extends FlatButton {
	public static final int MAX_STARS = 5;
	protected RatingBar mRatingBar;
	
	public FlatButtonRating(Context context, AttributeSet attributes) {
		super(context, attributes);
		this.init(context, attributes);
	}
	
	@Override
	protected void init(Context context, AttributeSet attributes) {
		super.init(context, attributes);
		
		mRatingBar = new FlatRatingBar(context, attributes, android.R.attr.ratingBarStyleSmall);	
	}
	
	@Override
	protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld){
    	super.onSizeChanged(xNew, yNew, xOld, yOld);
    	
    	// TODO Scale shadowOffset and cornerRadius
		int scaledHeight = (int)(getHeight() - mShadowOffset);
		int scaledWidth = getWidth();
		
		mRatingBar.setIsIndicator(true);
		mRatingBar.setNumStars(MAX_STARS);
		mRatingBar.setStepSize(1.0f);
		
		mRatingBar.measure(
				MeasureSpec.getSize(mRatingBar.getMeasuredWidth()), 
				MeasureSpec.getSize(mRatingBar.getMeasuredHeight()));
		mRatingBar.layout(0, 0, mRatingBar.getMeasuredWidth(), mRatingBar.getMeasuredHeight());
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		int ratingBarOffset = 20;
		canvas.translate(
				this.getWidth() / 2 - mRatingBar.getWidth() / 2, 
				this.getHeight() - mShadowOffset - mRatingBar.getHeight() - ratingBarOffset);
		mRatingBar.draw(canvas);
		canvas.restore();
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		
	}
	
	public void setRating(float rating) {
		mRatingBar.setRating(rating);
	}
}