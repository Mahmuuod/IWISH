/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oracledb;
/**
 *
 * @author osama
 */
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
//import oracle.jdbc.OracleDriver;
public class OracleDb {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            //Class.forName("oracle.jdbc.OracleDriver");
            Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "JavaProject", "123");
            System.out.println("success");
        } catch (SQLException ex) {
            Logger.getLogger(OracleDb.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
