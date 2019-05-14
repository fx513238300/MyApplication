package com.example.dell.myapplication;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyList2Activity extends ListActivity implements Runnable,AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener {
     Handler handler;
     private List<HashMap<String ,String>>listItems;//存放文字、图片信息
    private SimpleAdapter listItemsAdapter;//适配器
    private final String TAG = "MyList2Activity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            initListView();
            //MyAdapter myAdapte=new MyAdapter(this,R.layout.list_item,listItems);
            //this.setListAdapter(myAdapte);
            this.setListAdapter(listItemsAdapter);

            Thread t=new Thread(this);
        t.start();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 7) {
                    listItems=(List<HashMap<String,String>>)msg.obj;
                    listItemsAdapter=new SimpleAdapter(MyList2Activity.this,listItems,//listItems数据源
                            R.layout.list_item,//ListItem的XML布局实现
                            new String[]{"ItemTitle","ItemDetail"},
                            new int[]{R.id.itemTitle,R.id.itemDetail}
                    );
                    setListAdapter(listItemsAdapter);
                }
                super.handleMessage(msg);
            }
        };
        getListView().setOnItemClickListener(this);
        getListView().setOnItemLongClickListener(this);
    }

    private  void initListView(){
        listItems =new  ArrayList<HashMap<String ,String>>();
        for (int i=0;i<10;i++){
            HashMap<String,String>map=new HashMap<String,String>();
            map.put("ItemTitle","Rate:"+i);//标题文字
            map.put("ItemDetail","detail1"+i);//详细描述
            listItems.add(map);
        }
        //生成适配器的Item和动态数组对应的元素
        listItemsAdapter=new SimpleAdapter(this,listItems,//listItems数据源
                R.layout.list_item,//ListItem的XML布局实现
                new String[]{"ItemTitle","ItemDetail"},
                new int[]{R.id.itemTitle,R.id.itemDetail}
                );
    }

    @Override
    public void run() {
        //获取网络数据，放入list带回到主线程中
        List<HashMap<String,String>> retList =new ArrayList<HashMap<String,String>>();
        Document doc=null;
        try{
            Thread.sleep(1000);
            doc = Jsoup.connect("http://www.boc.cn/sourcedb/whpj/").get();
            Log.i(TAG, "run:" + doc.title());
            Elements tables=doc.getElementsByTag("table");

            Element table2=tables.get(1);
            //获取TD中的数据
            Elements tds = table2.getElementsByTag("td");
            for(int i=0;i<tds.size();i+=8) {
                Element td1=tds.get(i);
                Element td2=tds.get(i+5);

                String str1=td1.text();
                String val=td2.text();
                HashMap<String,String>map=new HashMap<String,String>();
                map.put("ItemTitle",str1);
                map.put("ItemDetail",val);
                retList.add(map);
            }
        }
        catch(IOException e)
        { e.printStackTrace(); }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        Message msg =handler.obtainMessage(7);
        msg.obj=retList;
        handler.sendMessage(msg);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i(TAG, "onTtemClick:parent" + parent);
        Log.i(TAG, "onTtemClick:view" + view);
        Log.i(TAG, "onTtemClick:position" + position);
        Log.i(TAG, "onTtemClick:id" + id);
        HashMap<String,String> map=(HashMap<String,String>) getListView().getItemAtPosition(position);
        String titleStr=map.get("ItemTitle");
        String detailStr=map.get("ItemDetail");
        Log.i(TAG, "onTtemClick:titleStr" + titleStr);
        Log.i(TAG, "onTtemClick:detailStr" + detailStr);

        TextView title=(TextView)view.findViewById(R.id.itemTitle);
        TextView detail=(TextView)view.findViewById(R.id.itemDetail);
        String title2=String.valueOf(title.getText());
        String detail2=String.valueOf(detail.getText());
        Log.i(TAG, "onTtemClick:title2" + title2);
        Log.i(TAG, "onTtemClick:detail2" + detail2);

        //打开新的页面，传入参数
        Intent rateCalc=new Intent(this,RateCalcActivity.class);
        rateCalc.putExtra("title",titleStr);
        rateCalc.putExtra("rate",Float.parseFloat(detailStr));
        startActivity(rateCalc);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        Log.i(TAG, "onTtemLongClick:长按列表项position=" + position);
        //删除操作
        //listItems.remove(position);
        //listItemsAdapter.notifyDataSetChanged();
        //构造对话框进行确认删除操作
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("提示").setMessage("请确认是否删除当前数据").setPositiveButton("是",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i(TAG, "onClick:对话框事件处理" );
                listItems.remove(position);
                listItemsAdapter.notifyDataSetChanged();
            }
        })
                .setNegativeButton("否",null);
        builder.create().show();



        Log.i(TAG, "onTtemLongClick:size=" + listItems.size());
        return true;
    }
}
