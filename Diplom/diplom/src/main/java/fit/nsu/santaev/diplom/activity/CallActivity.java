package fit.nsu.santaev.diplom.activity;

import org.linphone.mediastream.video.AndroidVideoWindowImpl;

import fit.nsu.santaev.diplom.R;
import fit.nsu.santaev.diplom.sip.CallHelper;
import fit.nsu.santaev.diplom.sip.LinphoneManager;
import android.app.Activity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class CallActivity extends Activity{

	private final String USER = "santaev11";
	private final String DEST_USER = "santaev1";
	private final String PSW = "santaev11";
	private final String SIPSER = "sip2sip.info";
	private final String PROXY = "sip:sip2sip.info";
	private final String IDENTIFY = "sip:santaev11@sip2sip.info";
	
	private EditText userEdit;
	private EditText userDest;
	private EditText userPassword;
	private EditText userSipServer;
	private EditText userProxy;
	private EditText userIdentify;
	
	private View callButton;
	private View regButton;
	private View call2Button;
	private View initButton;
	
	private SurfaceView mVideoView;
	private SurfaceView mCaptureView;
	
	private final AndroidVideoWindowImpl.VideoWindowListener videoWindowListener = new AndroidVideoWindowImpl.VideoWindowListener() {
        @Override
        public void onVideoRenderingSurfaceReady(final AndroidVideoWindowImpl vw, final SurfaceView surface) {
            LinphoneManager.getLc().setVideoWindow(vw);
            mVideoView = surface;
        }

        @Override
        public void onVideoRenderingSurfaceDestroyed(final AndroidVideoWindowImpl vw) {
            LinphoneManager.getLc().setVideoWindow(null);
        }

        @Override
        public void onVideoPreviewSurfaceReady(final AndroidVideoWindowImpl vw, final SurfaceView surface) {
            mCaptureView = surface;
            LinphoneManager.getLc().setPreviewWindow(mCaptureView);
        }

        @Override
        public void onVideoPreviewSurfaceDestroyed(final AndroidVideoWindowImpl vw) {
            LinphoneManager.getLc().setPreviewWindow(null);
        }
    };
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.call_activity);
		userEdit = (EditText) findViewById(R.id.user_edit_text);
		userPassword = (EditText) findViewById(R.id.pasword_edit_text);
		userSipServer = (EditText) findViewById(R.id.sip_edit_text);
		userProxy = (EditText) findViewById(R.id.proxy_edit_text);
		userIdentify = (EditText) findViewById(R.id.identify_edit_text);
		userDest = (EditText) findViewById(R.id.dest_user);
		callButton = findViewById(R.id.buttonCall);
		regButton = findViewById(R.id.register);
		call2Button = findViewById(R.id.buttonCall2);
		initButton = findViewById(R.id.buttonInit);
		userEdit.setText(USER);
		userPassword.setText(PSW);
		userSipServer.setText(SIPSER);
		userProxy.setText(PROXY);
		userIdentify.setText(IDENTIFY);
		userDest.setText(DEST_USER);
		
		LinphoneManager.init(getApplicationContext());
		LinphoneManager.getInstance().register(getApplicationContext(), userEdit.getText().toString(), userPassword.getText().toString(), 
				userSipServer.getText().toString(), userProxy.getText().toString(), userIdentify.getText().toString());
		
		
		
		callButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//CallHelper.callAsync(CallActivity.this,  userEdit.getText().toString(), userDest.getText().toString(), userPassword.getText().toString(), 
				//		userSipServer.getText().toString(), userProxy.getText().toString(), userIdentify.getText().toString());
				//LinphoneManager.getInstance().call(userEdit.getText().toString(), userDest.getText().toString(), userPassword.getText().toString(),
				//		userSipServer.getText().toString(), userProxy.getText().toString(), userIdentify.getText().toString());
			}
		});
		/*regButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				LinphoneManager.getInstance().register(getApplicationContext(), userEdit.getText().toString(), userPassword.getText().toString(), 
						userSipServer.getText().toString(), userProxy.getText().toString(), userIdentify.getText().toString());				
			}
		});
		
		call2Button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CallHelper.callAsync(CallActivity.this,  userEdit.getText().toString(), userDest.getText().toString(), userPassword.getText().toString(), 
						userSipServer.getText().toString(), userProxy.getText().toString(), userIdentify.getText().toString());
			}
		});
		
		initButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				LinphoneManager.init(getApplicationContext());
			}
		});*/
		//LinphoneManager.getInstance()./register(getApplicationContext(), userEdit.getText().toString(), userPassword.getText().toString(), 
		//		userSipServer.getText().toString(), userProxy.getText().toString(), userIdentify.getText().toString());
		
		
		LinphoneManager.getLc().enableSpeaker(true);
        LinphoneManager.getLc().enableVideo(true, true);
        
        
        mVideoView = (SurfaceView)findViewById(R.id.surfaceView1);
        mCaptureView = (SurfaceView)findViewById(R.id.surfaceView2);
        
	}
	
	
	
	
}
