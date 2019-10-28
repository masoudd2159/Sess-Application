package ir.ac.sku.www.sessapplication.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.models.SendInformation;

public class InstantMessage {

    private Context context;
    private SendInformation.Result instantMessage;
    private Dialog dialog;

    private TextView tv_Sender;
    private TextView tv_Date;
    private TextView tv_Subject;
    private TextView tv_Message;
    private Button btn_NextAndClose;


    public InstantMessage(Context context, SendInformation.Result instantMessage) {
        this.context = context;
        this.instantMessage = instantMessage;
    }

    public void showInstantMessageDialog() {
        if (!(instantMessage.getInstantMessage().size() <= 0)) {
            dialog = new Dialog(context);
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
            layoutParams.dimAmount = 0.7f;
            dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.dialog_instant_message);

            tv_Sender = dialog.findViewById(R.id.instantMessage_Sender);
            tv_Date = dialog.findViewById(R.id.instantMessage_Date);
            tv_Subject = dialog.findViewById(R.id.instantMessage_Subject);
            tv_Message = dialog.findViewById(R.id.instantMessage_Message);
            btn_NextAndClose = dialog.findViewById(R.id.instantMessage_NextAndClose);

            tv_Message.setMovementMethod(new ScrollingMovementMethod());

            setInstantMessageInformation((instantMessage.getInstantMessage().size() - 1));

            dialog.show();
        } else {
            return;
        }
    }

    private void setInstantMessageInformation(final int index) {

        adaptInstantMessageInformation(index);

        if (index == 0) {
            btn_NextAndClose.setText("بستن");
            btn_NextAndClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        } else {
            btn_NextAndClose.setText("بعدی");
            btn_NextAndClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setInstantMessageInformation(index - 1);
                }
            });
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
