package ir.ac.sku.www.sessapplication.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ir.ac.sku.www.sessapplication.API.MyLog;
import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.activities.ShowMessageActivity;
import ir.ac.sku.www.sessapplication.models.MSGMessagesParcelable;

public class MessageSliderAdapter extends RecyclerView.Adapter<MessageSliderAdapter.MyViewHolder> {

    private MSGMessagesParcelable.Result results;

    @SuppressLint("LongLogTag")
    public MessageSliderAdapter(MSGMessagesParcelable.Result myResult) {
        this.results = (myResult == null) ? new MSGMessagesParcelable.Result() : myResult;
        Log.i(MyLog.MESSAGE, "3- MessageSliderAdapter : " + String.valueOf(results.getMessages().size()));
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_message_item, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        myViewHolder.bind(results.getMessages().get(position));
    }

    @Override
    public int getItemCount() {
        return results.getMessages().size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView sender;
        private TextView subject;
        private TextView date;
        private TextView priority;
        private final Context context;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();

            sender = itemView.findViewById(R.id.customMessage_TextViewSender);
            subject = itemView.findViewById(R.id.customMessage_TextViewSubject);
            date = itemView.findViewById(R.id.customMessage_TextViewDate);
            priority = itemView.findViewById(R.id.customMessage_TextViewType);

            itemView.setOnClickListener(this);
        }

        void bind(MSGMessagesParcelable.Result.Message message) {
            if (message.getSender() != null) {
                sender.setText(message.getSender());
            } else {
                sender.setText(message.getReceiver());
            }

            date.setText(message.getDate());
            subject.setText(message.getSubject());
            priority.setText(message.getPriority());

            if (message.getAttachment()) {
                date.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_attachment_3, 0);
            } else if (!message.getAttachment()) {
                date.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            }

            if (message.getPriority().equals("عادي")) {
                priority.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_circle_normal, 0);
            }
            if (message.getPriority().equals("ضروري")) {
                priority.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_circle_essential, 0);
            }
            if (message.getPriority().equals("مهم")) {
                priority.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_circle_important, 0);
            }
            if (message.getPriority().equals("خيلي مهم")) {
                priority.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_circle_very_important, 0);
            }
        }

        @Override
        public void onClick(View view) {
            final Intent intent = new Intent(context, ShowMessageActivity.class);
            intent.putExtra("ident", results.getMessages().get(getLayoutPosition()).getIdent());
            intent.putExtra("type", results.getType());
            context.startActivity(intent);
        }
    }
}
