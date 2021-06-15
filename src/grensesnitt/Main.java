package grensesnitt;
	
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import domene.Billett;
import domene.Film;
import domene.Plassbillett;
import domene.Plass;
import domene.Visning;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import kontroll.Kontroll;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
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

	private TableView<List<String>> statistikklinjer = new TableView();

	private TableView sletttabell = new TableView<>();
	
	private Scene menyscene;
	private Scene kinoscene;
	private Scene rapportScene;
	private Scene filmStatistikkScene;
	private Scene kinoStatistikkScene;
	private Scene scene_kundeBestilling;
	private Scene loginscene;
	private Scene planleggerScene;
	private Scene nyFilmScene;
	private Scene nyVisningScene;
	private Scene ledigePlasserScene;

	public void start(Stage primaryStage) {
		try {
			vindu=primaryStage;
			kontroll.lagForbindelse();
			//Kaller p� metoder som henter ut data fra database og legger det i lister
			kontroll.hentBilletter();
			kontroll.hentFilmer();
			kontroll.hentKinosaler();
			kontroll.leggInnVisningerIListe();
			kontroll.hentPlasser();
			kontroll.hentPlassbilletter(); //getPlassbillett() for aa faa plassbilletter sin liste
			//kontroll.slettinnholdAlleTabeller();
			vindu.setTitle("Kinosentralen");
			vindu.setWidth(800);
			vindu.setHeight(600);
			lagKundescene();
			lagStatistikkKinosal();
			lagStatistikkFilm();
			lagMenyscene();
			lagKinobetjentscene();
			

		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void lagMenyscene() {
		vindu.setWidth(800);
		vindu.setHeight(800);
		String planleggeren="planlegger";
		String kinobetjent="kinobetjent";
		BorderPane menyrotpanel = new BorderPane();
		FlowPane panel = new FlowPane();
		menyscene = new Scene(menyrotpanel,600,600);
		//Oppretter en knapp for planlegger:
		Button planleggerknapp = new Button("Kinosentralens planlegger");
		planleggerknapp.setOnAction(e -> lagLoginscene(planleggeren));
		//Oppretter en knapp for kinobetjent:
		Button kinobetjentknapp = new Button("Kinobetjent");
		//kinobetjentknapp.setOnAction(e -> lagLoginscene(kinobetjent));
		kinobetjentknapp.setOnAction(e -> vindu.setScene(kinoscene));
		//Oppretter en knapp for kunde:
		Button kundeknapp = new Button("Kunde");
		kundeknapp.setOnAction(e -> vindu.setScene(scene_kundeBestilling));
		//Avsluttknapp
		Button avslutt = new Button("Avslutt");
		avslutt.setOnAction(e -> {stop();});
		panel.getChildren().addAll(planleggerknapp,kinobetjentknapp,kundeknapp,avslutt);
		//FlowPane settings
		panel.setHgap(10);
		menyrotpanel.setCenter(panel);
		menyscene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		vindu.setScene(menyscene);
		vindu.show();
	}
	
	public void lagLoginscene(String bruker) {
		BorderPane loginrootpanel = new BorderPane();
		GridPane gridpane = new GridPane();
		loginscene = new Scene(loginrootpanel,400,400);
		gridpane.add(new Label("Brukernavn"), 0, 0);
		TextField brukernavn = new TextField();
		gridpane.add(brukernavn, 1, 0);
		TextField passord = new TextField();
		gridpane.add(passord, 1, 1);
		gridpane.add(new Label("Passord"), 0, 1);
		Button loggInn = new Button("Logg inn");
		gridpane.add(loggInn, 1, 2);
		Button tilbake = new Button("Tilbake");
		tilbake.setOnAction(e -> behandleTilbake(menyscene));
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
		GridPane planleggerGridpane = new GridPane();
		planleggerScene = new Scene(planleggerRotpanel,600,600);
		
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
		tilbake.setOnAction(e -> behandleTilbake(menyscene));
		
		planleggerGridpane.getChildren().addAll();
		
		//GridPane settings
		planleggerGridpane.setHgap(10);
		planleggerRotpanel.setBottom(tilbake);
		planleggerRotpanel.setCenter(planleggerGridpane);
		
	}
	
	public void lagRapportScene() {
		BorderPane rapportRotpanel = new BorderPane();
		GridPane rapportGridpane = new GridPane();
		rapportScene = new Scene(rapportRotpanel,600,600);
		
		//Oppretter en knapp for statistikk for film:
		Button statistikkFilm = new Button("Statistikk film");
		rapportGridpane.add(statistikkFilm,0,0);
		statistikkFilm.setOnAction(e -> vindu.setScene(filmStatistikkScene));
		
		//Oppretter en knapp for statistikk for kinosal:
		Button statistikkKinosal = new Button("Statistikk kinosal");
		rapportGridpane.add(statistikkKinosal, 1, 0);
		statistikkKinosal.setOnAction(e -> vindu.setScene(kinoStatistikkScene));
			
		Button tilbake = new Button("Logg ut");
		tilbake.setOnAction(e -> behandleTilbake(menyscene)); //Opprette ny tilbake funksjon her
		
		rapportGridpane.getChildren().addAll();
		
		//GridPane settings
		rapportGridpane.setHgap(10);
		rapportRotpanel.setBottom(tilbake);
		rapportRotpanel.setCenter(rapportGridpane);
		
		vindu.setScene(rapportScene);
		vindu.show();
	
	}
	
	public void lagStatistikkFilm() {
		BorderPane filmStatistikkPanel = new BorderPane();
		GridPane filmStatistikkGrid = new GridPane();

		FlowPane valgpanel = new FlowPane();

		filmStatistikkScene = new Scene(filmStatistikkPanel, 800, 400);		

		statistikklinjer = new TableView();
		
		filmStatistikkPanel.setCenter(statistikklinjer); 
		filmStatistikkPanel.setTop(filmStatistikkGrid);
		filmStatistikkPanel.setBottom(valgpanel);
		
		//Bygger opp innholdet i tabellen:
		TableColumn colVisningnr = new TableColumn("Visningnr:");
		colVisningnr.setMinWidth(150);	
		//colVisningnr.setCellValueFactory(new PropertyValueFactory< , >("visningnr"));
		
		TableColumn colAntallSett = new TableColumn("Antall sett:");
		colAntallSett.setMinWidth(150);	
		//colAntallSett.setCellValueFactory(new PropertyValueFactory< , >(""));
		
		TableColumn colSalprosent = new TableColumn("Prosent kinosal:");
		colSalprosent.setMinWidth(150);	
		//colSalprosent.setCellValueFactory(new PropertyValueFactory< , >(""));
		
		TableColumn colSlettet = new TableColumn("Bestilling slettet:");
		colSlettet.setMinWidth(150);	
		//colSlettet.setCellValueFactory(new PropertyValueFactory< , >(""));
		
		statistikklinjer.getColumns().addAll(colVisningnr, colAntallSett, colSalprosent, colSlettet);
		//statistikklinjer.setItems();		
		
		Label lblFilmnr = new Label("Filmnummer: ");
		filmStatistikkGrid.add(lblFilmnr, 2, 1);
		TextField filmSok = new TextField();
		filmStatistikkGrid.add(filmSok, 3, 1);
		Button btnFinnFilm = new Button("Finn film");
		filmStatistikkGrid.add(btnFinnFilm, 2, 2);
		//btnFinnFilm.setOnAction(e-> kontroll.behandleFinnFilm(filmSok.getText()));
		Button btnTilbake = new Button("Tilbake");
		btnTilbake.setOnAction(e-> behandleTilbake(menyscene));
		
		valgpanel.getChildren().addAll(btnTilbake);
		//vindu.setWidth(600);
		//vindu.setHeight(500);
		vindu.setScene(filmStatistikkScene);
		vindu.show();	
	}
	
	
	public void lagStatistikkKinosal() {
		BorderPane kinoStatistikkPanel = new BorderPane();
		GridPane kinoStatistikkGrid = new GridPane();

		FlowPane valgpanel = new FlowPane();

		kinoStatistikkScene = new Scene(kinoStatistikkPanel, 800, 400);		

		statistikklinjer = new TableView();
		
		kinoStatistikkPanel.setCenter(statistikklinjer);
		kinoStatistikkPanel.setTop(kinoStatistikkGrid);
		kinoStatistikkPanel.setBottom(valgpanel);
		
		//Bygger opp innholdet i tabellen:
		TableColumn colAntallVisninger = new TableColumn("Antall visninger:");
		colAntallVisninger.setMinWidth(150);	
		//colAntallVisninger.setCellValueFactory(new PropertyValueFactory<, >("visningnr"));
		
		TableColumn colProsentSolgt = new TableColumn("Prosent solgt:");
		colProsentSolgt.setMinWidth(150);	
		//colAntallSett.setCellValueFactory(new PropertyValueFactory< , >(""));
		
		statistikklinjer.getColumns().addAll(colAntallVisninger, colProsentSolgt);
		//statistikklinjer.setItems(); 		
		
		Label lblKinosal = new Label("Kinosal: ");
		kinoStatistikkGrid.add(lblKinosal, 2, 1);
		TextField kinoSok = new TextField();
		kinoStatistikkGrid.add(kinoSok, 3, 1);
		Button btnFinnKino = new Button("Finn kino");
		kinoStatistikkGrid.add(btnFinnKino, 2, 2);
		btnFinnKino.setOnAction(e-> {
			try {
				kontroll.kinoStatistikk(kinoSok.getText());
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		Button btnTilbake = new Button("Tilbake");
		btnTilbake.setOnAction(e-> behandleTilbake(menyscene));
		
		valgpanel.getChildren().addAll(btnTilbake);
		vindu.setWidth(300);
		vindu.setHeight(500);

		vindu.setScene(kinoStatistikkScene);
		vindu.show();
	}
	
	public void lagNyFilmScene() {
		//vindu.setWidth(300);
		//vindu.setHeight(200);
		BorderPane nyFilmPanel = new BorderPane();
		nyFilmScene = new Scene(nyFilmPanel,400,400);
		GridPane panel = new GridPane();
		Label lblFilmNavn = new Label("Filmnavn:");
		panel.add(lblFilmNavn, 0, 0);
		TextField txtFilmNavn = new TextField();
		panel.add(txtFilmNavn, 1, 0);
		Button leggTil = new Button("Legg til");
		panel.add(leggTil, 1, 1);
		leggTil.setOnAction(e -> kontroll.leggTilFilm(txtFilmNavn.getText()));
		Button tilbake = new Button("Tilbake");
		panel.add(tilbake, 0, 6);
		tilbake.setOnAction(e -> lagPlanleggerscene());
		
		panel.getChildren().addAll();
		nyFilmPanel.setCenter(panel);
		nyFilmPanel.setBottom(tilbake);
	}
	
	public void lagNyVisningScene(){
		//vindu.setWidth(300);
		//vindu.setHeight(250);
		BorderPane nyVisningPanel = new BorderPane();
		nyVisningScene = new Scene(nyVisningPanel,400,400);
		GridPane panel = new GridPane();
		
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
		Label lblKr = new Label("Kr");
		panel.add(lblKr, 2, 4);
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
		Button tilbake = new Button("Tilbake");
		panel.add(tilbake, 0, 6);
		tilbake.setOnAction(e -> lagPlanleggerscene());
		panel.getChildren().addAll();
		nyVisningPanel.setCenter(panel);
		nyVisningPanel.setBottom(tilbake);
	}
	
	public void lagLedigePlasserVisning(String visningsnr, int kinosalnr) {
		BorderPane ledigePlasserPanel = new BorderPane();
		ledigePlasserScene = new Scene(ledigePlasserPanel,400,400);
		FlowPane comboBoxPanel = new FlowPane();
		ComboBox comboBoxrad = kontroll.hentrader();
		Label radnr= new Label();
		comboBoxrad.setOnAction((e) -> {radnr.setText(comboBoxrad.getValue().toString()); });
		comboBoxrad.setPromptText("Velg rad");
		ComboBox comboBoxsete = kontroll.hentseter(radnr.getText());
		comboBoxsete.setPromptText("Velg sete");
		Button tilbake = new Button("Tilbake");
		tilbake.setOnAction(e -> behandleTilbake(scene_kundeBestilling));
		comboBoxPanel.getChildren().addAll(tilbake, comboBoxrad,comboBoxsete);
		comboBoxPanel.setHgap(10);
		ledigePlasserPanel.setTop(comboBoxPanel);
		vindu.setScene(ledigePlasserScene);
		vindu.show();
	}
		
	public void lagKinobetjentscene() {
		vindu.setWidth(600);
		vindu.setHeight(500);
		BorderPane kinorotpanel = new BorderPane();
		kinoscene = new Scene(kinorotpanel,400,400);
		FlowPane knappePanel = new FlowPane();
		TableColumn colBillettkode = new TableColumn("Billettkode:");
		colBillettkode.setMinWidth(100);
		colBillettkode.setCellValueFactory(new PropertyValueFactory<Billett, String>("billettkode"));
		TableColumn colBetalt = new TableColumn("Er betalt:");
		colBetalt.setMinWidth(100);
		colBetalt.setCellValueFactory(new PropertyValueFactory<Billett, Boolean>("erBetalt"));
		sletttabell.getColumns().addAll(colBillettkode, colBetalt);
		Button avbestill = new Button("Slett alle bestillinger");
		avbestill.setOnAction(e -> knappBehandleAvbestill());
		Button tilbake = new Button("Tilbake");
		tilbake.setOnAction(e -> behandleTilbake(menyscene));
		TextField billettkode = new TextField();
		billettkode.setPromptText("Skriv inn billettnr:");
		Button oppdater = new Button("Sett som betalt");
		oppdater.setOnAction(e -> {knappBehandleSettBetalt(billettkode.getText());});
		sletttabell.setItems(kontroll.hentUbetalteBilletter());
		knappePanel.getChildren().addAll(tilbake,billettkode,oppdater,avbestill);
		knappePanel.setHgap(10);
		kinorotpanel.setCenter(sletttabell);
		kinorotpanel.setBottom(knappePanel);
	}
	
	public void lagKundescene() {
		try {
			BorderPane rotpanel = new BorderPane();
			scene_kundeBestilling = new Scene(rotpanel,600,600);
			//vindu.setWidth(1000);
			//vindu.setHeight(600);
			TableColumn visningnr = new TableColumn("Visningnr");
	        visningnr.setMinWidth(50);
	        visningnr.setCellValueFactory(new PropertyValueFactory<Visning, Integer>("visningnr"));
	        TableColumn pris = new TableColumn("Pris");
	        pris.setMinWidth(100);
	        pris.setCellValueFactory(new PropertyValueFactory<Visning, Double>("pris"));
	        TableColumn dato = new TableColumn("Dato");
	        dato.setMinWidth(100);
	        dato.setCellValueFactory(new PropertyValueFactory<Visning, Date>("dato"));
	        TableColumn starttid = new TableColumn("Startid");
	        starttid.setMinWidth(100);
	        starttid.setCellValueFactory(new PropertyValueFactory<Visning, Time>("starttid")); 
	        TableColumn filmnr = new TableColumn("Filmnrn");
	        filmnr.setMinWidth(50);
	        filmnr.setCellValueFactory(new PropertyValueFactory<Visning, String>("filmnr"));
	        TableColumn filmnavn = new TableColumn("Filmnavn");
	        filmnavn.setMinWidth(150);
	        filmnavn.setCellValueFactory(new PropertyValueFactory<Film, String>("filmnavn"));
	        TableColumn kinosal = new TableColumn("Kinosal");
	        kinosal.setMinWidth(150);
	        kinosal.setCellValueFactory(new PropertyValueFactory<Object, String>("kinosalnr"));

	        tabellVisning.getColumns().addAll(visningnr, pris, dato, starttid, filmnr, filmnavn, kinosal);
	        
	        tabellVisning.setItems(kontroll.getVisning());
	        rotpanel.setCenter(tabellVisning);
	        
	        Button tilbake = new Button("Tilbake");
	        tilbake.setOnAction(e -> behandleTilbake(menyscene));
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
	            	//Metode for aapne nytt vindu for � se ledige enkeltplasser. Velge/ombestemme plasser. 
	            	//Maa vise totalbelop og antall plasser
	            	int hentetKinosalnr = kontroll.finnKinosalnrBasertPaaVisningsnr(sokVisninger.getText());
	            	lagLedigePlasserVisning(sokVisninger.getText(), hentetKinosalnr);
	            } catch (Exception exception) { exception.printStackTrace(); }
	        });	
	        
	        rotpanel.setTop(sokpanel);
	        sokpanel.getChildren().addAll(sokVisninger, sokKnapp, tilbake);
	        sokpanel.setHgap(10);
	        
			
		} catch(Exception e) {e.printStackTrace();}
	}
	
	public void knappBehandleAvbestill(){
		kontroll.slettAlleBilletter(kontroll.hentUbetalteBilletter());
		sletttabell.getItems().clear(); 
		sletttabell.setItems(kontroll.hentUbetalteBilletter());
	}
	
	public void knappBehandleSettBetalt(String billettkode) {
		kontroll.settBillettSomBetalt(billettkode);
		sletttabell.getItems().clear();
		sletttabell.setItems(kontroll.hentUbetalteBilletter());
	}
	
	public void behandleTilbake(Scene scene) {
		vindu.setScene(scene);
	}
	
	public void settStorrelse(Scene scene, int bredde, int hoyde) {
		//
	}
	
	public void settBetalt(String billettKode) throws Exception {
		//kontroll.hentBilletter();
		kontroll.settBillettSomBetalt(billettKode);
	}
	
	@Override
	//Ref javadoc: https://docs.oracle.com/javase/8/javafx/api/javafx/application/Application.html
	public void stop() {
		try {
			kontroll.slettinnholdAlleTabeller();
			kontroll.lagreFilmDB();
			kontroll.lagreKinosalDB();
			kontroll.lagrePlassDB();
			kontroll.lagreVisningDB();
			kontroll.lagreBillettDB();
			kontroll.lagrePlassBillett();
			kontroll.lukk();
			Platform.exit();
		}catch(Exception e) {
	}
		
	}
	
	
	public static void main(String[] args) {
		launch(args);
	}
}
