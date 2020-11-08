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
    protected TextView ruleText, countText, lifeText, stageText;
    private final String TAG = "MainActivity";
    Player player;
    Computer computer;
    GameState gameState;
    int stageCount = 1;
    int colorRandom = 1;
    boolean gameOver = false;
    boolean gaming = false;
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
                Log.d(TAG, "INIT_GAME");
                break;
            case START_GAME:
                Log.d(TAG, "START_GAME");
                startGame();
                break;
            case COMPUTER_ROUND:
                Log.d(TAG, "COMPUTER_ROUND");
                computer.AI();
                break;
            case PLAYER_ROUND:
                Log.d(TAG, "PLAYER_ROUND");
                startGameCountDown();
                break;
            case CHECK_WIN_STATE:
                Log.d(TAG, "Stage " + stageCount);
                Log.d(TAG, WinState.getWinState(player.getMora(), computer.getMora(), computer.getRule()).toString());
                if (WinState.getWinState(player.getMora(), computer.getMora(), computer.getRule()) == WinState.COMPUTER_WIN) {
                    Log.d(TAG, "LOSE LIFE:"+player.getLife()+"->"+(player.getLife()-1));
                    player.setLife(player.getLife() - 1);
                    String life = String.format("HP:%d", player.getLife());
                    lifeText.setText(life);
                } else {
                    Log.d(TAG, "WIN");
                    stageCount++;
                    stageText.setText(String.valueOf(stageCount));
                }

                if (player.getLife() <= 0) {
                    Log.d(TAG, "Game Over"); //終結倒數 想辦法還原到初始狀態
                    gaming = false;
                    gameCountDownFinish = true;
                    ruleText.setText("遊戲結束");
                    onAction(GameState.INIT_GAME);
                } else {
                    onAction(GameState.COMPUTER_ROUND);
                }
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

    private void init() {
        player = new Player();
        computer = new Computer(this);
        gameState = GameState.INIT_GAME;
    }

    private void startGame() {

        gameMilliSecond = 0;
        targetMilliSecond = 1500;
        gameCountDownFinish = false;
        player.setLife(player.getINIT_LIFE());
        stageCount = 1;
        stageText.setText(String.valueOf(stageCount));
        String life = String.format("HP:%d", player.getLife());
        lifeText.setText(life);
        gaming = true;
        if (gaming) {
            onAction(GameState.COMPUTER_ROUND);
        } else {
            Log.d(TAG, "NO");
        }
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
                if (!gaming) {
                    Log.d(TAG, getResources().getString(R.string.start));
                    gaming = true;
                    onAction(GameState.START_GAME);
                }
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

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        gameMilliSecond = gameMilliSecond + 100;
        if (gameMilliSecond > targetMilliSecond) {
            gameMilliSecond = targetMilliSecond;
            gameCountDownFinish = true;
            player.setMora(Mora.NONE); //超時未出拳 倒數會加速??
            onAction(GameState.CHECK_WIN_STATE);
            return;
        }

        int sec = (targetMilliSecond - gameMilliSecond) / 1000;
        int ms = (targetMilliSecond - gameMilliSecond) % 1000;
        String timer = String.format("%d:%03d", sec, ms);
        countText.setText(timer);
        gameTimer.postDelayed(this, 10);

    }

    private void setRuleText() {
        Rule rule = computer.getRule();

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
        colorRandom++;
        switch (colorRandom % 3) {
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
        lifeText = findViewById(R.id.life_text);
        ruleText = findViewById(R.id.rule_text);
        countText = findViewById(R.id.count_text);
        stageText = findViewById(R.id.stage_text);
        startBtn.setOnClickListener(this);
        quitBtn.setOnClickListener(this);
        scissorBtn.setOnClickListener(this);
        paperBtn.setOnClickListener(this);
        rockBtn.setOnClickListener(this);
    }
}