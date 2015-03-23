package fit.nsu.santaev.diplom.motiondetector;

import java.util.Random;

import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.core.Mat;

public class TestMotionDetector extends IMotionDetector{

	private double maxTreshold = 0;
	private long startTime = 0;
	private Random random = new Random();
	
	public TestMotionDetector(double treshhold){
		maxTreshold = treshhold; 
	}
	@Override
	public ResultFrame processFrame(CvCameraViewFrame inputFrame) {
		Mat frame = inputFrame.rgba();
		if (0 == startTime){
			startTime = System.currentTimeMillis();
		}
		ResultFrame resultFrame = new ResultFrame();
		resultFrame.frame = frame;
		if ((System.currentTimeMillis() - startTime) / 1000 % 2 == 0){
			resultFrame.value = 10 + ((double) (System.currentTimeMillis() - startTime)) / 1000;
		} else {
			resultFrame.value = 5;
		}
		return resultFrame;
	}

	public double getMaxTreshold() {
		return maxTreshold;
	}

	@Override
	public void setTrashold(double i) {
		maxTreshold = 100 * i;
	}

}
