package grensesnitt;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			BorderPane root = new BorderPane();
			FlowPane panel = new FlowPane();
			Scene scene = new Scene(root,400,400);
			primaryStage.setTitle("Kinosentralen");
			primaryStage.setWidth(900);
			primaryStage.setHeight(600);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			//Oppretter en knapp for planlegger:
			Button planleggerknapp = new Button("Kinosentralens planlegger");
			panel.getChildren().add(planleggerknapp);
			planleggerknapp.setOnAction(e -> planleggeren());
			//Oppretter en knapp for kinobetjent:
			Button kinobetjentknapp = new Button("Kinobetjent");
			kinobetjentknapp.setOnAction(e -> kinobetjenten());
			panel.getChildren().add(kinobetjentknapp);
			//Oppretter en knapp for kunde:
			Button kundeknapp = new Button("Kunde");
			kundeknapp.setOnAction(e -> kunden());
			panel.getChildren().add(kundeknapp);
			
			//FlowPane settings
			panel.setHgap(10);
			root.setCenter(panel);
			
			

			primaryStage.setScene(scene);
			primaryStage.show();
			
			
			
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("Dette gikk dårlig. Prøv igjen");
		}
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
