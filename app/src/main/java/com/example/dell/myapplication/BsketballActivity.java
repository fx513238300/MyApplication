package com.example.dell.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class BsketballActivity extends AppCompatActivity {

   TextView score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bsketball);

    score = (TextView)findViewById(R.id.score);

    }
    public void btnAdd1(View btn){
      show(1);
    }
    public void btnAdd2(View btn){
show(2);
    }
    public void btnAdd3(View btn){
show(3);
    }
    public void btnReset(View btn){
        score.setText("0");
    }
    private void show(int i){
        Log.i("show","i="+i);
        String oldscore=(String)score.getText();
        int newscore=Integer.parseInt(oldscore)+i;
        score.setText(""+newscore);

    }
}
