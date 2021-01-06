package com.example.moragame;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.moragame.game.About;
import com.example.moragame.game.HowToPlay;
import com.example.moragame.game.RuleList;

public class Title2 extends AppCompatActivity {

    Button normalModeBtn, easyModeBtn, hardModeBtn, howToPlayBtn, exitBtn, demoBtn, aboutBtn,ruleBtn;
    ImageView titleImage;
    TextView titleText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title2);
        findView();
        Intent intent = new Intent(this, MainActivity.class);
        Bundle bundle = new Bundle();

        demoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putInt("mode", 5);
                bundle.putInt("scoreRate", 30);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        normalModeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putInt("mode", 2);
                //bundle.putInt("scoreRate",1);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });

        easyModeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putInt("mode", 1);
                //bundle.putInt("scoreRate",1);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        hardModeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putInt("mode", 3);
                //bundle.putInt("scoreRate",1);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        aboutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = new About().getContent();
                titleImage.setAlpha(0.1f);
                //titleImage.setVisibility(View.INVISIBLE);
                titleText.setText(text);
                titleText.scrollTo(0,0);
            }
        });

        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showExitDialog();
            }
        });

        ruleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = new RuleList().getContent();
                titleImage.setAlpha(0.1f);
                //titleImage.setVisibility(View.INVISIBLE);
                titleText.setText(text);
                titleText.scrollTo(0,0);
            }
        });

        howToPlayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = new HowToPlay().getContent();
                titleImage.setAlpha(0.1f);
                //titleImage.setVisibility(View.INVISIBLE);
                titleText.setText(text);
                titleText.scrollTo(0,0);
            }
        });

    }

    private void findView() {
        howToPlayBtn = findViewById(R.id.HowToPlay_button);
        hardModeBtn = findViewById(R.id.hard_mode);
        normalModeBtn = findViewById(R.id.normal_mode);
        easyModeBtn = findViewById(R.id.easy_mode);
        exitBtn = findViewById(R.id.exit);
        demoBtn = findViewById(R.id.demo);
        aboutBtn = findViewById(R.id.about_button);
        ruleBtn=findViewById(R.id.rule_button);
        titleImage = findViewById(R.id.title_image);
        titleText = findViewById(R.id.title_text);
        titleText.setMovementMethod(ScrollingMovementMethod.getInstance());
    }

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
        //alertDialog.setMessage(R.string.exit);
        alertDialog.setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Title2.this.finish();
                System.exit(0);
            }
        }).setPositiveButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        }).create().show();

    }


}