package com.example.moragame.game;

import android.util.Log;

import com.example.moragame.GameState;
import com.example.moragame.OnActionListener;

import java.util.Random;

public class Computer extends Player {
    Rule rule;
    OnActionListener listener;

    public Computer(OnActionListener listener) {
        this.listener = listener;
    }

    public Rule getRule() {
        return rule;
    }

    public String getRuleString() {
        return rule.toString();
    }

    public void setRule(Rule rule) {
        this.rule = rule;
    }

    public void AI() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setMora(getRandomMora());
        setRule(getRandomRule());
        Log.d("MainActivity","AI");

        listener.onAction(GameState.PLAYER_ROUND);
    }

    public static Mora getRandomMora() {
        int index = new Random().nextInt(Mora.PAPER.ordinal() + 1);
        if (index == 0) {
            return Mora.SCISSOR;
        }
        if (index == 1) {
            return Mora.PAPER;
        }
        if (index == 2) {
            return Mora.ROCK;
        }
        return null;
    }

    public static Rule getRandomRule() {
        int index = new Random().nextInt(Rule.NOT_LOSE.ordinal() + 1);
        if (index == 0) {
            return Rule.P_WIN;
        }
        if (index == 1) {
            return Rule.P_LOSE;
        }
        if (index == 2) {
            return Rule.C_WIN;
        }
        if (index == 3) {
            return Rule.C_LOSE;
        }
        if (index == 4) {
            return Rule.EVEN;
        }
        if (index == 5) {
            return Rule.NOT_EVEN;
        }
        if (index == 6) {
            return Rule.NOT_WIN;
        }
        if (index == 7) {
            return Rule.NOT_LOSE;
        }


        return null;
    }

}
