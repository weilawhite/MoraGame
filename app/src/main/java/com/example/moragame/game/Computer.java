package com.example.moragame.game;

import java.util.Random;

public class Computer extends Player {
    Rule rule;

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
        setMora(getRandomMora());
        setRule(getRandomRule());
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
