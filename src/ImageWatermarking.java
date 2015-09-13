import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class ImageWatermarking {
		public ImageWatermarking(){
			
		}
        @SuppressWarnings("finally")
		public boolean waterMark(String filepath,String message) {
        	boolean result=false; 
                try {
                        File file = new File(filepath);
                        if (!file.exists()) {
                                System.out.println("File not Found");
                                return false;
                        }
                        System.out.println("file.getPath() "+file.getPath());
                        ImageIcon icon = new ImageIcon(file.getPath());
                        BufferedImage bufferedImage = new BufferedImage(
                                        icon.getIconWidth(), icon.getIconHeight(),
                                        BufferedImage.TYPE_INT_RGB);
                        Graphics2D g2d = (Graphics2D) bufferedImage.getGraphics();
                        g2d.drawImage(icon.getImage(), 0, 0, null);
                        AlphaComposite alpha = AlphaComposite.getInstance(
                                        AlphaComposite.SRC_OVER, 0.5f);
                        g2d.setComposite(alpha);
                        g2d.setColor(Color.white);
                        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                                        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                        g2d.setFont(new Font("Arial", Font.BOLD, 30));
                        FontMetrics fontMetrics = g2d.getFontMetrics();
                        Rectangle2D rect = fontMetrics.getStringBounds(message, g2d);
                        g2d.drawString(message, (icon.getIconWidth() - (int) rect
                                        .getWidth()) / 2, (icon.getIconHeight() - (int) rect
                                        .getHeight()) / 2);
                        g2d.dispose();
                        File fileout = new File(filepath);
                        ImageIO.write(bufferedImage, "jpg", fileout);
                        result=true;
                } catch (Exception ioe) {
                	ioe.printStackTrace();
                	 result=false;
                }
                finally{
                	return result;
                }
        }
}