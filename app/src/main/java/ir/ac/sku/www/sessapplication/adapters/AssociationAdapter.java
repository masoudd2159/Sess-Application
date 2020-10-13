package ir.ac.sku.www.sessapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.model.AssociationModel;
import ir.ac.sku.www.sessapplication.utils.LineItemDecoration;
import ir.ac.sku.www.sessapplication.utils.Tools;
import ir.ac.sku.www.sessapplication.utils.ViewAnimation;

public class AssociationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<AssociationModel> items = new ArrayList<>();


    private Context ctx;
    private OnItemClickListener mOnItemClickListener;

    public AssociationAdapter(Context context, List<AssociationModel> items) {
        this.items = items;
        ctx = context;
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expand, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            final OriginalViewHolder viewHolder = (OriginalViewHolder) holder;

            final AssociationModel model = items.get(position);
            viewHolder.name.setText(model.getSection());
            viewHolder.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, items.get(position), position);
                    }
                }
            });

            viewHolder.bt_expand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean show = toggleLayoutExpand(!model.expanded, v, viewHolder.lyt_expand);
                    items.get(position).expanded = show;

                    viewHolder.recyclerView.setAdapter(new ExpandAdapter(ctx, model.getData()));
                }
            });


            // void recycling view
            if (model.expanded) {
                viewHolder.lyt_expand.setVisibility(View.VISIBLE);
            } else {
                viewHolder.lyt_expand.setVisibility(View.GONE);
            }
            Tools.toggleArrow(model.expanded, viewHolder.bt_expand, false);

        }
    }

    private boolean toggleLayoutExpand(boolean show, View view, View lyt_expand) {
        Tools.toggleArrow(show, view);
        if (show) {
            ViewAnimation.expand(lyt_expand);
        } else {
            ViewAnimation.collapse(lyt_expand);
        }
        return show;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, AssociationModel obj, int position);
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public ImageButton bt_expand;
        public View lyt_expand;
        public View lyt_parent;
        public RecyclerView recyclerView;

        public OriginalViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.name);
            bt_expand = (ImageButton) v.findViewById(R.id.bt_expand);
            lyt_expand = (View) v.findViewById(R.id.lyt_expand);
            lyt_parent = (View) v.findViewById(R.id.lyt_parent);
            recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView_expand);

            recyclerView.setLayoutManager(new LinearLayoutManager(ctx));
            recyclerView.addItemDecoration(new LineItemDecoration(ctx, LinearLayout.VERTICAL));
            //recyclerView.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(ctx, R.anim.layout_animation_from_right));
        }
    }
}
