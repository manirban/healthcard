/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import ij.IJ;
import ij.ImagePlus;
import ij.process.ByteProcessor;
import ij.process.ImageProcessor;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.imageio.ImageIO;
import model.CardReaderHandler;
import model.GeneralHandler;
import model.CardReaderHandler;
import model.DataFormattingHandler;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.ArrayUtils;

/**
 *
 * @author maitr
 */
public class DoctorHomeFrameController implements Initializable {

    private GeneralHandler GH;
    private static final String PORTNAME = "COM4";
    private SerialPort comPort;
    private String accessType;
    private String fileName;
    private int total;
    private boolean readCard = true, cardProc;
    private InputStream inputStream;
    private BufferedReader inputBuf;
    private OutputStream outputStream;
    private FileOutputStream recFos;
    private int readFileSize;
    private byte[] fingerPrintByte = {};

    @FXML
    private ImageView docProfilePhoto_imgv;
    @FXML
    private Label docName_lab;
    @FXML
    private Label docMedCenter_lab;
    @FXML
    private ImageView doc_progress_imgv;
    @FXML
    private ImageView patProfilePhoto_imgv;
    @FXML
    private Label patName_lab;
    @FXML
    private Label patCardNumber_lab;
    @FXML
    private Label patYOB_lab;
    @FXML
    private Label patGender_lab;
    @FXML
    private Label dochomeproc_lab;
    @FXML
    private TextField patName_txt;
    @FXML
    private TextField patCardNumber_txt;
    @FXML
    private TextField patYOB_txt;
    @FXML
    private TextField patGender_txt;

    // @FXML
    //  public TableView medTabList;
    @FXML
    private void saveAction(ActionEvent event) {
        // doctorname_lab.setText(GH.getExpertID());
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        doc_progress_imgv.setVisible(false);
        if (new CardReaderHandler().checkCardReader()) {
            dochomeproc_lab.setText("Card Reader Found");
            initSerialPortHandler();
        } else {
            dochomeproc_lab.setText("Card Reader Not Found");
        }
        // System.out.println(GH.getExpertID());
        // TODO       

    }

    private void setPatHome() throws IOException {
        ArrayList<String> data = new DataFormattingHandler().getPatientDemographic();
        patName_txt.setText(data.get(1) + " " + data.get(2));
        patCardNumber_txt.setText(data.get(0));
        patYOB_txt.setText(data.get(3));
        patGender_txt.setText(data.get(4));
        byte[] profilePic = Base64.decodeBase64(data.get(8));
        ByteArrayInputStream ba = new ByteArrayInputStream(profilePic);
        InputStream is = new BufferedInputStream(ba);
        BufferedImage pic = ImageIO.read(is);
        Image image = SwingFXUtils.toFXImage(pic, null);
        patProfilePhoto_imgv.setImage(image);
        GH.setHealthcardNumber(data.get(0));
    }

