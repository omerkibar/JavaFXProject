
import java.io.File;
import java.util.Scanner;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;


public class GamePane extends BorderPane{
	Stage stage;
	private double volume;
	private boolean isFirstStart;
	private String points = "";
	private int score = 0;
	private Label scoreLabel = new Label();
	private Label nextLevel = new Label("Next Level");
	private Label highScoreLabel = new Label();
	private Label currentLevelLabel = new Label();
	private Label hitLabel = new Label("---Text---");
	public static final int LEVEL_COUNT = 5;
	private Box[][] boxes = new Box[10][10];
	private int currentLevel;
    MediaPlayer mp;
    MediaPlayer lvlMP;
    GameProfile profile;
	public GamePane(Stage stage,double volume) throws Exception {
		this.stage = stage;
		stage.setOnCloseRequest(e->profile.saveProfile(currentLevel, hitLabel.getText(), scoreLabel.getText(), boxes,this.volume));
		this.profile = new GameProfile();
		if(!new File("profile/boxes.txt").exists()) {
			currentLevel = 1;
			scoreLabel.setText("Score: "+score);
			currentLevelLabel.setText(String.format("Level %d", this.getCurrentLevel()));			
		}
		else{
			String[] labels = profile.loadLabels();
			currentLevelLabel.setText(labels[0]);
			hitLabel.setText(labels[1]);
			scoreLabel.setText(labels[2]);
			score = Integer.parseInt(labels[2].split(" ")[1]);
			currentLevel = Integer.parseInt(labels[0].split(" ")[1]);
			isFirstStart = true;
		}
		nextLevel.setOnMouseClicked(e->{
			try {
			this.nextLevel();
			currentLevelLabel.setText(String.format("Level %d", this.getCurrentLevel()));
			hitLabel.setText("---Text---");
			} catch (Exception e1) {	
					e1.printStackTrace();
		}});
		
		this.setOnMouseClicked(e-> {	
			hitLabel.setText(this.getPoints());
		});
		
	    Media sound = new Media(new File("sound.mp3").toURI().toString());
	    mp = new MediaPlayer(sound);
	    
	    Media lvl = new Media(new File("levelup.mp3").toURI().toString());
	    lvlMP = new MediaPlayer(lvl);
	    
	    this.volume = volume;
	    mp.setVolume(volume);
	    lvlMP.setVolume(volume);
	    
	    nextLevel.setDisable(true);
		
		highScoreLabel.setText("High Score: " + profile.getHighScore(currentLevel));
			
		draw(currentLevel);
		
		if(isFinished()) {
			nextLevel.setDisable(false);
		}
	}
	
