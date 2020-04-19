package ir.ac.sku.www.sessapplication.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

@SuppressLint("Registered")
public class MyActivity extends AppCompatActivity {
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }
}
