package com.example.moragame.game;

import com.example.moragame.R;

public enum Mora {
    SCISSOR,ROCK,PAPER,NONE;

    //丟進拳頭 回傳圖片的ID
    public static int getMoraResId(Mora mora){
        int resId[]={R.drawable.scissors,R.drawable.rock,R.drawable.paper};
        return resId[mora.ordinal()];
    }
}
