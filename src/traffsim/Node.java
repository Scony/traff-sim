package traffsim;

import java.awt.Graphics;
import java.util.ArrayList;

public class Node
{
    static int nextId = 0;
    public int x;
    public int y;
    public final int id;
    public ArrayList<Path> nextPaths;
    public ArrayList<Path> allPaths;
    Node(int x, int y)
    {
        id = Node.nextId++;
        this.x = x;
        this.y = y;
        nextPaths = new ArrayList<Path>();
        allPaths = new ArrayList<Path>();
    }
    Node(String in)
    {
        id = Node.nextId++;
        String [] tmp = in.split(" ");
        x = Integer.parseInt(tmp[1]);
        y = Integer.parseInt(tmp[2]);
        nextPaths = new ArrayList<Path>();
        allPaths = new ArrayList<Path>();
    }
    public void draw(int r, Graphics g)
    {
        g.fillOval(x-r, y-r, r*2, r*2);
    }
    @Override
    public String toString()
    {
        return "(" + x + ";" + y + ")[" + id + "]";
    }
}
