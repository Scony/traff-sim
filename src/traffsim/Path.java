package traffsim;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class Path implements Drawable
{
    static int nextId = 0;
    public Node begin;
    public Node end;
    public int halfWidth;
    public double length;
    public boolean frozen;
    public boolean freezable;
    public double A;
    public double Apr;
    public double B;
    public double Bpr1;
    int[] xarr;
    int[] yarr;
    public ArrayList<Car> cars;
    public final int id;
    Path(String in, ArrayList<Node> nodes)
    {
        String [] tmp = in.split(" ");
        begin = nodes.get(Integer.parseInt(tmp[1]));
        begin.allPaths.add(this);
        begin.nextPaths.add(this);
        end = nodes.get(Integer.parseInt(tmp[2]));
        end.allPaths.add(this);
        halfWidth = Integer.parseInt(tmp[3]);
        if(Integer.parseInt(tmp[4])==1)
            frozen = true;
        else
            frozen = false;
        if(Integer.parseInt(tmp[5])==1)
            freezable = true;
        else
            freezable = false;
        length = Math.sqrt(Math.pow(begin.x-end.x, 2)+Math.pow(begin.y-end.y, 2));
        xarr = new int[4];
        yarr = new int[4];
        if(begin.x!=end.x)
        {
            A = (double)(begin.y-end.y)/(begin.x-end.x);
            B = end.y - A * end.x;
            if(begin.y!=end.y)
            {
                Apr = (double)(-1)/A;
                Bpr1 = begin.y - Apr * begin.x;
                double Bpr2 = end.y-end.x*Apr;
                double B1 = halfWidth*Math.sqrt(A*A+1)+B;
                double B2 = (-1)*halfWidth*Math.sqrt(A*A+1)+B;
                xarr[0] = (int)Math.round((Bpr1-B2)/(A-Apr));
                yarr[0] = (int)Math.round(A*(Bpr1-B2)/(A-Apr)+B2);
                xarr[1] = (int)Math.round((Bpr1-B1)/(A-Apr));
                yarr[1] = (int)Math.round(A*(Bpr1-B1)/(A-Apr)+B1);
                xarr[3] = (int)Math.round((Bpr2-B2)/(A-Apr));
                yarr[3] = (int)Math.round(A*(Bpr2-B2)/(A-Apr)+B2);
                xarr[2] = (int)Math.round((Bpr2-B1)/(A-Apr));
                yarr[2] = (int)Math.round(A*(Bpr2-B1)/(A-Apr)+B1);
            } else
            {
                xarr[0] = begin.x;
                xarr[1] = begin.x;
                xarr[2] = end.x;
                xarr[3] = end.x;
                yarr[0] = begin.y + halfWidth;
                yarr[1] = begin.y - halfWidth;
                yarr[2] = end.y - halfWidth;
                yarr[3] = end.y + halfWidth;
            }
        } else
        {
            xarr[0] = begin.x - halfWidth;
            xarr[1] = begin.x + halfWidth;
            xarr[2] = end.x + halfWidth;
            xarr[3] = end.x - halfWidth;
            yarr[0] = begin.y;
            yarr[1] = begin.y;
            yarr[2] = end.y;
            yarr[3] = end.y;
        }
        cars = new ArrayList<Car>();
        id = Path.nextId++;
    }
    public boolean checkOffset(Car who, int offset) //sprawdzanie czy przesuniecie pojazdu nie spowoduje kolizji
    {
        if(freezable && cars.size()>0)
            if(cars.get(0)!=who)
                return false;
        for(Car i : cars)
            if(i != who && offset >= i.offset-12 && offset <= i.offset+12)
                return false;
        return true;
    }
    public Coords countCoords(int offset) //policz x i y w zaleznosci od przesuniecia na sciezce wzgledem poczatkowego wierzcholka
    {
        if(begin.x==end.x)
        {
            if(begin.y>end.y)
                return new Coords(begin.x,begin.y-offset);
            else
                return new Coords(begin.x,begin.y+offset);
        } else if(begin.y==end.y)
        {
            if(begin.x>end.x)
                return new Coords(begin.x-offset,begin.y);
            else
                return new Coords(begin.x+offset,begin.y);
        } else
        {
            double x1 = (Math.sqrt(Apr*Apr+1)*offset+B-Bpr1)/(Apr-A);
            double x2 = ((-1)*Math.sqrt(Apr*Apr+1)*offset+B-Bpr1)/(Apr-A);
            if((x1>=begin.x && x1<=end.x) || (x1<=begin.x && x1>=end.x))
            {
                int y = (int)Math.round(A*x1+B);
                return new Coords((int)Math.round(x1),y);
            } else
            {
                int y = (int)Math.round(A*x2+B);
                return new Coords((int)Math.round(x2),y);
            }
        }
    }
    public void draw(Graphics g)
    {
        Color oldColor = g.getColor();
        if(!frozen && (!freezable || cars.isEmpty()))
            g.setColor(new Color(60,179,113));//lekki zielony
        else
            g.setColor(new Color(205,91,69));//lekki czerwony
        g.fillPolygon(xarr, yarr, 4);
        begin.draw(halfWidth, g);
        end.draw(halfWidth, g);
        g.setColor(oldColor);
    }
    @Override
    public String toString()
    {
        return begin + "->" + end + "[[" + id + "]]";
    }
}
