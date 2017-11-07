/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.user;

import dbmodel.ExceptionLogger;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.AESDataEncryption;
import model.CardTokenChecker;
import model.ExpertCheckRelated;
import model.UserLoginChecker;

/**
 *
 * @author Maitreya SPML
 */
public class UserAuthServ extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/x-java-serialized-object");
        Vector response_data = new Vector();
        try {
            InputStream in = request.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(in);
            Vector v = (Vector) ois.readObject();
            ois.close();
            in.close();
            String uType = v.get(0).toString();
            System.out.println(uType);
            if (uType.equals("CARDHOLDER")) {
                String expertid = v.get(1).toString();
                if (new ExpertCheckRelated().checkExpert(expertid)) {
                    String experttoken = new ExpertCheckRelated().getExpertToken(expertid);
                    String cardnumber = new AESDataEncryption().getDecrypt(v.get(2).toString(), experttoken);
                    String fingerprint = new AESDataEncryption().getDecrypt(v.get(3).toString(), experttoken);
                    CardTokenChecker ctc = new CardTokenChecker();
                    boolean stat = ctc.checkCard(expertid, cardnumber, fingerprint);
                    if (stat) {
                        response_data.add("TRUE");
                        response_data.add(new AESDataEncryption().getEncrypt(cardnumber, experttoken));
                        response_data.add(new AESDataEncryption().getEncrypt(ctc.getSecurityKey(), experttoken));
                    } else {
                        response_data.add("FALSE");
                    }
                } else {
                    response_data.add("FALSE");
                }

            } else {
                String loginid = v.get(1).toString();
                String password = v.get(2).toString();
                System.out.println(loginid + password);
                UserLoginChecker ulc = new UserLoginChecker();
                boolean stat = ulc.checkExpertLogin(loginid, password, uType);
                System.out.println(stat);
                if (stat) {
                    response_data.add("TRUE");
                    response_data.add(ulc.getExpertID());
                    response_data.add(new ExpertCheckRelated().getExpertToken(ulc.getExpertID()));
                    response_data.add(new ExpertCheckRelated().getExpertRegNumber(ulc.getExpertID()));
                    response_data.add(new ExpertCheckRelated().getExpertMedCenter(ulc.getExpertID()));
                    response_data.add(new ExpertCheckRelated().getExpertProfilePhoto(ulc.getExpertID()));
                    response_data.add(new ExpertCheckRelated().getExpertName(ulc.getExpertID()));
                } else {
                    response_data.add("FALSE");
                }
            }
        } catch (Exception e) {
            ExceptionLogger.writeToDB(this.getClass().getName() + ": " + e);
        } finally {
            ServletOutputStream out = response.getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(out);
            oos.writeObject(response_data);
            oos.flush();
            oos.close();
            out.close();

        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
