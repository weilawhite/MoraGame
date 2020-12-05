package com.example.moragame.game;

public class About {
    String content = "";

    public About() {
        //this.content = content;
    }

    public String getContent() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("v1.1 - 11/18\n規則有語音說明（可開關）\n新增選單功能，會提示規則\n新增簡單模式，只會有兩種規則\n新增加命功能（每1000分）\n"
        ).append("v1.2 - 11/30\n不太美觀的版本\n修正大量錯誤\n新增遊戲主畫面，應該可以自由切換了，主畫面其他按鈕暫時隱藏\n新增展示模式（分數３０倍）\n加命音效改變"
        );
        return stringBuilder.toString();
    }
}
