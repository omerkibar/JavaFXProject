
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class MainClass extends Application{

	public static void main(String[] args) {
		launch();
	}
	
	@Override
	public void start(Stage stage) throws Exception {

		GamePane gamePane = new GamePane();
		
		Scene scene = new Scene(gamePane);
		stage.setTitle("Game");
		stage.setScene(scene);
		stage.show();
		
		gamePane.setOnMouseClicked(e -> { 
			if (e.getButton() == MouseButton.SECONDARY) {
			
					GridPane settings = new GridPane();
					settings.setPadding(new Insets(100, 100, 100, 100));
					settings.setHgap(100);
					settings.setVgap(100);
					settings.setStyle("-fx-background-color: #9c9a9a");
					Button resume = new Button("Resume");
					resume.setPrefSize(300, 50);
					settings.add(resume, 0, 0);
					
					Button load = new Button("Load");
					load.setPrefSize(300, 50);
					TextField loadtf = new TextField();
					settings.add(load, 0, 1);
					settings.add(loadtf, 1, 1);
					
					Button save = new Button("Save");
					save.setPrefSize(300, 50);
					TextField tf = new TextField();
					settings.add(save, 0, 2);
					settings.add(tf, 1, 2);
					
					Slider volume = new Slider();
					volume.setPrefSize(300, 50);
					settings.add(volume, 0, 3);
					
			        Scene scene2 = new Scene(settings);
			        stage.setScene(scene2);
			        stage.centerOnScreen();
			        stage.show();
			        
			        resume.setOnAction((e2)->
				    {
				    	stage.setScene(scene);
				    	stage.show();
				    });
			        
			        save.setOnAction((e3)->
				    {
				    	String text = tf.getText();
				    	if (!text.equals("")) {
							try {
								gamePane.saveGame(text);
							} catch (Exception e1) {
								e1.printStackTrace();
							}
				    	}
				    	
				    });
			        
			        load.setOnAction((e4)->
				    {
				    	String text = loadtf.getText();
				    	if (!text.equals("")) {
							try {
								gamePane.loadGame(text);
							} catch (Exception e1) {
								e1.printStackTrace();
							}
				    	}
				    	
				    });
			}
		});
	}

}
