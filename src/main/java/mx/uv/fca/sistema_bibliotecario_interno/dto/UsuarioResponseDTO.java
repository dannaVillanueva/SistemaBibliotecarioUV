package mx.uv.fca.sistema_bibliotecario_interno.dto;

import mx.uv.fca.sistema_bibliotecario_interno.model.Usuario;

public class UsuarioResponseDTO {

    private final Integer idUsuario;
    private final String nombre;
    private final String correo;
    private final String rol;
    private final Boolean activo;

    public UsuarioResponseDTO(Usuario usuario) {
        this.idUsuario = usuario.getIdUsuario();
        this.nombre = usuario.getNombre();
        this.correo = usuario.getCorreo();
        this.rol = usuario.getRol().name();
        this.activo = usuario.getActivo();
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public String getRol() {
        return rol;
    }

    public Boolean getActivo() {
        return activo;
    }
}