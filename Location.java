import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

/**
 * This class represents the 3D location of an object in the game.
 * Some dimensions are purposefully omitted because they were not used in this game.
 * @author Eric Song
 * @version 1.0
 * @since 05-20-2019
 *
 */
public class Location {
	
	/**
	 * This represents the x coordinate of the object's location.
	 */
	private double myX;
	
	/**
	 * This represents the z coordinate of the object's location.
	 */
	private double myZ;
	
	/**
	 * This represents the last recorded x position (on the computer's 2D monitor) of the mouse.
	 */
	private double oldX;
	
	/**
	 * This represents the last recorded y position (on the computer's 2D monitor) of the mouse.
	 */
	private double oldY;
	
	/**
	 * This represents the velocity of the object in the x direction.
	 */
	private double xVel;
	
	/**
	 * This represents the velocity of the object in the z direction.
	 */
	private double zVel;
	
	/**
	 * This is essentially a double representing the camera horizontal angle, stored as a DoubleProperty for use with a PerspectiveCamera object.
	 */
	private DoubleProperty angleX;
	
	/**
	 * This is essentially a double representing the camera's vertical angle, stored as a DoubleProperty for use with a PerspectiveCamera object.
	 */
	private DoubleProperty angleY;
	
	/**
	 * This constructor initializes the angles to 0 each.
	 */
	public Location() {
		angleX = new SimpleDoubleProperty(0);
		angleY = new SimpleDoubleProperty(0);
	}
	
	/**
	 * This method sets the x coordinate of the object.
	 * @param d The x coordinate of the object.
	 */
	public void setX(double d) {
		myX = d;
	}
	
	/**
	 * This method sets the z coordinate of the object.
	 * @param d the z coordinate of the object.
	 */
	public void setZ(double d) {
		myZ = d;
	}
	
	/**
	 * This method sets the last x position of the mouse on the monitor's 2D surface.
	 * @param d The last x position of the mouse.
	 */
	public void setOldX(double d) {
		oldX = d;
	}
	
	/**
	 * This method sets the last y position of the mouse on the monitor's 2D surface.
	 * @param d The last y position of the mouse.
	 */
	public void setOldY(double d) {
		oldY = d;
	}
	
	/**
	 * This method sets the object's velocity in the x direction.
	 * @param x The object's velocity in the x direction.
	 */
	public void setXVel(double x) {
		xVel = x;
	}
	
	/**
	 * This method sets the object's velocity in the z direction.
	 * @param x The object's velocity in the z direction.
	 */
	public void setZVel(double x) {
		zVel = x;
	}
	
	/**
	 * This method gets the object's x coordinate.
	 * @return The object's x coordinate.
	 */
	public double getX() {
		return myX;
	}
	
	/**
	 * This method gets the object's z coordinate.
	 * @return The object's z coordinate.
	 */
	public double getZ() {
		return myZ;
	}
	
	/**
	 * This method gets the mouse's last x position.
	 * @return The mouse's last x position.
	 */
	public double getOldX() {
		return oldX;
	}
	
	/**
	 * This method gets the mouse's last y position.
	 * @return The mouse's last y position.
	 */
	public double getOldY() {
		return oldY;
	}
	
	/**
	 * This method gets the object's x velocity.
	 * @return The object's x velocity.
	 */
	public double getXVel() {
		return xVel;
	}
	
	/**
	 * This method gets the object's z velocity.
	 * @return The object's z velocity.
	 */
	public double getZVel() {
		return zVel;
	}
	
	/**
	 * This method gets the camera's horizontal angle.
	 * @return The horizontal angle of the camera.
	 */
	public DoubleProperty getAngleX() {
		return angleX;
	}
	
	/**
	 * This method returns the camera's vertical angle.
	 * @return The vertical angle of the camera.
	 */
	public DoubleProperty getAngleY() {
		return angleY;
	}
}
