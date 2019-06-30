package ir.ac.sku.www.sessapplication.activities.television;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

import ir.ac.sku.www.sessapplication.API.MyConfig;
import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.utils.MyActivity;

public class Channel3Activity extends MyActivity {

    private VideoView videoView;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel3);
        changeStatusBarColor();

        videoView = (VideoView) findViewById(R.id.channel3Activity_VideoView);
    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        videoView.setMediaController(new MediaController(this));
        videoView.setVideoURI(Uri.parse(MyConfig.CHANNEL_3));
        videoView.requestFocus();
        videoView.start();

        progressDialog = new ProgressDialog(Channel3Activity.this);
        progressDialog.setMessage("درحال بارگذاری اطلاعات ...");
        progressDialog.setCancelable(true);
        progressDialog.show();


        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            public void onPrepared(MediaPlayer mp) {
                progressDialog.dismiss();
            }
        });
    }
}
