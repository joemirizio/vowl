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
import android.widget.Button;
import edu.cmu.rwsefe.vowl.R;
import edu.cmu.rwsefe.vowl.model.UserSettings;

public class FlatButton extends Button {
	public static final String TAG = FlatButton.class.getName();
	
	private static final String DEFAULT_FONT = "FredokaOne-Regular.ttf";
	
	protected ShapeDrawable mBase;
	protected ShapeDrawable mShadow;
	
	protected int mShadowOffset;
	protected int mBaseColor, mShadowColor;
	protected MetricAffectingSpan mTextStyle;
	
	private String mFormattedTextCache;
	private float[] mCorners = new float[8];
	private boolean mIsPressed;
	private boolean mIsSticky;
	
	public FlatButton(Context context, AttributeSet attributes) {
		super(context, attributes);
		this.init(context, attributes);
	}

	protected void init(Context context, AttributeSet attributes) {
	    TypedArray attrs = context.obtainStyledAttributes(attributes, R.styleable.FlatButton);
	    
	    mIsPressed = attrs.getBoolean(R.styleable.FlatButton_isPressed, false);
	    setShadowOffset(attrs.getInteger(R.styleable.FlatButton_shadowOffset, 30));
	    
	    float cornerRadius = attrs.getFloat(R.styleable.FlatButton_cornerRadius, 50f);
	    float topLeftCornerRadius = attrs.getFloat(R.styleable.FlatButton_topLeftCornerRadius, cornerRadius);
	    float topRightCornerRadius = attrs.getFloat(R.styleable.FlatButton_topRightCornerRadius, cornerRadius);
	    float bottomRightCornerRadius = attrs.getFloat(R.styleable.FlatButton_bottomRightCornerRadius, cornerRadius);
	    float bottomLeftCornerRadius = attrs.getFloat(R.styleable.FlatButton_bottomLeftCornerRadius, cornerRadius); 
	    setCornerRadius(topLeftCornerRadius, topRightCornerRadius, bottomRightCornerRadius, bottomLeftCornerRadius);
	    
	    mBaseColor = attrs.getColor(R.styleable.FlatButton_baseColor, getResources().getColor(R.color.redLight));
	    mShadowColor = attrs.getColor(R.styleable.FlatButton_shadowColor, getResources().getColor(R.color.redDark));
	
		// Set custom font if not displaying in editor
	    if (!this.isInEditMode()) {
	    	setCustomFont(DEFAULT_FONT);
	    } else {
	    	mTextStyle = new StyleSpan(Typeface.NORMAL);
	    }
		
	    // Override default button look
	    this.setBackgroundColor(getResources().getColor(android.R.color.transparent));
	    
		// Recycle TypedArray
		attrs.recycle();
	}
	
	public void setCustomFont(String fontFamily) {
		mTextStyle = new CustomTextView.TypefaceSpan(getContext(), fontFamily);
	}
	
	protected void applyCustomFont() {
		Spannable styledText = new SpannableString(getText());
		styledText.setSpan(mTextStyle, 0, styledText.length(), 0);
		setText(styledText);
		// Cache current text value
		mFormattedTextCache = styledText.toString();
	}
	
	protected void rebuildGraphics() {
		// TODO: Scale shadowOffset and cornerRadius
		int scaledHeight = (int)(getMeasuredHeight() - mShadowOffset);
		int scaledWidth = getMeasuredWidth();
		    	
		// Base
		mBase = new ShapeDrawable(new RoundRectShape(mCorners, null, null));
		mBase.getPaint().setColor(mBaseColor);
		mBase.setBounds(0, 0, scaledWidth, scaledHeight);

		// Shadow
		mShadow = new ShapeDrawable(mBase.getShape());
		mShadow.getPaint().setColor(mShadowColor);
		mShadow.setBounds(0, mShadowOffset, scaledWidth, scaledHeight + mShadowOffset);
	}
	
	@Override
	protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld){
    	super.onSizeChanged(xNew, yNew, xOld, yOld);
    	rebuildGraphics();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// Reset custom font if text changed
		if (!getText().equals(mFormattedTextCache)) {
			applyCustomFont();
		}
		
		// Draw shadow
		mShadow.draw(canvas);
		
		// Translate down if pressed
		canvas.save();
		if (mIsPressed || mIsSticky) {
			canvas.translate(0, mShadowOffset);
		}
		
		// Draw content
		onDrawContent(canvas);
		super.onDraw(canvas);
		canvas.restore();
	}
	
	protected void onDrawContent(Canvas canvas) {
		mBase.draw(canvas);
	}
	
	@Override
	public void setPressed(boolean isPressed) {
		super.setPressed(isPressed);
		mIsPressed = isPressed;
		rebuildGraphics();
	}
	
	public void setSticky(boolean isSticky) {
		mIsSticky = isSticky;
	}
	
	public void press() {
		setPressed(!mIsPressed);
	}
	
	public void setCornerRadius(float radius) {
		Arrays.fill(mCorners, radius);
	}
	
	public void setCornerRadius(float topLeft, float topRight, float bottomRight, float bottomLeft) {
		mCorners = new float[] { topLeft, topLeft, topRight, topRight, 
				bottomRight, bottomRight, bottomLeft, bottomLeft };
	}
	
	public void setBaseColor(int color) {
		mBaseColor = color;
	}
	
	public void setShadowColor(int color) {
		mShadowColor = color;
	}
	
	public void setShadowOffset(int offset) {
		// Offset text by the shadow offset
		this.setPadding(getPaddingLeft(), getPaddingTop() + mShadowOffset - offset, 
				getPaddingRight(), getPaddingBottom());
		mShadowOffset = offset;
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		
	}
}