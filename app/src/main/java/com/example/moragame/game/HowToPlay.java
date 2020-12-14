package com.example.moragame.game;

public class HowToPlay {
    int beginMilliSecond, roundStep, minMilliSecond;

    public HowToPlay(int beginMilliSecond, int roundStep, int minMilliSecond) {
        this.beginMilliSecond = beginMilliSecond;
        this.roundStep = roundStep;
        this.minMilliSecond = minMilliSecond;
    }

    public String getContent() {
        String content =
                "依照的規則與電腦出拳，判斷你該出什麼。\n" +
                        "例：規則是「請讓我輸」，電腦出「布」，則你該出「剪刀」才是正確的。\n" +
                        "出錯或時間內未出拳會扣生命\n\n" +
                        "初始思考秒數為" + minMilliSecond + "毫秒\n每過" + roundStep + "關會縮短500毫秒\n" +
                        "最低不會低於" + minMilliSecond + "毫秒\n" +
                        "每獲得1000分會增加一點生命\n" +
                        "生命上限為9"+
                        "簡單模式下獲得的分數只有三分之一 ";
        return content;
    }
}
