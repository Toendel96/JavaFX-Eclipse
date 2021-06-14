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
			String kinobetjent="kinobetjent";
			String planleggeren="planlegger";
			
			BorderPane root = new BorderPane();
			FlowPane panel = new FlowPane();
			Scene scene = new Scene(root,400,400);
			vindu.setTitle("Kinosentralen");
			vindu.setWidth(900);
			vindu.setHeight(600);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			//Oppretter en knapp for planlegger:
			Button planleggerknapp = new Button("Kinosentralens planlegger");
			panel.getChildren().add(planleggerknapp);
			planleggerknapp.setOnAction(e -> login(planleggeren));
			//Oppretter en knapp for kinobetjent:
			Button kinobetjentknapp = new Button("Kinobetjent");
			kinobetjentknapp.setOnAction(e -> login(kinobetjent));
			panel.getChildren().add(kinobetjentknapp);
			//Oppretter en knapp for kunde:
			Button kundeknapp = new Button("Kunde");
			kundeknapp.setOnAction(e -> kunden());
			panel.getChildren().add(kundeknapp);
			
			//FlowPane settings
			panel.setHgap(10);
			root.setCenter(panel);

			vindu.setScene(scene);
			vindu.show();
			
			
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("Dette gikk dÃ¥rlig. PrÃ¸v igjen");
		}
	}
	
	public void login(String bruker) {
		BorderPane loginrootpanel = new BorderPane();
		GridPane gridpane = new GridPane();
		Scene loginscene = new Scene(loginrootpanel,400,400);
		gridpane.add(new Label(), 0, 0);
		gridpane.add(new TextField(), 0, 1);
		gridpane.add(new Label(), 1, 0);
		gridpane.add(new TextField(), 1, 1);
		gridpane.add(new Button(), 3, 0);
		if (bruker=="planlegger") {
			//Planleggeren prøver å logge inn
		}
		else {
			//Kinobetjenten prøver å logge inn
		}
		gridpane.getChildren().addAll();
		loginrootpanel.setCenter(gridpane);
		vindu.setScene(loginscene);
		vindu.show();
	}
	
	public void planleggeren() {
		try {
			System.out.println("Test for planlegger");
		}catch(Exception e) {System.out.println("nope");}
	}
		
		
		public void kinobetjenten() {
			try {
				System.out.println("Test for kinobetjent");
				
			}catch(Exception e) {System.out.println("nope");}
		}
			
		public void kunden() {
			try {
				System.out.println("Test for kunden");
				}catch(Exception e) {System.out.println("nope");}
			}
	
	
	
	
	public static void main(String[] args) {
		launch(args);
	}
}
