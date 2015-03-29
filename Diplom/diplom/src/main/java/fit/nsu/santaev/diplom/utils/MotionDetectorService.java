package fit.nsu.santaev.diplom.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Looper;
import android.util.Base64;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Date;

/**
 * Created by Pavel on 26.03.2015.
 */
public class MotionDetectorService {

    private static final String URL = "http://10.3.71.67:8080/MainProject/GCMHttpServer";
    private static final String PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
    private static final String SEPARATOR = "/";

    private static long lastDetect = 0;
    private static long startTimeDelta = 1000 * 20;
    private static long startTime = 0;

    public static void init() {
        startTime = System.currentTimeMillis();
    }

    public static void detect(final Bitmap bmp) {
        if (System.currentTimeMillis() - startTime < startTimeDelta){
            return;
        }
        if (System.currentTimeMillis() - lastDetect < 10000){
            return;
        }
        lastDetect = System.currentTimeMillis();
        String root = Environment.getExternalStorageDirectory().getAbsolutePath();
        File myDir = new File(root + "/saved_images/");
        myDir.mkdir();
        //Random generator = new Random();
        //int n = 10000;
        //n = generator.nextInt(n);
        Date date = new Date(System.currentTimeMillis());
        String fname = "Image-"+ + date.getMinutes() + date.getSeconds() +".jpg";
        File file = new File(myDir, fname);
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Thread t = new Thread()
        {
            public void run()
            {
                //Looper.prepare(); //For Preparing Message Pool for the child Thread
                HttpClient client = new DefaultHttpClient();
                HttpConnectionParams.setConnectionTimeout(client.getParams(), 15000); //Timeout Limit
                HttpResponse response;
                HttpPost post = new HttpPost(URL);
                JSONObject json = new JSONObject();
                try
                {
                    Bitmap bitmapOrg = bmp;//BitmapFactory.decodeFile(PATH + SEPARATOR + image);
                    ByteArrayOutputStream bao = new ByteArrayOutputStream();
                    bitmapOrg.compress(Bitmap.CompressFormat.JPEG, 90, bao);
                    byte [] byteArrayImage = bao.toByteArray();
                    String encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
                    json.put("TYPE", "SEND");
                    json.put("VALUE", encodedImage);
                    StringEntity se = new StringEntity(json.toString());
                    se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                    post.setEntity(se);
                    response = client.execute(post);

/*Checking response */
                    if(null != response)
                    {
                        InputStream in = response.getEntity().getContent(); //Get the data in the entity
                    }

                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
                //Looper.loop(); //Loop in the message queue
            }
        };
        t.start();

    }
}
