package org.activiti.rest.controller;

/**
 * @author Belyavtsev Vladimir Vladimirovich (BW)
 */
public class Renamer {

    private static final String sa = new String("����������������������������������");
    private static final String[] as = { "a", "b", "v", "g", "d", "e", "yo", "g", "z", "i", "y", "i",
            "k", "l", "m", "n", "o", "p", "r", "s", "t", "u",
            "f", "h", "tz", "ch", "sh", "sh", "'", "e", "yu", "ya", "ji", "i", "e" };

    public static String sRenamed(String sOld) {
        StringBuilder sNew = new StringBuilder();
        char[] aChar = sOld.toLowerCase().toCharArray();
        for (int i = 0; i < aChar.length; i++) {
            int nAt = sa.indexOf(aChar[i]);
            sNew.append(nAt != -1 ? as[nAt] : aChar[i]);
        }
        return sNew.toString();
    }
}
