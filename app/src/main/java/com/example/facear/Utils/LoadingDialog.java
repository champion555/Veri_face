package com.example.facear.Utils;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.facear.R;

public class LoadingDialog extends Dialog {
    private Dialog mpd = null;
    private View view_lyt;

    public LoadingDialog(Context context, boolean cancelable) {
        super(context);
        showDialog(context, cancelable);
    }

    @SuppressLint("InflateParams")
    private void showDialog(Context context, boolean cancelable) {
        LayoutInflater lyt_Inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        boolean newView = false;
        if(view_lyt != null){
            Log.d("Reub", "Reloading the loading circle");
        }else{
            Log.d("Reub", "Creating the loading circle");
            newView = true;
            view_lyt = lyt_Inflater.inflate(R.layout.loading_dialog,null);
        }

        ImageView img = view_lyt.findViewById(R.id.img);
        img.setColorFilter(Color.rgb(255, 255, 255));

        Animation rotation = AnimationUtils.loadAnimation(context, R.anim.progress_animation);
        rotation.setRepeatCount(Animation.INFINITE);
        img.startAnimation(rotation);

        try {
            mpd = new Dialog(context, R.style.Theme_Dialog);
            mpd.requestWindowFeature(Window.FEATURE_NO_TITLE);
            if(newView){
                mpd.setContentView(view_lyt);
            }
            mpd.setCancelable(cancelable);
            mpd.show();
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    public void hide() {
        if (mpd != null) {
            if (mpd.isShowing()) {
                mpd.dismiss();
            }
        }
    }
}
