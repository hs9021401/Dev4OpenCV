package com.foxlinkimage.alex.dev4opencv;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.opencv.android.InstallCallbackInterface;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

public class MainActivity extends AppCompatActivity {
    Button btnConvert, btnFingerCrop;
    ImageView img;
    HandlerThread mHandlerThread;
    OpenCVFunc objOpenCVFunc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
    }

    void initialize()
    {
        img = (ImageView) findViewById(R.id.img);
        btnConvert = (Button) findViewById(R.id.btnConvert);
        btnFingerCrop = (Button) findViewById(R.id.btnFingerCrop);

        btnConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                objOpenCVFunc = new OpenCVFunc(getApplicationContext());
                Bitmap myBmp = objOpenCVFunc.Covert2Gray();
                img.setImageBitmap(myBmp);
            }
        });

        btnFingerCrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                objOpenCVFunc = new OpenCVFunc(getApplicationContext());

                mHandlerThread = new HandlerThread("HandlerThread");
                mHandlerThread.start();

                new Handler(mHandlerThread.getLooper()).post(mRunnable);
            }
        });

        //Load default bitmap
        Bitmap map = BitmapFactory.decodeResource(getResources(), R.drawable.android);
        img.setImageBitmap(map);
    }

    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            //每500秒毫丟一張圖，一共40張圖,模擬Finger Crop情境
            for (int i = 0; i < 40; i++) {
                Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.android);
                objOpenCVFunc.FingerCrop(bmp);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };



    private LoaderCallbackInterface mLoaderCallback = new LoaderCallbackInterface() {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                    Log.i("OPENCV", "OpenCV loaded successfully");
                    break;
                case LoaderCallbackInterface.INIT_FAILED:
                    Log.i("OPENCV", "OpenCV init failed");
                    break;
                case LoaderCallbackInterface.INCOMPATIBLE_MANAGER_VERSION:
                    Log.i("OPENCV", "Incompatible Version");
                    break;
                case LoaderCallbackInterface.MARKET_ERROR:
                    Log.i("OPENCV", "Market Error");
                    break;
                default:
                    Log.i("OPENCV", "OpenCV Manager Install");
                    break;
            }
        }

        @Override
        public void onPackageInstall(int operation, InstallCallbackInterface callback) {
            Log.i("OPENCV", "Open market to install OpenCV Manager....");
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
    }


}
