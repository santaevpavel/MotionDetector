package fit.nsu.santaev.diplom.motiondetector;

import android.content.Context;
import android.widget.Toast;

import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class BackbroundSubMotionDetector3 extends IMotionDetector{

	private double maxTreshold = 64D;
	private Mat bg;
	private double a = 0.1d;
	private long startTime;
	private long startDelta;
	private long fpsDelta = 300;
	private long maxDelta = 1000;
	private Mat last;
	private boolean isUseMask = false;
	List<Mat> rgba = new ArrayList<Mat>();
	Mat r;
	Mat g;
	Mat b;
	Mat aa;
    int num = 0;
    int frames = 100;
	public BackbroundSubMotionDetector3(double treshhold){
		setTrashold(treshhold);

	}
	@Override
	public ResultFrame processFrame(CvCameraViewFrame inputFrame) {
		ResultFrame resultFrame = new ResultFrame();
		Mat frame = inputFrame.gray();//.reshape(2);
        int w = frame.width();
        int h = frame.height();

        if (bg == null){
            bg = frame.clone();
        }
		/*if (fpsDelta > System.currentTimeMillis()){
			resultFrame.frame = last;
			return resultFrame;
		} else {
			fpsDelta = System.currentTimeMillis() + 200;
		}*/
		/*if (startTime == 0){
			startTime = System.currentTimeMillis();
			startDelta = System.currentTimeMillis() + 2000;
		}
		if (startDelta > System.currentTimeMillis()){
			resultFrame.frame = bg;
			resultFrame.value = 0;
			return resultFrame;
		}*/

		Mat frame2 = new Mat();
		//Mat frame3 = new Mat();
		Mat bg2 = new Mat();
		
		Mat review = new Mat();
		Mat review2 = new Mat();
		//Mat review3 = new Mat();
		//Mat mask = new Mat();
		
		
		Core.absdiff(bg, frame, review);
		org.opencv.imgproc.Imgproc.threshold(review, review2, 30, 255, Imgproc.THRESH_BINARY);
		
		
		// bg = bg * (1 - a) + frame * mask(tresh((bg - frame)) * a)
		// bg = bg * (1 - a) + frame * mask(tresh((bg - frame)) * a) 
		Core.multiply(frame, new Scalar(a), frame2, 1);
		Core.multiply(bg, new Scalar(1 - a), bg2, 1);
		Core.add(frame2, bg2, bg);

		
		//Core.add(review2, frame2, review3, review2);
		//double d[] = Core.sumElems(review).val;
		//double d2 = d[0] / review.rows() / review.cols();
		resultFrame.value = 5;// + (new Random()).nextInt(10);
        resultFrame.frame = review2;
		//last = bg;
        //Mat rev3 = new Mat();
		return resultFrame;

	}

	@Override
	public void func1(Context context) {
		//isUseMask = !isUseMask;
        num = (num + 1) % 3;
		Toast.makeText(context, "" + num, Toast.LENGTH_SHORT).show();
	}
	
	private Mat thresh(Mat src, int d){
		byte[] b = new byte[1];
		Mat m = src.clone();
		byte[] white = new byte[]{(byte)255};
		byte[] black = new byte[]{(byte)0};
		for (int i = 0; i < src.rows(); i += 5){
			for (int j = 0; j < src.cols(); j += 5){
				src.get(i, j, b);
				if (b[0] > d){
					m.put(i, j, white);
				} else {
					m.put(i, j, black);
				}
			}
		}
		return m;
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
