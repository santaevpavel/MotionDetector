package fit.nsu.santaev.diplom.motiondetector;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.core.Core;
import org.opencv.core.Mat;

public class FrameSubMotionDetector extends IMotionDetector{

	private double maxTreshold = 64D;
	private Mat lastFrame;
	
	public FrameSubMotionDetector(double treshhold){
		setTrashold(treshhold); 
	}
	@Override
	public ResultFrame processFrame(CvCameraViewFrame inputFrame) {
		Mat frame = inputFrame.gray();
		if (lastFrame == null){
			lastFrame = frame;
		}
		ResultFrame resultFrame = new ResultFrame();
		resultFrame.frame = null;
		Mat mat = new Mat();
		Core.absdiff(lastFrame, frame, mat);
		double d[] = Core.sumElems(mat).val;
		double d2 = d[0] / mat.rows() / mat.cols();
		resultFrame.value = d2;
		resultFrame.frame = mat;
		lastFrame = frame;
		return resultFrame;
	}

	public double getMaxTreshold() {
		return maxTreshold;
	}

	@Override
	public void setTrashold(double i) {
		maxTreshold = 255d / 4 * i;
	}

}
