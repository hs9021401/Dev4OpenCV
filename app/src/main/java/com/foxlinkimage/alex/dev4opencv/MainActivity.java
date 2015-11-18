package com.foxlinkimage.alex.dev4opencv;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.opencv.android.InstallCallbackInterface;
import org.opencv.android.LoaderCallbackInterface;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button btnConvert, btnFingerCrop, btnStirch;
    ImageView img;
    HandlerThread mHandlerThread;
    OpenCVFunc objOpenCVFunc;

    //將opencv .so包進來, 就不需要安裝OpenCV Manager
    static {
        System.loadLibrary("opencv_java3");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
    }

    void initialize() {
        img = (ImageView) findViewById(R.id.img);
        btnConvert = (Button) findViewById(R.id.btnConvert);
        btnFingerCrop = (Button) findViewById(R.id.btnFingerCrop);
        btnStirch = (Button) findViewById(R.id.btnStitch);

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
                mHandlerThread = new HandlerThread("HandlerThread_FingerCrop");
                mHandlerThread.start();

                new Handler(mHandlerThread.getLooper()).post(mRunnableFingerCrop);
            }
        });

        btnStirch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                objOpenCVFunc = new OpenCVFunc(getApplicationContext());
                mHandlerThread = new HandlerThread("HandlerThread_Stitch");
                mHandlerThread.start();

                new Handler(mHandlerThread.getLooper()).post(mRunnableStich);
            }
        });


        //Load default bitmap
        Bitmap MyBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.android);
        img.setImageBitmap(MyBitmap);
    }


    Runnable mRunnableFingerCrop = new Runnable() {
        @Override
        public void run() {
            //每500秒毫丟一張圖
            while(!OpenCVFunc.getCordinatesDone()){
                Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.android);
                objOpenCVFunc.FingerCrop(bmp);
                try {
                    Thread.sleep(66);   // 15fps
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            //得到手指裁切圖片四點座標
            ArrayList<Point> p_alCordinates = OpenCVFunc.p_alFourCordinates;

        }
    };

    Runnable mRunnableStich = new Runnable() {
        @Override
        public void run() {
            Bitmap bmpStitched;
            Bitmap bmp1 = BitmapFactory.decodeResource(getResources(), R.drawable.part1);
            Bitmap bmp2 = BitmapFactory.decodeResource(getResources(), R.drawable.part2);
            bmpStitched = objOpenCVFunc.Stitch(bmp1, bmp2);

            // Save stitched image to file
            String strSavePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/DevOpenCVFunc/1.jpg";
            File save2File = new File(strSavePath);
            try {
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(save2File));
                bmpStitched.compress(Bitmap.CompressFormat.JPEG, 90, bos);
                bos.flush();
                bos.flush();
            } catch (IOException e) {
                e.printStackTrace();
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
//        if (!OpenCVLoader.initDebug()) {
//            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
//        } else {
        mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
//        }
    }


}
