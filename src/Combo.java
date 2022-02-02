/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author salva
 */
public class Combo {
    String nombre,productos;
    float precio;
    int idDescuento;

    public Combo(String nombre, String productos, float precio,int idDescuento) {
        this.nombre = nombre;
        this.productos = productos;
        this.precio = precio;
        this.idDescuento=idDescuento;
    }

    public Combo() {
    }
    public int getidDesceunto()
    {
        return idDescuento;
    }
    public String getNombre() {
        return nombre;
    }

    public String getProductos() {
        return productos;
    }

    public float getPrecio() {
        return precio;
    }
            
}
