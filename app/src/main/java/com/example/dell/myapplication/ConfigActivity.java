package com.example.dell.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class ConfigActivity extends AppCompatActivity {
    public final String TAG="ConfigActivity";

    EditText dollar;
    EditText euro;
    EditText won;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        Intent intent =getIntent();
        float dollar2 = intent.getFloatExtra("dollar_rate_key",0.0f);
        float euro2 = intent.getFloatExtra("euro_rate_key",0.0f);
        float won2 = intent.getFloatExtra("won_rate_key",0.0f);

        Log.i(TAG,"onCreate:dollar2="+dollar2);
        Log.i(TAG,"onCreate:euro2="+euro2);
        Log.i(TAG,"onCreate:won2="+won2);

        dollar=(EditText)findViewById(R.id.dollar_rate);
        euro=(EditText)findViewById(R.id.euro_rate);
        won=(EditText)findViewById(R.id.won_rate);

        //显示数据到控件
        dollar.setText(String.valueOf(dollar2));
        euro.setText(String.valueOf(euro2));
        won.setText(String.valueOf(won2));


    }

    public void save(View btn){
        Log.i( TAG,"save:");
        //获取新的数值
        float newdollar =Float.parseFloat(dollar.getText().toString());
        float neweuro =Float.parseFloat(euro.getText().toString());
        float newwon =Float.parseFloat(won.getText().toString());
        Log.i( TAG,"save:获取到新的值");
        Log.i(TAG,"save:newdollar="+newdollar);
        Log.i(TAG,"save:neweuro="+neweuro);
        Log.i(TAG,"save:newwon="+newwon);
        //保存到Bundle或放入到Extra
        Intent intent =getIntent();
        Bundle bdl=new Bundle();
        bdl.putFloat("key_dollar",newdollar);
        bdl.putFloat("key_euro",neweuro);
        bdl.putFloat("key_won",newwon);
        intent.putExtras(bdl);
        setResult(2,intent);
        //返回到调用页面
        finish();

    }
}
