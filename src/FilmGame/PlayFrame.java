package FilmGame;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;

import database.ManageDataBase;

public class PlayFrame {
    private static Scene scene;
    private Screen screen = Screen.getPrimary();
    private Rectangle2D bounds = screen.getBounds();
    private double width = bounds.getWidth(), height = bounds.getHeight();
    private ImageView mainImage;
    private filmdata film = ManageDataBase.getFilmDataById(ManageDataBase.getLevel());
    private String actorName = film.getHero();
    private String filmName = film.getName().trim().toUpperCase();
    private Label coinsValueLabel;
    private int coins = ManageDataBase.getCoins();

    private boolean gameOver = false;
    private Random random = new Random();
    public static Clip clickEffect, enterEffect, collectEffect, winEffect, correctEffect;

    public void start(Stage stage) throws Exception {
        StackPane root = new StackPane();
        scene = new Scene(root, width, height - 50);

        stage.setTitle("Play");
        stage.setScene(scene);
        
        loadSounds();

        mainImage = new ImageView(new Image(getClass().getResourceAsStream("/images/background_film.jpg")));
        mainImage.setFitWidth(width);
        mainImage.setFitHeight(height - 50);

        VBox contentBox = new VBox(40);
        contentBox.setAlignment(Pos.CENTER);

        HBox banner = new HBox();
        banner.setAlignment(Pos.CENTER);
        banner.setStyle("-fx-background-color: darkcyan; -fx-padding: 20px; -fx-background-radius: 20px;");
        Text actorTitle = new Text("Actor: " + actorName);
        actorTitle.setFont(Font.font("Verdana", 40));
        actorTitle.setFill(Color.WHITE);
        banner.getChildren().add(actorTitle);

        HBox filmBox = new HBox(15);
        filmBox.setAlignment(Pos.CENTER);

        TextField[] letterFields = new TextField[filmName.length()];
        for (int i = 0; i < filmName.length(); i++) {
            TextField letterField = new TextField();
            letterField.setPrefWidth(80);
            letterField.setPrefHeight(90);
            letterField.setFont(Font.font("Arial", 36));
            letterField.setAlignment(Pos.CENTER);
            letterField.setStyle("-fx-background-color: white; -fx-border-color: darkcyan; -fx-border-width: 3px; -fx-border-radius: 10px;");
            final int index = i;
            letterField.textProperty().addListener((obs, oldText, newText) -> {
                if (newText.length() > 1) {
                    letterField.setText(newText.substring(0, 1));
                }
            });
            letterField.setOnKeyReleased(e -> {
                if (!letterField.getText().isEmpty() && index < filmName.length() - 1) {
                    letterFields[index + 1].requestFocus();
                }
            });
            letterFields[i] = letterField;
            filmBox.getChildren().add(letterField);
        }

        HBox coinsBox = new HBox(20);
        coinsBox.setAlignment(Pos.CENTER_RIGHT);
        coinsBox.setPadding(new Insets(50));

        ImageView coinsImage = new ImageView(new Image(getClass().getResourceAsStream("/images/coins.png")));
        coinsImage.setFitWidth(60);
        coinsImage.setFitHeight(60);

        coinsValueLabel = new Label(String.valueOf(coins));
        coinsValueLabel.setFont(Font.font("Arial", 40));
        coinsValueLabel.setTextFill(Color.BROWN);

        coinsBox.getChildren().addAll(coinsImage, coinsValueLabel);

        HBox buttonBox = new HBox(40);
        buttonBox.setAlignment(Pos.CENTER);

        Button submitButton = new Button("Submit", new ImageView(new Image(getClass().getResourceAsStream("/images/submit.png"))));
        styleButton(submitButton, "darkgreen");
        submitButton.addEventHandler(MouseEvent.MOUSE_ENTERED, E->{
            scene.setCursor(Cursor.HAND);
            enterEffect.start();
            enterEffect.setFramePosition(0);
        });
        submitButton.addEventHandler(MouseEvent.MOUSE_EXITED, E->{
            scene.setCursor(Cursor.DEFAULT);
        });

        Button hintButton = new Button("Hint", new ImageView(new Image(getClass().getResourceAsStream("/images/hint.png"))));
        styleButton(hintButton, "darkorange");
        hintButton.addEventHandler(MouseEvent.MOUSE_ENTERED, E->{
            scene.setCursor(Cursor.HAND);
            enterEffect.start();
            enterEffect.setFramePosition(0);
        });
        hintButton.addEventHandler(MouseEvent.MOUSE_EXITED, E->{
            scene.setCursor(Cursor.DEFAULT);
        });

        Button backButton = new Button("Back", new ImageView(new Image(getClass().getResourceAsStream("/images/back.png"))));
        styleButton(backButton, "darkred");
        backButton.addEventHandler(MouseEvent.MOUSE_ENTERED, E->{
            scene.setCursor(Cursor.HAND);
            enterEffect.start();
            enterEffect.setFramePosition(0);
        });
        backButton.addEventHandler(MouseEvent.MOUSE_EXITED, E->{
            scene.setCursor(Cursor.DEFAULT);
        });
        backButton.setOnAction(e -> {
        	if (LevelsFrame.from_levels && LevelsFrame.level_chosed != LevelsFrame.current_level) {
        		ManageDataBase.saveLevel(LevelsFrame.current_level);
        	}
            clickEffect.start();
            clickEffect.setFramePosition(0);
            try {
                MainFrame.musicClip.stop();
                new MainFrame().start(stage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        buttonBox.getChildren().addAll(submitButton, hintButton, backButton);

        Runnable checkAnswer = () -> {
            if (gameOver) return;
            boolean allCorrect = true;
            for (int i = 0; i < filmName.length(); i++) {
                String userInput = letterFields[i].getText().toUpperCase();
                String correctChar = String.valueOf(filmName.charAt(i));
                if (userInput.equals(correctChar)) {
                    letterFields[i].setStyle("-fx-background-color: lightgreen; -fx-border-color: darkcyan; -fx-border-width: 3px; -fx-border-radius: 10px;");
                    letterFields[i].setEditable(false);
                } else {
                    if (letterFields[i].isEditable()) {
                        letterFields[i].setStyle("-fx-background-color: white; -fx-border-color: darkcyan; -fx-border-width: 3px; -fx-border-radius: 10px;");
                    }
                    allCorrect = false;
                }
            }
            if (allCorrect) {
                submitButton.setDisable(true);
                hintButton.setDisable(true);
                gameOver = true;
                if (!LevelsFrame.from_levels) {
                showWinOverlay(root, stage);
                ManageDataBase.saveLevel(ManageDataBase.getLevel() + 1);
                ManageDataBase.addCoins(15);
                winEffect.start();
                winEffect.setFramePosition(0);
                }
                else {
                	if (LevelsFrame.level_chosed == LevelsFrame.current_level) {
                	     ManageDataBase.saveLevel(LevelsFrame.current_level + 1);
                	     showWinOverlay(root, stage); 
                	     ManageDataBase.addCoins(15);
                	     winEffect.start();
                         winEffect.setFramePosition(0);
                	}
                	else {
                		ManageDataBase.saveLevel(LevelsFrame.current_level);
                	}
                }
            }
        };

        submitButton.setOnAction(e -> {checkAnswer.run();clickEffect.start();clickEffect.setFramePosition(0);});
        scene.setOnKeyPressed(e -> {
            if (!gameOver && e.getCode() == KeyCode.ENTER) {
                checkAnswer.run();
            }
        });

        hintButton.setOnAction(e -> {
            clickEffect.start();
            clickEffect.setFramePosition(0);
            if (coins < 5) {
                return;
            }
            List<Integer> available = new ArrayList<>();
            for (int i = 0; i < filmName.length(); i++) {
                if (letterFields[i].isEditable()) {
                    available.add(i);
                }
            }
            if (!available.isEmpty()) {
                int randomIndex = available.get(random.nextInt(available.size()));
                letterFields[randomIndex].setText(String.valueOf(filmName.charAt(randomIndex)));
                letterFields[randomIndex].setEditable(false);
                letterFields[randomIndex].setStyle("-fx-background-color: orange; -fx-border-color: darkcyan; -fx-border-width: 3px; -fx-border-radius: 10px;");
                coins -= 5;
                coinsValueLabel.setText(String.valueOf(coins));
                if (coins < 5) {
                    hintButton.setDisable(true);
                }
            }
        });

        VBox centerBox = new VBox(30, banner, filmBox, buttonBox);
        centerBox.setAlignment(Pos.CENTER);

        BorderPane layout = new BorderPane();
        layout.setTop(coinsBox);
        layout.setCenter(centerBox);

        root.getChildren().addAll(mainImage, layout);
    }
    
    
    private void loadSounds() throws LineUnavailableException {
        enterEffect = MusicAndSound.soundEffect("src/sound/enter.wav", enterEffect);
        clickEffect = MusicAndSound.soundEffect("src/sound/click.wav", clickEffect);
        collectEffect = MusicAndSound.soundEffect("src/sound/collect.wav", collectEffect);
        winEffect = MusicAndSound.soundEffect("src/sound/win_level.wav", winEffect);
    }

    private void showWinOverlay(StackPane root, Stage stage) {
        VBox overlay = new VBox(30);
        overlay.setAlignment(Pos.CENTER);
        overlay.setPadding(new Insets(40));
        overlay.setStyle("-fx-background-color: rgba(0,128,0,0.9); -fx-background-radius: 20px;");

        Label winLabel = new Label("🎉 You Win! 🎉");
        winLabel.setFont(Font.font("Verdana", 60));
        winLabel.setTextFill(Color.WHITE);

        // NEW: Win coins message with coin image
        HBox winCoinsBox = new HBox(15);
        winCoinsBox.setAlignment(Pos.CENTER);
        ImageView coinImg = new ImageView(new Image(getClass().getResourceAsStream("/images/coins.png")));
        coinImg.setFitWidth(50);
        coinImg.setFitHeight(50);
        Label coinMsg = new Label("You earned +15 coins!");
        coinMsg.setFont(Font.font("Arial", 40));
        coinMsg.setTextFill(Color.GOLD);
        winCoinsBox.getChildren().addAll(coinImg, coinMsg);

        Button nextLevelBtn = new Button("Next Level");
        styleButton(nextLevelBtn, "goldenrod");
        nextLevelBtn.setOnAction(e -> {
            clickEffect.start();
            clickEffect.setFramePosition(0);
            collectEffect.start();
            collectEffect.setFramePosition(0);
            playFlashEffect(root, stage);
        });
        nextLevelBtn.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
            scene.setCursor(Cursor.HAND);
            enterEffect.start();
            enterEffect.setFramePosition(0);
        });
        nextLevelBtn.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
            scene.setCursor(Cursor.DEFAULT);
        });

        overlay.getChildren().addAll(winLabel, winCoinsBox, nextLevelBtn);

        root.getChildren().add(overlay);

        overlay.setOpacity(0);
        overlay.setScaleX(0.5);
        overlay.setScaleY(0.5);

        FadeTransition fade = new FadeTransition(Duration.seconds(1), overlay);
        fade.setFromValue(0);
        fade.setToValue(1);

        ScaleTransition scale = new ScaleTransition(Duration.seconds(1), overlay);
        scale.setFromX(0.5);
        scale.setFromY(0.5);
        scale.setToX(1);
        scale.setToY(1);

        fade.play();
        scale.play();
    }

    private void playFlashEffect(StackPane root, Stage stage) {
        Rectangle flash = new Rectangle(width, height, Color.WHITE);
        flash.setOpacity(0);
        root.getChildren().add(flash);

        FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.2), flash);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.3), flash);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);

        SequentialTransition flashEffect = new SequentialTransition(fadeIn, fadeOut);

        flashEffect.setOnFinished(ev -> {
            try {
                PlayFrame newFrame = new PlayFrame();
                newFrame.start(stage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        flashEffect.play();
    }

    private void styleButton(Button button, String color) {
        button.setFont(Font.font(28));
        button.setTextFill(Color.WHITE);
        button.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 20px; -fx-padding: 10px 30px;");
        button.setContentDisplay(javafx.scene.control.ContentDisplay.TOP);
    }
}
