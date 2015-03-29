package fit.nsu.santaev.diplom.motiondetector;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.opencv.android.Utils;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class BackbroundSubMotionDetector extends IMotionDetector{

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
	public BackbroundSubMotionDetector(double treshhold){
		setTrashold(treshhold);
		
	}
	@Override
	public ResultFrame processFrame(CvCameraViewFrame inputFrame) {

        if (frames > 50){
            synchronized (this) {
                System.gc();
                frames = 0;
            }
        }
        frames++;
		ResultFrame resultFrame = new ResultFrame();
		Mat frame = inputFrame.rgba();//.reshape(2);
		//if (r == null){
		//rgba.clear();
			/*r = new Mat();
			g = new Mat();
			b = new Mat();
			aa = new Mat();
			rgba.add(r);
			rgba.add(g);
			rgba.add(b);
			rgba.add(aa);*/
		//}

        Mat blured = new Mat();
        Mat rgb = new Mat();
        Mat cvt = new Mat();
        Imgproc.blur(frame, blured, new Size(10, 10));
        Imgproc.cvtColor(blured, rgb, Imgproc.COLOR_RGBA2RGB);
        Imgproc.cvtColor(rgb, cvt, Imgproc.COLOR_RGB2HSV_FULL);
		org.opencv.core.Core.split(cvt, rgba);


		//rgba.get(3).release();
        //Mat mat = new Mat(rgba.get(0).height(), rgba.get(0).width(), rgba.get(0).type());
        int w = rgba.get(0).width();
        int h = rgba.get(0).height();

        /*Mat mat2 = new Mat(rgba.get(0).height(), rgba.get(0).width(), rgba.get(0).type());
        Mat r = rgba.get(0).submat(0, h/2, 0, w/2);
        Mat g = rgba.get(1).submat(0, h/2, w/2, w);
        Mat b = rgba.get(2).submat(h/2, h, 0, w/2);
        */
        /*Imgproc.resize(rgba.get(0), r, new Size(rgba.get(0).width()/2, rgba.get(0).height()/2));
        Imgproc.resize(rgba.get(1), g, new Size(rgba.get(0).width()/2, rgba.get(0).height()/2));
        Imgproc.resize(rgba.get(2), b, new Size(rgba.get(0).width()/2, rgba.get(0).height()/2));
*/
        /*Core.add(mat2.submat(0, h/2, 0, w/2), r, mat2.submat(0, h/2, 0, w/2));
        Core.add(mat2.submat(0, h/2, w/2, w), g, mat2.submat(0, h/2, w/2, w));
        Core.add(mat2.submat(h/2, h, 0, w/2), b, mat2.submat(h/2, h, 0, w/2));*/
        resultFrame.frame = rgba.get(num);//.clone();
        return resultFrame;
/*
		frame = r;
		if (bg == null){
			bg = frame.clone();
		}
		/*if (fpsDelta > System.currentTimeMillis()){
			resultFrame.frame = last;
			return resultFrame;
		} else {
			fpsDelta = System.currentTimeMillis() + 200;
		}
		if (startTime == 0){
			startTime = System.currentTimeMillis();
			startDelta = System.currentTimeMillis() + 2000;
		}
		if (startDelta > System.currentTimeMillis()){
			resultFrame.frame = bg;
			resultFrame.value = 0;
			return resultFrame;
		}*/
        /*
		Mat frame2 = new Mat();
		//Mat frame3 = new Mat();
		Mat bg2 = new Mat();
		
		Mat review = new Mat();
		Mat review2 = new Mat();
		//Mat review3 = new Mat();
		//Mat mask = new Mat();
		
		
		Core.absdiff(bg, frame, review);
		//review2 = thresh(review,30);
		org.opencv.imgproc.Imgproc.threshold(review, review2, 50, 255, Imgproc.THRESH_BINARY_INV);
		
		
		// bg = bg * (1 - a) + frame * mask(tresh((bg - frame)) * a)
		// bg = bg * (1 - a) + frame * mask(tresh((bg - frame)) * a) 
		Core.multiply(frame, new Scalar(a / 2), frame2, 1);
		Core.multiply(bg, new Scalar(1 - a), bg2, 1);
		if (isUseMask){
			Core.add(bg2, frame2, bg, review2);
		} else {
			Core.add(frame2, bg2, bg);
		}
		
		//Core.add(review2, frame2, review3, review2);
		//double d[] = Core.sumElems(review).val;
		//double d2 = d[0] / review.rows() / review.cols();
		resultFrame.value = 5;// + (new Random()).nextInt(10);
		last = bg;
		//resultFrame.frame = last;
        Mat rev3 = new Mat();
        Imgproc.resize(bg, rev3, new Size(w/2, h/2));
        Core.add(mat2.submat(h/2, h, w/2, w), rev3, mat2.submat(h/2, h, w/2, w));
		return resultFrame;
        */
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
