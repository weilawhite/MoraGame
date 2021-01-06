package com.example.moragame.game;

public class About {
    String content = "";

    public About() {
        //this.content = content;
    }

    public String getContent() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append
                ("v1.1 - 11/18\n" +
                "規則有語音說明（可開關）\n" +
                "新增選單功能，會提示規則\n" +
                "新增簡單模式，只會有兩種規則\n" +
                "新增加命功能（每1000分）\n" +
                "- - -\n"
        ).append("v1.2 - 11/30\n" +
                "不太美觀的版本\n" +
                "修正大量錯誤\n" +
                "新增遊戲主畫面，應該可以自由切換了，主畫面其他按鈕暫時隱藏\n" +
                "新增展示模式（分數３０倍）\n" +
                "加命音效改變\n" +
                "- - -\n"
        ).append("v1.22 - 12/14\n" +
                "修改圖標\n" +
                "修改各模式參數\n" +
                "- - -\n"
        ).append("v1.23 - 12/14\n" +
                "修改文檔錯誤\n" +
                "設定生命上限\n" +
                "- - -\n"
        ).append("v1.3 - 12/26\n" +
                "把1.0版移除的難度選項加回\n" +
                "主畫面增加三個說明按鈕\n"
        ).append("v1.31 - 1/7\n" +
                "修正遊戲選單、計時、結算文本\n" +
                "首頁選單切換時會自動捲回頂部");
        return stringBuilder.toString();
    }
}
