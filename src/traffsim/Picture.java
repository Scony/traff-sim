package traffsim;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;

public class Picture extends Coords implements Drawable
{
    Image img;
    Picture(int x, int y, String fileName)
    {
        super(x,y);
        ImageIcon im = new ImageIcon(fileName);
        img = im.getImage();
    }
    Picture(String in)
    {
        super(0,0);
        String [] tmp = in.split(" ");
        this.x = Integer.parseInt(tmp[0]);
        this.y = Integer.parseInt(tmp[1]);
        ImageIcon im = new ImageIcon(tmp[2]);
        img = im.getImage();
    }
    public void draw(Graphics g)
    {
        g.drawImage(img, x, y, null, null);
    }
}
