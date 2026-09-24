package mx.uv.fca.sistema_bibliotecario_interno.dto;

import mx.uv.fca.sistema_bibliotecario_interno.model.Rol;

public class UsuarioUpdateDTO {

    private String nombre;
    private String correo;
    private Rol rol;
    private Boolean activo;

    public UsuarioUpdateDTO() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}