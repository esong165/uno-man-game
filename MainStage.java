import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Robot;
import javafx.stage.Screen;
import java.util.Scanner;
/** 
 * This program runs a game in which the player uses the WASD keys and mouse input to get to the goal while avoiding an enemy.
 * 
 * @author Eric Song
 * @version 1.0
 * @since 05-20-2019
 *
 */
public class MainStage extends Application{
	
	/**
	 * Represents the location of the player.
	 */
	private Location playerState;
	
	/**
	 * Represents the point around which the camera rotates horizontally, to be used in mouse movement.
	 */
	private Rotate xRotate;
	
	/**
	 * Represents the point around which the camera rotates vertically, to be used in mouse movement.
	 */
	private Rotate yRotate;
	
	/**
	 * Represents the width of the monitor (subtracting a bit to reset the mouse properly), in pixels.
	 */
	private final int screenWidth = (int) Screen.getPrimary().getBounds().getWidth() - 20;
	
	/**
	 * Represents the height of the monitor (subtracting a bit to reset the mouse properly), in pixels.
	 */
	private final int screenHeight = (int) Screen.getPrimary().getBounds().getHeight() - 20;
	
	/**
	 * Represents the player's movement speed.
	 */
	private final int WALKING_SPEED = 30;
	
	/**
	 * Represents the western boundary of the world.
	 */
	private final int WORLD_WEST_BORDER = -14500;
	
	/**
	 * Represents the eastern boundary of the world.
	 */
	private final int WORLD_EAST_BORDER = 14500;
	
	/**
	 * Represents the northern boundary of the world.
	 */
	private final int WORLD_NORTH_BORDER = 14500;
	
	/**
	 * Represents the southern boundary of the world.
	 */
	private final int WORLD_SOUTH_BORDER = -14500;
	
	/**
	 * Represents the state of the W key.
	 */
	private boolean wPressed;
	
	/**
	 * Represents the state of the A key.
	 */
	private boolean aPressed;
	
	/**
	 * Represents the state of the S key.
	 */
	private boolean sPressed;
	
	/**
	 * Represents the state of the D key.
	 */
	private boolean dPressed;
	
	/**
	 * Represents if the game has ended.
	 */
	private boolean end;
	
	/**
	 * Represents if the player has input a movement command.
	 */
	private boolean hasStarted;

	/**
	 * This method prompts the user with the introduction, affirms their readiness state, and then launches the game.
	 */
	public static void main(String[] args) {
		System.out.println("It is the final game of the Uno Tournament of Champions,");
		System.out.println("and you, Dumb Bot, are currently in second place.");
		System.out.println("You currently have just one card left in your hand, but");
		System.out.println("your opponent has a trick up their sleeve: a Wild +4!");
		System.out.println("Can you get your card to the pile and be crowned");
		System.out.println("this year's Champion?");
		System.out.println("Type \"Ready\" to continue:");
		Scanner in = new Scanner(System.in);
		boolean ready = false;
		while(!ready) {
			if(in.nextLine().toLowerCase().equals("ready")) {
				ready = true;
			}
		}
		in.close();
		launch(args);
	}

