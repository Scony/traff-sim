package traffsim;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JPanel;

public class Stage extends JPanel
{;
    public ArrayList<Node> nodes; //wierzcholki grafu
    public ArrayList<Path> paths; //krawedzie grafu (skierowane)
    public ArrayList<Drawable> drawables;
    public ArrayList<Movable> movables;
    public ArrayList<Movable> factories; //fabryki obiektow dynamicznych (aut ludzi)
    public ArrayList<Collisional> collisionals;
    public ArrayList<RescueCopter> copters;
    Stage(int width, int height)
    {
        setPreferredSize(new Dimension(width,height));
        nodes = new ArrayList<Node>();
        paths = new ArrayList<Path>();
        drawables = new ArrayList<Drawable>();
        movables = new ArrayList<Movable>();
        factories = new ArrayList<Movable>();
        collisionals = new ArrayList<Collisional>();
        copters = new ArrayList<RescueCopter>();
    }
    public void loadFromFile(String fileName) throws IOException
    {
        RandomAccessFile file = new RandomAccessFile(fileName,"r");
        int n = Integer.parseInt(file.readLine());
        for(int i = 0; i < n; i++)
            nodes.add(new Node(file.readLine()));
        int m = Integer.parseInt(file.readLine());
        for(int i = 0; i < m; i++)
        {
            Path tmp = new Path(file.readLine(),nodes);
            paths.add(tmp);
            drawables.add(tmp);
        }
        int k = Integer.parseInt(file.readLine());
        for(int i = 0; i < k; i++)
            movables.add(new Controller(file.readLine(),paths));
        int l = Integer.parseInt(file.readLine());
        for(int i = 0; i < l; i++)
            drawables.add(0, new Polygonal(file.readLine(),nodes));
        int o = Integer.parseInt(file.readLine());
        for(int i = 0; i < o; i++)
            drawables.add(0, new Picture(file.readLine()));
        int p = Integer.parseInt(file.readLine());
        for(int i = 0; i < p; i++)
            drawables.add(0,new Text(file.readLine()));
        int q = Integer.parseInt(file.readLine());
        for(int i = 0; i < q; i++)
            factories.add(new HumanFactory(file.readLine(),this));
        int r = Integer.parseInt(file.readLine());
        for(int i = 0; i < r; i++)
            factories.add(new CarFactory(file.readLine(),this));
        file.close();
    }    
    @Override
    public void paintComponent(Graphics g)
    {
        int w = getWidth();
        int h = getHeight();
        g.setColor(Color.lightGray);
        g.fillRect(0,0,w,h);
        g.setColor(Color.BLACK);
        for(Drawable i : drawables)
            i.draw(g);
    }
    public void move(int time)
    {
        for(Iterator i = copters.iterator(); i.hasNext();) //usuwanie niepotrzebnych helikopterow
        {
            RescueCopter tmp = (RescueCopter)i.next();
            if(tmp.devnull)
            {
                drawables.remove(tmp);
                movables.remove(tmp);
                i.remove();
            }
        }
        for(Collisional i : collisionals) //wykrywanie kolizji kazdy-z-kazdym
        {
            if(i.isCauser())
            {
                for(Collisional j : collisionals)
                {
                    if(i!=j && (i.getEnabled() || j.getEnabled()))
                    {
                        double dist = Math.sqrt(Math.pow(i.x-j.x, 2)+Math.pow(i.y-j.y, 2));
                        if(!j.isCauser() && ((Human)j).sexappeal > 99 && dist < 100)
                            ((Car)i).dissociation += 2;
                        if(dist < i.r + j.r)
                        {
                            if(i.getEnabled() && j.getEnabled())
                            {
                                RescueCopter rc = new RescueCopter(i);
                                drawables.add(rc);
                                movables.add(rc);
                                copters.add(rc);
                                i.addCoColliding(j);
                            } else if(i.getEnabled())
                                j.addCoColliding(i);
                            else
                                i.addCoColliding(j);
                            i.setEnabled(false);
                            j.setEnabled(false);
                        }
                    }
                }
            }
        }
        for(Movable i : factories)
            i.move(time);
        for(Movable i : movables)
            i.move(time);
        repaint();
    }
}
