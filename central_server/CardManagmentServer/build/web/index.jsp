<%-- 
    Document   : index
    Created on : 4 Nov, 2017, 1:07:17 PM
    Author     : Maitreya SPML
--%>

<%@page import="java.sql.SQLException"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.Connection"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Hello World!</h1>
        <%
    out.println("-------- MySQL JDBC Connection Testing ------------");

    try {
      Class.forName("com.mysql.jdbc.Driver");
    } catch (ClassNotFoundException e) {
      out.println("Where is your MySQL JDBC Driver?");
      e.printStackTrace();
      return;
    }

    out.println("MySQL JDBC Driver Registered!");
    Connection connection = null;

    try {
      connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/server_user_primarydb","root", "");

    } catch (SQLException e) {
      out.println("Connection Failed! Check output console");
      e.printStackTrace();
      return;
    }

    if (connection != null) {
      out.println("You made it, take control your database now!");
    } else {
      out.println("Failed to make connection!");
    }
  %>
    </body>
</html>
