package ir.ac.sku.www.sessapplication.fragment.onlineshop;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.adapters.CategoryAdapter;
import ir.ac.sku.www.sessapplication.base.BaseFragment;
import ir.ac.sku.www.sessapplication.model.CategoryModel;

public class CategoryFragment extends BaseFragment {
    @BindView(R.id.recyclerView_category) RecyclerView recyclerView;
    private List<CategoryModel> categoryModels = new ArrayList<>();

    @Override protected int getLayoutResource() {
        return R.layout.fragment_category;
    }

    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpData();
    }

    private void setUpData() {
        String[] array = getResources().getStringArray(R.array.category);
        for (int i = 0; i < array.length; i += 3) {
            CategoryModel model = new CategoryModel();
            model.setIcon(array[i]);
            model.setTitle(array[i + 1]);
            model.setChild(Boolean.parseBoolean(array[i + 2]));

            categoryModels.add(model);
        }
        setUpView();
    }

    private void setUpView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new CategoryAdapter(getContext(), categoryModels));
    }
}