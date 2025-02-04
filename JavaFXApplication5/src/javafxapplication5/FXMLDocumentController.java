/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication5;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Application;
import static javafx.application.Platform.exit;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import org.json.*;
/**
 *
 * @author osama
 */
public class FXMLDocumentController extends Thread implements Initializable {

    @FXML
    private Label label;
    @FXML
    private Button button;

    @FXML
    private void handleButtonAction(ActionEvent event) {
        // test for show friend list
        JSONObject data=new JSONObject();
        data.put("header", "show friendlist");
        data.put("user_id", 1);
        
        ps.println(data.toString());  
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {
            s = new Socket("localhost", 5005);
            dis = new DataInputStream(s.getInputStream());
            ps = new PrintStream(s.getOutputStream());
            new Thread(new Runnable(){ public void run(){
                try {
                    while(true)
                    {
                     String msg = dis.readLine();
                     System.out.println(msg);
                    }

                }
                catch (IOException ex) {
                    Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }}).start();

        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    PrintStream ps;
    DataInputStream dis;
    Socket s;
}
