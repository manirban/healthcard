/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import ij.ImagePlus;
import java.io.FileInputStream;
import java.util.Vector;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author Maitreya SPML
 */
public class DataSecurityHandler {

    /**
     * @return the GH
     */
    public GeneralHandler getGH() {
        return GH;
    }

    /**
     * @param GH the GH to set
     */
    public void setGH(GeneralHandler GH) {
        this.GH = GH;
    }
    private GeneralHandler GH;

    public boolean getCardAuthenticate(String cardNumber) {
        boolean stat = false;
        try {
            // ImagePlus fpsimg = new CardReaderHandler().getFingerPrint();
            String expertId = GH.getExpertID();
            String expertToken = GH.getExpertToken();

            FileInputStream fis = new FileInputStream(".\temp\finger.jpg");
            byte[] data = new byte[fis.available()];
            fis.read(data);
            fis.close();
            String fingerStr = Base64.encodeBase64URLSafeString(data);
            Vector request = new Vector();
            request.addElement("CARDHOLDER");
            request.addElement(expertId);
            request.add(new AESDataEncryption().getEncrypt(cardNumber, expertToken));
            request.add(new AESDataEncryption().getEncrypt(fingerStr, expertToken));

            Vector response_data = new RemoteServerHandler().getRemoteData("UserAuthServ", request);
            String statRes = response_data.get(0).toString();
            String cid = new AESDataEncryption().getDecrypt(response_data.get(1).toString(), expertToken);
            String cardToken = new AESDataEncryption().getDecrypt(response_data.get(2).toString(), expertToken);
            if (statRes.equals("TRUE")) {
                if (cid.equals(cardNumber)) {
                    GH.setHelathcardToken(cardToken);
                }
                stat = true;
            } else {
                stat = false;
            }
        } catch (Exception e) {
            System.err.println(this.getClass().getName() + ": getCardAuthenticate :" + e);
        } finally {
            return stat;
        }
    }
}
