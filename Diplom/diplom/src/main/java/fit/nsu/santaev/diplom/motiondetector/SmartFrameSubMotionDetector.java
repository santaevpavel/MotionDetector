package fit.nsu.santaev.diplom.motiondetector;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.core.Core;
import org.opencv.core.Mat;

import android.util.Log;

public class SmartFrameSubMotionDetector extends IMotionDetector{

	private double maxTreshold = 64D;
	private Mat lastFrame;
	//private LastFrames frames = new LastFrames();
	private long last;
	private long fpsDelta = 3000;
	private long maxDelta = 1000;
	
	public SmartFrameSubMotionDetector(double treshhold){
		setTrashold(treshhold); 
	}
	@Override
	public ResultFrame processFrame(CvCameraViewFrame inputFrame) {
		Mat frame = inputFrame.gray();
		if (lastFrame == null){
			lastFrame = frame.clone();
		}
		if (last == 0){
			last = System.currentTimeMillis();
		}
		if (System.currentTimeMillis() - last > fpsDelta){
			lastFrame = frame.clone();
			last = System.currentTimeMillis();
		}
		ResultFrame resultFrame = new ResultFrame();
		Mat mat = new Mat();
		Core.absdiff(lastFrame, frame, mat);
		double d[] = Core.sumElems(mat).val;
		double d2 = d[0] / mat.rows() / mat.cols();
		resultFrame.value = d2;
		resultFrame.frame = mat;
		return resultFrame;
	}

	public double getMaxTreshold() {
		return maxTreshold;
	}

	@Override
	public void setTrashold(double i) {
		maxTreshold = 255d / 3 * i;
	}
	
	

	class LastFrames{
		
		private List<Mat> values = new ArrayList();
		private List<Long> times = new ArrayList<Long>();
		
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
