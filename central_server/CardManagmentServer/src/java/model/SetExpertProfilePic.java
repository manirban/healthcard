/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import dbmodel.DBConnection;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author Maitreya SPML
 */
public class SetExpertProfilePic {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        try{
            FileInputStream fis = new FileInputStream("D:\\MM.jpg");
            byte[] data = new byte[fis.available()];
            fis.read(data);
            fis.close();
            String profilestr = Base64.encodeBase64URLSafeString(data);
            
            System.out.println(profilestr);
            
            
             String sql = "UPDATE  tbl_expert_details SET ProfilePhoto=? WHERE ExpertID='XYZ1234567';"; 
            Connection con = new DBConnection().getDBConnection();
           // con.setAutoCommit(false);
            PreparedStatement pst = con.prepareStatement(sql);       
            pst.setString(1,profilestr);           
            pst.executeUpdate();
           // con.commit();
            pst.close();
            
            
        
        
        
        }catch(Exception e){System.err.println(e.toString());}
    }
    
}
