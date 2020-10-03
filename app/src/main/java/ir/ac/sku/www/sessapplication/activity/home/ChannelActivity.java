package ir.ac.sku.www.sessapplication.activity.home;

import android.net.Uri;
import android.os.Bundle;

import com.devbrackets.android.exomedia.listener.OnPreparedListener;
import com.devbrackets.android.exomedia.ui.widget.VideoView;

import butterknife.BindView;
import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.base.BaseActivity;

public class ChannelActivity extends BaseActivity implements OnPreparedListener {

    @BindView(R.id.channelActivity_VideoView) VideoView videoView;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_channel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changeStatusBarColor();
        videoView.setOnPreparedListener(this);
        videoView.setVideoURI(Uri.parse(getIntent().getStringExtra("URL")));
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onPrepared() {
        videoView.start();
    }
}
