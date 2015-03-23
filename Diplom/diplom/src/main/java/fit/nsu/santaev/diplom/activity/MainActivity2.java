package fit.nsu.santaev.diplom.activity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;

import fit.nsu.santaev.diplom.Graphics;
import fit.nsu.santaev.diplom.R;
import fit.nsu.santaev.diplom.R.id;
import fit.nsu.santaev.diplom.R.layout;
import fit.nsu.santaev.diplom.R.raw;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class MainActivity2 extends Activity implements CvCameraViewListener2 {

	private static final String TAG = "myLogs";
	private CameraBridgeViewBase mOpenCvCameraView;
	private Mat mat;
	private Mat m;
	private long deltaDef = 5 * 1000;
	private long last;
	private Graphics graphics;
	private long time;
	private List<Mat> values = new LinkedList();
	private List<Long> times = new ArrayList<Long>();
	private int maxChange = 10;
	double max = 0;
	private MediaPlayer mediaPlayer = new MediaPlayer();
	SeekBar seekBar;
	private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
		@Override
		public void onManagerConnected(int status) {
			switch (status) {
			case LoaderCallbackInterface.SUCCESS: {
				Log.i(TAG, "OpenCV loaded successfully");
				mOpenCvCameraView.enableView();
			}
				break;
			default: {
				super.onManagerConnected(status);
			}
				break;
			}
		}
	};

	@Override
	public void onResume() {
		super.onResume();
		OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_6, this,
				mLoaderCallback);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "called onCreate");
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_main2);
		mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.HelloOpenCvView);
		mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
		mOpenCvCameraView.setCvCameraViewListener(this);
		graphics = (Graphics) findViewById(R.id.graph);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

		mediaPlayer = MediaPlayer.create(this, R.raw.b);
		seekBar = (SeekBar) findViewById(R.id.seekBar);
		seekBar.setMax(60);
		seekBar.setProgress(20);
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar arg0) {
			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
			}

			@Override
			public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
				maxChange = arg1;
			}
		});

	}

	@Override
	public void onPause() {
		super.onPause();
		if (mOpenCvCameraView != null)
			mOpenCvCameraView.disableView();
	}

	public void onDestroy() {
		super.onDestroy();
		if (mOpenCvCameraView != null)
			mOpenCvCameraView.disableView();
	}

	public void onCameraViewStarted(int width, int height) {
	}

	public void onCameraViewStopped() {
	}

	public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
		Mat frame = inputFrame.gray();
		if (null == mat) {
			mat = frame;
			time = System.currentTimeMillis();
			last = time;
			return frame;
		}

		boolean b = false;
		if (System.currentTimeMillis() - last < 300) {
			b = true;
		}
		m = new Mat();
		last = System.currentTimeMillis();
		Core.absdiff(mat, frame, m);
		// mat = frame;
		double d[] = Core.sumElems(m).val;
		double d2 = d[0] / m.rows() / m.cols();
		// graphics.add(d2, System.currentTimeMillis() - time);

		if (max < d2) {
			max = d2;
		}
		Log.d("myLogs", "1 " + values.size());
		if (maxChange < d2) {
			mediaPlayer.start();
		}
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				graphics.invalidate();
			}
		});
		if (b) {
			values.add(0, frame.clone());
			times.add(0, System.currentTimeMillis() - time);
		}
		if (!values.isEmpty()) {
			int num = getCompareMat();
			mat = values.get(num);
		}

		/*
		 * if (time - last > 3 * 1000){ mat = frame.clone(); last = time; }
		 */

		time = System.currentTimeMillis();
		return m;
	}

	private int getCompareMat() {
		long sum = 0;
		for (int i = 0; i < values.size(); i++) {
			sum += times.get(i);
			if (sum >= deltaDef) {
				for (int j = i + 1; j < values.size(); j++) {
					values.get(j).release();
				}
				values = values.subList(0, i);
				times = times.subList(0, i);
				return i - 1;
			}
		}
		if (values.size() > 0) {
			return values.size() - 1;
		}
		return 0;
	}
}
