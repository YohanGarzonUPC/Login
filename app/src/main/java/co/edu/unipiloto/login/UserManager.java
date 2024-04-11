package co.edu.unipiloto.login;

import java.util.ArrayList;
import java.util.List;

public class UserManager {
    private static UserManager instance;

    private String name;
    private String phone;
    private String direccion;
    private String email;
    private String type;
    private String imagen_url;

    private List<Publicacion> publications;

    private UserManager() {
        publications = new ArrayList<>();
    }

    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImagen_url() {
        return imagen_url;
    }

    public void setImagen_url(String imagen_url) {
        this.imagen_url = imagen_url;
    }

    public List<Publicacion> getPublications() {
        return publications;
    }

    public void setPublicaciones(List<Publicacion> publications) {
        this.publications = publications;
    }

    public void addPublicaciones(Publicacion publication) {
        this.publications.add(publication);
    }
}
