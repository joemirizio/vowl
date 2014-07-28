package edu.cmu.rwsefe.vowl;

import java.io.File;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.canvas.LipiTKJNIInterface;
import com.canvas.LipitkResult;
import com.canvas.Stroke;

import edu.cmu.rwsefe.vowl.ui.CustomTextView;

public class CanvasView extends View implements OnTouchListener {

	private static final String TAG = CanvasView.class.getName();
	
	private ArrayList<Stroke> strokes;
	private Stroke[] recognitionStrokes;
	private LipiTKJNIInterface lipitkInterface;
	private LipiTKJNIInterface recognizer;
	private Stroke currentStroke;
	private HashMap<String, Integer> characters;
	private ArrayList<Point> vals = new ArrayList<Point>();
	private int minY = 480;
	private int maxY = 0;
	private int minX = 800;
	private int maxX = 0;
	private MyCount counter;
	private MyLongPressCount myLongPress;

	private Path drawPath;
	private ScoreEventListener mScoreEventListener;
	
	// Guides and outline
	private Paint mSolidPaint;
	private Paint mDottedPaint;
	private Paint mOutlinePaint;
	private Path mSolidGuides;
	private Path mDottedGuides;
	private String mOutlineCharacter = "";

	// global variable imports
	private static boolean timerFlag = true;
	private static Paint paint = null;
	private static boolean longPressFlag=true;
	private static boolean isUserWriting=true;
	private static boolean isFirststroke = true;

	@SuppressLint("NewApi")
	public CanvasView(Context context, AttributeSet attributes) {
		super(context, attributes);
		
		setFocusable(true);
		setFocusableInTouchMode(true);
		setOnTouchListener(this);
		
		paint = new Paint();
		paint.setColor(Color.BLACK);
		paint.setAntiAlias(true);
		paint.setDither(true);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeJoin(Paint.Join.ROUND);
		paint.setStrokeCap(Paint.Cap.ROUND);
		paint.setStrokeWidth(15);
		
		initializeStroke();
		
		if (!isInEditMode()) {
			Context contextlipi = getContext();
			File externalFileDir = contextlipi.getExternalFilesDir(null);
			String path = externalFileDir.getPath();
			lipitkInterface = new LipiTKJNIInterface(path, "SHAPEREC_ALPHANUM");
			lipitkInterface.initialize();
			recognizer = lipitkInterface;
		} else {
			mOutlineCharacter = "x";
		}
	}
	public boolean onTouch(View v, MotionEvent event) {
		Point vs = new Point((int) event.getX(), (int) event.getY());
		float X = (float) vs.x;
		float Y = (float) vs.y;
		PointF p = new PointF(X, Y);
		currentStroke.addPoint(p);

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			
			drawPath.moveTo(X, Y);
			counter.cancel();
			myLongPress.start();
			isUserWriting = true;
			vals.add(vs);
			invalidate();

			if (vs.y > maxY) { maxY = vs.y;	}
			if (vs.y < minY) { minY = vs.y;	}
			if (vs.x > maxX) { maxX = vs.x; }
			if (vs.x < minX) { minX = vs.x;	}

			Log.i(TAG, "ACTION - Down Stroke:");
			break;
			
		case MotionEvent.ACTION_MOVE:

			drawPath.lineTo(X, Y);
			counter.cancel();
			longPressFlag = true;
			vals.add(vs);
			invalidate();

			if (vs.y > maxY) { maxY = vs.y;	}
			if (vs.y < minY) { minY = vs.y;	}
			if (vs.x > maxX) { maxX = vs.x; }
			if (vs.x < minX) { minX = vs.x;	}

			Log.i(TAG, "ACTION - Move Stroke:");
			break;
			
		case MotionEvent.ACTION_UP:
			// This condition should be checked only once for the first stroke
			// after a time out
			if (isFirststroke && (maxY - minY) < 30 && (maxY != minY)) {
				isUserWriting = false;
			}

			isFirststroke = (isFirststroke && !isUserWriting);

			if (isUserWriting) {
				counter.start();
			}

			myLongPress.cancel();
			Log.i(TAG, "ACTION - Up Stroke:");
			break;
		}

