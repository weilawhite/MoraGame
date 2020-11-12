package com.example.moragame;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
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
    protected TextView ruleText, countText, lifeText, stageText, heartText, winCountText, bigCounterText, hitCountText, roundText, hitCombotext;
    private final String TAG = "MainActivity";
    Player player;
    Computer computer;
    GameState gameState;
    private int stageCount = 1, countSecond = 0;
    private int colorRandom = 1;
    private int round = 1;
    private int combo = 0, hitCombo = 0;
    private SoundPool soundPool;
    private int[] soundResId;
    private final int SOUND_CORRECT = 0;
    private final int SOUND_WRONG = 1;
    int topHitCombo=0;

    boolean gameOver = false;
    boolean gaming = false;
    private int gameMilliSecond;
    private int beginMilliSecond=3000;
    private int roundStep=2;
    private int targetMilliSecond;
    boolean gameCountDownFinish;
    Handler gameTimer;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void initSound() {
        soundPool = new SoundPool.Builder().setMaxStreams(10).build();
        soundResId = new int[]{soundPool.load(this, R.raw.correct_ogg, 1), soundPool.load(this, R.raw.wrong, 1)};
    }

    public void playSound(int id) {
        soundPool.play(soundResId[id], 1, 1, 1, 0, 1);
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
        heartText = findViewById(R.id.heart_text);
        winCountText = findViewById(R.id.win_count_text);
        roundText = findViewById(R.id.round_text);

        bigCounterText = findViewById(R.id.big_counter_text);
        bigCounterText.setVisibility(View.INVISIBLE);
        hitCountText = findViewById(R.id.hit_count_text);
        hitCountText.setVisibility(View.INVISIBLE);
        hitCombotext = findViewById(R.id.hit_combo_text);
        hitCombotext.setText(String.format("hit combo:\n %02d", hitCombo));
        startBtn.setOnClickListener(this);
        quitBtn.setOnClickListener(this);
        scissorBtn.setOnClickListener(this);
        paperBtn.setOnClickListener(this);
        rockBtn.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        load();
        findView();
        init();
        initSound();
    }

    public void onAction(GameState gameState) {
        this.gameState = gameState;
        switch (gameState) {
            case INIT_GAME:
                Log.d(TAG, "INIT_GAME");
                init();
                bigCounterText.setText(String.valueOf(countSecond));
                findViewById(R.id.grid_layout).setVisibility(View.INVISIBLE);
                bigCounterText.setVisibility(View.VISIBLE);
                gameTimer = new Handler(Looper.getMainLooper());
                gameTimer.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        countSecond--;
                        if (countSecond == 0) {
                            gameTimer.removeCallbacks(this);
                            findViewById(R.id.grid_layout).setVisibility(View.VISIBLE);
                            bigCounterText.setVisibility(View.INVISIBLE);
                            onAction(GameState.START_GAME);
                            return;
                        }
                        bigCounterText.setText(String.valueOf(countSecond));
                        gameTimer.post(this);
                    }
                });
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

            case GAME_OVER:
                StringBuilder sb = new StringBuilder();
                sb.append("SCORE:" + player.getWinCount()).append("\nHIT COMBO:" + hitCombo);
                gaming = false;
                if(hitCombo>topHitCombo){
                    topHitCombo=hitCombo;
                    save();
                }
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle(R.string.result);
                alertDialog.setMessage(sb.toString());
                alertDialog.setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
                break;
            case CHECK_WIN_STATE:
                Log.d(TAG, "Stage " + stageCount);
                Log.d(TAG, WinState.getWinState(player.getMora(), computer.getMora(), computer.getRule()).toString());

                if (WinState.getWinState(player.getMora(), computer.getMora(), computer.getRule()) == WinState.COMPUTER_WIN) {
                    Log.d(TAG, "LOSE LIFE:" + player.getLife() + "->" + (player.getLife() - 1));

                    playSound(SOUND_WRONG);
                    player.setLife(player.getLife() - 1);
                    //String life = String.format("HP:%d", player.getLife());
                    lifeText.setText(String.format("HP:%d", player.getLife()));
                    //String heart = player.getLifeString();
                    heartText.setText(player.getLifeString());
                    combo = 0;


                } else {
                    Log.d(TAG, "WIN");
                    playSound(SOUND_CORRECT);

                    stageCount++;
                    player.setWinCount(player.getWinCount() + 1);
                    winCountText.setText(String.valueOf(player.getWinCount()));
                    stageText.setText(String.valueOf(stageCount));
                    combo++;
                    hitCountText.setVisibility(View.VISIBLE);
                    hitCountText.setText(combo + "連擊!");

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(800);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    hitCountText.setVisibility(View.INVISIBLE);
                                }
                            });
                        }
                    }).start();

                    if (combo > hitCombo) {
                        hitCombo = combo;
                        //save();
                        hitCombotext.setText(String.format("hit combo:\n %02d", hitCombo));}

                }

                if (player.getLife() <= 0) {
                    Log.d(TAG, "Game Over"); //終結倒數 想辦法還原到初始狀態
                    gaming = false;
                    gameCountDownFinish = true;
                    ruleText.setText("遊戲結束");
                    if (combo > hitCombo) {
                        hitCombo = combo;
                        save();
                        //hitCombotext.setText(String.format("hit combo:\n %02d", hitCombo));
                    }
                    gameState = GameState.GAME_OVER;
                    onAction(GameState.GAME_OVER);

                    //onAction(GameState.INIT_GAME);
                } else {
                    onAction(GameState.START_GAME);
                }
                break;
        }
    }

    public void save() {
        SharedPreferences sharedPreferences = getSharedPreferences("Game", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("Combo",topHitCombo);
        editor.commit();
    }

    public void load(){
        SharedPreferences sharedPreferences=getSharedPreferences("Game",Context.MODE_PRIVATE);
        topHitCombo=sharedPreferences.getInt("Combo",0);
        hitCombo=topHitCombo;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            showExitDialog();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void showExitDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setTitle(R.string.exit);
        alertDialog.setMessage(R.string.exit);
        alertDialog.setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.this.finish();
                System.exit(0);
            }
        }).setPositiveButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        }).create().show();

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
        stageCount = 1;
        countSecond = 3;
        round = 1;
        combo = 0;
        player.setLife(player.getINIT_LIFE());
    }

    private void startGame() {

        gameMilliSecond = 0;
        //targetMilliSecond = 1500;
        gameCountDownFinish = false;

        this.setTitle(getResources().getString(R.string.app_name)+" > "+targetMilliSecond);

        roundText.setText("ROUND: " + round++);

        stageText.setText(String.valueOf(stageCount));
        winCountText.setText(String.valueOf(player.getWinCount()));

        //新的愛心血量
        heartText.setText(player.getLifeString());

        //舊的文字血量
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
                    onAction(GameState.INIT_GAME);
                }
                break;
            case R.id.quit_btn:
                if (gameOver = true) {
                    Log.d(TAG, getResources().getString(R.string.quit));
                    showExitDialog();
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
/*
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


 */
        targetMilliSecond=beginMilliSecond-(round/roundStep)*500;

        if(targetMilliSecond<1000){
            targetMilliSecond=1000;
        }

        gameMilliSecond = gameMilliSecond + 10;
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
        //gameTimer.post(this);
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

}