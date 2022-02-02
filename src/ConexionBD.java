
import confidencial.Files;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author GONZALO
 */
public class ConexionBD {
    
    public Connection conectar(){
        Files r = new Files();
        Connection cn = null;
        try {
           String computerName = InetAddress.getLocalHost().getHostName();
           String user = "sa";
           String pass = r.leer();           
           //System.out.println(computerName + "\n" + user + " y "+pass);
           Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
           //String connectionURL = "jdbc:sqlserver://" + "192.168.1.2" + ":1433;databaseName=alitasexpress;user=" + user + ";password=" + pass +";";
           String connectionURL = "jdbc:sqlserver://" + computerName + ":1433;databaseName=alitasexpress;user=" + user + ";password=" + pass +";";
           cn = (Connection) DriverManager.getConnection(connectionURL);
           //System.out.println("Conectado.");
       } catch (ClassNotFoundException ex) {
            System.out.println("");
       } catch (SQLException ex) {
           javax.swing.JOptionPane.showMessageDialog(null, "Error al cargar la base de datos");
       } catch (UnknownHostException ex) {
            
       }
       return cn;
    }    
}
