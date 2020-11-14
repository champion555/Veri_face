/*
 * Copyright (c) 2017 Razeware LLC
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish, 
 * distribute, sublicense, create a derivative work, and/or sell copies of the 
 * Software in any work that is designed, intended, or marketed for pedagogical or 
 * instructional purposes related to programming, coding, application development, 
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works, 
 * or sale is expressly withheld.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.example.facear;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.RemoteException;
import android.provider.Settings;
import android.util.Log;
import android.view.PixelCopy;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.facear.Activity.FaceMatchResultActivity;
import com.example.facear.Activity.IDDocCameraActivity;
import com.example.facear.Activity.IntentActivity;
import com.example.facear.Activity.MainActivity;
import com.example.facear.Activity.ResultActivity;
import com.example.facear.Models.AuthenticationResponse;
import com.example.facear.Models.FaceMatchIDResponse;
import com.example.facear.Models.ImageBlurryArray;
import com.example.facear.Models.ImageCheckResponse;
import com.example.facear.Models.PhotoLivenessResponse;
import com.example.facear.Models.UserAuthenticationResponse;
import com.example.facear.Models.UserEnrollmentResponse;
import com.example.facear.CustomView.CustomView;
import com.example.facear.Services.RetrofitClient;
import com.example.facear.Utils.AppConstants;
import com.example.facear.Utils.CoreApp;
import com.example.facear.Utils.LoadingDialog;
import com.example.facear.Utils.Preferences;
import com.example.facear.camera.CameraSourcePreview;
import com.example.facear.camera.GraphicOverlay;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public final class FaceActivity extends AppCompatActivity {

  private static final String TAG = "FaceActivity";
  private static final int RC_HANDLE_GMS = 9001;
  private static final int RC_HANDLE_CAMERA_PERM = 255;
  private CameraSource mCameraSource = null;
  private CameraSourcePreview mPreview;
  private GraphicOverlay mGraphicOverlay;
  private boolean mIsFrontFacing = true;
  private CustomView ovalView;
  private static final int MY_CAMERA_PERMISSION_CODE = 100;
  private RelativeLayout faceView,processingView;
  private TextView txtTitle,txtMessage;
  private ImageView btnBack,btBright;
  private String isEnrollment;
  private String isAuthentication;
  LoadingDialog loadingDialog;
  Bundle bundle;
  String imgFileName;
  String imgFilePath;
  String isEnrollCheck = "";
  String faceTarget;
  File imgFile;
  boolean isdetected = false;
  boolean userProfile = false;
  boolean faceMatch = false;
  boolean isBright = false;
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.d(TAG, "onCreate called.");
    setContentView(R.layout.activity_face);
    init();
    onBack();
    onBright();
    createCameraSource();
    bundle = getIntent().getExtras();
    faceTarget = bundle.getString("faceTarget");
    isEnrollCheck = bundle.getString("isEnrollCheck");
    userProfile = bundle.getBoolean("userProfile");
    faceMatch = bundle.getBoolean("faceMatch");
    if (faceTarget.equals("faceLiveness")){
      txtTitle.setText("Face Liveness");
    }else if (faceTarget.equals("userEnrollment")){
      txtTitle.setText("Enrollment");
    }else if (faceTarget.equals("userAuthentication")){
      txtTitle.setText("Authentication");
    }else if (faceTarget.equals("faceMatchToIDCard")){
      txtTitle.setText("Face Match ID Card");
    }else if (faceTarget.equals("SanctionPEP")){
      txtTitle.setText("Face Liveness");
    }
  }
  private void init(){
    ovalView = findViewById(R.id.custom_view);
    faceView = findViewById(R.id.faceView);
    mPreview = (CameraSourcePreview) findViewById(R.id.preview);
    mGraphicOverlay = (GraphicOverlay) findViewById(R.id.faceOverlay);
    processingView = findViewById(R.id.processingView);
    processingView.setVisibility(View.GONE);
//    faceView.setBackground(getResources().getDrawable(R.drawable.ic_undetected3));
    faceView.setBackground(getResources().getDrawable(R.drawable.ic_undetected5));
    txtTitle = findViewById(R.id.txtTitle);
    txtMessage = findViewById(R.id.txtMessage);
    btnBack = findViewById(R.id.btBack);
    btBright = findViewById(R.id.btBright);
  }
  private void onBack(){
    btnBack.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(FaceActivity.this, MainActivity.class);
        startActivity(intent);
      }
    });
  }
  private void  onBright(){
    btBright.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (isBright == false){
          btBright.setImageResource(R.drawable.ic_highbright);
          isBright = true;
          WindowManager.LayoutParams lp = getWindow().getAttributes();
          float brightness=1.0f;
          lp.screenBrightness = brightness;
          getWindow().setAttributes(lp);

        }else {
          btBright.setImageResource(R.drawable.ic_bright);
          isBright = false;
          WindowManager.LayoutParams lp = getWindow().getAttributes();
          float brightness=0.3f;
          lp.screenBrightness = brightness;
          getWindow().setAttributes(lp);
        }
      }
    });
  }

    @Override
  protected void onResume() {
    super.onResume();
    Log.d(TAG, "onResume called.");
    startCameraSource();
  }
  @Override
  protected void onPause() {
    super.onPause();
    mPreview.stop();
  }
  @Override
  public void onSaveInstanceState(Bundle savedInstanceState) {
    super.onSaveInstanceState(savedInstanceState);
    savedInstanceState.putBoolean("IsFrontFacing", mIsFrontFacing);
  }
  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (mCameraSource != null) {
      mCameraSource.release();
    }
  }
  private void createCameraSource() {
    Log.d(TAG, "createCameraSource called.");
    Context context = getApplicationContext();
    FaceDetector detector = createFaceDetector(context);
    int facing = CameraSource.CAMERA_FACING_FRONT;
    mCameraSource = new CameraSource.Builder(context, detector)
            .setFacing(facing)
//            .setRequestedPreviewSize(640, 480)
            .setRequestedFps(60.0f)
            .setAutoFocusEnabled(true)
            .build();
  }
  private void startCameraSource() {
    Log.d(TAG, "startCameraSource called.");
    int code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(
      getApplicationContext());
    if (code != ConnectionResult.SUCCESS) {
      Dialog dlg = GoogleApiAvailability.getInstance().getErrorDialog(this, code, RC_HANDLE_GMS);
      dlg.show();
    }
    if (mCameraSource != null) {
      try {
        mPreview.start(mCameraSource, mGraphicOverlay);
      } catch (IOException e) {
        Log.e(TAG, "Unable to start camera source.", e);
        mCameraSource.release();
        mCameraSource = null;
      }
    }
  }
  @NonNull
  private FaceDetector createFaceDetector(final Context context) {
    Log.d(TAG, "createFaceDetector called.");
    FaceDetector detector = new FaceDetector.Builder(context)
      .setLandmarkType(FaceDetector.ALL_LANDMARKS)
      .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
      .setTrackingEnabled(true)
      .setMode(FaceDetector.FAST_MODE)
      .setProminentFaceOnly(mIsFrontFacing)
      .setMinFaceSize(mIsFrontFacing ? 0.35f : 0.15f)
      .build();

    final MultiProcessor.Factory<Face> factory = new MultiProcessor.Factory<Face>() {
      @Override
      public Tracker<Face> create(Face face) {
        return new FaceTracker(mGraphicOverlay, FaceActivity.this, mIsFrontFacing);
      }
    };
    Detector.Processor<Face> processor = new MultiProcessor.Builder<>(factory).build();
    detector.setProcessor(processor);

    if (!detector.isOperational()) {
      Log.w(TAG, "Face detector dependencies are not yet available.");
      IntentFilter lowStorageFilter = new IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW);
      boolean hasLowStorage = registerReceiver(null, lowStorageFilter) != null;
      if (hasLowStorage) {
        Log.w(TAG, getString(R.string.low_storage_error));
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int id) {
            finish();
          }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.app_name)
          .setMessage(R.string.low_storage_error)
          .setPositiveButton(R.string.disappointed_ok, listener)
          .show();
      }
    }
    return detector;
  }
  public void detectedFace(Float left,Float right,Float top,Float bottom) {
    float oval_left = ovalView.oval.left;
    float oval_right = ovalView.oval.right;
    float oval_top = ovalView.oval.top;
    float oval_bottom = ovalView.oval.bottom;
    float height_space = ovalView.oval.height()*10/100;
    float width_space = ovalView.oval.width();
    Log.e("limited_space:","width"+width_space+"height"+height_space);
//    Log.e("oval size:","oval_left:"+oval_left+"oval_right:"+oval_right+"oval_top:"+oval_top+"oval_bottom:"+oval_bottom);
//    Log.e("face coordinate:","left:"+left+"right:"+right+"top:"+top+"bottom:"+bottom);
    float left_space = left - oval_left;
    float right_space = right - oval_right;
    float top_space = top - oval_top;
    float bottom_space = oval_bottom - bottom;
    Log.e("space coordinate:","left:"+left_space+"right:"+right_space+"top:"+top_space+"bottom:"+bottom_space);
    if (0 < top_space && top_space < height_space && 0 < bottom_space && bottom_space < height_space){
      runOnUiThread(new Runnable() {
        public void run() {
          runOnUiThread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            public void run() {
//              faceView.setBackground(getResources().getDrawable(R.drawable.ic_detected3));
              faceView.setBackground(getResources().getDrawable(R.drawable.ic_detected5));
              txtMessage.setText("please keep your head in the frame to start selfie capture");
              isdetected = true;
              captureImage();
            }
          });
        }
      });
    }else if(top_space > height_space && bottom_space > height_space){
      runOnUiThread(new Runnable() {
        public void run() {
          runOnUiThread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            public void run() {
              isdetected = false;
//              faceView.setBackground(getResources().getDrawable(R.drawable.ic_undetected3));
              faceView.setBackground(getResources().getDrawable(R.drawable.ic_undetected5));
              txtMessage.setText("Please move closer the face ");
            }
          });
        }
      });
    }else {
      isdetected = false;
//      faceView.setBackground(getResources().getDrawable(R.drawable.ic_undetected3));
      faceView.setBackground(getResources().getDrawable(R.drawable.ic_undetected5));
      txtMessage.setText("Please place your face on the oval\n and get closer to the device");
    }
  }
  @RequiresApi(api = Build.VERSION_CODES.N)
  private void captureImage() {
    SurfaceView view = mPreview.mSurfaceView;
//    Log.e("screen size:","width:"+ view.getWidth()+"height"+ view.getHeight());
    final Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),
            Bitmap.Config.ARGB_8888);
    final HandlerThread handlerThread = new HandlerThread("PixelCopier");
    handlerThread.start();
    PixelCopy.request(view, bitmap, new PixelCopy.OnPixelCopyFinishedListener() {
      @Override
      public void onPixelCopyFinished(int copyResult) {
        if (copyResult == PixelCopy.SUCCESS) {
          Log.e(TAG, bitmap.toString());
          runOnUiThread(new Runnable() {
            @Override
            public void run() {
              if (isdetected) {
                imgFileName = "capturedPhoto.jpg";
                imgFilePath = FaceActivity.this.getFilesDir().getPath().toString() + "/" + imgFileName;
                imgFile = new File(imgFilePath);
                Log.i(TAG, "" + imgFile);
                if (imgFile.exists())
                  imgFile.delete();
                try {
                  FileOutputStream out = new FileOutputStream(imgFile);
                  Log.e("out", String.valueOf(out));
                  bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                  out.flush();
                  out.close();
//                  AuthenticationToServer();

                  if (faceTarget.equals("faceLiveness")){
                    uploadPhotoToServer();
                  }else if (faceTarget.equals("userEnrollment")){
                    uploadUserEnrollmentToServer();
                  }else if (faceTarget.equals("userAuthentication")){
                    uploadUserAuthenticationToServer();
                  }else if (faceTarget.equals("faceMatchToIDCard")){
                    FaceMatchIDCardToServer();
                  }else if (faceTarget.equals("SanctionPEP")){
                    Log.e("adfadsfasdf","adsadgadsfasd");
                  }
                } catch (Exception e) {
                  e.printStackTrace();
                }
              }
            }
          });
        } else {
          Log.d(TAG, "Failed to take the photo from camera");
        }
        handlerThread.quitSafely();
      }
    }, new Handler(handlerThread.getLooper()));
  }

  private void uploadPhotoToServer() {
    final RequestBody reqFile = RequestBody.create(MediaType.parse("image/jpeg"),
            imgFile);
    loadingDialog = new LoadingDialog(FaceActivity.this, false);
    processingView.setVisibility(View.VISIBLE);
    Call<PhotoLivenessResponse> call = RetrofitClient
            .getInstance().getApi().photoLivenessCheck(reqFile);
    call.enqueue(new Callback<PhotoLivenessResponse>() {
      @Override
      public void onResponse(Call<PhotoLivenessResponse> call, Response<PhotoLivenessResponse> response) {
        PhotoLivenessResponse photoLivenessResponse = response.body();
        String result = photoLivenessResponse.getResult();
        processingView.setVisibility(View.GONE);
        loadingDialog.hide();
        AppConstants.photoLivenessResponse = photoLivenessResponse;
        Intent intent = new Intent(FaceActivity.this,ResultActivity.class);
        intent.putExtra("faceTarget",faceTarget);
        startActivity(intent);
      }

      @Override
      public void onFailure(Call<PhotoLivenessResponse> call, Throwable t) {
        Toast.makeText(FaceActivity.this, "Server is not working now", Toast.LENGTH_LONG).show();
        isdetected = false;
        loadingDialog.hide();
        processingView.setVisibility(View.GONE);
      }
    });

  }
  private void uploadUserEnrollmentToServer() {
    String aaa = Preferences.getValue_String(FaceActivity.this, Preferences.UserName);
    RequestBody reqFile = RequestBody.create(MediaType.parse("image/jpeg"), imgFile);
    RequestBody username = RequestBody.create(MediaType.parse("multipart/form-data"), Preferences.getValue_String(FaceActivity.this, Preferences.UserName));
    RequestBody force_enrollment = RequestBody.create(MediaType.parse("multipart/form-data"), isEnrollCheck);
    loadingDialog = new LoadingDialog(FaceActivity.this, false);
    processingView.setVisibility(View.VISIBLE);
    Call<UserEnrollmentResponse> call = RetrofitClient
            .getInstance().getApi().faceEnroll(reqFile, username, force_enrollment);
    call.enqueue(new Callback<UserEnrollmentResponse>() {
      @Override
      public void onResponse(Call<UserEnrollmentResponse> call, Response<UserEnrollmentResponse> response) {
        loadingDialog.hide();
        processingView.setVisibility(View.GONE);
        UserEnrollmentResponse userEnrollmentResponse = response.body();
        int statuscode = Integer.parseInt(userEnrollmentResponse.getStatusCode());
        if (statuscode == 200) {
          isEnrollment = "success";
          Intent intent_enrollment = new Intent(FaceActivity.this, ResultActivity.class);
          intent_enrollment.putExtra("isEnrollment", isEnrollment);
          intent_enrollment.putExtra("faceTarget",faceTarget);
          intent_enrollment.putExtra("isEnrollCheck",isEnrollCheck);
          startActivity(intent_enrollment);
        }else if (statuscode == 404){
          String message = userEnrollmentResponse.getMessage();
          if (message.equals("Spoof detected")){
            isEnrollment = "failed";
            Intent intent_enrollment = new Intent(FaceActivity.this, ResultActivity.class);
            intent_enrollment.putExtra("isEnrollment", isEnrollment);
            intent_enrollment.putExtra("faceTarget",faceTarget);
            intent_enrollment.putExtra("isEnrollCheck",isEnrollCheck);
            startActivity(intent_enrollment);
          }else if (message.equals("Unable to confirm liveness")){
            isEnrollment = "unable";
            Intent intent_enrollment = new Intent(FaceActivity.this, ResultActivity.class);
            intent_enrollment.putExtra("isEnrollment", isEnrollment);
            intent_enrollment.putExtra("isEnrollCheck",isEnrollCheck);
            intent_enrollment.putExtra("faceTarget",faceTarget);
            startActivity(intent_enrollment);
          }else {
            Toast.makeText(FaceActivity.this, userEnrollmentResponse.getMessage(), Toast.LENGTH_LONG).show();
          }
        }
      }
      @Override
      public void onFailure(Call<UserEnrollmentResponse> call, Throwable t) {
        Toast.makeText(FaceActivity.this, "Server is not working now", Toast.LENGTH_LONG).show();
        isdetected = false;
        loadingDialog.hide();
        processingView.setVisibility(View.GONE);
      }
    });
  }
  private void uploadUserAuthenticationToServer(){
    RequestBody reqFile = RequestBody.create(MediaType.parse("image/jpeg"), imgFile);
    RequestBody username = RequestBody.create(MediaType.parse("multipart/form-data"), Preferences.getValue_String(FaceActivity.this, Preferences.UserName));
    loadingDialog = new LoadingDialog(FaceActivity.this, false);
    processingView.setVisibility(View.VISIBLE);
    Call<UserAuthenticationResponse> call = RetrofitClient
            .getInstance().getApi().userAuthentication(reqFile,username);
    call.enqueue(new Callback<UserAuthenticationResponse>() {
      @Override
      public void onResponse(Call<UserAuthenticationResponse> call, Response<UserAuthenticationResponse> response) {
        loadingDialog.hide();
        processingView.setVisibility(View.GONE);
        UserAuthenticationResponse userAuthenticationResponse = response.body();
        AppConstants.userAuthenticationResponse = userAuthenticationResponse;
        int statuscode = Integer.parseInt(userAuthenticationResponse.getStatusCode());
        if (statuscode == 200) {
          Double authentication_score= Double.parseDouble(userAuthenticationResponse.getScore());
          Double authentication_threshold= Double.parseDouble(userAuthenticationResponse.getFace_matching_threshold());
          Double authe_result = authentication_score - authentication_threshold;
          if (authe_result > 0){
            isAuthentication = "success";
            Intent intent = new Intent(FaceActivity.this, ResultActivity.class);
            intent.putExtra("isAuthentication", isAuthentication);
            intent.putExtra("faceTarget",faceTarget);
            startActivity(intent);
          }else {
            isAuthentication = "no_success";
            Intent intent = new Intent(FaceActivity.this, ResultActivity.class);
            intent.putExtra("isAuthentication", isAuthentication);
            intent.putExtra("faceTarget",faceTarget);
            startActivity(intent);
          }
        }else if (statuscode == 404){
          String message = userAuthenticationResponse.getMessage();
          if (message.equals("Spoof detected")){
            isAuthentication = "failed";
            Intent intent = new Intent(FaceActivity.this, ResultActivity.class);
            intent.putExtra("isAuthentication", isAuthentication);
            intent.putExtra("faceTarget",faceTarget);
            startActivity(intent);
          }else if (message.equals("Unable to confirm liveness")){
            isAuthentication = "unable";
            Intent intent = new Intent(FaceActivity.this, ResultActivity.class);
            intent.putExtra("isAuthentication", isAuthentication);
            intent.putExtra("faceTarget",faceTarget);
            startActivity(intent);
          }else {
            Toast.makeText(FaceActivity.this, userAuthenticationResponse.getMessage(), Toast.LENGTH_LONG).show();
          }
        }

//        Intent intent = new Intent(FaceActivity.this,ResultActivity.class);
//        intent.putExtra("faceTarget",faceTarget);
//        startActivity(intent);

      }
      @Override
      public void onFailure(Call<UserAuthenticationResponse> call, Throwable t) {
        Toast.makeText(FaceActivity.this, "Server is not working now", Toast.LENGTH_LONG).show();
        isdetected = false;
        loadingDialog.hide();
        processingView.setVisibility(View.GONE);
      }
    });

  }
  private void FaceMatchIDCardToServer() {
    String target_file_path = Preferences.getValue_String(FaceActivity.this, Preferences.FACEMATCHIDPATH);
    File target_file = new File(target_file_path);
    RequestBody sourceFile = RequestBody.create(MediaType.parse("image/jpeg"), imgFile);
    RequestBody targetFile = RequestBody.create(MediaType.parse("image/jpeg"), target_file);
    loadingDialog = new LoadingDialog(FaceActivity.this, false);
    processingView.setVisibility(View.VISIBLE);
    Call<FaceMatchIDResponse> call = RetrofitClient
            .getInstance().getApi().faceMatchIDCard(sourceFile, targetFile);
    call.enqueue(new Callback<FaceMatchIDResponse>() {
      @Override
      public void onResponse(Call<FaceMatchIDResponse> call, Response<FaceMatchIDResponse> response) {
        loadingDialog.hide();
        processingView.setVisibility(View.GONE);
        FaceMatchIDResponse faceMatchIDResponse = response.body();
        String status = faceMatchIDResponse.getStatus();

        int statuscode = Integer.parseInt(faceMatchIDResponse.getStatusCode());
        if (status.equals("SUCCESS")) {
          AppConstants.faceMatchIDResponse = faceMatchIDResponse;
          Intent intent = new Intent(FaceActivity.this, FaceMatchResultActivity.class);
          startActivity(intent);

        } else {
          Toast.makeText(FaceActivity.this, "Anyone of the input images may be not available for face matching!", Toast.LENGTH_LONG).show();
        }
      }
      @Override
      public void onFailure(Call<FaceMatchIDResponse> call, Throwable t) {
        Toast.makeText(FaceActivity.this, "Server is not working now", Toast.LENGTH_LONG).show();
        isdetected = false;
        loadingDialog.hide();
        processingView.setVisibility(View.GONE);
      }
    });
  }
  public void showAlert() {
    AlertDialog.Builder dialog = new AlertDialog.Builder(FaceActivity.this);
    dialog.setTitle(FaceActivity.this.getResources().getString(R.string.app_name));
    dialog.setTitle("User Authentication Failed");
    dialog.setMessage("This user is not exists in this enrollment. please go to enrollment and then enroll with your face.");
    dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        Intent intent = new Intent(FaceActivity.this, MainActivity.class);
        startActivity(intent);
      }
    });
    dialog.show();
  }
  public void livenessFailedMessage() {
    AlertDialog.Builder dialog = new AlertDialog.Builder(FaceActivity.this);
    dialog.setTitle(FaceActivity.this.getResources().getString(R.string.app_name));
    dialog.setTitle("Liveness failed");
    dialog.setMessage("Please try again");
    dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        isdetected = false;
      }
    });
    dialog.show();
  }
}