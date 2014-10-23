package traffsim;

import java.util.ArrayList;
import java.util.Iterator;

public class CarFactory implements Movable
{
    public Node begin;
    public Node end;
    public int interval;
    public int counter;
    public int speed;
    public int limit;
    public ArrayList<Car> myCars;
    Stage gameMap;
    CarFactory(Node begin, Node end, int interval, int speed, int limit, Stage gameMap)
    {
        this.begin = begin;
        this.end = end;
        this.interval = interval;
        this.speed = speed;
        this.limit = limit;
        this.gameMap = gameMap;
        counter = interval;
        myCars = new ArrayList<Car>();
    }
    CarFactory(String in, Stage gameMap)
    {
        String [] tmp = in.split(" ");
        begin = gameMap.nodes.get(Integer.parseInt(tmp[0]));
        end = gameMap.nodes.get(Integer.parseInt(tmp[1]));
        interval = Integer.parseInt(tmp[2]);
        speed = Integer.parseInt(tmp[3]);
        limit = Integer.parseInt(tmp[4]);
        this.gameMap = gameMap;
        counter = interval;
        myCars = new ArrayList<Car>();
    }
    public void move(int time)
    {
        for(Iterator i = myCars.iterator(); i.hasNext();)
        {
            Car tmp = (Car)i.next();
            if(tmp.devnull)
            {
                tmp.actual.cars.remove(tmp);
                gameMap.drawables.remove(tmp);
                gameMap.movables.remove(tmp);
                gameMap.collisionals.remove(tmp);
                i.remove();
                limit++;
            }
        }
        if(limit>0)
        {
            counter -= time;
            if(counter<=0)
            {
                Car c = new Car(begin,end,speed);
                gameMap.drawables.add(c);
                gameMap.movables.add(c);
                gameMap.collisionals.add(c);
                myCars.add(c);
                counter = interval;
                limit--;
            }
        }
    }
}
