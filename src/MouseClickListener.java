import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseClickListener extends MouseAdapter {
    private int dotX;
    private int dotY;
    private int prevX;
    private int prevY;
    private boolean dragging;
    private final int startX;
    private final int startY;
    public static final int dotSize = 10;

    public MouseClickListener(int startX, int startY) {
        this.startX = startX;
        this.startY = startY;
        setAll();
    }

    // Reset to start place
    public void setAll() {
        dotX = startX;
        dotY = startY;
        prevX = 0;
        prevY = 0;
        dragging = false;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int a = dotSize;
        if (e.getX() + a >= dotX && e.getX() <= dotX + a && e.getY() + a >= dotY && e.getY() <= dotY + a) {
            dragging = true;
            prevX = e.getX();
            prevY = e.getY();
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (dragging) {
            int newX = dotX + e.getX() - prevX;
            int newY = dotY + e.getY() - prevY;
            dotX = newX;
            dotY = newY;
            prevX = e.getX();
            prevY = e.getY();
         //   System.out.println("mouse moved at: (" + dotX + ", " + dotY + ")");

        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        dragging = false;
    }

    public int getX() {
        return dotX;
    }

    public int getY() {
        return dotY;
    }

    public void setX(int x) {
        this.dotX = x;
        this.prevX = x;
    }

    public void setY(int y) {
        this.dotY = y;
        this.prevY = y;
    }
}
