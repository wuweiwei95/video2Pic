package video2pic;

import org.bytedeco.javacv.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by wuweiwei on 04/05/2017.
 */

public class video2Pic {

    public static void videoProcess(File inputFile, String outputPath) throws IOException {
        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(inputFile);
        long startTime = System.currentTimeMillis();
        System.out.println("Process file " + inputFile);

        try {
            grabber.start();
        } catch (Exception e) {
            // TODO exception print
            e.printStackTrace();
        }

        long frameLength = grabber.getLengthInFrames();

        // To grab frames per 0.5 seconds
        for (int i=1; i < frameLength; i += grabber.getFrameRate()/2) {
            Java2DFrameConverter converter = new Java2DFrameConverter();
            grabber.setFrameNumber(i);

            BufferedImage bi = converter.getBufferedImage(grabber.grab());
            ImageIO.write(bi, "jpg", new File(outputPath + "frame" + i + ".jpg"));
        }

        // To know number for each video
        System.out.println(frameLength);

        // get the process time
        long endTime   = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("Video decode take time " + totalTime/1000 + "s");
        try {
            grabber.stop();
        } catch (FrameGrabber.Exception e) {
            e.printStackTrace();
        }


    }

    public static void main(String[] args) {

        File[] videoFiles = new File("/Users/wuweiwei/code/Java/video2Pic/videos/").listFiles();
        String outputPath = "/Users/wuweiwei/code/Java/video2Pic/out-pics/";

        // file iterator process
        for (File file: videoFiles){
            String name = file.getName();
            String extension = name.substring(name.lastIndexOf(".") + 1);
            try {
                if (file.isFile() && (extension.equals("mp4"))) {
                    videoProcess(file, outputPath);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}