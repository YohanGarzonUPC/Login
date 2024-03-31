package co.edu.unipiloto.login;

public class Publicacion {
    private String nombre;
    private String origen;
    private String destino;
    private String peso;
    private String descripcion;
    private String fecha;

    public int getIdPublicacion() {
        return idPublicacion;
    }

    public void setIdPublicacion(int idPublicacion) {
        this.idPublicacion = idPublicacion;
    }

    private int idPublicacion;

    public Publicacion(String nombre, String origen,String destino, String peso, String descripcion, String fecha, int idPublicacion) {
        this.nombre = nombre;
        this.origen = origen;
        this.destino= destino;
        this.peso = peso;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.idPublicacion=idPublicacion;
    }

    public Publicacion() {

    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setOrigen(String direccion) {
        this.origen = direccion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public void setPeso(String peso) {
        this.peso = peso;
    }

    public String getNombre() {
        return nombre;
    }

    public String getOrigen() {
        return origen;
    }

    public String getPeso() {
        return peso;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getFecha() {
        return fecha;
    }
    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }
}