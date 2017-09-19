package com.tddp2.grupo2.linkup.utils;



public class LinkupUtils {

    public static String getChatId(String fbidU, String fbidL){

        String chatId = "";
        if (fbidU.compareTo(fbidL)<=0){
            chatId = fbidU+":"+fbidL;
        }else{
            chatId = fbidL+":"+fbidU;
        }
        return chatId;
    }
}
