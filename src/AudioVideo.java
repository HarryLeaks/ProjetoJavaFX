import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Point3D;
import javafx.geometry.VPos;
import javafx.scene.Camera;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.text.Font;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.FileChooser;
import javafx.stage.StageStyle;
import javafx.util.Duration;


class Audio {
	private Pane root = new Pane();
	private Scene scene;
	private Button Toggle = new Button();
	private Button addAudio = new Button();
	private Button state, btnLeft, btnRight, btnRemove;
	private Image btnPlayIMG = new Image(new File("src\\buttonImages\\btnPlay.png").toURI().toString(), true);
	private Image btnStopIMG = new Image(new File("src\\buttonImages\\btnStop.png").toURI().toString(), true);
	private Image addToPlaylist = new Image(new File("src\\buttonImages\\addToPlaylist.png").toURI().toString(), true);
	private Image ToggleIMG1 = new Image(new File("src\\buttonImages\\toggle1.png").toURI().toString(), true);
	private Image Music = new Image(new File("src\\buttonImages\\musicTheme.png").toURI().toString(), true);
	private Image Remove = new Image(new File("src\\buttonImages\\remove.png").toURI().toString(), true);
	private Label image = new Label();
	private Label vol = new Label();
	private ImageView view;
	private Boolean isOpened = false;
	private Boolean isPlaying = false;
	private AnchorPane anp = new AnchorPane();
	private MediaPlayer sound;
	private Slider volume = new Slider(0.0, 1.0, 0.5);
	private FileChooser fileChooser = new FileChooser();
	private int currentPositionAudio = 0;
	
	private ListView<String> list = new ListView<String>();
	private ObservableList<String> items = FXCollections.observableArrayList();
	private List<String> pathsList = new ArrayList<String>();
	private Boolean mustGetOtherAudio;
	private BorderPane pane = new BorderPane();
	private Group terrainGroup = new Group();
	private PerspectiveCamera camera = new PerspectiveCamera(true);
	private static final double CAM_SPEED = 0.1;
	private double anchorAngleY = 0;
	private final DoubleProperty angleY = new SimpleDoubleProperty(0);
	private boolean inGame = false, falling = true; 
	private float gravity = 0.0008f; 
	private Boolean falling2 = false;
	private Label readMe = new Label();
	private Boolean isHide = false;
	
