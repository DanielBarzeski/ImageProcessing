import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Info {
    public static final int IMAGE_WIDTH = 700; // minimum = 500
    public static final int IMAGE_HEIGHT = 350; // minimum = 300
    public static final int IMAGE_START_X = 20;
    public static final int IMAGE_START_Y = 20;
    public static final Rectangle IMAGE_DISPLAY_BOUNDS =
            new Rectangle(10,10,
                    IMAGE_WIDTH+10+IMAGE_START_X*2,IMAGE_HEIGHT+10+IMAGE_START_Y*2);
    public static final Rectangle MENU_BOUNDS =
            new Rectangle(IMAGE_DISPLAY_BOUNDS.x,IMAGE_DISPLAY_BOUNDS.height+15,
                    IMAGE_DISPLAY_BOUNDS.width,IMAGE_DISPLAY_BOUNDS.height/2);
    public static final Dimension FRAME_BOUNDS = new Dimension(IMAGE_DISPLAY_BOUNDS.width+40,
            IMAGE_DISPLAY_BOUNDS.height + MENU_BOUNDS.height+70);
    public static final Dimension FRAME_START_BOUNDS = new Dimension(IMAGE_DISPLAY_BOUNDS.width+40,
            IMAGE_DISPLAY_BOUNDS.height+70);
    public static final Rectangle BUTTON_BOUNDS =
            new Rectangle((IMAGE_DISPLAY_BOUNDS.width-IMAGE_DISPLAY_BOUNDS.x)/2-50,
                    (IMAGE_DISPLAY_BOUNDS.height-IMAGE_DISPLAY_BOUNDS.y)/2-25,100,50);
    private static final ArrayList<JButton> buttons = new ArrayList<>();

    public static ArrayList<JButton> getButtons() {
        return buttons;
    }

}
