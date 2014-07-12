package edu.cmu.rwsefe.vowl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.canvas.LipiTKJNIInterface;
import com.canvas.LipitkResult;
import com.canvas.Stroke;

public class CanvasView extends View implements OnTouchListener {

	private ArrayList<Stroke> strokes;
	private Stroke[] recognitionStrokes;
	private LipiTKJNIInterface lipitkInterface;
	private LipiTKJNIInterface recognizer;
	private Stroke currentStroke;
	private CanvasActivity canvasActivity;
	private HashMap<String, Integer> characters;
	private ArrayList<Values> vals = new ArrayList<Values>();
	private int minY = 480;
	private int maxY = 0;
	private int minX = 800;
	private int maxX = 0;
	private MyCount counter;
	private MyLongPressCount myLongPress;

	private Path drawPath;
	private Paint canvasPaint;

	// global variable imports
	private static boolean timerFlag = true;
	private static Paint paint = null;
	private static boolean longPressFlag=true;
	private static boolean isUserWriting=true;
	private static boolean isFirststroke = true;


	@SuppressLint("NewApi")
	public CanvasView(Context context, CanvasActivity canvas) {
		super(context);
		// TODO Auto-generated constructor stub
		canvasActivity = canvas;
		paint = new Paint();
		setFocusable(true);
		setFocusableInTouchMode(true);
		this.setOnTouchListener(this);
		paint.setColor(Color.BLACK);
		paint.setAntiAlias(true);
		paint.setDither(true);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeJoin(Paint.Join.ROUND);
		paint.setStrokeCap(Paint.Cap.ROUND);
		paint.setStrokeWidth(15);
		counter = new MyCount(700, 1000);
		myLongPress = new MyLongPressCount(3000, 1000);
		currentStroke = new Stroke();
		strokes = new ArrayList<Stroke>();
		recognizer = null;
		characters = new HashMap<String, Integer>();
		Context contextlipi = getContext();
		File externalFileDir = contextlipi.getExternalFilesDir(null);
		String path = externalFileDir.getPath();
		lipitkInterface = new LipiTKJNIInterface(path, "SHAPEREC_ALPHANUM");
		lipitkInterface.initialize();
		recognizer = lipitkInterface;

		drawPath = new Path();
		canvasPaint = new Paint(Paint.DITHER_FLAG);
	}

	public boolean onTouch(View v, MotionEvent event) {
		Values vs = new Values();

		vs.x = (int) event.getX();
		vs.y = (int) event.getY();
		float X = (float) vs.x;
		float Y = (float) vs.y;
		PointF p = new PointF(X, Y);
		currentStroke.addPoint(p);

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: {

			drawPath.moveTo(X, Y);

			counter.cancel();
			myLongPress.start();
			isUserWriting = true;

			if (vs.y > maxY)
				maxY = vs.y;
			if (vs.y < minY)
				minY = vs.y;

			if (vs.x > maxX)
				maxX = vs.x;
			if (vs.x < minX)
				minX = vs.x;

			vals.add(vs);
			invalidate();
			System.out.println("action down stroke values===");

			break;
		}
		case MotionEvent.ACTION_MOVE: {

			drawPath.lineTo(X, Y);

			counter.cancel();

			// myLongPress.cancel();
			longPressFlag = true;

			vals.add(vs);

			if (vs.y > maxY)
				maxY = vs.y;
			if (vs.y < minY)
				minY = vs.y;

			if (vs.x > maxX)
				maxX = vs.x;
			if (vs.x < minX)
				minX = vs.x;

			System.out.println("action move stroke values===");
			invalidate();
			break;
		}
		case MotionEvent.ACTION_UP: {

			/*
			 * this condition should be checked only once for the first stroke
			 * after a time out
			 */
			if (isFirststroke) {
				if ((maxY - minY) < 30 && (maxY != minY)) {
					isUserWriting = false;
				}
			}

			if (isFirststroke
					&& isUserWriting == false) {
				isFirststroke = true;
			} else {
				isFirststroke = false;
			}

			if (isUserWriting) {
				counter.start();
			}

			myLongPress.cancel();
			System.out.println("action up stroke values===");

			break;
		}
		}

		return true;
	}

	public void addStroke() {
		strokes.add(currentStroke);
		recognitionStrokes = new Stroke[strokes.size()];
		for (int s = 0; s < strokes.size(); s++)
			recognitionStrokes[s] = strokes.get(s);
		LipitkResult[] results = recognizer.recognize(recognitionStrokes);

		for (LipitkResult result : results) {
			Log.e("jni", "ShapeID = " + result.Id + " Confidence = "
					+ result.Confidence);
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

	@Override
	protected void onDraw(Canvas canvas) {

		canvas.save();
		canvas.drawPath(drawPath, paint);
		canvas.restore();

	}

	public class MyCount extends CountDownTimer {

		public MyCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {
			System.out.println("Timer Flag :: " + timerFlag);
			if (longPressFlag) {
				canvasActivity.processDialogBox();
				isUserWriting = false;
				isFirststroke = true;
			}

			else {

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
			System.out.println("Long press timer expiry: Timer Flag :: "
					+ timerFlag);
			// canObj.ClearCanvas();

		}

		@Override
		public void onTick(long millisUntilFinished) {
			System.out.println("Tick tick Flag :: " + timerFlag);
		}

	}
}

class Values {
	int x, y;

	@Override
	public String toString() {
		return x + ", " + y;
	}
}