    private void initSerialPortHandler() {
        comPort = SerialPort.getCommPort(PORTNAME);
        comPort.openPort();
        comPort.setBaudRate(115200);
        inputStream = comPort.getInputStream();
        inputBuf = new BufferedReader(new InputStreamReader(inputStream));
        outputStream = comPort.getOutputStream();
        comPort.addDataListener(new SerialPortDataListener() {
            @Override
            public int getListeningEvents() {
                return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
            }

            @Override
            public void serialEvent(SerialPortEvent event) {
                if (event.getEventType() == SerialPort.LISTENING_EVENT_DATA_AVAILABLE) {
                    if (accessType.equals("RF")) {
                        if (readCard) {
                            try {
                                String inputLine = inputBuf.readLine();
                                System.out.println(inputLine);
                                if (inputLine.contains("DBFILE")) {
                                    File file = new File("./temp/" + fileName);
                                    recFos = new FileOutputStream(file);
                                    String s[] = inputLine.split(":");
                                    readFileSize = Integer.parseInt(s[1]);
                                    readCard = false;
                                }
                            } catch (Exception ex) {
                                System.out.println(ex.toString());
                            }
                        } else {
                            try {
                                byte[] newData = new byte[comPort.bytesAvailable()];
                                int numRead = comPort.readBytes(newData, newData.length);
                                recFos.write(newData);
                                readFileSize = readFileSize - numRead;
                                if (readFileSize <= 0) {
                                    recFos.flush();
                                    recFos.close();
                                    readCard = true;
                                    System.out.println("File Received");
                                    invisibleProgressbarTask();
                                    setPatHome();
                                    setCardConnectionTrueTask();
                                }

                            } catch (Exception ex) {
                                System.out.println(ex.toString());
                            }
                        }
                    } else if (accessType.equals("FP")) {
                        byte[] newData = new byte[comPort.bytesAvailable()];
                        int numRead = comPort.readBytes(newData, newData.length);
                        fingerPrintByte = ArrayUtils.addAll(fingerPrintByte, newData);
                        total = total + numRead;
                        if (total == 52122) {
                            fingerPrintByte = ArrayUtils.remove(fingerPrintByte, fingerPrintByte.length - 1);
                            fingerPrintByte = ArrayUtils.remove(fingerPrintByte, fingerPrintByte.length - 1);
                            fingerPrintByte = ArrayUtils.remove(fingerPrintByte, 0);
                            fingerPrintByte = ArrayUtils.remove(fingerPrintByte, 0);
                            fingerPrintByte = ArrayUtils.remove(fingerPrintByte, 0);
                            fingerPrintByte = ArrayUtils.remove(fingerPrintByte, 0);
                            ImageProcessor ip = new ByteProcessor(258, 202);
                            int c = 0;
                            int imagepix[][] = new int[258][202];
                            for (int i = 0; i < 258; i++) {
                                for (int j = 0; j < 202; j++) {
                                    ip.putPixel(i, j, (int) fingerPrintByte[c++]);
                                }
                            }
                            ImagePlus fpsimg = new ImagePlus("FingerPrint", ip);
                            IJ.save(fpsimg, "./temp/" + fileName);
                        }
                    } else {

                    }
                } else {
                    return;
                }
            }
        });
    }

    public void initParameters() {
        total = 0;
        readFileSize = 0;
        fingerPrintByte = null;
        cardProc = true;
    }

    public void setDocHome(String docName, String docCenter, String photo) throws IOException {
        docName_lab.setText("Dr. " + docName);
        docMedCenter_lab.setText(docCenter);
        byte[] profilePic = Base64.decodeBase64(photo);
        ByteArrayInputStream ba = new ByteArrayInputStream(profilePic);
        InputStream is = new BufferedInputStream(ba);
        BufferedImage pic = ImageIO.read(is);
        Image image = SwingFXUtils.toFXImage(pic, null);
        docProfilePhoto_imgv.setImage(image);
    }

    @FXML
    private void connectHealthCardAction(ActionEvent event) {
        try {
            visibleProgressbarTask();
            connectHealthCardTask();
        } catch (Exception e) {
            System.err.println(this.getClass().getName() + ": connectHealthCardAction :" + e);
        }
    }

    @FXML
    private void authenticateHealthCardAction(ActionEvent event) {
        authenticateFingerPrintTask();
    }

    public void authenticateFingerPrintTask() {
        try {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    initParameters();
                    String command = "FP";
                    accessType = "FP";
                    fileName = "finger.jpg";
                    comPort.writeBytes(command.getBytes(), command.length());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    public void connectHealthCardTask() {
        try {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    initParameters();
                    String command = "RFTBL_DEMOGRAP.db";
                    accessType = "RF";
                    fileName = "TBL_DEMOGRAP.db";
                    comPort.writeBytes(command.getBytes(), command.length());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void visibleProgressbarTask() {
        try {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    doc_progress_imgv.setVisible(true);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void invisibleProgressbarTask() {
        try {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    doc_progress_imgv.setVisible(false);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    public void setCardConnectionTrueTask() {
        try {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    dochomeproc_lab.setText("Health card connected");
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

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
}
