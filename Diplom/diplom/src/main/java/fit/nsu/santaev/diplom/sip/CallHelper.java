package fit.nsu.santaev.diplom.sip;

import java.nio.ByteBuffer;

import org.linphone.core.LinphoneAddress;
import org.linphone.core.LinphoneAuthInfo;
import org.linphone.core.LinphoneCall;
import org.linphone.core.LinphoneCallStats;
import org.linphone.core.LinphoneChatMessage;
import org.linphone.core.LinphoneChatRoom;
import org.linphone.core.LinphoneContent;
import org.linphone.core.LinphoneCore;
import org.linphone.core.LinphoneCoreException;
import org.linphone.core.LinphoneCoreFactory;
import org.linphone.core.LinphoneCoreFactoryImpl;
import org.linphone.core.LinphoneCoreListener;
import org.linphone.core.LinphoneEvent;
import org.linphone.core.LinphoneFriend;
import org.linphone.core.LinphoneInfoMessage;
import org.linphone.core.LinphoneProxyConfig;
import org.linphone.core.PublishState;
import org.linphone.core.SubscriptionState;
import org.linphone.core.LinphoneCall.State;
import org.linphone.core.LinphoneCore.EcCalibratorStatus;
import org.linphone.core.LinphoneCore.GlobalState;
import org.linphone.core.LinphoneCore.RegistrationState;
import org.linphone.core.LinphoneCore.RemoteProvisioningState;
import org.linphone.mediastream.video.capture.hwconf.AndroidCameraConfiguration;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class CallHelper {
	
	public static void call(final Context context, String name, String dest, String psw, String server, String proxy, String iden){
		//LinphoneCoreFactory.setFactoryClassName("LinphoneCoreFactoryImpl");
		LinphoneCoreFactory linphoneCoreFactory = LinphoneCoreFactory.instance();///LinphoneCoreFactory.instance();
		linphoneCoreFactory = LinphoneCoreFactory.instance();
		try {
			LinphoneCore core = linphoneCoreFactory.createLinphoneCore(new LinphoneCoreListener() {
				
				@Override
				public void transferState(LinphoneCore lc, LinphoneCall call,
						State new_call_state) {
					Log.d("TAG!!!", "transferState  " + new_call_state); 
				}
				
				@Override
				public void textReceived(LinphoneCore lc, LinphoneChatRoom cr,
						LinphoneAddress from, String message) {
					Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
				}
				
				@Override
				public void subscriptionStateChanged(LinphoneCore lc, LinphoneEvent ev,
						SubscriptionState state) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void show(LinphoneCore lc) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void registrationState(LinphoneCore lc, LinphoneProxyConfig cfg,
						RegistrationState cstate, String smessage) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void publishStateChanged(LinphoneCore lc, LinphoneEvent ev,
						PublishState state) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void notifyReceived(LinphoneCore lc, LinphoneEvent ev,
						String eventName, LinphoneContent content) {
					Log.d("TAG!!!", "notifyReceived " + eventName);
				}
				
				@Override
				public void notifyReceived(LinphoneCore lc, LinphoneCall call,
						LinphoneAddress from, byte[] event) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void notifyPresenceReceived(LinphoneCore lc, LinphoneFriend lf) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void newSubscriptionRequest(LinphoneCore lc, LinphoneFriend lf,
						String url) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void messageReceived(LinphoneCore lc, LinphoneChatRoom cr,
						LinphoneChatMessage message) {
					Toast.makeText(context, message.getText(), Toast.LENGTH_SHORT).show();;
				}
				
				@Override
				public void isComposingReceived(LinphoneCore lc, LinphoneChatRoom cr) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void infoReceived(LinphoneCore lc, LinphoneCall call,
						LinphoneInfoMessage info) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void globalState(LinphoneCore lc, GlobalState state, String message) {
					Log.d("TAG!!!", "globalState " + message);
				}
				
				@Override
				public int fileTransferSend(LinphoneCore lc, LinphoneChatMessage message,
						LinphoneContent content, ByteBuffer buffer, int size) {
					// TODO Auto-generated method stub
					return 0;
				}
				
				@Override
				public void fileTransferRecv(LinphoneCore lc, LinphoneChatMessage message,
						LinphoneContent content, byte[] buffer, int size) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void fileTransferProgressIndication(LinphoneCore lc,
						LinphoneChatMessage message, LinphoneContent content, int progress) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void ecCalibrationStatus(LinphoneCore lc, EcCalibratorStatus status,
						int delay_ms, Object data) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void dtmfReceived(LinphoneCore lc, LinphoneCall call, int dtmf) {
					Log.d("TAG!!!", "dtmfReceived");
				}
				
				@Override
				public void displayWarning(LinphoneCore lc, String message) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void displayStatus(LinphoneCore lc, String message) {
					Log.d("TAG!!!", "displayStatus");
				}
				
				@Override
				public void displayMessage(LinphoneCore lc, String message) {
					Log.d("TAG!!!", "displayMessage");
				}
				
				@Override
				public void configuringStatus(LinphoneCore lc,
						RemoteProvisioningState state, String message) {
					Log.d("TAG!!!", "configuringStatus " + message);
				}
				
				@Override
				public void callStatsUpdated(LinphoneCore lc, LinphoneCall call,
						LinphoneCallStats stats) {
					Log.d("TAG!!!", "callStatsUpdated");
				}
				
				@Override
				public void callState(LinphoneCore lc, LinphoneCall call, State cstate,
						String message) {
					Log.d("TAG!!!", "callState " + message);
					lc.enableVideo(true, true);
					call.enableCamera(true);
				}
				
				@Override
				public void callEncryptionChanged(LinphoneCore lc, LinphoneCall call,
						boolean encrypted, String authenticationToken) {
					Log.d("TAG!!!", "callEncryptionChanged");
				}
				
				@Override
				public void authInfoRequested(LinphoneCore lc, String realm,
						String username, String Domain) {
					Log.d("TAG!!!", "authInfoRequested");
				}
			}, context);
			//LinphoneAddress linphoneAddress = linphoneCoreFactory.createLinphoneAddress("santaev1", "sip2sip.info", "santaev");
			//core.addProxyConfig(core.createProxyConfig("sip:santaev11@sip2sip.info", "sip:sip2sip.info", "", true));
			//core.addAuthInfo(linphoneCoreFactory.createAuthInfo("santaev11", "santaev11", "", "sip2sip.info"));
			LinphoneAddress linphoneAddress = linphoneCoreFactory.createLinphoneAddress(dest, server, "santaev11");
			core.enableVideo(true, true);
			core.setVideoDevice(AndroidCameraConfiguration.retrieveCameras()[0].id);
			core.addProxyConfig(core.createProxyConfig(iden, proxy, "", true));
			core.addAuthInfo(linphoneCoreFactory.createAuthInfo(name, psw, "", server));
			core.enableSpeaker(true);
			core.enableVideo(true, true);
			LinphoneCall call = core.invite(linphoneAddress);
			call.enableCamera(true);
			call.enableEchoCancellation(true);
			int i = 0;
			while(i < 1000){
				core.iterate();
				i++;
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			
		} catch (LinphoneCoreException e) {
			e.printStackTrace();
		}
	}
	
	public static void callAsync(final Context context, final String name, final String dest,
			final String psw, final String server, final String proxy, final String iden){
		Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				call(context, name, dest, psw, server, proxy, iden);
			}
			
		});
		thread.start();
	}
	
	public static void register(final Context context, String name, String dest, String psw, String server, String proxy, String iden){
		LinphoneCoreFactory linphoneCoreFactory = LinphoneCoreFactory.instance();
		linphoneCoreFactory = LinphoneCoreFactory.instance();
		//LinphoneCore core = linphoneCoreFactory.createLinphoneCore(
	}
}
