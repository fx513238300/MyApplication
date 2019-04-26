package com.example.dell.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class TwoActivity extends AppCompatActivity {

    TextView score;
    TextView scoreb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);

        score = (TextView)findViewById(R.id.score);
        scoreb = (TextView)findViewById(R.id.scoreb);

    }
    public void btnAdd1(View btn){
        if(btn.getId()==R.id.btn1)
        show(1);
        else{ showb(1);}
    }
    public void btnAdd2(View btn){
        if(btn.getId()==R.id.btn2)
            show(2);
        else{ showb(2);}
    }
    public void btnAdd3(View btn){
        if(btn.getId()==R.id.btn3)
            show(3);
        else{ showb(3);}
    }
    public void btnReset(View btn){
        score.setText("0");
        scoreb.setText("0");
    }
    private void show(int i){
        Log.i("show","i="+i);
        String oldscore=(String)score.getText();
        int newscore=Integer.parseInt(oldscore)+i;
        score.setText(""+newscore);

    }  private void showb(int i){
        Log.i("show","i="+i);
        String oldscore=(String)scoreb.getText();
        int newscore=Integer.parseInt(oldscore)+i;
        scoreb.setText(""+newscore);}
}
