package com.fei.lib;

public class MyClass {

    public static void main(String[] args) {

        String[] array = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n",
                "o","p","q","r","s","t","u","v","w","x","y","z"};
        for (int i = 0; i < array.length; i++) {
            if (i % 3 == 0 || i == 0 || i ==25) {
                System.out.println(array[i]);
            }
        }
    }

}