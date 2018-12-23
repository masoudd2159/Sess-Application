package ir.ac.sku.www.sessapplication.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ir.ac.sku.www.sessapplication.API.MyLog;
import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.activities.ShowMessageActivity;
import ir.ac.sku.www.sessapplication.models.MSGMessagesParcelable;

public class MessageSliderAdapter extends RecyclerView.Adapter<MessageSliderAdapter.MyViewHolder> {

    private List<MSGMessagesParcelable.Result.Message> messages;

    @SuppressLint("LongLogTag")
    public MessageSliderAdapter(List<MSGMessagesParcelable.Result.Message> myMessage) {
        this.messages = (myMessage == null) ? new ArrayList<MSGMessagesParcelable.Result.Message>() : myMessage;
        Log.i(MyLog.MESSAGE,"3- MessageSliderAdapter : " +  String.valueOf(myMessage.size()));
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_message_item, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        myViewHolder.bind(messages.get(position));
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView sender;
        private TextView subject;
        private TextView date;
        private TextView priority;
        private final Context context;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();

            sender = itemView.findViewById(R.id.customMessage_TextViewSender);
            subject = itemView.findViewById(R.id.customMessage_TextViewSubject);
            date = itemView.findViewById(R.id.customMessage_TextViewDate);
            priority = itemView.findViewById(R.id.customMessage_TextViewType);

            itemView.setOnClickListener(this);
        }

        public void bind(MSGMessagesParcelable.Result.Message message) {

            sender.setText(message.getSender());
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
            Toast.makeText(view.getContext(), "ident = " + messages.get(getLayoutPosition()).getIdent(), Toast.LENGTH_SHORT).show();

            final Intent intent = new Intent(context, ShowMessageActivity.class);
            intent.putExtra("ident", messages.get(getLayoutPosition()).getIdent());
            context.startActivity(intent);
        }
    }
}
