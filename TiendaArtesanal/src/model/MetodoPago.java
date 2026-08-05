package src.model;

public class MetodoPago {
    private String id;
    private String tipo; // e.g., "Tarjeta de Crédito", "PayPal"
    private String ultimosDigitos;
    private String titular;

    public MetodoPago(String id, String tipo, String ultimosDigitos, String titular) {
        this.id = id;
        this.tipo = tipo;
        this.ultimosDigitos = ultimosDigitos;
        this.titular = titular;
    }

    public String getId() { return id; }
    public String getTipo() { return tipo; }
    public String getUltimosDigitos() { return ultimosDigitos; }
    public String getTitular() { return titular; }
}
