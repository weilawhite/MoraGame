package com.example.moragame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.moragame.game.Computer;
import com.example.moragame.game.Mora;
import com.example.moragame.game.Player;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button startBtn, quitBtn;
    private ImageButton scissorBtn, paperBtn, rockBtn;
    private ImageView comImg;
    private final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findView();
        //init();

    }

    private void init() {
        Player player = new Player();
        Computer computer = new Computer();
        computer.AI();
        comImg.setImageResource(Mora.getMoraResId(computer.getMora()));

    }

    private void findView() {
        startBtn = findViewById(R.id.start_btn);
        quitBtn = findViewById(R.id.quit_btn);
        scissorBtn = findViewById(R.id.scissors_ibn);
        paperBtn = findViewById(R.id.paper_ibn);
        rockBtn = findViewById(R.id.rock_ibn);
        comImg = findViewById(R.id.computer_img);

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
                break;
            case R.id.scissors_ibn:
                Log.d(TAG, getResources().getString(R.string.scissors));
                break;
            case R.id.rock_ibn:
                Log.d(TAG, getResources().getString(R.string.rock));
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