	public void draw(int currentLevel) throws Exception {
		BorderPane topPane = new BorderPane();
		topPane.setLeft(currentLevelLabel);
		topPane.setCenter(scoreLabel);
		topPane.setRight(highScoreLabel);
		this.setTop(topPane);
		
		BorderPane bottomPane = new BorderPane();
		
		Label menuLabel = new Label("Menu");
		menuLabel.setOnMouseClicked(e->{profile.saveProfile(currentLevel, hitLabel.getText(), scoreLabel.getText(), boxes,this.volume);
			stage.close();
			stage.setScene(new Scene(new MenuPane(stage)));
			stage.show();
			stage.setOnCloseRequest(null);	
		});
		bottomPane.setCenter(hitLabel);
		bottomPane.setLeft(menuLabel);
		bottomPane.setRight(nextLevel);
		this.setBottom(bottomPane);
		
		GridPane center = new GridPane();
		center.setPadding(new Insets(2, 2, 2, 2));
		center.setStyle("-fx-background-color: #9c9a9a");
		center.setHgap(2);
		center.setVgap(2);
		 
		for(int row = 0;row<10;row++) {
			for(int column = 0;column<10;column++) {
				boxes[row][column] = new Box("Wall");
				center.add(boxes[row][column],column, row);
				boxes[row][column].setOnMouseClicked(e-> {
					if (e.getButton() == MouseButton.PRIMARY) {
						setPoints("");
						Box box = (Box)e.getSource();
						try {
							hitBoxes(box);
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				});
			}	
		}
		Scanner levelfile;
		if(isFirstStart) {
			levelfile = new Scanner(new File("profile/boxes.txt"));
			isFirstStart = false;	
		}
		else {
			levelfile = new Scanner(new File("levels/Level"+currentLevel+".txt"));
		}
		while(levelfile.hasNext()) {
			String line = levelfile.nextLine();
			String[] parts = line.split(",");
			boxes[Integer.parseInt(parts[1])][Integer.parseInt(parts[2])].setType(parts[0]);
		}
		levelfile.close();
		this.setCenter(center);
	}
	
	public String getPoints() {
		if (points.equals(""))
			return "---Text---";
		return points;
	}

	public void setPoints(String points) {
		this.points = points;
	}
	
	public void hitOneBox(Box box) {
		String type = box.getType();
		switch(type) {
		case("Wood"):
			box.setType("Mirror");
			break;
		case("Mirror"):
			box.setType("Empty");
			break;
		case("Wall"):
			break;
		case("Empty"):
			break;
		}
	}
	
	public void hitBoxes(Box box) throws Exception {
		if(box.getType().equals("Wall")||box.getType().equals("Empty")) {
			return;
		}
		int row = 0;
		int column = 0;
		for (int i = 0; i < boxes.length; i++) {
			for (int j = 0; j < boxes[i].length; j++) {
				if (box == boxes[i][j]) {
					row = i;
					column = j;
				}	
			}
		}
		
		mp.stop();
		mp.seek(mp.getStartTime());
		mp.play();
		
		if (row < 9 && row > 0 && column < 9 && column > 0) {
			hitOneBox(boxes[row][column]);
			int hits = 1;
			setPoints("Box:" + row + "-" + column);
			
			if(boxes[row+1][column].getType().equals("Mirror") || boxes[row+1][column].getType().equals("Wood")) {
				hitOneBox(boxes[row+1][column]);
				setPoints(getPoints() + " - Hit:" + (row+1) + "," + column);
				hits++;
			}
			if(boxes[row-1][column].getType().equals("Mirror") || boxes[row-1][column].getType().equals("Wood")) {
				hitOneBox(boxes[row-1][column]);
				setPoints(getPoints() + " - Hit:" + (row-1) + "," + column);
				hits++;
			}
			
			if(boxes[row][column+1].getType().equals("Mirror") || boxes[row][column+1].getType().equals("Wood")) {
				hitOneBox(boxes[row][column+1]);
				setPoints(getPoints() + " - Hit:" + (row) + "," + (column+1));
				hits++;
			}
			
			if(boxes[row][column-1].getType().equals("Mirror") || boxes[row][column-1].getType().equals("Wood")) {
				hitOneBox(boxes[row][column-1]);
				setPoints(getPoints() + " - Hit:" + (row) + "," + (column-1));
				hits++;
			}
			
			switch(hits) {
			case(1):
				setPoints(getPoints() + " (-3 points)");
				score -= 3;
				break;
			case(2):
				setPoints(getPoints() + " (-1 points)");
				score -= 1;
				break;
			case(3):
				setPoints(getPoints() + " (+1 points)");
				score += 1;
				break;
			case(4):
				setPoints(getPoints() + " (+2 points)");
				score += 2;
				break;
			case(5):
				score += 4;
				setPoints(getPoints() + " (+4 points)");
				break;
			}
			scoreLabel.setText("Score: "+score);
			if(isFinished()) {
				nextLevel.setDisable(false);
				mp.stop();
				lvlMP.seek(lvlMP.getStartTime());
				lvlMP.play();
				if (profile.getHighScore(getCurrentLevel()).equals("*")||Integer.parseInt(profile.getHighScore(getCurrentLevel())) < score ) {
					profile.saveNewHighScore(getCurrentLevel(), score,LEVEL_COUNT);
					highScoreLabel.setText("High Score: "+String.valueOf(profile.getHighScore(currentLevel)));
				}
			}
		}
	}
	
	public void nextLevel() throws Exception {
		if(currentLevel+1<=LEVEL_COUNT) {
			currentLevel++;
			draw(currentLevel);
		}
		else {
			currentLevel = 1;
			draw(currentLevel);
		}
		score = 0;
		scoreLabel.setText("Score: "+score);
		nextLevel.setDisable(true);
		highScoreLabel.setText("High Score: " + profile.getHighScore(currentLevel));
	}
	
	public int getCurrentLevel() {
		return currentLevel;
	}
	
	private boolean isFinished() {
		for(int row = 0;row<boxes.length;row++) {
			for(int column = 0;column<boxes[0].length;column++) {
				if(!boxes[row][column].getType().equals("Wall")&&!boxes[row][column].getType().equals("Empty"))
					return false;
			}
		}
		return true;
	}
}
