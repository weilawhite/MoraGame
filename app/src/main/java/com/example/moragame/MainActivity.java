package com.example.moragame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.moragame.game.Computer;
import com.example.moragame.game.Mora;
import com.example.moragame.game.Player;
import com.example.moragame.game.Rule;
import com.example.moragame.game.WinState;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button startBtn, quitBtn;
    private ImageButton scissorBtn, paperBtn, rockBtn;
    private ImageView comImg;
    protected TextView ruleText;
    private final String TAG = "MainActivity";
    Player player;
    Computer computer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findView();
        //init();
        //delay();
        //3secCountdown();
        //delay();
        //com(); (rule)
        //player();
        //checkWin();
        //lifeLost or combo
        //back to delay
        //com();
    }

    private void init() {
        player = new Player();
        computer = new Computer();
        computer.AI();
        comImg.setImageResource(Mora.getMoraResId(computer.getMora()));
        setRuleText();
    }

    private void setRuleText() {
        Rule rule = computer.getRule();
        //String ruleString = computer.getRuleString();
        //ruleText.setText(getResources().);
        switch (rule) {
            case EVEN:
                ruleText.setText(getResources().getString(R.string.EVEN));
                break;
            case P_WIN:
                ruleText.setText(getResources().getString(R.string.P_WIN));
                break;
            case P_LOSE:
                ruleText.setText(getResources().getString(R.string.P_LOSE));
                break;
            case C_WIN:
                ruleText.setText(getResources().getString(R.string.C_WIN));
                break;
            case C_LOSE:
                ruleText.setText(getResources().getString(R.string.C_LOSE));
                break;
            case NOT_EVEN:
                ruleText.setText(getResources().getString(R.string.NOT_EVEN));
                break;
            case NOT_WIN:
                ruleText.setText(getResources().getString(R.string.NOT_WIN));
                break;
            case NOT_LOSE:
                ruleText.setText(getResources().getString(R.string.NOT_LOSE));
                break;
        }
    }

    private void findView() {
        startBtn = findViewById(R.id.start_btn);
        quitBtn = findViewById(R.id.quit_btn);
        scissorBtn = findViewById(R.id.scissors_ibn);
        paperBtn = findViewById(R.id.paper_ibn);
        rockBtn = findViewById(R.id.rock_ibn);
        comImg = findViewById(R.id.computer_img);

        ruleText = findViewById(R.id.rule_text);

        startBtn.setOnClickListener(this);
        quitBtn.setOnClickListener(this);
        scissorBtn.setOnClickListener(this);
        paperBtn.setOnClickListener(this);
        rockBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.paper_ibn:
                Log.d(TAG, getResources().getString(R.string.paper));
                player.setMora(Mora.PAPER);
                //Log.d(TAG, WinState.getWinState(player.getMora(), computer.getMora()).toString());
                Log.d(TAG, WinState.getWinState(player.getMora(), computer.getMora(), computer.getRule()).toString());
                break;
            case R.id.scissors_ibn:
                Log.d(TAG, getResources().getString(R.string.scissors));
                player.setMora(Mora.SCISSOR);
                //Log.d(TAG, WinState.getWinState(player.getMora(), computer.getMora()).toString());
                Log.d(TAG, WinState.getWinState(player.getMora(), computer.getMora(), computer.getRule()).toString());
                break;
            case R.id.rock_ibn:
                Log.d(TAG, getResources().getString(R.string.rock));
                player.setMora(Mora.ROCK);
                //Log.d(TAG, WinState.getWinState(player.getMora(), computer.getMora()).toString());
                Log.d(TAG, WinState.getWinState(player.getMora(), computer.getMora(), computer.getRule()).toString());
                break;
            case R.id.start_btn:
                Log.d(TAG, getResources().getString(R.string.start));
                init();
                break;
            case R.id.quit_btn:
                Log.d(TAG, getResources().getString(R.string.quit));
                break;
        }

    }
}