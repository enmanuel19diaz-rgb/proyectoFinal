package com.colmado.modelo;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

public class Venta {
    private LocalDateTime fecha;
   private int id_venta;
   private int id_cliente;
   private double total;
   private List<DetalleVenta> detalle;


   //Constructor
   public Venta( int id_cliente){
       this.id_cliente = id_cliente;
       this.detalle = new ArrayList<>();
       this.total = 0.0;
       this.fecha = LocalDateTime.now();
   }

   //Setters
   public void setId_venta(int id_venta){
       this.id_venta = id_venta;
   }
   public void setId_cliente(int id_cliente){
       this.id_cliente = id_cliente;
   }

   //Getters
    public double getTotal(){
       double suma = 0.0;

       for (DetalleVenta d : detalle){
           suma += (d.getCantidad() * d.getPrecioUnit());
       }
       return suma;
    }
    public int getId_venta(){
       return id_venta;
    }
    public int getId_cliente(){
       return id_cliente;
    }
    public List<DetalleVenta> getDetalle(){
       return detalle;
    }
    public LocalDateTime getFecha(){
       return fecha;
    }

    //Metodo para agregar Detalle

    public void addDetalle(DetalleVenta d){
       this.detalle.add(d);
    }

}