		return true;
	}
		
	public void initializeStroke() {
		// TODO Reset static variables?
		counter = new MyCount(700, 1000);
		myLongPress = new MyLongPressCount(3000, 1000);
		currentStroke = new Stroke();
		strokes = new ArrayList<Stroke>();
		characters = new HashMap<String, Integer>();
		drawPath = new Path();
		invalidate();
	}

	public void addStroke() {
		strokes.add(currentStroke);
		recognitionStrokes = new Stroke[strokes.size()];
		for (int s = 0; s < strokes.size(); s++)
			recognitionStrokes[s] = strokes.get(s);
		LipitkResult[] results = recognizer.recognize(recognitionStrokes);

		for (LipitkResult result : results) {
			Log.i(TAG, "ShapeID = " + result.Id + 
					", Confidence = " + result.Confidence);
		}

		String configFileDirectory = recognizer.getLipiDirectory()
				+ "/projects/alphanumeric/config/";
		characters.clear();
		for (LipitkResult result : results) {
			String key = recognizer.getSymbolName(result.Id,
					configFileDirectory);
			int value = (int) (result.Confidence * 100);
			characters.put(key, value);
		}

		recognitionStrokes = null;
	}

	public HashMap<String, Integer> getCharacterResults() {
		return characters;
	}
	
	private void buildGuides() {
		// Character outline
		mOutlinePaint = new Paint();
		mOutlinePaint.setTextAlign(Align.CENTER);
		mOutlinePaint.setTextSize(getHeight() / 2);
		mOutlinePaint.setColor(isInEditMode() ? 
				Color.LTGRAY : this.getResources().getColor(R.color.grayLight));
		if (!isInEditMode()) {
			mOutlinePaint.setTypeface(CustomTextView.getCustomTypeface(getContext(), "FredokaOne-Regular.ttf"));
		}
		
		// Solid guide
		mSolidPaint = new Paint(paint);
		mSolidPaint.setColor(
				isInEditMode() ? Color.GRAY : this.getResources().getColor(R.color.grayMedium));
		mSolidGuides = new Path();
		int topOffset = 80;
		FontMetrics fontMetrics = mOutlinePaint.getFontMetrics();
		
		float xHeight = -1 * fontMetrics.top; //(int)(getHeight() / 2.0f);
		mSolidGuides.moveTo(0, topOffset);
		mSolidGuides.lineTo(getWidth(), topOffset);
		mSolidGuides.moveTo(0, xHeight);
		mSolidGuides.lineTo(getWidth(), xHeight);
		
		// Dotted guide
		mDottedPaint = new Paint(paint);
		mDottedPaint.setColor(mOutlinePaint.getColor());
		mDottedPaint.setPathEffect(new DashPathEffect(new float[] {10,20}, 0));
		mDottedGuides = new Path();
		mDottedGuides.moveTo(0, xHeight / 2);
		mDottedGuides.lineTo(getWidth(), xHeight / 2);
		mDottedGuides.moveTo(0, xHeight + fontMetrics.bottom);
		mDottedGuides.lineTo(getWidth(), xHeight + fontMetrics.bottom);
		
	}
	
	public void setOutlineCharacter(String outlineCharacter) {
		mOutlineCharacter = outlineCharacter;
		// Force a redraw
		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (mSolidGuides == null || mDottedGuides == null) {
			buildGuides();
		}
		canvas.save();
		canvas.drawPath(mSolidGuides, mSolidPaint);
		canvas.drawPath(mDottedGuides, mDottedPaint);
		canvas.drawText(mOutlineCharacter, getWidth() / 2, getHeight() / 2, mOutlinePaint);
		canvas.drawPath(drawPath, paint);
		canvas.restore();
	}

	public void setScoreEventListener(ScoreEventListener scoreEventListener) {
		mScoreEventListener = scoreEventListener;
	}
	
	
	
	public interface ScoreEventListener extends EventListener {
		public void onScore();
	}
	
	public class MyCount extends CountDownTimer {

		public MyCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {
			System.out.println("Timer Flag :: " + timerFlag);
			if (longPressFlag) {
				if (mScoreEventListener != null) {
					mScoreEventListener.onScore();
				}
				isUserWriting = false;
				isFirststroke = true;
			}
		}

		@Override
		public void onTick(long millisUntilFinished) {
			System.out.println("Tick tick Flag :: " + timerFlag);
		}

	}
	
	public class MyLongPressCount extends CountDownTimer {

		public MyLongPressCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {
			longPressFlag = false;
			Log.i(TAG, "Long press timer expiry: Timer Flag :: " + timerFlag);
		}

		@Override
		public void onTick(long millisUntilFinished) {
			Log.i(TAG, "Tick tick Flag :: " + timerFlag);
		}

	}
}
