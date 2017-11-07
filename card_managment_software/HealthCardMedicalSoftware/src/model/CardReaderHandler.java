/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import com.fazecast.jSerialComm.SerialPort;

/**
 *
 * @author Maitreya SPML
 */
public class CardReaderHandler {

    private static final String PORTNAME = "COM4";
    private SerialPort comPort;

    public boolean checkCardReader() {
        boolean stat = false;
        try {
             comPort = SerialPort.getCommPort(PORTNAME);
             if(comPort.isOpen()) comPort.closePort();
             comPort.openPort();
             comPort.closePort();
             stat = true;
        } catch (Exception e) {
            System.err.println(this.getClass().getName() + ": connectHealthCardAction :" + e);
        } finally {
            return stat;
        }
    }  
     
    

}
