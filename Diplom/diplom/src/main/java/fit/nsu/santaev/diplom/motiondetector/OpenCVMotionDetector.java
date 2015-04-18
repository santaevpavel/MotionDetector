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

    List<Mat> rgba = new ArrayList<Mat>();

	BackgroundSubtractorMOG2 bsubU = null;
    BackgroundSubtractorMOG2 bsubV = null;
	
	public OpenCVMotionDetector(double treshhold){
		setTrashold(treshhold);
	}
	@Override
	public ResultFrame processFrame(CvCameraViewFrame inputFrame) {
		if (null == bsubU){
			bsubU = new BackgroundSubtractorMOG2(100, 16);
            bsubV = new BackgroundSubtractorMOG2(100, 16);
		}
        Mat resized = new Mat();
        int w = inputFrame.rgba().width();
        int h = inputFrame.rgba().height();
        Imgproc.resize(inputFrame.rgba(), resized, new Size(w/2, h/2));
        Mat rgb = new Mat();
        Mat cvt = new Mat();
        Imgproc.cvtColor(resized, rgb, Imgproc.COLOR_RGBA2RGB);
        Imgproc.cvtColor(rgb, cvt, Imgproc.COLOR_RGB2YUV);
        org.opencv.core.Core.split(cvt, rgba);

		ResultFrame resultFrame = new ResultFrame();
        //Imgproc.blur(cvt, frame, new Size(5, 5));
		Mat outputU = new Mat();
        Mat outputV = new Mat();
		bsubU.apply(rgba.get(1), outputU, 0.01f);
        bsubV.apply(rgba.get(2), outputV, 0.01f);
        Mat resFrame = new Mat();
        Core.addWeighted(outputU, 1f, outputV, 1f, 0, resFrame);
        Mat resizedResFrame = new Mat();
        Imgproc.resize(resFrame, resizedResFrame, new Size(0, 0), 2d, 2d, Imgproc.INTER_LINEAR);
		resultFrame.frame = resizedResFrame;
        double dU[] = Core.sumElems(outputU).val;
        double dV[] = Core.sumElems(outputV).val;
        double d2 = (dU[0] + dV[0]) / rgba.get(1).rows() / rgba.get(1).cols() / 2;
        resultFrame.value = d2;
        outputU.release();
        outputV.release();
        rgb.release();
        cvt.release();
        resFrame.release();
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
