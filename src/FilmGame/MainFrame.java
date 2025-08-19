package FilmGame;

import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;

import database.ManageDataBase;
import javafx.animation.Animation;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class MainFrame {

    private static Group root;
    private static Scene scene;
    private Screen screen = Screen.getPrimary();
    private Rectangle2D bounds = screen.getBounds();
    private double width = bounds.getWidth(), height = bounds.getHeight();
    private ImageView mainImage, thinkImage;
    private Button playButton, levelsButton, musicButton;
    private Scale scale;
    private Rectangle container;
    private boolean isMusicOn = true;
    private Label coinsValueLabel;
    public static Clip clickEffect, enterEffect, musicClip;
    private Stage stage;

    public void start(Stage stage) throws Exception {
    	this.stage = stage;
    	initializeUI(stage);
        stage.show();
    }

    private void initializeUI(Stage stage) throws LineUnavailableException {
        root = new Group();
        scene = new Scene(root, width, height - 50);
        stage.setScene(scene);
        stage.setTitle("FILM GAME");

        scene.setFill(Color.LIGHTGRAY);

        loadSounds();
        setupBackgroundImage();
        setupThinkImage();
        setupContainers();
        setupButtons();
        root.getChildren().addAll(mainImage, thinkImage, container, playButton, levelsButton, musicButton);
        setupCoinsDisplay();
        setupText();
    }

    private void loadSounds() throws LineUnavailableException {
        enterEffect = MusicAndSound.soundEffect("src/sound/enter.wav", enterEffect);
        clickEffect = MusicAndSound.soundEffect("src/sound/click.wav", clickEffect);
        musicClip = MusicAndSound.playMusic("src/sound/videoplayback.wav", musicClip);
        musicClip.start();
    }

    private void setupBackgroundImage() {
        mainImage = new ImageView(new Image(getClass().getResourceAsStream("/images/background_film.jpg")));
    }

    private void setupThinkImage() {
        thinkImage = new ImageView(new Image(getClass().getResourceAsStream("/images/think.png")));
        thinkImage.setLayoutX(870);
        thinkImage.setLayoutY(135);
        animateThinkImage(thinkImage);
    }

    private void setupContainers() {
        container = createContainer(310, 410, 900, 270, Color.DARKCYAN);
    }

    private Rectangle createContainer(double x, double y, double width, double height, Color strokeColor) {
        Rectangle container = new Rectangle(x, y, width, height);
        container.setFill(Color.TRANSPARENT);
        container.setStroke(strokeColor);
        container.setStrokeWidth(10);
        container.setArcHeight(60);
        container.setArcWidth(60);
        return container;
    }

    private void setupCoinsDisplay() {
        // HBox for coins (like PlayFrame)
        HBox coinsBox = new HBox(10);
        coinsBox.setAlignment(Pos.CENTER_RIGHT);
        coinsBox.setPadding(new Insets(20));
        coinsBox.setLayoutX(width - 200); // push it near the right side
        coinsBox.setLayoutY(20);

        ImageView coinsImage = new ImageView(new Image(getClass().getResourceAsStream("/images/coins.png")));
        coinsImage.setFitWidth(60);
        coinsImage.setFitHeight(60);

        coinsValueLabel = new Label(String.valueOf(ManageDataBase.getCoins()));
        coinsValueLabel.setFont(Font.font("Arial", 40));
        coinsValueLabel.setTextFill(Color.BROWN);

        coinsBox.getChildren().addAll(coinsImage, coinsValueLabel);

        root.getChildren().add(coinsBox);
    }

    private void setupButtons() {
        scale = new Scale(1.01, 1.01);

        playButton = createButton("PLAY", 400, 420, Color.DARKGREEN, Color.DARKGOLDENROD, "/images/game_icon.png");
        playButton.setOnAction(this::handlePlayButtonAction);

        levelsButton = createButton("LEVELS", 800, 420, Color.DARKORANGE, Color.DARKGREEN, "/images/levels_icon.png");
        levelsButton.setOnAction(this::handleLevelsButtonAction);

        musicButton = new Button();
        musicButton.setLayoutX(665);
        musicButton.setLayoutY(270);
        musicButton.setPrefSize(150, 80);
        musicButton.setStyle("-fx-background-color: cyan; -fx-border-color: darkcyan; -fx-border-width: 7px; -fx-background-radius: 30px; -fx-border-radius: 30px");
        musicButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/music_on.png"))));
        musicButton.setOnAction(this::handleMusicButtonAction);
        musicButton.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> handleMouseEntered(musicButton));
        musicButton.addEventHandler(MouseEvent.MOUSE_EXITED, e -> handleMouseExited(musicButton));
    }

    private Button createButton(String text, double x, double y, Color bgColor, Color textColor, String iconPath) {
        Button button = new Button(text);
        button.setLayoutX(x);
        button.setLayoutY(y);
        button.setStyle("-fx-background-radius: 40px; -fx-font-size: 40px; -fx-background-color: " + colorToHex(bgColor) + "; -fx-font-weight: bold");
        button.setTextFill(textColor);
        button.setPrefSize(300, 250);
        button.setGraphic(new ImageView(new Image(getClass().getResourceAsStream(iconPath))));
        button.setContentDisplay(ContentDisplay.TOP);
        button.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> handleMouseEntered(button));
        button.addEventHandler(MouseEvent.MOUSE_EXITED, e -> handleMouseExited(button));
        return button;
    }

    private void handleMouseEntered(Button button) {
        scene.setCursor(Cursor.HAND);
        button.getTransforms().add(scale);
        button.setStyle(button.getStyle().replace("DARK", "LIGHT")); // Change to lighter color
        enterEffect.setFramePosition(0);
        enterEffect.start();
    }

    private void handleMouseExited(Button button) {
        scene.setCursor(Cursor.DEFAULT);
        button.getTransforms().remove(scale);
        button.setStyle(button.getStyle().replace("LIGHT", "DARK")); // Change back to dark color
    }

    private void handlePlayButtonAction(ActionEvent event) {
        playSound(clickEffect);
        try {
			new PlayFrame().start(this.stage);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    private void handleLevelsButtonAction(ActionEvent event) {
        playSound(clickEffect);
        new LevelsFrame().start(stage);
    }

    private void handleMusicButtonAction(ActionEvent event) {
        playSound(clickEffect);
        isMusicOn = !isMusicOn;
        musicButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream(isMusicOn ? "/images/music_on.png" : "/images/music_off.png"))));
        if (isMusicOn) {
            musicClip.start();
        } else {
            musicClip.stop();
        }
    }

    private void playSound(Clip clip) {
        clip.setFramePosition(0);
        clip.start();
    }

    private void setupText() {
        Text filmNameText = new Text("FILM NAME");
        filmNameText.setFill(Color.DARKCYAN);
        filmNameText.setStyle("-fx-font-size: 50px; -fx-font-weight: bold;"); 
        filmNameText.setLayoutX(570);
        filmNameText.setLayoutY(190);

        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.GRAY);
        dropShadow.setRadius(5);
        dropShadow.setOffsetX(3);
        dropShadow.setOffsetY(3);
        filmNameText.setEffect(dropShadow);

        root.getChildren().add(filmNameText); 
    }

    private void animateThinkImage(ImageView image) {
        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.RED);
        dropShadow.setRadius(10);
        dropShadow.setOffsetX(0);
        dropShadow.setOffsetY(0);
        image.setEffect(dropShadow);

        ScaleTransition scaleTransition = new ScaleTransition(javafx.util.Duration.millis(700), image);
        scaleTransition.setFromX(1.0);
        scaleTransition.setToX(1.1);
        scaleTransition.setFromY(1.0);
        scaleTransition.setToY(1.1);
        scaleTransition.setCycleCount(Animation.INDEFINITE);
        scaleTransition.setAutoReverse(true);
        scaleTransition.play();
    }

    private String colorToHex(Color color) {
        return String.format("#%02x%02x%02x", 
            (int)(color.getRed() * 255), 
            (int)(color.getGreen() * 255), 
            (int)(color.getBlue() * 255));
    }
}