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

/**
 *
 * @author Maitreya SPML
 */
public class ExpertCheckRelated {

    public boolean checkExpert(String expertid) {
        boolean stat = false;
        try {

            String sql = "SELECT\n"
                    + "ted.ExpertID\n"
                    + "FROM\n"
                    + "tbl_expert_details AS ted\n"
                    + "INNER JOIN tbl_expert_logininfo AS tel ON tel.ExpertID = ted.ExpertID\n"
                    + "WHERE\n"
                    + "ted.ExpertID = ? AND\n"
                    + "tel.AccountStat = 'ACTIVE'";
            Connection con = new DBConnection().getDBConnection();
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, expertid);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                stat = true;
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

    public String getExpertToken(String expertid) {
        String token = null;
        try {
            String sql = "SELECT\n"
                    + "ted.ExpertToken\n"
                    + "FROM\n"
                    + "tbl_expert_details AS ted\n"
                    + "WHERE\n"
                    + "ted.ExpertID = ?";
            Connection con = new DBConnection().getDBConnection();
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, expertid);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                token = rs.getString(1);
            }
            rs.close();
            pst.close();
            con.close();

        } catch (Exception e) {
            dbmodel.ExceptionLogger.writeToDB(this.getClass().getName() + ": " + e);
        } finally {
            return token;
        }
    }
    
    public String getExpertRegNumber(String expertid) {
        String token = null;
        try {
            String sql = "SELECT\n"
                    + "ted.RegistrationNumber\n"
                    + "FROM\n"
                    + "tbl_expert_details AS ted\n"
                    + "WHERE\n"
                    + "ted.ExpertID = ?";
            Connection con = new DBConnection().getDBConnection();
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, expertid);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                token = rs.getString(1);
            }
            rs.close();
            pst.close();
            con.close();

        } catch (Exception e) {
            dbmodel.ExceptionLogger.writeToDB(this.getClass().getName() + ": " + e);
        } finally {
            return token;
        }
    }
    
    public String getExpertProfilePhoto(String expertid) {
        String token = null;
        try {
            String sql = "SELECT\n"
                    + "ted.ProfilePhoto\n"
                    + "FROM\n"
                    + "tbl_expert_details AS ted\n"
                    + "WHERE\n"
                    + "ted.ExpertID = ?";
            Connection con = new DBConnection().getDBConnection();
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, expertid);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                token = rs.getString(1);
            }
            rs.close();
            pst.close();
            con.close();

        } catch (Exception e) {
            dbmodel.ExceptionLogger.writeToDB(this.getClass().getName() + ": " + e);
        } finally {
            return token;
        }
    }
    
    public String getExpertMedCenter(String expertid) {
        String token = null;
        try {
            String sql = "SELECT\n"
                    + "ted.MedicalCenter\n"
                    + "FROM\n"
                    + "tbl_expert_details AS ted\n"
                    + "WHERE\n"
                    + "ted.ExpertID = ?";
            Connection con = new DBConnection().getDBConnection();
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, expertid);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                token = rs.getString(1);
            }
            rs.close();
            pst.close();
            con.close();

        } catch (Exception e) {
            dbmodel.ExceptionLogger.writeToDB(this.getClass().getName() + ": " + e);
        } finally {
            return token;
        }
    }
    
    public String getExpertName(String expertid) {
        String token = null;
        try {
            String fname, lname;
            String sql = "SELECT\n"
                    + " ted.FirstName\n"
                    + ", ted.LastName\n"
                    + " FROM\n"
                    + " tbl_expert_details AS ted\n"
                    + " WHERE\n"
                    + " ted.ExpertID = ?";
            Connection con = new DBConnection().getDBConnection();
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, expertid);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                fname = rs.getString(1);
                lname = rs.getString(2);
                token = fname+" "+lname;
            }
            rs.close();
            pst.close();
            con.close();

        } catch (Exception e) {
            dbmodel.ExceptionLogger.writeToDB(this.getClass().getName() + ": " + e);
        } finally {
            return token;
        }
    }

}
