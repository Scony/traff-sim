package traffsim;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class RescueCopter extends Coords implements Drawable, Movable
{
    public boolean devnull;
    public boolean devnullable;
    public Path actual;
    public int offset;
    public Collisional target;
    RescueCopter(Collisional target)
    {
        super(0,0);
        devnull = false;
        devnullable = false;
        Node begin = new Node(0,0);
        Node end = new Node(target.x,target.y);
        ArrayList<Node> tmp = new ArrayList<Node>();
        tmp.add(begin);
        tmp.add(end);
        actual = new Path("0 0 1 0 0 0",tmp);
        offset = 0;
        this.target = target;
    }
    public void draw(Graphics g)
    {
        g.setColor(Color.BLACK);
        g.fillOval(x-6, y-6, 11, 11);
        g.fillRect(x-2, y-10, 4, 20);
        g.fillRect(x-10, y-2, 20, 4);
        g.drawLine(x-10, y-10, x+10, y+10);
        g.drawLine(x-10, y+10, x+10, y-10);
    }
    public void move(int time)
    {
        if(!devnull)
        {
            offset += 4*time;
            if(offset<actual.length)
            {
                Coords tmp = actual.countCoords(offset);
                x = tmp.x;
                y = tmp.y;
            } else
            {
                x = actual.end.x;
                y = actual.end.y;
                if(!devnullable)
                {
                    target.setDevnull(true);
                    devnullable = true;
                    ArrayList<Node> tmp = new ArrayList<Node>();
                    tmp.add(actual.end);
                    tmp.add(actual.begin);
                    Path neu = new Path("0 0 1 0 0 0",tmp);
                    actual = neu;
                    offset = 0;
                } else
                {
                    devnull = true;
                }
            }
        }
    }
}
