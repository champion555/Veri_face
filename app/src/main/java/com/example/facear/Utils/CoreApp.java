package com.example.facear.Utils;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;

public class CoreApp extends Application {
//    public static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();

//        int sessionTime = Integer.parseInt(Preferences.getValue_String(CoreApp.this, Preferences.user_session));
//        new CountDownTimer(sessionTime*60*1000, 1000) {
//            public void onTick(long millisUntilFinished) {
//            }
//            public void onFinish() {
////                showAlert();
//            }
//        }.start();
    }

    public static boolean isNetworkConnection(final Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        return connectivityManager.getActiveNetworkInfo() != null
                && connectivityManager.getActiveNetworkInfo().isAvailable()
                && connectivityManager.getActiveNetworkInfo().isConnected();
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
