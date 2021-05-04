
import java.io.File;
import java.util.Scanner;

import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;


public class GamePane extends GridPane{
	
	private Box[][] boxes = new Box[10][10];

	public GamePane(File level) throws Exception {
		draw(level);
	}
	
	public void draw(File level) throws Exception {
		this.setPadding(new Insets(5, 5, 5, 5));
		this.setStyle("-fx-background-color: #9c9a9a");
		this.setHgap(5);
		this.setVgap(5);
		 
		for(int row = 0;row<10;row++) {
			for(int column = 0;column<10;column++) {
				boxes[row][column] = new Box("Wall");
				this.add(boxes[row][column],column, row);
				boxes[row][column].setOnMouseClicked(e->
				{	Box box = (Box)e.getSource();
					hitBoxes(box);
				//	box.setType("Empty");
				});
			}
		}
		Scanner levelfile = new Scanner(level);
		while(levelfile.hasNext()) {
			String line = levelfile.nextLine();
			String[] parts = line.split(",");
			boxes[Integer.parseInt(parts[1])][Integer.parseInt(parts[2])].setType(parts[0]);
		}
		levelfile.close();
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
	
	public void hitBoxes(Box box) {
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
		
		if (box.getType().equals("Mirror")) {
			String music = "mirrorsounddeneme.mp3";
	        Media sound = new Media(new File(music).toURI().toString());
	        MediaPlayer mediaPlayer = new MediaPlayer(sound);
	        mediaPlayer.play();
		}
		
		hitOneBox(boxes[row][column]);
		if(row<9) {
			hitOneBox(boxes[row+1][column]);
		}
		if(row>0) {
			hitOneBox(boxes[row-1][column]);
		}
		if(column<9) {
			hitOneBox(boxes[row][column+1]);
		}
		if(column>0) {
			hitOneBox(boxes[row][column-1]);
		}
	}
}
