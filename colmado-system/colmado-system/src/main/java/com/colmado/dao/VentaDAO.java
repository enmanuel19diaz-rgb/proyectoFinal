package com.colmado.dao;
import com.colmado.modelo.Venta;
import com.colmado.modelo.DetalleVenta;
import com.colmado.util.ConexionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class VentaDAO {

    public boolean registrarVentas(Venta venta){
        Connection con = ConexionDB.getInstancia().getConexion();

        try{
            con.setAutoCommit(false);

            String ventaSql = "INSERT INTO ventas (id_cliente, fecha, total) VALUES (?,?,?)";
            PreparedStatement psVenta = con.prepareStatement(ventaSql, Statement.RETURN_GENERATED_KEYS);
            psVenta.setInt(1, venta.getId_cliente());
            psVenta.setObject(2, venta.getFecha());
            psVenta.setDouble(3, venta.getTotal());
            psVenta.executeUpdate();

            ResultSet rs = psVenta.getGeneratedKeys();
            int idventaGenerado = 0;
            if (rs.next()){
                idventaGenerado = rs.getInt(1);
            }

            String detalleSql = "INSERT INTO detalle_venta (id_venta, id_producto, cantidad, precio_unit) VALUES (?,?,?,?)";
            PreparedStatement psDetalle = con.prepareStatement(detalleSql);

            for (DetalleVenta d: venta.getDetalle()){
                psDetalle.setInt(1, idventaGenerado);
                psDetalle.setInt(2, d.getId_producto());
                psDetalle.setInt(3, d.getCantidad());
                psDetalle.setDouble(4, d.getPrecioUnit());
                psDetalle.addBatch();
            }
            psDetalle.executeBatch();
            con.commit();
            return true;
        }catch(Exception e){
            try{
                con.rollback();
            } catch(Exception ex){
                ex.printStackTrace();
            }
            System.out.println("Error al vender: " + e.getMessage());
            return false;
        } finally{
            try{
                con.setAutoCommit(true);
            }catch(Exception ex){}
        }

    }
}
