package grensesnitt;
	
import static javax.swing.JOptionPane.showMessageDialog;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.mysql.cj.conf.StringProperty;

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
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;


public class Main extends Application {
	//@Override
	Kontroll kontroll = new Kontroll();
	private Stage vindu;
	private TableView tabellVisning = new TableView<>();

	private TableView statistikkTabell = new TableView();
	
	
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
	private Scene registrerBillettKBScene;
	private Scene sceneOppdatering;
	private Scene adminScene;
	private String radnr;

	public void start(Stage primaryStage) {
		try {
			vindu=primaryStage;
			kontroll.lagForbindelse();
			//Kaller pï¿½ metoder som henter ut data fra database og legger det i lister
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
			lagRapportScene();
			lagStatistikkFilm();
			lagAdminScene();
			lagPlanleggerscene();
			lagNyFilmScene();
			lagNyVisningScene();
			lagMenyscene();
			lagKinobetjentscene();
			lagOppdaterScene();
			registrerBillettKBScene();
			kontroll.lesslettingerfrafil();

		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	/** Kodet av 7104, kontrollert og godkjent av 7085 */
	public void lagMenyscene() {
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
		kinobetjentknapp.setOnAction((e) -> {
			vindu.setScene(kinoscene);
			});
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
	/** Kodet av 7104, kontrollert og godkjent av 7085 */
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
		gridpane.setHgap(5);
		gridpane.setVgap(10);
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
	/** Kodet av 7074, kontrollert og godkjent av 7079 */
	public void loggInnBruker(String brukernavn, String passord, String bruker) {
		Alert loggInnFeilet = new Alert(AlertType.ERROR);
		if (bruker.equals("planlegger")) {
			if (brukernavn.equals("tunk")) {
				if (passord.equals("4321")) {
					vindu.setScene(planleggerScene);
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
	/** Kodet av 7079, kontrollert og godkjent av 7088  */
	public void lagPlanleggerscene() {
		BorderPane planleggerRotpanel = new BorderPane();
		GridPane planleggerGridpane = new GridPane();
		planleggerScene = new Scene(planleggerRotpanel,600,600);
		
		//Oppretter en knapp for administrasjonsdel:
		Button administrasjon = new Button("Administrasjon");
		planleggerGridpane.add(administrasjon,0,0);
		administrasjon.setOnAction(e -> vindu.setScene(adminScene));
		
		//Oppretter en knapp for rapportdel:
		Button rapport = new Button("Rapport");
		planleggerGridpane.add(rapport, 1, 0);
		rapport.setOnAction(e -> vindu.setScene(rapportScene));
		
		Button tilbake = new Button("Logg ut");
		tilbake.setOnAction(e -> behandleTilbake(menyscene));
		
		planleggerGridpane.getChildren().addAll();
		
		//GridPane settings
		planleggerGridpane.setHgap(10);
		planleggerRotpanel.setBottom(tilbake);
		planleggerRotpanel.setCenter(planleggerGridpane);
	}
	
	public void lagAdminScene() {
		BorderPane adminRotpanel = new BorderPane();
		GridPane adminGridpane = new GridPane();
		adminScene = new Scene(adminRotpanel,600,600);
		
		Button leggTilFilm = new Button("Legg til en film");
		leggTilFilm.setOnAction(e -> vindu.setScene(nyFilmScene));
		adminGridpane.add(leggTilFilm,0,0);
		
		Button leggTilVisning = new Button("Ny visning");
		leggTilVisning.setOnAction(e -> vindu.setScene(nyVisningScene));
		adminGridpane.add(leggTilVisning,1,0);
		
		//Oppretter en knapp for Oppdater info:
		Button oppdaterInformasjon = new Button("Oppdater informasjon");
		adminGridpane.add(oppdaterInformasjon,2,0);
		//administrasjon.setOnAction(e -> lagLoginscene());
		
		oppdaterInformasjon.setOnAction(e -> {
        	try {
        		vindu.setScene(sceneOppdatering);
        	} catch (Exception exception) { exception.printStackTrace(); }
        });
			
		Button tilbake = new Button("Logg ut");
		tilbake.setOnAction(e -> behandleTilbake(menyscene)); //Opprette ny tilbake funksjon her
		
		adminGridpane.getChildren().addAll();
		
		//GridPane settings
		adminGridpane.setHgap(10);
		adminRotpanel.setBottom(tilbake);
		adminRotpanel.setCenter(adminGridpane);
		
		
	}
	/** Kodet av 7085, kontrollert og godkjent av 7074 */
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
		tilbake.setOnAction(e -> behandleTilbake(planleggerScene));
		
		rapportGridpane.getChildren().addAll();
		
		//GridPane settings
		rapportGridpane.setHgap(10);
		rapportRotpanel.setBottom(tilbake);
		rapportRotpanel.setCenter(rapportGridpane);
		
		
	
	}
	/** Kodet av 7085, kontrollert og godkjent av 7088  */
	public void lagStatistikkFilm() {
		BorderPane filmStatistikkPanel = new BorderPane();
		filmStatistikkScene = new Scene(filmStatistikkPanel, 800, 400);						
		FlowPane valgpanel = new FlowPane();
		TextField filmSok = new TextField();
		Label lblFilmStatistikk = new Label(kontroll.getStatistikkFilm(filmSok.getText()));
		VBox layout1 = new VBox(20);
		layout1.getChildren().addAll(lblFilmStatistikk);
		filmStatistikkPanel.setCenter(layout1);
		filmSok.setPromptText("Filmnr");
		filmSok.setMinWidth(100);
		Button sokKnapp = new Button ("Hent statistikk");
		sokKnapp.setOnAction(e-> lblFilmStatistikk.setText(kontroll.getStatistikkFilm(filmSok.getText())));
		Button btnTilbake = new Button("Tilbake");
		btnTilbake.setOnAction(e-> behandleTilbake(planleggerScene));
		valgpanel.getChildren().addAll(filmSok,sokKnapp,btnTilbake);
		filmStatistikkPanel.setTop(valgpanel);
	}
	
	/** Kodet av 7085, kontrollert og godkjent av 7088  */
	public void lagStatistikkKinosal() {
		BorderPane kinoStatistikkPanel = new BorderPane();
		kinoStatistikkScene = new Scene(kinoStatistikkPanel, 800, 400);	
		TextField kinoSok = new TextField();
		Label lblKinoStatistikk = new Label(kontroll.getStatistikkKino(kinoSok.getText()));
		VBox layout1 = new VBox(20);
		layout1.getChildren().addAll(lblKinoStatistikk);
		kinoStatistikkPanel.setCenter(layout1);
		FlowPane valgpanel = new FlowPane();
		kinoSok.setPromptText("Kinosalnr");
		kinoSok.setMinWidth(100);
		Button sokKnapp = new Button ("Hent statistikk");
		sokKnapp.setOnAction(e-> lblKinoStatistikk.setText(kontroll.getStatistikkKino(kinoSok.getText())));
		Button btnTilbake = new Button("Tilbake");
		btnTilbake.setOnAction(e-> behandleTilbake(planleggerScene));
		valgpanel.getChildren().addAll(kinoSok,sokKnapp,btnTilbake);
		kinoStatistikkPanel.setTop(valgpanel);
		
	}
	/** Kodet av 7079, kontrollert og godkjent av 7088 */
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
		tilbake.setOnAction(e -> behandleTilbake(planleggerScene));
		
		panel.setHgap(5);
		panel.setVgap(5);
		
		panel.getChildren().addAll();
		nyFilmPanel.setCenter(panel);
		nyFilmPanel.setBottom(tilbake);
	}
	/** Kodet av 7079, kontrollert og godkjent av 7088 */
	public void lagNyVisningScene(){
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
		
		panel.setHgap(5);
		panel.setVgap(5);
		
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
		tilbake.setOnAction(e -> behandleTilbake(planleggerScene));
		panel.getChildren().addAll();
		nyVisningPanel.setCenter(panel);
		nyVisningPanel.setBottom(tilbake);
	}
	/** Kodet av 7079, kontrollert og godkjent av 7074 */
	public void lagLedigePlasserVisning(String visningsnr, int kinosalnr, boolean erKinoBetjent) {
		BorderPane ledigePlasserPanel = new BorderPane();
		ledigePlasserScene = new Scene(ledigePlasserPanel,400,400);
		FlowPane comboBoxPanel = new FlowPane();
		FlowPane bunnpanel = new FlowPane();
		ComboBox comboBoxrad = kontroll.hentrader(visningsnr,kinosalnr);
		comboBoxrad.setPromptText("Velg rad");
		Button tilbake = new Button("Tilbake");
		
		tilbake.setOnAction(e -> {
        	try {
        		kontroll.setTempreservasjonNull();
        		behandleTilbake(scene_kundeBestilling);
        	} catch (Exception exception) { exception.printStackTrace(); }
        });
		
		Button leggtil = new Button("Legg til");
		Button fjern = new Button("Fjern");
		Button reserver = new Button("Reserver");
		//reserver.setOnAction(e -> behandleTilbake(scene_kundeBestilling));
		Label tekstpris= new Label("Totalpris:");
		TextField totalpris= new TextField();
		totalpris.setMaxWidth(100);
		totalpris.setEditable(false);
		ComboBox<String> comboboxsete= new ComboBox();
		ComboBox<Plass> comboboxplass= new ComboBox();
		comboboxsete.setPromptText("Velg sete");
		comboBoxrad.setOnAction((e) -> { radnr= comboBoxrad.getValue().toString();
		ObservableList<String> seter=kontroll.hentseter(visningsnr,radnr, kinosalnr).getItems();
		 comboboxsete.setItems(seter);
		 });
		leggtil.setOnAction((e) -> {
			comboboxplass.setItems(kontroll.leggTilSete(comboboxsete.getValue().toString(),comboBoxrad.getValue().toString(),kinosalnr));
			String totalAaBetale=kontroll.regnutpris(visningsnr);
			totalpris.setText(totalAaBetale);
		});
		fjern.setOnAction((e) -> {
			comboboxplass.setItems(kontroll.fjernplass(comboboxplass.getValue().getRadnr(),comboboxplass.getValue().getSetenr()));
		String totalAaBetale=kontroll.regnutpris(visningsnr);
		totalpris.setText(totalAaBetale);
		});
		
		
		reserver.setOnAction(e -> {
        	try {
        		boolean status = kontroll.giBestillingBekreftelse(visningsnr,erKinoBetjent);
        		if (status) behandleTilbake(menyscene);
        	} catch (Exception exception) { exception.printStackTrace(); }
        });
		
		comboBoxPanel.getChildren().addAll(tilbake, comboBoxrad,comboboxsete,leggtil);
		comboBoxPanel.setHgap(10);
		Region tomt= new Region();
		tomt.setMinWidth(100);
		bunnpanel.setHgap(5);
		bunnpanel.getChildren().addAll(comboboxplass,fjern,tomt, reserver,tekstpris,totalpris);
		ledigePlasserPanel.setTop(comboBoxPanel);
		ledigePlasserPanel.setBottom(bunnpanel);
		vindu.setScene(ledigePlasserScene);
		vindu.show();
	}
	/** Kodet av 7074, kontrollert og godkjent av 7079 */
	public void hentseter(String visningsnr,int kinosalnr, String radnr, Button tilbake, ComboBox comboBoxrad, FlowPane comboBoxPanel) {
		System.out.println("radnummer: " + radnr);
		ComboBox comboBoxsete = kontroll.hentseter(visningsnr,radnr, kinosalnr);
		comboBoxsete.setPromptText("Velg sete");
		comboBoxPanel.getChildren().addAll(comboBoxsete);
		
	}
	/** Kodet av 7074, kontrollert og godkjent av 7104 */
	public void lagKinobetjentscene() {
		BorderPane kinorotpanel = new BorderPane();
		kinoscene = new Scene(kinorotpanel,400,400);
		FlowPane knappePanel = new FlowPane();
		
		Button registrerBilletter = new Button("Registrer billetter");
		registrerBilletter.setOnAction(e -> vindu.setScene(registrerBillettKBScene));
		
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
		
		tilbake.setOnAction(e -> {
        	try {
        		behandleTilbake(menyscene);
        	} catch (Exception exception) { exception.printStackTrace(); }
        });
		
		TextField billettkode = new TextField();
		billettkode.setPromptText("Skriv inn billettnr:");
		Button oppdater = new Button("Sett som betalt");
		oppdater.setOnAction(e -> {knappBehandleSettBetalt(billettkode.getText());});
		sletttabell.setItems(kontroll.hentUbetalteBilletter());
		knappePanel.getChildren().addAll(tilbake,billettkode,oppdater,avbestill);
		knappePanel.setHgap(10);
		kinorotpanel.setTop(registrerBilletter);
		kinorotpanel.setCenter(sletttabell);
		kinorotpanel.setBottom(knappePanel);
	}
	/** Kodet av 7079, kontrollert og godkjent av 7104 */
	public void registrerBillettKBScene() {
		//Kopi av lagKundeScene
		BorderPane rotpanel = new BorderPane();
		registrerBillettKBScene = new Scene(rotpanel,600,600);
		
		Label label1= new Label(
                kontroll.getFormattertString1()
        		);
        VBox layout1 = new VBox(20);
        layout1.getChildren().addAll(label1);
        rotpanel.setCenter(layout1);
        
        Button tilbake = new Button("Tilbake");
        
        tilbake.setOnAction(e -> {
        	try {
        		kontroll.setTempreservasjonNull();
        		behandleTilbake(menyscene);
        	} catch (Exception exception) { exception.printStackTrace(); }
        });
        
        Button standard = new Button("Standard");
        Button sorterFilm = new Button("Sorter: film");
        Button sorterTidspunkt = new Button("Sorter: tidspunkt");
        
        standard.setOnAction(e -> {
        	try {
        		label1.setText(kontroll.getFormattertString1());
        	} catch (Exception exception) { exception.printStackTrace(); }
        });
        
        sorterFilm.setOnAction(e -> {
        	try {
        		label1.setText(kontroll.getFormattertString2());
        	} catch (Exception exception) { exception.printStackTrace(); }
        });
        
        sorterTidspunkt.setOnAction(e -> {
        	try {
        		label1.setText(kontroll.getFormattertString3());
        	} catch (Exception exception) { exception.printStackTrace(); }
        });
        
        tilbake.setOnAction(e -> behandleTilbake(menyscene));
        
      //Sok etter visning -------------------------------------------------------
        FlowPane sokpanel = new FlowPane();
        TextField sokVisninger = new TextField();
        sokVisninger.setPromptText("Visningnr ");
        sokVisninger.setMinWidth(100);

        Button sokKnapp = new Button("Velg visning");
        
        sokKnapp.setOnAction(e -> {
            try {
            	//Metode for aapne nytt vindu for ï¿½ se ledige enkeltplasser. Velge/ombestemme plasser. 
            	//Maa vise totalbelop og antall plasser
            	int hentetKinosalnr = kontroll.finnKinosalnrBasertPaaVisningsnr(sokVisninger.getText());
            	boolean erKinoBetjent = true;
            	//Metode for ï¿½ sjekke om Visning finnes
            	if (hentetKinosalnr!=0) {
	            	if(kontroll.finnSpesifikkVisning(sokVisninger.getText())) {
	            		lagLedigePlasserVisning(sokVisninger.getText(), hentetKinosalnr, erKinoBetjent);
	            		sokVisninger.clear();
	            	}
            	}
            } catch (Exception exception) { exception.printStackTrace(); }
        });	
        
        rotpanel.setTop(sokpanel);
        sokpanel.getChildren().addAll(tilbake, sokVisninger, sokKnapp, sorterFilm, sorterTidspunkt, standard);
        sokpanel.setHgap(10);
        
        
	}
	
	/** Kodet av 7079, kontrollert og godkjent av 7104 */
	public void lagKundescene() {
		try {
			BorderPane rotpanel = new BorderPane();
			scene_kundeBestilling = new Scene(rotpanel,600,600);
	        
	        //Tekst - startside
			
	        Label label1= new Label(
	                kontroll.getFormattertString1()
	        		);
	        VBox layout1 = new VBox(20);
	        layout1.getChildren().addAll(label1);
	        rotpanel.setCenter(layout1);
	        
	        Button tilbake = new Button("Tilbake");
	        
	        tilbake.setOnAction(e -> {
	        	try {
	        		//kontroll.setTempreservasjonNull();
	        		behandleTilbake(menyscene);
	        	} catch (Exception exception) { exception.printStackTrace(); }
	        });
	        
	        Button standard = new Button("Standard");
	        Button sorterFilm = new Button("Sorter: film");
	        Button sorterTidspunkt = new Button("Sorter: tidspunkt");
	        
	        standard.setOnAction(e -> {
	        	try {
	        		label1.setText(kontroll.getFormattertString1());
	        	} catch (Exception exception) { exception.printStackTrace(); }
	        });
	        
	        sorterFilm.setOnAction(e -> {
	        	try {
	        		label1.setText(kontroll.getFormattertString2());
	        	} catch (Exception exception) { exception.printStackTrace(); }
	        });
	        
	        sorterTidspunkt.setOnAction(e -> {
	        	try {
	        		label1.setText(kontroll.getFormattertString3());
	        	} catch (Exception exception) { exception.printStackTrace(); }
	        });
	        
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
	            	//Metode for aapne nytt vindu for ï¿½ se ledige enkeltplasser. Velge/ombestemme plasser. 
	            	//Maa vise totalbelop og antall plasser
	            	int hentetKinosalnr = kontroll.finnKinosalnrBasertPaaVisningsnr(sokVisninger.getText());
	            	boolean erKinoBetjent = false;
	            	//Metode for ï¿½ sjekke om Visning finnes
	            	if (hentetKinosalnr!=0) {
		            	if(kontroll.finnSpesifikkVisning(sokVisninger.getText())) {
		            		lagLedigePlasserVisning(sokVisninger.getText(), hentetKinosalnr, erKinoBetjent);
		            		sokVisninger.clear();
		            	}
	            	}
	            } catch (Exception exception) { exception.printStackTrace(); }
	        });	
	        
	        rotpanel.setTop(sokpanel);
	        sokpanel.getChildren().addAll(tilbake, sokVisninger, sokKnapp, sorterFilm, sorterTidspunkt, standard);
	        sokpanel.setHgap(10);
	        
			
		} catch(Exception e) {e.printStackTrace();}
	}
	
	/** Kodet av 7079, kontrollert og godkjent av 7104 */
	public void lagOppdaterScene() {
		try {
			BorderPane oppdateringRotpanel = new BorderPane();
			GridPane oppdateringGridpane = new GridPane();
			FlowPane panel = new FlowPane();
			sceneOppdatering = new Scene(oppdateringRotpanel, 600,600);
			
			//Oppretter en knapp for tilbake
			Button tilbake = new Button("Tilbake");
			oppdateringGridpane.add(tilbake,0,1);
			tilbake.setOnAction(e -> {
	        	try {
	        		behandleTilbake(adminScene);
	        	} catch (Exception exception) { exception.printStackTrace(); }
	        });
			
			//Labels
			Label lblfilmnr = new Label("Filmnr:");
			oppdateringGridpane.add(lblfilmnr, 1, 1);
			
			Label lblkinosalnr = new Label("Kinosalnr:");
			oppdateringGridpane.add(lblkinosalnr, 2, 1);
			
			Label lbldato = new Label("Dato:");
			oppdateringGridpane.add(lbldato, 3, 1);
			
			Label lblstarttid = new Label("Starttid:");
			oppdateringGridpane.add(lblstarttid, 4, 1);
			
			Label lblpris = new Label("Pris:");
			oppdateringGridpane.add(lblpris, 5, 1);
			
			Label lblvisningsnr = new Label("Visningsnr:");
			oppdateringGridpane.add(lblvisningsnr, 6, 1);
			
			Label lblinfo = new Label("\nDu må skrive inn visningsnr og info til feltet du vil oppdatere. Klikk deretter på tilhørende oppdater-knapp");
		
			oppdateringRotpanel.getChildren().addAll(lblinfo);
			oppdateringRotpanel.setCenter(panel);
			
			//Textfield
			TextField txtfilmnr = new TextField();
			oppdateringGridpane.add(txtfilmnr, 1, 2);
			txtfilmnr.setMaxWidth(70);
			txtfilmnr.setMinWidth(60);
			
			TextField txtkinosalnr = new TextField();
			oppdateringGridpane.add(txtkinosalnr, 2, 2);
			txtkinosalnr.setMaxWidth(70);
			txtkinosalnr.setMinWidth(60);
			
			TextField txtdato = new TextField();
			oppdateringGridpane.add(txtdato, 3, 2);
			txtdato.setMaxWidth(80);
			txtdato.setMinWidth(70);
			
			TextField txtstarttid = new TextField();
			oppdateringGridpane.add(txtstarttid, 4, 2);
			txtstarttid.setMaxWidth(80);
			txtstarttid.setMinWidth(70);
			
			TextField txtpris = new TextField();
			oppdateringGridpane.add(txtpris, 5, 2);
			txtpris.setMaxWidth(70);
			txtpris.setMinWidth(60);
			
			TextField txtvisningsnr = new TextField();
			oppdateringGridpane.add(txtvisningsnr, 6, 2);
			txtvisningsnr.setMaxWidth(70);
			txtvisningsnr.setMinWidth(60);
			
			//Oppretter en knapp for filmnr:
			Button filmnr = new Button("Oppdater");
			oppdateringGridpane.add(filmnr,1,3);
			filmnr.setOnAction(e -> {
	        	try {
	        		if (kontroll.oppdaterVisningFilmnr(txtvisningsnr.getText(), txtfilmnr.getText())) {
	        			showMessageDialog(null, "Oppdatering godkjent");
	        			lagKundescene();
	        		} else {
	        			showMessageDialog(null, "Oppdatering feilet");
	        		}
	        	} catch (Exception exception) { exception.printStackTrace(); }
	        });
			
			//Oppretter en knapp for kinosalnr:
			Button kinosalnr = new Button("Oppdater");
			oppdateringGridpane.add(kinosalnr, 2, 3);
			kinosalnr.setOnAction(e -> {
	        	try {
	        		if (kontroll.oppdaterVisningKinosalnr(txtvisningsnr.getText(), txtkinosalnr.getText())) {
	        			showMessageDialog(null, "Oppdatering godkjent");
	        			lagKundescene();
	        		} else {
	        			showMessageDialog(null, "Oppdatering feilet");
	        		}
	        	} catch (Exception exception) { exception.printStackTrace(); }
	        });
			
			//Oppretter en knapp for dato:
			Button dato = new Button("Oppdater");
			oppdateringGridpane.add(dato,3,3);
			dato.setOnAction(e -> {
	        	try {
	        		if (kontroll.oppdaterVisningDato(txtvisningsnr.getText(), txtdato.getText())) {
	        			showMessageDialog(null, "Oppdatering godkjent");
	        			lagKundescene();
	        		} else {
	        			showMessageDialog(null, "Oppdatering feilet");
	        		}
	        	} catch (Exception exception) { showMessageDialog(null, "Oppdatering feilet"); }
	        });
			
			//Oppretter en knapp for starttid:
			Button starttid = new Button("Oppdater");
			oppdateringGridpane.add(starttid,4,3);
			starttid.setOnAction(e -> {
	        	try {
	        		if (kontroll.oppdaterVisningStarttid(txtvisningsnr.getText(), txtstarttid.getText())) {
	        			showMessageDialog(null, "Oppdatering godkjent");
	        			lagKundescene();
	        		} else {
	        			showMessageDialog(null, "Oppdatering feilet");
	        		}
	        	} catch (Exception exception) { showMessageDialog(null, "Oppdatering feilet"); }
	        });
			
			//Oppretter en knapp for pris:
			Button pris = new Button("Oppdater");
			oppdateringGridpane.add(pris,5,3);
			pris.setOnAction(e -> {
	        	try {
	        		if (kontroll.oppdaterVisningPris(txtvisningsnr.getText(), txtpris.getText())) {
	        			showMessageDialog(null, "Oppdatering godkjent");
	        			lagKundescene();
	        		} else {
	        			showMessageDialog(null, "Oppdatering feilet");
	        		}
	        	} catch (Exception exception) { exception.printStackTrace(); }
	        });
			
			//Oppretter en knapp for pris:
			Button info = new Button("Info-knapp");
			info.setStyle("-fx-background-color: #42A87A");
			oppdateringGridpane.add(info,7,2);
			info.setOnAction(e -> showMessageDialog(null, "Du må skrive inn visningsnr og info til feltet du vil oppdatere. Klikk deretter på tilhørende oppdater-knapp\nVisningsnr kan ikke oppdateres"));
			
			oppdateringGridpane.getChildren().addAll();
			
			//GridPane settings
			oppdateringGridpane.setHgap(30);
			oppdateringGridpane.setVgap(5);
			oppdateringRotpanel.setCenter(oppdateringGridpane);
	        
			
		} catch(Exception e) {e.printStackTrace();}
	}
	
	/** Kodet av 7074, kontrollert og godkjent av 7085 */
	public void knappBehandleAvbestill(){
		kontroll.slettAlleBilletter(kontroll.hentUbetalteBilletter());
		sletttabell.getItems().clear(); 
		sletttabell.setItems(kontroll.hentUbetalteBilletter());
	}
	/** Kodet av 7074, kontrollert og godkjent av 7085 */
	public void knappBehandleSettBetalt(String billettkode) {
		kontroll.settBillettSomBetalt(billettkode);
		sletttabell.getItems().clear();
		sletttabell.setItems(kontroll.hentUbetalteBilletter());
	}
	/** Kodet av 7074, kontrollert og godkjent av 7085 */
	public void behandleTilbake(Scene scene) {
		vindu.setScene(scene);
	}
	/** Kodet av 7074, kontrollert og godkjent av 7085 */
	public void settStorrelse(Scene scene, int bredde, int hoyde) {
		//
	}
	/** Kodet av 7074, kontrollert og godkjent av 7085 */
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
