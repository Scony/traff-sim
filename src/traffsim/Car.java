package traffsim;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JTextArea;

public class Car extends Collisional implements Drawable, Movable
{
    static int nextId = 0;
    final int dissociationLimit = 100;
    public int dissociation;
    public boolean ptp;
    public boolean devnull;
    public boolean enabled;
    public int speed;
    public Path actual;
    public int offset;
    public Random rand;
    public boolean focus;
    public JTextArea myTextArea;
    public ArrayList<Node> order;
    public ArrayList<Collisional> coColliding;
    public final int id;
    boolean DFS(Node begin, Node end, ArrayList<Node> visited, ArrayList<Node> order) //poszukiwanie sciezki
    {
        order.add(begin);
        visited.add(begin);
        if(begin == end)
            return true;
        for(Path i : begin.nextPaths)
            if(!visited.contains(i.end))
                if(DFS(i.end,end,visited,order))
                    return true;
        order.remove(begin);
        return false;
    }
    Car(Node begin, Node end, int speed)
    {
        super(0,0,5);
        dissociation = 0;
        devnull = false;
        enabled = true;
        order = new ArrayList<Node>();
        coColliding = new ArrayList<Collisional>();
        boolean res = DFS(begin,end,new ArrayList<Node>(),order);
        order.remove(0);
        if(res && order.size()>0)
        {
            ptp = true;
            for(Path i : begin.nextPaths)
                if(i.end == order.get(0))
                {
                    order.remove(0);
                    actual = i;
                    actual.cars.add(this);
                    offset = 0;
                    break;
                }

        } else
        {
            ptp = false;
            actual = begin.nextPaths.get(0);//sprawdzanie
            actual.cars.add(this);
            offset = 0;
        }
        x = begin.x;
        y = begin.y;
        this.speed = speed;
        id = nextId++;
        rand = new Random(id);
        focus = false;
        myTextArea = null;
    }
    Car(Path start, int speed)
    {
        this(start.begin,start.end,speed);
    }
    public void draw(Graphics g)
    {
        if(focus)
            g.setColor(Color.WHITE);
        else if(!enabled)
            g.setColor(Color.RED);
        else if(dissociation > 100)
            g.setColor(Color.LIGHT_GRAY);
        else if(dissociation > 90)
            g.setColor(Color.GRAY);
        else if(dissociation > 50)
            g.setColor(Color.DARK_GRAY);
        else
            g.setColor(Color.BLACK);
        g.fillOval(x-5, y-5, 10, 10);
    }
    public void move(int time) //realizacja ruchu (RND - losowy ruch w poszukiwaniu parkingu PTP - ruch z i do punktu)
    {
        if(enabled)
        {
            dissociation -= time;
            if(dissociation<0)
                dissociation = 0;
            if(myTextArea!=null)
                myTextArea.setText(toString());
            if(actual.checkOffset(this,offset+speed*time) || dissociation>dissociationLimit)
                offset += speed * time;
            if(actual.length-offset<=12)
            {
                for(Path i : actual.end.allPaths)
                    if((actual.end == i.begin && !i.freezable && !i.checkOffset(this, 0)) || (actual.end == i.end && !i.freezable && !i.checkOffset(this, (int)Math.floor(i.length))))
                    {
                        offset -= speed * time;
                        break;
                    }
            }
            if(!ptp) //RND
            {
                if(offset<actual.length)
                {
                    Coords tmp = actual.countCoords(offset);
                    x = tmp.x;
                    y = tmp.y;
                } else
                {
                    x = actual.end.x;
                    y = actual.end.y;
                    offset = (int)Math.floor(actual.length);
                    if(dissociation<=dissociationLimit)
                    {
                        boolean flag = false;
                        for(Path i : actual.end.nextPaths)
                            if(!i.frozen && i.checkOffset(this,0))
                            {
                                flag = true;
                                break;
                            }
                        if(flag)
                        {
                            boolean flag2 = false;
                            Path tmp = actual.end.nextPaths.get(rand.nextInt(actual.end.nextPaths.size()));
                            for(Path i : actual.end.nextPaths)
                                if(i.freezable && i.checkOffset(this, 0))
                                {
                                    tmp = i;
                                    flag2 = true;
                                    break;
                                }
                            if(!flag2)
                                while(tmp.frozen || !tmp.checkOffset(this, 0))
                                    tmp = actual.end.nextPaths.get(rand.nextInt(actual.end.nextPaths.size()));
                            actual.cars.remove(this);
                            actual = tmp;
                            actual.cars.add(this);
                            offset = 0;
                        } else
                        {
                            if(actual.end.nextPaths.isEmpty() && !actual.freezable && !devnull)
                            {
                                devnull = true;
                            }
                        }
                    } else
                    {
                        Path tmp = actual.end.nextPaths.get(rand.nextInt(actual.end.nextPaths.size()));
                        actual.cars.remove(this);
                        actual = tmp;
                        actual.cars.add(this);
                        offset = 0;
                    }
                }
            } else // PTP
            {
                if(offset<actual.length)
                {
                    Coords tmp = actual.countCoords(offset);
                    x = tmp.x;
                    y = tmp.y;
                } else
                {
                    x = actual.end.x;
                    y = actual.end.y;
                    offset = (int)Math.floor(actual.length);
                    if(order.size()>0)
                    {
                        for(Path i : actual.end.nextPaths)
                            if(i.end == order.get(0) && ((!i.frozen && i.checkOffset(this, 0)) || (dissociation>dissociationLimit)))
                            {
                                order.remove(0);
                                actual.cars.remove(this);
                                actual = i;
                                actual.cars.add(this);
                                offset = 0;
                                break;
                            }
                    } else //go RND
                        ptp = false;
                }
            }
        }
    }
    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }
    @Override
    public String toString()
    {
        return "Car\n\nID: " + id +
               "\nCoords: " + x + ";" + y +
               "\nDissociation: " + dissociation +
               "\nMaxSpeed: " + speed +
               "\nOn Path no. " + actual.id +
               "\n" + (ptp ? "Driving to point" : "Looking for parking") +
               (ptp ? "\n\nRealizing: " + order : "");
    }
    public void setFocus(boolean focus, JTextArea txt)
    {
        this.focus = focus;
        myTextArea = txt;
    }
    public boolean isCauser() //czy inicjuje kolizje
    {
        return true;
    }
    public boolean getEnabled()
    {
        return enabled;
    }
    public void addCoColliding(Collisional c) //dodaj wspolkolidujacy obiekt
    {
        coColliding.add(c);
    }
    public void setDevnull(boolean devnull) //oznacz do usuniecia
    {
        this.devnull = devnull;
        for(Collisional i : coColliding)
            i.setDevnull(devnull);
    }
}
