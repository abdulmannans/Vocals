package com.example.splashscreen;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class hashPass {
    static int i =0;
    hashPass(){

    }
    public static String hashedPass(String pass){
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-1");

            byte[] messageDigest = md.digest(pass.getBytes());

            BigInteger no = new BigInteger(1, messageDigest);

            String hashText = no.toString(16);

            while (hashText.length() < 32) {
                hashText = "0" + hashText;
            }

            return hashText;
        }catch (NoSuchAlgorithmException e){
            throw new RuntimeException(e);
        }
    }
    public int Count(){
        i++;
        return i;
    }
}
