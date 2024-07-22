import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class MenuDisplay extends JPanel {
    private static final boolean[] buttonClicked = new boolean[14];
    private static final String[] buttonsName = new String[]{"reset","go back", "save to file",
            "bl & wh","posterize","pixelate","darker","solarize",
            "contrast","sepia","lighter","negative","add noise","vintage"};
    public MenuDisplay() {
        setBackground(Color.GRAY);
        setBounds(Info.MENU_BOUNDS);
        setLayout(null);
        setFocusable(true);
        requestFocus();

        addButtons();
    }
    private void addButtons(){
        int sum = 0;
        int numX = getWidth()/8;
        int numY = getHeight()/3;


        for (int i = 1; i <= buttonsName.length; i++) {
            Info.getButtons().add(i, new JButton(buttonsName[i-1]));
            add(Info.getButtons().get(i));
            Info.getButtons().get(i).setBackground(Color.WHITE);
            if (sum<8) {
                Info.getButtons().get(i).setBounds(i*10+sum*numX, 10, numX, numY);
                sum++;
            }
            if (sum>=8) {
                Info.getButtons().get(i).setBounds((i-7)*10+(sum-8)*numX, 20+numY, numX, numY);
                sum++;
            }
            int finalI = i-1;
            Info.getButtons().get(i).addActionListener(e -> buttonClicked[finalI] = true);
        }
    }
    public static boolean[] getButtonClicked() {
        return buttonClicked;
    }

    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
    }

    public static boolean modifyImageButtonClicked() {
        for (int i = 3; i < buttonClicked.length; i++) {
            boolean b = buttonClicked[i];
            if (b)
                return true;
        }
        return false;
    }
    public static boolean resetButtonClicked() {
        return buttonClicked[0];
    }
    public static boolean goBackButtonClicked() {
        return buttonClicked[1];
    }
    public static boolean saveToFileButtonClicked() {
        return buttonClicked[2];
    }
    public static void setButtons(boolean f) {
        Arrays.fill(buttonClicked, f);
    }
}
