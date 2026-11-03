import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Clase modelo que representa una reserva del hotel.
 */
public class Reserva {

    private final String codigo;
    private final String estado;
    private final String clienteNombre;
    private final String clienteApellidos;
    private final String clienteEmail;
    private final String habitacion;
    private final LocalDate entrada;
    private final LocalDate salida;
    private final int personas;
    private final String observaciones;

    public Reserva(String codigo, String estado, String clienteNombre,
                   String clienteApellidos, String clienteEmail, String habitacion,
                   LocalDate entrada, LocalDate salida, int personas, String observaciones) {
        this.codigo = codigo;
        this.estado = estado;
        this.clienteNombre = clienteNombre;
        this.clienteApellidos = clienteApellidos;
        this.clienteEmail = clienteEmail;
        this.habitacion = habitacion;
        this.entrada = entrada;
        this.salida = salida;
        this.personas = personas;
        this.observaciones = observaciones;
    }

    public String getCodigo() { return codigo; }
    public String getEstado() { return estado; }
    public String getClienteNombre() { return clienteNombre; }
    public String getClienteApellidos() { return clienteApellidos; }
    public String getClienteEmail() { return clienteEmail; }
    public String getHabitacion() { return habitacion; }
    public LocalDate getEntrada() { return entrada; }
    public LocalDate getSalida() { return salida; }
    public int getPersonas() { return personas; }
    public String getObservaciones() { return observaciones; }

    public String getClienteCompleto() {
        return clienteNombre + " " + clienteApellidos;
    }

    public String getEstadoFormateado() {
        return Character.toUpperCase(estado.charAt(0)) + estado.substring(1);
    }

    public static LocalDate parseFecha(String fechaTexto) {
        if (fechaTexto == null || fechaTexto.isEmpty()) {
            return null;
        }
        return LocalDate.parse(fechaTexto, DateTimeFormatter.ISO_LOCAL_DATE);
    }

    @Override
    public String toString() {
        return codigo + " - " + estado + " - " + getClienteCompleto()
                + " - Hab. " + habitacion + " - " + entrada + " a " + salida;
    }
}