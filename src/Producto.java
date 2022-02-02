/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author salva
 */
public class Producto {
int id;
String nombre;
float precio, precioDom;
    public Producto() {
       this.id =0;
        this.nombre = "";
        this.precio = 0; 
        this.precioDom = 0;
    }

    public Producto(int id, String nombre, float precio, float precioDom) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.precioDom = precioDom;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public void setPrecioDom(float precioDom) {
        this.precio = precioDom;
    }
    
    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public float getPrecio() {
        return precio;
    }
        
    public float getPrecioDom() {
        return precioDom;
    }
}
