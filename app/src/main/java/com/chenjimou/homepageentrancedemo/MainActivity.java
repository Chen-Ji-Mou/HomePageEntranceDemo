package com.chenjimou.homepageentrancedemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static List<Model> list = new ArrayList<>();

    static {
        list.add(new Model("张三"));
        list.add(new Model("李四"));
        list.add(new Model("王五"));
        list.add(new Model("赵六"));
        list.add(new Model("田七"));
        list.add(new Model("迪丽热巴"));
        list.add(new Model("古力娜扎"));
        list.add(new Model("马尔扎哈"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        MyLayout myLayout = findViewById(R.id.myLayout);
        // 设置数据
        myLayout.setData(list);
        // 添加监听
        myLayout.setOnClickListener(new MyLayout.OnClickListener() {
            @Override
            public void OnClick(int position) {
                Toast.makeText(MainActivity.this, list.get(position).getName(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}