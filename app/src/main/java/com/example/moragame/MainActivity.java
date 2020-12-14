package com.example.moragame;

import androidx.annotation.NonNull;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.moragame.game.About;
import com.example.moragame.game.Computer;
import com.example.moragame.game.HowToPlay;
import com.example.moragame.game.Mora;
import com.example.moragame.game.Player;
import com.example.moragame.game.Rule;
import com.example.moragame.game.WinState;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, OnActionListener, Runnable {
    private Button startBtn, quitBtn;
    private ImageButton scissorBtn, paperBtn, rockBtn;
    private ImageView comImg;
    protected TextView scoreText, ruleText, countText, lifeText, stageText, heartText, winCountText, bigCounterText, hitCountText, roundText, hitCombotext;
    private final String TAG = "MainActivity";
    Player player;
    Computer computer;
    GameState gameState;
    private int stageCount = 1, countSecond = 0;
    private int colorRandom = 1;
    private int round = 1;
    int score;
    int lifeBonusTimes, lifeBonus = 1000;//加命次數 加命門檻
    private int combo = 0, hitCombo = 0;
    private SoundPool soundPool;
    private int[] soundResId;
    private final int SOUND_CORRECT = 0;
    private final int SOUND_WRONG = 1;
    private final int LIFE_ADD = 10;
    int getTopHitComboThisGame = 0;
    int topHitCombo = 0; //會被存取

    boolean easyMode = false;
    boolean gameOver = false;
    boolean gaming = false;
    private int gameMilliSecond;
    private int minMilliSecond = 1500;  //最低時間
    private int beginMilliSecond = 3000;  //初始時間
    private int roundStep = 10; //每幾關加速一次
    private int scoreRate = 1; //分數倍率
    private int targetMilliSecond;
    private boolean soundOn;
    boolean gameCountDownFinish;
    Handler gameTimer;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void initSound() {
        soundOn = true;
        soundPool = new SoundPool.Builder().setMaxStreams(15).build();
        soundResId = new int[]{soundPool.load(this, R.raw.correct_ogg, 1), soundPool.load(this, R.raw.wrong, 1),
                soundPool.load(this, R.raw.r1, 1), soundPool.load(this, R.raw.r2, 1), soundPool.load(this, R.raw.r3, 1),
                soundPool.load(this, R.raw.r4, 1), soundPool.load(this, R.raw.r5, 1), soundPool.load(this, R.raw.r6, 1),
                soundPool.load(this, R.raw.r7, 1), soundPool.load(this, R.raw.r8, 1), soundPool.load(this, R.raw.life_add, 1)
        };
    }

    public void playSound(int id) {
        if (soundOn) {
            soundPool.play(soundResId[id], 1, 1, 1, 0, 1);
        }
    }

    private void findView() {
        scoreText = findViewById(R.id.score_text);
        startBtn = findViewById(R.id.start_btn);
        quitBtn = findViewById(R.id.quit_btn);
        scissorBtn = findViewById(R.id.scissors_ibn);
        paperBtn = findViewById(R.id.paper_ibn);
        rockBtn = findViewById(R.id.rock_ibn);
        comImg = findViewById(R.id.computer_img);
        ruleText = findViewById(R.id.rule_text);
        countText = findViewById(R.id.count_text);
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

        getDifficultParameter();
    }

    private void getDifficultParameter() {
        Bundle bundle = getIntent().getExtras();
        scoreRate = bundle.getInt("scoreRate");

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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
                if (easyMode) {
                    computer.easyAI();
                } else {
                    computer.AI();
                }
                break;
            case PLAYER_ROUND:
                Log.d(TAG, "PLAYER_ROUND");
                startGameCountDown();
                break;

            case GAME_OVER:
                StringBuilder sb = new StringBuilder();


                gaming = false;
                if (hitCombo > topHitCombo) {
                    topHitCombo = hitCombo;
                    save();
                }
                //待修改
                sb.append("這次挑戰到第" + player.getWinCount()).append("關\n歷史最高連擊:" + topHitCombo).append("\n總分:" + score);
                //sb.append("總分:" + player.getWinCount()).append("\n最高連擊數:" + hitCombo).append("\n歷史最高連擊:"+topHitCombo);

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

                    //lifeText.setText(String.format("HP:%d", player.getLife()));

                    heartText.setText(player.getLifeString());
                    combo = 0;


                } else {
                    Log.d(TAG, "WIN");


                    stageCount++;
                    player.setWinCount(player.getWinCount() + 1);
                    winCountText.setText(String.valueOf(player.getWinCount()));
                    //stageText.setText(String.valueOf(stageCount));
                    combo++;
                    hitCountText.setVisibility(View.VISIBLE);
                    hitCountText.setText(combo + "連擊!");

                    Boolean lifeAdd = false;
                    lifeAdd = scoreCompute();
                    if (lifeAdd) {
                        playSound(LIFE_ADD);
                    } else {
                        playSound(SOUND_CORRECT);
                    }

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(1200);
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
                        hitCombotext.setText(String.format("hit combo:\n %02d", hitCombo));
                    }

                }

                if (player.getLife() <= 0) {
                    Log.d(TAG, "Game Over"); //終結倒數 想辦法還原到初始狀態
                    gaming = false;
                    gameCountDownFinish = true;
                    ruleText.setText("遊戲結束");

                    if (hitCombo > topHitCombo) {
                        topHitCombo = hitCombo;
                        save();
                    }
                    gameState = GameState.GAME_OVER;
                    onAction(GameState.GAME_OVER);

                } else {
                    onAction(GameState.START_GAME);
                }
                break;
        }
    }

    private boolean scoreCompute() {
        int scoreAdd = 0;
        boolean lifeAdd = false;
        scoreAdd = 5 + (int) Math.sqrt(combo * stageCount / 2);
        scoreAdd = scoreAdd * scoreRate;
        if (easyMode) {
            scoreAdd = scoreAdd / 3;
        }
        score = score + scoreAdd;
        scoreText.setText(String.valueOf(score));

        while ((score / 1000) > lifeBonusTimes) {
            player.setLife(player.getLife() + 1);
            lifeBonusTimes++;
            lifeAdd = true;
        }

        heartText.setText(player.getLifeString());
        return lifeAdd;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.sound_switch:
                soundOn = !soundOn;
                if (soundOn) {
                    item.setTitle(R.string.sound_off);
                } else {
                    item.setTitle(R.string.sound_on);
                }
                break;
            case R.id.about:
                new AlertDialog.Builder(this)
                        .setTitle(getResources().getString(R.string.about) + " " + getResources().getString(R.string.app_name))
                        .setMessage(new About().getContent())
                        .setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create().show();
                break;
            case R.id.easy_mode:
                easyMode = !easyMode;
                if (easyMode) {
                    item.setTitle(R.string.normal_mode);
                } else {
                    item.setTitle(R.string.easy_mode);
                }
                break;
            case R.id.how_to_play:
                new AlertDialog.Builder(this)
                        .setTitle(getResources().getString(R.string.how_to_play))
                        .setMessage(new HowToPlay(beginMilliSecond, roundStep, minMilliSecond).getContent())
                        .setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create()
                        .show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void save() {
        SharedPreferences sharedPreferences = getSharedPreferences("Game", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("Combo", topHitCombo);
        editor.commit();
    }

    public void load() {
        //不應該自動把之前最高紀錄 當作本場最高紀錄
        SharedPreferences sharedPreferences = getSharedPreferences("Game", Context.MODE_PRIVATE);
        topHitCombo = sharedPreferences.getInt("Combo", 0);
        hitCombo = topHitCombo;
        //hitCombotext.setText(String.valueOf(topHitCombo));
        //讀取之後 在開始遊戲就秀出之前最高紀錄
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (gaming == true) {
                return false;
            }

            showExitDialog();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void showExitDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setTitle(R.string.return_to_title);
        //alertDialog.setMessage(R.string.return_to_title);
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
        int sound = computer.getRule().ordinal() + 2;
        playSound(sound);
        gameCountDownFinish = false;
        gameMilliSecond = 0;

        if (gameTimer != null) {
            gameTimer.removeCallbacks(this);
        }
        gameTimer = new Handler(Looper.getMainLooper());
        gameTimer.post(this);

    }

    private void init() {
        lifeBonusTimes = 0;
        score = 0;
        scoreText.setText("0");
        player = new Player();
        computer = new Computer(this);
        gameState = GameState.INIT_GAME;
        stageCount = 1;
        countSecond = 3;
        round = 1;
        combo = 0;
        lifeBonus = 0;
        targetMilliSecond = beginMilliSecond;
        player.setLife(player.getINIT_LIFE());
        this.setTitle("猜拳反應遊戲");
        ruleText.setText("我是規則");
        heartText.setText(player.getLifeString());
        countText.setText(" ");
        winCountText.setText("00");
        roundText.setText(getResources().getString(R.string.round));
    }

    private void startGame() {

        gameMilliSecond = 0;
        gameCountDownFinish = false;

        this.setTitle(getResources().getString(R.string.app_name) + " 本關限時 " + targetMilliSecond + " 毫秒");

        roundText.setText("ROUND: " + round++);

        //stageText.setText(String.valueOf(stageCount));
        winCountText.setText(String.valueOf(player.getWinCount()));

        //新的愛心血量
        heartText.setText(player.getLifeString());

        //舊的文字血量
        //String life = String.format("HP:%d", player.getLife());
        //lifeText.setText(life);

        gaming = true;
        if (gaming) {

/*
            ruleText.setVisibility(View.INVISIBLE);
            comImg.setVisibility(View.INVISIBLE);*/
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
                if (gameOver = true && gaming == false) {
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

        targetMilliSecond = beginMilliSecond - (round / roundStep) * 500;

        if (targetMilliSecond < minMilliSecond) {
            targetMilliSecond = minMilliSecond;
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
        String timer = String.format("%d:%d", sec, ms/100);
        countText.setText(timer);
        //gameTimer.post(this);
        gameTimer.postDelayed(this, 100);

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
        switch (colorRandom % 6) {
            case 0:
                ruleText.setTextColor(getResources().getColor(R.color.colorDarkRed));
                break;
            case 1:
                ruleText.setTextColor(getResources().getColor(R.color.colorGreen));
                break;
            case 2:
                ruleText.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                break;
            case 3:
                ruleText.setTextColor(getResources().getColor(R.color.colorBlack));
                break;
            case 4:
                ruleText.setTextColor(getResources().getColor(R.color.colorBrown));
                break;
            case 5:
                ruleText.setTextColor(getResources().getColor(R.color.colorBlue2));
                break;
        }
    }

}