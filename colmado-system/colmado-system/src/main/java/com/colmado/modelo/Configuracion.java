package com.colmado.modelo;

public class Configuracion {
        private int id;
        private String nombre;
        private String rnc;
        private String direccion;
        private String telefono;

        public Configuracion() {}

        public Configuracion(int id, String nombre, String rnc, String direccion, String telefono) {
            this.id = id;
            this.nombre = nombre;
            this.rnc = rnc;
            this.direccion = direccion;
            this.telefono = telefono;
        }

        // Getters y Setters
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
        public String getRnc() { return rnc; }
        public void setRnc(String rnc) { this.rnc = rnc; }
        public String getDireccion() { return direccion; }
        public void setDireccion(String direccion) { this.direccion = direccion; }
        public String getTelefono() { return telefono; }
        public void setTelefono(String telefono) { this.telefono = telefono; }
}
