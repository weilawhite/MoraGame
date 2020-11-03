package com.example.moragame.game;

public enum WinState {
    PLAYER_WIN,COMPUTER_WIN,EVEN,IDEL;

    public static WinState getWinState(Mora player,Mora computer){

        if(player==computer){
            return EVEN;
        }
        if(player==Mora.SCISSOR&&computer==Mora.PAPER){
            return PLAYER_WIN;
        }
        if(player==Mora.PAPER&&computer==Mora.SCISSOR){
            return COMPUTER_WIN;
        }
        if(player.ordinal()>computer.ordinal()){
            return PLAYER_WIN;
        }
        else {
            return COMPUTER_WIN;
        }
    }


}
