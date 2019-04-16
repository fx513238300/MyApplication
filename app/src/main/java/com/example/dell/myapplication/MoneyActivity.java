package com.example.dell.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class MoneyActivity extends AppCompatActivity implements Runnable{
    EditText rmb;
    TextView result;
    Handler handler;
    private final String TAG="MoneyActivity";
    private float dollarrate=0.1f;
    private float eurorate=0.2f;
    private float wonrate=0.3f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money);
        rmb = (EditText)findViewById(R.id.rmb);
        result = (TextView)findViewById(R.id.result);

        //获取sp里存放的数据
        SharedPreferences sharedpreferences= getSharedPreferences("myrate", Activity.MODE_PRIVATE);
        SharedPreferences sp=PreferenceManager.getDefaultSharedPreferences(this);
        dollarrate=sharedpreferences.getFloat("dolla_rate",0.0f);
        eurorate=sharedpreferences.getFloat("euro_rate",0.0f);
        wonrate=sharedpreferences.getFloat("won_rate",0.0f);
        Log.i(TAG,"onnCreate:sp dollarrate="+dollarrate);
        Log.i(TAG,"onnCreate:sp eurorate="+eurorate);
        Log.i(TAG,"onnCreate:sp wonrate="+wonrate);

        //开启子线程
        Thread t=new Thread(this);
        t.start();

        handler= new Handler(){
            @Override
          public void handleMessage(Message msg){
                if(msg.what==5){
                    String str=(String)msg.obj;
                    Log.i(TAG,"handlerMessage:getMessage msg="+str);
                    result.setText(str);
                }
              super.handleMessage(msg);
            }
        };
    }

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
            SharedPreferences sharedpreferences= getSharedPreferences("myrate", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor=sharedpreferences.edit();
            editor.putFloat("dollar_rate",dollarrate);
            editor.putFloat("euro_rate",eurorate);
            editor.putFloat("won_rate",wonrate);
            editor.commit();
            Log.i(TAG,"onActivityResult:数据已保存到sharedpreferences");

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void run() {
        Log.i(TAG,"run:run()......");
        for(int i=1;i<6;i++){
            Log.i(TAG,"run:i="+i);
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //获取msg对象，用于返回主线程
        Message msg=handler.obtainMessage(5);
        //msg.what=5;
        msg.obj="Hello from run()";
        handler.sendMessage(msg);

        //获取网络数据
        URL url=null;
        try {
            url=new URL("http://www.ysd-cny.com/icbc.htm");
            URLConnection http= (HttpURLConnection) url.openConnection();
            InputStream in=http.getInputStream();

            String html=inputStream2String(in);
            Log.i(TAG,"run:html="+html);


        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }



    private  String inputStream2String(InputStream inputStream)throws IOException {
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(inputStream, "gb2312");
        for (; ; ) {
            int rsz = in.read(buffer, 0, buffer.length);
            if (rsz < 0)
                break;
            out.append(buffer, 0, rsz);
        }
        return out.toString();
    }
}