	Audio(String path, String name) throws Exception{
		root.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
		
		setUpTerrain();
		
	    camera.getTransforms().addAll(new Translate(0.219, 0.129, 0.2), new Rotate(-90, Rotate.X_AXIS), new Rotate(90, Rotate.Y_AXIS));
	    camera.setFieldOfView(70);
	    Group root3D = new Group(camera, terrainGroup);
	    SubScene subScene = new SubScene(root3D, 490, 396, true, SceneAntialiasing.BALANCED);
	    subScene.setFill(Color.AQUAMARINE);
	    subScene.setCamera(camera);
	    pane.setCenter(subScene);
	    pane.setCursor(Cursor.CROSSHAIR);
	    root.getChildren().add(pane);
		
		items.add(name);
		list.setItems(items);
		pathsList.add(path);
		
		//System.out.println(pathsList.get(0));
		sound = new MediaPlayer(new Media(new File(pathsList.get(currentPositionAudio)).toURI().toString()));
		
		list.setPrefHeight(185);
		list.setPrefWidth(220);
		list.setLayoutX(15);
		list.setLayoutY(150);
		list.getSelectionModel().select(currentPositionAudio);
		list.setEditable(true);
			
		Toggle.setLayoutX(430);
		Toggle.setLayoutY(10);
		view = new ImageView(ToggleIMG1);
		view.setFitHeight(20);
		view.setFitWidth(20);
		Toggle.setGraphic(view);
		
		addAudio.setLayoutX(15);
		addAudio.setLayoutY(345);
		view = new ImageView(addToPlaylist);
		view.setFitHeight(20);
		view.setFitWidth(20);
		addAudio.setGraphic(view);
		
		view = new ImageView(btnStopIMG);
		view.setFitHeight(20);
		view.setFitWidth(20);
		state = new Button();
		state.setGraphic(view);
		state.setLayoutX(102);
		state.setLayoutY(107);
		
		readMe.setText("1. Button ESC to play or to pause!(Paused by default)\n2. W -> move X\n3. S -> move X\n4. D -> move Y\n5. A -> move Y\n6. While playing the game you can add blocks clicking the mouse right button\n7. While playing the game you can delete blocks clicking the mouse left button\n8. Press Q key to hide or unhide this information");
		readMe.setLayoutX(10);
		readMe.setLayoutY(10);
		
		btnRight = new Button(">>");
		btnRight.setLayoutX(144);
		btnRight.setLayoutY(110);
		btnLeft = new Button("<<");
		btnLeft.setLayoutX(63);
		btnLeft.setLayoutY(110);
		btnRight.setDisable(true);
		btnLeft.setDisable(true);
		
		view = new ImageView(Remove);
		view.setFitHeight(20);
		view.setFitWidth(20);
		btnRemove = new Button();
		btnRemove.setGraphic(view);
		btnRemove.setLayoutX(200);
		btnRemove.setLayoutY(107);
		
		view = new ImageView(Music);
		view.setFitHeight(140);
		view.setFitWidth(190);
		image.setGraphic(view);
		image.setLayoutX(25);
		image.setLayoutY(-10);
		
		vol.setText("vol:");
		vol.setLayoutX(77);
		vol.setLayoutY(343);
		vol.setFont(new Font(15));
		
		volume.setMajorTickUnit(0.1);
		volume.setShowTickLabels(true);
		volume.setLayoutY(347);
		volume.setLayoutX(100);		
		
		RotateTransition rotation = new RotateTransition(Duration.seconds(0.75), Toggle);
		rotation.setCycleCount(1);
		rotation.setByAngle(90);
		
		anp.setLayoutX(-(490/2));
		anp.setLayoutY(0);
		anp.setPrefWidth(490/2);
		anp.setPrefHeight(396);
		anp.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
		anp.setOpacity(0.7);
		
		anp.getChildren().add(image);
		anp.getChildren().add(btnLeft);
		anp.getChildren().add(state);
		anp.getChildren().add(btnRight);
		anp.getChildren().add(btnRemove);
		anp.getChildren().add(list);
		anp.getChildren().add(addAudio);
		anp.getChildren().add(vol);
		anp.getChildren().add(volume);
		
		root.getChildren().add(readMe);
		root.getChildren().add(Toggle);
		root.getChildren().add(anp);
				
		Toggle.setOnAction((event) ->{
			if(!isOpened) {
				rotation.setDuration(Duration.seconds(1));
				rotation.play();
				isOpened = true;
				root.translateXProperty().set(0);
				Timeline timeline = new Timeline();
				KeyValue kv = new KeyValue(anp.translateXProperty(), scene.getWidth()/2, Interpolator.EASE_IN);
				KeyFrame kf = new KeyFrame(Duration.seconds(1), kv);
				timeline.getKeyFrames().add(kf);
				timeline.play();
			}else {
				rotation.setDuration(Duration.seconds(0.75));
				rotation.play();
				isOpened = false;
				root.translateXProperty().set(0);
				Timeline timeline = new Timeline();
				KeyValue kv = new KeyValue(anp.translateXProperty(), -(scene.getWidth()/2), Interpolator.EASE_OUT);
				KeyFrame kf = new KeyFrame(Duration.seconds(1), kv);
				timeline.getKeyFrames().add(kf);
				timeline.play();
			}
		});
		
		state.setOnAction((event) -> {
			if(!isPlaying) {
				isPlaying = true;
				view = new ImageView(btnPlayIMG);
				view.setFitHeight(20);
				view.setFitWidth(20);
				state.setGraphic(view);
				sound.play();
				list.getSelectionModel().select(currentPositionAudio);
			}else {
				isPlaying = false;
				view = new ImageView(btnStopIMG);
				view.setFitHeight(20);
				view.setFitWidth(20);
				state.setGraphic(view);
				sound.pause();
			}
		});
		
		volume.valueProperty().addListener((observable) -> {
			if(volume.isValueChanging()) {
				sound.setVolume(volume.getValue());
			}
		});
		
		btnRight.setOnAction((event) -> {
			sound.stop();
			if(currentPositionAudio+1 < items.size()) {
				currentPositionAudio++;
				sound = new MediaPlayer(new Media(new File(pathsList.get(currentPositionAudio)).toURI().toString()));
				sound.play();
			}else if(currentPositionAudio+1 == items.size()) {
				currentPositionAudio = 0;
				sound = new MediaPlayer(new Media(new File(pathsList.get(currentPositionAudio)).toURI().toString()));
				sound.play();
			}
			
			list.getSelectionModel().select(currentPositionAudio);

			
			view = new ImageView(btnPlayIMG);
			view.setFitHeight(20);
			view.setFitWidth(20);
			state.setGraphic(view);
			
			volume.valueProperty().addListener((observable) -> {
				if(volume.isValueChanging()) {
					sound.setVolume(volume.getValue());
				}
			});
		});
		
		btnLeft.setOnAction((event) -> {
			sound.stop();
			if(currentPositionAudio != 0) {
				currentPositionAudio--;
				sound = new MediaPlayer(new Media(new File(pathsList.get(currentPositionAudio)).toURI().toString()));
				sound.play();
			}else if(currentPositionAudio == 0) {
				currentPositionAudio = items.size()-1;
				sound = new MediaPlayer(new Media(new File(pathsList.get(currentPositionAudio)).toURI().toString()));
				sound.play();
			}
			
			list.getSelectionModel().select(currentPositionAudio);
			
			view = new ImageView(btnPlayIMG);
			view.setFitHeight(20);
			view.setFitWidth(20);
			state.setGraphic(view);
			
			volume.valueProperty().addListener((observable) -> {
				if(volume.isValueChanging()) {
					sound.setVolume(volume.getValue());
				}
			});
		});
		
		btnRemove.setOnAction((event) -> {
			sound.stop();
			list.getItems().remove(currentPositionAudio);
			pathsList.remove(currentPositionAudio);
			if(!list.getSelectionModel().isEmpty()) {
				sound.stop();
				if(currentPositionAudio != 0)
					currentPositionAudio--;
				sound = new MediaPlayer(new Media(new File(pathsList.get(currentPositionAudio)).toURI().toString()));
				sound.play();
				list.getSelectionModel().select(currentPositionAudio);
			}else {
				sound.stop();
				mustGetOtherAudio = true;
				while(mustGetOtherAudio == true) {
					Alert alert = new Alert(Alert.AlertType.ERROR);
			        alert.initStyle(StageStyle.UTILITY);
			        alert.setTitle("Error");
			        alert.setHeaderText("You must choose an audio!");

			        alert.showAndWait();
					File file = fileChooser.showOpenDialog(null);
					if(file != null) {
						String Extension = getExtension(file.getName().toString()).toString();
						if(file != null && Extension.equals("Optional[mp3]")) {
							addOtherMusic(file.getAbsolutePath(), file.getName());
							mustGetOtherAudio = false;
						}
					}
				}
			}
		});
		
		addAudio.setOnAction((event) -> {
			File file = fileChooser.showOpenDialog(null);
			if(file != null) {
				String Extension = getExtension(file.getName().toString()).toString();
				if(file != null && Extension.equals("Optional[mp3]")) {
					addOtherMusic(file.getAbsolutePath(), file.getName());
				}else {
					Alert alert = new Alert(Alert.AlertType.ERROR);
			        alert.initStyle(StageStyle.UTILITY);
			        alert.setTitle("Error");
			        alert.setHeaderText("Error in opening the file");
	
			        alert.showAndWait();
				}
			}
		});
		
		list.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		        //System.out.println(list.getSelectionModel().getSelectedIndex());
		    	if(!list.getSelectionModel().isEmpty()) {
			    	currentPositionAudio = list.getSelectionModel().getSelectedIndex();
					sound = new MediaPlayer(new Media(new File(pathsList.get(currentPositionAudio)).toURI().toString()));
					sound.play();
					view = new ImageView(btnPlayIMG);
					view.setFitHeight(20);
					view.setFitWidth(20);
					state.setGraphic(view);
		    	}
		    }
		});
		
		scene = new Scene(root, 490, 396, true);	
		initMouseControl(camera, scene);
	    
		scene.setOnKeyPressed(e -> {
			if(inGame) {
			     switch(e.getCode()) {
			           case W:
			              	//System.out.println("W");
			                camera.translateXProperty().set(camera.getTranslateX() + CAM_SPEED/10);
			                break;
			           case S:
			              	//System.out.println("S");
			                camera.translateXProperty().set(camera.getTranslateX() - CAM_SPEED/10);
			                break;
			           case A:
			              	//System.out.println("A");
			                camera.translateYProperty().set(camera.getTranslateY() + CAM_SPEED/10);
			                break;
			           case D:
			                //System.out.println("D");
			                camera.translateYProperty().set(camera.getTranslateY() - CAM_SPEED/10);
			                break;
					default:
						break;
			   }
			}
			KeyCode key = e.getCode();
            if (key == KeyCode.ESCAPE){
            	if(inGame) {
            		inGame = false;
            	}else {
            		inGame = true;
            	}
            }
            if(key == KeyCode.Q) {
            	if(isHide) {
            		isHide = false;
            		readMe.setVisible(true);
            	}else {
            		isHide = true;
            		readMe.setVisible(false);
            	}
            }
		});
		
		pane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
            	MouseButton button = event.getButton();
				if(button == MouseButton.PRIMARY)
					addBlock();
				if(button == MouseButton.SECONDARY)
					delBlock();
            }
		});
		
		new Thread(() -> {
			while(true) {
				while(falling) {
					try {
						Thread.sleep(70);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					camera.translateZProperty().set(camera.getTranslateZ() - gravity);
					for(int x=0; x<25; x++) {
						Cube c = (Cube)terrainGroup.getChildren().get(x);
						if(c.isColliding(camera))
							falling = false;
					}
				}
				//System.out.println("Trasnlation x:" + camera.getTranslateX() + " y:" + camera.getTranslateY());
				//System.out.println("Rotation x:" + camera.getRotationAxis().getX() + " y:" + camera.getRotationAxis().getY());
				if(camera.getTranslateX() >= 0.255 || camera.getTranslateX() <= -0.27 || camera.getTranslateY() <= -0.2 || camera.getTranslateY() >= 0.33)
					falling2 = true;				
				if(falling2) {
					camera.translateZProperty().set(camera.getTranslateZ() - gravity);
					if(camera.getTranslateZ() <= -0.5) {
						camera.translateZProperty().set(0.1);
						camera.translateXProperty().set(0.219);
						camera.translateYProperty().set(0.129);
						falling2 = false;
						falling = true;
					}
				}
			}
		}).start();
		
		scene.setOnMouseExited((event) -> {
			isPlaying = false;
			view = new ImageView(btnStopIMG);
			view.setFitHeight(20);
			view.setFitWidth(20);
			state.setGraphic(view);
			sound.pause();
		});
	}
	
	public void addOtherMusic(String Path, String name) {
		items.add(name);
		list.setItems(items);
		pathsList.add(Path);
		btnRight.setDisable(false);
		btnLeft.setDisable(false);
		//System.out.println(pathsList.get(1));
		//System.out.println(pathsList.get(0));
	}
	
	public Optional<String> getExtension(String filename) {
		return Optional.ofNullable(filename)
				.filter(f -> f.contains("."))
				.map(f -> f.substring(filename.lastIndexOf(".") + 1));
	}
	
	public void setUpTerrain() {
		for(double y = 0; y < 0.5; y+=0.1) {
			for(double x = 0; x < 0.5; x+=0.1) {
				Random random = new Random(); 
		        int r = random.nextInt(255); 
		        int g = random.nextInt(255); 
		        int b = random.nextInt(255);  
				Cube block = new Cube(Color.rgb(r, g, b), 0.1, 0.1, 0.1);
				block.set(new Point3D(x, y, 0));
				terrainGroup.getChildren().add(block);
			}
		}
	}
	
	public void addBlock() {
		Random random = new Random(); 
        int r = random.nextInt(255); 
        int g = random.nextInt(255); 
        int b = random.nextInt(255);  
		Cube block = new Cube(Color.rgb(r, g, b), 0.03, 0.03, 0.03);
		block.set(new Point3D(camera.getTranslateX()+0.13, camera.getTranslateY()+0.13, 0.07));
		terrainGroup.getChildren().add(block);
	}
	
	public void delBlock() {
		if(terrainGroup.getChildren().size() > 25) {
			int pos = 25;
			double distancia = 0;
			for(int i = 25; i < terrainGroup.getChildren().size(); i++) {
				if(i == 25) {
					distancia = Math.sqrt(Math.pow(camera.getTranslateX()+0.13, terrainGroup.getChildren().get(i).getTranslateX())+Math.pow(camera.getTranslateY()+0.13, terrainGroup.getChildren().get(i).getTranslateY())); 
				}else {
					if(distancia >  Math.sqrt(Math.pow(camera.getTranslateX()+0.13, terrainGroup.getChildren().get(i).getTranslateX())+Math.pow(camera.getTranslateY()+0.13, terrainGroup.getChildren().get(i).getTranslateY()))) {
						distancia = Math.sqrt(Math.pow(camera.getTranslateX()+0.13, terrainGroup.getChildren().get(i).getTranslateX())+Math.pow(camera.getTranslateY()+0.13, terrainGroup.getChildren().get(i).getTranslateY()));
						pos = i;
					}
				}
			}
			terrainGroup.getChildren().remove(pos);
		}
	}

	private void initMouseControl(PerspectiveCamera camera, Scene scene) {
		Rotate yRotate;
		camera.getTransforms().addAll(
			yRotate = new Rotate(0, Rotate.Y_AXIS)
		);
		yRotate.angleProperty().bind(angleY);
		
		scene.setOnMouseMoved(event -> {
			if(inGame) {
			angleY.set(anchorAngleY + event.getSceneX()); 
			}
		});
	}
	
	public Scene getScene() {
		return scene;
	}
}

