package co.edu.unipiloto.login;

import java.util.ArrayList;
import java.util.List;

public class UserManager {
    private static UserManager instance;

    private String name;
    private String phone;
    private String email;
    private String type;
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
