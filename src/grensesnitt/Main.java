package grensesnitt;
	
import java.sql.Date;
import java.sql.Time;

import domene.Film;
import domene.Visning;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;


public class Main extends Application {
	//@Override
	private Stage vindu;
	private TableView tabellVisning = new TableView<>();
	public void start(Stage primaryStage) {
		try {
			vindu=primaryStage;
			vindu.setTitle("Kinosentralen");
			vindu.setWidth(800);
			vindu.setHeight(600);
			lagMenyscene();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void lagMenyscene() {
		vindu.setWidth(500);
		vindu.setHeight(200);
		String kinobetjent="kinobetjent";
		String planleggeren="planlegger";
		BorderPane menyrotpanel = new BorderPane();
		FlowPane panel = new FlowPane();
		Scene menyscene = new Scene(menyrotpanel,600,600);
		//Oppretter en knapp for planlegger:
		Button planleggerknapp = new Button("Kinosentralens planlegger");
		panel.getChildren().add(planleggerknapp);
		planleggerknapp.setOnAction(e -> lagLoginscene(planleggeren));
		//Oppretter en knapp for kinobetjent:
		Button kinobetjentknapp = new Button("Kinobetjent");
		kinobetjentknapp.setOnAction(e -> lagLoginscene(kinobetjent));
		panel.getChildren().add(kinobetjentknapp);
		//Oppretter en knapp for kunde:
		Button kundeknapp = new Button("Kunde");
		kundeknapp.setOnAction(e -> lagKundescene());
		panel.getChildren().add(kundeknapp);
		//FlowPane settings
		panel.setHgap(10);
		menyrotpanel.setCenter(panel);
		menyscene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		vindu.setScene(menyscene);
		vindu.show();
	}
	
	public void lagLoginscene(String bruker) {
		vindu.setWidth(300);
		vindu.setHeight(200);
		BorderPane loginrootpanel = new BorderPane();
		GridPane gridpane = new GridPane();
		Scene loginscene = new Scene(loginrootpanel,400,400);
		gridpane.add(new Label("Brukernavn"), 0, 0);
		gridpane.add(new TextField(), 1, 0);
		gridpane.add(new Label("Passord"), 0, 1);
		gridpane.add(new TextField(), 1, 1);
		gridpane.add(new Button("Logg inn"), 1, 2);
		if (bruker=="planlegger") {
			//Planleggeren prøver å logge inn
		}
		else {
			//Kinobetjenten prøver å logge inn
		}
		Button tilbake = new Button("Tilbake");
		tilbake.setOnAction(e -> behandleTilbake());
		gridpane.getChildren().addAll();
		loginrootpanel.setCenter(gridpane);
		loginrootpanel.setBottom(tilbake);
		vindu.setScene(loginscene);
		vindu.show();
	}
	
		public void lagPlanleggerscene() {
			try {
				System.out.println("Test for planlegger");
			}catch(Exception e) {System.out.println("nope");}
		}
		
		
		public void lagKinobetjentscene() {
			try {
				System.out.println("Test for kinobetjent");
				
			}catch(Exception e) {System.out.println("nope");}
		}
			
	public void lagKundescene() {
		try {
			BorderPane rotpanel = new BorderPane();
			Scene scene_kundeBestilling = new Scene(rotpanel,600,600);
			
			vindu.setWidth(900);
			vindu.setHeight(600);
			
			//v_visningnr, v_filmnr, v_pris, v_dato, v_starttid
			
			TableColumn visningnr = new TableColumn("Visningnr");
	        visningnr.setMinWidth(150);
	        visningnr.setCellValueFactory(new PropertyValueFactory<Visning, Integer>("v_visningnr"));

	        TableColumn filmnr = new TableColumn("Filmnrn");
	        filmnr.setMinWidth(150);
	        filmnr.setCellValueFactory(new PropertyValueFactory<Visning, String>("v_filmnr"));
	        
	        TableColumn filmnavn = new TableColumn("Filmnavn");
	        filmnavn.setMinWidth(150);
	        filmnavn.setCellValueFactory(new PropertyValueFactory<Film, String>("f_filmnavn"));

	        TableColumn pris = new TableColumn("Pris");
	        pris.setMinWidth(150);
	        pris.setCellValueFactory(new PropertyValueFactory<Visning, Double>("v_pris"));

	        TableColumn dato = new TableColumn("Dato");
	        dato.setMinWidth(150);
	        dato.setCellValueFactory(new PropertyValueFactory<Visning, Date>("v_dato"));
	        
	        TableColumn starttid = new TableColumn("Startid");
	        dato.setMinWidth(150);
	        dato.setCellValueFactory(new PropertyValueFactory<Visning, Time>("v_starttid"));

	        tabellVisning.getColumns().addAll(visningnr, filmnr, pris, dato, starttid);
			
		} catch(Exception e) {e.printStackTrace();}
		}
	
		public void behandleTilbake() {
			lagMenyscene();
		}
	
	
	public static void main(String[] args) {
		launch(args);
	}
}
