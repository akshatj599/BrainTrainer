package com.example.braintrainer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.gridlayout.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewAnimator;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    int answer;
    int left;
    int right;
    int score;
    int upperBound;
    char operation;
    boolean gameOn;
    Random rand;
    char[] operationList;
    Button[] optionButtonsList;
    TextView scoreTextView;
    TextView timeTextView;
    TextView questionTextView;
    CountDownTimer cdt;
    Context window;
    MediaPlayer mp;

    private void hide(){
        this.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Handler hld= new Handler();
        hld.postDelayed(new Runnable() {
            @Override
            public void run() {
                MediaPlayer mpStart=MediaPlayer.create(window, R.raw.startmusic);
                mpStart.start();
            }
        }, 2000);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        optionButtonsList=new Button[]{findViewById(R.id.ans00), findViewById(R.id.ans01),
                findViewById(R.id.ans10), findViewById(R.id.ans11)};
        scoreTextView=findViewById(R.id.scoreTextView);
        timeTextView=findViewById(R.id.timeTextView);
        questionTextView=findViewById(R.id.questionTextView);
        operationList=new char[]{'+', '-', 'x'};
        upperBound=20;
        window=this;
        gameOn=false;
        rand=new Random();
        hide();
        reset();
        mp=MediaPlayer.create(this, R.raw.click);
        ConstraintLayout main=findViewById(R.id.mainConstLayout);
        main.setOnHoverListener(new View.OnHoverListener() {
            @Override
            public boolean onHover(View v, MotionEvent event) {
                hide();
                return false;
            }
        });
    }

    private void reset(){
        answer=0;
        left=0;
        right=0;
        score=0;
        operation='+';
        timeTextView.setText("0:00");
        scoreTextView.setText("0");
        questionTextView.setText("^");
        GridLayout gl=findViewById(R.id.optionsGridLayout);
        Button startbt=(Button) gl.getChildAt(0);
        startbt.setBackgroundResource(R.drawable.letsgo);
        startbt.setText("");
        for(int i=1; i<4; i++){
            Button bt=(Button)gl.getChildAt(i);
            bt.setEnabled(false);
            bt.setAlpha((float)0.5);
            bt.setText("");
        }
    }

    public void checkAnswer(View view){
        mp.start();
        if(gameOn) {
            final Button tapped = (Button) view;
            if (Integer.parseInt(tapped.getText().toString()) == answer) {
                score++;
                scoreTextView.setText(score + "");
                tapped.setBackgroundResource(R.drawable.buttonsright);
            } else {
                tapped.setBackgroundResource(R.drawable.buttonswrong);
            }
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    tapped.setBackgroundResource(R.drawable.buttons2b);
                }
            }, 200);
            update();
        }
        else{
            start(findViewById(R.id.ans00));
        }
    }

    private void update(){ //Putting new question
        left=rand.nextInt(upperBound)+1;
        right=rand.nextInt(left+1);
        if(right==0)
            right=1;
        int ops=rand.nextInt(3);
        operation=operationList[ops];
        if(operation=='+')
            answer=left+right;
        else if(operation=='-')
            answer=left-right;
        else
            answer=left*right;

        questionTextView.setText(left+" "+operation+" "+right);
        ops=rand.nextInt(4);
        Button currOption=optionButtonsList[ops];
        currOption.setText(answer+"");
        for(int i=0; i<4; i++){
            Button curr=optionButtonsList[i];
            if(curr!=currOption){
                curr.setText(rand.nextInt(300)+"");
            }
        }
    }

    public void start(View view){
        cdt=new CountDownTimer(25000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long out=(millisUntilFinished/1000+1);
                if(out>=10)
                    timeTextView.setText("0:"+out);
                else
                    timeTextView.setText("0:0"+out);
            }

            @Override
            public void onFinish() {
                gameOn=false;
                Toast.makeText(window, "You scored "+score+"!", Toast.LENGTH_LONG).show();
                Handler hld=new Handler();
                hld.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        MediaPlayer mpStart=MediaPlayer.create(window, R.raw.end);
                        mpStart.start();
                    }
                }, 100);
                reset();
            }
        }.start();
        mp.start();
        gameOn=true;
        scoreTextView.setText("0");
        GridLayout gl=findViewById(R.id.optionsGridLayout);
        Button startbt=(Button) gl.getChildAt(0);
        startbt.setBackgroundResource(R.drawable.buttons2b);
        for(int i=0; i<4; i++){
            Button bt=(Button)gl.getChildAt(i);
            bt.setEnabled(true);
            bt.setAlpha((float)1);
            bt.setText("");
        }
        update();
    }
}
