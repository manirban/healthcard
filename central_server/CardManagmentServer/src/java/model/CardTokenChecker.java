/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import dbmodel.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Maitreya SPML
 */
public class CardTokenChecker {

    private String securityKey;

    public boolean checkCard(String expertid, String cardnumber, String fingerprint) {
        boolean stat = false;
        try {            
            String sql = "SELECT\n"
                    + "tcd.SecurityToken,\n"
                    + "tcd.FingerPrint\n"
                    + "tcd.ClientID\n"
                    + "FROM\n"
                    + "tbl_client_detials AS tcd\n"
                    + "WHERE\n"
                    + "tcd.HealthCardNumber = ? AND\n"
                    + "tcd.AccountStatus = 'ACTIVE'";
            Connection con = new DBConnection().getDBConnection();
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, cardnumber);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                String key = rs.getString(1);
                String fp = rs.getString(2);
                String cid = rs.getString(3);
                if (checkFingerPrint(fingerprint, fp)) {
                    stat = true;
                    setSecurityKey(key);
                    entryCardAccess(cid,expertid);
                }
            }
            rs.close();
            pst.close();
            con.close();
        } catch (Exception e) {
            dbmodel.ExceptionLogger.writeToDB(this.getClass().getName() + ": " + e);
        } finally {
            return stat;
        }
    }
    
    private boolean checkFingerPrint(String fingerprint, String dbfingerprint){
        //Write code for fingerprint matching
        return true;
    }

     private void entryCardAccess(String clinetid, String expertid) throws SQLException {
        Connection con = null;
        try {
            String sql = "INSERT INTO tbl_card_accesslog VALUES(?, ?, Now(), 'CARDTOKENREQUEST');"; 
            con = new DBConnection().getDBConnection();
            con.setAutoCommit(false);
            PreparedStatement pst = con.prepareStatement(sql);       
            pst.setString(1,clinetid);
            pst.setString(1,expertid);
            pst.executeUpdate();
            con.commit();
            pst.close();
        } catch (Exception e) {
            dbmodel.ExceptionLogger.writeToDB(this.getClass().getName() + ": " + e);
            con.rollback();
        } finally {         
            con.close();
           
        }
    }

    /**
     * @return the securityKey
     */
    public String getSecurityKey() {
        return securityKey;
    }

    /**
     * @param securityKey the securityKey to set
     */
    public void setSecurityKey(String securityKey) {
        this.securityKey = securityKey;
    }
}
