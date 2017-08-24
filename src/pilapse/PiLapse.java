package pilapse;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import javax.imageio.ImageIO;

/**
 * @author Nicklas Boserup
 */
public class PiLapse {
    
    static final String BUF_DIR = "/home/pi/Pictures/PiLapse/Buf/";
    static final String IMG_DIR = "/home/pi/Pictures/PiLapse/Img/";
    static int SHUTTER_SPEED = 10_000;
    
    static int fileNumber = -1;
    
    public static void main(String[] args) throws InterruptedException, IOException {
        System.out.println("PiLapse is booting...");
        
        ProcessBuilder imageCapture = new ProcessBuilder("raspistill", "-n", "-t", "1000", "-w", "1920", "-h", "1080",
                "-ex", "off", "-awb", "off", "-ifx", "none", /*"-ss", ""+SHUTTER_SPEED,*/ "-ISO", "400",
                "-o", BUF_DIR + "img0.jpg");
        clearBuffer();
        
        System.out.println("Camera initialized");
        while(true) {
            long startTime = System.currentTimeMillis();
            updateBuffer();
            imageCapture.start();
            System.out.println("Image captured");
            
            Thread.sleep(2_000);
            
            File img = new File(BUF_DIR + "img0.jpg");
            BufferedImage image = ImageIO.read(img);
            boolean dif = false;
            for(int i = 1; i <= 10 && !dif; i++) {
                File f = new File(BUF_DIR + "img" + i + ".jpg");
                if(f.exists())
                    dif = isDifferent(image, ImageIO.read(f));
            }
            if(dif) {
                System.out.println("Movement registered");
                String n = fileName();
                System.out.println("FileName: " + n);
                Files.copy(img.toPath(), new File(n).toPath());
            } else {
                System.out.println("No movement registered");
            }
            System.out.println("Execution time: " + (System.currentTimeMillis()-startTime) + " ms");
            Thread.sleep(Math.max(10_000-(System.currentTimeMillis()-startTime), 0));
        }
        
    }
    
    public static boolean isDifferent(BufferedImage img0, BufferedImage img1) {
        int diffCount = 0;
        for(int x = 0; x < 1920; x+=2) {
            for(int y = 0; y < 1080; y+=2) {
                Color c0 = new Color(img0.getRGB(x, y), true);
                Color c1 = new Color(img1.getRGB(x, y), true);
                if(
                    Math.abs(c0.getRed()-c1.getRed()) > 20 ||
                    Math.abs(c0.getGreen()-c1.getGreen()) > 20 ||
                    Math.abs(c0.getBlue()-c1.getBlue()) > 20
                    )
                    diffCount++;
            }
        }
        
        System.out.println("DiffCount = " + diffCount);
        return diffCount > 500;
    }
    
    public static void clearBuffer() {
        for(int i = 10; i >= 0;  i--) {
            try {
                Files.delete(new File(BUF_DIR + "img" + i + ".jpg").toPath());
            } catch (IOException ex) {
                //Logger.getLogger(PiLapse.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public static void updateBuffer() {
        try {
            Files.delete(new File(BUF_DIR + "img10.jpg").toPath());
        } catch (IOException ex) { //Probably means the file didn't ever exist
            //Logger.getLogger(PiLapse.class.getName()).log(Level.SEVERE, null, ex);
        }
        for(int i = 10; i >= 0;  i--) {
            new File(BUF_DIR + "img" + i + ".jpg").renameTo(new File(BUF_DIR + "img" + (i+1) + ".jpg"));
        }
    }
    
    public static String fileName() {
        if(fileNumber < 0) {
            File f;
            do {
                f = new File(IMG_DIR + "img" + (++fileNumber) + ".jpg");
            } while(f.exists());
        }
        
        return IMG_DIR + "img" + (fileNumber++) + ".jpg";
    }
    
    public static void calculateBrightness() {
        
    }
    
}
