package fit.nsu.santaev.diplom.motiondetector;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

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
			lastFrame = frame.clone();
		}
		ResultFrame resultFrame = new ResultFrame();
		resultFrame.frame = null;
		Mat mat = new Mat();
        Mat mat2 = new Mat();
		Core.absdiff(lastFrame, frame, mat2);
        org.opencv.imgproc.Imgproc.threshold(mat2, mat, 50, 255, Imgproc.THRESH_BINARY);
		double d[] = Core.sumElems(mat).val;
		double d2 = d[0] / mat.rows() / mat.cols();
		resultFrame.value = d2;
		resultFrame.frame = mat;
        lastFrame.release();
        mat2.release();
		lastFrame = frame.clone();
        frame.release();
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
