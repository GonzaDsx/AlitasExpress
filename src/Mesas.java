/**
 *
 * @author Gonzalo
 */

public class Mesas {
    int id;
    String nombre;
    public Mesas(){
        this.id = 0;
        this.nombre = "";
    }
    
    public Mesas(int id, String nombre){
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
