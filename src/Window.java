import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class Window extends JFrame {

    public Window() {
        setTitle("Image Display");

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(true);
        setSize(Info.FRAME_START_BOUNDS);

        add(new ImageDisplay());

        Info.getButtons().get(0).addActionListener(e-> setSize(Info.FRAME_BOUNDS));
        Info.getButtons().get(0).addActionListener(e-> add(new MenuDisplay()));

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                    centerFrame(Window.this);
            }
        });
        centerFrame(this);

        setLocationRelativeTo(null);
        setLayout(null);
        setVisible(true);
    }

    private static void centerFrame(Window frame) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - frame.getWidth()) / 2;
        int y = (screenSize.height - frame.getHeight()) / 2;
        frame.setLocation(x, y);
    }
}
