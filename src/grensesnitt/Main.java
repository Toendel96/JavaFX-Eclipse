package grensesnitt;
	
import javafx.application.Application;
import javafx.stage.Stage;
import kontroll.Kontroll;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;


public class Main extends Application {
	//@Override
	Kontroll kontroll = new Kontroll();
	private Stage vindu;
	public void start(Stage primaryStage) {
		try {
			vindu=primaryStage;
			vindu.setTitle("Kinosentralen");
			vindu.setWidth(800);
			vindu.setHeight(600);
			lagMenyscene();
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("Dette gikk dårlig. Prøv igjen");
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
		panel.getChildren().add(planleggerknapp);
		planleggerknapp.setOnAction(e -> lagLoginscene(planleggeren));
		//Oppretter en knapp for kinobetjent:
		Button kinobetjentknapp = new Button("Kinobetjent");
		//kinobetjentknapp.setOnAction(e -> lagLoginscene(kinobetjent));
		kinobetjentknapp.setOnAction(e -> lagKinobetjentscene());
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
		try {
			System.out.println("Test for planlegger");
		}catch(Exception e) {System.out.println("nope");}
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
				Button oppdater = new Button("Oppdater som betalt");
				gridpane.add(oppdater, 1, 2);
				Button tilbake = new Button("Tilbake");
				tilbake.setOnAction(e -> behandleTilbake());
				oppdater.setOnAction(e -> System.out.println("TESTER OM BETALT"));
				gridpane.getChildren().addAll();
				kinobetjentrotpanel.setCenter(gridpane);
				kinobetjentrotpanel.setBottom(tilbake);
				vindu.setScene(kinoscene);
				vindu.show();
			}catch(Exception e) {System.out.println("nope");}
		}
			
		public void lagKundescene() {
			try {
				System.out.println("Test for kunden");
				}catch(Exception e) {System.out.println("nope");}
			}
	
		public void behandleTilbake() {
			lagMenyscene();
		}
	
	
	public static void main(String[] args) {
		launch(args);
	}
}
