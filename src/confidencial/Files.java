/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package confidencial;

import java.io.*;
import static javax.swing.JOptionPane.showMessageDialog;


/**
 *
 * @author Gonzalo
 */
public class Files {
    File archivo = null;
    FileReader fr = null;
    BufferedReader br = null;    
    
    private String getFile(){
        String cad="";
        File f = new File("route.txt"); // Creamos un objeto file            
        String coroute = f.getAbsolutePath();
        String route[] = coroute.split("\\\\");
        //System.out.println(f.getAbsolutePath());
        for(int i = 0; i<(route.length)-1;i++){            
            cad += route[i]+"\\\\";
        }                        
        return cad;
    }
    
    public boolean leerBand(){        
        if(leer().equals("1")){
            return true;
        }else{
            return false;
        }        
    }
    
    public String leer(){
        String ruta="";
        try {     
            String f = getFile();
            archivo = new File (f+"passencrypt.dll");
            fr = new FileReader (archivo);
            br = new BufferedReader(fr);

            // Lectura del fichero
            String linea;
            while((linea=br.readLine())!=null){
                ruta+=linea+"";
            }
            String [] pass = ruta.split("-");
            //String passwd = desEncrpt(pass[1]);
            return pass[1];
        }
        catch(Exception e){
            e.printStackTrace();
        }finally{
            // En el finally cerramos el fichero, para asegurarnos
            // que se cierra tanto si todo va bien como si salta 
            // una excepcion.
            try{                    
                if( null != fr ){   
                   fr.close();     
                }                  
            }catch (Exception e2){ 
               e2.printStackTrace();
            }
        }
        return null;
    }
    
    private String encrpt(String pass){
        String passEncrpt = "1-";
        for(int i = 0; i<pass.length(); i++){
            passEncrpt += pass.charAt(i)+18;
        }
        return passEncrpt;
    }
    
    private String desEncrpt(String pass){
        String passDesenc ="";
        for(int i= 0; i<pass.length(); i++){
            passDesenc += (char) pass.charAt(i)-18;
        }
        showMessageDialog(null,passDesenc);
        return passDesenc;
    }
    
    public void escribir(String pass){
        FileWriter fichero = null;
        PrintWriter pw = null;
        try
        {
            String r = getFile();
            fichero = new FileWriter(r+"passencrypt.dll");
            pw = new PrintWriter(fichero);
            
            //String passencpt = encrpt(pass);
            pw.println(pass);
            

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
            // Nuevamente aprovechamos el finally para 
            // asegurarnos que se cierra el fichero.
            if (null != fichero)
               fichero.close();
            } catch (Exception e2) {
               e2.printStackTrace();
            }
        }
    }
    
    public static void main(String[] args) {
        //Files f = new Files();
        //f.escribir("alitasexpress2019");
        //System.out.println(f.leerBand());
        System.out.println("a".charAt(0)+1);
        System.out.println("a".charAt(0)+5);
    }
}
