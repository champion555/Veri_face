package com.example.facear.BackgroundService;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;

import com.example.facear.Activity.SigninActivity;
import com.example.facear.Utils.Preferences;

public class BackgroundService extends Service {
    private static final String TAG = "BackgroundService";
    public static Context mContext;
    public static CountDownTimer timer;
    @Override
    public void onCreate(){
        super.onCreate();
        try{
            int sessionTime = Integer.parseInt(Preferences.getValue_String(BackgroundService.this, Preferences.user_session));
//            new CountDownTimer(sessionTime*60*1000, 1000)
            new CountDownTimer(sessionTime*60*1000, 1000) {
                public void onTick(long millisUntilFinished) {
                }
                public void onFinish() {
//                    Intent intent = new Intent(mContext, KYC_LoginActivity.class);
                    Intent intent = new Intent(mContext, SigninActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    stopSelf();
                }
            }.start();
        } catch(NumberFormatException ex){}

    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
//    private void showAlert() {
//        android.app.AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
//        dialog.setTitle("User Session Time Out");
//        dialog.setMessage("User Session was time out,Please Login with your pin code again.");
//        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                Intent intent = new Intent(mContext, KYC_LoginActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//            }
//        });
//        dialog.show();
//    }
}
