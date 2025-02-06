/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projectserver;

import DAL.UserInfo;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.*;
import org.json.*;
import DBA.DBA;
import java.util.ArrayList;
import projectserver.*;

/**
 *
 * @author osama
 */
class HandleClients extends Thread {

    HandleRequests req = new HandleRequests();
    Utilities utility = new Utilities();
    PrintStream ps;
    DataInputStream dis;
    static Vector<HandleClients> usrs = new Vector<HandleClients>();

    HandleClients(Socket s) {
        try {
            ps = new PrintStream(s.getOutputStream());
            dis = new DataInputStream(s.getInputStream());
            usrs.add(this);
            start();
        } catch (IOException ex) {
            Logger.getLogger(HandleClients.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {

        while (true) {
            try {
                String msg = dis.readLine();
                System.out.println(msg);
                if (msg != null) {
                    JSONObject request = new JSONObject(msg);
                    String command = request.getString("header");
                    JSONObject respond;
                    switch (command) {
                        case "sign in":
                            String Username = request.getString("Username");
                            String Password = request.getString("Password");

                            if (req.signIn(Username, Password)) {
                                String user = utility.getUsrData(Username, Password);
                                respond = new JSONObject(user);
                                respond.put("header", "user exists");
                                System.out.println(respond.toString());
                                ps.println(respond.toString());
                                respond.clear();
                            } else {
                                respond = new JSONObject();
                                respond.put("header", "not exists");
                                System.out.println(respond.toString());
                                ps.println(respond.toString());
                                respond.clear();
                            }
                            break;
                    }

                } else {
                    usrs.remove(this);
                    stop();
                    dis.close();
                }
            } catch (SocketException sc) {
                try {
                    usrs.remove(this);
                    stop();
                    dis.close();
                } catch (IOException ex) {
                    Logger.getLogger(HandleClients.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (IOException ex) {
                Logger.getLogger(HandleClients.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

}

public class ProjectServer {

    ProjectServer() {
        try {
            ServerSocket ss = new ServerSocket(5005);
            while (true) {
                Socket s = ss.accept();
                new HandleClients(s);

            }
        } catch (IOException ex) {
            Logger.getLogger(ProjectServer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        new ProjectServer();
        /*Utilities u=new Utilities();
        System.out.println(u.checkGetUsrData(888));*/
    }

}
