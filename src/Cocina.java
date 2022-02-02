
import java.awt.Image;
import java.awt.Toolkit;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.util.Date;
import java.util.Properties;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.showMessageDialog;
import javax.swing.table.DefaultTableModel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author GONZALO
 */

public class Cocina extends javax.swing.JFrame {
    DateFormat hourFormat;
    DateFormat dateFormat;
    Date date = new Date();
    private ResultSet resultado;
   private PreparedStatement enunciado;
   private Connection conexion;
   
    /**
     * Creates new form Cocina
     */
    public Cocina() {
        initComponents();
        this.setExtendedState(MAXIMIZED_BOTH);
        this.setTitle("Cocina");
        conectar();
        dtm= (DefaultTableModel) tablapedidos.getModel();
        dtm.setRowCount(0);
        Actualizartabla();
        Actualizar act=new Actualizar();
        new  Thread((Runnable)act).start();        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */int c=0;
    public void Actualizartabla()
    {
    
        Object []O=new Object[6];   
        dtm.setNumRows(0);
       int idpedido=0;
        try{
          java.sql.Date date2 = new java.sql.Date(date.getTime()); 
         enunciado = conexion.prepareStatement("select * from venta,Cliente,productos,pedidos where venta.Fecha='"+date2+"' and Estado=? and Cliente_idCliente=cliente.idCliente and idPedidos=Pedidos_idPedidos and Productos_idProductos=Productos.idProducto and Productos.idProducto<50 and TipoProducto = 'C'");
        
         enunciado.setString(1,"En Preparacion...");
         resultado = enunciado.executeQuery();
         while(resultado.next()){
             
             O[0]=resultado.getInt("Pedidos_idPedidos");
             
             if(idpedido!=Integer.parseInt(O[0].toString()))
             {
               Object A[]=new Object[5];
               A[0]="";
               A[1]="";
               A[2]="";
               A[3]="";
               A[4]="";
               dtm.addRow(A);
               
               dtm.addRow(A);
               idpedido=Integer.parseInt(O[0].toString());
             }
             else
             {
                 
             }
             O[1]=resultado.getString("Lugar");
             O[2]=resultado.getString("NombreCliente");
             O[4]=resultado.getString("Nombre");       
             O[5]=resultado.getString("Descripcion");
             O[3]=resultado.getString("Cantidad");
             
             
             dtm.addRow(O);
         }
         
      }catch(Exception e){
         e.printStackTrace();
      }
        
    
    }
    
    ConexionBD con = new ConexionBD();
    
    private void conectar(){
        conexion = con.conectar();
    }
    class Actualizar implements Runnable {
        public Actualizar(){      
        }
        public void run(){
            while(true){    
                try{Thread.sleep(8000);}catch(Exception ex){}
                Actualizartabla();
            }      
        }  
    }
    
    @Override
    public Image getIconImage() {
        Image retValue = Toolkit.getDefaultToolkit().
        getImage(ClassLoader.getSystemResource("Recursos/icono.png"));
        return retValue;
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tablapedidos = new javax.swing.JTable();

        setIconImage(getIconImage());

        tablapedidos.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        tablapedidos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "IdPedido", "Lugar", "Cliente", "Cantidad", "Producto", "Descripcion"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, false, true, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tablapedidos);
        if (tablapedidos.getColumnModel().getColumnCount() > 0) {
            tablapedidos.getColumnModel().getColumn(0).setMinWidth(100);
            tablapedidos.getColumnModel().getColumn(0).setMaxWidth(110);
            tablapedidos.getColumnModel().getColumn(1).setMinWidth(200);
            tablapedidos.getColumnModel().getColumn(1).setMaxWidth(200);
            tablapedidos.getColumnModel().getColumn(2).setMinWidth(150);
            tablapedidos.getColumnModel().getColumn(2).setMaxWidth(200);
            tablapedidos.getColumnModel().getColumn(3).setMinWidth(70);
            tablapedidos.getColumnModel().getColumn(3).setMaxWidth(70);
            tablapedidos.getColumnModel().getColumn(4).setMinWidth(200);
            tablapedidos.getColumnModel().getColumn(4).setMaxWidth(250);
            tablapedidos.getColumnModel().getColumn(5).setMinWidth(300);
            tablapedidos.getColumnModel().getColumn(5).setMaxWidth(800);
        }

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1366, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 720, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Cocina.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Cocina.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Cocina.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Cocina.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Cocina().setVisible(true);
            }
        });
    }
DefaultTableModel dtm;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tablapedidos;
    // End of variables declaration//GEN-END:variables
}