	/**
	 * This method defines the logic and event handlers for mouse movement, and adds these components to the game.
	 * @param c The camera that is being moved.
	 * @param s The scene that the game is presented on.
	 */
	private void initializeMouseControl(PerspectiveCamera c, Scene s){
		try {
			Robot r = new Robot();
			r.mouseMove(screenWidth/2, screenHeight/2);
			playerState.setOldX(MouseInfo.getPointerInfo().getLocation().x);
			playerState.setOldY(MouseInfo.getPointerInfo().getLocation().y);
		}
		catch (AWTException e)
	    {
			e.printStackTrace();
	    }
		c.getTransforms().addAll(
			xRotate = new Rotate(0, playerState.getX(), 0, playerState.getZ(), Rotate.Y_AXIS),
			yRotate = new Rotate(0, playerState.getX(), 0, playerState.getZ(), Rotate.X_AXIS)
		);
		
		xRotate.angleProperty().bind(playerState.getAngleX());
		yRotate.angleProperty().bind(playerState.getAngleY());
		
		EventHandler<MouseEvent> filter = new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event){
				
				try {
					Robot r = new Robot();
					r.mouseMove(screenWidth / 2, screenHeight / 2);
				}
				catch (AWTException e)
			    {
					e.printStackTrace();
			    }
				event.consume();
			}

		};
		
		EventHandler<MouseEvent> movementHandler = new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				if(!end) {
					double newX = MouseInfo.getPointerInfo().getLocation().x;
					double newY = MouseInfo.getPointerInfo().getLocation().y;
				
					double dx = newX - playerState.getOldX();
					double dy = newY - playerState.getOldY();
				
					playerState.setOldX(newX);
					playerState.setOldY(newY);
				
					if(dx < 300 && dx > -300 && dy < 300 && dy > -300) {
						double finalx = playerState.getAngleX().getValue() + dx;
						playerState.getAngleX().set(finalx);
						
						double finaly = playerState.getAngleY().getValue() - dy;
						if(finaly < 90 && finaly > -90) {
							playerState.getAngleY().set(finaly);
						}
					}
					while(playerState.getAngleX().getValue() > 360) {
						playerState.getAngleX().set(playerState.getAngleX().getValue() - 360);
					}
					while(playerState.getAngleX().getValue() < 0) {
						playerState.getAngleX().set(playerState.getAngleX().getValue() + 360);
					}
				}
			}
		};
		
		s.setOnMouseExited(event -> {
			s.removeEventHandler(MouseEvent.MOUSE_MOVED, movementHandler);
			s.addEventFilter(MouseEvent.MOUSE_EXITED, filter);
		});
	
		s.setOnMouseEntered(event -> {
			s.removeEventFilter(MouseEvent.MOUSE_ENTERED, filter);
			s.addEventHandler(MouseEvent.MOUSE_MOVED, movementHandler);
		});		
	}
	
	/**
	 * This method defines the handler which sets the booleans defining the states of the WASD keys, and adds it to the game.
	 * It also indicates to the enemy that the player has started moving.
	 * @param s The scene that the game is presented on.
	 */
	public void initializeKeyControl(Scene s) {
		EventHandler<KeyEvent> keyInput = new EventHandler<KeyEvent>() {
			public void handle(KeyEvent event) {
				hasStarted = true;
				if(!end) {
					if(event.getCode() == KeyCode.W) {
						wPressed = true;
					}
					else if (event.getCode() == KeyCode.S) {
						sPressed = true;
					}
					else if (event.getCode() == KeyCode.A) {
						aPressed = true;
					}
					else if (event.getCode() == KeyCode.D) {
						dPressed = true;
					}
				}
			}
		};	
		
		EventHandler<KeyEvent> keyRelease = new EventHandler<KeyEvent>() {
			public void handle(KeyEvent event) {
				if(event.getCode() == KeyCode.W) {
					wPressed = false;
				}
				else if (event.getCode() == KeyCode.S) {
					sPressed = false;
		        }
				else if (event.getCode() == KeyCode.A) {
					aPressed = false;
		        }
				else if (event.getCode() == KeyCode.D) {
					dPressed = false;
		        }
			}
		};
		
		s.addEventHandler(KeyEvent.KEY_PRESSED, keyInput);
		s.addEventHandler(KeyEvent.KEY_RELEASED, keyRelease);
	}
	
	/**
	 * This method creates and starts a timer which automatically ticks. On each tick, it updates the player's location 
	 * and checks to see if the player has won the game, it will update the screen accordingly.
	 * @param c The camera which the player is controlling as their field of view.
	 * @param pic The Picture object representing the goal.
	 * @param loc The Location object representing the location of the player.
	 * @param stage The stage where the game is happening.
	 */
	public void initializeTimer(PerspectiveCamera c, Picture pic, Location loc, Stage stage) {
		AnimationTimer t = new AnimationTimer() {
			public void handle(long l) {
				updateLocation(c);
				if(Math.abs(pic.getLocation().getX() - loc.getX()) < 1000 && Math.abs(pic.getLocation().getZ() - loc.getZ()) < 1000) {
					win(stage);
				}
				
			}
		};
		t.start();
	}
	
	/**
	 * This method creates and starts a timer which sets the initial location of the enemy and updates its location on each tick.
	 * The enemy only moves if the player has started to move and if the game is not over.
	 * This method also checks to see if the player has lost, and updates the screen if the player does lose.
	 * @param pic The Picture object representing the enemy.
	 * @param loc The Location object representing the location of the player.
	 * @param stage The stage where the game is happening.
	 */
	public void initializeEnemyMovement(Picture pic, Location loc, Stage stage) {
		pic.getImageView().setTranslateX(3000);
		pic.getImageView().setTranslateZ(3000);
		pic.getImageView().translateXProperty();
		pic.getImageView().translateZProperty();
		pic.getLocation().setX(3000);
		pic.getLocation().setZ(3000); 
		AnimationTimer a = new AnimationTimer() {
			public void handle(long l) {
				if(hasStarted && !end) {
					updateEnemyLocation(pic, loc);
					if(Math.abs(pic.getLocation().getX() - loc.getX()) < 300 && Math.abs(pic.getLocation().getZ() - loc.getZ()) < 300) {
						lose(stage);
					}
				}
			}
		};
		a.start();
	}
	
	/**
	 * This method sets the movement of the player based on the angle which the player's camera is facing and the keys being pressed.
	 * @param c The camera controlled by the player.
	 */
	public void updateLocation(PerspectiveCamera c) {
		if(wPressed) {
			if(aPressed) {
				playerState.setXVel(WALKING_SPEED*Math.sin(playerState.getAngleX().getValue()*Math.PI/180) - WALKING_SPEED*Math.cos(playerState.getAngleX().getValue()*Math.PI/180));
				playerState.setZVel(WALKING_SPEED*Math.cos(playerState.getAngleX().getValue()*Math.PI/180) + WALKING_SPEED*Math.sin(playerState.getAngleX().getValue()*Math.PI/180));
			}
			else if(dPressed) {
				playerState.setXVel(WALKING_SPEED*Math.sin(playerState.getAngleX().getValue()*Math.PI/180) + WALKING_SPEED*Math.cos(playerState.getAngleX().getValue()*Math.PI/180));
				playerState.setZVel(WALKING_SPEED*Math.cos(playerState.getAngleX().getValue()*Math.PI/180) - WALKING_SPEED*Math.sin(playerState.getAngleX().getValue()*Math.PI/180));
			}
			else if(!sPressed){
				playerState.setXVel(WALKING_SPEED*Math.sin(playerState.getAngleX().getValue()*Math.PI/180));
				playerState.setZVel(WALKING_SPEED*Math.cos(playerState.getAngleX().getValue()*Math.PI/180));
			}
		}
		else if(sPressed) {
			if(aPressed) {
				playerState.setXVel(-WALKING_SPEED*Math.sin(playerState.getAngleX().getValue()*Math.PI/180) - WALKING_SPEED*Math.cos(playerState.getAngleX().getValue()*Math.PI/180));
				playerState.setZVel(-WALKING_SPEED*Math.cos(playerState.getAngleX().getValue()*Math.PI/180) + WALKING_SPEED*Math.sin(playerState.getAngleX().getValue()*Math.PI/180));
			}
			else if(dPressed) {
				playerState.setXVel(-WALKING_SPEED*Math.sin(playerState.getAngleX().getValue()*Math.PI/180) + WALKING_SPEED*Math.cos(playerState.getAngleX().getValue()*Math.PI/180));
				playerState.setZVel(-WALKING_SPEED*Math.cos(playerState.getAngleX().getValue()*Math.PI/180) - WALKING_SPEED*Math.sin(playerState.getAngleX().getValue()*Math.PI/180));
			}
			else if(!wPressed) {
				playerState.setXVel(-WALKING_SPEED*Math.sin(playerState.getAngleX().getValue()*Math.PI/180));
				playerState.setZVel(-WALKING_SPEED*Math.cos(playerState.getAngleX().getValue()*Math.PI/180));
			}
		}
		else if(aPressed) {
			if(wPressed) {
				playerState.setXVel(-WALKING_SPEED*Math.cos(playerState.getAngleX().getValue()*Math.PI/180) + WALKING_SPEED*Math.sin(playerState.getAngleX().getValue()*Math.PI/180));
				playerState.setZVel(WALKING_SPEED*Math.sin(playerState.getAngleX().getValue()*Math.PI/180) + WALKING_SPEED*Math.cos(playerState.getAngleX().getValue()*Math.PI/180));
			}
			else if(sPressed) {
				playerState.setXVel(-WALKING_SPEED*Math.cos(playerState.getAngleX().getValue()*Math.PI/180) - WALKING_SPEED*Math.sin(playerState.getAngleX().getValue()*Math.PI/180));
				playerState.setZVel(WALKING_SPEED*Math.sin(playerState.getAngleX().getValue()*Math.PI/180) - WALKING_SPEED*Math.cos(playerState.getAngleX().getValue()*Math.PI/180));
			}
			else if(!dPressed) {
				playerState.setXVel(-WALKING_SPEED*Math.cos(playerState.getAngleX().getValue()*Math.PI/180));
				playerState.setZVel(WALKING_SPEED*Math.sin(playerState.getAngleX().getValue()*Math.PI/180));
			}
		}
		else if(dPressed) {
			if(wPressed) {
				playerState.setXVel(WALKING_SPEED*Math.cos(playerState.getAngleX().getValue()*Math.PI/180) + WALKING_SPEED*Math.sin(playerState.getAngleX().getValue()*Math.PI/180));
				playerState.setZVel(-WALKING_SPEED*Math.sin(playerState.getAngleX().getValue()*Math.PI/180) + WALKING_SPEED*Math.cos(playerState.getAngleX().getValue()*Math.PI/180));
			}
			else if(sPressed) {
				playerState.setXVel(WALKING_SPEED*Math.cos(playerState.getAngleX().getValue()*Math.PI/180) - WALKING_SPEED*Math.sin(playerState.getAngleX().getValue()*Math.PI/180));
				playerState.setZVel(-WALKING_SPEED*Math.sin(playerState.getAngleX().getValue()*Math.PI/180) - WALKING_SPEED*Math.cos(playerState.getAngleX().getValue()*Math.PI/180));
			}
			else if(!aPressed) {
				playerState.setXVel(WALKING_SPEED*Math.cos(playerState.getAngleX().getValue()*Math.PI/180));
				playerState.setZVel(-WALKING_SPEED*Math.sin(playerState.getAngleX().getValue()*Math.PI/180));
			}
		}	
		else {
			playerState.setXVel(0);
			playerState.setZVel(0);
		}
		if(playerState.getX() + playerState.getXVel() < WORLD_EAST_BORDER && playerState.getX() + playerState.getXVel() > WORLD_WEST_BORDER) {
			playerState.setX(playerState.getX() + playerState.getXVel());
			c.setTranslateX(c.getTranslateX() + playerState.getXVel());
			c.translateXProperty();
		}
		if(playerState.getZ() + playerState.getZVel() < WORLD_NORTH_BORDER && playerState.getZ() + playerState.getZVel() > WORLD_SOUTH_BORDER) {
			playerState.setZ(playerState.getZ() + playerState.getZVel());
			c.setTranslateZ(c.getTranslateZ() + playerState.getZVel());
			c.translateZProperty();
		}
	}
	
	/**
	 * This method moves the enemy based on where it is in respect to the player.
	 * @param pic The Picture object representing the enemy.
	 * @param loc The Location object representing the location of the player.
	 */
	public void updateEnemyLocation(Picture pic, Location loc) {
		double deltaX = pic.getLocation().getX() - loc.getX();
		double deltaZ = pic.getLocation().getZ() - loc.getZ();
		
		if(deltaX > 0 && deltaZ > 0) {
			pic.getLocation().getAngleX().set(Math.PI + Math.atan(deltaX / deltaZ * Math.PI/180));
		}
		else if(deltaX > 0 && deltaZ < 0) {
			pic.getLocation().getAngleX().set(1.5*Math.PI + Math.atan(-deltaZ / deltaX * Math.PI/180));
		}
		else if(deltaX < 0 && deltaZ > 0) {
			pic.getLocation().getAngleX().set(0.5*Math.PI + Math.atan(deltaZ / -deltaX * Math.PI/180));
		}
		else if(deltaX < 0 && deltaZ < 0) {
			pic.getLocation().getAngleX().set(Math.atan(deltaX / deltaZ * Math.PI/180));
		}

		pic.getLocation().setXVel(35 * Math.sin(pic.getLocation().getAngleX().getValue()));
		pic.getLocation().setZVel(35 * Math.cos(pic.getLocation().getAngleX().getValue()));
		pic.getLocation().setX(pic.getLocation().getX() + pic.getLocation().getXVel());
		pic.getLocation().setZ(pic.getLocation().getZ() + pic.getLocation().getZVel());
		pic.getImageView().setTranslateX(pic.getImageView().getTranslateX() + pic.getLocation().getXVel());
		pic.getImageView().setTranslateZ(pic.getImageView().getTranslateZ() + pic.getLocation().getZVel());
		pic.getImageView().translateXProperty();
		pic.getImageView().translateZProperty();
	}
	
	/**
	 * This method brings up a lose screen and stops the rest of the game.
	 * @param stage The stage on which the game is occurring.
	 */
	public void lose(Stage stage) {
		end = true;
		Pane newPane = new Pane();
		
		Rectangle r = new Rectangle(screenWidth, screenHeight);
		r.setFill(Color.BLACK);
		newPane.getChildren().add(r);
		
		TextField t = new TextField("You Lose!");
		t.setEditable(false);
		t.setTranslateX(-200 + screenWidth / 2);
		t.setTranslateY(screenHeight / 2);
		t.setStyle("-fx-font-size: 32px;");
		newPane.getChildren().add(t);

		
		Scene loseScreen = new Scene(newPane, screenWidth, screenHeight);
		stage.setScene(loseScreen);
	}
	
	/**
	 * This method brings up a win screen and stops the rest of the game.
	 * @param stage The stage on which the game is occurring.
	 */
	public void win(Stage stage) {
		end = true;
		Pane newPane = new Pane();
		
		Rectangle r = new Rectangle(screenWidth, screenHeight);
		r.setFill(Color.LIGHTGREEN);
		newPane.getChildren().add(r);
		
		TextField t = new TextField("You Win!");
		t.setEditable(false);
		t.setTranslateX(-200 + screenWidth / 2);
		t.setTranslateY(screenHeight / 2);
		t.setStyle("-fx-font-size: 32px;");
		newPane.getChildren().add(t);

		
		Scene winScreen = new Scene(newPane, screenWidth, screenHeight);
		stage.setScene(winScreen);
	}
	
	/**
	 * This method initializes the player's location and the game window, defines the ground, creates the enemy and goal images, and adds these to the game window.
	 * @param stage The stage on which the game will occur.
	 */
	public void start(Stage stage) throws Exception{
		playerState = new Location();
		
		Pane p = new Pane();
		
		Box ground = new Box(32000, 0, 32000);
		PhongMaterial greenColor = new PhongMaterial(Color.rgb(0, 255, 0));
		ground.setMaterial(greenColor);
		p.getChildren().add(ground);
		ground.setTranslateY(screenHeight + 400);
		ground.translateYProperty();

		Picture w4 = new Picture("http://www.costumeslife.com/images/Funny/4904-Uno-Draw-4-Card-Costume-large.jpg");
		Picture pile = new Picture("https://i1.wp.com/primaryplayground.net/wp-content/uploads/2018/06/5-math-games-to-play-with-UNO-cardsFB.png");
		p.getChildren().add(pile.getImageView());
		p.getChildren().add(w4.getImageView());
		
		double pileX = 14000;
		double pileZ = 14000;
		pile.getImageView().setTranslateX(pileX);
		pile.getImageView().setTranslateZ(pileZ);
		pile.getImageView().translateXProperty();
		pile.getImageView().translateZProperty();
		pile.getLocation().setX(pileX);
		pile.getLocation().setZ(pileZ);

		
		PerspectiveCamera cam = new PerspectiveCamera();
		
		Group root = new Group(p);

		Scene scene = new Scene(root, screenWidth, screenHeight);
		scene.setCursor(Cursor.NONE);
		scene.setCamera(cam);
		scene.setFill(Color.LIGHTSKYBLUE);
		stage.setTitle("Uno Tournament of Champions");
		stage.setScene(scene);
		stage.show();
		
		
		initializeMouseControl(cam, scene);
		initializeKeyControl(scene);
		initializeTimer(cam, pile, playerState, stage);
		initializeEnemyMovement(w4, playerState, stage);
		
	}
}
