package com.example.dell.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    TextView out;
    EditText edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);  //r开头 res文件下   图案 布局 图案 res下所有资源变成一个R R下一个错，整个R都出错

        out = (TextView)findViewById(R.id.textout);
        edit= (EditText)findViewById(R.id.inp);

        Button btn = (Button)findViewById(R.id.btn);
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
           Log.i("main","onClick msg....");
           String str = edit.getText().toString();
           int b =Integer.valueOf(str);
           Double result = 33.8*b;
           out.setText("结果为：华氏度"+result);
    }
}
