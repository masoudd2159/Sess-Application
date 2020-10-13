package ir.ac.sku.www.sessapplication.fragment.onlineshop;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import butterknife.BindView;
import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.base.BaseFragment;

public class SearchFragment extends BaseFragment {

    @BindView(R.id.fragmentSearch_EditTextSearch) EditText editTextSearch;

    @Override protected int getLayoutResource() {
        return R.layout.fragment_search;
    }

    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

    }

    private void showInputMethod() {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }
}