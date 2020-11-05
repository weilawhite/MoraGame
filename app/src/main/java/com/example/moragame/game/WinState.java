package com.example.moragame.game;

public enum WinState {
    PLAYER_WIN, COMPUTER_WIN, EVEN, IDEL;

    public static WinState getWinState(Mora player, Mora computer) {
        WinState result;
        if (player == computer) {
            return EVEN;
        }
        if (player == Mora.SCISSOR && computer == Mora.PAPER) {
            return PLAYER_WIN;
        }
        if (player == Mora.PAPER && computer == Mora.SCISSOR) {
            return COMPUTER_WIN;
        }
        if (player.ordinal() > computer.ordinal()) {
            return PLAYER_WIN;
        } else {
            return COMPUTER_WIN;
        }
    }

    public static WinState getWinState(Mora player, Mora computer, Rule rule) {
        WinState result;
        if(player==Mora.NONE){
            return COMPUTER_WIN;
        }
        result = getWinState(player, computer);
        if (rule == Rule.P_WIN || rule == Rule.C_LOSE) {
            if (result == PLAYER_WIN) return PLAYER_WIN;
            return COMPUTER_WIN;
        }
        if (rule == Rule.P_LOSE || rule == Rule.C_WIN) {
            if (result == COMPUTER_WIN) return PLAYER_WIN;
            return COMPUTER_WIN;
        }
        if (rule == Rule.EVEN) {
            if (result == EVEN) return PLAYER_WIN;
            return COMPUTER_WIN;
        }
        if (rule == Rule.NOT_EVEN) {
            if (result != EVEN) return PLAYER_WIN;
            return COMPUTER_WIN;
        }
        if (rule == Rule.NOT_WIN) {
            if (result != PLAYER_WIN) return PLAYER_WIN;
            return COMPUTER_WIN;
        }
        if (rule == Rule.NOT_LOSE) {
            if (result != COMPUTER_WIN) return PLAYER_WIN;
            return COMPUTER_WIN;
        }
        return EVEN;

        /*
        if (rule == Rule.P_WIN||rule== Rule.C_LOSE) {
            if (player == Mora.SCISSOR && computer == Mora.PAPER) {
                return PLAYER_WIN;
            }
            if (player == Mora.PAPER && computer == Mora.SCISSOR) {
                return COMPUTER_WIN;
            }
            if (player.ordinal() > computer.ordinal()) {
                return PLAYER_WIN;
            }
            return COMPUTER_WIN;

        }
        if (rule == Rule.P_LOSE||rule==Rule.C_WIN) {

            if (player == Mora.PAPER && computer == Mora.SCISSOR) {
                return PLAYER_WIN;
            }
            if (player == Mora.SCISSOR && computer == Mora.PAPER) {
                return COMPUTER_WIN;
            }
            if (player.ordinal() < computer.ordinal()) {
                return PLAYER_WIN;
            }
            return COMPUTER_WIN;

        }

        if(rule==Rule.EVEN){
            if(player.equals(computer)){
                return PLAYER_WIN;
            }
            return COMPUTER_WIN;
        }
        if(rule==Rule.NOT_EVEN){
            if(!player.equals(computer)){
                return PLAYER_WIN;
            }
            return COMPUTER_WIN;
        }
        if(rule==Rule.NOT_WIN){
            if (player == Mora.SCISSOR && computer == Mora.PAPER) {
                return COMPUTER_WIN;
            }
            if (player == Mora.PAPER && computer == Mora.ROCK) {
                return COMPUTER_WIN;
            }
            if (player == Mora.ROCK && computer == Mora.SCISSOR) {
                return COMPUTER_WIN;
            }
            return PLAYER_WIN;
        }
        if(rule==Rule.NOT_LOSE){
            if (player == Mora.PAPER && computer == Mora.SCISSOR) {
                return COMPUTER_WIN;
            }
            if (player == Mora.ROCK && computer == Mora.PAPER) {
                return COMPUTER_WIN;
            }
            if (player == Mora.SCISSOR && computer == Mora.ROCK) {
                return COMPUTER_WIN;
            }
            return PLAYER_WIN;
        }
        return EVEN;*/
    }

}
