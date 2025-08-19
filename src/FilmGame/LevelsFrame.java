package FilmGame;

import database.ManageDataBase;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class LevelsFrame {
    private Screen screen = Screen.getPrimary();
    private Rectangle2D bounds = screen.getBounds();
    private double width = bounds.getWidth(), height = bounds.getHeight();
    private ImageView mainImage;
    public static boolean from_levels = false;
    public static int level_chosed, current_level;

    public void start(Stage stage) {
        StackPane root = new StackPane();

        mainImage = new ImageView(new Image(getClass().getResourceAsStream("/images/background_film.jpg")));
        mainImage.setFitWidth(width);
        mainImage.setFitHeight(height);
        mainImage.setPreserveRatio(false);

        TilePane levelsPane = new TilePane();
        levelsPane.setPadding(new Insets(20));
        levelsPane.setHgap(20);
        levelsPane.setVgap(20);
        levelsPane.setPrefColumns(4);
        levelsPane.setAlignment(Pos.TOP_CENTER);

        for (int i = 1; i <= ManageDataBase.levelsCount(); i++) {
            Button levelBtn = new Button("Level " + i);
            levelBtn.setPrefSize(150, 60);
            levelBtn.setStyle(
                    "-fx-background-color: white;" +
                    "-fx-text-fill: black;" +
                    "-fx-font-weight: bold;" +
                    "-fx-background-radius: 10;" +
                    "-fx-cursor: hand;" +
                    "-fx-border-color: #cccccc;" +
                    "-fx-border-radius: 10;" +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 5, 0, 2, 2);" +
                    "-fx-font-size: 20px"
            );
            levelBtn.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
                MainFrame.enterEffect.start();
                MainFrame.enterEffect.setFramePosition(0);
            });
            int level = i;
            levelBtn.setOnAction(e -> {
                MainFrame.clickEffect.start();
                MainFrame.clickEffect.setFramePosition(0);
                System.out.println("Clicked Level " + level);
                if (level <= ManageDataBase.getLevel()) {
                from_levels = true;
                current_level = ManageDataBase.getLevel();
                level_chosed = level;
                ManageDataBase.saveLevel(level_chosed);
                try {
					new PlayFrame().start(stage);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
                }
            });            
            levelsPane.getChildren().add(levelBtn);
            if (level <= ManageDataBase.getLevel()) {
            	levelBtn.setStyle(
                        "-fx-background-color: green;" +
                        "-fx-text-fill: black;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 10;" +
                        "-fx-cursor: hand;" +
                        "-fx-border-color: #cccccc;" +
                        "-fx-border-radius: 10;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 5, 0, 2, 2);" +
                        "-fx-font-size: 20px"
                );
            }
        }
        

        ScrollPane scrollPane = new ScrollPane(levelsPane);
        scrollPane.setFitToWidth(true);
        scrollPane.setPannable(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent; -fx-border-color: transparent;");

        Button backButton = new Button("Back", new ImageView(new Image(getClass().getResourceAsStream("/images/back.png"))));
        backButton.setPrefSize(120, 40);
        backButton.setStyle(
                "-fx-background-color: darkred;" +
                "-fx-text-fill: black;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 10;" +
                "-fx-border-color: #cccccc;" +
                "-fx-border-radius: 10;" +
                "-fx-cursor: hand;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 5, 0, 2, 2);" +
                "-fx-font-size: 20px"
        );
        backButton.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
            MainFrame.enterEffect.start();
            MainFrame.enterEffect.setFramePosition(0);
        });
        backButton.setOnAction(e -> {
            try {
                MainFrame.musicClip.stop();
                MainFrame.clickEffect.start();
                MainFrame.clickEffect.setFramePosition(0);
                new MainFrame().start(stage);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        VBox content = new VBox(10, scrollPane, backButton);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(20));

        root.getChildren().addAll(mainImage, content);

        Scene scene = new Scene(root, width, height - 50);
        stage.setTitle("Levels");
        stage.setScene(scene);
        stage.show();
    }
}
