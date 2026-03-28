package com.colmado.service;

import com.colmado.modelo.Venta;
import com.colmado.modelo.DetalleVenta;
import com.colmado.dao.VentaDAO;
import java.util.List;
import java.util.ArrayList;

public class VentaService {
    private List<DetalleVenta> carrito;
    private VentaDAO ventaDAO;


    public VentaService(){
        this.carrito = new ArrayList<>();
        this.ventaDAO = new VentaDAO();
    }

}
