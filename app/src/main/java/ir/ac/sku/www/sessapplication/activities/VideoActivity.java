package ir.ac.sku.www.sessapplication.activities;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.utils.MyActivity;
import ir.ac.sku.www.sessapplication.utils.PlayerService;

public class VideoActivity extends MyActivity implements ServiceConnection {

    private SimpleExoPlayerView mPlayerView;
    private PlayerService mPlayerService;
    private boolean mBound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        changeStatusBarColor();

        mPlayerView = (SimpleExoPlayerView) findViewById(R.id.videoActivity_SimpleExoPlayerView);
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
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, PlayerService.class);
        bindService(intent, this, Context.BIND_AUTO_CREATE);
        startService(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBound) {
            unbindService(this);
            mBound = false;
        }
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        PlayerService.MyBinder binder = (PlayerService.MyBinder) iBinder;
        mPlayerService = (PlayerService) binder.getService();
        mPlayerView.setPlayer(mPlayerService.getExoPlayer());

        if (getIntent().getStringExtra("mp4") != null &&
                (mPlayerService.getMediaUri() == null |
                        !(((mPlayerService.getMediaUri() == null) ? "null" : mPlayerService.getMediaUri().toString())
                                .equals((getIntent().getStringExtra("mp4") == null) ? "null" : getIntent().getStringExtra("mp4"))))) {
            mPlayerService.setMediaUri(Uri.parse(getIntent().getStringExtra("mp4")));
            mPlayerService.setMusicTitle(getIntent().getStringExtra("mp4Name"));
            PendingIntent contentPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, VideoActivity.class), 0);
            mPlayerService.setContentPendingIntent(contentPendingIntent);
            mPlayerService.preparePlayer();
        }

        mBound = true;
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        mBound = false;
    }
}
