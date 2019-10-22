package util.webP.example;

import com.luciad.imageio.webp.WebPReadParam;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class DecodeTest {
    public static void main(String args[]) throws IOException{
        String inputWebpPath = "test_pic/228.webp";
        String outputJpgPath = "test_pic/228.jpg";
        String outputJpegPath = "test_pic/228.jpeg";
        String outputPngPath = "test_pic/228.png";
    
        long st = System.currentTimeMillis();
        // Obtain a WebP ImageReader instance
        ImageReader reader = ImageIO.getImageReadersByMIMEType("image/webp").next();

        // Configure decoding parameters
        WebPReadParam readParam = new WebPReadParam();
        readParam.setBypassFiltering(false);

        // Configure the input on the ImageReader
        reader.setInput(new FileImageInputStream(new File(inputWebpPath)));

        // Decode the image
        BufferedImage image = reader.read(0, readParam);

        ImageIO.write(image, "png", new File(outputPngPath));
        ImageIO.write(image, "jpg", new File(outputJpgPath));
        ImageIO.write(image, "jpeg", new File(outputJpegPath));
        System.out.println("cost: " + (System.currentTimeMillis() - st));
    }
}
