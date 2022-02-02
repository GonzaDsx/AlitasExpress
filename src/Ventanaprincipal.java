import confidencial.Files;
import de.javasoft.plaf.synthetica.SyntheticaPlainLookAndFeel;
import java.sql.Connection;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import static javax.swing.JOptionPane.*;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import javax.swing.UIManager;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author GONZALO
 */
public class Ventanaprincipal extends javax.swing.JFrame {
   DateFormat hourFormat;
    DateFormat dateFormat;
    Date date = new Date();
    
   
   int tamaño;
   Boolean bandera=false;
   Boolean EsCliente=false;
   
   ArrayList<Producto> listaproductos =new ArrayList<Producto>();
    ArrayList<Salsa> listasalsa =new ArrayList<Salsa>();
    ArrayList<Combo> listacombo =new ArrayList<Combo>();
   JButton[] arrBtn,arrBtn2;
   int NumeroCliente;
   
   // 
    /**
     * Creates new form Ventanaprincipal
     */
   
    public Ventanaprincipal() {              
        initComponents();
        conectar();
        inicializarcomponentes2();      
        bandera=true;
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        txtfecha.setText(dateFormat.format(date));
        this.setExtendedState(MAXIMIZED_BOTH);                           
    }
    
    Files f = new Files();
    
    public void passVerify(){        
        if(!f.leerBand()){
            String pass = showInputDialog("Contraseña de SQL Server");
            f.escribir("1-"+pass);
            conectar();
        }        
        else{
            System.out.println("Error");
        }
    }
    
    public void inicializarcomponentes2()
    {
        dtm= (DefaultTableModel) Tablapedido.getModel();        
        dtm2= (DefaultTableModel) tblclientes.getModel();
        panelcombos.removeAll();
        panelcombos.repaint(); 
        panelbotones.removeAll();
        panelbotones.repaint();
        listaproductos.removeAll(listaproductos);
        listacombo.removeAll(listacombo);
        listasalsa.removeAll(listasalsa);        
        cargarsalsas();
        llenarMesas();        
        Bloquearcampos();
        panelsalsas.setVisible(false);         
    }
    
    public void Bloquearcampos()
    {
        txtcalle.setEnabled(false);
        txtcalles.setEnabled(false);
        txttelefono.setEnabled(false);
        txtdescripcion.setEnabled(false);
    }
    
    public void Habilitarcampos()
    {
        txtcalle.setEnabled(true);
        txtcalles.setEnabled(true);
        txttelefono.setEnabled(true);
        txtdescripcion.setEnabled(true);
    }
    
    public void cargarcombos(){
        panelcombos.removeAll();
        panelcombos.repaint(); 
        listacombo.removeAll(listacombo);
        ResultSet resultado;
        PreparedStatement enunciado;
        int resul;
        String nombre,productos;
        float precio;
        int idDescuento;
        try{
            enunciado = cn.prepareStatement("select * from Combos ");
            resultado = enunciado.executeQuery();
            while(resultado.next()){
              productos=resultado.getString("Productos");
              nombre=resultado.getString("Nombre");
              precio=resultado.getFloat("Precio");
              idDescuento=resultado.getInt("idDescuento");
              Combo c=new Combo(nombre,productos,precio,idDescuento);
              listacombo.add(c);
            }
        }catch(Exception e){
           e.printStackTrace();
        }
         arrBtn2 = new JButton[listacombo.size()];
         cargarbotonescombos();
    }
    
    public void cargarproductos()
    {
        listaproductos.removeAll(listaproductos);
        panelbotones.removeAll();
        panelbotones.repaint();
        ResultSet resultado;
        PreparedStatement enunciado;        
        int id;
        String nombre;
        float precio, precioDom;
        try{
            enunciado = cn.prepareStatement("select * from Productos ");        
            resultado = enunciado.executeQuery();
            while(resultado.next()){
                id=resultado.getInt("idProducto");
                nombre=resultado.getString("Nombre");
                precio=resultado.getFloat("Precio");
                precioDom=resultado.getFloat("PrecioDom");
                //if(id<50){
                    Producto p=new Producto(id,nombre,precio,precioDom);
                    listaproductos.add(p);
                //}
            }         
        }catch(Exception e){
            e.printStackTrace();
        }
        tamaño=listaproductos.size();
        arrBtn = new JButton[tamaño];
        mostrarBot();                
    }
    
    public void cargarbotonescombos(){
        panelcombos.setLayout(new GridLayout(1, 4, 1, 1));
        for( int i=0;i<listacombo.size();i++){//ciclo para crear, añadir, establecer propiedades a los botones
            arrBtn2[i] = new JButton(listacombo.get(i).getNombre()+"    "+listacombo.get(i).getProductos()+"/"+listacombo.get(i).getidDesceunto());
            
            arrBtn2[i].setBackground(new java.awt.Color(204, 204, 255));
            
           arrBtn2[i].setFont(new java.awt.Font("Arial", 1, 16));
           arrBtn2[i].addActionListener(new ActionListener()
         {
            @Override
            public void actionPerformed(ActionEvent evento){
                float suma=0;  
                JButton evt=(JButton)evento.getSource();
                String ar[]=evt.getText().split("/");
                for(int i=1;i<ar.length;i++)
                {     
                    agregaratabla(Integer.parseInt(ar[i]));             
                }         
            }
        }
         );
            panelcombos.add(arrBtn2[i]);
            arrBtn2[i].setMargin(new Insets(1, 1, 1, 1));  
        }//fin ciclo
    }
    public void cargarsalsas()
    {
        ResultSet resultado;
        PreparedStatement enunciado;        
        String nombre;
        try{
            enunciado = cn.prepareStatement("select * from Salsas ");       
            resultado = enunciado.executeQuery();
            while(resultado.next()){             
                nombre=resultado.getString("Nombre");          
                Salsa s=new Salsa(nombre);
                listasalsa.add(s);
            }         
        }catch(Exception e){                        
            f.escribir("0-0");
            passVerify();
        }
    }
    
