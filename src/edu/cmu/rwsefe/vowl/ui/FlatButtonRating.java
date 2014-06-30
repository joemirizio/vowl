package edu.cmu.rwsefe.vowl.ui;

import java.util.Arrays;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.MetricAffectingSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import edu.cmu.rwsefe.vowl.R;
import edu.cmu.rwsefe.vowl.R.color;
import edu.cmu.rwsefe.vowl.R.styleable;
import edu.cmu.rwsefe.vowl.ui.CustomTextView;

public class FlatButtonRating extends FlatButton {
	protected RatingBar mRatingBar;
	
	public FlatButtonRating(Context context, AttributeSet attributes) {
		super(context, attributes);
		this.init(context, attributes);
	}
	
	@Override
	protected void init(Context context, AttributeSet attributes) {
		super.init(context, attributes);
		
		mRatingBar = new RatingBar(context, null, android.R.attr.ratingBarStyleSmall);	
		//
	}
	
	@Override
	protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld){
    	super.onSizeChanged(xNew, yNew, xOld, yOld);
    	
    	// TODO: Scale shadowOffset and cornerRadius
		int scaledHeight = (int)(getHeight() - mShadowOffset);
		int scaledWidth = getWidth();
		
		
		mRatingBar.setIsIndicator(true);
		mRatingBar.setNumStars(5);
		mRatingBar.setStepSize(1.0f);
		mRatingBar.setRating(3.0f);
		
		
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
}