package mx.uv.fca.sistema_bibliotecario_interno.dto;

public class LoginResponse {

    private Integer idUsuario;
    private String nombre;
    private String correo;
    private String rol;
    private String mensaje;
    private String token;

    public LoginResponse() {
    }

    public LoginResponse(
            Integer idUsuario,
            String nombre,
            String correo,
            String rol,
            String mensaje,
            String token) {

        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.correo = correo;
        this.rol = rol;
        this.mensaje = mensaje;
        this.token = token;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
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

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}