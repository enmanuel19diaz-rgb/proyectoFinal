package com.colmado.service;

import com.colmado.dao.UsuarioDAO;
import com.colmado.modelo.Usuario;

public class LoginService {
    private static UsuarioDAO usuarioDAO;

    public LoginService(){
        this.usuarioDAO = new UsuarioDAO();

    }

    public static Usuario autenticar(String user, String pass)throws Exception{
    // validamos que no este vacio al iniciar seccion
        if(user == null || user.trim().isEmpty()){
            throw new Exception("El nombre de usuario es obligatorio");
        }
        if(pass == null || pass.trim().isEmpty()){
            throw new Exception("la contraseña es obligatoria y no puede estar vacia");
        }
        // llamamos al DAO
        Usuario usuario = usuarioDAO.login(user,pass);

        if(usuario == null){
            throw new Exception("Usuario o contraseña incorrectos.");
        }
        return usuario;
    }

    public boolean esAdmin(Usuario usuario){
        return usuario != null && usuario.getRol() == Usuario.rol.ADMIN;
    }
}
