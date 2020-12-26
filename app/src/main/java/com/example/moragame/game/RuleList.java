package com.example.moragame.game;

public class RuleList {
    public RuleList() {
    }

    public String getContent() {
        //P_WIN,P_LOSE,C_WIN,C_LOSE,EVEN,NOT_EVEN,NOT_WIN,NOT_LOSE
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append
                ("規則一覽:\n\n" +
                        "1.請贏我\n" +

                        "2.請輸我\n" +

                        "3.請讓我贏\n" +

                        "4.請讓我輸\n" +

                        "5.請跟我平手\n" +

                        "6.請不要平手\n" +

                        "7.請不要贏我\n" +

                        "8.請不要輸我\n" +

                        "9.? (預定加入)\n" +

                        "10.? (預定加入)\n" +

                        "11.? (預定加入)\n" +
                        "");
        return stringBuilder.toString();


    }
}
