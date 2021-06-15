package grensesnitt;
	
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;

import domene.Billett;
import domene.Film;
import domene.Visning;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import kontroll.Kontroll;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;


public class Main extends Application {
	//@Override
	Kontroll kontroll = new Kontroll();
	private Stage vindu;
	private TableView tabellVisning = new TableView<>();
	public void start(Stage primaryStage) {
		try {
			kontroll.lagForbindelse();
			kontroll.hentBilletter();
			kontroll.hentFilmer();
			kontroll.hentKinosaler();
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
		
		vindu.setWidth(500);
		vindu.setHeight(200);
		
		BorderPane planleggerRotpanel = new BorderPane();
		GridPane planleggerGridpane = new GridPane();
		Scene planleggerScene = new Scene(planleggerRotpanel,600,600);
		
		//Oppretter en knapp for administrasjonsdel:
		Button administrasjon = new Button("Administrasjon");
		planleggerGridpane.add(administrasjon,0,0);
		//administrasjon.setOnAction(e -> lagLoginscene());
		
		//Oppretter en knapp for rapportdel:
		Button rapport = new Button("Rapport");
		planleggerGridpane.add(rapport, 1, 0);
		rapport.setOnAction(e -> lagRapportScene());
		
		Button leggTilFilm = new Button("Legg til en film");
		leggTilFilm.setOnAction(e -> lagNyFilmScene());
		planleggerGridpane.add(leggTilFilm,2,0);
		
		Button leggTilVisning = new Button("Ny visning");
		leggTilVisning.setOnAction(e -> lagNyVisningScene());
		planleggerGridpane.add(leggTilVisning,3,0);
		
		Button tilbake = new Button("Logg ut");
		tilbake.setOnAction(e -> behandleTilbake());
		
		planleggerGridpane.getChildren().addAll();
		
		//GridPane settings
		planleggerGridpane.setHgap(10);
		planleggerRotpanel.setBottom(tilbake);
		planleggerRotpanel.setCenter(planleggerGridpane);
		vindu.setScene(planleggerScene);
		vindu.show();
	}
	
	public void lagRapportScene() {
		BorderPane rapportRotpanel = new BorderPane();
		GridPane rapportGridpane = new GridPane();
		Scene rapportScene = new Scene(rapportRotpanel,600,600);
		
		//Oppretter en knapp for statistikk for film:
		Button statistikkFilm = new Button("Statistikk film");
		rapportGridpane.add(statistikkFilm,0,0);
		//statistikkFilm.setOnAction(e -> sceneNavnHer());
		
		//Oppretter en knapp for statistikk for kinosal:
		Button statistikkKinosal = new Button("Statistikk kinosal");
		rapportGridpane.add(statistikkKinosal, 1, 0);
		//statistikkFilm.setOnAction(e -> sceneNavnHer());
			
		Button tilbake = new Button("Logg ut");
		tilbake.setOnAction(e -> behandleTilbake()); //Opprette ny tilbake funksjon her
		
		rapportGridpane.getChildren().addAll();
		
		//GridPane settings
		rapportGridpane.setHgap(10);
		rapportRotpanel.setBottom(tilbake);
		rapportRotpanel.setCenter(rapportGridpane);
		vindu.setScene(rapportScene);
		vindu.show();
		
		
	}
	
	public void lagNyFilmScene() {
		vindu.setWidth(300);
		vindu.setHeight(200);
		BorderPane nyFilmPanel = new BorderPane();
		Scene nyFilmScene = new Scene(nyFilmPanel,400,400);
		GridPane panel = new GridPane();
		Label lblFilmNavn = new Label("Filmnavn:");
		panel.add(lblFilmNavn, 0, 0);
		TextField txtFilmNavn = new TextField();
		panel.add(txtFilmNavn, 1, 0);
		Button leggTil = new Button("Legg til");
		panel.add(leggTil, 1, 1);
		leggTil.setOnAction(e -> kontroll.leggTilFilm(txtFilmNavn.getText()));
		
		panel.getChildren().addAll();
		nyFilmPanel.setCenter(panel);
		vindu.setScene(nyFilmScene);
		vindu.show();
	}
	
	public void lagNyVisningScene(){
		vindu.setWidth(300);
		vindu.setHeight(200);
		BorderPane nyVisningPanel = new BorderPane();
		Scene nyVisningScene = new Scene(nyVisningPanel,400,400);
		GridPane panel = new GridPane();
		//String filmnr, String kinosalnr, LocalDate dato, String starttid, String pris
		
		Label lblFilmNavn = new Label("Filmnavn");
		panel.add(lblFilmNavn, 0, 0);
		//Henter en choicebox med alle filmer fra kontroll
		ChoiceBox<String> cbxFilmNavn = kontroll.visFilmerChoice();
		panel.add(cbxFilmNavn, 1, 0);
		Label lblKinosal = new Label("Kinosal:");
		panel.add(lblKinosal, 0, 1);
		//Henter en choicebox med alle kinosaler fra kontroll
		ChoiceBox<String> cbxKinosal = kontroll.visKinosalerChoice();
		panel.add(cbxKinosal, 1, 1);
		Label lblDato = new Label("Dato: ");
		panel.add(lblDato, 0, 2);
		DatePicker pcrDato = new DatePicker();
		panel.add(pcrDato, 1, 2);
		Label lblTidspunkt = new Label("Tidspunkt: ");
		panel.add(lblTidspunkt, 0, 3);
		TextField txtTidspunkt = new TextField();
		txtTidspunkt.setPromptText("Eks: 18:00");
		panel.add(txtTidspunkt, 1, 3);
		Label lblPris = new Label("Pris");
		panel.add(lblPris, 0, 4);
		TextField txtPris = new TextField();
		panel.add(txtPris, 1, 4);
		Button leggTil = new Button("Legg til");
		panel.add(leggTil, 1, 5);
		leggTil.setOnAction(e -> {
			int filmNr = kontroll.hentFilmnrFraNavn(cbxFilmNavn.getValue());
			String kinosalNr = cbxKinosal.getValue();
			LocalDate dato = pcrDato.getValue();
			String tidsPunkt = txtTidspunkt.getText();
			String pris = txtPris.getText();
			kontroll.leggTilVisning(String.valueOf(filmNr), kinosalNr, dato, tidsPunkt, pris);
		});
		
		
		panel.getChildren().addAll();
		nyVisningPanel.setCenter(panel);
		vindu.setScene(nyVisningScene);
		vindu.show();
	}
		
	public void lagKinobetjentscene() {
		try {
			vindu.setWidth(400);
			vindu.setHeight(300);
			BorderPane kinobetjentrotpanel = new BorderPane();
			GridPane gridpane = new GridPane();
			Scene kinoscene = new Scene(kinobetjentrotpanel,400,400);
			gridpane.add(new Label("Skriv ned billettkoden:"), 0, 0);
			TextField billettkode = new TextField();
			gridpane.add(billettkode, 1, 0);
			Button oppdater = new Button("Sett som betalt");
			gridpane.add(oppdater, 1, 2);
			Button avbestill = new Button("Slett alle bestillinger");
			avbestill.setOnAction(e -> {lagSlettBillettScene();});
			Button tilbake = new Button("Tilbake");
			tilbake.setOnAction(e -> behandleTilbake());
			oppdater.setOnAction(e -> {
				try {
					settBetalt(billettkode.getText());
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			});
			gridpane.getChildren().addAll();
			kinobetjentrotpanel.setTop(gridpane);
			kinobetjentrotpanel.setBottom(tilbake);
			kinobetjentrotpanel.setCenter(avbestill);
			vindu.setScene(kinoscene);
			vindu.show();
		}catch(Exception e) {System.out.println("nope");}
	}
		
	public void lagSlettBillettScene() {
		vindu.setWidth(600);
		vindu.setHeight(500);
		BorderPane slettBillettRotpanel = new BorderPane();
		Scene slettBestillingScene = new Scene(slettBillettRotpanel,400,400);
		TableView sletttabell = new TableView<>();
		FlowPane knappePanel = new FlowPane();
		TableColumn colBillettkode = new TableColumn("Billettkode:");
		colBillettkode.setMinWidth(100);
		colBillettkode.setCellValueFactory(new PropertyValueFactory<Billett, String>("billettkode"));
		TableColumn colBetalt = new TableColumn("Er betalt:");
		colBetalt.setMinWidth(100);
		colBetalt.setCellValueFactory(new PropertyValueFactory<Billett, Boolean>("erBetalt"));
		sletttabell.getColumns().addAll(colBillettkode, colBetalt);
		sletttabell.setItems(kontroll.hentUbetalteBilletter());
		Button avbestill = new Button("Slett alle bestillinger");
		avbestill.setOnAction(e -> kontroll.slettAlleBestillinger(kontroll.hentUbetalteBilletter()));
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
			
			vindu.setWidth(1000);
			vindu.setHeight(600);
			
			TableColumn visningnr = new TableColumn("Visningnr");
	        visningnr.setMinWidth(50);
	        visningnr.setCellValueFactory(new PropertyValueFactory<Visning, Integer>("visningnr"));

	        TableColumn filmnr = new TableColumn("Filmnrn");
	        filmnr.setMinWidth(50);
	        filmnr.setCellValueFactory(new PropertyValueFactory<Visning, String>("filmnr"));
	        
	        TableColumn filmnavn = new TableColumn("Filmnavn");
	        filmnavn.setMinWidth(150);
	        filmnavn.setCellValueFactory(new PropertyValueFactory<Film, String>("filmnavn"));
	        
	        TableColumn kinosal = new TableColumn("Kinosal");
	        kinosal.setMinWidth(150);
	        kinosal.setCellValueFactory(new PropertyValueFactory<Film, String>("kinosalnr"));

	        TableColumn pris = new TableColumn("Pris");
	        pris.setMinWidth(100);
	        pris.setCellValueFactory(new PropertyValueFactory<Visning, Double>("pris"));

	        TableColumn dato = new TableColumn("Dato");
	        dato.setMinWidth(100);
	        dato.setCellValueFactory(new PropertyValueFactory<Visning, Date>("dato"));
	        
	        TableColumn starttid = new TableColumn("Startid");
	        dato.setMinWidth(100);
	        dato.setCellValueFactory(new PropertyValueFactory<Visning, Time>("starttid")); 

	        tabellVisning.getColumns().addAll(visningnr, filmnr, filmnavn, kinosal, pris, dato, starttid);
	        
	        kontroll.leggInnVisningerIListe();
	        tabellVisning.setItems(kontroll.getVisning());
	        rotpanel.setCenter(tabellVisning);
	        
	        Button tilbake = new Button("Tilbake");
	        tilbake.setOnAction(e -> behandleTilbake());
	        //rotpanel.setRight(tilbake);
	        //vindu.setScene(scene_faktura);
	        
	      //Sok etter visning -------------------------------------------------------
	        FlowPane sokpanel = new FlowPane();
	        TextField sokVisninger = new TextField();
	        sokVisninger.setPromptText("Visningnr ");
	        sokVisninger.setMinWidth(100);

	        Button sokKnapp = new Button("Velg visning");
	        
	        sokKnapp.setOnAction(e -> {
	            try {
	                //Metode for aapne nytt vindu for � se ledige enkeltplasser. Velge/ombestemme plasser. Maa vise totalbelop og antall plasser
	            } catch (Exception exception) { exception.printStackTrace(); }
	        });	
	        
	        rotpanel.setTop(sokpanel);
	        sokpanel.getChildren().addAll(sokVisninger, sokKnapp, tilbake);
	        sokpanel.setHgap(10);
	        
	        vindu.setScene(scene_kundeBestilling);
	        vindu.show();
			
		} catch(Exception e) {e.printStackTrace();}
		}
	
		public void behandleTilbake() {
			lagMenyscene();
		}
		
		public void settBetalt(String billettKode) throws Exception {
			//kontroll.hentBilletter();
			kontroll.settBillettSomBetalt(billettKode);
		}
	
	
	public static void main(String[] args) {
		launch(args);
	}
}
