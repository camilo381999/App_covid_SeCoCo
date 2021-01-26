package project.main.Informes;

import java.io.Serializable;

public class Informe implements Serializable {
    String cedula="";
    String correo="";
    String estado="";
    String fecha="";
    String puntaje="";
    String edad="";
    String genero="";
    String  direccion="";
 public Informe(){

 }

    public Informe(String cedula, String correo, String estado, String fecha,
                   String puntaje,String edad, String genero,String direccion) {
        this.cedula = cedula;
        this.correo = correo;
        this.estado = estado;
        this.fecha = fecha;
        this.puntaje = puntaje;
        this.edad=edad;
        this.genero=genero;
        this.direccion=direccion;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getEdad() {
        return edad;
    }

    public void setEdad(String edad) {
        this.edad = edad;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(String puntaje) {
        this.puntaje = puntaje;
    }
}
