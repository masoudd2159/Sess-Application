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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import ir.ac.sku.www.sessapplication.api.MyLog;
import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.activity.messages.ShowMessageActivity;
import ir.ac.sku.www.sessapplication.model.MSGMessagesParcelable;

public class MessageSliderAdapter extends RecyclerView.Adapter<MessageSliderAdapter.MyViewHolder> {

    private MSGMessagesParcelable.Result results;

    @SuppressLint("LongLogTag")
    public MessageSliderAdapter(MSGMessagesParcelable.Result myResult) {
        this.results = (myResult == null) ? new MSGMessagesParcelable.Result() : myResult;
        Log.i(MyLog.MESSAGE, "3- MessageSliderAdapter : " + results.getMessages().size());
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
        private CardView cardViewPriority;
        private final Context context;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();

            sender = itemView.findViewById(R.id.customMessage_TextViewSender);
            subject = itemView.findViewById(R.id.customMessage_TextViewSubject);
            date = itemView.findViewById(R.id.customMessage_TextViewDate);
            priority = itemView.findViewById(R.id.customMessage_TextViewType);
            cardViewPriority = itemView.findViewById(R.id.customMessage_CardViewType);

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
                cardViewPriority.setCardBackgroundColor(0xFFA6A6A6);
            }
            if (message.getPriority().equals("ضروري")) {
                cardViewPriority.setCardBackgroundColor(0xFF990000);
            }
            if (message.getPriority().equals("مهم")) {
                cardViewPriority.setCardBackgroundColor(0xFF05813B);
            }
            if (message.getPriority().equals("خيلي مهم")) {
                cardViewPriority.setCardBackgroundColor(0xFF001C56);
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
