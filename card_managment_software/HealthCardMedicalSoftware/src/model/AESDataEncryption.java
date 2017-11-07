/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Maitreya SPML
 */
public class AESDataEncryption {
    public String getEncrypt(String plaintxt, String key){
        String encryptedString = AES.encrypt(plaintxt, key);
        return encryptedString;
    }
    
    public String getDecrypt(String ciphertxt, String key){
        String decryptedString = AES.decrypt(ciphertxt, key);
        return decryptedString;
    }
}
