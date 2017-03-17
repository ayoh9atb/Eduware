/*
 * CryptoSystem.java
 *
 * Created on October 8, 2007, 3:19 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rsdynamix.projects.security.bean;

/**
 *
 * @author p-aniah
 */

import java.util.*;
import java.io.*;

public class CryptoSystem {
    
    private String buffer;
    private List privilegeList;
    private Properties propertyObj;
    private int propCardinality;
    
    /** Creates a new instance of CryptoSystem */
    public CryptoSystem() {
    }
    
    private String key = "k~A@l#akp_*7ok&po$kp^oKAL5_6An+wO`fiAGur564u1nyIaimonekeIPIMINye03";
    private int maxLength = 127;
    private int sentinel = 273104;
    private long discriminant = 268679193;
    
    boolean isAlpha(char cc){
        return (((int)cc >= 65)&&((int)cc <= 122));
    }
    
    boolean isSpace(char cc){
        return (((int)cc >= 0)&&((int)cc <= 32));
    }
    
    int skipwhite(String str,int index){
        char look = str.charAt(index);
        while((isSpace(look))&&(index <= str.length()-1)){
            setBuffer(getBuffer() + String.valueOf(str.charAt(index++)));
            look = str.charAt(index);
        }
        return index;
    }
    
    int asciiTag(char ch){
        return (int)ch;
    }
    
    char f(int message, int key){
        return (char)((message + key) % getMaxLength());
    }
    
    int fNumber(int message, int key){
        return ((message + key) % getMaxLength());
    }
    
    char df(int cipher, int key){
        if(cipher < key){
            return (char)(((cipher + getMaxLength()) - key) % getMaxLength());
        }else{
            return (char)((cipher - key) % getMaxLength());
        }
    }
    
    public String encipherToNumberString(String message){
        String cipherText = "";
        int keyCount = 0;
        
        for(int i = 0;i <= message.length()-1;i++){
            if(keyCount > getKey().length()-1){
                keyCount = 0;
            }
            cipherText += String.valueOf(fNumber(message.charAt(i), getKey().charAt(keyCount++)))+"_";
        }
        
        return cipherText.substring(0, cipherText.length()-1);
    }
    
    int [] getCipherCharArray(String cipher){
        String [] cipherArrayStr = cipher.split("_");
        int [] cipherArray = new int[cipherArrayStr.length];
        
        for(int i = 0;i <= cipherArrayStr.length-1;i++){
            cipherArray[i] = Integer.parseInt(cipherArrayStr[i]);
        }
        
        return cipherArray;
    }
    
    public String decipherNumberString(String cipher){
        String messageText = "";
        int keyCount = 0;
        int [] cipherArray = getCipherCharArray(cipher);
        
        for(int i = 0;i <= cipherArray.length-1;i++){
            if(keyCount > getKey().length()-1){
                keyCount = 0;
            }
            messageText += String.valueOf(df(cipherArray[i], getKey().charAt(keyCount++)));
        }
        
        return messageText;
    }
    
    public String getEnciphered(String message){
        String cipherText = "";
        int keyCount = 0;
        
        for(int i = 0;i <= message.length()-1;i++){
            if(keyCount > getKey().length()-1){
                keyCount = 0;
            }
            cipherText += String.valueOf(f(message.charAt(i), getKey().charAt(keyCount++)));
        }
        
        return cipherText;
    }
    
    public String getDeciphered(String cipher){
        String messageText = "";
        int keyCount = 0;
        if(cipher != null){
            for(int i = 0;i <= cipher.length()-1;i++){
                if(keyCount > getKey().length()-1){
                    keyCount = 0;
                }
                messageText += String.valueOf(df(cipher.charAt(i), getKey().charAt(keyCount++)));
            }
        }
        return messageText;
    }
    
    double power(double x,int n){
        double pow = 0;
        if(n == 0){
            pow = 1;
        }else if(n > 0){
            pow = x * power(x,n-1);
        }else if(n < 0){
            pow = 1/(x * power(x,abs(n)-1));
        }
        
        return pow;
    }
    
    int abs(int val){
        int absVal = val;
        if(val < 0){
            absVal = -val;
        }
        
        return absVal;
    }
    
    boolean isDigit(char cc){
        return (((int)cc >= (int)'0')&&((int)cc <= (int)'9'));
    }
    
    String posStr(int pos){
        String str = "";
        if((pos % 10 == 1)&&(pos != 11))
            str = String.valueOf(pos) + "st";
        else if((pos % 10 == 2)&&(pos != 12))
            str = String.valueOf(pos) + "nd";
        else if((pos % 10 == 3)&&(pos != 13))
            str = String.valueOf(pos) + "rd";
        else
            str = String.valueOf(pos) + "th";
        return str;
    }
    
    
    long reverse(long numb){
        String nStr = String.valueOf(numb);
        
        String revStr = "";
        for(int i = 0;i <= nStr.length()-1;i++){
            revStr += String.valueOf(nStr.charAt(nStr.length()-(i+1)));
        }
        
        return Long.parseLong(revStr);
    }
    
