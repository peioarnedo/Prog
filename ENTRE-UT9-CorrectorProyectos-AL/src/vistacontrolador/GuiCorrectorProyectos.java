
package vistacontrolador;

import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.io.IOException;
import java.util.Iterator;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import modelo.AlumnoNoExistenteExcepcion;
import modelo.CorrectorProyectos;
import modelo.Proyecto;

public class GuiCorrectorProyectos extends Application
{

	
	private MenuItem itemLeer;
	private MenuItem itemGuardar;
	private MenuItem itemSalir;

	private TextField txtAlumno;
	private Button btnVerProyecto;

	private RadioButton rbtAprobados;
	private RadioButton rbtOrdenados;
	private Button btnMostrar;

	private TextArea areaTexto;

	private Button btnClear;
	private Button btnSalir;

	private CorrectorProyectos corrector; // el modelo


	public void start(Stage stage) {

		corrector = new CorrectorProyectos();

		BorderPane root = crearGui();

		Scene scene = new Scene(root, 800, 600);
		stage.setScene(scene);
		stage.setTitle("- Corrector de proyectos -");
		scene.getStylesheets().add(getClass()
		                .getResource("/css/application.css").toExternalForm());
		stage.show();
	}

	private BorderPane crearGui() {

		BorderPane panel = new BorderPane();
		MenuBar barraMenu = crearBarraMenu();
		panel.setTop(barraMenu);

		VBox panelPrincipal = crearPanelPrincipal();
		panel.setCenter(panelPrincipal);

		HBox panelBotones = crearPanelBotones();
		panel.setBottom(panelBotones);

		return panel;
	}

	private MenuBar crearBarraMenu() {

		MenuBar barraMenu = new MenuBar();

		Menu menu = new Menu("Archivo");

		itemLeer = new MenuItem("_Leer de fichero");
		itemLeer.setAccelerator(KeyCombination.keyCombination("CTRL+L"));
		itemLeer.setOnAction(event -> leerDeFichero());
		
		itemGuardar = new MenuItem("_Guardar en fichero");
		itemGuardar.setAccelerator(KeyCombination.keyCombination("CTRL+G"));
		itemGuardar.setDisable(true);
		
		
		itemGuardar.setOnAction(event -> salvarEnFichero());
		
		
		
		itemSalir = new MenuItem("_Salir");
		itemSalir.setAccelerator(KeyCombination.keyCombination("CTRL+S")); 
		itemSalir.setOnAction(event -> salir());
	
		menu.getItems().addAll(itemLeer, itemGuardar, new SeparatorMenuItem(),  itemSalir);
		barraMenu.getMenus().addAll(menu);

		

		return barraMenu;
	}

	private VBox crearPanelPrincipal() {

		VBox panel = new VBox();
		panel.setPadding(new Insets(5));
		panel.setSpacing(10);

		
		Label lblEntrada = new Label("Panel de entrada");
		lblEntrada.getStyleClass().add("titulo-panel");
		lblEntrada.setMaxWidth(Integer.MAX_VALUE);
		


		
		Label lblOpciones = new Label("Panel de opciones");
		lblOpciones.getStyleClass().add("titulo-panel");
		lblOpciones.setMaxWidth(Integer.MAX_VALUE);
		
		
		 areaTexto = new TextArea();
		areaTexto.setPrefHeight(Integer.MAX_VALUE);
		areaTexto.setPrefWidth(Integer.MAX_VALUE);

		
		
		panel.getChildren().addAll(lblEntrada,crearPanelEntrada(), lblOpciones, crearPanelOpciones(),  areaTexto);
		
		return panel;
	}

	private HBox crearPanelEntrada() {

		HBox panel = new HBox();
		panel.setPadding(new Insets(5));
		panel.setSpacing(10);
		
		Label lblAlumno = new Label("Alumno");
		
		 txtAlumno = new TextField();
		
		txtAlumno.setPrefColumnCount(30);
		
		 btnVerProyecto = new Button ("Ver proyecto");
		btnVerProyecto.setPrefWidth(120);
		btnVerProyecto.setOnAction(event -> verProyecto());
		

		panel.getChildren().addAll(lblAlumno,txtAlumno, btnVerProyecto);


		return panel;
	}

