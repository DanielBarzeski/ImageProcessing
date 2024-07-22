import javax.swing.*;
public class Main {
    public static void main(String[] args) {
        FileManager.resetDirectory();
        FileManager.loadImagesFromDirectory();
        System.out.println("application running...");
        javax.swing.SwingUtilities.invokeLater(() -> {
            JFrame ImageProcessing = new Window();
            ImageProcessing.setVisible(true);
        });
    }
}
