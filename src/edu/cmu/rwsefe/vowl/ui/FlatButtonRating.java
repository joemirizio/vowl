package edu.cmu.rwsefe.vowl.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.RatingBar;

public class FlatButtonRating extends FlatButton {
	
	public static final int MAX_STARS = 5;
	
	private static final int RATING_BAR_OFFSET = 20;
	
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
		
		mRatingBar.setIsIndicator(true);
		mRatingBar.setNumStars(MAX_STARS);
		mRatingBar.setStepSize(1.0f);
		
		mRatingBar.measure(
				MeasureSpec.getSize(mRatingBar.getMeasuredWidth()), 
				MeasureSpec.getSize(mRatingBar.getMeasuredHeight()));
		mRatingBar.layout(0, 0, mRatingBar.getMeasuredWidth(), mRatingBar.getMeasuredHeight());
	}

	@Override
	protected void onDrawContent(Canvas canvas) {
		super.onDrawContent(canvas);

		canvas.save();
		canvas.translate(
				this.getWidth() / 2 - mRatingBar.getWidth() / 2, 
				this.getHeight() - mShadowOffset - mRatingBar.getHeight() - RATING_BAR_OFFSET);
		mRatingBar.draw(canvas);
		canvas.restore();
	}
	
	public void setRating(float rating) {
		mRatingBar.setRating(rating);
	}
}