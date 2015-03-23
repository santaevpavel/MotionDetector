package fit.nsu.santaev.diplom.sip;

import java.nio.ByteBuffer;
import java.util.Timer;
import java.util.TimerTask;

import org.linphone.core.LinphoneAddress;
import org.linphone.core.LinphoneAuthInfo;
import org.linphone.core.LinphoneCall;
import org.linphone.core.LinphoneCallParams;
import org.linphone.core.LinphoneCallStats;
import org.linphone.core.LinphoneChatMessage;
import org.linphone.core.LinphoneChatRoom;
import org.linphone.core.LinphoneContent;
import org.linphone.core.LinphoneCore;
import org.linphone.core.LinphoneCoreException;
import org.linphone.core.LinphoneCoreFactory;
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
import android.os.Handler;
import android.util.Log;

public class LinphoneManager {

	private static LinphoneManager instance = null;
	
	
	private LinphoneCoreFactory linphoneCoreFactory;
	private LinphoneCore linphoneCore;
	
	private LinphoneManager(Context context) throws LinphoneCoreException{
		linphoneCoreFactory = LinphoneCoreFactory.instance();
		linphoneCore = linphoneCoreFactory.createLinphoneCore(new LinphoneCoreListener() {
			
			@Override
			public void transferState(LinphoneCore lc, LinphoneCall call,
					State new_call_state) {
				Log.d("LINMANAGER", "transferState " + new_call_state);
			}
			
			@Override
			public void textReceived(LinphoneCore lc, LinphoneChatRoom cr,
					LinphoneAddress from, String message) {
				Log.d("LINMANAGER", "textReceived " + message);
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
				Log.d("LINMANAGER", "registrationState " + cstate + " " + smessage);
			}
			
			@Override
			public void publishStateChanged(LinphoneCore lc, LinphoneEvent ev,
					PublishState state) {
				Log.d("LINMANAGER", "publishStateChanged " + state + " " + ev);
			}
			
			@Override
			public void notifyReceived(LinphoneCore lc, LinphoneEvent ev,
					String eventName, LinphoneContent content) {
				Log.d("LINMANAGER", "notifyReceived " + eventName);
			}
			
			@Override
			public void notifyReceived(LinphoneCore lc, LinphoneCall call,
					LinphoneAddress from, byte[] event) {
				Log.d("LINMANAGER", "notifyReceived " + from);
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
				Log.d("LINMANAGER", "messageReceived " + message);
			}
			
			@Override
			public void isComposingReceived(LinphoneCore lc, LinphoneChatRoom cr) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void infoReceived(LinphoneCore lc, LinphoneCall call,
					LinphoneInfoMessage info) {
				Log.d("LINMANAGER", "infoReceived " + info);
			}
			
			@Override
			public void globalState(LinphoneCore lc, GlobalState state, String message) {
				Log.d("LINMANAGER", "globalState " + message + " " + state);
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
				Log.d("LINMANAGER", "dtmfReceived " + dtmf);
			}
			
			@Override
			public void displayWarning(LinphoneCore lc, String message) {
				Log.d("LINMANAGER", "displayWarning " + message);
			}
			
			@Override
			public void displayStatus(LinphoneCore lc, String message) {
				Log.d("LINMANAGER", "displayStatus " + message);
			}
			
			@Override
			public void displayMessage(LinphoneCore lc, String message) {
				Log.d("LINMANAGER", "displayMessage " + message);
			}
			
			@Override
			public void configuringStatus(LinphoneCore lc,
					RemoteProvisioningState state, String message) {
				Log.d("LINMANAGER", "configuringStatus " + state + " "+ message);
			}
			
			@Override
			public void callStatsUpdated(LinphoneCore lc, LinphoneCall call,
					LinphoneCallStats stats) {
				Log.d("LINMANAGER", "callStatsUpdated " + stats);
			}
			
			@Override
			public void callState(LinphoneCore lc, LinphoneCall call, State cstate,
					String message) {
				Log.d("LINMANAGER", "callState " + cstate + " " + message);
				if (cstate.fromInt(cstate.value()) == State.IncomingReceived){
					try {
						linphoneCore.acceptCall(call);
						linphoneCore.enableVideo(true, true);
						call.enableCamera(true);
					} catch (LinphoneCoreException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else 
					if (cstate == State.StreamsRunning || cstate == State.CallUpdatedByRemote){
						linphoneCore.enableVideo(true, true);
						call.enableCamera(true);
						linphoneCore.enableSpeaker(true);
						LinphoneCallParams lcp = call.getRemoteParams();
						lcp.setVideoEnabled(true);
						try {
							linphoneCore.acceptCallUpdate(call, lcp);
						} catch (LinphoneCoreException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} 
			
		
			}
			
			@Override
			public void callEncryptionChanged(LinphoneCore lc, LinphoneCall call,
					boolean encrypted, String authenticationToken) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void authInfoRequested(LinphoneCore lc, String realm,
					String username, String Domain) {
				Log.d("LINMANAGER", "authInfoRequested " + realm + " " + username + " " + Domain);
			}
			
		}, context);
		linphoneCoreFactory.setDebugMode(true, "LINMANAGEE");
		linphoneCore.setContext(context);
		TimerTask lTask = new TimerTask() {
			@Override
			public void run() {
				UIThreadDispatcher.dispatch(new Runnable() {
					@Override
					public void run() {
						if (linphoneCore != null) {
							linphoneCore.iterate();
						}
					}
				});
			}
		};
		Timer mTimer = new Timer("Linphone scheduler");
		mTimer.schedule(lTask, 0, 20);
	}
	
	public void register(Context context, String name, String psw, String server, String proxy, String iden){
		/*try {
			linphoneCore.addAuthInfo(linphoneCoreFactory.createAuthInfo(name, psw, "", server));
			LinphoneProxyConfig lproxy = linphoneCore.createProxyConfig(iden, proxy, "", true);
			linphoneCore.setDefaultProxyConfig(lproxy);
			linphoneCore.addProxyConfig(lproxy);
			//linphoneCore.
		} catch (LinphoneCoreException e) {
			e.printStackTrace();
		}*/
		
		
		//linphoneCore.enableSpeaker(true);
		//linphoneCore.enableVideo(true, true);
		//int i = 0;
		/*try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		while(i < 5){
			linphoneCore.iterate();
			i++;
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		i = 10;*/
		linphoneCore.setVideoDevice(AndroidCameraConfiguration.retrieveCameras()[0].id);
		linphoneCore.enableVideo(true,  true);
		
		String tempUsername = name;
		String tempDisplayName = name;
		String tempDomain = server;
		String identity = "sip:" + tempUsername + "@" + tempDomain;
		proxy = "sip:";
		
		String tempProxy = null;
		if (tempProxy == null) {
			proxy += tempDomain;
		} else {
			if (!tempProxy.startsWith("sip:") && !tempProxy.startsWith("<sip:")
				&& !tempProxy.startsWith("sips:") && !tempProxy.startsWith("<sips:")) {
				proxy += tempProxy;
			} else {
				proxy = tempProxy;
			}
		}
		LinphoneAddress identityAddr = null;
		LinphoneAddress proxyAddr  = null;
		try {
			proxyAddr = linphoneCoreFactory.createLinphoneAddress(proxy);
			identityAddr = linphoneCoreFactory.createLinphoneAddress(identity);
		} catch (LinphoneCoreException e) {
			e.printStackTrace();
		}
		String route = proxyAddr.asStringUriOnly();

		if (tempDisplayName != null) {
			identityAddr.setDisplayName(tempDisplayName);
		}
		LinphoneAuthInfo authInfo = LinphoneCoreFactory.instance().createAuthInfo(tempUsername, psw, name, null, null, tempDomain);
		LinphoneProxyConfig prxCfg = null;
		try {
			prxCfg = linphoneCore.createProxyConfig(identityAddr.asString(), proxyAddr.asStringUriOnly(), route, true);
		} catch (LinphoneCoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			linphoneCore.addProxyConfig(prxCfg);
		} catch (LinphoneCoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		linphoneCore.addAuthInfo(authInfo);
		
		int i = 0;
		/*while(i < 100){
			linphoneCore.iterate();
			i++;
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}*/
		
	}
	
	public void call(String name, String dest, String psw, String server, String proxy, String iden){
		LinphoneCore core = linphoneCore;
		LinphoneAddress linphoneAddress = linphoneCoreFactory.createLinphoneAddress(dest, server, name);
		//core.enableVideo(true, true);
		//core.setVideoDevice(AndroidCameraConfiguration.retrieveCameras()[0].id);
		/*try {
			core.addProxyConfig(core.createProxyConfig(iden, proxy, "", true));
		} catch (LinphoneCoreException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
		//core.addAuthInfo(linphoneCoreFactory.createAuthInfo(name, psw, "", server));
		//core.enableSpeaker(true);
		//core.enableVideo(true, true);
		LinphoneCall call = null;
		try {
			call = core.invite(linphoneAddress);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//call.enableCamera(true);
		//call.enableEchoCancellation(true);
		int i = 0;
		/*while(i < 100){
			core.iterate();
			i++;
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}*/
	}
	
	public static void init(Context context){
		try {
			instance = new LinphoneManager(context);
		} catch (LinphoneCoreException e) {
			e.printStackTrace();
		}
	}
	
	
	public static LinphoneManager getInstance(){
		return instance;
	}
	
	public static LinphoneCore getLc(){
		return getInstance().linphoneCore;
	}
	
}
