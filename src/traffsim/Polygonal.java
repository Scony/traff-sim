package traffsim;

import java.awt.Graphics;
import java.awt.Polygon;
import java.util.ArrayList;

public class Polygonal implements Drawable
{
    public Polygon polygon;
    Polygonal(Polygon in)
    {
        polygon = in;
    }
    Polygonal(String in, ArrayList<Node> nodes)
    {
        String [] tmp = in.split(" ");
        int n = tmp.length;
        int [] xarr = new int[n];
        int [] yarr = new int[n];
        for(int i = 0; i < tmp.length; i++)
        {
            xarr[i] = nodes.get(Integer.parseInt(tmp[i])).x;
            yarr[i] = nodes.get(Integer.parseInt(tmp[i])).y;
        }
        polygon = new Polygon(xarr,yarr,n);
    }
    public void draw(Graphics g)
    {
        g.fillPolygon(polygon);
    }
}
