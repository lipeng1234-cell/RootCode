package algorithm;

import java.util.HashMap;

public class FindNoRepairStrMaxLength {

    public static void main(String[] args) {
        String a = "abcabcbb";
        HashMap<String, Integer> tempMap = new HashMap<>();
        char[] chars = a.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            String temp = String.valueOf(chars[i]);
            tempMap.put(temp, temp.length());
            for (int j = i + 1; j < chars.length; j++) {
                if (temp.contains(""))
                temp = String.valueOf(temp + chars[j]);
                if (!tempMap.containsKey(temp)){
                    tempMap.put(temp, temp.length());
                }
            }
        }
        System.out.println(tempMap);

    }


}
