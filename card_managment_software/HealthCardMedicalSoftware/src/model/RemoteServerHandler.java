/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

/**
 *
 * @author maitreya maity VAIO
 */
public class RemoteServerHandler {

    private HttpURLConnection con = null;

    private HttpURLConnection getServletConnection(String RUrl)
            throws MalformedURLException, IOException {

        URL urlServlet = new URL("http://127.0.0.1:8080/CardManagmentServer/" + RUrl);
        con = (HttpURLConnection) urlServlet.openConnection();

        con.setDoInput(true);
        con.setDoOutput(true);
        con.setUseCaches(false);
        con.setRequestProperty("Content-Type", "application/x-java-serialized-object");
        con.setConnectTimeout(20000);

        return con;
    }

    public Vector getRemoteData(String servlet, Vector request) {
        Vector response = new Vector();
        try {
            HttpURLConnection Hservletconn = getServletConnection(servlet);
            OutputStream outstream = Hservletconn.getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(outstream);
            oos.writeObject(request);
            oos.flush();
            oos.close();
            InputStream in = Hservletconn.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(in);
            response = (Vector) ois.readObject();
            ois.close();
            in.close();

        } catch (Exception e) {
            System.err.println(this.getClass().getName() + ": getRemoteData :" + e);
        } finally {
            return response;
        }
    }

}
