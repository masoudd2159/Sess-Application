package ir.ac.sku.www.sessapplication.fragment.dialogfragment;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import butterknife.BindView;
import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.base.BaseDialogFragment;
import ir.ac.sku.www.sessapplication.model.SendInformation;

public class DialogFragmentInstantMessage extends BaseDialogFragment {

    @BindView(R.id.instantMessage_Sender) TextView tv_Sender;
    @BindView(R.id.instantMessage_Date) TextView tv_Date;
    @BindView(R.id.instantMessage_Subject) TextView tv_Subject;
    @BindView(R.id.instantMessage_Message) TextView tv_Message;
    @BindView(R.id.instantMessage_NextAndClose) Button btn_NextAndClose;

    private SendInformation.Result instantMessage;

    public DialogFragmentInstantMessage(SendInformation.Result instantMessage) {
        this.instantMessage = instantMessage;
    }

    @Override
    public int getLayoutResource() {
        return R.layout.dialog_instant_message;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tv_Message.setMovementMethod(new ScrollingMovementMethod());
        setInstantMessageInformation((instantMessage.getInstantMessage().size() - 1));
    }

    @Override public void onResume() {
        super.onResume();
        changeDialogSize((int) (getDisplayMetrics().widthPixels * 0.85), ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    private void setInstantMessageInformation(final int index) {
        adaptInstantMessageInformation(index);
        if (index == 0) {
            btn_NextAndClose.setText("بستن");
            btn_NextAndClose.setOnClickListener(v -> dismiss());
        } else {
            btn_NextAndClose.setText("بعدی");
            btn_NextAndClose.setOnClickListener(v -> setInstantMessageInformation(index - 1));
        }
    }

    private void adaptInstantMessageInformation(int index) {
        tv_Sender.setText(instantMessage.getInstantMessage().get(index).getSender());
        tv_Date.setText(instantMessage.getInstantMessage().get(index).getDate());
        tv_Subject.setText(instantMessage.getInstantMessage().get(index).getSubject());

        tv_Message.scrollTo(0, 0);
        tv_Message.setText(instantMessage.getInstantMessage().get(index).getText());

        if (instantMessage.getInstantMessage().get(index).getAttachment()) {
            tv_Subject.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_attachment_1, 0, R.drawable.ic_subject, 0);
        } else if (!instantMessage.getInstantMessage().get(index).getAttachment()) {
            tv_Subject.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_subject, 0);
        }
    }
}
