package fit.nsu.santaev.diplom.activity;

import fit.nsu.santaev.diplom.R;
import fit.nsu.santaev.diplom.fragment.CamFragment;
import fit.nsu.santaev.diplom.motiondetector.BackbroundSubMotionDetector;
import fit.nsu.santaev.diplom.motiondetector.FrameSubMotionDetector;
import fit.nsu.santaev.diplom.motiondetector.HystogramDetector;
import fit.nsu.santaev.diplom.motiondetector.OpenCVMotionDetector;
import fit.nsu.santaev.diplom.motiondetector.OpenCVMotionDetector2;
import fit.nsu.santaev.diplom.motiondetector.SmartFrameSubMotionDetector;
import fit.nsu.santaev.diplom.motiondetector.TestMotionDetector;
import fit.nsu.santaev.diplom.sip.CallHelper;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends Activity implements OnClickListener {

	private View buttonFrameSub = null;
	private View buttonFrameSubSmart = null;
	private View buttonFrameTest = null;
	private View buttonHystogram = null;
	private View buttonOpenCV = null;
	private View buttonOpenCV2 = null;
	private View buttonCall = null;
	private MediaPlayer mediaPlayer = new MediaPlayer();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dectector_chooser_activity);

		buttonFrameSub = findViewById(R.id.frame_subtract_button);
		buttonFrameTest = findViewById(R.id.frame_test_button);
		buttonFrameSubSmart = findViewById(R.id.frame_subtract_button2);
		buttonHystogram = findViewById(R.id.frame_hystogram);
		buttonCall = findViewById(R.id.call);
		buttonOpenCV = findViewById(R.id.frame_open_cv);
		buttonOpenCV2 = findViewById(R.id.frame_open_cv2);
		
		buttonFrameSub.setOnClickListener(this);
		buttonFrameTest.setOnClickListener(this);
		buttonFrameSubSmart.setOnClickListener(this);
		buttonHystogram.setOnClickListener(this);
		buttonOpenCV.setOnClickListener(this);
		buttonOpenCV2.setOnClickListener(this);
		buttonCall.setOnClickListener(this);

		mediaPlayer = MediaPlayer.create(this, R.raw.b);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.frame_subtract_button:
			setFragment(CamFragment
					.getInstance(new FrameSubMotionDetector(0.5f)));
			break;
		case R.id.frame_test_button:
			setFragment(CamFragment.getInstance(new TestMotionDetector(100)));
			break;
		case R.id.frame_subtract_button2:
			setFragment(CamFragment
					.getInstance(new SmartFrameSubMotionDetector(100)));
			break;
		case R.id.frame_hystogram:
			setFragment(CamFragment
					.getInstance(new BackbroundSubMotionDetector(100)));
			break;
		case R.id.frame_open_cv:
			setFragment(CamFragment
					.getInstance(new OpenCVMotionDetector(100)));
			break;
		case R.id.frame_open_cv2:
			setFragment(CamFragment
					.getInstance(new OpenCVMotionDetector2(100)));
			break;
		case R.id.call:
			Intent i = new Intent(this,  CallActivity.class);
			startActivity(i);
			break;
		default:
			break;
		}
	}

	private void setFragment(Fragment fragment) {
		if (null == fragment) {
			return;
		}
		getFragmentManager().beginTransaction()
				.replace(R.id.container, fragment).addToBackStack(null)
				.commit();
	}

	@Override
	public void onBackPressed() {
		if (getFragmentManager().getBackStackEntryCount() > 0) {
			getFragmentManager().popBackStack();
		} else {
			super.onBackPressed();
		}
	}

	public void warning() {
		mediaPlayer.start();
	}
}
