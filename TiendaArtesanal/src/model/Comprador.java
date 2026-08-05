package src.model;

import java.util.ArrayList;

public class Comprador {
    private String id;
    private String nombre;
    private String email;
    private String direccion;
    private ArrayList<Envio> envios;
    private ArrayList<MetodoPago> metodosPago;

    public Comprador(String id, String nombre, String email, String direccion) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.direccion = direccion;
        this.envios = new ArrayList<>();
        this.metodosPago = new ArrayList<>();
    }

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public String getDireccion() { return direccion; }
    public ArrayList<Envio> getEnvios() { return envios; }
    public ArrayList<MetodoPago> getMetodosPago() { return metodosPago; }

    public void addEnvio(Envio e) { envios.add(e); }
    public void addMetodoPago(MetodoPago m) { metodosPago.add(m); }
}
