import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class ImageDisplay extends JPanel {
    private MouseClickListener[] mouse;
    private boolean imageOpened;
    private int index;
    public ImageDisplay() {
        setBackground(Color.DARK_GRAY);
        setBounds(Info.IMAGE_DISPLAY_BOUNDS);
        setLayout(null);

        getMouseListener();

        getButton();

        setFocusable(true);
        requestFocus();
    }
    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        if (imageOpened) {
            graphics.drawImage(FileManager.getImageFromIndex(index), Info.IMAGE_START_X, Info.IMAGE_START_Y, this);
            drawMouseListener(graphics);
            if_buttons_clicked();
        }
        revalidate();
        repaint();
    }






    private void if_buttons_clicked(){
        if (MenuDisplay.modifyImageButtonClicked()) {
            applyChangeToImage();
            index++;
        }
        if (MenuDisplay.resetButtonClicked()){
            index = 0 ;
            setAllMouseListeners();
            for (int i = FileManager.getSize()-1; i > 0; i--) {
                FileManager.deleteImageAtIndex(i);
            }
        }
        if (MenuDisplay.goBackButtonClicked()){
            if (index != 0) {
                FileManager.deleteImageAtIndex(index);
                index--;
            }
        }
        if (MenuDisplay.saveToFileButtonClicked()){
            FileManager.saveDirectoryToFile("output_images.zip");
        }
        MenuDisplay.setButtons(false);
    }
    private void setAllMouseListeners(){
        for (MouseClickListener mouseClickListener : mouse) {
            mouseClickListener.setAll();
        }
    }

    private void getMouseListener(){
        mouse = new MouseClickListener[]{new MouseClickListener(Info.IMAGE_START_X,Info.IMAGE_START_Y),
                new MouseClickListener(Info.IMAGE_START_X,Info.IMAGE_HEIGHT+Info.IMAGE_START_Y),
                new MouseClickListener(Info.IMAGE_WIDTH+Info.IMAGE_START_X,Info.IMAGE_START_Y),
                new MouseClickListener(Info.IMAGE_WIDTH+Info.IMAGE_START_X,Info.IMAGE_HEIGHT+Info.IMAGE_START_Y)};
        for (MouseClickListener mouseClickListener : mouse) {
            addMouseListener(mouseClickListener);
            addMouseMotionListener(mouseClickListener);
        }
    }
    private void getButton(){
        Info.getButtons().add(new JButton("open image"));
        add(Info.getButtons().get(0));
        Info.getButtons().get(0).setBackground(Color.WHITE);
        Info.getButtons().get(0).setBounds(Info.BUTTON_BOUNDS);
        Info.getButtons().get(0).addActionListener(e -> openImage());
    }
    private void openImage(){
        File file = FileManager.getImageFromDesktop();
        BufferedImage img = FileManager.loadImageFromFile(file.getAbsolutePath());
        ImageManipulator.fixBounds(img);
        if (img != null) {
            FileManager.saveImageToFile(img, "png");
            remove(Info.getButtons().get(0));
            imageOpened = true;
        }
    }
    private void drawMouseListener(Graphics graphics){
        Color[] colors = {Color.blue, Color.green, Color.red, Color.yellow};
        int a = MouseClickListener.dotSize;
        for (int i = 0; i < mouse.length; i++) {
            graphics.setColor(colors[i]);
            if (mouse[i].getX() < Info.IMAGE_START_X)
                mouse[i].setX(Info.IMAGE_START_X);
            if (mouse[i].getX() > Info.IMAGE_WIDTH+Info.IMAGE_START_X)
                mouse[i].setX(Info.IMAGE_WIDTH+Info.IMAGE_START_X);
            if (mouse[i].getY() < Info.IMAGE_START_Y)
                mouse[i].setY(Info.IMAGE_START_Y);
            if (mouse[i].getY() > Info.IMAGE_HEIGHT+Info.IMAGE_START_Y)
                mouse[i].setY(Info.IMAGE_HEIGHT+Info.IMAGE_START_Y);
            graphics.fillOval(mouse[i].getX() - a / 2 + 1, mouse[i].getY() - a / 2 + 1, a, a);
            graphics.setColor(Color.BLACK);
            if (i == 1 || i == 2) {
                graphics.drawLine(mouse[0].getX(), mouse[0].getY(), mouse[i].getX(), mouse[i].getY());
            }if (i == 3) {
                graphics.drawLine(mouse[2].getX(), mouse[2].getY(), mouse[3].getX(), mouse[3].getY());
                graphics.drawLine(mouse[1].getX(), mouse[1].getY(), mouse[3].getX(), mouse[3].getY());
            }
        }

    }
    private void applyChangeToImage(){
        int x = Info.IMAGE_START_X;
        int y = Info.IMAGE_START_Y;
        int x1 = mouse[0].getX() - x, y1 = mouse[0].getY() - y,
                x2 = mouse[1].getX() - x, y2 = mouse[1].getY() - y,
                x3 = mouse[2].getX() - x, y3 = mouse[2].getY() - y,
                x4 = mouse[3].getX() - x, y4 = mouse[3].getY() - y;
        ImageManipulator.setPolygonBounds(x1,y1,x2,y2,x4,y4,x3,y3);

        boolean reqBlackAndWhite = MenuDisplay.getButtonClicked()[3];
        boolean reqPosterize = MenuDisplay.getButtonClicked()[4];
        boolean reqPixelate = MenuDisplay.getButtonClicked()[5];
        boolean reqDarker = MenuDisplay.getButtonClicked()[6];
        boolean reqSolarize = MenuDisplay.getButtonClicked()[7];
        boolean reqContrast = MenuDisplay.getButtonClicked()[8];
        boolean reqSepia = MenuDisplay.getButtonClicked()[9];
        boolean reqLighter = MenuDisplay.getButtonClicked()[10];
        boolean reqNegative = MenuDisplay.getButtonClicked()[11];
        boolean reqAddNoise = MenuDisplay.getButtonClicked()[12];
        boolean reqVintage = MenuDisplay.getButtonClicked()[13];
        if (reqPosterize) {
            FileManager.saveImageToFile(ImageManipulator.applyPosterize(FileManager.getImageFromIndex(index), 54), "png");
        }if (reqPixelate) {
            FileManager.saveImageToFile(ImageManipulator.applyPixelate(FileManager.getImageFromIndex(index), 4), "png");
        }if (reqDarker) {
            FileManager.saveImageToFile(ImageManipulator.applyDarker(FileManager.getImageFromIndex(index), 0.3), "png");
        }if (reqSolarize) {
            FileManager.saveImageToFile(ImageManipulator.applySolarize(FileManager.getImageFromIndex(index)), "png");
        }if (reqContrast) {
            FileManager.saveImageToFile(ImageManipulator.applyContrast(FileManager.getImageFromIndex(index), 5), "png");
        }if (reqSepia) {
            FileManager.saveImageToFile(ImageManipulator.applySepia(FileManager.getImageFromIndex(index)), "png");
        }if (reqLighter) {
            FileManager.saveImageToFile(ImageManipulator.applyLighter(FileManager.getImageFromIndex(index), 0.3), "png");
        }if (reqNegative) {
            FileManager.saveImageToFile(ImageManipulator.applyNegative(FileManager.getImageFromIndex(index)), "png");
        }if (reqAddNoise) {
            FileManager.saveImageToFile(ImageManipulator.applyAddNoise(FileManager.getImageFromIndex(index), 40), "png");
        }if (reqVintage) {
            FileManager.saveImageToFile(ImageManipulator.applyVintage(FileManager.getImageFromIndex(index)), "png");
        }if (reqBlackAndWhite) {
            FileManager.saveImageToFile(ImageManipulator.applyBlackAndWhite(FileManager.getImageFromIndex(index)), "png");
        }
    }
}
