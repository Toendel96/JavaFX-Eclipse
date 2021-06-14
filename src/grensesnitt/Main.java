package grensesnitt;
	
import java.sql.Date;
import java.sql.Time;

import domene.Billett;
import domene.Film;
import domene.Visning;
import javafx.application.Application;
import javafx.stage.Stage;
import kontroll.Kontroll;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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
	Kontroll kontroll = new Kontroll();
	private Stage vindu;
	private TableView tabellVisning = new TableView<>();
	public void start(Stage primaryStage) {
		try {
			kontroll.lagForbindelse();
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
		String planleggeren="planlegger";
		String kinobetjent="kinobetjent";
		BorderPane menyrotpanel = new BorderPane();
		FlowPane panel = new FlowPane();
		Scene menyscene = new Scene(menyrotpanel,600,600);
		//Oppretter en knapp for planlegger:
		Button planleggerknapp = new Button("Kinosentralens planlegger");
		planleggerknapp.setOnAction(e -> lagLoginscene(planleggeren));
		//Oppretter en knapp for kinobetjent:
		Button kinobetjentknapp = new Button("Kinobetjent");
		//kinobetjentknapp.setOnAction(e -> lagLoginscene(kinobetjent));
		kinobetjentknapp.setOnAction(e -> lagKinobetjentscene());
		//Oppretter en knapp for kunde:
		Button kundeknapp = new Button("Kunde");
		kundeknapp.setOnAction(e -> lagKundescene());
		panel.getChildren().addAll(planleggerknapp,kinobetjentknapp,kundeknapp);
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
		TextField brukernavn = new TextField();
		gridpane.add(brukernavn, 1, 0);
		TextField passord = new TextField();
		gridpane.add(passord, 1, 1);
		gridpane.add(new Label("Passord"), 0, 1);
		Button loggInn = new Button("Logg inn");
		gridpane.add(loggInn, 1, 2);
		Button tilbake = new Button("Tilbake");
		tilbake.setOnAction(e -> behandleTilbake());
		loggInn.setOnAction(e -> loggInnBruker(brukernavn.getText(), passord.getText(), bruker));
		gridpane.getChildren().addAll();
		loginrootpanel.setCenter(gridpane);
		loginrootpanel.setBottom(tilbake);
		vindu.setScene(loginscene);
		vindu.show();
	}
	
	

	public void loggInnBruker(String brukernavn, String passord, String bruker) {
		Alert loggInnFeilet = new Alert(AlertType.ERROR);
		if (bruker.equals("planlegger")) {
			if (brukernavn.equals("tunk")) {
				if (passord.equals("4321")) {
					lagPlanleggerscene();
				}else {loggInnFeilet.setContentText("Feil passord");
						loggInnFeilet.show();}
			}else {loggInnFeilet.setContentText("Feil brukernavn");
					loggInnFeilet.show();}
		}else {
			if (brukernavn.equals("knut")) {
				if (passord.equals("1234")) {
					lagKinobetjentscene();
				}else {loggInnFeilet.setContentText("Feil passord");
						loggInnFeilet.show();}
			} else {
					loggInnFeilet.setContentText("Feil brukernavn");
					loggInnFeilet.show();
			}
		} 
	}

	
	public void lagPlanleggerscene() {
		
		BorderPane planleggerRotpanel = new BorderPane();
		FlowPane planleggerFlowpane = new FlowPane();
		Scene planleggerScene = new Scene(planleggerRotpanel,600,600);
		
		//Oppretter en knapp for administrasjonsdel:
		Button administrasjon = new Button("Administrasjon");
		planleggerFlowpane.getChildren().add(administrasjon);
		//administrasjon.setOnAction(e -> lagLoginscene());
		
		//Oppretter en knapp for rapportdel:
		Button rapport = new Button("Rapport");
		//rapport.setOnAction(e -> lagLoginscene());
		planleggerFlowpane.getChildren().add(rapport);
		
		/*Button leggTilFilm = new Button("Legg til en film");
		leggTilFilm.setOnAction(e -> lagNyFilmScene());
		planleggerFlowpane.getChildren().add(leggTilFilm);*/
		
		//FlowPane settings
		planleggerFlowpane.setHgap(10);
		planleggerRotpanel.setCenter(planleggerFlowpane);
		vindu.setScene(planleggerScene);
		vindu.show();
	}
	
	public void lagNyFilmScene() {
		vindu.setWidth(300);
		vindu.setHeight(200);
		BorderPane nyFilmPanel = new BorderPane();
		Scene nyFilmScene = new Scene(nyFilmPanel,400,400);
		FlowPane panel = new FlowPane();
		
		vindu.setScene(nyFilmScene);
		vindu.show();
	}
		
	public void lagKinobetjentscene() {
		try {
			vindu.setWidth(300);
			vindu.setHeight(200);
			BorderPane kinobetjentrotpanel = new BorderPane();
			GridPane gridpane = new GridPane();
			Scene kinoscene = new Scene(kinobetjentrotpanel,400,400);
			gridpane.add(new Label("Skriv ned billettkoden:"), 0, 0);
			TextField billettkode = new TextField();
			gridpane.add(billettkode, 1, 0);
			Button oppdater = new Button("Sett som betalt");
			gridpane.add(oppdater, 1, 2);
			Button avbestill = new Button("Slett alle bestillinger");
			avbestill.setOnAction(e -> lagSlettBillettScene());
			Button tilbake = new Button("Tilbake");
			tilbake.setOnAction(e -> behandleTilbake());
			oppdater.setOnAction(e -> System.out.println("TESTER OM BETALT"));
			gridpane.getChildren().addAll();
			kinobetjentrotpanel.setTop(gridpane);
			kinobetjentrotpanel.setBottom(tilbake);
			kinobetjentrotpanel.setCenter(avbestill);
			vindu.setScene(kinoscene);
			vindu.show();
		}catch(Exception e) {System.out.println("nope");}
	}
		
	public void lagSlettBillettScene(){
		vindu.setWidth(600);
		vindu.setHeight(500);
		BorderPane slettBillettRotpanel = new BorderPane();
		Scene slettBestillingScene = new Scene(slettBillettRotpanel,400,400);
		TableView sletttabell = new TableView<>();
		FlowPane knappePanel = new FlowPane();
		TableColumn colBillettkode = new TableColumn("Billettkode:");
		colBillettkode.setMinWidth(100);
		colBillettkode.setCellValueFactory(new PropertyValueFactory<Billett, String>("b_billettkode"));
		TableColumn colBetalt = new TableColumn("Er betalt:");
		colBetalt.setMinWidth(100);
		colBetalt.setCellValueFactory(new PropertyValueFactory<Billett, Boolean>("b_erBetalt"));
		sletttabell.getColumns().addAll(colBillettkode, colBetalt);
		sletttabell.setItems(kontroll.hentUbetalteBilletter());
		Button avbestill = new Button("Slett alle bestillinger");
		//avbestill.setOnAction(e -> kontroll.slettAlleBestillinger());
		Button tilbake = new Button("Tilbake");
		tilbake.setOnAction(e -> behandleTilbake());
		knappePanel.getChildren().addAll(tilbake, avbestill);
		knappePanel.setHgap(10);
		slettBillettRotpanel.setCenter(sletttabell);
		slettBillettRotpanel.setBottom(knappePanel);
		vindu.setScene(slettBestillingScene);
		vindu.show();
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
