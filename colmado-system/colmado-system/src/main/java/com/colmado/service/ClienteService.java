package com.colmado.service;

import com.colmado.dao.ClienteDAO;
import com.colmado.modelo.Cliente;

import java.util.List;

public class ClienteService {

    private final ClienteDAO dao = new ClienteDAO();

    // ── Guardar (nuevo o editar) ───────────────────────────────────────────────
    public String guardar(Cliente c) {
        String error = validar(c);
        if (error != null) return error;

        boolean ok = (c.getId() == 0) ? dao.insertar(c) : dao.actualizar(c);
        return ok ? null : "Error al guardar en la base de datos.";
    }

    // ── Eliminar ──────────────────────────────────────────────────────────────
    public String eliminar(int id) {
        if (id <= 0) return "Selecciona un cliente válido.";
        return dao.eliminar(id) ? null : "No se pudo eliminar el cliente.";
    }

    // ── Listar ────────────────────────────────────────────────────────────────
    public List<Cliente> listarTodos() {
        return dao.listarTodos();
    }

    public List<Cliente> buscar(String texto) {
        if (texto == null || texto.isBlank()) return dao.listarTodos();
        return dao.buscarPorNombre(texto.trim());
    }

    public Cliente buscarPorId(int id) {
        return dao.buscarPorId(id);
    }

    // ── Validaciones ──────────────────────────────────────────────────────────
    private String validar(Cliente c) {
        if (c.getNombre() == null || c.getNombre().isBlank())
            return "El nombre es obligatorio.";
        if (c.getNombre().length() > 100)
            return "El nombre no puede superar 100 caracteres.";
        if (c.getTelefono() != null && !c.getTelefono().isBlank()
                && !c.getTelefono().matches("[0-9\\-\\+\\s()]{7,20}"))
            return "El teléfono tiene un formato inválido.";
        if (c.getEmail() != null && !c.getEmail().isBlank()
                && !c.getEmail().matches("^[\\w._%+\\-]+@[\\w.\\-]+\\.[a-zA-Z]{2,}$"))
            return "El correo electrónico tiene un formato inválido.";
        return null;
    }
}