	private HBox crearPanelOpciones() {

		HBox panel = new HBox();
		panel.setPadding(new Insets(5));
		panel.setSpacing(50);
		 rbtAprobados = new RadioButton("Mostrar aprobados");
		rbtAprobados.setSelected(true);;
		
		 rbtOrdenados = new RadioButton("Mostrar ordenados");
	
		ToggleGroup grupo = new ToggleGroup();
		rbtAprobados.setToggleGroup(grupo);
		rbtOrdenados.setToggleGroup(grupo);
		
		 btnMostrar = new Button("Mostrar");
		btnMostrar.setPrefWidth(120);
		btnMostrar.setOnAction(event -> mostrar());
		
		
		panel.setAlignment(Pos.CENTER);
		panel.getChildren().addAll(rbtAprobados, rbtOrdenados, btnMostrar);
		return panel;
	}

	private HBox crearPanelBotones() {

		HBox panel = new HBox();
		panel.setPadding(new Insets(5));
		panel.setSpacing(10);
		
		 btnClear = new Button("Clear");
		btnClear.setAlignment(Pos.BOTTOM_CENTER);
		btnClear.setPrefWidth(90);
		btnClear.setOnAction(event -> clear());
		
		 btnSalir = new Button("Salir");
		btnSalir.setAlignment(Pos.BOTTOM_CENTER);
		btnSalir.setPrefWidth(90);
		btnSalir.setOnAction(event -> salir());
		
		panel.getChildren().addAll(btnClear, btnSalir);
		panel.setAlignment(Pos.BOTTOM_RIGHT);
		
		return panel;
	}

	private void salvarEnFichero() {
		try {
			corrector.guardarOrdenadosPorNota();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			areaTexto.setText(e.getMessage());
		}
		


	}

	private void leerDeFichero() {

		itemLeer.setDisable(true);
		itemGuardar.setDisable(false);
		if(itemLeer.isDisable() == true)
		{
			try {
				corrector.leerDatosProyectos();
			} catch (Exception e) {
				areaTexto.setText("Leido fichero de texto\n\n" + e.getMessage() + corrector.getErrores());
				
			}
		}
		
		
		

	}

	private void verProyecto() {
		String alumno = txtAlumno.getText();
		corrector.proyectoDe(alumno);
		if(corrector == null)
		{
			areaTexto.setText("No se han leído todavía los datos del fichero\r\n" + 
					"Vaya a la opción leer del menú");
		}
		else if(alumno == null)
		{
		areaTexto.setText("Teclee nombre de alumno");	
		}
		else 
		{
			try {
				Proyecto proyectoAlumno = corrector.proyectoDe(alumno);
				areaTexto.setText(proyectoAlumno.toString());
			} catch (AlumnoNoExistenteExcepcion e) {
				areaTexto.setText(e.toString());
			}
			finally {
				cogerFoco();
			}
			
			
		}
	}

	private void mostrar() {

		
		if(itemLeer.isDisable() == false)
		{
			areaTexto.setText("No se han leído todavía los datos del fichero\r\n" + 
					"Vaya a la opción leer del menú");
		}
		
		
		else if(rbtAprobados.isSelected())
			{
				areaTexto.setText("Han aprobado el proyecto " +  corrector.aprobados() +   " alumnos/as");
			}
			else
			{
				areaTexto.setText("" + corrector.ordenadosPorNota());
			}
			
	
			
		
		



	}

	private void cogerFoco() {

		txtAlumno.requestFocus();
		txtAlumno.selectAll();

	}

	private void salir() {

		System.exit(0);
	}

	private void clear() {

		areaTexto.clear();
		cogerFoco();
	}

	public static void main(String[] args) {

		launch(args);
	}
}