class Video {
	private Scene scene;
	private Duration duration;
	private Slider volume, time;
	private String Path;
	private List<MediaPlayer> repList = new ArrayList<MediaPlayer>();
	private MediaView winVideo;
	private Button state, addVideo, btnLeft, btnRight;
	private Image btnPlayIMG = new Image(new File("src\\buttonImages\\btnPlay.png").toURI().toString(), true);
	private Image btnStopIMG = new Image(new File("src\\buttonImages\\btnStop.png").toURI().toString(), true);
	private ImageView view;
	private int VideoCounter = 0, actualPosition = 0; 
	private FileChooser fileChooser = new FileChooser();
	
	Video(String path) throws Exception{
		Path = path; 
		repList.add(new MediaPlayer(new Media(new File(Path).toURI().toString())));
		VideoCounter++;
		setVideo();
	}
	
	public void setVideo() {
		volume = new Slider(0.0, 1.0, 0.5);
		time = new Slider();
		volume.setMajorTickUnit(0.1);
		volume.setShowTickLabels(true);		
		time.setShowTickLabels(true);
		repList.get(actualPosition).setAutoPlay(true);
		
		addVideo = new Button("Load other video");
		fileChooser.setTitle("Select file");
		
		winVideo = new MediaView(repList.get(actualPosition));
		winVideo.setFitWidth(490);
		winVideo.setFitHeight(396);
		
		final DropShadow effect = new DropShadow();
		effect.setOffsetY(5.0);
		effect.setOffsetX(5.0);
		effect.setColor(Color.GREY);
		winVideo.setEffect(effect);
		
		view = new ImageView(btnPlayIMG);
		view.setFitHeight(20);
		view.setFitWidth(20);
		state = new Button();
		state.setGraphic(view);
		state.setOnAction((event) -> {
			if(repList.get(actualPosition).getStatus() == Status.PLAYING) {
				repList.get(actualPosition).pause();
				view = new ImageView(btnStopIMG);
				view.setFitHeight(20);
				view.setFitWidth(20);
				state.setGraphic(view);
			}
			if(repList.get(actualPosition).getStatus() == Status.PAUSED) {
				repList.get(actualPosition).play();
				view = new ImageView(btnPlayIMG);
				view.setFitHeight(20);
				view.setFitWidth(20);
				state.setGraphic(view);
			}
		});
		
		btnLeft = new Button("<<");
		btnLeft.setDisable(true);
		btnRight = new Button(">>");
		btnRight.setDisable(true);
		
		addVideo.setOnAction((event) -> {
			File file = fileChooser.showOpenDialog(null);
			String Extension = getExtension(file.getName().toString()).toString();
			if(file != null && Extension.equals("Optional[mp4]")) {
				addOtherVideo(file.getAbsolutePath());
			}else {
				Alert alert = new Alert(Alert.AlertType.ERROR);
		        alert.initStyle(StageStyle.UTILITY);
		        alert.setTitle("Error");
		        alert.setHeaderText("Error in opening the file");

		        alert.showAndWait();
			}
		});
		
		volume.valueProperty().addListener((observable) -> {
			if(volume.isValueChanging()) {
				repList.get(actualPosition).setVolume(volume.getValue());
			}
		});
		
		repList.get(actualPosition).currentTimeProperty().addListener((Observable) -> {
			duration = repList.get(actualPosition).getMedia().getDuration();
			actValue(actualPosition);
		});
		
		time.valueProperty().addListener((Observable) -> {
			if(time.isPressed()) {
				repList.get(actualPosition).seek(repList.get(actualPosition).getMedia().getDuration().multiply(time.getValue() / 100));
			}
		});
		
		GridPane panelVideo=new GridPane();
		panelVideo.setHgap(5);
		panelVideo.setVgap(5);
		Label vol=new Label("Volume:");
		Label dur=new Label("Duration(%):");
		
		panelVideo.setStyle("-fx-padding: 4;");
		GridPane.setValignment(btnLeft, VPos.TOP);
		panelVideo.add(btnLeft, 1, 0);
		GridPane.setValignment(state, VPos.TOP);
		panelVideo.add(state, 5, 0);
		GridPane.setValignment(btnRight, VPos.TOP);
		GridPane.setHalignment(btnRight, HPos.RIGHT);
		panelVideo.add(btnRight, 8, 0);
		GridPane.setValignment(vol, VPos.TOP);
		panelVideo.add(vol, 1, 1);
		panelVideo.add(volume, 2, 1);
		GridPane.setValignment(dur, VPos.TOP);
		panelVideo.add(dur, 7, 1);
		panelVideo.add(time, 8, 1);
		GridPane.setValignment(addVideo, VPos.BOTTOM);
		GridPane.setHalignment(addVideo, HPos.RIGHT);
		panelVideo.add(addVideo, 8, 3);
				
		VBox root = new VBox(winVideo,panelVideo);
		
		scene = new Scene(root, 490, 396);
		scene.setFill(Color.TRANSPARENT);
		scene.addEventFilter(MouseEvent.MOUSE_CLICKED, evt -> {
			if(!inHierarchy(evt.getPickResult().getIntersectedNode(), panelVideo) && repList.get(actualPosition).getStatus() == Status.PLAYING) {
				repList.get(actualPosition).pause();
				view = new ImageView(btnStopIMG);
				view.setFitHeight(20);
				view.setFitWidth(20);
				state.setGraphic(view);
			}else if(!inHierarchy(evt.getPickResult().getIntersectedNode(), panelVideo) && repList.get(actualPosition).getStatus() == Status.PAUSED) {
				repList.get(actualPosition).play();
				view = new ImageView(btnPlayIMG);
				view.setFitHeight(20);
				view.setFitWidth(20);
				state.setGraphic(view);
			}
		});
		scene.setOnMouseExited(ev -> {
			repList.get(actualPosition).pause();
			view = new ImageView(btnStopIMG);
			view.setFitHeight(20);
			view.setFitWidth(20);
			state.setGraphic(view);
		});
		
		scene.setOnMouseEntered(ev -> {
			repList.get(actualPosition).play();
			view = new ImageView(btnPlayIMG);
			view.setFitHeight(20);
			view.setFitWidth(20);
			state.setGraphic(view);
		});
		
		btnRight.setOnAction((event) ->{
			repList.get(actualPosition).pause();
			actualPosition++;
			btnLeft.setDisable(false);
			
			if(actualPosition == VideoCounter)
				btnRight.setDisable(true);
			
			volume.valueProperty().addListener((observable) -> {
				if(volume.isValueChanging()) {
					repList.get(actualPosition).setVolume(volume.getValue());
				}
			});
			
			repList.get(actualPosition).currentTimeProperty().addListener((Observable) -> {
				duration = repList.get(actualPosition).getMedia().getDuration();
				actValue(actualPosition);
			});
			
			time.valueProperty().addListener((Observable) -> {
				if(time.isPressed()) {
					repList.get(actualPosition).seek(repList.get(actualPosition).getMedia().getDuration().multiply(time.getValue() / 100));
				}
			});
			
			changeVideo(actualPosition);
		});
		
		btnLeft.setOnAction((event) ->{
			repList.get(actualPosition).pause();
			
			actualPosition--;
			btnRight.setDisable(false);
			
			if(actualPosition == 0)
				btnLeft.setDisable(true);
			
			volume.valueProperty().addListener((observable) -> {
				if(volume.isValueChanging()) {
					repList.get(actualPosition).setVolume(volume.getValue());
				}
			});
			
			repList.get(actualPosition).currentTimeProperty().addListener((Observable) -> {
				duration = repList.get(actualPosition).getMedia().getDuration();
				actValue(actualPosition);
			});
			
			time.valueProperty().addListener((Observable) -> {
				if(time.isPressed()) {
					repList.get(actualPosition).seek(repList.get(actualPosition).getMedia().getDuration().multiply(time.getValue() / 100));
				}
			});
			
			changeVideo(actualPosition);
		});
	}
	
