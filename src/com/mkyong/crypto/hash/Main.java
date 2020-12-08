package com.mkyong.crypto.hash;
        import java.security.NoSuchAlgorithmException;
        import java.util.Arrays;
        import java.util.Random;
        import java.util.Scanner;
        import java.security.SecureRandom;
        import java.security.MessageDigest;

public class Main {

    public static void main(String[] args) throws NoSuchAlgorithmException {
        Random r = new Random();
        int min = 1;
        Scanner in = new Scanner(System.in);
        System.out.print("Enter line: ");
        String str = in.nextLine();
        String lineToHash = str;
        byte[] salt = getKey();
        String secureLine = SecurePassword(lineToHash, salt);
        System.out.println("HMAC: " + secureLine);
        String[] words = str.split("\\s");
        String[] newArr = Arrays.copyOf(words, words.length + 1);
        for(int i = 0; i < newArr.length; i++) {
            if (i == 0) {
                newArr[i] = "exit";
            } else {
                newArr[i] = words[i - 1];
            }
        }
        int max = newArr.length - 1;
        int rIndex = r.nextInt((max - min) + 1) + min;
        if ( words.length % 2 != 1 ){
            System.out.println("Введите нечетное количество строк >= 3. (Например: rock paper scissors)");
            return;
        } else if (words.length < 3){
            System.out.println("Введите нечетное количество строк >= 3. (Например: rock paper scissors)");
            return;
        }
        for(int i = 0; i < words.length; i++){
            for(int j = i + 1; j < words.length; j++){
                if (words[i] == words[j]) {
                    System.out.println("Нельзя вводить одинаковые строки, только уникальные. (Например: rock paper scissors)");
                    return;
                }
            }
        }
        int medium = Math.round(words.length / 2);
        System.out.println("Available moves:");
        for(int i = 0; i < newArr.length; i++){
            System.out.println(i + "-" + newArr[i]);
        }
        Scanner inIndex = new Scanner(System.in);
        System.out.print("Enter your move: ");
        int index = inIndex.nextInt();
        if (index == 0){
            return;
        } else if (index >= newArr.length) {
            for(int i = 0; i < newArr.length; i++){
                System.out.println(i + "-" + newArr[i]);
            }
            System.out.print("Enter your move: ");
            index = inIndex.nextInt();
            if (index == 0){
                return;
            }
        }
        System.out.println("Your move: " + newArr[index]);
        System.out.println("Computer move: " + newArr[rIndex]);
        if (rIndex > index) {
            if (rIndex - index > medium) {
                System.out.println("You win!");
            } else {
                System.out.println("You lose!");
            }
        } else {
            if (index - rIndex > medium) {
                System.out.println("You lose!");
            } else {
                System.out.println("You win!");
            }
        }
        String lineToUser = newArr[index];
        String secureLineUser = SecurePassword(lineToUser, salt);
        System.out.println("HMAC key: " + secureLineUser);
    }

    private static String SecurePassword(String lineToHash, byte[] salt)
    {
        String generatedKey = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA3-256");
            md.update(salt);
            byte[] bytes = md.digest(lineToHash.getBytes());
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedKey = sb.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return generatedKey;
    }


    private static byte[] getKey() throws NoSuchAlgorithmException
    {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] key = new byte[16];
        sr.nextBytes(key);
        return key;
    }
}