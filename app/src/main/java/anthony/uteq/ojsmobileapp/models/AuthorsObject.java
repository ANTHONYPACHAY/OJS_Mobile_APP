package anthony.uteq.ojsmobileapp.models;

public class AuthorsObject {

    String nombres = "";
    String filiacion = "";
    String email = "";

    public AuthorsObject() {
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getFiliacion() {
        return filiacion;
    }

    public void setFiliacion(String filiacion) {
        this.filiacion = filiacion;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
