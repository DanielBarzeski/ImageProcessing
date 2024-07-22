import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


public class FileManager {
    private static final ArrayList<BufferedImage> images = new ArrayList<>();
    private static final ArrayList<String> imageFormats = new ArrayList<>();
    public static final String directoryPath = "your_directory_path";
    public static final int targetWidth = Info.IMAGE_WIDTH;
    public static final int targetHeight = Info.IMAGE_HEIGHT;

    public static File getImageFromDesktop() {
        File selectedFile = null;
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Images", "jpg", "jpeg", "png", "gif");
        fileChooser.setFileFilter(filter);
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
        } else {
            System.out.println("didn't find the image, image is null");
        }
        return selectedFile;
    }

    public static void loadImagesFromDirectory() {
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        } else {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    try {
                        BufferedImage img = ImageIO.read(file);
                        if (img != null) {
                            images.add(img);
                            imageFormats.add(getImageFormat(file));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private static String getImageFormat(File file) {
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex >= 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1).toLowerCase();
        }
        return "png";
    }

    public static BufferedImage loadImageFromFile(String imagePath) {
        try {
            return ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        Image resultingImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resizedImage.createGraphics();
        g2d.drawImage(resultingImage, 0, 0, null);
        g2d.dispose();
        return resizedImage;
    }

    public static void saveImageToFile(BufferedImage image, String imageFormat) {
        try {
            BufferedImage resizedImage = resizeImage(image, targetWidth, targetHeight);
            images.add(resizedImage);
            imageFormats.add(imageFormat);
            String fileName = directoryPath + "/image_" + (images.size() - 1) + "." + imageFormat;
            ImageIO.write(resizedImage, imageFormat, new File(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int getSize() {
        return images.size();
    }

    public static BufferedImage getImageFromIndex(int index) {
        if (index >= 0 && index < images.size()) {
            return images.get(index);
        } else {
            System.out.println("Index out of range");
            return null;
        }
    }

    public static void resetDirectory() {
        File directory = new File(directoryPath);
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                file.delete();
            }
        }
        images.clear();
        imageFormats.clear();
    }

    public static void deleteImageAtIndex(int index) {
        if (index >= 0 && index < images.size()) {
            // מחיקת התמונה מהרשימה
            images.remove(index);
            String imageFormat = imageFormats.remove(index);

            // מחיקת הקובץ מהדיסק
            File fileToDelete = new File(directoryPath + "/image_" + index + "." + imageFormat);
            if (fileToDelete.exists()) {
                fileToDelete.delete();
            }

            // עדכון שמות הקבצים של שאר התמונות
            for (int i = index; i < images.size(); i++) {
                File oldFile = new File(directoryPath + "/image_" + (i + 1) + "." + imageFormats.get(i));
                File newFile = new File(directoryPath + "/image_" + i + "." + imageFormats.get(i));
                if (oldFile.exists()) {
                    oldFile.renameTo(newFile);
                }
            }
        } else {
            System.out.println("Index out of range");
        }
    }
    public static void saveDirectoryToFile(String outputFilePath) {
            File directory = new File(directoryPath);
            if (!directory.exists() || !directory.isDirectory()) {
                System.out.println("Directory does not exist or is not a directory");
                return;
            }

            File outputFile = new File(outputFilePath);
            try (FileOutputStream fos = new FileOutputStream(outputFile);
                 ZipOutputStream zos = new ZipOutputStream(fos)) {

                File[] files = directory.listFiles();
                if (files != null) {
                    for (File file : files) {
                        try (FileInputStream fis = new FileInputStream(file)) {
                            ZipEntry zipEntry = new ZipEntry(file.getName());
                            zos.putNextEntry(zipEntry);

                            byte[] buffer = new byte[1024];
                            int length;
                            while ((length = fis.read(buffer)) > 0) {
                                zos.write(buffer, 0, length);
                            }

                            zos.closeEntry();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                System.out.println("Directory saved to " + outputFilePath);

            } catch (IOException e) {
                e.printStackTrace();
            }
    }
}





