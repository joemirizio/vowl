package edu.cmu.rwsefe.vowl;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

public class FlatButton extends View {
	private ShapeDrawable mBase;
	private ShapeDrawable mShadow;
	private TextView mText;

	public FlatButton(Context context, AttributeSet attributes) {
		super(context, attributes);
		this.init(context, attributes);
	}

	private void init(Context context, AttributeSet attributes) {
	    TypedArray attrs = context.obtainStyledAttributes(attributes, R.styleable.FlatButton);		
		
		int x = attrs.getInt(R.styleable.FlatButton_x, 0);
		int y = attrs.getInt(R.styleable.FlatButton_y, 0);
		int width = attrs.getInt(R.styleable.FlatButton_width, 300);
		int height = attrs.getInt(R.styleable.FlatButton_height, 200);
		int cornerRadius = 50;
		int shadowOffset = 80;
		
		float[] round = new float[] { 
				cornerRadius, cornerRadius, cornerRadius, cornerRadius, 
				cornerRadius, cornerRadius, cornerRadius, cornerRadius };
		
		// Base
	    int baseColor = attrs.getColor(R.styleable.FlatButton_baseColor, getResources().getColor(R.color.redLight));
		mBase = new ShapeDrawable(new RoundRectShape(round, null, null));
		mBase.getPaint().setColor(baseColor);
		mBase.setBounds(x, y, x + width, y + height);
		
		// Shadow
	    int shadowColor = attrs.getColor(R.styleable.FlatButton_shadowColor, getResources().getColor(R.color.redDark));
		mShadow = new ShapeDrawable(mBase.getShape());
		mShadow.getPaint().setColor(shadowColor);
		mShadow.setBounds(x, y + shadowOffset, x + width, y + height + shadowOffset);
		
		// Text
		mText = new TextView(context);
		mText.setTextSize(20);
		mText.setText("Hello World");
		//mText.setId(123456);
		
		// Recycle TypedArray
		attrs.recycle();
	}

	protected void onDraw(Canvas canvas) {
		mShadow.draw(canvas);
		mBase.draw(canvas);
		mText.draw(canvas);
	}
}