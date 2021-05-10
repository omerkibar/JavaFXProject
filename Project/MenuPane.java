import java.io.File;
import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MenuPane extends GridPane {
	Stage stage;
	public MenuPane(Stage stage) {
		this.stage = stage;
		setPadding(new Insets(100, 100, 100, 100));
		setHgap(100);
		setVgap(100);
		setStyle("-fx-background-color: #9c9a9a");
		Button play = new Button(new File("profile").exists() ? "Resume" : "Play");
		play.setPrefSize(300, 50);
		add(play, 0, 0);
		play.setOnMouseClicked(e->{
			try {
				stage.setScene(new Scene(new GamePane("profile2",stage)));
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		
		Button newGame = new Button("New Game");
		if(play.getText().equals("Play")) {
			newGame.setDisable(true);
		}
		newGame.setOnMouseClicked(e->{
			File file = new File("profile");
			File saveFile = new File(file+"/save.txt");
			saveFile.delete();
			File boxesFile = new File(file+"/boxes.txt");
			boxesFile.delete();
			File highscoresFile = new File(file+"/highscores.txt");
			highscoresFile.delete();
			file.delete();	
			play.setText("Play");
			((Button)(e.getSource())).setDisable(true);
		});
		newGame.setPrefSize(300, 50);
		add(newGame, 0, 1);
		
		Button highScores = new Button("High Scores");
		highScores.setOnMouseClicked(e->{
			VBox pane = new VBox();
			pane.setPrefWidth(300);
			ArrayList<Integer> highScoresList = GameProfile.getHighScores();
			for(int i = 0;i<GamePane.LEVEL_COUNT;i++) {
				pane.getChildren().add(new Label(String.format("Level %d: %d",i+1,highScoresList == null ? 0 : highScoresList.get(i))));
			}
		
			Stage highScoresStage = new Stage();
			highScoresStage.setTitle("High Scores");
			highScoresStage.setScene(new Scene(pane));
			highScoresStage.show();

			
		});
		highScores.setPrefSize(300, 50);
		add(highScores,0,2);
		
		Slider volume = new Slider();
		volume.setPrefSize(300, 50);
		add(volume, 0, 3);
		
	}
}
