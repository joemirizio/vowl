package edu.cmu.rwsefe.vowl.ui;

import edu.cmu.rwsefe.vowl.R;
import edu.cmu.rwsefe.vowl.R.styleable;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v4.util.LruCache;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;
import android.util.AttributeSet;
import android.widget.TextView;


//import android.text.style.MetricAffectingSpan;
//import android.util.AttributeSet;
//import android.widget.TextView;


/**
 * A {@link TextView} supporting cached custom {@link Typeface}s.
 * 
 * @see http://stackoverflow.com/questions/2376250/custom-fonts-and-xml-layouts-android/7197867
 */
public class CustomTextView extends TextView {

	private static LruCache<String, Typeface> sTypefaceCache = new LruCache<>(12);
	
    public CustomTextView(Context context) {
        super(context);
    }
    
    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setCustomFont(context, attrs);
    }
	
    public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.setCustomFont(context, attrs);
    }

    public void setCustomFont(Context context, AttributeSet attrs) {
    	TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.CustomTextView);
        String fontFamily = attributes.getString(R.styleable.CustomTextView_fontFamily);

        this.setTypeface(getCustomTypeface(context, fontFamily));
        
        attributes.recycle();
    }
    
    public static Typeface getCustomTypeface(Context context, String fontFamily) {
        Typeface typeface = sTypefaceCache.get(fontFamily);
        if (typeface == null) {
        	typeface = Typeface.createFromAsset(context.getAssets(), "fonts/" + fontFamily);
        	sTypefaceCache.put(fontFamily, typeface);
        }
        return typeface;
    }
    
    
    /**
     * Style a {@link Spannable} with a custom {@link Typeface}.
     * 
     * @see http://stackoverflow.com/questions/8607707/how-to-set-a-custom-font-in-the-actionbar-title
     */
    public static class TypefaceSpan extends MetricAffectingSpan {
    	
    	private Typeface mTypeface;
    	
    	public TypefaceSpan(Context context, String fontFamily) {
            mTypeface = getCustomTypeface(context, fontFamily);
        }

        @Override
        public void updateMeasureState(TextPaint paint) {
        	paint.setTypeface(mTypeface);
            // Note: This flag is required for proper typeface rendering
        	paint.setFlags(paint.getFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        }

        @Override
        public void updateDrawState(TextPaint paint) {
        	paint.setTypeface(mTypeface);
            // Note: This flag is required for proper typeface rendering
        	paint.setFlags(paint.getFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        }
    }
}
