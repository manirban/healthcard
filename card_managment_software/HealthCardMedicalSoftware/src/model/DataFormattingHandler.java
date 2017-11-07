/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 *
 * @author Maitreya SPML
 */
public class DataFormattingHandler {
    public ArrayList<String> getPatientDemographic() {
        ArrayList<String> patient = new ArrayList<String>();
        try {
            File f = new File("./temp/TBL_DEMOGRAP.db");
            Connection con = new CardDatabaseHandler().getDBConnection(f.getAbsolutePath());
            String sql = "SELECT\n"
                    + "td.CardID,\n"
                    + "td.FirstName,\n"
                    + "td.LastName,\n"
                    + "td.YOB,\n"
                    + "td.Gender,\n"
                    + "td.Address,\n"
                    + "td.EmgContact,\n"
                    + "td.GovtID,\n"
                    + "td.ProfilePhoto\n"
                    + "FROM\n"
                    + "tbl_demographic AS td";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                patient.add(rs.getString(1));
                patient.add(rs.getString(2));
                patient.add(rs.getString(3));
                patient.add(rs.getString(4));
                patient.add(rs.getString(5));
                patient.add(rs.getString(6));
                patient.add(rs.getString(7));
                patient.add(rs.getString(8));
                patient.add(rs.getString(9));
            }
            rs.close();
            pst.close();
            con.close();
        } catch (Exception e) {
            System.err.println(this.getClass().getName() + ": getPatientDemographic :" + e);
        } finally {
            return patient;
        }
    }
}
