package com.tddp2.grupo2.linkup.utils;


import java.util.HashSet;
import java.util.Set;

public class BannedWords {

    private static Set<String> badwords;

    public BannedWords() {
        loadBadWords();
    }

    private void loadBadWords() {
        badwords = new HashSet<String>();
        badwords.add("puto");
        badwords.add("puta");
        badwords.add("forro");
        badwords.add("forra");
        badwords.add("boludo");
        badwords.add("boluda");
        badwords.add("culo");
        badwords.add("mierda");
        badwords.add("pelotudo");
        badwords.add("pelotuda");
        badwords.add("cabron");
        badwords.add("imbécil");
        badwords.add("orto");
        badwords.add("chota");
        badwords.add("poronga");
        badwords.add("pija");
        badwords.add("mogólica");
        badwords.add("mogólico");
        badwords.add("cagón");
        //TODO: agregar o ver como llenar el set
    }

    public static boolean isBadWord(String word){
        return badwords.contains(word);
    }
}
