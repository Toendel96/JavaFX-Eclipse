package grensesnitt;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;


public class Main extends Application {
	//@Override
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
			System.out.println("Dette gikk dÃ¥rlig. PrÃ¸v igjen");
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
