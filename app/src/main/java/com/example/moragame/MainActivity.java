package com.example.moragame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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

public class MainActivity extends AppCompatActivity implements View.OnClickListener, OnActionListener, Runnable {
    private Button startBtn, quitBtn;
    private ImageButton scissorBtn, paperBtn, rockBtn;
    private ImageView comImg;
    protected TextView ruleText, countText;
    private final String TAG = "MainActivity";
    Player player;
    Computer computer;
    GameState gameState;
    int stageCount = 0;
    boolean gameOver = false;
    boolean gaming=false;
    private int gameMilliSecond;
    private int targetMilliSecond;
    boolean gameCountDownFinish;
    Handler gameTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findView();
        init();
    }

    public void onAction(GameState gameState) {
        this.gameState = gameState;
        switch (gameState) {
            case INIT_GAME:
                break;
            case START_GAME:
                startGame();
                break;
            case COMPUTER_ROUND:
                computer.AI();
                Log.d(TAG, "1");
                break;
            case PLAYER_ROUND:
                Log.d(TAG, "2");
                startGameCountDown();
                break;
            case CHECK_WIN_STATE:
                Log.d(TAG, WinState.getWinState(player.getMora(), computer.getMora(), computer.getRule()).toString());
                if (WinState.getWinState(player.getMora(), computer.getMora(), computer.getRule()) == WinState.COMPUTER_WIN) {
                    player.setLife(player.getLife() - 1);
                    Log.d(TAG,"-1");
                }
                onAction(GameState.COMPUTER_ROUND);
                break;
        }
    }

    private void startGameCountDown() {
        comImg.setImageResource(Mora.getMoraResId(computer.getMora()));
        setRuleText();
        gameCountDownFinish = false;
        gameMilliSecond = 0;

        if (gameTimer != null) {
            gameTimer.removeCallbacks(this);
        }
        gameTimer = new Handler(Looper.getMainLooper());
        gameTimer.post(this);

    }

    private void startGame() {
        gameMilliSecond = 0;
        targetMilliSecond = 1000;
        gameCountDownFinish = false;
        onAction(GameState.COMPUTER_ROUND);
    }

    private void init() {
        player = new Player();
        computer = new Computer(this);
        gameState = GameState.INIT_GAME;
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
        stageCount++;
        switch (stageCount % 3) {
            case 0:
                ruleText.setTextColor(getResources().getColor(R.color.colorDarkRed));
                break;
            case 1:
                ruleText.setTextColor(getResources().getColor(R.color.colorGreen));
                break;
            case 2:
                ruleText.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
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
        countText = findViewById(R.id.count_text);

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
                if (gameState == GameState.PLAYER_ROUND) {
                    Log.d(TAG, getResources().getString(R.string.paper));
                    player.setMora(Mora.PAPER);
                    onAction(GameState.CHECK_WIN_STATE);
                }
                break;
            case R.id.scissors_ibn:
                if (gameState == GameState.PLAYER_ROUND) {
                    Log.d(TAG, getResources().getString(R.string.scissors));
                    player.setMora(Mora.SCISSOR);
                    onAction(GameState.CHECK_WIN_STATE);
                }
                break;
            case R.id.rock_ibn:
                if (gameState == GameState.PLAYER_ROUND) {
                    Log.d(TAG, getResources().getString(R.string.rock));
                    player.setMora(Mora.ROCK);
                    onAction(GameState.CHECK_WIN_STATE);
                }
                break;
            case R.id.start_btn:
                Log.d(TAG, getResources().getString(R.string.start));
                onAction(GameState.START_GAME);
                break;
            case R.id.quit_btn:
                if (gameOver = true) {
                    Log.d(TAG, getResources().getString(R.string.quit));
                }
                break;
        }

    }

    @Override
    public void run() {

        if (gameCountDownFinish) {
            Log.d(TAG, "T");
            return;
        }
        Log.d(TAG, "?");
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        gameMilliSecond = gameMilliSecond + 10;
        if (gameMilliSecond > targetMilliSecond) {
            gameMilliSecond = targetMilliSecond;
            gameCountDownFinish = true;
            player.setMora(Mora.NONE);
            onAction(GameState.CHECK_WIN_STATE);
        }

        int sec = (targetMilliSecond - gameMilliSecond) / 1000;
        int ms = (targetMilliSecond - gameMilliSecond) % 1000;
        String timer = String.format("%d:%03d", sec, ms);
        countText.setText(timer);
        gameTimer.postDelayed(this, 10);


    }
}