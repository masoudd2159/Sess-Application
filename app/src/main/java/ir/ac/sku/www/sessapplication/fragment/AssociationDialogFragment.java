package ir.ac.sku.www.sessapplication.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.Objects;

import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.adapters.AdminAdapter;
import ir.ac.sku.www.sessapplication.models.AssociationModel;

public class AssociationDialogFragment extends DialogFragment {

    private View rootView;

    private TextView establishedYear;
    private TextView faName;
    private TextView enName;
    private TextView description;
    private TextView secretary;
    private ImageView logo;
    private ImageView email;
    private ImageView webPage;
    private ImageView phone;
    private ImageView twitter;
    private ImageView telegram;
    private ImageView instagram;
    private RelativeLayout layoutAdmin;
    private RecyclerView recyclerViewAdmins;

    private AssociationModel.Data model;

    public AssociationDialogFragment(AssociationModel.Data model) {
        this.model = model;
    }

    @SuppressLint("CommitPrefEdits")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.dialog_association, container, false);

        if (getDialog() != null && getDialog().isShowing())
            dismiss();

        init();

        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams layoutParams = getDialog().getWindow().getAttributes();
        layoutParams.dimAmount = 0.7f;
        getDialog().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getDialog().setCancelable(false);

        setUpView();

        return rootView;
    }

    private void setUpView() {
        if (!model.getEstablishedYear().equals("")) {
            establishedYear.setVisibility(View.VISIBLE);
            establishedYear.setText("سال تأسیس " + model.getEstablishedYear());
        }

        if (!model.getFaName().equals("")) {
            faName.setVisibility(View.VISIBLE);
            faName.setText(model.getFaName());
        }

        if (!model.getEnName().equals("")) {
            enName.setVisibility(View.VISIBLE);
            enName.setText(model.getEnName());
        }

        if (!model.getDescription().equals("")) {
            description.setVisibility(View.VISIBLE);
            description.setText(model.getDescription());
        }

        if (!model.getSecretary().equals("")) {
            secretary.setText("دبیر " + model.getSecretary());
            secretary.setVisibility(View.VISIBLE);
        }

        if (!model.getLogo().equals("")) {
            logo.setVisibility(View.VISIBLE);
            Glide
                    .with(rootView.getContext())
                    .load("https://app.sku.ac.ir/Logo/" + model.getLogo())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(logo)
            ;
        }

        if (!model.getEmail().equals("")) {
            email.setVisibility(View.VISIBLE);
            email.setOnClickListener(View -> {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", model.getEmail(), null));
                startActivity(Intent.createChooser(emailIntent, "ارسال ایمیل به " + model.getFaName()));
            });
        }

        if (!model.getPhone().equals("")) {
            phone.setVisibility(View.VISIBLE);
            phone.setOnClickListener(View -> {
                Intent intentPhone = new Intent(Intent.ACTION_VIEW);
                intentPhone.setData(Uri.parse("tel:" + model.getPhone()));
                startActivity(intentPhone);
            });
        }

        if (!model.getWebsite().equals("")) {
            webPage.setVisibility(View.VISIBLE);
            webPage.setOnClickListener(View -> {
                Intent intentWebsite = new Intent(Intent.ACTION_VIEW);
                intentWebsite.setData(Uri.parse(model.getWebsite()));
                startActivity(intentWebsite);
            });
        }

        if (!model.getTwitter().equals("")) {
            twitter.setVisibility(View.VISIBLE);
            twitter.setOnClickListener(View -> {
                Intent intentTwitter = new Intent(Intent.ACTION_VIEW);
                intentTwitter.setData(Uri.parse(model.getTwitter()));
                startActivity(intentTwitter);
            });
        }

        if (!model.getTelegram().equals("")) {
            telegram.setVisibility(View.VISIBLE);
            telegram.setOnClickListener(View -> {
                Intent intentTelegram = new Intent(Intent.ACTION_VIEW);
                intentTelegram.setData(Uri.parse(model.getTelegram()));
                startActivity(intentTelegram);
            });
        }

        if (!model.getInstagram().equals("")) {
            instagram.setVisibility(View.VISIBLE);
            instagram.setOnClickListener(View -> {
                Intent intentInstagram = new Intent(Intent.ACTION_VIEW);
                intentInstagram.setData(Uri.parse(model.getInstagram()));
                startActivity(intentInstagram);
            });
        }

        if (!(model.getAdmin().get(0).getName().equals(""))) {
            layoutAdmin.setVisibility(View.VISIBLE);
            recyclerViewAdmins.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(rootView.getContext(), R.anim.layout_animation_from_right));
            recyclerViewAdmins.setLayoutManager(new LinearLayoutManager(rootView.getContext(), LinearLayoutManager.VERTICAL, false));
            recyclerViewAdmins.setAdapter(new AdminAdapter(rootView.getContext(), model.getAdmin()));
        }
    }

    private void init() {
        establishedYear = rootView.findViewById(R.id.dialogAssociation_TextViewEstablishedYear);
        faName = rootView.findViewById(R.id.dialogAssociation_TextViewFaName);
        enName = rootView.findViewById(R.id.dialogAssociation_TextViewEnName);
        description = rootView.findViewById(R.id.dialogAssociation_TextViewDescriptions);
        secretary = rootView.findViewById(R.id.dialogAssociation_TextViewSecretary);
        logo = rootView.findViewById(R.id.dialogAssociation_CircularImageViewLogo);
        email = rootView.findViewById(R.id.dialogAssociation_LogoEmail);
        webPage = rootView.findViewById(R.id.dialogAssociation_LogoWebPage);
        phone = rootView.findViewById(R.id.dialogAssociation_LogoPhone);
        twitter = rootView.findViewById(R.id.dialogAssociation_LogoTwitter);
        telegram = rootView.findViewById(R.id.dialogAssociation_LogoTelegram);
        instagram = rootView.findViewById(R.id.dialogAssociation_LogoInstagram);
        layoutAdmin = rootView.findViewById(R.id.dialogAssociation_LayoutAdmin);
        recyclerViewAdmins = rootView.findViewById(R.id.dialogAssociation_RecyclerViewAdmins);
    }

    @Override
    public void onResume() {
        super.onResume();
        changeDialogSize();
    }

    private void changeDialogSize() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        Objects.requireNonNull(getActivity()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        Objects.requireNonNull(Objects.requireNonNull(getDialog()).getWindow()).setLayout(
                (int) (displayMetrics.widthPixels * 0.85),
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }
}
