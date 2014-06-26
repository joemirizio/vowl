package edu.cmu.rwsefe.vowl;

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
import android.widget.Button;
import android.widget.TextView;
import edu.cmu.rwsefe.vowl.CustomTextView;

public class FlatButton extends Button {
	protected ShapeDrawable mBase;
	protected ShapeDrawable mShadow;
	
	protected int mShadowOffset;
	protected int mCornerRadius;
	protected int mBaseColor, mShadowColor;
	protected MetricAffectingSpan mTextStyle;
	
	private String mFormattedTextCache;
	
	public FlatButton(Context context, AttributeSet attributes) {
		super(context, attributes);
		this.init(context, attributes);
	}

	private void init(Context context, AttributeSet attributes) {
	    TypedArray attrs = context.obtainStyledAttributes(attributes, R.styleable.FlatButton);
	    
	    mShadowOffset = attrs.getInteger(R.styleable.FlatButton_shadowOffset, 30);
	    mCornerRadius = attrs.getInteger(R.styleable.FlatButton_cornerRadius, 50);
	    
	    mBaseColor = attrs.getColor(R.styleable.FlatButton_baseColor, getResources().getColor(R.color.redLight));
	    mShadowColor = attrs.getColor(R.styleable.FlatButton_shadowColor, getResources().getColor(R.color.redDark));
	
		// Set custom font if not displaying in editor
	    if (!this.isInEditMode()) {
	    	mTextStyle = new CustomTextView.TypefaceSpan(context, "FredokaOne-Regular.ttf");
	    } else {
	    	mTextStyle = new StyleSpan(Typeface.NORMAL);
	    }
	    this.applyCustomFont();
		
		// Recycle TypedArray
		attrs.recycle();
	}
	
	protected void applyCustomFont() {
		Spannable styledText = new SpannableString(getText());
		styledText.setSpan(mTextStyle, 0, styledText.length(), 0);
		setText(styledText);
		// Cache current text value
		mFormattedTextCache = styledText.toString();
	}
	
	@Override
	protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld){
    	super.onSizeChanged(xNew, yNew, xOld, yOld);
    	
    	// TODO: Scale shadowOffset and cornerRadius
		int scaledHeight = (int)(getHeight() - mShadowOffset);
		int scaledWidth = getWidth();
		
		// Offset text by the shadow offset
		this.setPadding(getPaddingLeft(), getPaddingTop() - mShadowOffset, 
				getPaddingRight(), getPaddingBottom());
		
		float[] roundCorners = new float[8];
		Arrays.fill(roundCorners, mCornerRadius);
    	
		// Base
		mBase = new ShapeDrawable(new RoundRectShape(roundCorners, null, null));
		mBase.getPaint().setColor(mBaseColor);
		mBase.setBounds(0, 0, scaledWidth, scaledHeight);

		// Shadow
		mShadow = new ShapeDrawable(mBase.getShape());
		mShadow.getPaint().setColor(mShadowColor);
		mShadow.setBounds(0, mShadowOffset, scaledWidth, scaledHeight + mShadowOffset);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		mShadow.draw(canvas);
		mBase.draw(canvas);
		// Reset custom font if text changed
		if (!getText().equals(mFormattedTextCache)) {
			applyCustomFont();
		}
		
		super.onDraw(canvas);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		
	}
}