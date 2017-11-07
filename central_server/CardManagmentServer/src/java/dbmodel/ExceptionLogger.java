/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dbmodel;

import java.sql.Connection;
import java.sql.PreparedStatement;


/**
 *
 * @author amber imam
 */
public class ExceptionLogger {


    public static void writeToDB(String text)
    {
        String errortxt = text;
        if(errortxt.trim().length()>500)
        {
            errortxt=errortxt.substring(0,498);
        }
        try
        {
            Connection con=new DBConnection().getDBConnection();
            PreparedStatement pst = con.prepareStatement("insert into tbl_exception_log values (NOW(),?)");
            pst.setString(1,errortxt.trim());
            pst.executeUpdate();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }
}
