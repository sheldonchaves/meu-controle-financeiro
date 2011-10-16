/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.money.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe utilizada para criptografar password. Não existe como descriptografar, para validar
 * você deve criptografar o password passado e comparar com o password criptografado gravado
 * no banco.
 * Utiliza MD5
 * @author gbvbahia
 */
public class Criptografia {

    private static MessageDigest md = null;

    static {
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Criptografia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Criptografia() {
    }

    /**
     * Método utilizado para criptografar password. Não existe como descriptografar, para validar
     * você deve criptografar o password passado e comparar com o password criptografado gravado
     * no banco.
     * @param rawPass
     * @return Retorna o password criptografado.
     */
    public static String encodePassword(String rawPass) {
        return new String(hexCodes(md.digest(rawPass.getBytes())));
    }

    private static char[] hexCodes(byte[] text) {
        char[] hexOutput = new char[text.length * 2];
        String hexString;
        for (int i = 0; i < text.length; i++) {
            hexString = "00" + Integer.toHexString(text[i]);
            hexString.toUpperCase().getChars(hexString.length() - 2,
                    hexString.length(), hexOutput, i * 2);
        }
        return hexOutput;
    }
}
