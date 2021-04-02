package com.chenjimou.homepageentrancedemo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MyLayout extends FrameLayout {

    private final Context mContext;
    private RecyclerView recyclerView;
    private IndicatorView indicator;
    private RecyclerViewAdapter adapter;
    private OnClickListener onClickListener;
    private int itemWidth; // 最终的每个 item 的宽度

    private final List<Model> list = new ArrayList<>();

    public MyLayout(@NonNull Context context) {
        this(context, null);
    }

    public MyLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        View.inflate(mContext, R.layout.my_layout, this);
        recyclerView = findViewById(R.id.rv_entrance);
        indicator = findViewById(R.id.rv_entrance_indicator);

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerViewAdapter();
        recyclerView.setAdapter(adapter);

        // 默认不显示
        indicator.setVisibility(View.GONE);
    }

    public void setData(List<Model> data){
        if (null != data && !data.isEmpty()){
            list.clear();
            list.addAll(data);
        }
        // 计算出每个 item 依据个数决定的新宽度
        int containerWidth = DisplayUtils.getDisplaySize(mContext).widthPixels;
        if (list.size() > 5){
            itemWidth = containerWidth / 5;
        } else {
            itemWidth = containerWidth / list.size();
        }

        adapter.notifyDataSetChanged();
        indicator.bindRecyclerView(recyclerView);

        if (list.size() > 5){
            indicator.setVisibility(View.VISIBLE);
        }
    }

    public interface OnClickListener{
        void OnClick(int position);
    }

    public void setOnClickListener(OnClickListener onClickListener){
        this.onClickListener = onClickListener;
    }

    class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

        @NonNull
        @Override
        public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.recycler_view_item, viewGroup, false);
            if (itemWidth > 0){
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.width = itemWidth;
                view.setLayoutParams(layoutParams);
            }
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder viewHolder, int position) {
            Model model = list.get(position);
            viewHolder.tv.setText(model.getName());
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            private final TextView tv;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tv = itemView.findViewById(R.id.item_tv);
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null != onClickListener){
                            onClickListener.OnClick(getAdapterPosition());
                        }
                    }
                });
            }
        }
    }
}
