package panaderiaonline.panaderiaonline.controlador;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import modelo.PropiedadesMostrarPedidos;
import panaderiaonline.panaderiaonline.App;
import services.conector.Conector;
import services.dao.PedCliente;
import services.dao.Pedido;
import services.dao.Producto;
import services.manager.PedClienteManager;
import services.manager.ProductoManager;

/**
 * 
 * @author Christian Pons Hernández
 *
 */

public class CMostrarPedidos implements Initializable{

	//	ATRIBUTOS
	
	@FXML private VBox ventana;
	private PropiedadesMostrarPedidos datos;
	private VBox seccion2;
	
	
	//	INICIALIZADOR
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		datos = new PropiedadesMostrarPedidos();
		crearSecciones();
		prepararSeccion1();
		seccion2 = (VBox)ventana.getChildren().get(1); // se hace una variable para que sea más facil usarla
		mostrarDatosOrdenados(1);
		
		
	}
	

	/**
	 * Crea las secciones necesarias para la ventana.
	 */
	private void crearSecciones() {
		List<Node>hijosVentana = ventana.getChildren();
		hijosVentana.add(new FlowPane());
		hijosVentana.add(new VBox());
		
	}
	
	
	/**
	 * hace toda la primera sección de la ventana y añade los eventos de ratón a los botones.
	 */
	private void prepararSeccion1() {
		((FlowPane)ventana.getChildren().get(0)).setMinHeight(datos.getVMostrarPedidosTamanoSección1());
		List<Node>hijosPanel = ((FlowPane)ventana.getChildren().get(0)).getChildren();
		
		for(int i = 0; i < datos.getNumeroBotones(); i++) {
			Button boton = new Button(datos.getTextosBotones().get(i));
			boton.setId("" + i);
			boton.addEventHandler(MouseEvent.MOUSE_CLICKED, eventosBotones);
			hijosPanel.add(boton);
		}	
	}
	
	
	/**
	 * recibe una opcion de uno de los eventos, ordena todos los pedidos en función de esa opción y los añade a la ventana.
	 * @param opcion valor numérico que indica cómo se va a ordenar los pedidos.
	 */
	private void mostrarDatosOrdenados(int opcion) {
		List<PedCliente>listaOrdenada;
		List<Node>hijosSeccion = seccion2.getChildren();
		hijosSeccion.removeAll(hijosSeccion);
		List<FlowPane> paneles = new ArrayList<>();
		
		switch(opcion) {
		
		case 1:
			listaOrdenada = listaOrdenadaPorCliente();
			break;
			
		case 2:
			listaOrdenada = listaOrdenadaPorDía();
			break;
			
		default:
			listaOrdenada = listaordenadaPorTiposDePanes();
			
		}
		
		listaOrdenada.forEach(pedido -> {
			mostrarPedidos(paneles, pedido);
		});
		hijosSeccion.addAll(paneles);
	}


	/**
	 * Añade un panel los datos de de un pedido a la lista de paneles.
	 * @param paneles lista de paneles que pertenecerán a la ventana.
	 * @param pedido pedido actual a ser introducido en la ventana.
	 */
	private void mostrarPedidos(List<FlowPane> paneles, PedCliente pedido) {
		FlowPane panel = new FlowPane();
		panel.setPadding(datos.getInsets());
		
		List<Node>hijosPanel = panel.getChildren();
		
		hijosPanel.add(new Label(datos.getTextosLabels1().get(0)));
		hijosPanel.add(new TextField(pedido.getTelefono()));
		
		((TextField)hijosPanel.get(1)).setEditable(false);
		
		hijosPanel.add(new Label(datos.getTextosLabels1().get(1)));
		hijosPanel.add(new TextField("" + pedido.getFecha()));
		
		((TextField)hijosPanel.get(3)).setEditable(false);
		
		hijosPanel.add(new Label(datos.getTextosLabels1().get(2)));
		hijosPanel.add(new TextField(""+ pedido.getNumPedido()));
		
		((TextField)hijosPanel.get(5)).setEditable(false);
		
		paneles.add(panel);
		pedido.getPedidos().forEach(subPedido -> {
			mostrarSubPedidos(paneles, subPedido);
		});
	}


	/**
	 * Se encarga de añadir un panel a la lista de paneles con todos los datos de un producto de un pedido a la ventana.
	 * @param paneles Lista de paneles que pertenecerán a la ventana.
	 * @param subPedido producto del pedido.
	 */
	private void mostrarSubPedidos(List<FlowPane> paneles, Pedido subPedido) {
		FlowPane subPanel = new FlowPane();
		List<Node>hijosSubPanel = subPanel.getChildren();
		
		hijosSubPanel.add(new Label(datos.getTextosLabels2().get(0)));
		hijosSubPanel.add(new TextField(subPedido.getProducto().getNombre()));
		
		((TextField)hijosSubPanel.get(1)).setEditable(false);
		
		hijosSubPanel.add(new Label(datos.getTextosLabels2().get(1)));
		hijosSubPanel.add(new TextField("" + subPedido.getCantidad()));
		
		
		((TextField)hijosSubPanel.get(3)).setEditable(false);
		
		paneles.add(subPanel);
	}


	/**
	 * obtiene la lista de todos los pedidos y la ordena por el número de teléfono.
	 * @return lista ordenada.
	 */
	public List<PedCliente> listaOrdenadaPorCliente() {
		try (Connection con = new Conector().getMySQLConnection()){
			List<PedCliente> listaOrdenada = new PedClienteManager().obtenerTodosLosPedidos(con).stream()
					.sorted(Comparator.comparing(PedCliente::getTelefono)).collect(Collectors.toList());

			return listaOrdenada;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}

	
	/**
	 * obtiene la lista de todos los productos y la ordena por su fecha.
	 * @return lista ordenada
	 */
	public List<PedCliente> listaOrdenadaPorDía() {
		try (Connection con = new Conector().getMySQLConnection()){
			List<PedCliente> listaOrdenada = new PedClienteManager().obtenerTodosLosPedidos(con).stream()
					.sorted(Comparator.comparing(PedCliente::getFecha)).collect(Collectors.toList());

			return listaOrdenada;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


	/**
	 * obtiene una lista de todos los productos y la ordena por productos.
	 * @return lista ordenada.
	 */
	public List<PedCliente> listaordenadaPorTiposDePanes() {
		try (Connection con = new Conector().getMySQLConnection()){
			List<PedCliente> lista = new PedClienteManager().obtenerTodosLosPedidos(con);
			List<PedCliente>listaOrdenada = new ArrayList<>();
			List<PedCliente>yaEnLaLista = new ArrayList<>();
			List<Producto>productos = new ProductoManager().obtenerProductos(con);
			
			/*for(int i = 0; i < productos.size(); i++) {
				for(int j = 0; j < lista.size(); j++) {
					for(int k = 0; k < lista.get(j).getPedidos().size(); k++) {
						if(lista.get(j).getPedidos().get(k).getCodigoProducto() == productos.get(i).getCodigo()) {
							if(!yaEnLaLista.contains(lista.get(j))) {
								listaOrdenada.add(lista.get(j));
								yaEnLaLista.add(lista.get(j));
								k = lista.get(j).getPedidos().size();
							}
						}
					}
				}
			}*/
			
			//	Tanto la parte comentada como la siguiente hacen lo mismo.
			
			productos.forEach(producto ->{
				lista.forEach(pedido ->{
					pedido.getPedidos().forEach(subPedido ->{
						if(subPedido.getCodigoProducto() == producto.getCodigo() && !yaEnLaLista.contains(pedido)) {
							listaOrdenada.add(pedido);
							yaEnLaLista.add(pedido);
							subPedido = pedido.getPedidos().get(pedido.getPedidos().size() - 1);
						}
					});
				});
			});
	
			

			return listaOrdenada;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	//	EVENTOS
	
	
	/**
	 * Recoge la id del botón que realiza el evento y realiza una acción.
	 * 	<ul>
	 * 		<li>Si la id es 0 se vuelve al inicio.</li>
	 * 		<li>Cualquier otra id llama al método mostrarDatosOrdenados indicando la opción para ordenar los datos.</li>
	 * 	</ul>
	 */
	EventHandler<MouseEvent> eventosBotones = new EventHandler<MouseEvent>() {
		
		@Override
		public void handle(MouseEvent evt) {
			int id = Integer.parseInt(((Button)evt.getSource()).getId());
			
			switch(id) {
			
			case 0:
				try {
					App.setRoot("Inicio");
					
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
				
			case 1:
				mostrarDatosOrdenados(1);
				break;
				
			case 2:
				mostrarDatosOrdenados(2);
				break;
				
			default:
				mostrarDatosOrdenados(3);
					
			
			}
			
		}	
	};

}
