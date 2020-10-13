package ir.ac.sku.www.sessapplication.fragment.onlineshop;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.yarolegovich.discretescrollview.DSVOrientation;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.adapters.SliderAdapter;
import ir.ac.sku.www.sessapplication.base.BaseFragment;
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
}