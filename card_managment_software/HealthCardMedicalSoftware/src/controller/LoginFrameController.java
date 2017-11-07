/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import model.AuthenticationHandler;
import model.GeneralHandler;

/**
 * FXML Controller class
 *
 * @author Maitreya SPML
 */
public class LoginFrameController implements Initializable {

    @FXML
    private AnchorPane loginPane;
    @FXML
    private TextField loginid_txt;
    @FXML
    private PasswordField password_pwd;
    @FXML
    private ComboBox accessrole_com;
    @FXML
    private Label loginstatus_lab;
    @FXML
    private ImageView login_progress_imgv;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        accessrole_com.getItems().addAll(
                "DOCTOR",
                "LABTECHNICIAN",
                "PHARMACIST"
        );
        accessrole_com.setEditable(false);
        accessrole_com.getSelectionModel().selectFirst();
        login_progress_imgv.setVisible(false);
    }

    @FXML
    private void userloginAction(ActionEvent event) throws Exception {
        new Thread(tastProgressVis).start();       
        String loginid = loginid_txt.getText().trim();
        String password = password_pwd.getText().trim();
        String accessrole = accessrole_com.getValue().toString();
        if (!loginid.isEmpty() || !password.isEmpty()) {          
            AuthenticationHandler authHandler = new AuthenticationHandler();
            boolean stat = authHandler.doLogin(loginid, password, accessrole);
            if (stat) {
                GeneralHandler GH = new GeneralHandler();
                boolean stat2 = authHandler.loginCheck(GH);
                if (stat2) {
                    try {
                        loginstatus_lab.setText("Login successful!");
                        FXMLLoader fxml = new FXMLLoader();
                        fxml.setLocation(getClass().getResource("/view/DoctorHomeFrameDocument.fxml"));
                        AnchorPane pane = fxml.load();                        
                        DoctorHomeFrameController controller = fxml.getController();
                        controller.setGH(GH);  
                        controller.setDocHome(GH.getExpertName(), GH.getExpertMedCenter(), GH.getExpertProfilePhoto());
                        loginPane.getChildren().setAll(pane);
                    } catch (IOException ex) {
                        Logger.getLogger(LoginFrameController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    loginstatus_lab.setText("Login failed! Incorrect credential.");
                }
            } else {
                loginstatus_lab.setText("Server communication error!");
            }            
        } else {
            loginstatus_lab.setText("Invalid information.");
        }
        new Thread(tastProgressIvis).start();
    }

    Task<Void> tastProgressVis = new Task<Void>() {
        @Override
        protected Void call() throws Exception {

            login_progress_imgv.setVisible(true);
            return null;
        }

        @Override

        protected void succeeded() {
            super.succeeded();
            updateMessage("Done!");
        }

        @Override
        protected void cancelled() {
            super.cancelled();
            updateMessage("Cancelled!");
        }

        @Override
        protected void failed() {
            super.failed();
            updateMessage("Failed!");
        }
    };
    
Task<Void> tastProgressIvis = new Task<Void>() {
        @Override
        protected Void call() throws Exception {

            login_progress_imgv.setVisible(false);
            return null;
        }

        @Override

        protected void succeeded() {
            super.succeeded();
            updateMessage("Done!");
        }

        @Override
        protected void cancelled() {
            super.cancelled();
            updateMessage("Cancelled!");
        }

        @Override
        protected void failed() {
            super.failed();
            updateMessage("Failed!");
        }
    };

}
