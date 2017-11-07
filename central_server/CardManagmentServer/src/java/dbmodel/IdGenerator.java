/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dbmodel;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Connection;

public class IdGenerator
{
private String next_id;


// date wise Id Genarator.............

public synchronized String getNextIdDatewise(String tableName,String prefix,String columnName)throws Exception
{
  //  System.out.println(prefix);
     int temp=0;
       Connection con = null;
        try{
             con=new DBConnection().getDBConnection();
             con.setAutoCommit(false);
            Statement stmt=con.createStatement(); // getCon() is the return type of created Connection.
            ResultSet rs=stmt.executeQuery("select "+columnName.trim()+" from "+tableName.trim()+" order by '"+columnName.trim()+"'" );

                String name=prefix.trim();
                String subname=name.substring(7,15);
                String p_u_code = name.substring(1,6);



            while(rs.next())
            {
                String t_code_temp=rs.getString(1).trim();
                String user_code=t_code_temp.substring(1,6);
                String sub_t_code_tmp=t_code_temp.substring(7,15);
            
           
                if(subname.equals(sub_t_code_tmp) && p_u_code.equals(user_code)) 
                {

                    int code=Integer.parseInt(t_code_temp.substring(prefix.trim().length()));
                    if(code>temp)
                    {
                        temp=code;
                    }
                }
            }
            rs.close();
            stmt.close();
            con.commit();
         }catch(Exception e){
                    if(con != null){
                        con.rollback();
                    }
                    throw e;
         }
         finally
         {
             con.setAutoCommit(true);
         }
        con.close();
//        if(status)
//        temp=0;
        next_id=prefix.trim()+(++temp);
      //  System.out.println(next_id);
        return next_id;
}
public synchronized String getNextIdDateCodeWise(String tableName,String prefix,String columnName)throws Exception
{
  //  System.out.println(prefix);
     int temp=0;
       Connection con = null;
        try{
             con=new DBConnection().getDBConnection();
             con.setAutoCommit(false);
            Statement stmt=con.createStatement(); // getCon() is the return type of created Connection.
            ResultSet rs=stmt.executeQuery("select "+columnName.trim()+" from "+tableName.trim()+" order by '"+columnName.trim()+"'" );

                String name=prefix.trim();
                String pdate=name.substring(1,9);
                String ucode = name.substring(9,14);



            while(rs.next())
            {
                String t_code_temp=rs.getString(1).trim();
                String u_code=t_code_temp.substring(9,14);
                String p_date=t_code_temp.substring(1,9);


                if(pdate.equals(p_date) && ucode.equals(u_code))
                {

                    int code=Integer.parseInt(t_code_temp.substring(prefix.trim().length()));
                    if(code>temp)
                    {
                        temp=code;
                    }
                }
            }
            rs.close();
            stmt.close();
            con.commit();
         }catch(Exception e){
                    if(con != null){
                        con.rollback();
                    }
                    throw e;
         }
         finally
         {
             con.setAutoCommit(true);
         }
        con.close();
//        if(status)
//        temp=0;
        next_id=prefix.trim()+(++temp);
      //  System.out.println(next_id);
        return next_id;
}

//general Id Generation
public String getNextId(String tableName,String prefix,String columnName)throws Exception
{
     //create the connection of database.
   // System.out.println(prefix);
     int temp=0;
       Connection con = null;
        try{
             con=new DBConnection().getDBConnection();
             con.setAutoCommit(false);
            Statement stmt=con.createStatement(); // getCon() is the return type of created Connection.
            ResultSet rs=stmt.executeQuery("select "+columnName.trim()+" from "+tableName.trim()+" order by '"+columnName.trim()+"'" );
            while(rs.next())
            {
                String t_code_temp=rs.getString(1).trim();
                int code=Integer.parseInt(t_code_temp.substring(prefix.trim().length()));
                if(code>temp)
                {
                    temp=code;
                }
            }
            rs.close();
            stmt.close();
            con.commit();
         }catch(Exception e){
                    if(con != null){
                        con.rollback();
                    }
                    throw e;
         }
         finally
         {
             con.setAutoCommit(true);
         }
        con.close();
        next_id=prefix.trim()+(++temp);
      //  System.out.println(next_id);
        return next_id;
}

//Formated Id Generation
public String getFormatedNextId(String tableName,String prefix,String columnName, int padlen)throws Exception
{
        //create the connection of database.
        int temp=0;
        Connection con = null;
        try{
             con=new DBConnection().getDBConnection();
             con.setAutoCommit(false);
            Statement stmt=con.createStatement(); // getCon() is the return type of created Connection.
            ResultSet rs=stmt.executeQuery("select "+columnName.trim()+" from "+tableName.trim()+" order by '"+columnName.trim()+"'");
            while(rs.next())
            {
                String t_code_temp=rs.getString(1).trim();
                int code=Integer.parseInt(t_code_temp.substring(prefix.trim().length()));

                if(code>temp)
                {
                    temp=code;
                }
            }

            ++temp;

            String new_no = ""+temp;
            for(int i = new_no.trim().length();i<padlen;i++)
            {
                prefix =prefix.trim()+"0";
            }
            rs.close();
            stmt.close();
            con.commit();
         }catch(Exception e){
                    if(con != null){
                        con.rollback();
                    }
                    throw e;
         }
         finally
         {
             con.setAutoCommit(true);
         }

        con.close();
        next_id=prefix.trim()+(temp);

       // System.out.println("New ID............."+next_id);
        
        return next_id;
}

public String getDateFormatedNextId(String tableName,String prefix,String columnName, int padlen)throws Exception
{
//create the connection of database.
    int temp=0;
    Connection con = null;
    try{
        con=new DBConnection().getDBConnection();
        con.setAutoCommit(false);
        Statement stmt=con.createStatement(); // getCon() is the return type of created Connection.
        ResultSet rs=stmt.executeQuery("Select max(cast(SUBSTRING_INDEX("+columnName.trim()+",'/',-1) as unsigned int )) from "+tableName.trim()+" where SUBSTRING_INDEX("+columnName.trim()+",'/',1)='"+prefix.trim()+"'");

        if(rs.next())
        {
            String str = rs.getString(1);
            if(str != null)
            {
                temp=Integer.parseInt(str);
            }
        }

        ++temp;

        String new_no = ""+temp;
        prefix +="_";
        for(int i = new_no.trim().length();i<padlen;i++)
        {
            prefix =prefix.trim()+"0";
        }
        rs.close();
        stmt.close();
        con.commit();
    }catch(Exception e){
        if(con != null){
        con.rollback();
        }
        throw e;
    }
    finally
    {
    con.setAutoCommit(true);
    }

    con.close();
    next_id=prefix.trim()+(temp);

return next_id;
}


public String getNextId(String tableName,String prefix,String columnName, int padlen)throws Exception
{
        //create the connection of database.
        int temp=0;
        Connection con = null;
        try{
             con=new DBConnection().getDBConnection();
             con.setAutoCommit(false);
            Statement stmt=con.createStatement(); // getCon() is the return type of created Connection.
            ResultSet rs=stmt.executeQuery("select "+columnName.trim()+" from "+tableName.trim()+" order by '"+columnName.trim()+"'");
            while(rs.next())
            {
                String t_code_temp=rs.getString(1).trim();

                int code=Integer.parseInt(t_code_temp.substring(prefix.trim().length()));
                String pre_temp=t_code_temp.substring(0, prefix.trim().length());
                if(pre_temp.trim().equals(prefix.trim()))
                {
                    if(code>temp)
                    {
                        temp=code;
                    }
                }
               
            }

            ++temp;

            String new_no = ""+temp;
            for(int i = new_no.trim().length();i<padlen;i++)
            {
                prefix =prefix.trim()+"0";
            }
            rs.close();
            stmt.close();
            con.commit();
         }catch(Exception e){
                    if(con != null){
                        con.rollback();
                    }
                    throw e;
         }
         finally
         {
             con.setAutoCommit(true);
         }

        con.close();
        next_id=prefix.trim()+(temp);

       // System.out.println("New ID............."+next_id);

        return next_id;
}

}
