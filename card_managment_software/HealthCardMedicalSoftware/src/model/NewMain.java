/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author Maitreya SPML
 */
public class NewMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        // TODO code application logic here
        Connection con = new CardDatabaseHandler().getDBConnection("D:\\Maitreya\\PAPER\\Card_Paper\\db\\TBL_DEMOGRAP.db");
        if (con != null) {
            System.out.println("Connection to SQLite has been established.");
        }
        FileInputStream fis = new FileInputStream("D:\\FC05008.jpg");
        byte[] data = new byte[fis.available()];
        fis.read(data);
        fis.close();
        String profilestr = Base64.encodeBase64URLSafeString(data);
        String sql = "UPDATE  tbl_demographic SET ProfilePhoto=? WHERE CardID='123412341234';)";
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setString(1, profilestr);
        stmt.executeUpdate();
        stmt.close();
        //c.close();
        con.close();

    }

}
