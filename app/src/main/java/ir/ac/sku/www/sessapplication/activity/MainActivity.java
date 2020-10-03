package ir.ac.sku.www.sessapplication.activity;

import android.os.Bundle;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.base.BaseActivity;
import ir.ac.sku.www.sessapplication.fragment.FoodReservationFragment;
import ir.ac.sku.www.sessapplication.fragment.ProcessesFragment;
import ir.ac.sku.www.sessapplication.fragment.dialogfragment.DialogFragmentSignIn;
import ir.ac.sku.www.sessapplication.model.information.SendInformation;

public class MainActivity extends BaseActivity implements DialogFragmentSignIn.UserInterface {

    private Bundle extras;

    @Override protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        extras = getIntent().getExtras();
        openFragment();
    }

    private void openFragment() {
        if (extras != null) {
            if (extras.getInt("position") == 1) {

                setTitleToolbar(extras.getString("title"));

                FoodReservationFragment foodReservationFragment = new FoodReservationFragment();

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.layout_content, foodReservationFragment);
                fragmentTransaction.commit();
            } else if (extras.getInt("position") == 2) {

                setTitleToolbar(extras.getString("title"));

                ProcessesFragment processesFragment = new ProcessesFragment();

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.layout_content, processesFragment);
                fragmentTransaction.commit();
            }
        }
    }

    @Override public void addUserPersonalInfo(SendInformation.Result.UserInformation userInformation, String cookie) {

    }
}