	public static boolean inHierarchy(Node node, Node potentialHierarchyElement) {
		if(potentialHierarchyElement == null) {
			return true;
		}
		
		while(node != null) {
			if(node == potentialHierarchyElement) {
				return true;
			}
			node = node.getParent();
		}
		return false;
	}
	
	public void addOtherVideo(String Path) {
		repList.add(new MediaPlayer(new Media(new File(Path).toURI().toString())));
		btnRight.setDisable(false);
	}
	
	public void changeVideo(int pos) {
		repList.get(pos).setAutoPlay(true);
		winVideo.setMediaPlayer(repList.get(pos));
	}
	
	public Scene getScene() {
		return scene;
	}
	
	private void actValue(int pos) {
		if(time!=null) {
			Platform.runLater(() -> {
				if(!time.isDisabled() && duration.greaterThan(Duration.ZERO) && !time.isValueChanging()) {
						time.setValue(repList.get(pos).getCurrentTime().divide(duration.toMillis()).toMillis()*100.0);		
				}
			});
		}
	}
	
	public Optional<String> getExtension(String filename) {
		return Optional.ofNullable(filename)
				.filter(f -> f.contains("."))
				.map(f -> f.substring(filename.lastIndexOf(".") + 1));
	}
}

class Cube extends Box{
	public Cube(Color color, double x, double y, double z) {
		super(x,y,z);
		setMaterial(new PhongMaterial(color));
	}
	
	public void set(Point3D p) {
		setTranslateX(p.getX());
		setTranslateY(p.getY());
		setTranslateZ(p.getZ());
	}
	
	public Boolean isColliding(Camera c) {
		return getTranslateZ()-0.08 >= c.getTranslateZ();
	}
}

public class AudioVideo {
	public Scene createScene(boolean isAudio, String path, String name) throws Exception {
		if(isAudio) {
			return new Audio(path, name).getScene();
		}else { 
			return new Video(path).getScene();
		}
	}
}