    String reverseBin(String nStr){
        String revStr = "";
        for(int i = 0;i <= nStr.length()-1;i++){
            revStr += String.valueOf(nStr.charAt(nStr.length()-(i+1)));
        }
        
        return revStr;
    }
    
    long toDec(String str){
        long dec = 0;
        for(int i = 0;i <= str.length()-1;i++){
            dec += Long.parseLong(String.valueOf(str.charAt(i))) * (int)Math.pow(2,str.length()-(i+1));
        }
        
        return dec;
    }
    
    String toBin(long numb){
        long num = numb;
        long rem = 0;
        String binStr = "";
        while(num != 0){
            rem = num % 2;
            num = num/2;
            binStr += String.valueOf(rem);
        }
        
        return reverseBin(binStr);
    }
    
    long encrypt(long m, long k, long n){
        long f = (m + k)% n;
        return(f);
    }
    
    long decrypt(long f, long k, long n){
        long m = 0;
        if(f >= k){
            m = (f - k)% n;
        }else{
            m = ((f+n) - k)% n;
        }
        
        return m;
    }
    
    public long encryptAccess(String accStr){
        long mantissa = getPropCardinality() + discriminant;
        return(encrypt(toDec(accStr),sentinel,mantissa));
    }
    
    public String decryptAccess(long code){
        long mantissa = getPropCardinality() + discriminant;
        String accStr = toBin(decrypt(code, sentinel, mantissa));
        
        String retStr = "";
        for(int i = accStr.length();i <= getPropCardinality()-1;i++){
            retStr += "0";
        }
        retStr += accStr;
        
        return retStr;
    }
    
    public int computePropCardinality(ArrayList systemAccessList){
        int cardinality = 0;
        Iterator accessIt = systemAccessList.iterator();
        while(accessIt.hasNext()){
            accessIt.next();
            cardinality++;
        }
        
        return cardinality;
    }
    
    public ArrayList getUserAccessListFromBinary(ArrayList systemAccessList, String accessBinary){
        ArrayList accessList = new ArrayList();
        String temp = "";
        Iterator systemIt = systemAccessList.iterator();
        for(int i = 0;i <= accessBinary.length()-1;i++){
            if(systemIt.hasNext()){
                temp = (String)systemIt.next();
                if(accessBinary.charAt(i) == '1'){
                    accessList.add(temp);
                }else{
                    accessList.add("");
                }
            }else{
                i = accessBinary.length();
            }
        }
        
        return accessList;
    }
    
    public String getUserAccessListInBinary(ArrayList systemAccessList, ArrayList selectedAccessList){
        Iterator selectedIt = selectedAccessList.iterator();
        String accessList = "";
        
        while(selectedIt.hasNext()){
            String selectedString = (String)selectedIt.next();
            accessList += getUserRightInBinary(systemAccessList, selectedString);
        }
        
        return accessList;
    }
    
    public String getUserRightInBinary(ArrayList systemAccessList, String selectedAccess){
        Iterator systemIt = systemAccessList.iterator();
        String accessRet = "0";
        boolean found = false;
        
        try{
            if(!selectedAccess.trim().equals("")){
                while(!found){
                    if(systemIt.hasNext()){
                        String systemString = (String)systemIt.next();
                        if(selectedAccess.equalsIgnoreCase(systemString)){
                            accessRet = "1";
                            found = true;
                        }
                    }
                }
            }
        }catch(Exception ex){}
        
        return accessRet;
    }
    
    String replaceAt(String str,char ch,int pos){
        String ret = "";
        for(int i = 0;i <= str.length()-1;i++){
            if(i == pos){
                ret += String.valueOf(ch);
            }else{
                ret += String.valueOf(str.charAt(i));
            }
        }
        return ret;
    }
    
    public String getKey() {
        return key;
    }
    
    public void setKey(String key) {
        this.key = key;
    }
    
    public int getMaxLength() {
        return maxLength;
    }
    
    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }
    
    public String getBuffer() {
        return buffer;
    }
    
    public void setBuffer(String buffer) {
        this.buffer = buffer;
    }
    
    public List getPrivilegeList() {
        return privilegeList;
    }
    
    public void setPrivilegeList(List privilegeList) {
        this.privilegeList = privilegeList;
    }
    
    public int getPropCardinality() {
        return propCardinality;
    }
    
    public void setPropCardinality(int propCardinality) {
        this.propCardinality = propCardinality;
    }
    
    public String getEncryptedTableName(String tableName){
        String tableNameCipher = getEnciphered(tableName);
        String alphaNumericTableName = "";
        
        for(int i = 0;i <= tableNameCipher.length()-1;i++){
            if(isAlpha(tableNameCipher.charAt(i))){
                alphaNumericTableName += String.valueOf(tableNameCipher.charAt(i));
            }else{
                alphaNumericTableName += "_" + String.valueOf((int)tableNameCipher.charAt(i))+"_";
            }
        }
        
        return alphaNumericTableName;
    }
}
