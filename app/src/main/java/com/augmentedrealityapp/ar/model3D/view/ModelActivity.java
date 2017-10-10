package com.augmentedrealityapp.ar.model3D.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;


import com.augmentedrealityapp.R;
import com.augmentedrealityapp.ar.model3D.services.ExampleSceneLoader;
import com.augmentedrealityapp.ar.model3D.services.SceneLoader;
import com.augmentedrealityapp.ar.util.Utils;

import java.io.File;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This activity represents the container for our 3D viewer.
 * 
 * @author andresoviedo
 */
 public class ModelActivity extends Activity implements SurfaceHolder.Callback, Camera.PreviewCallback{

	private String paramAssetDir;
	private String paramAssetFilename;
	/**
	 * The file to load. Passed as input parameter
	 */
	private String paramFilename;
	/**
	 * Enter into Android Immersive mode so the renderer is full screen or not
	 */
	private boolean immersiveMode = true;
	/**
	 * Background GL clear color. Default is light gray
	 */
	private float[] backgroundColor = new float[]{0.2f, 0.2f, 0.2f, 1.0f};

	private GLSurfaceView gLView;

	private SceneLoader scene;

	private Handler handler;
	Camera mCamera;
	SurfaceView mView;
	SurfaceHolder mHolder;

	int mX, mY;
	private int pixelFormat;
	private ViewGroup mFrame;
	private byte[] mData;
	private int[] mDataRGB8888;

	ModelRenderer modelRenderer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Try to get input parameters
		Bundle b = getIntent().getExtras();
		if (b != null) {
			this.paramAssetDir = b.getString("assetDir");
			this.paramAssetFilename = b.getString("assetFilename");
			this.paramFilename = b.getString("uri");
			this.immersiveMode = "true".equalsIgnoreCase(b.getString("immersiveMode"));
			try{
		//		String[] backgroundColors = b.getString("backgroundColor").split(" ");
		//		backgroundColor[0] = Float.parseFloat(backgroundColors[0]);
		//		backgroundColor[1] = Float.parseFloat(backgroundColors[1]);
			//	backgroundColor[2] = Float.parseFloat(backgroundColors[2]);
			//	backgroundColor[3] = Float.parseFloat(backgroundColors[3]);
			}catch(Exception ex){
				// Assuming default background color
			}
		}
		Log.i("Renderer", "Params: assetDir '" + paramAssetDir + "', assetFilename '" + paramAssetFilename + "', uri '"
				+ paramFilename + "'");

		handler = new Handler(getMainLooper());



		modelRenderer=new ModelRenderer(new ModelSurfaceView(this));
		gLView = new ModelSurfaceView(this);


setContentView(R.layout.activity_model);

//		CameraView cameraView = new CameraView( this );

		mFrame = (ViewGroup)this.findViewById(R.id.relate);
		mFrame.addView(gLView);

		mView = new SurfaceView(this);
		mHolder = mView.getHolder();
		mHolder.addCallback( this );

		mFrame.addView(mView);


		// Create our 3D sceneario
		if (paramFilename == null && paramAssetFilename == null) {
			scene = new ExampleSceneLoader(this);
		} else {
			scene = new SceneLoader(this);
		}
		scene.init();


		setupActionBar();

		// TODO: Alert user when there is no multitouch support (2 fingers). He won't be able to rotate or zoom for
		// example
		Utils.printTouchCapabilities(getPackageManager());

		setupOnSystemVisibilityChangeListener();
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		// if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
		// getActionBar().setDisplayHomeAsUpEnabled(true);
		// }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.model, menu);
		return true;
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupOnSystemVisibilityChangeListener() {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
			return;
		}
		getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
			@Override
			public void onSystemUiVisibilityChange(int visibility) {
				// Note that system bars will only be "visible" if none of the
				// LOW_PROFILE, HIDE_NAVIGATION, or FULLSCREEN flags are set.
				if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
					// TODO: The system bars are visible. Make any desired
					// adjustments to your UI, such as showing the action bar or
					// other navigational controls.
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
						hideSystemUIDelayed(3000);
					}
				} else {
					// TODO: The system bars are NOT visible. Make any desired
					// adjustments to your UI, such as hiding the action bar or
					// other navigational controls.
				}
			}
		});
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
				if (immersiveMode) hideSystemUIDelayed(5000);
			}
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.model_toggle_wireframe:
			scene.toggleWireframe();
			break;
		case R.id.model_toggle_boundingbox:
			scene.toggleBoundingBox();
			break;
		case R.id.model_toggle_textures:
			scene.toggleTextures();
			break;
		case R.id.model_toggle_lights:
			scene.toggleLighting();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void hideSystemUIDelayed(long millis) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
			return;
		}
		handler.postDelayed(new Runnable() {
			public void run() {
				hideSystemUI();
			}
		}, millis);
	}

	private void hideSystemUI() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			hideSystemUIKitKat();
		} else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {

			hideSystemUIJellyBean();
		}
	}

	// This snippet hides the system bars.
	@TargetApi(Build.VERSION_CODES.KITKAT)
	private void hideSystemUIKitKat() {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
			return;
		}
		// Set the IMMERSIVE flag.
		// Set the content to appear under the system bars so that the content
		// doesn't resize when the system bars hide and show.
		final View decorView = getWindow().getDecorView();
		decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
				| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
				| View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
				| View.SYSTEM_UI_FLAG_IMMERSIVE);
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	private void hideSystemUIJellyBean() {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
			return;
		}
		final View decorView = getWindow().getDecorView();
		decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
				| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
				| View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LOW_PROFILE);
	}

	// This snippet shows the system bars. It does this by removing all the flags
	// except for the ones that make the content appear under the system bars.
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	private void showSystemUI() {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
			return;
		}
		final View decorView = getWindow().getDecorView();
		decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
				| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
	}

	public File getParamFile() {
		return getParamFilename() != null ? new File(getParamFilename()) : null;
	}

	public String getParamAssetDir() {
		return paramAssetDir;
	}

	public String getParamAssetFilename() {
		return paramAssetFilename;
	}

	public String getParamFilename() {
		return paramFilename;
	}

	public float[] getBackgroundColor(){
		return backgroundColor;
	}

	public SceneLoader getScene() {
		return scene;
	}

	public GLSurfaceView getgLView() {
		return gLView;
	}

	@Override
	protected void onResume() {
		super.onResume();

		// Open the default i.e. the first rear facing camera.
		mCamera = Camera.open();

		mCamera.startPreview();

	}

	@Override
	protected void onPause() {
		super.onPause();

		mCamera.setPreviewCallback(null);
		mCamera.stopPreview();
		mCamera.release();
		mCamera = null;

		//if( nv21Decoder != null)
		//	nv21Decoder.releaseThread();

	}


	@Override

	public void surfaceCreated(SurfaceHolder holder) {
		try {
			if (mCamera != null)
				mCamera.setPreviewDisplay(holder);
		}
		catch (Exception exception) {}

	}
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		if( mCamera != null ) {
			Camera.Parameters parameters = mCamera.getParameters();

			pixelFormat = parameters.getPreviewFormat();


			mX = mFrame.getWidth();
			mY = mFrame.getHeight();
			//mX = (mX/4) * 4;
			//mY = (mY/4) * 4;
			//parameters.setPreviewSize( mX, mY);
			//mCamera.setParameters(parameters);

			parameters = mCamera.getParameters();
			Camera.Size size = parameters.getPreviewSize();

			mX = size.width;
			mY = size.height;

			//nv21Decoder = new AsynсNV21Decoder(mX, mY);
			//nv21Decoder.start();
            mCamera.setDisplayOrientation(90);
			mData = new byte[mX * mY * 3 / 2];
			mCamera.addCallbackBuffer(mData);
			mCamera.setPreviewCallback(this);

			mDataRGB8888 = new int[mX * mY];
		}
	}


	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
	}

	@Override
	public void onPreviewFrame(byte[] data, Camera camera) {
		// System.arraycopy(data, 0, mData, 0, data.length);

		Camera.Parameters parameters = mCamera.getParameters();
		Camera.Size s = parameters.getPreviewSize();

		//if( nv21Decoder != null ) nv21Decoder.processBuffer( data );
	//	modelRenderer.drawFrame(s.width, s.height, data);
		gLView.requestRender();

/*
		 System.arraycopy(data, 0, mData , 0, s.width * s.height * 3 / 2);


		 Bitmap bmp = getBitmapFromNV21(mData, s.width, s.height );
		 mRenderer.loadTexture(s.width, s.height, bmp);
		 mGLSurfaceView.requestRender();
*/
		mCamera.addCallbackBuffer(mData);

	}

	public Bitmap getBitmapFromNV21(byte[] data, int width, int height) {

		int grey = 0;
		//int pixelsNumber = width * height;
		//int[] colors = new int[pixelsNumber];

		//for (int pixel = 0; pixel < pixelsNumber; pixel++) {
		//        grey = data[pixel] & 0xff;
		//        colors[pixel] = 0xff000000 | (grey * 0x00010101);
		//}


		//decodeYUV(mDataRGB8888, data, width, height);
		decodeYUV420SP(mDataRGB8888, data, width, height);

		Bitmap bitmap ;
		//if( counter % 2 == 0 ) {
		//   bitmap = loadDemoBitmap();
		//}  else {
        	/*
            for (int i = 0; i < width * height ; i++)
            {
            	mDataRGB8888[i] = 0xFFFF0000;
            }
            */
		bitmap = Bitmap.createBitmap(mDataRGB8888, width, height, Bitmap.Config.ARGB_8888);
		//}
		//counter++;

		return bitmap;
	}

	public static void YUV_NV21_TO_RGB(int[] argb, byte[] yuv, int width, int height) {
		final int frameSize = width * height;

		final int ii = 0;
		final int ij = 0;
		final int di = +1;
		final int dj = +1;

		int a = 0;
		for (int i = 0, ci = ii; i < height; ++i, ci += di) {
			for (int j = 0, cj = ij; j < width; ++j, cj += dj) {
				int y = (0xff & ((int) yuv[ci * width + cj]));
				int v = (0xff & ((int) yuv[frameSize + (ci >> 1) * width + (cj & ~1) + 0]));
				int u = (0xff & ((int) yuv[frameSize + (ci >> 1) * width + (cj & ~1) + 1]));
				y = y < 16 ? 16 : y;

				int r = (int) (1.164f * (y - 16) + 1.596f * (v - 128));
				int g = (int) (1.164f * (y - 16) - 0.813f * (v - 128) - 0.391f * (u - 128));
				int b = (int) (1.164f * (y - 16) + 2.018f * (u - 128));

				r = r < 0 ? 0 : (r > 255 ? 255 : r);
				g = g < 0 ? 0 : (g > 255 ? 255 : g);
				b = b < 0 ? 0 : (b > 255 ? 255 : b);

				argb[a++] = 0xff000000 | (r << 16) | (g << 8) | b;
			}
		}
	}

	public static void decodeYUV(int[] out, byte[] fg, int width, int height) throws NullPointerException, IllegalArgumentException {

		final int sz = width * height;
		if(out == null) throw new NullPointerException("buffer 'out' is null");
		if(out.length < sz) throw new IllegalArgumentException("buffer 'out size " + out.length + " < minimum " + sz);
		if(fg == null) throw new NullPointerException("buffer 'fg' is null");
		if(fg.length < sz) throw new IllegalArgumentException("buffer 'fg' size " + fg.length + " < minimum " + sz * 3/ 2);
		int i, j;
		int Y, Cr = 0, Cb = 0;
		for(j = 0; j < height; j++) {
			int pixPtr = j * width;
			final int jDiv2 = j >> 1;
			for(i = 0; i < width; i++) {
				Y = fg[pixPtr]; if(Y < 0) Y += 255;
				if((i & 0x1) != 1) {
					final int cOff = sz + jDiv2 * width + (i >> 1) * 2;
					Cb = fg[cOff];
					if(Cb < 0) Cb += 127; else Cb -= 128;
					Cr = fg[cOff + 1];
					if(Cr < 0) Cr += 127; else Cr -= 128;
				}
				int R = Y + Cr + (Cr >> 2) + (Cr >> 3) + (Cr >> 5);
				if(R < 0) R = 0; else if(R > 255) R = 255;
				int G = Y - (Cb >> 2) + (Cb >> 4) + (Cb >> 5) - (Cr >> 1) + (Cr >> 3) + (Cr >> 4) + (Cr >> 5);
				if(G < 0) G = 0; else if(G > 255) G = 255;
				int B = Y + Cb + (Cb >> 1) + (Cb >> 2) + (Cb >> 6);
				if(B < 0) B = 0; else if(B > 255) B = 255;
				out[pixPtr++] = 0xff000000 + (B << 16) + (G << 8) + R;
			}
		}
	}

	//Method from Ketai project! Not mine! See below...
	void decodeYUV420SP(int[] rgb, byte[] yuv420sp, int width, int height) {

		final int frameSize = width * height;

		for (int j = 0, yp = 0; j < height; j++) {       int uvp = frameSize + (j >> 1) * width, u = 0, v = 0;
			for (int i = 0; i < width; i++, yp++) {
				int y = (0xff & ((int) yuv420sp[yp])) - 16;
				if (y < 0)
					y = 0;
				if ((i & 1) == 0) {
					v = (0xff & yuv420sp[uvp++]) - 128;
					u = (0xff & yuv420sp[uvp++]) - 128;
				}

				int y1192 = 1192 * y;
				int r = (y1192 + 1634 * v);
				int g = (y1192 - 833 * v - 400 * u);
				int b = (y1192 + 2066 * u);

				if (r < 0)                  r = 0;               else if (r > 262143)
					r = 262143;
				if (g < 0)                  g = 0;               else if (g > 262143)
					g = 262143;
				if (b < 0)                  b = 0;               else if (b > 262143)
					b = 262143;

				rgb[yp] = 0xff000000 | ((r << 6) & 0xff0000) | ((g >> 2) & 0xff00) | ((b >> 10) & 0xff);
			}
		}
	}

	class AsynсNV21Decoder extends Thread {

		private int width;
		private int height;
		private byte [] buffer;
		int[] colors;
		//private byte [] buffer2;
		//private byte [] buffer3;

		private boolean isFreeBuffer;
		private boolean quitFromThrea = false;
		final Lock lock = new ReentrantLock();
		final Condition readyToProcess  = lock.newCondition();


		public AsynсNV21Decoder(int width, int height) {
			this.width = width;
			this.height = height;
			buffer = new byte[width * height * 3 /2];
			isFreeBuffer = true;
			colors = new int [width * height];
		}


		public void processBuffer( byte[] buf) {
			lock.lock();
			if( isFreeBuffer )
			{
				System.arraycopy(buf, 0, buffer, 0, width * height * 3 / 2);
				//buffer = buf;
				//isFreeBuffer = false;
				readyToProcess.signal();
			}
			lock.unlock();
		}

		@Override
		public synchronized void start () {
			quitFromThrea = false;
			isFreeBuffer = true;
			super.start();
		}

		public void releaseThread() {
			lock.lock();
			if( isFreeBuffer )
			{
				quitFromThrea = true;
				readyToProcess.signal();
			}
			lock.unlock();

		}

		//ThreeBuffer
		@Override
		public void run() {

			while(!quitFromThrea)
			{
				lock.lock();
				try {
					readyToProcess.await();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				isFreeBuffer = false;
				lock.unlock();

				if( quitFromThrea ) break;

		Bitmap bmp = getBitmapFromNV21(buffer, width, height );
			//modelRenderer.loadTexture(width, height, bmp);

				gLView.requestRender();
   	    		/*
   	    		runOnUiThread ( new Runnable() {
						@Override
						public void run() {
   	    	   	    		mGLSurfaceView.requestRender();

						}
   	    		}
   	    		);
   	    		*/


				lock.lock();
				isFreeBuffer = true;
				lock.unlock();


			}

		}

	}









}
