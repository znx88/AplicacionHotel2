import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class HotelController {

    /* ---------- Vistas existentes ---------- */
    @FXML
    private Label lblInfo;

    @FXML
    private ListView<String> listaHabitaciones;

    @FXML
    private TextField txtNumero;

    @FXML
    private Label lblResultado;

    /* ---------- Nuevos controles para informes ---------- */
    @FXML
    private TabPane tabPane;

    @FXML
    private Tab tabInformes;

    @FXML
    private ComboBox<String> cmbEstadoFiltro;

    @FXML
    private ComboBox<String> cmbMesFiltro;

    @FXML
    private TableView<Reserva> tablaReservas;

    @FXML
    private TableColumn<Reserva, String> colCodigo;

    @FXML
    private TableColumn<Reserva, String> colEstado;

    @FXML
    private TableColumn<Reserva, String> colCliente;

    @FXML
    private TableColumn<Reserva, String> colHabitacion;

    @FXML
    private TableColumn<Reserva, String> colEntrada;

    @FXML
    private TableColumn<Reserva, String> colSalida;

    @FXML
    private TableColumn<Reserva, Integer> colPersonas;

    @FXML
    private Label lblTotalReservas;

    @FXML
    private Label lblTotalConfirmadas;

    @FXML
    private Label lblTotalFinalizadas;

    @FXML
    private Label lblTotalCanceladas;

    @FXML
    private Label lblTotalPendientes;

    @FXML
    private Button btnAplicarFiltro;

    /* ---------- Datos ---------- */
    // numero -> "estado|tipo|capacidad|precio"
    private Map<String, String[]> habitaciones = new HashMap<>();
    private List<Reserva> todasReservas = new ArrayList<>();
    private ObservableList<Reserva> reservasFiltradas = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        cargarXML();
        mostrarDatos();
        inicializarInformes();
    }

    private void cargarXML() {
        try {
            InputStream is = getClass().getResourceAsStream("reservas.xml");
            // Permite ejecutar también desde el IDE si el XML está en resources
            if (is == null) {
                is = getClass().getResourceAsStream("/reservas.xml");
            }
            Document doc = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder().parse(is);
            doc.getDocumentElement().normalize();

            String nombre = doc.getElementsByTagName("nombre").item(0).getTextContent();
            String ciudad = doc.getElementsByTagName("ciudad").item(0).getTextContent();
            lblInfo.setText("Hotel " + nombre + " (" + ciudad + ") - datos leídos desde reservas.xml");

            NodeList listaHab = doc.getElementsByTagName("habitacion");
            for (int i = 0; i < listaHab.getLength(); i++) {
                Element h = (Element) listaHab.item(i);
                String numero = h.getAttribute("numero");
                String estado = h.getAttribute("estado");
                String tipo = h.getElementsByTagName("tipo").item(0).getTextContent();
                String capacidad = h.getElementsByTagName("capacidad").item(0).getTextContent();
                String precio = h.getElementsByTagName("precioNoche").item(0).getTextContent();
                habitaciones.put(numero.trim(), new String[]{estado, tipo, capacidad, precio});
            }

            // --- Cargar reservas ---
            NodeList listaRes = doc.getElementsByTagName("reserva");
            for (int i = 0; i < listaRes.getLength(); i++) {
                Element r = (Element) listaRes.item(i);
                String codigo = r.getAttribute("codigo");
                String estado = r.getAttribute("estado");

                Element cliente = (Element) r.getElementsByTagName("cliente").item(0);
                String nombreCli = cliente.getElementsByTagName("nombre").item(0).getTextContent();
                String apellidos = cliente.getElementsByTagName("apellidos").item(0).getTextContent();
                String email = cliente.getElementsByTagName("email").item(0).getTextContent();

                String habitacion = r.getElementsByTagName("habitacionReservada").item(0).getTextContent().trim();
                LocalDate entrada = Reserva.parseFecha(r.getElementsByTagName("entrada").item(0).getTextContent());
                LocalDate salida = Reserva.parseFecha(r.getElementsByTagName("salida").item(0).getTextContent());
                int personas = Integer.parseInt(r.getElementsByTagName("personas").item(0).getTextContent().trim());
                String obs = "";
                NodeList obsList = r.getElementsByTagName("observaciones");
                if (obsList.getLength() > 0) {
                    obs = obsList.item(0).getTextContent();
                }

                todasReservas.add(new Reserva(codigo, estado, nombreCli, apellidos, email,
                        habitacion, entrada, salida, personas, obs));
            }
        } catch (Exception e) {
            lblInfo.setText("Error al leer reservas.xml: " + e.getMessage());
        }
    }

    private void mostrarDatos() {
        listaHabitaciones.getItems().clear();
        for (Map.Entry<String, String[]> e : habitaciones.entrySet()) {
            String num = e.getKey();
            String[] d = e.getValue();
            listaHabitaciones.getItems().add(
                "Hab. " + num + " - " + d[1] + " (" + d[2] + " pers, " + d[3] + " EUR) - " + d[0]);
        }
    }

    /* ---------- Lógica de informes ---------- */
    private void inicializarInformes() {
        // Configurar columnas de la tabla
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estadoFormateado"));
        colCliente.setCellValueFactory(new PropertyValueFactory<>("clienteCompleto"));
        colHabitacion.setCellValueFactory(new PropertyValueFactory<>("habitacion"));
        colEntrada.setCellValueFactory(cell -> {
            LocalDate fecha = cell.getValue().getEntrada();
            return new javafx.beans.property.SimpleStringProperty(fecha != null ? fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "");
        });
        colSalida.setCellValueFactory(cell -> {
            LocalDate fecha = cell.getValue().getSalida();
            return new javafx.beans.property.SimpleStringProperty(fecha != null ? fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "");
        });
        colPersonas.setCellValueFactory(new PropertyValueFactory<>("personas"));

        // Rellenar ComboBox de estados
        cmbEstadoFiltro.getItems().addAll("Todos", "Confirmada", "Finalizada", "Cancelada", "Pendiente");
        cmbEstadoFiltro.setValue("Todos");

        // Rellenar ComboBox de meses
        cmbMesFiltro.getItems().addAll("Todos los meses", "Octubre 2026", "Noviembre 2026");
        cmbMesFiltro.setValue("Todos los meses");

        // Cargar datos iniciales (sin filtro)
        aplicarFiltro();

        // Acción del botón
        btnAplicarFiltro.setOnAction(e -> aplicarFiltro());
    }

    @FXML
    private void aplicarFiltro() {
        String estadoFiltro = cmbEstadoFiltro.getValue();
        String mesFiltro = cmbMesFiltro.getValue();

        reservasFiltradas.clear();
        for (Reserva r : todasReservas) {
            boolean coincideEstado = "Todos".equals(estadoFiltro)
                    || r.getEstadoFormateado().equalsIgnoreCase(estadoFiltro);

            boolean coincideMes = true;
            if (!"Todos los meses".equals(mesFiltro) && r.getEntrada() != null) {
                int mes = r.getEntrada().getMonthValue();
                int anio = r.getEntrada().getYear();
                if ("Octubre 2026".equals(mesFiltro)) {
                    coincideMes = (anio == 2026 && mes == Month.OCTOBER.getValue());
                } else if ("Noviembre 2026".equals(mesFiltro)) {
                    coincideMes = (anio == 2026 && mes == Month.NOVEMBER.getValue());
                }
            }

            if (coincideEstado && coincideMes) {
                reservasFiltradas.add(r);
            }
        }

        tablaReservas.setItems(reservasFiltradas);
        actualizarTotales();
    }

    private void actualizarTotales() {
        int total = reservasFiltradas.size();
        int confirmadas = 0, finalizadas = 0, canceladas = 0, pendientes = 0;

        for (Reserva r : reservasFiltradas) {
            switch (r.getEstado().toLowerCase()) {
                case "confirmada" -> confirmadas++;
                case "finalizada" -> finalizadas++;
                case "cancelada" -> canceladas++;
                case "pendiente" -> pendientes++;
            }
        }

        lblTotalReservas.setText("Total reservas: " + total);
        lblTotalConfirmadas.setText("Confirmadas: " + confirmadas);
        lblTotalFinalizadas.setText("Finalizadas: " + finalizadas);
        lblTotalCanceladas.setText("Canceladas: " + canceladas);
        lblTotalPendientes.setText("Pendientes: " + pendientes);
    }

    @FXML
    private void buscar() {
        String numero = txtNumero.getText().trim();

        if (numero.isEmpty()) {
            lblResultado.setText("Escribe un número de habitación.");
            lblResultado.setStyle("-fx-text-fill: orange;");
            return;
        }

        if (!habitaciones.containsKey(numero)) {
            lblResultado.setText("La habitación " + numero + " no existe.");
            lblResultado.setStyle("-fx-text-fill: purple;");
            return;
        }

        String[] d = habitaciones.get(numero);
        String estado = d[0];
        String texto = "Hab. " + numero + " (" + d[1] + ", " + d[2]
                + " pers, " + d[3] + " EUR/noche): ";

        if (estado.equalsIgnoreCase("libre")
                || estado.equalsIgnoreCase("disponible")) {
            lblResultado.setText(texto + "DISPONIBLE.");
            lblResultado.setStyle("-fx-text-fill: green;");
        } else {
            lblResultado.setText(texto + "OCUPADA.");
            lblResultado.setStyle("-fx-text-fill: red;");
        }
    }
}
