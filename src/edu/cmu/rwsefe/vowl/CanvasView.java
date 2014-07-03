package edu.cmu.rwsefe.vowl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
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
import com.canvas.Page;
import com.canvas.Stroke;
import com.canvas.globalvariable;

public class CanvasView extends View implements OnTouchListener {

	private ArrayList<Stroke> strokes;
	private Page page;
	private Stroke[] recognitionStrokes;
	private LipiTKJNIInterface lipitkInterface;
	private LipiTKJNIInterface recognizer;
	private Stroke currentStroke;
	private CanvasActivity canvasActivity;
	private HashMap<String, Integer> characters;
	private ArrayList<Values> vals = new ArrayList<Values>();
	private int minY=480;
	private int maxY=0;
	private int minX=800;
	private int maxX=0;
	private MyCount counter;
	private MyLongPressCount myLongPress;
	
	private Path drawPath;
	private Paint canvasPaint;
	
	@SuppressLint("NewApi")
	public CanvasView(Context context, CanvasActivity canvas) {
		super(context);
		// TODO Auto-generated constructor stub
		canvasActivity = canvas;
		globalvariable.paint=new Paint();
		setFocusable(true);
		setFocusableInTouchMode(true);
		this.setOnTouchListener(this);
		globalvariable.paint.setColor(Color.BLACK);
		globalvariable.paint.setAntiAlias(true);
		globalvariable.paint.setDither(true); 
		globalvariable.paint.setStyle(Paint.Style.STROKE); 
		globalvariable.paint.setStrokeJoin(Paint.Join.ROUND);
		globalvariable.paint.setStrokeCap(Paint.Cap.ROUND);
		globalvariable.paint.setStrokeWidth(15);
		counter = new MyCount(700,1000);
		myLongPress = new MyLongPressCount(3000,1000);
		currentStroke = new Stroke();
		strokes = new ArrayList<Stroke>();
		recognizer = null;
		characters = new HashMap<String, Integer>();
		Context contextlipi = getContext();
		File externalFileDir = contextlipi.getExternalFilesDir(null);
		String path = externalFileDir.getPath();
		lipitkInterface = new LipiTKJNIInterface(path, "SHAPEREC_ALPHANUM");
		lipitkInterface.initialize();
		page = new Page(lipitkInterface);
		recognizer=lipitkInterface;

		drawPath = new Path();
		canvasPaint = new Paint(Paint.DITHER_FLAG);
	}

	public boolean onTouch(View v, MotionEvent event) {
		Values vs=new Values();
		
		vs.x = (int) event.getX();
		vs.y = (int) event.getY();
		float  X= (float) vs.x;
		float  Y= (float) vs.y;
		PointF p = new PointF(X, Y);
		currentStroke.addPoint(p);
		
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN: {
				
				drawPath.moveTo(X, Y);
				
				counter.cancel();
				myLongPress.start();
				globalvariable.IsUserWriting=true;
	
				if(vs.y>maxY)
					maxY=vs.y;
				if(vs.y<minY)
					minY=vs.y;

				if(vs.x>maxX)
					maxX=vs.x;
				if(vs.x<minX)
					minX=vs.x;
	
				globalvariable.strokeXY += "{" + vs.x + "," + vs.y + "}|";
				vals.add(vs);
				invalidate();
				System.out.println("action down stroke values===");
				
				break;
			}
			case MotionEvent.ACTION_MOVE: {
				
				drawPath.lineTo(X, Y);
				
				counter.cancel();
				
				//myLongPress.cancel();
				globalvariable.VSG=vs.x;
				globalvariable.LongPressFlag=true;
	
				globalvariable.strokeXY += "{" + vs.x + "," + vs.y + "}|";
				vals.add(vs);
	
				if(vs.y>maxY)
					maxY=vs.y;
				if(vs.y<minY)
					minY=vs.y;

				if(vs.x>maxX)
					maxX=vs.x;
				if(vs.x<minX)
					minX=vs.x;
	
				System.out.println("action move stroke values===");
				invalidate();
				break;
			}
			case MotionEvent.ACTION_UP:{ 

				//drawPath.reset();
				
				System.out.println("Max==="+maxY);
				System.out.println("Min==="+minY);
				globalvariable.strokeXY += "N";
	
				/* this condition should be checked only once for the first stroke after 
				   a time out */
				if(globalvariable.isFirststroke)
				{
					if((maxY-minY) < 30 &&(maxY!=minY))
					{
						globalvariable.IsUserWriting = false;
					}
				}
	
				if(globalvariable.isFirststroke && globalvariable.IsUserWriting == false)
				{
					globalvariable.isFirststroke = true;
				}
				else
				{
					globalvariable.isFirststroke = false;
				}
	
				if(globalvariable.IsUserWriting)
				{
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
			Log.e("jni", "ShapeID = " + result.Id + " Confidence = " + result.Confidence);			
		}
		
		String configFileDirectory = recognizer.getLipiDirectory() + "/projects/alphanumeric/config/";
		characters.clear();
		for(LipitkResult result: results){
			String key = recognizer.getSymbolName(result.Id, configFileDirectory);
			int value = (int) (result.Confidence * 100);
			characters.put(key, value);
		}
							
		recognitionStrokes = null;
		
	}
	
	public HashMap<String,Integer> getCharacterResults() {
		return characters;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {

		
		BufferedWriter out = null;
		/*
		for (Values values : vals) {
			canvas.save();
			canvas.drawPoint(values.x, values.y, globalvariable.paint);
			canvas.restore();
		}
		*/
		
		canvas.drawPath(drawPath, globalvariable.paint);
		File root = android.os.Environment.getExternalStorageDirectory(); 
		File file = new File(root, "Freepad/points.txt");
		if(!(file.isDirectory()))
		{
			return;
		}
		else
		{
			try {
				out = new BufferedWriter(new FileWriter(file));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				out.write(globalvariable.strokeXY);
	
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				out.close();
	
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			globalvariable.canvasvar=canvas;
			System.out.println("stroke values:-------"+globalvariable.strokeXY);
			System.out.println("stroke values:-------"+globalvariable.strokeXY.length());
		}
		
	}
	
	public class MyCount extends CountDownTimer{

		public MyCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {
			System.out.println("Timer Flag :: " + globalvariable.TimerFlag);
			if(globalvariable.LongPressFlag){
				canvasActivity.processDialogBox();
				globalvariable.IsUserWriting=false;
				globalvariable.isFirststroke = true;
			}
			
			else{

			}

		}

		@Override
		public void onTick(long millisUntilFinished) {
			System.out.println("Tick tick Flag :: " + globalvariable.TimerFlag);
		}

	}
	
	public class MyLongPressCount extends CountDownTimer{

		public MyLongPressCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {
			globalvariable.LongPressFlag=false;
			System.out.println("Long press timer expiry: Timer Flag :: " + globalvariable.TimerFlag);
			//canObj.ClearCanvas();

		}

		@Override
		public void onTick(long millisUntilFinished) {
			System.out.println("Tick tick Flag :: " + globalvariable.TimerFlag);
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
