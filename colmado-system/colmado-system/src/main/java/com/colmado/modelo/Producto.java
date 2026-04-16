package com.colmado.modelo;

public class Producto {
    private int id_producto;
    private String nombre;
    private double precio;
    private int cantidad;
    private String descripcion;
    private String categoria;

    public Producto(String nombre, double precio, int cantidad,
                    String descripcion, String categoria) {
        this.nombre      = nombre;
        this.precio      = precio;
        this.cantidad    = cantidad;
        this.descripcion = descripcion;
        this.categoria   = categoria;
    }

    // Setters
    public void setId_producto(int id_producto) { this.id_producto = id_producto; }
    public void setNombre(String nombre)         { this.nombre = nombre; }
    public void setPrecio(double precio)         { this.precio = precio; }
    public void setCantidad(int cantidad)        { this.cantidad = cantidad; }
    public void setDescripcion(String desc)      { this.descripcion = desc; }
    public void setCategoria(String categoria)   { this.categoria = categoria; }

    // Getters
    public int    getId_producto() { return id_producto; }
    public String getNombre()      { return nombre; }
    public double getPrecio()      { return precio; }
    public int    getCantidad()    { return cantidad; }
    public String getDescripcion() { return descripcion; }
    public String getCategoria()   { return categoria; }

    @Override
    public String toString() { return nombre; }
}