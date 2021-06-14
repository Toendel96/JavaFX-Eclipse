package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			BorderPane root = new BorderPane();
			Scene scene = new Scene(root,400,400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			System.out.println("Din applikasjon fungerer");
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("Dette gikk d�rlig. Pr�v igjen");
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}