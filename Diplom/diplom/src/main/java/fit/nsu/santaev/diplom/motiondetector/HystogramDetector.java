package fit.nsu.santaev.diplom.motiondetector;

import java.util.Random;

import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.video.BackgroundSubtractor;

import fit.nsu.santaev.diplom.utils.Hystogram;

public class HystogramDetector extends IMotionDetector{

	private double maxTreshold = 1;
	private double[] lastHyst;

	private long last;
	private long fpsDelta = 5000;
	
	public HystogramDetector(double treshhold){
		setTrashold(treshhold); 
	}
	@Override
	public ResultFrame processFrame(CvCameraViewFrame inputFrame) {
		Mat frame = inputFrame.rgba();
		//int ii = CvType.channels(frame.type());
		Mat mat = new Mat();
		Imgproc.GaussianBlur(frame, mat, new Size(15,15), 50);
		if (lastHyst == null){
			lastHyst = Hystogram.getHystogram(mat);
		}
		if (System.currentTimeMillis() - last > fpsDelta){
			lastHyst = Hystogram.getHystogram(mat);
			last = System.currentTimeMillis();
		}
		ResultFrame resultFrame = new ResultFrame();
		double[] hyst = Hystogram.getHystogram(mat);
		resultFrame.value = Hystogram.getDifference(lastHyst, hyst);
		Hystogram.addToMath(lastHyst, mat, 0, 0, 255);
		Hystogram.addToMath(hyst, mat, 255, 0, 0);
		resultFrame.frame = mat;
		//lastHyst = hyst;
		return resultFrame;
	}

	public double getMaxTreshold() {
		return maxTreshold;
	}

	@Override
	public void setTrashold(double i) {
		maxTreshold = i / 100;
	}
}