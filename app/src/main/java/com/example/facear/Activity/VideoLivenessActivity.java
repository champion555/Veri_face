package com.example.facear.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daasuu.mp4compose.FillMode;
import com.daasuu.mp4compose.Rotation;
import com.daasuu.mp4compose.composer.Mp4Composer;
import com.example.facear.BackgroundService.BackgroundService;
import com.example.facear.Models.AuthenticationResponse;
import com.example.facear.Models.PhotoLivenessResponse;
import com.example.facear.R;
import com.example.facear.Services.RetrofitClient;
import com.example.facear.Utils.AppConstants;
import com.example.facear.Utils.CoreApp;
import com.example.facear.Utils.LoadingDialog;
import com.example.facear.Utils.Preferences;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoLivenessActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    private static final String TAG = "VideoLivenessActivity";
    private int mCameraContainerWidth = 0;
    private SurfaceView mSurfaceView = null;
    private SurfaceHolder mSurfaceHolder = null;
    private Camera mCamera = null;
    private boolean mIsRecording = false;
    private RelativeLayout processingView;
    private int mPreviewHeight;
    private int mPreviewWidth;
    MediaRecorder mRecorder;
    private ImageView btnRecord,img_frame,imgDirection,btBack;
    private File videoFile,rotatedVideo;
    private String videoFileName, videoFilePath,rotateFilePath;
    private TextView txtDescription,txtDigit;
    private LoadingDialog loadingDialog;
    String[] fourDigits;
    int digit;
    Bundle bundle;
    String Target;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_liveness);
        BackgroundService.mContext = VideoLivenessActivity.this;
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
                releaseMediaRecorder();
                releaseCamera();
            }
        });
        bundle = getIntent().getExtras();
        Target = bundle.getString("Target");
        mCamera = getCamera();
        init();
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mCameraContainerWidth = mSurfaceView.getLayoutParams().width;

        btnRecord.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                btnRecord.setImageResource(R.drawable.ic_record);
                img_frame.setImageResource(R.drawable.ic_detected6);
                Random random = new Random();
                digit = random.nextInt(2);
                fourDigits = new String[4];
                for (int i=0; i<4; i++){
                    Random rand = new Random();
                    int ranNum = rand.nextInt(10);
                    fourDigits[i] = String.valueOf(ranNum);
                }
                if (mIsRecording) {
                    stopRecording();
                }else {
                    if (prepareVideoRecorder()) {
                        mRecorder.start();
                        mIsRecording = true;
                        new CountDownTimer(12000, 1000)
                        {
                            public void onTick(long millisUntilFinished)
                            {
                                int time = (int) (millisUntilFinished/1000);
                                if (time == 10){
                                    txtDescription.setVisibility(View.VISIBLE);
                                    txtDigit.setVisibility(View.VISIBLE);
                                    txtDescription.setText("Speak this digit step by step");
                                    txtDigit.setText(fourDigits[0] +" - "+ fourDigits[1] + " - " + fourDigits[2] + " - " + fourDigits[3]);
                                }else if (time == 7 ){
                                    txtDescription.setVisibility(View.GONE);
                                    txtDigit.setVisibility(View.GONE);
                                }else if (time == 6){
                                    txtDescription.setVisibility(View.VISIBLE);
                                    imgDirection.setVisibility(View.VISIBLE);
                                    if (digit == 0){
                                        imgDirection.setImageResource(R.drawable.ic_left);
                                        txtDescription.setText("Turn your head to left");
                                    }else if (digit == 1){
                                        imgDirection.setImageResource(R.drawable.ic_right);
                                        txtDescription.setText("Turn your head to right");
                                    }else if (digit == 2){
                                        imgDirection.setImageResource(R.drawable.ic_down);
                                        txtDescription.setText("Turn your head to down");
                                    }
                                }else if (time == 4){
                                    txtDescription.setVisibility(View.GONE);
                                    imgDirection.setVisibility(View.GONE);
                                }else  if (time == 3){
                                    txtDescription.setVisibility(View.VISIBLE);
                                    imgDirection.setVisibility(View.VISIBLE);
                                    int num = digit+1;
                                    if (num == 3){
                                        num = 0;
                                    }
                                    if (num == 0){
                                        imgDirection.setImageResource(R.drawable.ic_left);
                                        txtDescription.setText("Turn your head to left");
                                    }else if (num == 1){
                                        imgDirection.setImageResource(R.drawable.ic_right);
                                        txtDescription.setText("Turn your head to right");
                                    }else if (num == 2){
                                        imgDirection.setImageResource(R.drawable.ic_down);
                                        txtDescription.setText("Turn your head to down");
                                    }
                                }else  if (time == 1){
                                    txtDescription.setVisibility(View.GONE);
                                    imgDirection.setVisibility(View.GONE);
                                }
                                Log.e("timer",String.valueOf(time));
                            }
                            public void onFinish()
                            {
                                stopRecording();
                                mIsRecording = false;
                            }
                        }.start();

                    } else {
                        releaseMediaRecorder();
                    }
                }
            }
        });

        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VideoLivenessActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
    private void init(){
        mSurfaceView = findViewById(R.id.surfaceView);
        btnRecord = findViewById(R.id.btnRecord);
        img_frame = findViewById(R.id.img_frame);
        imgDirection = findViewById(R.id.imgDirection);
        txtDescription = findViewById(R.id.txtDescription);
        txtDigit = findViewById(R.id.txtDigit);
        txtDescription.setVisibility(View.GONE);
        txtDigit.setVisibility(View.GONE);
        imgDirection.setVisibility(View.GONE);
        processingView = findViewById(R.id.processingView);
        btBack = findViewById(R.id.btBack);
    }
    private void stopRecording() {
        mRecorder.stop();  // stop the recording
        releaseMediaRecorder(); // release the MediaRecorder object
        mCamera.lock();
        img_frame.setImageResource(R.drawable.ic_undetected6);
        btnRecord.setImageResource(R.drawable.ic_unrecord);
        rotateVideo();
        mIsRecording = false;
    }
    private void rotateVideo(){
        loadingDialog = new LoadingDialog(VideoLivenessActivity.this, false);
        processingView.setVisibility(View.VISIBLE);
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String rotateFileName = "rotated_file"+timeStamp+"_"+".mp4";
        rotateFilePath = VideoLivenessActivity.this.getFilesDir().getPath().toString() + "/" + rotateFileName;
        rotatedVideo = new File(rotateFilePath);
        new Mp4Composer(videoFilePath, rotateFilePath)
                .rotation(Rotation.ROTATION_270)
                .size(640,480)
                .fillMode(FillMode.PRESERVE_ASPECT_FIT)
                .listener(new Mp4Composer.Listener() {
                    @Override
                    public void onProgress(double progress) {
                        Log.d(TAG, "onProgress = " + progress);
                    }
                    @Override
                    public void onCompleted() {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    @RequiresApi(api = Build.VERSION_CODES.O)
                                    public void run() {
                                        if (Target.equals("videoLiveness")){
//                                            uploadVideoToServer();
                                        }else if (Target.equals("SanctionPEP")){

                                        }
                                    }
                                });
                            }
                        });
                    }
                    @Override
                    public void onCanceled() {
                        Log.d(TAG, "onCanceled");
                        runOnUiThread(new Runnable() {
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    @RequiresApi(api = Build.VERSION_CODES.O)
                                    public void run() {
                                        loadingDialog.hide();
                                        processingView.setVisibility(View.GONE);
                                        Toast.makeText(VideoLivenessActivity.this, "VideoLiveness checking failed, Please try again ", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        });
                    }

                    @Override
                    public void onFailed(Exception exception) {
                        Log.wtf(TAG, "onFailed()", exception);
                        runOnUiThread(new Runnable() {
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    @RequiresApi(api = Build.VERSION_CODES.O)
                                    public void run() {
                                        loadingDialog.hide();
                                        processingView.setVisibility(View.GONE);
                                        Toast.makeText(VideoLivenessActivity.this, "VideoLiveness checking failed, Please try again", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        });
                    }
                })
                .start();
    }
//    private void uploadVideoToServer() {
//        RequestBody reqFile = RequestBody.create(MediaType.parse("video/mp4"),
//                rotatedVideo);
//        Call<PhotoLivenessResponse> call = RetrofitClient
//                .getInstance().getApi().videoLivenessCheck(reqFile);
//        call.enqueue(new Callback<PhotoLivenessResponse>() {
//            @Override
//            public void onResponse(Call<PhotoLivenessResponse> call, Response<PhotoLivenessResponse> response) {
//                PhotoLivenessResponse photoLivenessResponse = response.body();
//                int statuscode = Integer.parseInt(photoLivenessResponse.getStatusCode());
//                processingView.setVisibility(View.GONE);
//                loadingDialog.hide();
//                if (statuscode == 200) {
//                    AppConstants.photoLivenessResponse = photoLivenessResponse;
//                    Float score= Float.parseFloat(photoLivenessResponse.getScore());
//                    Float threshold= Float.parseFloat(photoLivenessResponse.getThreshold());
//                    Float result = score - threshold;
//                    if (result > 0){
//                        Intent intent_liveness = new Intent(VideoLivenessActivity.this, VideoResultActivity.class);
//                        intent_liveness.putExtra("scoreResult", "ok");
//                        startActivity(intent_liveness);
//                    }else{
//                        Intent intent_video = new Intent(VideoLivenessActivity.this, VideoResultActivity.class);
//                        intent_video.putExtra("scoreResult", "fail");
//                        startActivity(intent_video);
//                    }
//                } else {
//                    Toast.makeText(VideoLivenessActivity.this, "VideoLiveness checking failed, Please try again", Toast.LENGTH_LONG).show();
////                    Intent intent_video = new Intent(VideoLivenessActivity.this, VideoResultActivity.class);
////                    intent_video.putExtra("scoreResult", "no");
////                    startActivity(intent_video);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<PhotoLivenessResponse> call, Throwable t) {
//                Toast.makeText(VideoLivenessActivity.this, "Server is not working now", Toast.LENGTH_LONG).show();
//                loadingDialog.hide();
//                processingView.setVisibility(View.GONE);
//            }
//        });
//
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseMediaRecorder();
        releaseCamera();
    }

    private Camera getCamera() {
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        for (int camIdx = 0; camIdx < Camera.getNumberOfCameras(); camIdx++) {
            Camera.getCameraInfo(camIdx, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                try {
                    return mCamera = Camera.open(camIdx);
                } catch (RuntimeException e) {
                    Log.e("cameras", "Camera failed to open: " + e.getLocalizedMessage());
                }
            }
        }
        return null;
    }
    @Override
    protected void onPause() {
        super.onPause();
        releaseMediaRecorder();
        releaseCamera();              // release the camera immediately on pause event
    }
    @Override
    public void onResume(){
        super.onResume();
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
                releaseMediaRecorder();
                releaseCamera();
            }
        });
        mCamera = getCamera();
    }
    @Override
    public void onStop(){
        super.onStop();
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
                releaseMediaRecorder();
                releaseCamera();
            }
        });
        mCamera = getCamera();
    }

    private Camera.Size getBestPreviewSize(Camera.Parameters parameters) {
        Camera.Size result=null;
        for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
            if(size.width < size.height) continue; //we are only interested in landscape variants
            if (result == null) {
                result = size;
            }
            else {
                int resultArea = result.width*result.height;
                int newArea = size.width*size.height;

                if (newArea > resultArea) {
                    result = size;
                }
            }
        }
        return(result);
    }

    private boolean prepareVideoRecorder(){
        mRecorder = new MediaRecorder();
        mCamera.unlock();
        mRecorder.setCamera(mCamera);
        mRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
        mRecorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);
