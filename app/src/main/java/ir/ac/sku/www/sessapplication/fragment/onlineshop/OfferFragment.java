package ir.ac.sku.www.sessapplication.fragment.onlineshop;

import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.yarolegovich.discretescrollview.DSVOrientation;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.adapters.OfferVerticalAdapter;
import ir.ac.sku.www.sessapplication.adapters.SliderAdapter;
import ir.ac.sku.www.sessapplication.base.BaseFragment;
import ir.ac.sku.www.sessapplication.model.DetailsModel;
import ir.ac.sku.www.sessapplication.model.OfferModel;
import me.relex.circleindicator.CircleIndicator2;

public class OfferFragment extends BaseFragment {

    @BindView(R.id.fragmentOffer_DiscreteScrollView) DiscreteScrollView scrollView;
    @BindView(R.id.fragmentOffer_RecyclerView) RecyclerView recyclerView;
    @BindView(R.id.fragmentOffer_PagerIndicator) CircleIndicator2 pagerIndicator;

    private List<String> pictureURL = new ArrayList<String>();

    @Override protected int getLayoutResource() {
        return R.layout.fragment_offer;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pictureURL.add("img_1");
        pictureURL.add("img_2");
        pictureURL.add("img_3");
        pictureURL.add("img_4");
        setUpDiscreteScrollView(pictureURL);
        setUpRecyclerView(Arrays.asList(new Gson().fromJson(readTextFile(getResources().openRawResource(R.raw.offer)), OfferModel[].class)));
    }

    private void setUpDiscreteScrollView(List<String> pictureURL) {

        scrollView.setOrientation(DSVOrientation.HORIZONTAL);
        scrollView.setSlideOnFling(true);
        scrollView.setAdapter(new SliderAdapter(getContext(), pictureURL));
        scrollView.setItemTransformer(new ScaleTransformer.Builder()
                .setMinScale(0.8f)
                .build());

        PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(scrollView);

        pagerIndicator.attachToRecyclerView(scrollView, pagerSnapHelper);

    }

    private void setUpRecyclerView(List<OfferModel> items) {
        recyclerView.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_animation_from_right));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new OfferVerticalAdapter(getContext(), items));
    }

    public String readTextFile(InputStream inputStream) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        byte[] buf = new byte[1024];
        int len;
        try {
            while ((len = inputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, len);
            }
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputStream.toString();
    }
}