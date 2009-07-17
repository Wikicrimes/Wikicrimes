package org.wikicrimes.util;

import java.io.IOException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;

import org.wikicrimes.model.ChavesCriptografia;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * 
 * Implementação da criptografia RSA 1024bits
 * 
 * @version 1.1v
 * @author philipp
 *
 */

public class RSA  
{  
    private static final String ALGORITHM = "RSA"; 
    private final static int SIZE=1024;
    
    public RSA() {
  	  //Security.addProvider(new BouncyCastleProvider());
  	  Security.addProvider(new com.sun.crypto.provider.SunJCE());
    }

    public KeyPair generateKey() throws NoSuchAlgorithmException, NoSuchProviderException  
    {  
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);  
        keyGen.initialize(SIZE,new SecureRandom());  

        return keyGen.generateKeyPair();  
    }
    
    //pq q eh Key e nao PublicKey, pq alem de se garantir que ninguem interpretar a mensagem
    //ao ser cifrada, tb se precisa garantir a autencidade da passagem da mensagem
    //(garantir que a pessoa X passou pra min realmente a sua mensagem Y, pq eu vou ter o par de chaves)
    public byte[] encrypt(byte[] text, Key key) throws Exception  
    {  
        byte[]retorno=null;
        
        try{  
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");  
  
            cipher.init(Cipher.ENCRYPT_MODE, key); 
            retorno=cipher.doFinal(text);
        }  
        catch (Exception e){  
      	  e.printStackTrace();
        }  
        return retorno;  
    }  
   
    /**
     * 
     * @param text
     * @param stringKey
     * @param isPrivate - se a string passada eh formada por uma chave privada, senao eh formada por uma publica
     * @return
     * @throws Exception
     */
    
    public List<String> encrypt(String text, String stringKey,boolean isPrivate) throws Exception{
    	if(isPrivate){
    		return encrypt(text,getPrivateKeyFromString(stringKey));
    	}else{
    		return encrypt(text,getPublicKeyFromString(stringKey));
    	}
    }

    public List<String> encrypt(String text, Key key) throws Exception  
    {  
  	  
  	  List<String> objQuebrado = new ArrayList<String>();
        try{  
      	  StringBuffer sb = new StringBuffer(text);
      	  
      	  int tamParticionado=115;
      	  int tam = text.length(),partes;
      	  
      	  partes=tam/tamParticionado;
      	  partes+=2;
          	  
      	  for(int i=1;i<partes;++i){
      		  String aux=null;
      		  if(sb.length()>tamParticionado){
      			  aux=sb.substring(0,tamParticionado);
      			  sb=sb.delete(0, tamParticionado);
      		  }else{
      			  aux=sb.substring(0,sb.length());
      			  sb=sb.delete(0,sb.length());
      		  }
      		  
      		  objQuebrado.add(encodeBASE64(encrypt(aux.getBytes("ISO-8859-1"),key)));
      	  }

            
        }  
        catch (Exception e){  
      	  e.printStackTrace();
        }  
        return objQuebrado;  
    }  
  

    public byte[] decrypt(byte[] text, Key key) throws Exception  
    {  
        byte[] dectyptedText = new byte[100];  
        try{    
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");  
              
            cipher.init(Cipher.DECRYPT_MODE, key);
            dectyptedText = cipher.doFinal(text); 
            
        }  
        catch (Exception e){  
      	  e.printStackTrace();
        }  
        return dectyptedText;  
  
    } 
    
    /**
     * 
     * @param text
     * @param stringKey
     * @param isPrivate - se a string passada eh formada por uma chave privada, senao eh formada por uma publica
     * @return
     * @throws Exception
     */
    public String decrypt(List<String> text, String stringKey,boolean isPrivate) throws Exception{
    	if(isPrivate){
    		return decrypt(text,getPrivateKeyFromString(stringKey));
    	}else{
    		return decrypt(text,getPublicKeyFromString(stringKey));
    	}
    }

    public String decrypt(List<String> text, Key key) throws Exception  
    {  
  	   
  	  
        String result="",saida=null;  
        try {  
      	  
            for(String s:text){
          	  byte[] dectyptedText = decrypt(decodeBASE64(s),key);  
                saida = new String(dectyptedText, "ISO-8859-1");
                
                result=result.concat(saida);
            } 
        }  
        catch (Exception e){  
      	  e.printStackTrace();
        }  
        return result;  
  
    }  
  
    public String getKeyAsString(Key key)  
    {  

        byte[] keyBytes = key.getEncoded();  
        
        BASE64Encoder b64 = new BASE64Encoder();  
        return b64.encode(keyBytes);  
    }  
   
    public PrivateKey getPrivateKeyFromString(String key) throws Exception  
    {  
    	BASE64Decoder b64 = new BASE64Decoder();
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);    
        EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(b64.decodeBuffer(key));  
        PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);  
        return privateKey;  
    }  
   
    public PublicKey getPublicKeyFromString(String key) throws Exception  
    {  
        BASE64Decoder b64 = new BASE64Decoder();  
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);  
        EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(b64.decodeBuffer(key));  
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);  
        return publicKey;  
    }  
  
    private static String encodeBASE64(byte[] bytes)  
    {  
        BASE64Encoder b64 = new BASE64Encoder();  
        return b64.encode(bytes);  
    }  
  
    private static byte[] decodeBASE64(String text) throws IOException  
    {  
        BASE64Decoder b64 = new BASE64Decoder();  
        return b64.decodeBuffer(text);  
    } 
    
    public ChavesCriptografia gerarChaves() throws NoSuchAlgorithmException, NoSuchProviderException{
    	ChavesCriptografia cc = new ChavesCriptografia();
    	
    	KeyPair kp = generateKey();
    	
    	cc.setChavePrivada(getKeyAsString(kp.getPrivate()));
    	cc.setChavePublica(getKeyAsString(kp.getPublic()));
    	
    	return cc;
    }
}

