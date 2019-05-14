package com.example.dell.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class TwoActivity extends AppCompatActivity {

    TextView score;
    TextView scoreb;
    private final String TAG = "TwoActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);

        score = (TextView)findViewById(R.id.score);
        scoreb = (TextView)findViewById(R.id.scoreb);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String scorea=((TextView)findViewById(R.id.score)).getText().toString();
        String scoreb=((TextView)findViewById(R.id.scoreb)).getText().toString();
        Log.i(TAG,"onSaveInstanceState:");
        outState.putString("teama_score",scorea);
        outState.putString("teamb_score",scoreb);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String scorea=savedInstanceState.getString("teama_score");
        String scoreb=savedInstanceState.getString("teamb_score");
        Log.i(TAG,"onRestoreInstanceState:");
        ((TextView)findViewById(R.id.score)).setText(scorea);
        ((TextView)findViewById(R.id.scoreb)).setText(scoreb);

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
