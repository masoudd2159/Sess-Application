package ir.ac.sku.www.sessapplication.fragment.dialogfragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import butterknife.BindView;
import butterknife.OnClick;
import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.base.BaseDialogFragment;
import ir.ac.sku.www.sessapplication.model.AlbumModel;
import ir.ac.sku.www.sessapplication.utils.Tools;

@SuppressLint("NonConstantResourceId")
public class DialogFragmentShowImage extends BaseDialogFragment {
    private final AlbumModel.Picture items;
    @BindView(R.id.image) ImageView image;
    @BindView(R.id.close) Button close;
    @BindView(R.id.description) TextView description;

    public DialogFragmentShowImage(AlbumModel.Picture albumModel) {
        this.items = albumModel;
    }

    @Override public int getLayoutResource() {
        return R.layout.dialog_show_image;
    }

    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Tools.displayImageOriginal(
                view.getContext(),
                image,
                "https://app.sku.ac.ir/SkuPic/" + items.getImage());
        description.setText(items.getDescription());
    }

    @Override
    public void onResume() {
        super.onResume();
        changeDialogSize((int) (getDisplayMetrics().widthPixels * 0.85), ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @OnClick(R.id.close)
    public void onClickItemClose(View view) {
        getDialog().dismiss();
    }
}