    private void llenarMesas(){
        cmbMesas.removeAllItems();
        ResultSet rs;
        PreparedStatement enunciado;          
        try{
            enunciado = cn.prepareStatement("SELECT * FROM Inventario_Mesas");
            rs = enunciado.executeQuery();
            while(rs.next()){                          
                cmbMesas.addItem(rs.getString("NombreMesa"));
            }            
        }catch(Exception e){
             showMessageDialog(this,"Ocurrio un problema al cargar las mesas");             
        }   
    }
    
    public void mostrarBot(){//metodo donde se encontrara el jpanel que contiene los botones
        int tamañoScrol = listaproductos.size()/2;        
        panelbotones.setLayout(new GridLayout(tamañoScrol+1, 2, 2, 2));               
        for( int i=0;i<listaproductos.size();i++){//ciclo para crear, añadir, establecer propiedades a los botones
            arrBtn[i] = new JButton(listaproductos.get(i).getId()+"-"+listaproductos.get(i).getNombre());            
            arrBtn[i].setBackground(new java.awt.Color(204, 204, 255));
            arrBtn[i].setFont(new java.awt.Font("Arial", 1, 13));
            arrBtn[i].addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent evento){
                    JButton evt=(JButton)evento.getSource();
                    String ar[]=evt.getText().split("-");
                    agregaratabla(Integer.parseInt(ar[0]));
                }
            });
            panelbotones.add(arrBtn[i]);
            arrBtn[i].setMargin(new Insets(1, 1, 1, 1));            
        }//fin ciclo
    }
    
    public void calculacosto()
    {
        if(bandera==false){
           return; 
        }else{                        
            if(Tablapedido.getValueAt((Tablapedido.getRowCount())-1, 4) == null){                
                return;
            }
        }
        
        for(int i=0;i<dtm.getRowCount();i++){
            Float costo=(Float.parseFloat(dtm.getValueAt(i, 3).toString()))*(Float.parseFloat(dtm.getValueAt(i, 4).toString()));
            dtm.setValueAt(costo, i, 5);
        }
        calculatotal();
    }
    public void agregaratabla(int idproducto)
    {
        ResultSet resultado;
        PreparedStatement enunciado;        
        Object O[]=new Object[6];
        try{   
            enunciado = cn.prepareStatement("select * from Productos where idProducto=?");
            enunciado.setInt(1,idproducto);
            resultado = enunciado.executeQuery();
            while(resultado.next()){
                O[0]=idproducto;
                O[1]=resultado.getString("Nombre");
                O[2]="  ";
                O[3]=1;
                if(rbtn1.isSelected()){
                    O[4]=resultado.getFloat("Precio");
                }else if(rbtn2.isSelected()){
                    O[4]=resultado.getFloat("PrecioDom");
                }
                dtm.addRow(O); 
            } 
        }catch(Exception e){
            e.printStackTrace();
        }
        calculacosto();
    }
    public void calculatotal(){
        double suma=0;
        for(int i=0;i<dtm.getRowCount();i++){
            suma+=Float.parseFloat(dtm.getValueAt(i, 5).toString());
        }   
        txttotal.setText(suma+"");
    }
            
    Connection cn;
    ConexionBD con = new ConexionBD();
    private void conectar(){
       cn = con.conectar();
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Grupobtn = new javax.swing.ButtonGroup();
        jMenuItem2 = new javax.swing.JMenuItem();
        jPanel5 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtnombrecliente = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        rbtn1 = new javax.swing.JRadioButton();
        rbtn2 = new javax.swing.JRadioButton();
        jLabel9 = new javax.swing.JLabel();
        txtidcliente = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        btnverpedidos = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblclientes = new javax.swing.JTable();
        btnID1 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtcalle = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtcalles = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txttelefono = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtdescripcion = new javax.swing.JTextArea();
        btnID = new javax.swing.JButton();
        cmbMesas = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        Tablapedido = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();
        txttotal = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        txtfecha = new javax.swing.JLabel();
        btneliminar = new javax.swing.JButton();
        panelcombos = new javax.swing.JPanel();
        panelsalsas = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        panelbotones = new javax.swing.JPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        menuProductos = new javax.swing.JMenuItem();
        menuCombos = new javax.swing.JMenuItem();
        menuSalsas = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        Verventas = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        menuClientes = new javax.swing.JMenuItem();

        jMenuItem2.setText("jMenuItem2");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Alitas Express");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        setIconImage(getIconImage());
        setResizable(false);
        setSize(new java.awt.Dimension(1200, 720));
        addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                formFocusGained(evt);
            }
        });
        addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            public void windowGainedFocus(java.awt.event.WindowEvent evt) {
                formWindowGainedFocus(evt);
            }
            public void windowLostFocus(java.awt.event.WindowEvent evt) {
            }
        });
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setForeground(new java.awt.Color(204, 204, 204));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel2.setText("Nombre Cliente: ");

        txtnombrecliente.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        txtnombrecliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtnombreclienteKeyReleased(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 3, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 51, 0));
        jLabel1.setText("Realizar Pedido ");

        rbtn1.setBackground(new java.awt.Color(255, 255, 255));
        Grupobtn.add(rbtn1);
        rbtn1.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        rbtn1.setText("Aquí");
        rbtn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtn1ActionPerformed(evt);
            }
        });

        rbtn2.setBackground(new java.awt.Color(255, 255, 255));
        Grupobtn.add(rbtn2);
        rbtn2.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        rbtn2.setText("Domicilio");
        rbtn2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                rbtn2MouseClicked(evt);
            }
        });
        rbtn2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtn2ActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel9.setText("ID");

        txtidcliente.setEditable(false);
        txtidcliente.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N

        jButton2.setBackground(new java.awt.Color(51, 102, 255));
        jButton2.setFont(new java.awt.Font("Arial", 3, 18)); // NOI18N
        jButton2.setText("COCINA");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        btnverpedidos.setBackground(new java.awt.Color(51, 255, 51));
        btnverpedidos.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        btnverpedidos.setText("Ver Pedidos");
        btnverpedidos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnverpedidosActionPerformed(evt);
            }
        });

        tblclientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nombre ", "Domicilio", "Telefono", "id"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblclientes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblclientesMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tblclientes);

        btnID1.setBackground(new java.awt.Color(204, 204, 255));
        btnID1.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        btnID1.setText("X");
        btnID1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnID1ActionPerformed(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel3.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(153, 153, 153));
        jLabel3.setText("Pedido a domicilio");

        jLabel4.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel4.setText("Calle y Numero:");

        txtcalle.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N

        jLabel5.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel5.setText("Entre las calles:");

        txtcalles.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N

        jLabel6.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel6.setText("Numero de telefono:");

        txttelefono.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N

        jLabel8.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel8.setText("Descripcion:");

        jScrollPane2.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N

        txtdescripcion.setColumns(20);
        txtdescripcion.setRows(5);
        jScrollPane2.setViewportView(txtdescripcion);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 371, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6)
                            .addComponent(jLabel8)
                            .addComponent(txttelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 371, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtcalles, javax.swing.GroupLayout.PREFERRED_SIZE, 371, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtcalle, javax.swing.GroupLayout.PREFERRED_SIZE, 371, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(107, 107, 107)
                        .addComponent(jLabel3)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel4)
                .addGap(1, 1, 1)
                .addComponent(txtcalle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel5)
                .addGap(1, 1, 1)
                .addComponent(txtcalles, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txttelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 11, Short.MAX_VALUE))
        );

        btnID.setBackground(new java.awt.Color(204, 204, 255));
        btnID.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        btnID.setText("OK");
        btnID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIDActionPerformed(evt);
            }
        });

        cmbMesas.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cmbMesas.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccionar mesa" }));
        cmbMesas.setEnabled(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(rbtn1)
                        .addGap(18, 18, 18)
                        .addComponent(cmbMesas, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(41, 41, 41)
                        .addComponent(rbtn2))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jButton2)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnverpedidos))
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 209, Short.MAX_VALUE)
                                .addComponent(txtnombrecliente))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel9)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(txtidcliente, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(btnID1, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(btnID, javax.swing.GroupLayout.Alignment.TRAILING)))))
                .addContainerGap(45, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 352, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnID)
                        .addGap(10, 10, 10))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtidcliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtnombrecliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnID1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rbtn1)
                    .addComponent(rbtn2)
                    .addComponent(cmbMesas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnverpedidos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 35, Short.MAX_VALUE))
        );

        jPanel5.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 45, 450, -1));

        Tablapedido.setFont(new java.awt.Font("Arial", 3, 12)); // NOI18N
        Tablapedido.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id ", "Producto", "Descripcion ", "Cantidad ", "Precio", "Costo"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Integer.class, java.lang.Float.class, java.lang.Float.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, true, true, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        Tablapedido.setGridColor(new java.awt.Color(0, 0, 0));
        Tablapedido.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TablapedidoMouseClicked(evt);
            }
        });
        Tablapedido.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                TablapedidoPropertyChange(evt);
            }
        });
        jScrollPane1.setViewportView(Tablapedido);
        if (Tablapedido.getColumnModel().getColumnCount() > 0) {
            Tablapedido.getColumnModel().getColumn(0).setResizable(false);
            Tablapedido.getColumnModel().getColumn(1).setResizable(false);
            Tablapedido.getColumnModel().getColumn(2).setResizable(false);
            Tablapedido.getColumnModel().getColumn(3).setResizable(false);
            Tablapedido.getColumnModel().getColumn(4).setResizable(false);
            Tablapedido.getColumnModel().getColumn(5).setResizable(false);
        }

        jPanel5.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 410, 840, 200));

        jLabel7.setFont(new java.awt.Font("Arial", 1, 28)); // NOI18N
        jLabel7.setText("Total:");
        jPanel5.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 630, -1, -1));

        txttotal.setEditable(false);
        txttotal.setFont(new java.awt.Font("Arial", 1, 28)); // NOI18N
        txttotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txttotalActionPerformed(evt);
            }
        });
        jPanel5.add(txttotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 630, 117, -1));

        jButton1.setBackground(new java.awt.Color(0, 102, 204));
        jButton1.setFont(new java.awt.Font("Arial", 3, 36)); // NOI18N
        jButton1.setText("Ordenar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel5.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1150, 630, -1, 39));

        txtfecha.setFont(new java.awt.Font("Arial", 3, 36)); // NOI18N
        txtfecha.setForeground(new java.awt.Color(204, 204, 255));
        txtfecha.setText("00/00/0000");
        jPanel5.add(txtfecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, -1, 30));

        btneliminar.setBackground(new java.awt.Color(255, 0, 0));
        btneliminar.setFont(new java.awt.Font("Arial", 3, 14)); // NOI18N
        btneliminar.setForeground(new java.awt.Color(255, 255, 255));
        btneliminar.setText("Eliminar");
        btneliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btneliminarActionPerformed(evt);
            }
        });
        jPanel5.add(btneliminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 620, 100, 30));

        panelcombos.setBackground(new java.awt.Color(255, 255, 255));
        panelcombos.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout panelcombosLayout = new javax.swing.GroupLayout(panelcombos);
        panelcombos.setLayout(panelcombosLayout);
        panelcombosLayout.setHorizontalGroup(
            panelcombosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 836, Short.MAX_VALUE)
        );
        panelcombosLayout.setVerticalGroup(
            panelcombosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 36, Short.MAX_VALUE)
        );

        jPanel5.add(panelcombos, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 20, 840, 40));

        panelsalsas.setBackground(new java.awt.Color(255, 255, 255));
        panelsalsas.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panelsalsas.setPreferredSize(new java.awt.Dimension(160, 320));

        javax.swing.GroupLayout panelsalsasLayout = new javax.swing.GroupLayout(panelsalsas);
        panelsalsas.setLayout(panelsalsasLayout);
        panelsalsasLayout.setHorizontalGroup(
            panelsalsasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 346, Short.MAX_VALUE)
        );
        panelsalsasLayout.setVerticalGroup(
            panelsalsasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 316, Short.MAX_VALUE)
        );

        jPanel5.add(panelsalsas, new org.netbeans.lib.awtextra.AbsoluteConstraints(970, 70, 350, -1));

        panelbotones.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout panelbotonesLayout = new javax.swing.GroupLayout(panelbotones);
        panelbotones.setLayout(panelbotonesLayout);
        panelbotonesLayout.setHorizontalGroup(
            panelbotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 478, Short.MAX_VALUE)
        );
        panelbotonesLayout.setVerticalGroup(
            panelbotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 318, Short.MAX_VALUE)
        );

        jScrollPane4.setViewportView(panelbotones);

        jPanel5.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 70, 480, 320));

        jMenu1.setText("Administrador");

        menuProductos.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_3, java.awt.event.InputEvent.CTRL_MASK));
        menuProductos.setText("Administar Productos");
        menuProductos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuProductosActionPerformed(evt);
            }
        });
        jMenu1.add(menuProductos);

        menuCombos.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_4, java.awt.event.InputEvent.CTRL_MASK));
        menuCombos.setText("Administrar Combos");
        menuCombos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuCombosActionPerformed(evt);
            }
        });
        jMenu1.add(menuCombos);

        menuSalsas.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_5, java.awt.event.InputEvent.CTRL_MASK));
        menuSalsas.setText("Administrar Salsas");
        menuSalsas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuSalsasActionPerformed(evt);
            }
        });
        jMenu1.add(menuSalsas);

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_6, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setText("Administrar Mesas");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Ver");

        Verventas.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_1, java.awt.event.InputEvent.CTRL_MASK));
        Verventas.setText("Control de Ventas");
        Verventas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                VerventasActionPerformed(evt);
            }
        });
        jMenu2.add(Verventas);

        jMenuBar1.add(jMenu2);

        jMenu3.setText("Clientes");

        menuClientes.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_2, java.awt.event.InputEvent.CTRL_MASK));
        menuClientes.setText("Buscar Cliente");
        menuClientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuClientesActionPerformed(evt);
            }
        });
        jMenu3.add(menuClientes);

        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public void ActualizaDatos(int idDato,int idCliente,int idPedido)
    {
         ResultSet resultado;
         PreparedStatement enunciado;
        int resul;
        try{
         enunciado = cn.prepareStatement("update  Datos set Cliente=?, Pedido=? where idDatos=? ");
         enunciado.setInt(1, idCliente);
         enunciado.setInt(2, idPedido);
         enunciado.setInt(3, idDato);
         resul = enunciado.executeUpdate();
        }catch(Exception e){
         e.printStackTrace();
        }
    }
    public void InsertarCliente(int idCliente)
    {
        ResultSet resultado;
         PreparedStatement enunciado;
        int resul;
        try{ 
         enunciado = cn.prepareStatement("insert into Cliente(idCliente,MontoGastado,visitas,NombreCliente) values (?,?,?,?)");
         enunciado.setInt(1,idCliente);         
         enunciado.setFloat(2, Float.parseFloat(txttotal.getText()));
         enunciado.setInt(3,1);
         enunciado.setString(4,txtnombrecliente.getText());
         resul = enunciado.executeUpdate();
         if(resul > 0 ){
         
         }
         else{
            showMessageDialog(null,"Ocurrio un problema al guardar","Error",ERROR_MESSAGE);
            return;
         }
            }catch(Exception e){
                 e.printStackTrace();
          } 
    }
    public void InsertarDomicilio(int idCliente)
    {
        
        ResultSet resultado;
         PreparedStatement enunciado;
        int resul;
         try{
         enunciado = cn.prepareStatement("insert into Domicilio(Calle,Callereferencia,Descripcion,Telefono,Cliente_idCliente) values(?,?,?,?,?) ");
         enunciado.setString(1,txtcalle.getText());
         enunciado.setString(2,txtcalles.getText());
         enunciado.setString(3,txtdescripcion.getText());
         enunciado.setString(4,txttelefono.getText());
         enunciado.setInt(5,idCliente);
         resul = enunciado.executeUpdate();
           }catch(Exception e){
         e.printStackTrace();
        }  
    }
    public void ActualizaDatosCliente(int idCliente)
    {
        float monto=0;
        int visitas=0;
        ResultSet resultado;
         PreparedStatement enunciado;
        int resul;
         try{
         enunciado = cn.prepareStatement("select * from cliente where idCliente=? ");
         enunciado.setInt(1,idCliente);
         resultado = enunciado.executeQuery();
         while(resultado.next()){
             monto=resultado.getFloat("MontoGastado");
             visitas=resultado.getInt("Visitas");
            }  }catch(Exception e){
                e.printStackTrace();
            }
         //Aumentamos visita y monto    
         visitas++;
         monto+=Float.parseFloat(txttotal.getText());
        
         //Actualizamos los datos del cliente
         ResultSet resultado2;
         PreparedStatement enunciado2;
        int resul2;
         try{
         enunciado2 = cn.prepareStatement("update  cliente set MontoGastado=?, Visitas=? where idCliente=? ");
         enunciado2.setFloat(1, monto);
         enunciado2.setInt(2,visitas);
         enunciado2.setInt(3, idCliente);
         resul2 = enunciado2.executeUpdate();
        }catch(Exception e){
         e.printStackTrace();
        }// 
    }
    public void ActualizaDomicilio(int idCliente)
    {
         ResultSet resultado;
         PreparedStatement enunciado;
        int resul;
         try{
         enunciado = cn.prepareStatement("Update domicilio set Calle=?,Callereferencia=?,Descripcion=?,Telefono=? where Cliente_idCliente=? ");
         enunciado.setString(1, txtcalle.getText());
         enunciado.setString(2,txtcalles.getText());
         enunciado.setString(3,txtdescripcion.getText());
         enunciado.setString(4,txttelefono.getText());
         enunciado.setInt(5,idCliente);
         resul = enunciado.executeUpdate();
           }catch(Exception e){
         e.printStackTrace();
        }
    }
    public void InsertarPedido(int idPedido,int idCliente)
    {
        if(rbtn1.isSelected() == false && rbtn2.isSelected() == false){
            showMessageDialog(this,"Selecciona el destino del pedido");
            return;
        }
        PreparedStatement enunciado;
        PreparedStatement enun;
        int resul, res;
        try {
            enunciado = cn.prepareStatement("insert into Pedidos (idPedidos,Estado,Lugar,Fecha,Cliente_idCliente,monto,feria) values (?,?,?,?,?,?,?)");
            enunciado.setInt(1, idPedido);
            enunciado.setString(2, "En preparacion...");
            if(rbtn1.isSelected() == true){
                 enunciado.setString(3, "Local");
            }
            if(rbtn2.isSelected()== true){
                 enunciado.setString(3, "A domicilio");
            }
            java.sql.Date fecha = new java.sql.Date(date.getTime());
            enunciado.setString(4, fecha.toString());
            enunciado.setInt(5, idCliente);
            enunciado.setFloat(6, Float.parseFloat(txttotal.getText()));
            enunciado.setFloat(7, 0);
            resul = enunciado.executeUpdate();
            enun = cn.prepareStatement("insert into Mesas (Numero, Pedido_idPedido) values (?,?)");
            if(rbtn1.isSelected() == true){
                String mesa = cmbMesas.getSelectedItem().toString();
                String datoMesa[] = mesa.split(" ");                                
                enun.setInt(1, Integer.parseInt(datoMesa[1]));
            }
            if(rbtn2.isSelected() == true){
                enun.setInt(1, 0);
            }
            enun.setInt(2, idPedido);
            res = enun.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Ventanaprincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    public void InsertarVenta(int idPedido)
    {
        //System.out.println(idPedido);
        ResultSet resultado;
        PreparedStatement enunciado;
        int resul;
        try{
            for(int i=0;i<dtm.getRowCount();i++)
             {   
                enunciado = cn.prepareStatement("insert into Venta(Descripcion,Cantidad,Precio,Costo,Fecha,Hora,Pedidos_idPedidos,Productos_idProductos) values (?,?,?,?,?,?,?,?)");
                enunciado.setString(1,dtm.getValueAt(i,2).toString());
                enunciado.setInt(2,(Integer.parseInt(dtm.getValueAt(i,3).toString())));
                enunciado.setFloat(3,Float.parseFloat(dtm.getValueAt(i,4).toString()));
                enunciado.setFloat(4,Float.parseFloat(dtm.getValueAt(i,5).toString()));
                java.sql.Date date2 = new java.sql.Date(date.getTime());
                enunciado.setDate(5,date2);
                java.sql.Time date3 = new java.sql.Time(date.getTime());
                enunciado.setTime(6,date3);
                enunciado.setInt(7,idPedido);
                enunciado.setInt(8,Integer.parseInt(dtm.getValueAt(i,0).toString()));
                resul = enunciado.executeUpdate();
            }  
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if(rbtn1.isSelected() == false && rbtn2.isSelected() == false){
            showMessageDialog(this, "Seleccionar tipo de pedido");
        }else{
        int idcliente=0,idpedido=0,iddato=0;int resul;        
        
        //consultamos el numero de cliente y pedido
        ResultSet resultado;
        PreparedStatement enunciado;
        try{
            enunciado = cn.prepareStatement("select * from Datos ");
            resultado = enunciado.executeQuery();
            while(resultado.next()){
                iddato=resultado.getInt("idDatos");
                idcliente=resultado.getInt("Cliente");
                idpedido=resultado.getInt("Pedido");
            }  
        }catch(Exception e){
            e.printStackTrace();
        }
        
        if(EsCliente)
        {
            idpedido++;
            ActualizaDatos(iddato,idcliente,idpedido);
            ActualizaDatosCliente(NumeroCliente);
            if(rbtn2.isSelected()){
                ActualizaDomicilio(NumeroCliente);
            }
            
            InsertarPedido(idpedido,NumeroCliente);
            InsertarVenta(idpedido);
        }
        else
        {
            NumeroCliente=idcliente+1;
            idpedido++;
            ActualizaDatos(iddato,NumeroCliente,idpedido);
            InsertarCliente(NumeroCliente);
            if(rbtn2.isSelected()){
                InsertarDomicilio(NumeroCliente);
            }
            
            InsertarPedido(idpedido,NumeroCliente);
            InsertarVenta(idpedido);
        }
    
        
        limpiarcampos();
        EsCliente=false;
        txttotal.setText("");
        txtidcliente.setText("");
       
        showMessageDialog(this,"Pedido Realizado.");
        Grupobtn.clearSelection();
        //
        }
    }//GEN-LAST:event_jButton1ActionPerformed
    public boolean validarcampos()
    {
       if(txtnombrecliente.getText().equals(""))
       {
           showMessageDialog(this,"Ingresa el nombre del cliente");
           txtnombrecliente.requestFocus();
           
               return false;    
           
       }
       if(rbtn1.isSelected()!=true&&rbtn2.isSelected()!=true)
       {
           showMessageDialog(this,"Selecciona donde el lugar del pedido.");
           return false;
       }
       if(rbtn2.isSelected())
       {
       if(txtcalle.getText().equals(""))
       {
           showMessageDialog(this,"Ingresa la calle.");
           txtcalle.requestFocus();
           return false;
       }
       if(txtcalles.getText().equals(""))
       {
           showMessageDialog(this,"Ingresa entre que calles.");
           txtcalles.requestFocus();
           return false;
       }
       if(txttelefono.getText().equals(""))
       {
           showMessageDialog(this,"Ingresa un numero de telefono.");
           txttelefono.requestFocus();
           return false;
       }
       }
       
       
          return true; 
    }
    
    @Override
    public Image getIconImage() {
        Image retValue = Toolkit.getDefaultToolkit().
        getImage(ClassLoader.getSystemResource("Recursos/icono.png"));
        return retValue;
    }
    
    public void limpiarcampos()
    {
        txtnombrecliente.setText("");
        txtidcliente.setText("");
        rbtn1.setSelected(false);
        rbtn2.setSelected(false);
        txtcalle.setText(" ");
        txtcalles.setText("");
        txttelefono.setText("");
        txtdescripcion.setText("");
        txttotal.setText("");
        dtm.setRowCount(0);
        dtm2.setRowCount(0);
        
    }
     
    public void cargaralsas()
    {
        panelsalsas.removeAll();
        JButton[] arrBtnsalsas= new JButton[listasalsa.size()];
        panelsalsas.setLayout(new GridLayout(10,3, 0, 0));
        for( int i=0;i<listasalsa.size();i++){//ciclo para crear, añadir, establecer propiedades a los botones
            arrBtnsalsas[i] = new JButton(listasalsa.get(i).getNombresalsa());            
            arrBtnsalsas[i].setBackground(new java.awt.Color(255, 204, 204));              
            arrBtnsalsas[i].setFont(new java.awt.Font("Arial", 1, 12));
            arrBtnsalsas[i].addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent evento){
                    JButton evt=(JButton)evento.getSource();  
                    Tablapedido.setValueAt(evt.getText(), Tablapedido.getSelectedRow(), 2);
                    panelsalsas.setVisible(false);
                    panelsalsas.removeAll();
                }
            });
            panelsalsas.add(arrBtnsalsas[i]);
            arrBtnsalsas[i].setMargin(new Insets(1, 1, 1, 1));
        }//fin ciclo
    }
    
    
    private void TablapedidoPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_TablapedidoPropertyChange
     calculacosto();
    }//GEN-LAST:event_TablapedidoPropertyChange

    private void txttotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txttotalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txttotalActionPerformed
    
    private void btnverpedidosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnverpedidosActionPerformed
        Pedidos verp=new Pedidos();
        verp.setVisible(true);
    }//GEN-LAST:event_btnverpedidosActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        Cocina vercocina=new Cocina();
        vercocina.Actualizartabla();
        vercocina.setVisible(true);
        
        Barra b = new Barra();
        b.Actualizartabla();
        b.setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void TablapedidoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TablapedidoMouseClicked
        if(Tablapedido.getSelectedColumn()==2)
        {            
            String tipoProd = "";
            String prod = Tablapedido.getValueAt(Tablapedido.getSelectedRow(),1).toString();
            ResultSet resultado;
            PreparedStatement enunciado;
            try{
                enunciado = cn.prepareStatement("select TipoProducto from Productos where Nombre = ?");
                enunciado.setString(1, prod);
                resultado = enunciado.executeQuery();
                while(resultado.next()){
                    tipoProd = resultado.getString("TipoProducto");
                }
                if(tipoProd.equals("A")||tipoProd.equals("B")){
                    panelsalsas.setVisible(true);
                    cargaralsas();
                    panelsalsas.repaint();                
                }else{
                    panelsalsas.removeAll();
                    panelsalsas.repaint();
                }
            }catch(Exception e){
                e.printStackTrace();         
            }
            
        }else{
            panelsalsas.removeAll();
            panelsalsas.repaint();
        }
    }//GEN-LAST:event_TablapedidoMouseClicked

    private void btnIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIDActionPerformed
        ResultSet resultado;
        PreparedStatement enunciado;
        try
        {        
            NumeroCliente=Integer.parseInt(txtidcliente.getText());
        }catch(NumberFormatException ex){
            showMessageDialog(this,"Ingresa un ID valido");
            txtidcliente.setText("");
            txtidcliente.requestFocus();
            return;
        }
        String Nombre="";
        try{ 
            enunciado = cn.prepareStatement("select * from Cliente where idCliente=? ");
            enunciado.setInt(1,NumeroCliente);
            resultado = enunciado.executeQuery();
            while(resultado.next()){             
                Nombre=resultado.getString("NombreCliente");
            }
        }catch(Exception e){
            e.printStackTrace();
            showMessageDialog(null,"ID CLIENTE NO VALIDO.");
        }
        try{ 
            enunciado = cn.prepareStatement("select * from Domicilio where Cliente_idCliente=? ");
            enunciado.setInt(1,NumeroCliente);
            resultado = enunciado.executeQuery();
            while(resultado.next()){
                txtcalle.setText(resultado.getString("Calle"));
                txtcalles.setText(resultado.getString("Callereferencia"));
                txttelefono.setText(resultado.getString("Telefono")+"");
                txtdescripcion.setText(resultado.getString("Descripcion"));
            }
        }catch(Exception e){
           e.printStackTrace();         
        }
        txtnombrecliente.setText(Nombre);
        EsCliente=true;        
    }//GEN-LAST:event_btnIDActionPerformed

    private void VerventasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_VerventasActionPerformed
        Ventas v=new Ventas();
        v.setVisible(true);
    }//GEN-LAST:event_VerventasActionPerformed

    private void rbtn2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rbtn2MouseClicked
        
    }//GEN-LAST:event_rbtn2MouseClicked

    private void llenarPrecios(int p){
        if(Tablapedido.getRowCount() != 0){            
            for(int i = 0; i<(Tablapedido.getRowCount());i++){                
                for(int j = 0; j<listaproductos.size(); j++){
                    if(listaproductos.get(j).id == Integer.parseInt(Tablapedido.getValueAt(i, 0).toString())){
                        //showMessageDialog(this,"Se aplica el precio");
                        if(p == 1){
                            Tablapedido.setValueAt(listaproductos.get(j).precio, i, 4);
                        }else{
                            Tablapedido.setValueAt(listaproductos.get(j).precioDom, i, 4);
                        }
                        break;
                    }
                }
            }
            calculacosto();
        }else{
            return;
        }        
    }
    
    private void rbtn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtn1ActionPerformed
        cargarproductos();        
        llenarPrecios(1); 
        cargarcombos();               
        Bloquearcampos();
        cmbMesas.setEnabled(true);
    }//GEN-LAST:event_rbtn1ActionPerformed

    private void menuProductosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuProductosActionPerformed
       AdministrarProductos ap=new AdministrarProductos();
       ap.setVisible(true);
    }//GEN-LAST:event_menuProductosActionPerformed

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        inicializarcomponentes2();
        cargarproductos();
        cargarcombos();        
    }//GEN-LAST:event_formWindowActivated

    private void btneliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btneliminarActionPerformed
        if(Tablapedido.getSelectedRow()==-1){return;}
        int r=Tablapedido.getSelectedRow();
        dtm.removeRow(r);
        calculatotal();
    }//GEN-LAST:event_btneliminarActionPerformed

    private void menuClientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuClientesActionPerformed
        Clientes c=new Clientes();
        c.setVisible(true);
    }//GEN-LAST:event_menuClientesActionPerformed

    private void menuCombosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuCombosActionPerformed
        AdministrarCombos a=new AdministrarCombos();
        a.setVisible(true);
    }//GEN-LAST:event_menuCombosActionPerformed

    private void menuSalsasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuSalsasActionPerformed
        AdministrarSalsas as=new AdministrarSalsas();
        as.setVisible(true);
    }//GEN-LAST:event_menuSalsasActionPerformed

    private void formWindowGainedFocus(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowGainedFocus
       
    }//GEN-LAST:event_formWindowGainedFocus

    private void formFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_formFocusGained
        
    }//GEN-LAST:event_formFocusGained

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        
    }//GEN-LAST:event_formWindowOpened

    private void txtnombreclienteKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtnombreclienteKeyReleased
        String nombre=txtnombrecliente.getText();
        Object []O=new Object[4];   
        dtm2.setNumRows(0);
        ResultSet resultado;
        PreparedStatement enunciado;
        int resul;
        try{   
            enunciado = cn.prepareStatement("select * from Cliente,Domicilio where Cliente.idCliente=Cliente_idCliente and NombreCliente like'"+nombre+"%'");
            resultado = enunciado.executeQuery();
            while(resultado.next()){    
                O[0]=resultado.getString("NombreCliente");                         
                O[1]=resultado.getString("Calle");         
                O[2]=resultado.getString("Telefono");
                O[3]=resultado.getString("idCliente");
                dtm2.addRow(O);                
            } 
            if(dtm2.getRowCount()==0){
                enunciado = cn.prepareStatement("select * from Cliente,Domicilio where Cliente.idCliente=Cliente_idCliente and Telefono like'"+nombre+"%'");
                resultado = enunciado.executeQuery();
                while(resultado.next()){    
                    O[0]=resultado.getString("NombreCliente");                         
                    O[1]=resultado.getString("Calle");         
                    O[2]=resultado.getString("Telefono");
                    O[3]=resultado.getString("idCliente");
                    dtm2.addRow(O);                
                } 
            }
        }catch(Exception e){
           e.printStackTrace();
        }
    }//GEN-LAST:event_txtnombreclienteKeyReleased

    private void tblclientesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblclientesMouseClicked
        txtnombrecliente.setText(tblclientes.getValueAt(tblclientes.getSelectedRow(), 0).toString());
        txtidcliente.setText(tblclientes.getValueAt(tblclientes.getSelectedRow(), 3).toString());
        ResultSet resultado;
        PreparedStatement enunciado;
        try
        {        
            NumeroCliente=Integer.parseInt(txtidcliente.getText());
        }catch(NumberFormatException ex){
            showMessageDialog(this,"Ingresa un ID valido");
            txtidcliente.setText("");
            txtidcliente.requestFocus();
            return;
        }
        String Nombre="";
        try{ 
            enunciado = cn.prepareStatement("select * from Cliente where idCliente=? ");
            enunciado.setInt(1,NumeroCliente);
            resultado = enunciado.executeQuery();
            while(resultado.next()){             
                Nombre=resultado.getString("NombreCliente");
            }
        }catch(Exception e){
            e.printStackTrace();
            showMessageDialog(null,"ID CLIENTE NO VALIDO.");
        }
        try{ 
            enunciado = cn.prepareStatement("select * from Domicilio where Cliente_idCliente=? ");
            enunciado.setInt(1,NumeroCliente);
            resultado = enunciado.executeQuery();
            while(resultado.next()){
                txtcalle.setText(resultado.getString("Calle"));
                txtcalles.setText(resultado.getString("Callereferencia"));
                txttelefono.setText(resultado.getString("Telefono")+"");
                txtdescripcion.setText(resultado.getString("Descripcion"));
            }
        }catch(Exception e){
           e.printStackTrace();         
        }
        txtnombrecliente.setText(Nombre);
        EsCliente=true;      
    }//GEN-LAST:event_tblclientesMouseClicked

    private void btnID1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnID1ActionPerformed
        limpiarcampos();
        EsCliente=false;
                
    }//GEN-LAST:event_btnID1ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        AdministrarMesas adM = new AdministrarMesas();
        adM.setVisible(true);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void rbtn2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtn2ActionPerformed
        cargarproductos();
        llenarPrecios(2); 
        cargarcombos();
        Habilitarcampos();
        cmbMesas.setEnabled(false);
    }//GEN-LAST:event_rbtn2ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
       
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try 
        {
          UIManager.setLookAndFeel(new SyntheticaPlainLookAndFeel());
        } 
        catch (Exception e) 
        {
          e.printStackTrace();
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Ventanaprincipal().setVisible(true);
                
            }
        });
    }
