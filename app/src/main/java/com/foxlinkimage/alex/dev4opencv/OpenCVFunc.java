package com.foxlinkimage.alex.dev4opencv;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;

public class OpenCVFunc {
    private Context mContext;
    private ArrayList<Mat> mat_alFrameBuffered; //Store Mat-type image
    public static ArrayList<Point> p_alFourCordinates = new ArrayList<>(); //Store four coordinates for finger crop
    private Boolean isBufferFull;


    //建構式
    OpenCVFunc(Context ctx) {
        mContext = ctx;
        isBufferFull = false;
        mat_alFrameBuffered = new ArrayList<>();
    }


    public Bitmap Dewarper(Bitmap input) {
        Bitmap dewarperedBitmap = null;
        //TODO: 自動拉平功能

        return dewarperedBitmap;
    }

    public Bitmap AutoCrop(Bitmap input) {
        Bitmap croppedBitmap = null;
        //TODO: 自動裁切功能

        return croppedBitmap;
    }

    public Bitmap Stitch(Bitmap input1, Bitmap input2) {
        Bitmap combinedBitmap = null;
        //TODO: 拼接功能

        return combinedBitmap;
    }

    //手指裁切
    public void FingerCrop(Bitmap input) {
        //Bitmap covert to Mat
        Mat tmpMat = new Mat(input.getWidth(), input.getHeight(), CvType.CV_8UC1);
        Utils.bitmapToMat(input, tmpMat);
        mat_alFrameBuffered.add(tmpMat);

        //TODO:
        getFingerPostionAlgorithm();
    }

    private void getFingerPostionAlgorithm() {
        //宣告四點座標的物件
        Point pLeftTop = new Point();
        Point pRightTop = new Point();
        Point pLeftBottom = new Point();
        Point pRightBottom = new Point();

        //......OpenCV計算....
        //....................


        //TODO: OpenCV計算後得到的x,y值, 用set(x, y)方式將結果寫入物件
        //設定該物件的參數
        pLeftTop.set(0, 0);
        pRightTop.set(100, 0);
        pLeftBottom.set(0, 100);
        pRightBottom.set(100, 100);


        //將各四點的座標物件, 放到集合物件裡面
        p_alFourCordinates.add(pLeftTop);
        p_alFourCordinates.add(pRightTop);
        p_alFourCordinates.add(pLeftBottom);
        p_alFourCordinates.add(pRightBottom);

        //回傳集合物件
    }

    public static Boolean getCordinatesDone() {
        return (p_alFourCordinates.size() == 4);
    }


    //==Sample== 此函式功能為將圖讀進來, 並轉換為灰階圖片
    public Bitmap Covert2Gray() {
        //從APP的資源資料夾, 讀出圖片
        Bitmap myBmp = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.android);

        //以下開始為使用OpenCV Library的功能....
        Mat tmp = new Mat(myBmp.getWidth(), myBmp.getHeight(), CvType.CV_8UC1);
        Utils.bitmapToMat(myBmp, tmp);
        Imgproc.cvtColor(tmp, tmp, Imgproc.COLOR_RGB2GRAY);
        Utils.matToBitmap(tmp, myBmp);

        return myBmp;
    }
}
