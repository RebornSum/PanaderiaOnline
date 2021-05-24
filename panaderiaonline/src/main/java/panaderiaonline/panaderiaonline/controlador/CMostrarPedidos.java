package panaderiaonline.panaderiaonline.controlador;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javafx.event.ActionEvent;
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

public class CMostrarPedidos implements Initializable{

	@FXML private VBox ventana;
	private PropiedadesMostrarPedidos datos;
	private VBox seccion2;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		datos = new PropiedadesMostrarPedidos();
		crearSecciones();
		prepararSeccion1((FlowPane)ventana.getChildren().get(0));
		seccion2 = (VBox)ventana.getChildren().get(1);
		mostrarDatosOrdenados(1);
		
		
	}
	

	private void crearSecciones() {
		List<Node>hijosVentana = ventana.getChildren();
		hijosVentana.add(new FlowPane());
		hijosVentana.add(new VBox());
		
	}
	
	
	private void prepararSeccion1(FlowPane panel) {
		panel.setMinHeight(datos.getVMostrarPedidosTamanoSección1());
		List<Node>hijosPanel = panel.getChildren();
		
		for(int i = 0; i < datos.getNumeroBotones(); i++) {
			Button boton = new Button(datos.getTextosBotones().get(i));
			boton.setId("" + i);
			boton.addEventHandler(MouseEvent.MOUSE_CLICKED, eventosBotones);
		}	
	}
	
	
	private void mostrarDatosOrdenados(int opcion) {
		List<PedCliente>listaOrdenada;
		List<Node>hijosSeccion = seccion2.getChildren();
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
		
	}


	private void mostrarPedidos(List<FlowPane> paneles, PedCliente pedido) {
		FlowPane panel = new FlowPane();
		List<Node>hijosPanel = panel.getChildren();
		hijosPanel.add(new Label(datos.getTextosLabels1().get(0)));
		hijosPanel.add(new TextField(pedido.getTelefono()));
		hijosPanel.add(new Label(datos.getTextosLabels1().get(1)));
		hijosPanel.add(new TextField("" + pedido.getFecha()));
		hijosPanel.add(new Label(datos.getTextosLabels1().get(2)));
		hijosPanel.add(new TextField(""+ pedido.getNumPedido()));
		paneles.add(panel);
		pedido.getPedidos().forEach(subPedido -> {
			mostrarSubPedidos(paneles, subPedido);
		});
	}


	private void mostrarSubPedidos(List<FlowPane> paneles, Pedido subPedido) {
		FlowPane subPanel = new FlowPane();
		List<Node>hijosSubPanel = subPanel.getChildren();
		hijosSubPanel.add(new Label(datos.getTextosLabels2().get(0)));
		hijosSubPanel.add(new TextField(subPedido.getProducto().getNombre()));
		hijosSubPanel.add(new Label(datos.getTextosLabels2().get(1)));
		hijosSubPanel.add(new TextField("" + subPedido.getCantidad()));
		paneles.add(subPanel);
	}


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

	//	PENDIENTE DE HACER
	
	public List<PedCliente> listaordenadaPorTiposDePanes() {
		try (Connection con = new Conector().getMySQLConnection()){
			List<PedCliente> lista = new PedClienteManager().obtenerTodosLosPedidos(con);
			List<PedCliente>listaOrdenada = new ArrayList<>();
			List<PedCliente>yaEnLaLista = new ArrayList<>();
			List<Producto>productos = new ProductoManager().obtenerProductos(con);
			
			for(int i = 0; i < productos.size(); i++) {
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
			}
	
			

			return listaOrdenada;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
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
