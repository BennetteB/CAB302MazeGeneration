import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

public class ImageConverter {

    public ImageConverter() {}

    /**
     * Converts a string into an image
     * @param string A string representing an image
     * @return an image
     */
    // Sourced from https://www.tutorialspoint.com/How-to-convert-Byte-Array-to-Image-in-java
    public BufferedImage stringToImage(String string) {
        byte[] data = Base64.getDecoder().decode(string);
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        BufferedImage image;
        try {
            image = ImageIO.read(bis);
        } catch (IOException ex){
            return null;
        }
        return image;
    }

    /**
     * converts an image to a string
     * @param icon an ImageIcon of the image
     * @param image a BufferedImage of the image
     * @return binary string representation of the given image
     */
    // Sourced from https://www.tutorialspoint.com/How-to-convert-Byte-Array-to-Image-in-java
    // Sourced from http://electrocarta.blogspot.com/2017/05/how-to-convert-imageicon-to-base64.html
    public String imageToString(ImageIcon icon, BufferedImage image) {
        if (image == null && icon != null) image = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics g = image.createGraphics();
        if (icon != null) icon.paintIcon(null, g, 0, 0);
        g.dispose();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "jpg", bos );
        } catch (IOException ex){
            return null;
        }
        byte[] data = bos.toByteArray();
        return Base64.getEncoder().encodeToString(data);
    }

}
