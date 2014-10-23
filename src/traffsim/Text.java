package traffsim;

import java.awt.Graphics;

public class Text extends Coords implements Drawable
{
    public String text;
    Text(int x, int y, String text)
    {
        super(x,y);
        this.text = text;
    }
    Text(String in)
    {
        super(0,0);
        String [] tmp = in.split(";");
        this.x = Integer.parseInt(tmp[0]);
        this.y = Integer.parseInt(tmp[1]);
        text = tmp[2];
    }
    public void draw(Graphics g)
    {
        g.drawString(text, x, y);
    }
}
