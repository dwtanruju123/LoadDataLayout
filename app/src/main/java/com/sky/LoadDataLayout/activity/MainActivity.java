package com.sky.LoadDataLayout.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import com.sky.LoadDataLayout.R;
import com.sky.library.CustomLoadDataLayout;

public class MainActivity extends AppCompatActivity {
    /**
     * 提供 加载中,空数据,错误数据,无网络这4中状态的布局设置
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final CustomLoadDataLayout load = (CustomLoadDataLayout) findViewById(R.id.load);
        /**
         * 模拟一个从加载中的状态至空数据的状态转换
         */
        new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message message) {
                switch (message.what) {
                    case 1:
                        load.setStatus(CustomLoadDataLayout.EMPTY);
                        break;
                }
                return false;
            }
        }).sendEmptyMessageDelayed(1, 3000);

        /**
         *设置空布局的所有点击事件
         */
        load.setOnEmptyClickListener(new CustomLoadDataLayout.LoadDataLayoutClick() {
            @Override
            public void onClick(int id, View view) {
                switch (id) {
                    case R.id.bt:
                        Toast.makeText(MainActivity.this, "您点击了空布局中的按钮", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, R.id.bt);
    }
}
