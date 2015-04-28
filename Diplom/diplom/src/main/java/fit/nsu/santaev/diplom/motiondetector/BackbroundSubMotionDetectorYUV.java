package fit.nsu.santaev.diplom.motiondetector;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class BackbroundSubMotionDetectorYUV extends IMotionDetector{

    private double maxTreshold = 0;
    private BackbroundSubMotionDetector3 detector1;
    private BackbroundSubMotionDetector3 detector2;


	public BackbroundSubMotionDetectorYUV(double treshhold){
		setTrashold(treshhold);
        detector1 = new BackbroundSubMotionDetector3(100);
        detector2 = new BackbroundSubMotionDetector3(100);
	}

    @Override
    public ResultFrame processFrame(CvCameraViewFrame inputFrame) {
        Mat rgb = new Mat();
        Mat cvt = new Mat();
        //Imgproc.cvtColor(inputFrame., rgb, Imgproc.COLOR_RGBA2RGB);
        Imgproc.cvtColor(rgb, cvt, Imgproc.COLOR_RGB2YUV);
        //org.opencv.core.Core.split(cvt, rgba);
        return null;
    }

    @Override
    public void func1(Context context) {

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
