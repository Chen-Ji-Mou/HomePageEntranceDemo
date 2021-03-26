package com.chenjimou.homepageentrancedemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static List<Model> list = new ArrayList<>();

    static {
        list.add(new Model("张三"));
        list.add(new Model("张三"));
        list.add(new Model("张三"));
        list.add(new Model("张三"));
        list.add(new Model("张三"));
        list.add(new Model("张三"));
        list.add(new Model("张三"));
        list.add(new Model("张三"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        RecyclerView recyclerView = findViewById(R.id.myRecyclerView);
        IndicatorView indicatorView = findViewById(R.id.indicatorView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(new RecyclerViewAdapter());

        // 将recyclerView与指示器绑定
        indicatorView.bindRecyclerView(recyclerView);
        // 设置指示器的宽度
        indicatorView.setTotalWidth(150);
        // 设置指示器的高度
        indicatorView.setTotalHeight(30);
    }

    private class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

        @NonNull
        @Override
        public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
            return new RecyclerViewAdapter.ViewHolder(layoutInflater.inflate(R.layout.recycler_view_item, null));
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
            Model model = list.get(position);
            holder.textView.setText(model.getName() + position);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            private TextView textView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.item_tv);
            }
        }
    }
}