//        mRecorder.setOrientationHint(180);
        CamcorderProfile profile = CamcorderProfile.get(Camera.CameraInfo.CAMERA_FACING_FRONT, CamcorderProfile.QUALITY_HIGH);
        mRecorder.setProfile(profile);
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        videoFileName = "android_videoLiveness"+timeStamp+"_"+".mp4";
        videoFilePath = VideoLivenessActivity.this.getFilesDir().getPath().toString() + "/" + videoFileName;
        videoFile = new File(videoFilePath);
        mRecorder.setOutputFile(videoFilePath);
        mRecorder.setVideoSize(mPreviewWidth, mPreviewHeight);
        mRecorder.setVideoEncodingBitRate(5000000);

        mRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());
        try {
            mRecorder.prepare();
        } catch (IllegalStateException e) {
            Toast.makeText(getApplicationContext(), "exception: " + e.getMessage(), Toast.LENGTH_LONG).show();
            releaseMediaRecorder();
            return false;
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "exception: " + e.getMessage(), Toast.LENGTH_LONG).show();
            releaseMediaRecorder();
            return false;
        }
        return true;
    }
    private void releaseMediaRecorder(){
        if (mRecorder != null) {
            mRecorder.reset();
            mRecorder.release();
            mRecorder = null;
            mCamera.lock();
        }
    }
    private void releaseCamera(){
        if (mCamera != null){
            mCamera.release();
            mCamera = null;
        }
    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Camera.Parameters parameters = mCamera.getParameters();
//        parameters.set("scene-mode", "night");
//        parameters.setExposureCompensation(parameters.getMinExposureCompensation());
//        if(parameters.isAutoExposureLockSupported()) {
//            parameters.setAutoExposureLock(false);
//        }
        List<String> focusModes = parameters.getSupportedFocusModes();
        if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        } else if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        }
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            mCamera.setDisplayOrientation(90);
            parameters.setRotation(90);
        }else{
            mCamera.setDisplayOrientation(0);
            parameters.setRotation(0);
        }
        parameters.setRecordingHint(true);
        Camera.Size size = getBestPreviewSize(parameters);
        mCamera.setParameters(parameters);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mPreviewHeight = mCamera.getParameters().getPreviewSize().height;
        mPreviewWidth = mCamera.getParameters().getPreviewSize().width;
        mCamera.stopPreview();
        try {
            mCamera.setPreviewDisplay(mSurfaceHolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mCamera.startPreview();
    }
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (mIsRecording) {
            stopRecording();
        }
        releaseMediaRecorder();
        releaseCamera();
    }

}


