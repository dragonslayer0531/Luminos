package tk.luminos.graphics.gui;

import java.util.ArrayList;
import java.util.List;

import tk.luminos.graphics.textures.GUITexture;
import tk.luminos.input.MousePosition;
import tk.luminos.maths.vector.Vector2f;

/**
 * Creates a GUI Slider interface
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class GUISlider implements GUIObject {
	
	private int left_arrow, right_arrow;
	private int slider_background, slider;
	private float slider_location;
	private Vector2f scale, location;
	
	private GUITexture slide, slide_background;
	private GUIButton l_arrow, r_arrow;
	
	private boolean isActive = false;

	/**
	 * Constructor
	 * 
	 * @param left_arrow			GPU id of left arrow texture
	 * @param right_arrow			GPU id of right arrow texture
	 * @param slider_background		GPU id of slider background texture
	 * @param slider				GPU id of slider texture
	 * @param slider_location		Slider location
	 * @param scale					Scale of slider
	 * @param location				Initial location of slider
	 */
	public GUISlider(int left_arrow, int right_arrow, int slider_background, int slider, float slider_location, Vector2f scale, Vector2f location) {
		assert(scale.y < scale.x);
		this.left_arrow = left_arrow;
		this.right_arrow = right_arrow;
		this.slider_background = slider_background;
		this.slider = slider;
		this.slider_location = slider_location;
		this.scale = scale;
		this.location = location;
		init();
	}
	
	/**
	 * Gets {@link GUITexture}s used
	 * 
	 * @return 	textures used
	 */
	public List<GUITexture> getTextures() {
		List<GUITexture> tex = new ArrayList<GUITexture>();
		tex.addAll(l_arrow.getTextures());
		tex.addAll(r_arrow.getTextures());
		tex.add(slide_background);
		tex.add(slide);
		return tex;
	}

	/**
	 * Gets the location of the click on the slider
	 * 
	 * @return		Location of the click on the slider.  Null if not clicked
	 */
	public Vector2f getClickLocation() {
		Vector2f mouse_loc = new Vector2f((float) MousePosition.getX(), (float) MousePosition.getY());
		poll(mouse_loc);
		if(isActive) 
			return mouse_loc;
		else 
			return null;
	}
	
	/**
	 * Updates the slider location
	 * 
	 * @param mouse_loc		Mouse location
	 */
	public void updateSliderLocation(Vector2f mouse_loc) {
		float hs = slide_background.getScale().x;
		float x_center = slide_background.getPosition().x;
		if(mouse_loc.x < x_center - hs) {
			slider_location = 0;
			slide.setPosition(new Vector2f(location.x - slide_background.getScale().x + slide_background.getScale().x * 2 * slider_location / 100, location.y));
		} else if(mouse_loc.x > x_center + hs) {
			slider_location = 100;
			slide.setPosition(new Vector2f(location.x - slide_background.getScale().x + slide_background.getScale().x * 2 * slider_location / 100, location.y));
		} else {
			slider_location = 100 * (mouse_loc.x + hs) / (2 * hs);
			slide.setPosition(new Vector2f(location.x - slide_background.getScale().x + slide_background.getScale().x * 2 * slider_location / 100, location.y));
		}
		
		if(l_arrow.isDown()) 
			if(slider_location < 5) slider_location = 0;
			else slider_location -= 5;
		else if(r_arrow.isDown())
			if(slider_location > 95) slider_location = 100;
			else slider_location += 5;
		slide.setPosition(new Vector2f(location.x - slide_background.getScale().x + slide_background.getScale().x * 2 * slider_location / 100, location.y));
	}
	
//*************************************************Private Methods***********************************************//
	
	/**
	 * Initializes GUISlider instance
	 */
	private void init() {
		Vector2f arrowScale = new Vector2f(scale.y, scale.y);
		Vector2f slideScale = new Vector2f(scale.x - (2 * arrowScale.x), scale.y * .8f);
		Vector2f sliderScale = new Vector2f(scale.y, scale.y);
		
		Vector2f left_arrow_loc = new Vector2f(location.x - slideScale.x - arrowScale.x, location.y);
		Vector2f right_arrow_loc = new Vector2f(location.x + slideScale.x + arrowScale.x, location.y);
		if(slider_location > 100) slider_location = 100;
		if(slider_location < 0) slider_location = 0;
		Vector2f slider_loc = new Vector2f(location.x - slideScale.x + slideScale.x * 2 * slider_location / 100, location.y);
		l_arrow = new GUIButton(left_arrow, left_arrow, left_arrow_loc, arrowScale);
		r_arrow = new GUIButton(right_arrow,right_arrow, right_arrow_loc, arrowScale);
		slide = new GUITexture(slider, slider_loc, sliderScale);
		slide_background = new GUITexture(slider_background, location, slideScale);
	}
	
	/**
	 * Polls the GUI Slider
	 * 
	 * @param mouse		Mouse locaiton
	 */
	private void poll(Vector2f mouse) {
		Vector2f center = location;
		if(center.x + scale.x < mouse.x && center.x - scale.x > mouse.x) {
			if(center.y + scale.y < mouse.y && center.y - scale.y > mouse.y) isActive = true;
		} else {
			isActive = false;
		}
	}

}
