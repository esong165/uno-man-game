
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * This class represents a Picture object, which has both an Image and a Location.
 * @author Eric Song
 * @version 1.0
 * @since 05-20-2019
 */
public class Picture {
	
	/**
	 * This represents the image that will be displayed.
	 */
	private Image pic;
	
	/**
	 * This represents the location of the image.
	 */
	private Location l;
	
	/**
	 * This is a JavaFX component to add the image to the game.
	 */
	private ImageView iv;
	
	/**
	 * This constructor initializes the image and location of the Picture.
	 * @param s
	 */
	public Picture(String s) {
		pic = new Image(s);
		l = new Location();
		iv = new ImageView(pic);
	}
	
	/**
	 * This returns the Location object representing the location of the picture.
	 * @return The Location object representing the location of the picture.
	 */
	public Location getLocation() {
		return l;
	}
	
	/**
	 * This returns the JavaFX component (an ImageView object) representing the image.
	 * @return The ImageView object representing the image.
	 */
	public ImageView getImageView() {
		return iv;
	}
}
