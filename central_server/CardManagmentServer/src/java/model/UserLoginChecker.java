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
public class UserLoginChecker {
    /**
     * @return the expertID
     */
    public String getExpertID() {
        return expertID;
    }

    /**
     * @param ExpertID the expertID to set
     */
    public void setExpertID(String ExpertID) {
        this.expertID = ExpertID;
    }
    private String expertID;
    public boolean checkExpertLogin(String loginid, String password, String accessrole) {        
        boolean stat = false;
        try {
            String sql = "SELECT\n"
                    + "tel.LoginPwd,\n"
                    + "tel.AccessRole,\n"
                    + "tel.AccountStat,\n"
                    + "tel.ExpertID\n"
                    + "FROM\n"
                    + "tbl_expert_logininfo AS tel\n"
                    + "WHERE\n"
                    + "tel.LoginID = ?";
            Connection con = new DBConnection().getDBConnection();
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, loginid);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                String p = rs.getString(1);
                String a = rs.getString(2);
                String s = rs.getString(3);
                if (p.equals(password) && a.equals(accessrole) && s.equals("ACTIVE")) {
                    stat = true;
                    expertID = rs.getString(4);
                    entryLogin();
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

    private void entryLogin() throws SQLException {
        Connection con = null;
        try {
            String sql = "INSERT INTO tbl_expert_accesslog VALUES(?, Now(), 'LogIn', '123');"; 
            con = new DBConnection().getDBConnection();
            con.setAutoCommit(false);
            PreparedStatement pst = con.prepareStatement(sql);       
            pst.setString(1,expertID);           
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

}
