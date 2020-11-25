package bpj.com.facetracing;


import android.Manifest;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback, Camera
        .PreviewCallback {

    static {
        System.loadLibrary("native-lib");
    }

    private CameraHelper cameraHelper;
    int cameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getPermission();
        SurfaceView surfaceView = findViewById(R.id.surfaceView);
        surfaceView.getHolder().addCallback(this);

        cameraHelper = new CameraHelper(cameraId);
        cameraHelper.setPreviewCallback(this);
        Utils.copyAssets(this, "lbpcascade_frontalface.xml");
    }

    public static  native String stringFromJNI();


    @Override
    protected void onResume() {
        super.onResume();
        //初始化跟踪器
        init("/sdcard/lbpcascade_frontalface.xml");
        cameraHelper.startPreview();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //释放跟踪器
        release();
        cameraHelper.stopPreview();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        //设置surface 用于显示
        setSurface(holder.getSurface());
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        //传输数据
        postData(data, CameraHelper.WIDTH, CameraHelper.HEIGHT, cameraId);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            cameraHelper.switchCamera();
            cameraId = cameraHelper.getCameraId();
        }
        return super.onTouchEvent(event);
    }



    /**
     * 初始化 追踪器
     * @param model
     */
    native void init(String model);

    /**
     * 设置画布
     *  ANativeWindow
     * @param surface
     */
    native void setSurface(Surface surface);

    /**
     * 处理摄像头数据
     * @param data
     * @param w
     * @param h
     * @param cameraId
     */
    native void postData(byte[] data, int w, int h, int cameraId);

    /**
     * 释放
     */
    native void release();
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.INTERNET,
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
    };
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private void getPermission() {
        ActivityCompat.requestPermissions(this,
                PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
    }
}