DefaultTableModel dtm,dtm2;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup Grupobtn;
    private javax.swing.JTable Tablapedido;
    private javax.swing.JMenuItem Verventas;
    private javax.swing.JButton btnID;
    private javax.swing.JButton btnID1;
    private javax.swing.JButton btneliminar;
    private javax.swing.JButton btnverpedidos;
    private javax.swing.JComboBox<String> cmbMesas;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JMenuItem menuClientes;
    private javax.swing.JMenuItem menuCombos;
    private javax.swing.JMenuItem menuProductos;
    private javax.swing.JMenuItem menuSalsas;
    private javax.swing.JPanel panelbotones;
    private javax.swing.JPanel panelcombos;
    private javax.swing.JPanel panelsalsas;
    private javax.swing.JRadioButton rbtn1;
    private javax.swing.JRadioButton rbtn2;
    private javax.swing.JTable tblclientes;
    private javax.swing.JTextField txtcalle;
    private javax.swing.JTextField txtcalles;
    private javax.swing.JTextArea txtdescripcion;
    private javax.swing.JLabel txtfecha;
    private javax.swing.JTextField txtidcliente;
    private javax.swing.JTextField txtnombrecliente;
    private javax.swing.JTextField txttelefono;
    private javax.swing.JTextField txttotal;
    // End of variables declaration//GEN-END:variables
}
