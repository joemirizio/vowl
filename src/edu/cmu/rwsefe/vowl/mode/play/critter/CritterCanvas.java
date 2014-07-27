package edu.cmu.rwsefe.vowl.mode.play.critter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import edu.cmu.rwsefe.vowl.CanvasView;
import edu.cmu.rwsefe.vowl.R;

class CritterCanvas extends CanvasView {		
	private final int[] mCritterIds = new int[] {
		R.drawable.critter_red,
		R.drawable.critter_blue, 
		R.drawable.critter_green,
		R.drawable.critter_yellow, 
		R.drawable.critter_purple
	};
	private final List<Drawable> mCritterImages;
	private final List<Critter> mCritters;
	private int mCritterCount; 
	
	public CritterCanvas(Context context, AttributeSet attrs) {
		super(context, attrs);
		// Load critter images
		mCritterImages = new ArrayList<Drawable>();
		for (int critterId : mCritterIds) {
			mCritterImages.add(context.getResources().getDrawable(critterId));
		}
		mCritters = new ArrayList<Critter>();
	}
	
	public void generateRandomCritters(int critterCount) {
		mCritterCount = critterCount;
		mCritters.clear();
		Random randomGen = new Random();
		for (int critterIndex = 0; critterIndex < mCritterCount; critterIndex++) {
			Drawable image = getContext().getResources().getDrawable(mCritterIds[randomGen.nextInt(mCritterIds.length)]);
			
			// Ensure the bounds do not intersect
			boolean intersects;
			Rect bounds = null;
			do {
				intersects = false;
				int left = randomGen.nextInt(getWidth() - image.getIntrinsicWidth());
				int top = randomGen.nextInt(getHeight() - image.getIntrinsicHeight());
				bounds = new Rect(left, top, 
						left + image.getIntrinsicWidth(), 
						top + image.getIntrinsicHeight());
				for (Critter critter : mCritters) {
					intersects |= bounds.intersect(critter.getBounds());
				}
			} while (intersects);
			
			mCritters.add(new Critter(image, bounds));
		}
	}
	
	@Override
	public void onLayout(boolean changed, int left, int top, int right, int bottom) {
		generateRandomCritters(10);
	}
	
	@Override
	public void onDraw(Canvas canvas) {
		for (Critter critter : mCritters) {
			critter.getImage().draw(canvas);
		}
	}
	
	class Critter {
		private Drawable mImage;
		private Rect mBounds;
		
		public Critter(Drawable image, Rect bounds) {
			mImage = image;
			mBounds = bounds;
			image.setBounds(bounds);
		}
		
		public Drawable getImage() {
			return mImage;
		}
		
		public Rect getBounds() {
			return mBounds;
		}
	}
}