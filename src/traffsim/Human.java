package traffsim;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JTextArea;

public class Human extends Collisional implements Drawable, Movable
{
    static int nextId = 0;
    public int sexappeal;
    public boolean ptp;
    public boolean devnull;
    public boolean enabled;
    public int speed;
    public Path actual;
    public boolean toEnd;
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
        for(Path i : begin.allPaths)
            if(i.begin == begin)
            {
                if(!visited.contains(i.end))
                    if(DFS(i.end,end,visited,order))
                        return true;
            } else
            {
                if(!visited.contains(i.begin))
                    if(DFS(i.begin,end,visited,order))
                        return true;
            }
        order.remove(begin);
        return false;
    }
    Human(Node begin, Node end, int speed)
    {
        super(0,0,2);
        devnull = false;
        enabled = true;
        order = new ArrayList<Node>();
        coColliding = new ArrayList<Collisional>();
        boolean res = DFS(begin,end,new ArrayList<Node>(),order);
        if(res && begin!=end)
        {
            ptp = true;
            x = begin.x;
            y = begin.y;
            this.speed = speed;
            order.remove(0);
            for(Path i : begin.allPaths)
                if(i.begin == begin)
                {
                    if(i.end == order.get(0))
                    {
                        order.remove(0);
                        actual = i;
                        toEnd = true;
                        offset = 0;
                        break;
                    }
                } else
                {
                    if(i.begin == order.get(0))
                    {
                        order.remove(0);
                        actual = i;
                        toEnd = false;
                        offset = (int)Math.floor(actual.length);
                        break;
                    }
                }
            id = nextId++;
            rand = new Random(id);
            sexappeal = rand.nextInt(100) + 1;
            focus = false;
            myTextArea = null;
        } else
        {
            ptp = false;
            x = begin.x;
            y = begin.y;
            this.speed = speed;
            actual = begin.allPaths.get(0);
            if(actual.begin==begin)
            {
                toEnd = true;
                offset = 0;
            } else
            {
                toEnd = false;
                offset = (int)Math.floor(actual.length);
            }
            id = nextId++;
            rand = new Random(id);
            sexappeal = rand.nextInt(100) + 1;
            focus = false;
            myTextArea = null;
        }
    }
    Human(Path start, int speed)
    {
        this(start.begin,start.begin,speed);
    }
    public void draw(Graphics g)
    {
        if(focus)
            g.setColor(Color.WHITE);
        else if(!enabled)
            g.setColor(Color.RED);
        else if(sexappeal > 99)
            g.setColor(Color.PINK);
        else
            g.setColor(Color.BLACK);
        g.fillOval(x-2, y-2, 4, 4);
        if(focus && sexappeal > 99)
            g.drawOval(x-100, y-100, 200, 200);
    }
    public void move(int time) //ruch podobnie jak w przypadku samochodow, mozliwe poruszanie sie wbrew skierowaniu grafu
    {
        if(enabled)
        {
            if(myTextArea!=null)
                myTextArea.setText(toString());
            if(!ptp) //RND
            {
                if(toEnd)
                {
                    offset += time * speed;
                    if(offset<actual.length)
                    {
                        Coords tmp = actual.countCoords(offset);
                        x = tmp.x;
                        y = tmp.y;
                    } else
                    {
                        x = actual.end.x;
                        y = actual.end.y;
                        boolean flag = false;
                        for(Path i : actual.end.allPaths)
                            if(!i.frozen)
                            {
                                flag = true;
                                break;
                            }
                        if(flag)
                        {
                            Path tmp = actual.end.allPaths.get(rand.nextInt(actual.end.allPaths.size()));
                            while(tmp.frozen)
                                tmp = actual.end.allPaths.get(rand.nextInt(actual.end.allPaths.size()));
                            actual = tmp;
                            if(actual.begin.x == x && actual.begin.y == y)
                            {
                                toEnd = true;
                                offset = 0;
                            } else
                            {
                                toEnd = false;
                                offset = (int)Math.floor(actual.length);
                            }
                        }
                    }
                } else
                {
                    offset -= time * speed;
                    if(offset>0)
                    {
                        Coords tmp = actual.countCoords(offset);
                        x = tmp.x;
                        y = tmp.y;
                    } else
                    {
                        x = actual.begin.x;
                        y = actual.begin.y;
                        boolean flag = false;
                        for(Path i : actual.begin.allPaths)
                            if(!i.frozen)
                            {
                                flag = true;
                                break;
                            }
                        if(flag)
                        {
                            Path tmp = actual.begin.allPaths.get(rand.nextInt(actual.begin.allPaths.size()));
                            while(tmp.frozen)
                                tmp = actual.begin.allPaths.get(rand.nextInt(actual.begin.allPaths.size()));
                            actual = tmp;
                            if(actual.begin.x == x && actual.begin.y == y)
                            {
                                toEnd = true;
                                offset = 0;
                            } else
                            {
                                toEnd = false;
                                offset = (int)Math.floor(actual.length);
                            }
                        }
                    }
                }
            } else //PTP
            {
                if(toEnd)
                {
                    offset += time * speed;
                    if(offset<actual.length)
                    {
                        Coords tmp = actual.countCoords(offset);
                        x = tmp.x;
                        y = tmp.y;
                    } else
                    {
                        x = actual.end.x;
                        y = actual.end.y;
                        if(order.size()>0)
                        {
                            Node tmp = actual.end;
                            for(Path i : tmp.allPaths)
                                if(i.begin == tmp)
                                {
                                    if(i.end == order.get(0))
                                    {
                                        if(!i.frozen)
                                        {
                                            order.remove(0);
                                            actual = i;
                                            toEnd = true;
                                            offset = 0;
                                        }
                                        break;
                                    }
                                } else
                                {
                                    if(i.begin == order.get(0))
                                    {
                                        if(!i.frozen)
                                        {
                                            order.remove(0);
                                            actual = i;
                                            toEnd = false;
                                            offset = (int)Math.floor(actual.length);
                                        }
                                        break;
                                    }
                                }
                        } else
                        {
                            devnull = true;
                        }
                    }
                } else
                {
                    offset -= time * speed;
                    if(offset>0)
                    {
                        Coords tmp = actual.countCoords(offset);
                        x = tmp.x;
                        y = tmp.y;
                    } else
                    {
                        x = actual.begin.x;
                        y = actual.begin.y;
                        if(order.size()>0)
                        {
                            Node tmp = actual.begin;
                            for(Path i : tmp.allPaths)
                                if(i.begin == tmp)
                                {
                                    if(i.end == order.get(0))
                                    {
                                        if(!i.frozen)
                                        {
                                            order.remove(0);
                                            actual = i;
                                            toEnd = true;
                                            offset = 0;
                                        }
                                        break;
                                    }
                                } else
                                {
                                    if(i.begin == order.get(0))
                                    {
                                        if(!i.frozen)
                                        {
                                            order.remove(0);
                                            actual = i;
                                            toEnd = false;
                                            offset = (int)Math.floor(actual.length);
                                        }
                                        break;
                                    }
                                }
                        } else
                        {
                            devnull = true;
                        }
                    }
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
        return "Human\n\nID: " + id +
               "\nCoords: " + x + ";" + y +
               "\nSexappeal: " + sexappeal +
               "\nMaxSpeed: " + speed +
               "\nOn Path no. " + actual.id +
               "\nWalking " + (toEnd ? "frwd" : "bwrd") + (ptp ? " to point" : " randomly") +
               (ptp ? "\n\nRealizing: " + order : "");
    }
    public void setFocus(boolean focus, JTextArea txt)
    {
        this.focus = focus;
        myTextArea = txt;
    }
    public boolean isCauser()
    {
        return false;
    }
    public boolean getEnabled()
    {
        return enabled;
    }
    public void addCoColliding(Collisional c)
    {
        coColliding.add(c);
    }
    public void setDevnull(boolean devnull)
    {
        this.devnull = devnull;
        for(Collisional i : coColliding)
            i.setDevnull(devnull);
    }
}
