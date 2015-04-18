package fit.nsu.santaev.diplom.motiondetector;

import android.content.Context;
import android.util.Log;

import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class CheckHSV extends IMotionDetector{

	private double maxTreshold = 64D;
	List<Mat> rgba = new ArrayList<Mat>();
	public CheckHSV(double treshhold){
		setTrashold(treshhold);
	}

    private int w;
    private int h;

    private int frameNum = 0;

	@Override
	public ResultFrame processFrame(CvCameraViewFrame inputFrame) {

        w = inputFrame.rgba().width();
        h = inputFrame.rgba().height();
		ResultFrame resultFrame = new ResultFrame();
		Mat frame = inputFrame.rgba();
        Imgproc.resize(inputFrame.rgba(), frame, new Size(w / 8, h / 8));
        Mat rgb = new Mat();
        Mat cvt = new Mat();
        Imgproc.cvtColor(frame, rgb, Imgproc.COLOR_RGBA2RGB);
        Imgproc.cvtColor(rgb, cvt, Imgproc.COLOR_RGB2YUV);
		Core.split(cvt, rgba);

        int w = rgba.get(0).width();
        int h = rgba.get(0).height();

        //Mat hsvFrame = new Mat();
        //Core.addWeighted(rgba.get(1), 0.5d, rgba.get(2), 0.5d, 0, hsvFrame);
        resultFrame.frame = new Mat();
        Imgproc.resize(rgba.get(frameNum), resultFrame.frame, new Size(0, 0), 8d, 8d, Imgproc.INTER_LINEAR);
        frame.release();
        rgb.release();
        cvt.release();
        return resultFrame;
	}

	@Override
	public void func1(Context context) {
        frameNum = (frameNum + 1) % 3;
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
