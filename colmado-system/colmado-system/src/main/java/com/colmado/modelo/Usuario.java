package com.colmado.modelo;

public class Usuario {

    public enum rol {ADMIN, CAJERO}

    private int idusuario;
    private String username;
    private String password;
    private rol rol;

    public Usuario(int idusuario, String username, String password, rol rol){
        this.idusuario = idusuario;
        this.username = username;
        this.password = password;
        this.rol = rol;
    }

    public int getIdusuario() {
        return idusuario;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public rol getRol() {
        return rol;
    }

    public void setIdusuario(int idusuario) {
        this.idusuario = idusuario;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRol(rol rol) {
        this.rol = rol;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "idusuario=" + idusuario +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", rol='" + rol + '\'' +
                '}';
    }
}
