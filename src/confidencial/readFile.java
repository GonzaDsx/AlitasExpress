
package confidencial;

import java.io.*;

public class readFile {
    File archivo = null;
    FileReader fr = null;
    BufferedReader br = null;
    public readFile(){
        
    }
    
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
    
    public String leer(){
        String ruta="";
        try {     
            String f = getFile();
            archivo = new File (f+"passencrypt.txt");
            fr = new FileReader (archivo);
            br = new BufferedReader(fr);

            // Lectura del fichero
            String linea;
            while((linea=br.readLine())!=null){
                ruta+=linea+"";
            }
            String [] pass = ruta.split("-");
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
    
    /*public static void main(String[] args) {
        readFile r = new readFile();
        r.leer();
    }*/
}
