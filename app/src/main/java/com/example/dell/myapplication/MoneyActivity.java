package com.example.dell.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MoneyActivity extends AppCompatActivity {
    EditText rmb;
    TextView result;
    private final String TAG="MoneyActivity";
    private float dollarrate=6.7f;
    private float eurorate=11f;
    private float wonrate=500;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money);
        rmb = (EditText)findViewById(R.id.rmb);
        result = (TextView)findViewById(R.id.result);}

        public void onclick(View btn){
        String str=rmb.getText().toString();
        float r=0;
       if(str.length()>0){
                r=Float.parseFloat(str);}
                else{
            Toast.makeText(this, "请输入金额", Toast.LENGTH_SHORT).show();
        }
        float val=0;
        if(btn.getId()==R.id.dollar){
            val= r*(1/dollarrate);
        }
        else if(btn.getId()==R.id.euro) {
            val = r * (1 / eurorate);
        }
        else if(btn.getId()==R.id.won){
                val = r*wonrate;
            }
            result.setText(String.valueOf(val));
        }
        public void openone(View btn){
            openconfig();

        }

    private void openconfig() {
        Log.i("open","openone:");
        Intent config=new Intent(this,ConfigActivity.class);
        config.putExtra("dollar_rate_key",dollarrate);
        config.putExtra("euro_rate_key",eurorate);
        config.putExtra("won_rate_key",wonrate);
        Log.i(TAG,"openOne:dollarrate"+dollarrate);
        Log.i(TAG,"openOne:deurorate"+eurorate);
        Log.i(TAG,"openOne:wonrate"+wonrate);
        Intent web=new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.jd.com"));
        Intent tel=new Intent(Intent.ACTION_VIEW, Uri.parse("tel:17381564732"));
        //startActivity(config);
        startActivityForResult(config,1);//任意整数
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.rate,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.menu_set){
            Intent config=new Intent(this,ConfigActivity.class);
            config.putExtra("dollar_rate_key",dollarrate);
            config.putExtra("euro_rate_key",eurorate);
            config.putExtra("won_rate_key",wonrate);
            Log.i(TAG,"openOne:dollarrate"+dollarrate);
            Log.i(TAG,"openOne:deurorate"+eurorate);
            Log.i(TAG,"openOne:wonrate"+wonrate);
            Intent web=new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.jd.com"));
            Intent tel=new Intent(Intent.ACTION_VIEW, Uri.parse("tel:17381564732"));
            //startActivity(config);
            startActivityForResult(config,1);//任意整数
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==1 && resultCode==2){
            Bundle bundle= data.getExtras();
            dollarrate=bundle.getFloat("key_dollar",0.1f);
            eurorate=bundle.getFloat("key_euro",0.1f);
            wonrate=bundle.getFloat("key_won",0.1f);
            Log.i(TAG,"onActivityResult:dollarrate"+dollarrate);
            Log.i(TAG,"onActivityResult:deurorate"+eurorate);
            Log.i(TAG,"onActivityResult:wonrate"+wonrate);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}




