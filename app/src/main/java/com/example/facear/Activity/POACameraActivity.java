package com.example.facear.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.PixelCopy;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.facear.BackgroundService.BackgroundService;
import com.example.facear.CustomView.IDDocView;
import com.example.facear.IDDocCamera.CameraPreview;
import com.example.facear.IDDocCamera.CameraUtils;
import com.example.facear.Models.AuthenticationResponse;
import com.example.facear.Models.ImageBlurryArray;
import com.example.facear.Models.ImageCheckResponse;
import com.example.facear.R;
import com.example.facear.Services.RetrofitClient;
import com.example.facear.Utils.AppConstants;
import com.example.facear.Utils.CoreApp;
import com.example.facear.Utils.Preferences;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class POACameraActivity extends AppCompatActivity implements View.OnClickListener{
    private CameraPreview cameraPreview;
    private ImageView btnCapture,btnFlash;
    private TextView txtErrorMessage;
    private ImageView imgCrop,btBack;
    private RelativeLayout cropRelative,cameraRelative;
    private LinearLayout linErrorMessage;
    private Button btnTake,btnRetake;
    private IDDocView cropView;
    private static final String TAG = POACameraActivity.class.getSimpleName();
    private Bitmap demoBitmap, mCropBitmap;
    private Bundle bundle;
    //    private LoadingDialog loadingDialog;
    String imgFileName,imgFilePath;
    File imgFile;
    File demoFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poa_camera);
        BackgroundService.mContext = POACameraActivity.this;
        init();
        listener();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        RelativeLayout.LayoutParams layoutParams;
        layoutParams = new RelativeLayout.LayoutParams((int) width, (int) height);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        cameraPreview.setLayoutParams(layoutParams);
        linErrorMessage.setVisibility(View.GONE);
    }
    private void init(){
        cameraPreview = findViewById(R.id.camera_surface);
        btnCapture = findViewById(R.id.btnCapture);
//        txtMessage = findViewById(R.id.txtMessage);
//        txtTitle = findViewById(R.id.txtTitle);
        imgCrop = findViewById(R.id.imgCrop);
        cropRelative = findViewById(R.id.cropRel);
        cameraRelative = findViewById(R.id.cameraRel);
        btnTake = findViewById(R.id.btnTake);
        btnRetake = findViewById(R.id.btnRetake);
        cropView = findViewById(R.id.custom_view);
        btnFlash = findViewById(R.id.imgFlash);
        linErrorMessage = findViewById(R.id.linMessage);
        txtErrorMessage = findViewById(R.id.errorMessage);
        btBack = findViewById(R.id.btnBack);
    }
    private void listener(){
        cameraPreview.setOnClickListener(this);
        btnCapture.setOnClickListener(this);
        btnTake.setOnClickListener(this);
        btnRetake.setOnClickListener(this);
        btnFlash.setOnClickListener(this);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void captureImage() {
        SurfaceView view = cameraPreview;
        Log.e("screen size:","width:"+ cameraPreview.getWidth()+"height"+ cameraPreview.getHeight());
        demoBitmap = Bitmap.createBitmap(cameraPreview.getWidth(), cameraPreview.getHeight(),
                Bitmap.Config.ARGB_8888);
        final HandlerThread handlerThread = new HandlerThread("PixelCopier");
        handlerThread.start();
        PixelCopy.request(view, demoBitmap, new PixelCopy.OnPixelCopyFinishedListener() {
            @Override
            public void onPixelCopyFinished(int copyResult) {
                if (copyResult == PixelCopy.SUCCESS) {
                    Log.e(TAG, demoBitmap.toString());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (demoBitmap != null){
                                imgFileName = "POADoc.jpg";
                                imgFilePath = POACameraActivity.this.getFilesDir().getPath().toString() + "/" + imgFileName;
                                demoFile = new File(imgFilePath);
                                Log.i(TAG, "" + demoFile);
                                if (demoFile.exists())
                                    demoFile.delete();
                                try {
                                    FileOutputStream out = new FileOutputStream(demoFile);
                                    Log.e("out", String.valueOf(out));
                                    demoBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                                    out.flush();
                                    out.close();
                                    ImageCheckToServer();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }else {
                                Toast.makeText(POACameraActivity.this, "Id card capture was failed", Toast.LENGTH_LONG).show();
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
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.camera_surface:
                cameraPreview.focus();
                break;
            case R.id.btnCapture:
                captureImage();
                break;
            case R.id.btnTake:
                AppConstants.POADoc_path = imgFilePath;
                Intent intent = new Intent(POACameraActivity.this, IntentActivity.class);
                intent.putExtra("intentTarget","POA");
                startActivity(intent);
                break;
            case R.id.btnRetake:
                cameraRelative.setVisibility(View.VISIBLE);
                cropRelative.setVisibility(View.GONE);
                btnCapture.setVisibility(View.VISIBLE);
                btnRetake.setVisibility(View.GONE);
                btnTake.setVisibility(View.GONE);
                linErrorMessage.setVisibility(View.GONE);
                btnFlash.setVisibility(View.VISIBLE);
                break;
            case R.id.imgFlash:
                if (CameraUtils.hasFlash(this)) {
                    boolean isFlashOn = cameraPreview.switchFlashLight();
                    btnFlash.setImageResource(isFlashOn ? R.drawable.camera_flash_on : R.drawable.camera_flash_off);
                } else {
                    Toast.makeText(this, "The device does not support flash", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btBack:
                Intent intent_poa = new Intent(POACameraActivity.this, POADocumentActivity.class);
                startActivity(intent_poa);
                break;
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (cameraPreview != null) {
            cameraPreview.onStart();
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
//        if (cameraPreview != null) {
//            cameraPreview.onStop();
//        }
    }
    @Override
    public void onPause() {
        super.onPause();
        if (cameraPreview != null) {
            cameraPreview.onStop();
            cameraPreview = null;
        }
    }
    private void ImageCheckToServer() {
        RequestBody reqFile = RequestBody.create(MediaType.parse("image/jpeg"),
                demoFile);
        Call<ImageCheckResponse> call = RetrofitClient
                .getInstance().getApi().imageCheck(reqFile);
        call.enqueue(new Callback<ImageCheckResponse>() {
            @Override
            public void onResponse(Call<ImageCheckResponse> call, Response<ImageCheckResponse> response) {
                ImageCheckResponse imageCheckResponse = response.body();
                int statuscode = Integer.parseInt(imageCheckResponse.getStatusCode());
//                ImageBlurryArray err = imageCheckResponse.getErrorList().add();
                if (statuscode == 200) {
//                    loadingDialog.hide();
                    cameraRelative.setVisibility(View.GONE);
                    cropRelative.setVisibility(View.VISIBLE);
                    btnCapture.setVisibility(View.GONE);
                    btnFlash.setVisibility(View.GONE);
                    linErrorMessage.setVisibility(View.GONE);
                    btnRetake.setVisibility(View.VISIBLE);
                    btnTake.setVisibility(View.VISIBLE);
                    imgCrop.setImageBitmap(demoBitmap);
                } else {
//                    loadingDialog.hide();
                    List<ImageBlurryArray> errorList = imageCheckResponse.getErrorList();
                    int i = errorList.size();
                    if (i == 1){
                        String errorType1 = errorList.get(0).getErrorType();
                        if (errorType1.equals("ImageTooBlurry")){
                            txtErrorMessage.setText("- image too blur");
                        }else if (errorType1.equals("GlareFound")){
                            txtErrorMessage.setText("- Glares are detected on Image");
                        }
                    }else if (i == 2){
                        txtErrorMessage.setText("- Image too blur\n- Glares are detected on Image");
                    }
//                    for (int i = 0 ; i<= errorList.size(); i++){
//                        String errorType = errorList.get(i).getErrorType();
//                    }
                    cameraRelative.setVisibility(View.GONE);
                    cropRelative.setVisibility(View.VISIBLE);
                    btnCapture.setVisibility(View.GONE);
                    btnFlash.setVisibility(View.GONE);
                    btnRetake.setVisibility(View.VISIBLE);
                    btnTake.setVisibility(View.GONE);
                    linErrorMessage.setVisibility(View.VISIBLE);
                    imgCrop.setImageBitmap(demoBitmap);
                }
            }

            @Override
            public void onFailure(Call<ImageCheckResponse> call, Throwable t) {
                Toast.makeText(POACameraActivity.this, "Server is not working now", Toast.LENGTH_LONG).show();
//                loadingDialog.hide();
            }
        });

    }
}