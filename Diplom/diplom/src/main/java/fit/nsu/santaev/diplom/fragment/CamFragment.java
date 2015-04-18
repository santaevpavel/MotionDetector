package fit.nsu.santaev.diplom.fragment;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import fit.nsu.santaev.diplom.Graphics;
import fit.nsu.santaev.diplom.R;
import fit.nsu.santaev.diplom.activity.MainActivity;
import fit.nsu.santaev.diplom.motiondetector.IMotionDetector;
import fit.nsu.santaev.diplom.motiondetector.ResultFrame;
import fit.nsu.santaev.diplom.utils.MotionDetectorService;

import android.app.Fragment;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.NetworkInfo.DetailedState;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class CamFragment extends Fragment implements CvCameraViewListener2{

	private static final String TAG = "myLogs";

	public static CamFragment getInstance(IMotionDetector detector) {
		CamFragment camFragment = new CamFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable("Det", detector);
		camFragment.setArguments(bundle);
		return camFragment;
	}

	private CameraBridgeViewBase mOpenCvCameraView = null;
	private Graphics graphic = null;
	private SeekBar trasholdBar;
	private IMotionDetector motionDetector;
	private boolean showGrapchics = false;
	private int frames = 0;

	private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(
			getActivity()) {
		@Override
		public void onManagerConnected(int status) {
			switch (status) {
			case LoaderCallbackInterface.SUCCESS: {
				Log.i(TAG, "OpenCV loaded successfully");
				mOpenCvCameraView.enableView();
				//System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
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
	public void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "called onCreate");
		super.onCreate(savedInstanceState);
		getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		MotionDetectorService.init();
		motionDetector = (IMotionDetector) getArguments().getSerializable("Det");
        ((MainActivity)getActivity()).onMotionDetectionStatusChanged(true, this);
	}

	@Override
	public void onResume() {
		OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_5, getActivity(),
				mLoaderCallback);
		super.onResume();
	}

    @Override
    public void onDetach() {
        changeVideoStatus(false);
        ((MainActivity)getActivity()).onMotionDetectionStatusChanged(false, this);
        super.onDetach();
    }

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View content = View.inflate(getActivity(), R.layout.cam_fragment, null);
		mOpenCvCameraView = (CameraBridgeViewBase) content.findViewById(R.id.HelloOpenCvView);
		mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
		mOpenCvCameraView.setCvCameraViewListener(this);
		graphic = (Graphics) content.findViewById(R.id.graph);
		trasholdBar = (SeekBar) content.findViewById(R.id.seekBar);
		trasholdBar.setMax(100);
		trasholdBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar arg0) {				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
			}
			
			@Override
			public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
				motionDetector.setTrashold((double) arg1 / 100);
				graphic.setLine(motionDetector.getMaxTreshold());
			}
		});
		getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		
		trasholdBar.setProgress(99);
		
		mOpenCvCameraView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				//showGrapchics = !showGrapchics;
				motionDetector.func1(getActivity());
			}
		});
        mOpenCvCameraView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showGrapchics = !showGrapchics;
                return true;
            }
        });
		return content;
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
		ResultFrame res = motionDetector.processFrame(inputFrame);
		Mat frame = res.frame;
		if (showGrapchics){
			graphic.add(res.value);
		}
		if (motionDetector.getMaxTreshold() < res.value){
            Bitmap bmp = Bitmap.createBitmap(frame.cols(), frame.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(frame, bmp);
			((MainActivity)getActivity()).warning(bmp);
		}
                getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				graphic.invalidate();
			}
		});
        frames++;
        //Imgproc.
        if (10 <= frames){
            Log.e("GCGC", "GCGC");
            System.gc();
            frames = 0;
        }
		return frame;
	}

    public void changeVideoStatus(boolean value){
        if (value){
            mOpenCvCameraView.enableView();
        } else {
            mOpenCvCameraView.disableView();
        }
    }
}
