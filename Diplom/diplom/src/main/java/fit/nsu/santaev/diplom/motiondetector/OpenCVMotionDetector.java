package fit.nsu.santaev.diplom.motiondetector;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.video.BackgroundSubtractor;
import org.opencv.video.BackgroundSubtractorMOG;
import org.opencv.video.BackgroundSubtractorMOG2;

import android.util.Log;

public class OpenCVMotionDetector extends IMotionDetector{

	private double maxTreshold = 64D;
	private Mat bg;
	private double a = 0.1d;
	private long startTime;
	private long startDelta;
	private long fpsDelta = 300;
	private long maxDelta = 1000;
	private Mat last;
	
	BackgroundSubtractorMOG2 bsub = null;
	
	public OpenCVMotionDetector(double treshhold){
		setTrashold(treshhold);
	}
	@Override
	public ResultFrame processFrame(CvCameraViewFrame inputFrame) {
		if (null == bsub){
			bsub = new BackgroundSubtractorMOG2(100, 16);
		}
		ResultFrame resultFrame = new ResultFrame();
		Mat frame = new Mat();
        Imgproc.blur(inputFrame.gray(), frame, new Size(5, 5));
		Mat output = frame.clone();
		bsub.apply(frame, output, 0.01f);
		resultFrame.frame = inputFrame.rgba();
        double d[] = Core.sumElems(output).val;
        double d2 = d[0] / frame.rows() / frame.cols();
        resultFrame.value = d2;
        //List<MatOfPoint> contours = new LinkedList<>();
        //Imgproc.findContours(output, contours, new Mat(), Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
        //Imgproc.drawContours(inputFrame.rgba(), contours, -1, new Scalar(255, 0, 0), 1);
		return resultFrame;
	}

	public double getMaxTreshold() {
		return maxTreshold;
	}

	@Override
	public void setTrashold(double i) {
		maxTreshold = 255d / 10 * i;
	}
	
	

	class LastFrames{
		
		private List<Mat> values = new LinkedList<>();
		private List<Long> times = new LinkedList<Long>();
		
		public void add(Mat mat, Long time){
			values.add(0, mat);
			times.add(0, time);
		}
		
		public Mat getCompareMat(Long delta) {
			long sum = 0;
			for (int i = 0; i < values.size(); i++) {
				sum += times.get(i);
				if (sum > delta) {
					for (int j = i + 1; j < values.size(); j++) {
						values.get(j).release();
					}
					values = values.subList(0, i);
					times = times.subList(0, i);
					//Log.e("myLogs", "i = " + i);
					return values.get(i - 1);
				}
			}
			if (values.size() > 0) {
				//Log.e("myLogs", "i2 = " + (values.size() - 1));
				return values.get(values.size() - 1);
			}
			return null;
		}	
	}
}
