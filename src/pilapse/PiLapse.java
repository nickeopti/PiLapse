package pilapse;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * @author Nicklas Boserup
 */
public class PiLapse {
    
    static final String BUF_DIR = "/home/pi/Pictures/PiLapse/Buf/";
    static final String IMG_DIR = "/home/pi/Pictures/PiLapse/Img/";
    static final int BUF_SIZE = 8;
    static int SHUTTER_SPEED = 40_000;
    
    static int fileNumber = -1;
    
    public static void main(String[] args) throws InterruptedException, IOException {
        System.out.println("PiLapse is booting...");
        
        ProcessBuilder imageCapture = new ProcessBuilder("raspistill", "-n", "-t", "1000", "-w", "1920", "-h", "1080",
                "-ex", "off", "-awb", "auto", "-awbg", "1.5,1.2", "-ifx", "none", "-ss", ""+SHUTTER_SPEED, "-ISO", "400",
                "-o", BUF_DIR + "img0.jpg");
        clearBuffer();
        
        System.out.println("Camera initialized");
        while(true) {
            long startTime = System.currentTimeMillis();
            updateBuffer();
            imageCapture = new ProcessBuilder("raspistill", "-n", "-t", "1000", "-w", "1920", "-h", "1080",
                "-ex", "off", "-awb", "auto", "-awbg", "1.5,1.2", "-ifx", "none", "-ss", ""+SHUTTER_SPEED, "-ISO", "400",
                "-o", BUF_DIR + "img0.jpg");
            imageCapture.start();
            System.out.println("Image captured");
            
            Thread.sleep(2_500);
            
            File img = new File(BUF_DIR + "img0.jpg");
            BufferedImage image = ImageIO.read(img);
            boolean dif = false;
            for(int i = 1; i <= BUF_SIZE && !dif; i++) {
                File f = new File(BUF_DIR + "img" + i + ".jpg");
                if(f.exists())
                    dif = isDifferent(image, ImageIO.read(f));
            }
            if(dif) {
                System.out.println("Movement registered");
                String n = fileName();
                System.out.println("FileName: " + n);
                try {
                    Files.copy(img.toPath(), new File(n).toPath());
                } catch(IOException ioe) {}
            } else {
                System.out.println("No movement registered");
            }
            adjustBrightness();
            System.out.println("Execution time: " + (System.currentTimeMillis()-startTime) + " ms");
            Thread.sleep(Math.max(30_000-(System.currentTimeMillis()-startTime), 0));
        }
        
    }
    
    public static boolean isDifferent(BufferedImage img0, BufferedImage img1) {
        int diffCount = 0;
        for(int x = 0; x < 1920; x+=2) {
            for(int y = 0; y < 1080; y+=2) {
                Color c0 = new Color(img0.getRGB(x, y), true);
                Color c1 = new Color(img1.getRGB(x, y), true);
                if(
                    (Math.abs(c0.getRed()-c1.getRed()) > 52) ||
                    (Math.abs(c0.getGreen()-c1.getGreen()) > 52) ||
                    (Math.abs(c0.getBlue()-c1.getBlue()) > 52)
                    )
                    diffCount++;
            }
        }
        
        System.out.println("DiffCount = " + diffCount);
        return diffCount > 250;
    }
    
    public static void clearBuffer() {
        for(int i = BUF_SIZE; i >= 0;  i--) {
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
        for(int i = BUF_SIZE; i >= 0;  i--) {
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
    
    public static double calculateBrightness(BufferedImage img) {
        int sum = 0, count = 0;
        for(int x = 0; x < 1920; x += 2) {
            for(int y = 0; y < 1080; y += 2) {
                Color c = new Color(img.getRGB(x, y), true);
                sum += (c.getRed()+c.getGreen()+c.getBlue())/3d;
                count++;
            }
        }
        return ((double) sum) / count;
    }
    
    public static void adjustBrightness() {
        double sum = 0, count = 0;
        for(int i = BUF_SIZE; i >= 0;  i--) {
            try {
                sum += calculateBrightness(ImageIO.read(new File(BUF_DIR + "img" + i + ".jpg")));
                count++;
            } catch (IOException ex) {
                //Logger.getLogger(PiLapse.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        double target = 135d, factor = 10d;
        System.out.println("Calculated Brightness: " + (sum/count) + "\t" + (target - sum/count) + " \t " + ((target - sum/count)/256d/factor + 1));
        if(sum == 0 || count == 0)
            return;
        SHUTTER_SPEED *= (target - sum/count) / 256d / factor + 1;
        if(SHUTTER_SPEED > 300_000)
            SHUTTER_SPEED = 300_000;
        System.out.println("Shutter Speed " + SHUTTER_SPEED);
    }
    
}
