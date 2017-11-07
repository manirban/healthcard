/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.Connection;
import java.sql.DriverManager;


/**
 *
 * @author Maitreya SPML
 */
public class CardDatabaseHandler {
    private Connection con = null;
    private String url=null;
    public Connection getDBConnection(String dbPath)throws Exception
    {
        url="jdbc:sqlite:"+dbPath;        
        getSQLITEDBConnection();
        return con;
    }
    


    public void getSQLITEDBConnection() throws Exception
    {
        Class.forName("org.sqlite.JDBC");
        con = DriverManager.getConnection(url);
    }
}
