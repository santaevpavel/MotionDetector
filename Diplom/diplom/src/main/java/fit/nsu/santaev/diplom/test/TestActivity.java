package fit.nsu.santaev.diplom.test;

import android.app.Activity;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.util.ArrayList;

import fit.nsu.santaev.diplom.R;
import fit.nsu.santaev.diplom.activity.MainActivity;
import fit.nsu.santaev.diplom.motiondetector.BackbroundSubMotionDetector;
import fit.nsu.santaev.diplom.motiondetector.BackbroundSubMotionDetector2;
import fit.nsu.santaev.diplom.motiondetector.BackbroundSubMotionDetector3;
import fit.nsu.santaev.diplom.motiondetector.BackbroundSubMotionDetector4;
import fit.nsu.santaev.diplom.motiondetector.FrameSubMotionDetector;
import fit.nsu.santaev.diplom.motiondetector.IMotionDetector;
import fit.nsu.santaev.diplom.motiondetector.OpenCVMotionDetector;
import fit.nsu.santaev.diplom.motiondetector.ResultFrame;
import fit.nsu.santaev.diplom.utils.MotionDetectorService;

/**
 * Created by Pavel on 18.04.2015.
 */

public class TestActivity extends Activity implements CameraBridgeViewBase.CvCameraViewListener2 {

    private ArrayList<Bitmap> frames;
    private ImageView img;
    private int num = 0;
    //private IMotionDetector detector = new OpenCVMotionDetector(100);
    //private IMotionDetector detector = new BackbroundSubMotionDetector4(100);
    private IMotionDetector detector = new FrameSubMotionDetector(100);

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(
            this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    //System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
                    File sdcard = Environment.getExternalStorageDirectory();
                    final File file = new File(sdcard, "video2.mp4");
                    Thread t = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            frames = getFrames(file.getAbsolutePath());
                        }
                    });
                    t.start();
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity);
        File sdcard = Environment.getExternalStorageDirectory();
        final File file = new File(sdcard, "video.mp4");
        img = ((ImageView)findViewById(R.id.imageView));
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TestActivity.this, "" + num, Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onResume() {
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_5, this,
                mLoaderCallback);
        super.onResume();
    }

    private ArrayList<Bitmap> getFrames(String path){
        int start = 20 * 1000000;
        int dur = 48 * 1000000;
        int frames = 201;
        try {
            ArrayList<Bitmap> bArray = new ArrayList<Bitmap>();
            bArray.clear();
            MediaMetadataRetriever mRetriever = new MediaMetadataRetriever();
            mRetriever.setDataSource(path);
            for (int i = start; i < dur; i = i + (dur - start) / frames) {
                num++;
                final Bitmap b = mRetriever.getFrameAtTime(i, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
                //bArray.add();
                final ResultFrame frame = detector.processFrame(new CameraBridgeViewBase.CvCameraViewFrame() {
                    @Override
                    public Mat rgba() {
                        Mat m = new Mat();
                        Utils.bitmapToMat(b, m);
                        return m;
                    }

                    @Override
                    public Mat gray() {
                        Mat gray = new Mat();
                        Imgproc.cvtColor(rgba(), gray, Imgproc.COLOR_RGBA2GRAY);
                        return gray;
                    }
                });
                Mat res = frame.frame;
                final Bitmap bmp = Bitmap.createBitmap(res.cols(), res.rows(), Bitmap.Config.ARGB_8888);
                Utils.matToBitmap(res, bmp);
                //Thread.sleep(100);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        img.setImageBitmap(bmp);
                    }
                });

            }
            return bArray;
        } catch (Exception e) {
            //throw e;
            return null;
        }
    }

    @Override
    public void onCameraViewStarted(int width, int height) {

    }

    @Override
    public void onCameraViewStopped() {

    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        return null;
    }
}
