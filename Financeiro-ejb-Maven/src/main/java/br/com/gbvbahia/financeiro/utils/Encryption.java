package br.com.gbvbahia.financeiro.utils;

import org.jasypt.util.text.BasicTextEncryptor;

public class Encryption {
    
        //TODO MUDAR EM PROD
        /**
         * O tempero na criptografia.
         */
	public static final String SAL = "sal";
        /**
         * Resp pela criptografia e descriptografia.
         */
	private static BasicTextEncryptor textEncryptor;
	static {
		textEncryptor = new BasicTextEncryptor();
		textEncryptor.setPassword(SAL);
	}
	/**
         * Criptografa.
         * @param toEncrypting String a ser criptografada.
         * @return 
         */
	public static String encrypting(String toEncrypting){
		return textEncryptor.encrypt(toEncrypting);
	}
	
        /**
         * Descriptografa
         * @param toDecrypting String a ser descriptografada.
         * @return 
         */
	public static String decrypting(String toDecrypting){
		return textEncryptor.decrypt(toDecrypting);
	}
}
