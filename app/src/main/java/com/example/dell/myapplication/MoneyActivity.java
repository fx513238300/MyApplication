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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MoneyActivity extends AppCompatActivity implements Runnable {
    EditText rmb;
    TextView result;
    Handler handler;
    private final String TAG = "MoneyActivity";
    private float dollarrate = 0.1f;
    private float eurorate = 0.2f;
    private float wonrate = 0.3f;
    private String updateDate="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money);
        rmb = (EditText) findViewById(R.id.rmb);
        result = (TextView) findViewById(R.id.result);

        //获取sp里存放的数据
        SharedPreferences sharedpreferences = getSharedPreferences("myrate", Activity.MODE_PRIVATE);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        dollarrate = sharedpreferences.getFloat("dolla_rate", 0.0f);
        eurorate = sharedpreferences.getFloat("euro_rate", 0.0f);
        wonrate = sharedpreferences.getFloat("won_rate", 0.0f);
        updateDate=sharedpreferences.getString("update_date","");
        //获取当前系统时间
       Date today= Calendar.getInstance().getTime();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        final String todayStr=sdf.format(today);

        Log.i(TAG, "onnCreate:sp dollarrate=" + dollarrate);
        Log.i(TAG, "onnCreate:sp eurorate=" + eurorate);
        Log.i(TAG, "onnCreate:sp wonrate=" + wonrate);
        Log.i(TAG, "onnCreate:sp updateDate=" + updateDate);
        Log.i(TAG, "onnCreate:todayStr=" + todayStr);


        //判断时间
        if(!todayStr.equals(updateDate)){
            Log.i(TAG, "onnCreate:需要更新" );
            //开启子线程
            Thread t = new Thread(this);
            t.start();
        }else{
            Log.i(TAG, "onnCreate:不需要更新" );
        }


        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 5) {
                    Bundle bd1 = (Bundle) msg.obj;
                    dollarrate = bd1.getFloat("dollar-rate");
                    eurorate = bd1.getFloat("euro-rate");
                    wonrate = bd1.getFloat("won-rate");

                    Log.i(TAG, "handleMessage:dollarrate:" + dollarrate);
                    Log.i(TAG, "handleMessage:eurorate:" + eurorate);
                    Log.i(TAG, "handleMessage:wonrate:" + wonrate);

                    //保存更新的日期
                    SharedPreferences sharedpreferences = getSharedPreferences("myrate", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putFloat("dollar_rate", dollarrate);
                    editor.putFloat("euro_rate", eurorate);
                    editor.putFloat("won_rate", wonrate);
                    editor.putString("update_date",todayStr);
                    editor.apply();

                    Toast.makeText(MoneyActivity.this, "汇率已更新", Toast.LENGTH_SHORT).show();

                }
                super.handleMessage(msg);
            }
        };
    }

    public void onclick(View btn) {
        String str = rmb.getText().toString();
        float r = 0;
        if (str.length() > 0) {
            r = Float.parseFloat(str);
        } else {
            Toast.makeText(this, "请输入金额", Toast.LENGTH_SHORT).show();
        }
        float val = 0;
        if (btn.getId() == R.id.dollar) {
            val = r * (1 / dollarrate);
        } else if (btn.getId() == R.id.euro) {
            val = r * (1 / eurorate);
        } else if (btn.getId() == R.id.won) {
            val = r * wonrate;
        }
        result.setText(String.valueOf(val));
    }

    public void openone(View btn) {
        openconfig();

    }

    private void openconfig() {
        Log.i("open", "openone:");
        Intent config = new Intent(this, ConfigActivity.class);
        config.putExtra("dollar_rate_key", dollarrate);
        config.putExtra("euro_rate_key", eurorate);
        config.putExtra("won_rate_key", wonrate);
        Log.i(TAG, "openOne:dollarrate" + dollarrate);
        Log.i(TAG, "openOne:deurorate" + eurorate);
        Log.i(TAG, "openOne:wonrate" + wonrate);
        Intent web = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.jd.com"));
        Intent tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:17381564732"));
        //startActivity(config);
        startActivityForResult(config, 1);//任意整数
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.rate, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_set) {
            openConfig();
        }
        else if(item.getItemId()==R.id.open_list){
            //打开列表窗口
            //Intent list = new Intent(this, MyList2Activity.class);
            //startActivity(list);
            Intent list = new Intent(this, RateListActivity.class);
            startActivity(list);
            //测试数据库
            //RateItem item1=new RateItem("aaaa","123");
            //RateManager manager=new RateManager(this);
            //manager.add(item1);
            //manager.add(new RateItem("bbbb","23.5"));
            //Log.i(TAG,"onOptionsItemSelected:写入数据完毕");
             //查询所有数据
            //List<RateItem> testList=manager.listAll();
            //for(RateItem i:testList){
              //  Log.i(TAG,"onOptionsItemSelected:取出数据[id="+i.getId()+"]Name="+i.getCurName()+"Rate="+i.getCurRate());
            //}

        }
        return super.onOptionsItemSelected(item);
    }

    private void openConfig() {
        Intent config = new Intent(this, ConfigActivity.class);
        config.putExtra("dollar_rate_key", dollarrate);
        config.putExtra("euro_rate_key", eurorate);
        config.putExtra("won_rate_key", wonrate);
        Log.i(TAG, "openOne:dollarrate" + dollarrate);
        Log.i(TAG, "openOne:deurorate" + eurorate);
        Log.i(TAG, "openOne:wonrate" + wonrate);
        Intent web = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.jd.com"));
        Intent tel = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:17381564732"));
        //startActivity(config);
        startActivityForResult(config, 1);//任意整数
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 && resultCode == 2) {
            Bundle bundle = data.getExtras();
            dollarrate = bundle.getFloat("key_dollar", 0.1f);
            eurorate = bundle.getFloat("key_euro", 0.1f);
            wonrate = bundle.getFloat("key_won", 0.1f);
            Log.i(TAG, "onActivityResult:dollarrate" + dollarrate);
            Log.i(TAG, "onActivityResult:deurorate" + eurorate);
            Log.i(TAG, "onActivityResult:wonrate" + wonrate);

            //将新设置的汇率写到SP里
            SharedPreferences sharedpreferences = getSharedPreferences("myrate", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putFloat("dollar_rate", dollarrate);
            editor.putFloat("euro_rate", eurorate);
            editor.putFloat("won_rate", wonrate);
            editor.commit();
            Log.i(TAG, "onActivityResult:数据已保存到sharedpreferences");

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void run() {
        Log.i(TAG, "run:run()......");

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//用户保存获取的汇率
       Bundle bundle=new Bundle();


        //获取网络数据
       /* URL url = null;
        try {
            url = new URL("http://www.usd-cny.com/bankofChina.htm");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            InputStream in = http.getInputStream();

            String html = inputStream2String(in);
            Log.i(TAG, "run:html=" + html);
            Document doc =Jsoup.parse(html);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        bundle=getFormBOC();

        //bundle中保存所获取的汇率
        //获取msg对象，用于返回主线程
        Message msg = handler.obtainMessage(5);
        //msg.what=5;
        //msg.obj = "Hello from run()";
        msg.obj=bundle;
        handler.sendMessage(msg);

    }

    private Bundle getFormBOC( ) {
        Bundle bundle=new Bundle();
        Document doc=null;
        try{
            doc = Jsoup.connect("http://www.boc.cn/sourcedb/whpj/").get();
            //doc =Jsoup.parse(html);
            Log.i(TAG, "run:" + doc.title());
            Elements tables=doc.getElementsByTag("table");

            /*for(Element table :tables){
                Log.i(TAG,"run:table["+i+"]="+table);
                i++;
            }*/
            Element table2=tables.get(1);//定位table
            //获取TD中的数据
            Elements tds = table2.getElementsByTag("td");
            for(int i=0;i<tds.size();i+=8) {
              Element td1=tds.get(i);
                Element td2=tds.get(i+5);

                String str1=td1.text();
                String val=td2.text();
                Log.i(TAG,"run:"+str1+"==>"+val);

                if("美元".equals(str1)){
                    bundle.putFloat("dollar-rate",100f/Float.parseFloat(val));
                }else  if("欧元".equals(str1)){
                    bundle.putFloat("euro-rate",100f/Float.parseFloat(val));
                }else  if("韩国元".equals(str1)){
                    bundle.putFloat("won-rate",100f/Float.parseFloat(val));
                }

            }

        }
        catch(IOException e)
        { e.printStackTrace(); }
        return bundle;
    }

    private Bundle getFormUsdCny( ) {
        Bundle bundle=new Bundle();
        Document doc=null;
        try{
            doc = Jsoup.connect("http://www.usd-cny.com/bankofChina.htm").get();
            //doc =Jsoup.parse(html);
            Log.i(TAG, "run:" + doc.title());
            Elements tables=doc.getElementsByTag("table");

            /*for(Element table :tables){
                Log.i(TAG,"run:table["+i+"]="+table);
                i++;
            }*/
            Element table6=tables.get(5);
            // Log.i(TAG,"run:table6="+table6);
            //获取TD中的数据
            Elements tds = table6.getElementsByTag("td");
            for(int i=0;i<tds.size();i+=8) {
                Element td1=tds.get(i);
                Element td2=tds.get(i+5);
                Log.i(TAG,"run:"+td1.text()+"==>"+td2.text());
                String str1=td1.text();
                String val=td2.text();

                if("美元".equals(str1)){
                    bundle.putFloat("dollar-rate",100f/Float.parseFloat(val));
                }else  if("欧元".equals(str1)){
                    bundle.putFloat("euro-rate",100f/Float.parseFloat(val));
                }else  if("韩国元".equals(str1)){
                    bundle.putFloat("won-rate",100f/Float.parseFloat(val));
                }

            }

        }
        catch(IOException e)
        { e.printStackTrace(); }
        return bundle;
    }
    private String inputStream2String (InputStream inputStream)throws IOException {
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





