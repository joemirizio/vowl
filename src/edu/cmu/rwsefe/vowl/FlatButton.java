package edu.cmu.rwsefe.vowl;

import java.util.Arrays;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class FlatButton extends View {
	private ShapeDrawable mBase;
	private ShapeDrawable mShadow;
	//private TextView mText;
	
	private int mX, mY;
	private int mWidth, mHeight;
	private int mBaseColor, mShadowColor;
	
	public FlatButton(Context context, AttributeSet attributes) {
		super(context, attributes);
		this.init(context, attributes);
	}

	private void init(Context context, AttributeSet attributes) {
	    TypedArray attrs = context.obtainStyledAttributes(attributes, R.styleable.FlatButton);		
		
		mX = attrs.getInt(R.styleable.FlatButton_x, 0);
		mY = attrs.getInt(R.styleable.FlatButton_y, 0);
		mWidth = attrs.getInt(R.styleable.FlatButton_width, 100);
		mHeight = attrs.getInt(R.styleable.FlatButton_height, 100);
		
	    mBaseColor = attrs.getColor(R.styleable.FlatButton_baseColor, getResources().getColor(R.color.redLight));
	    mShadowColor = attrs.getColor(R.styleable.FlatButton_shadowColor, getResources().getColor(R.color.redDark));
	
		// Text
		//mText = new TextView(context);
		//mText.setTextSize(20);
		//mText.setText("Hello World");
		//mText.setId(123456);
		
		// Recycle TypedArray
		attrs.recycle();
	}
	
	@Override
	protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld){
    	super.onSizeChanged(xNew, yNew, xOld, yOld);
    	
		int shadowOffset =  (int)((mHeight / 100.0) * getHeight() / 10.0);
		int scaledHeight = (int)((mHeight / 100.0) * (getHeight() - shadowOffset));
		int scaledWidth = (int)((mWidth / 100.0) * getWidth());
		int cornerRadius = (int)(scaledWidth / 10.0);
    	
		float[] roundCorners = new float[8];
		Arrays.fill(roundCorners, cornerRadius);
    	
		// Base
		mBase = new ShapeDrawable(new RoundRectShape(roundCorners, null, null));
		mBase.getPaint().setColor(mBaseColor);
		mBase.setBounds(mX, mY, mX + scaledWidth, mY + scaledHeight);

		// Shadow
		mShadow = new ShapeDrawable(mBase.getShape());
		mShadow.getPaint().setColor(mShadowColor);
		mShadow.setBounds(mX, mY + shadowOffset, mX + scaledWidth, mY + scaledHeight + shadowOffset);
	}

	protected void onDraw(Canvas canvas) {
		mShadow.draw(canvas);
		mBase.draw(canvas);
		//mText.draw(canvas);
	}
}