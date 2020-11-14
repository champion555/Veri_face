package com.example.facear.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
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
import com.example.facear.FaceActivity;
import com.example.facear.IDDocCamera.CameraPreview;
import com.example.facear.IDDocCamera.CameraUtils;
import com.example.facear.Models.AuthenticationResponse;
import com.example.facear.Models.IDCardVeriResponse;
import com.example.facear.Models.ImageBlurryArray;
import com.example.facear.Models.ImageCheckResponse;
import com.example.facear.R;
import com.example.facear.Services.RetrofitClient;
import com.example.facear.Utils.AppConstants;
import com.example.facear.Utils.CoreApp;
import com.example.facear.Utils.LoadingDialog;
import com.example.facear.Utils.Preferences;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.annotation.Target;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IDDocCameraActivity extends AppCompatActivity implements View.OnClickListener{
    private CameraPreview cameraPreview;
    private ImageView btnCapture,btnFlash;
    private TextView txtMessage,txtTitle,txtErrorMessage,title,topTitle, txtCountry;
    private ImageView imgCrop,btnBack;
    private RelativeLayout cropRelative,cameraRelative;
    private LinearLayout linErrorMessage;
    private Button btnTake,btnRetake;
    private IDDocView cropView;
    private static final String TAG = IDDocCameraActivity.class.getSimpleName();
    private Bitmap demoBitmap, mCropBitmap;
    private Bundle bundle;
    private Bitmap frontbitmap,backbitmap;
    int width;
    String mergedPhotoFileName,mergedPhotoFilePath, docType;
    File mergedPhotoFile;
    private LoadingDialog loadingDialog;
    String imgFileName,imgFilePath,CardType;
    File imgFile;
    File demoFile;
    RequestBody reqFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iddoc_camera);
        BackgroundService.mContext = IDDocCameraActivity.this;
        init();
        listener();
        bundle = getIntent().getExtras();
        CardType = bundle.getString("CardType");
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        RelativeLayout.LayoutParams layoutParams;
        layoutParams = new RelativeLayout.LayoutParams((int) width, (int) height);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        cameraPreview.setLayoutParams(layoutParams);
        linErrorMessage.setVisibility(View.GONE);
        txtCountry.setText(AppConstants.countryName);
        if (CardType.equals("FaceMatch_FrontCard")){
            topTitle.setText("ID Card");
            txtTitle.setText("Powered by BIOMIID");
            title.setText("Front ID Card");
            txtMessage.setText("Place the Front Page of  ID Card inside \n the Frame and take the photo");
        }else if (CardType.equals("faceMatch_Passport")){
            txtTitle.setText("Powered by BIOMIID");
            topTitle.setText("Passport");
            title.setText("Passport");
            txtMessage.setText("Place the Passport inside the Frame and take the photo");
        }else if (CardType.equals("faceMatch_FrontDriving")){
            txtTitle.setText("Powered by BIOMIID");
            topTitle.setText("Driving License");
            title.setText("Front Driving License");
            txtMessage.setText("Place the Front Page of  Driving License \n inside the Frame and take the photo");
        }else if (CardType.equals("faceMatch_frontResident")){
            txtTitle.setText("Powered by BIOMIID");
            topTitle.setText("Resident Permit");
            title.setText("Front Resident Permit");
            txtMessage.setText("Place the Front Page of  Resident Permit \n inside the Frame and take the photo");
        }else if (CardType.equals("IDcard_FrontCard")){
            txtTitle.setText("Powered by BIOMIID");
            topTitle.setText("ID Card");
            title.setText("Front ID Card");
            txtMessage.setText("Place the Front Page of  ID Card inside \n the Frame and take the photo");
        }else if (CardType.equals("Passport")){
            txtTitle.setText("Powered by BIOMIID");
            topTitle.setText("Passport");
            title.setText("Passport");
            txtMessage.setText("Place the Passport inside the Frame and take the photo");
        }else if (CardType.equals("Driving_FrontCard")){
            txtTitle.setText("Powered by BIOMIID");
            topTitle.setText("Driving License");
            title.setText("Front Driving License");
            txtMessage.setText("Place the Front Page of  Driving License \n inside the Frame and take the photo");
        }else if (CardType.equals("Resident_Front")){
            txtTitle.setText("Powered by BIOMIID");
            topTitle.setText("Resident Permit");
            title.setText("Front Resident Permit");
            txtMessage.setText("Place the Front Page of  Resident Permit \n inside the Frame and take the photo");
        }
    }
    private void init(){
        cameraPreview = (CameraPreview) findViewById(R.id.camera_surface);
        btnCapture = findViewById(R.id.btnCapture);
        txtMessage = findViewById(R.id.txtMessage);
        txtTitle = findViewById(R.id.txtTitle);
        imgCrop = findViewById(R.id.imgCrop);
        cropRelative = findViewById(R.id.cropRel);
        cameraRelative = findViewById(R.id.cameraRel);
        btnTake = findViewById(R.id.btnTake);
        btnRetake = findViewById(R.id.btnRetake);
        cropView = findViewById(R.id.custom_view);
        btnFlash = findViewById(R.id.imgFlash);
        linErrorMessage = findViewById(R.id.linMessage);
        txtErrorMessage = findViewById(R.id.errorMessage);
        title = findViewById(R.id.title);
        btnBack = findViewById(R.id.btnBack);
        topTitle = findViewById(R.id.topTitle);
        txtCountry = findViewById(R.id.txtCountry);
    }
    private void listener(){
        cameraPreview.setOnClickListener(this);
        btnCapture.setOnClickListener(this);
        btnTake.setOnClickListener(this);
        btnRetake.setOnClickListener(this);
        btnFlash.setOnClickListener(this);
        btnBack.setOnClickListener(this);
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
                                String demoImageFileName = "demoImage.jpg";
                                String imgDemoFilePath = IDDocCameraActivity.this.getFilesDir().getPath().toString() + "/" + demoImageFileName;
                                demoFile = new File(imgDemoFilePath);
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
                                Toast.makeText(IDDocCameraActivity.this, "Id card capture was failed", Toast.LENGTH_LONG).show();
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
                float crop_left = cropView.rect.left;
                float crop_top = cropView.rect.top;
                float crop_width = cropView.rect.width();
                float crop_height = cropView.rect.height();
                mCropBitmap = Bitmap.createBitmap(demoBitmap,
                        (int) (crop_left),
                        (int) (crop_top),
                        (int) (crop_width),
                        (int) (crop_height));
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                imgFileName = "IDDoc"+timeStamp+"_"+".jpg";
                imgFilePath = IDDocCameraActivity.this.getFilesDir().getPath().toString() + "/" + imgFileName;
                imgFile = new File(imgFilePath);
                Log.i(TAG, "" + imgFile);
                if (imgFile.exists())
                    imgFile.delete();
                try {
                    FileOutputStream out = new FileOutputStream(imgFile);
                    Log.e("out", String.valueOf(out));
                    mCropBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    out.flush();
                    out.close();

                    if (CardType.equals("faceMatch_Passport")){
                        Preferences.setValue(IDDocCameraActivity.this, Preferences.FACEMATCHIDPATH, imgFilePath);
                        Intent intent = new Intent(IDDocCameraActivity.this, IntentActivity.class);
                        intent.putExtra("intentTarget","faceMatchID");
                        startActivity(intent);
                        finish();
                    }
                    if (CardType.equals("FaceMatch_FrontCard")){
//                        AppConstants.faceMatch_frontCardPath = imgFilePath;
                        frontbitmap = BitmapFactory.decodeFile(imgFilePath);
                        CardType = "FaceMatch_BackCard";
                        topTitle.setText("ID Card");
                        title.setText("Back ID Card");
                        txtMessage.setText("Place the Back page of ID Card inside \n the Frame and take the photo");

                    }else if(CardType.equals("FaceMatch_BackCard")) {
                        backbitmap = BitmapFactory.decodeFile(imgFilePath);
                        createSingleImageFromMultipleImages(frontbitmap,backbitmap);
                    }
                    if (CardType.equals("faceMatch_FrontDriving")){
                        frontbitmap = BitmapFactory.decodeFile(imgFilePath);
                        CardType = "faceMatch_BackDriving";
                        topTitle.setText("Driving License");
                        title.setText("Back Driving License");
                        txtMessage.setText("Place the Back page of Driving License inside \n the Frame and take the photo");
                    }else if (CardType.equals("faceMatch_BackDriving")){
                        backbitmap = BitmapFactory.decodeFile(imgFilePath);
                        createSingleImageFromMultipleImages(frontbitmap,backbitmap);
                    }
                    if (CardType.equals("faceMatch_frontResident")){
                        frontbitmap = BitmapFactory.decodeFile(imgFilePath);
                        CardType = "faceMatch_backResident";
                        topTitle.setText("Resident Permit");
                        title.setText("Back Resident Permit");
                        txtMessage.setText("Place the Back page of Resident Permit inside \n the Frame and take the photo");
                    }else if (CardType.equals("faceMatch_backResident")){
                        backbitmap = BitmapFactory.decodeFile(imgFilePath);
                        createSingleImageFromMultipleImages(frontbitmap,backbitmap);
                    }
                    //Verification ID Document
                    if (CardType.equals("IDcard_FrontCard")){
                        frontbitmap = BitmapFactory.decodeFile(imgFilePath);
                        CardType = "IDcard_BackCard";
                        topTitle.setText("ID Card");
                        title.setText("Back ID Card");
                        txtMessage.setText("Place the Back page of ID Card inside \n the Frame and take the photo");
                    }else if (CardType.equals("IDcard_BackCard")){
                        backbitmap = BitmapFactory.decodeFile(imgFilePath);
                        createSingleImageFromMultipleImages(frontbitmap,backbitmap);
                    }
                    if (CardType.equals("Passport")){
                        VerificationIDCardToServer();
                    }
                    if (CardType.equals("Driving_FrontCard")){
                        frontbitmap = BitmapFactory.decodeFile(imgFilePath);
                        CardType = "Driving_BackCard";
                        topTitle.setText("Driving License");
                        title.setText("Back Driving License");
                        txtMessage.setText("Place the Back page of Driving License inside \n the Frame and take the photo");
                    }else if (CardType.equals("Driving_BackCard")){
                        backbitmap = BitmapFactory.decodeFile(imgFilePath);
                        createSingleImageFromMultipleImages(frontbitmap,backbitmap);
                    }
                    if (CardType.equals("Resident_Front")){
                        frontbitmap = BitmapFactory.decodeFile(imgFilePath);
                        CardType = "Resident_Back";
                        topTitle.setText("Resident Permit");
                        title.setText("Back Resident Permit");
                        txtMessage.setText("Place the Back page of Resident Permit inside \n the Frame and take the photo");
                    }else if (CardType.equals("Resident_Back")){
                        backbitmap = BitmapFactory.decodeFile(imgFilePath);
                        createSingleImageFromMultipleImages(frontbitmap,backbitmap);
                    }


                    cameraRelative.setVisibility(View.VISIBLE);
                    cropRelative.setVisibility(View.GONE);
                    btnCapture.setVisibility(View.VISIBLE);
                    btnRetake.setVisibility(View.GONE);
                    btnTake.setVisibility(View.GONE);
                    linErrorMessage.setVisibility(View.GONE);
                    btnFlash.setVisibility(View.VISIBLE);
                    txtTitle.setText("Powered by BIOMID");
                    txtMessage.setVisibility(View.VISIBLE);
                    title.setVisibility(View.VISIBLE);
                    txtCountry.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btnRetake:
                cameraRelative.setVisibility(View.VISIBLE);
                cropRelative.setVisibility(View.GONE);
                btnCapture.setVisibility(View.VISIBLE);
                btnRetake.setVisibility(View.GONE);
                btnTake.setVisibility(View.GONE);
                linErrorMessage.setVisibility(View.GONE);
                btnFlash.setVisibility(View.VISIBLE);
                txtTitle.setText("Powered by BIOMID");
                txtMessage.setVisibility(View.VISIBLE);
                title.setVisibility(View.VISIBLE);
                txtCountry.setVisibility(View.VISIBLE);
                // face Match To ID Docuement
                if (CardType.equals("faceMatch_Passport")){
                    txtTitle.setText("Powered by BIOMIID");
                    topTitle.setText("Passport");
                    txtMessage.setText("Place the Passport inside the Frame and take the photo");
                }
                if (CardType.equals("FaceMatch_FrontCard")){
                    txtTitle.setText("Powered by BIOMID");
                    topTitle.setText("ID Card");
                    txtMessage.setText("Place the Front Page of  ID Card inside \n the Frame and take the photo");
                }else if (CardType.equals("FaceMatch_BackCard")){
                    txtTitle.setText("Powered by BIOMID");
                    topTitle.setText("ID Card");
                    txtMessage.setText("Place the Back page of ID Card inside \n the Frame and take the photo");
                }
                if (CardType.equals("faceMatch_FrontDriving")){
                    txtTitle.setText("Powered by BIOMIID");
                    topTitle.setText("Driving License");
                    txtMessage.setText("Place the Front Page of  Driving License \n inside the Frame and take the photo");
                }else if (CardType.equals("faceMatch_BackDriving")){
                    txtTitle.setText("Powered by BIOMIID");
                    topTitle.setText("Driving License");
                    txtMessage.setText("Place the Back Page of  Driving License \n inside the Frame and take the photo");
                }
                if (CardType.equals("faceMatch_frontResident")){
                    txtTitle.setText("Powered by BIOMIID");
                    topTitle.setText("Resident Permit");
                    txtMessage.setText("Place the Front Page of  Resident Permit \n inside the Frame and take the photo");
                }else if (CardType.equals("faceMatch_backResident")){
                    txtTitle.setText("Powered by BIOMIID");
                    topTitle.setText("Resident Permit");
                    txtMessage.setText("Place the Back Page of  Resident Permit \n inside the Frame and take the photo");
                }
                // Verification ID Docuement
                if (CardType.equals("IDcard_FrontCard")){
                    txtTitle.setText("Powered by BIOMID");
                    topTitle.setText("ID Card");
                    txtMessage.setText("Place the Front Page of  ID Card inside \n the Frame and take the photo");
                }else if (CardType.equals("IDcard_BackCard")){
                    txtTitle.setText("Powered by BIOMID");
                    topTitle.setText("ID Card");
                    txtMessage.setText("Place the Back page of ID Card inside \n the Frame and take the photo");
                }
                if (CardType.equals("Passport")){
                    txtTitle.setText("Powered by BIOMID");
                    topTitle.setText("Passport");
                    txtMessage.setText("Place the Passport inside the Frame and take the photo");
                }
                if (CardType.equals("Driving_FrontCard")){
                    txtTitle.setText("Powered by BIOMID");
                    topTitle.setText("Driving License");
                    txtMessage.setText("Place the Front Page of  Driving License \n inside the Frame and take the photo");
                }else if (CardType.equals("Driving_BackCard")){
                    txtTitle.setText("Powered by BIOMID");
                    topTitle.setText("Driving License");
                    txtMessage.setText("Place the Back Page of  Driving License \n inside the Frame and take the photo");
                }
                if (CardType.equals("Resident_Front")){
                    txtTitle.setText("Powered by BIOMID");
                    topTitle.setText("Resident Permit");
                    txtMessage.setText("Place the Front Page of  Resident Permit \n inside the Frame and take the photo");
                }else if (CardType.equals("Resident_Back")){
                    txtTitle.setText("Powered by BIOMID");
                    topTitle.setText("Resident Permit");
                    txtMessage.setText("Place the Back Page of  Resident Permit \n inside the Frame and take the photo");
                }
                break;
            case R.id.imgFlash:
                if (CameraUtils.hasFlash(this)) {
                    boolean isFlashOn = cameraPreview.switchFlashLight();
                    btnFlash.setImageResource(isFlashOn ? R.drawable.camera_flash_on : R.drawable.camera_flash_off);
                } else {
                    Toast.makeText(this, "The device does not support flash", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnBack:
                if (CardType.equals("faceMatch_Passport") || CardType.equals("FaceMatch_FrontCard") ||CardType.equals("FaceMatch_BackCard")|| CardType.equals("faceMatch_FrontDriving") ||CardType.equals("faceMatch_BackDriving")||CardType.equals("faceMatch_frontResident")||CardType.equals("faceMatch_backResident")){
                    Intent intent = new Intent(IDDocCameraActivity.this,IDDocMainActivity.class);
                    intent.putExtra("Target", "FaceMatchToIDDoc");
                    startActivity(intent);
                }else if (CardType.equals("IDcard_FrontCard")||CardType.equals("IDcard_BackCard")||CardType.equals("Passport")||CardType.equals("Driving_FrontCard")||CardType.equals("Driving_BackCard")||CardType.equals("Resident_Front")||CardType.equals("Resident_Back")) {
                    Intent intent = new Intent(IDDocCameraActivity.this,IDDocMainActivity.class);
                    intent.putExtra("Target", "IDDocVerification");
                    startActivity(intent);
                }
                break;
        }
    }

    private Bitmap createSingleImageFromMultipleImages(Bitmap firstImage, Bitmap secondImage) {
        if(firstImage.getWidth() < secondImage.getWidth()){
            width = firstImage.getWidth();
        }else {
            width = secondImage.getWidth();
        }
        Bitmap resultPhoto = Bitmap.createBitmap(width, firstImage.getHeight()+secondImage.getHeight(), firstImage.getConfig());
        Canvas canvas = new Canvas(resultPhoto);
        canvas.drawBitmap(firstImage, 0f, 0f, null);
        canvas.drawBitmap(secondImage, 0f, firstImage.getHeight(), null);
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        mergedPhotoFileName = "mergedCard"+timeStamp+"_"+".jpg";
        mergedPhotoFilePath = IDDocCameraActivity.this.getFilesDir().getPath().toString() + "/" + mergedPhotoFileName;
        mergedPhotoFile = new File(mergedPhotoFilePath);
        if (mergedPhotoFile.exists())
            mergedPhotoFile.delete();
        try {
            FileOutputStream out = new FileOutputStream(mergedPhotoFile);
            Log.e("out", String.valueOf(out));
            resultPhoto.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            if(CardType.equals("FaceMatch_BackCard")||CardType.equals("faceMatch_BackDriving")||CardType.equals("faceMatch_backResident")){
                Preferences.setValue(IDDocCameraActivity.this, Preferences.FACEMATCHIDPATH, mergedPhotoFilePath);
                Intent intent = new Intent(IDDocCameraActivity.this, IntentActivity.class);
                intent.putExtra("intentTarget","faceMatchID");
                startActivity(intent);
                finish();
            }else if (CardType.equals("IDcard_BackCard")||CardType.equals("Driving_BackCard")||CardType.equals("Resident_Back")){
                VerificationIDCardToServer();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultPhoto;
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
        if (cameraPreview != null) {
            cameraPreview.onStop();
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
                txtCountry.setVisibility(View.GONE);
                topTitle.setText("Image Preview");
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
                    title.setVisibility(View.VISIBLE);
                    txtTitle.setText("Preview Captured ID Document");
                    txtMessage.setText("Make sure the ID Document image is clear to read");
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
                    txtMessage.setVisibility(View.GONE);
                    title.setVisibility(View.GONE);
                    btnRetake.setVisibility(View.VISIBLE);
                    btnTake.setVisibility(View.GONE);
                    linErrorMessage.setVisibility(View.VISIBLE);
                    txtTitle.setText("Preview Captured ID Document");
                    imgCrop.setImageBitmap(demoBitmap);
                }
            }

            @Override
            public void onFailure(Call<ImageCheckResponse> call, Throwable t) {
                Toast.makeText(IDDocCameraActivity.this, "Server is not working now", Toast.LENGTH_LONG).show();
//                loadingDialog.hide();
            }
        });

    }
    private void VerificationIDCardToServer() {
        if (CardType.equals("Passport")){
            reqFile = RequestBody.create(MediaType.parse("image/jpeg"),
                    imgFile);
        }else{
            reqFile = RequestBody.create(MediaType.parse("image/jpeg"),
                    mergedPhotoFile);
        }
        loadingDialog = new LoadingDialog(IDDocCameraActivity.this, false);
        Call<IDCardVeriResponse> call = RetrofitClient
                .getInstance().getApi().IDCardVerification(reqFile);
        call.enqueue(new Callback<IDCardVeriResponse>() {
            @Override
            public void onResponse(Call<IDCardVeriResponse> call, Response<IDCardVeriResponse> response) {
                IDCardVeriResponse idCardVeriResponse = response.body();
                String status = idCardVeriResponse.getStatus();
                if (status.equals("SUCCESS")) {
                    loadingDialog.hide();
                    AppConstants.idCardVeriResponse = idCardVeriResponse;
                    if (CardType.equals("Passport")){
                        docType = "Passport";
                    }else if (CardType.equals("IDcard_BackCard")){
                        docType = "ID Card";
                    }else if (CardType.equals("Driving_BackCard")){
                        docType = "Driving License";
                    }else  if (CardType.equals("Resident_Back")){
                        docType = "Resident Permit";
                    }
                    Intent intent = new Intent(IDDocCameraActivity.this,IDCardVeriResultActivity.class);
                    intent.putExtra("IDCardType",docType);
                    startActivity(intent);
                } else {
                    loadingDialog.hide();
                    Toast.makeText(IDDocCameraActivity.this, "Bad input MRZ image!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<IDCardVeriResponse> call, Throwable t) {
                Toast.makeText(IDDocCameraActivity.this, "Server is not working now", Toast.LENGTH_LONG).show();
                loadingDialog.hide();
            }
        });

    }
}