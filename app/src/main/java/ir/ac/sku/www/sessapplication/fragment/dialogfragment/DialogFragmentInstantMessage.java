package ir.ac.sku.www.sessapplication.fragment.dialogfragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import butterknife.BindView;
import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.base.BaseDialogFragment;
import ir.ac.sku.www.sessapplication.model.information.SendInformation;

@SuppressLint("NonConstantResourceId")
public class DialogFragmentInstantMessage extends BaseDialogFragment {

    private final SendInformation.Result instantMessage;
    @BindView(R.id.instantMessage_Sender) TextView textViewSender;
    @BindView(R.id.instantMessage_Date) TextView textViewDate;
    @BindView(R.id.instantMessage_Subject) TextView textViewSubject;
    @BindView(R.id.instantMessage_Message) TextView textViewMessage;
    @BindView(R.id.instantMessage_NextAndClose) Button buttonNextAndClose;

    public DialogFragmentInstantMessage(SendInformation.Result instantMessage) {
        this.instantMessage = instantMessage;
    }

    @Override
    public int getLayoutResource() {
        return R.layout.dialog_instant_message;
    }

    @Override
    public void onViewCreated(@NonNull View rootView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);
        if (!instantMessage.getInstantMessage().isEmpty()) {
            textViewMessage.setMovementMethod(new ScrollingMovementMethod());
            setInstantMessageInformation((instantMessage.getInstantMessage().size() - 1));
        } else {
            dismiss();
        }
    }

    @Override public void onResume() {
        super.onResume();
        changeDialogSize((int) (getDisplayMetrics().widthPixels * 0.75), ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    private void setInstantMessageInformation(final int index) {
        adaptInstantMessageInformation(index);
        if (index == 0) {
            buttonNextAndClose.setText("بستن");
            buttonNextAndClose.setOnClickListener(v -> dismiss());
        } else {
            buttonNextAndClose.setText("بعدی");
            buttonNextAndClose.setOnClickListener(v -> setInstantMessageInformation(index - 1));
        }
    }

    private void adaptInstantMessageInformation(int index) {
        textViewSender.setText(instantMessage.getInstantMessage().get(index).getSender());
        textViewDate.setText(instantMessage.getInstantMessage().get(index).getDate());
        textViewSubject.setText(instantMessage.getInstantMessage().get(index).getSubject());

        textViewMessage.scrollTo(0, 0);
        textViewMessage.setText(instantMessage.getInstantMessage().get(index).getText());

        if (instantMessage.getInstantMessage().get(index).isAttachment()) {
            textViewSubject.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_attachment_1, 0, R.drawable.ic_subject, 0);
        } else if (!instantMessage.getInstantMessage().get(index).isAttachment()) {
            textViewSubject.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_subject, 0);
        }
    }
}
