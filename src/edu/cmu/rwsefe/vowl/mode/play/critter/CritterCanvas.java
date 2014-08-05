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
	private Random mRandomGen = new Random();
	
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
		final int MAX_TRIES = 500;
		
		mCritterCount = critterCount;
		mCritters.clear();
		for (int critterIndex = 0; critterIndex < mCritterCount; critterIndex++) {
			int critterImageIndex = mRandomGen.nextInt(mCritterIds.length);
			Drawable image = getContext().getResources().getDrawable(mCritterIds[critterImageIndex]);
			// Ensure the bounds do not intersect
			boolean intersects;
			Rect bounds = null;
			int tries = 0;
			do {
				intersects = false;
				int left = mRandomGen.nextInt(getWidth() - image.getIntrinsicWidth());
				int top = mRandomGen.nextInt(getHeight() - image.getIntrinsicHeight());
				bounds = new Rect(left, top, 
						left + image.getIntrinsicWidth(), 
						top + image.getIntrinsicHeight());
				
				for (Critter critter : mCritters) {
					intersects |= bounds.intersect(critter.getBounds());
				}
			} while (intersects && tries++ < MAX_TRIES);
			image.setBounds(bounds);
			float rotation = mRandomGen.nextFloat() * 60 - 30;
			float scale = mRandomGen.nextFloat() + 0.5f;
			mCritters.add(new Critter(image, rotation, scale));
		}
		invalidate();
	}
	
	public int getCritterCount() {
		return mCritterCount;
	}
	
	@Override
	public void onLayout(boolean changed, int left, int top, int right, int bottom) {
		generateRandomCritters(3);
	}
	
	@Override
	public void onDraw(Canvas canvas) {
		for (Critter critter : mCritters) {
			canvas.save();
			int centerX = critter.getBounds().left + critter.getBounds().width() / 2;
			int centerY = critter.getBounds().top + critter.getBounds().height() / 2;
            canvas.rotate(Math.abs(critter.getRotation()), centerX, centerY);
			canvas.scale(
					critter.getScale() * ((critter.getRotation() < 0) ? -1 : 1), 
					critter.getScale(), 
					centerX, centerY);
            critter.getImage().draw(canvas);
			canvas.restore();
		}
	}
	
	
	class Critter {
		private Drawable mImage;
		private float mRotation;
		private float mScale;
		
		public Critter(Drawable image, float rotation, float scale) {
			mImage = image;
			mRotation = rotation;
			mScale = scale;
		}
		
		public Drawable getImage() {
			return mImage;
		}
		
		public Rect getBounds() {
			return mImage.getBounds();
		}
		
		public float getRotation() {
			return mRotation;
		}
		
		public float getScale() {
			return mScale;
		}
	}
}