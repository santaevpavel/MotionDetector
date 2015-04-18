package fit.nsu.santaev.diplom.activity;

import fit.nsu.santaev.diplom.R;
import fit.nsu.santaev.diplom.fragment.CamFragment;
import fit.nsu.santaev.diplom.motiondetector.BackbroundSubMotionDetector;
import fit.nsu.santaev.diplom.motiondetector.BackbroundSubMotionDetector2;
import fit.nsu.santaev.diplom.motiondetector.CheckHSV;
import fit.nsu.santaev.diplom.motiondetector.FrameSubMotionDetector;
import fit.nsu.santaev.diplom.motiondetector.HystogramDetector;
import fit.nsu.santaev.diplom.motiondetector.OpenCVMotionDetector;
import fit.nsu.santaev.diplom.motiondetector.OpenCVMotionDetector2;
import fit.nsu.santaev.diplom.motiondetector.SmartFrameSubMotionDetector;
import fit.nsu.santaev.diplom.motiondetector.TestMotionDetector;
import fit.nsu.santaev.diplom.sip.CallHelper;
import fit.nsu.santaev.diplom.sip.LinphoneManager;
import fit.nsu.santaev.diplom.utils.MotionDetectorService;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends Activity implements OnClickListener, CallListener{

    private final String USER = "santaev11";
    private final String DEST_USER = "santaev1";
    private final String PSW = "";
    private final String SIPSER = "10.3.71.67";//"sip2sip.info";
    private final String PROXY = "sip:10.3.71.67:5080";//"sip:sip2sip.info";
    private final String IDENTIFY = "sip:santaev11@10.3.71.67";//"sip:santaev11@sip2sip.info";

	private View buttonFrameSub = null;
    private View buttonFrameSubSmart = null;
    private View buttonFrameTest = null;
	private View buttonHystogram = null;
    private View buttonHSV = null;
	private View buttonOpenCV = null;
	private View buttonOpenCV2 = null;
	private View buttonCall = null;
	private MediaPlayer mediaPlayer = new MediaPlayer();

    private boolean isVideoWorking = false;
    private CamFragment camFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dectector_chooser_activity);

		buttonFrameSub = findViewById(R.id.frame_subtract_button);
		buttonFrameTest = findViewById(R.id.frame_test_button);
		buttonFrameSubSmart = findViewById(R.id.frame_subtract_button2);
        buttonHystogram = findViewById(R.id.frame_hystogram);
        buttonHSV = findViewById(R.id.frame_hsv);
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
        buttonHSV.setOnClickListener(this);

		mediaPlayer = MediaPlayer.create(this, R.raw.b);

        LinphoneManager.init(getApplicationContext());
        LinphoneManager.getInstance().register(getApplicationContext(),USER, PSW,
                SIPSER, PROXY, IDENTIFY);
        LinphoneManager.getInstance().registerCallListener(this);
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
        case R.id.frame_hsv:
                setFragment(CamFragment
                        .getInstance(new CheckHSV(100)));
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

	public void warning(Bitmap bmp) {
		mediaPlayer.start();
        MotionDetectorService.detect(bmp);
	}

    public void onMotionDetectionStatusChanged(boolean isStarted, CamFragment camFragment){
        this.camFragment = camFragment;
        isVideoWorking = isStarted;
    }

    @Override
    public void onIncomingCall() {

        if (isVideoWorking){
            if (null != camFragment){
                camFragment.changeVideoStatus(false);
            }
        } else {
            if (null != camFragment){
                camFragment.changeVideoStatus(false);
            }
        }
    }

    @Override
    public void onCallFinished() {
        if (isVideoWorking){
            if (null != camFragment){
                camFragment.changeVideoStatus(true);
            }
        }
    }
}
