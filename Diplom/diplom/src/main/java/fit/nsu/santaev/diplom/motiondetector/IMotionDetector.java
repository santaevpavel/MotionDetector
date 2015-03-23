package fit.nsu.santaev.diplom.motiondetector;

import java.io.Serializable;

import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.core.Mat;

import android.content.Context;

public abstract class IMotionDetector implements Serializable{
	
	public abstract ResultFrame processFrame(CvCameraViewFrame inputFrame);
	
	public abstract double getMaxTreshold();
	
	public abstract void setTrashold(double i);
	
	public void func1(Context context){
		
	}
	
}
