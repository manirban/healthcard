/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dbmodel;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author kma infonet
 */
public class DBConnection {
    
    private Connection con = null;
    private String url=null,dbname=null,username=null,password=null;
    public Connection getDBConnection()throws Exception
    {
        url="jdbc:mysql://127.0.0.1:3306/";
        dbname="server_user_primarydb";
        username="root";
        password="";
        getMySQLDBConnection();
        return con;
    }
    


    public void getMySQLDBConnection() throws Exception
    {
        Class.forName("com.mysql.jdbc.Driver");
        con = DriverManager.getConnection(url+dbname,username,password);
    }

}
