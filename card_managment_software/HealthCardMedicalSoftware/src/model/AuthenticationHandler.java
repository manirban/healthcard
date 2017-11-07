/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.Vector;
import javax.xml.ws.Response;

/**
 *
 * @author Maitreya SPML
 */
public class AuthenticationHandler {

    private Vector response_data;

    public boolean doLogin(String loginid, String passwrod, String accessrole) {
        boolean stat = false;
        try {
            Vector request = new Vector();
            request.add(accessrole);
            request.add(loginid);
            request.add(passwrod);
            response_data = new RemoteServerHandler().getRemoteData("UserAuthServ", request);
            System.out.println(response_data.size());
         //   System.out.println(response_data.get(1).toString());
          //  System.out.println(response_data.get(2).toString());
            stat = true;
        } catch (Exception e) {
            System.err.println(this.getClass().getName() + ":doLogin: " + e);
        } finally {
            return stat;
        }
    }

    public boolean loginCheck(GeneralHandler GH) {
        boolean stat = false;
        try {
            String loginstat = response_data.get(0).toString();
            if (loginstat.equals("TRUE")) {
                GH.setExpertID(response_data.get(1).toString());
                GH.setExpertToken(response_data.get(2).toString());
                GH.setExpertRegNumber(response_data.get(3).toString());
                GH.setExpertMedCenter(response_data.get(4).toString());
                GH.setExpertProfilePhoto(response_data.get(5).toString());  
                GH.setExpertName(response_data.get(6).toString());   
                stat = true;
            } else {
                stat = false;
            }
        } catch (Exception e) {
            System.err.println(this.getClass().getName() + ":loginCheck: " + e);
        } finally {
            return stat;
        }

    }

}
