package traffsim;

import java.util.ArrayList;
import java.util.Iterator;


public class HumanFactory implements Movable
{
    public boolean ptp;
    public Path base;
    public Node begin;
    public Node end;
    public int interval;
    public int counter;
    public int speed;
    public int limit;
    public ArrayList<Human> myHumans;
    public Stage gameMap;
    HumanFactory(Node begin, Node end, int interval, int speed, int limit, Stage gameMap)
    {
        ptp = true;
        this.begin = begin;
        this.end = end;
        this.interval = interval;
        this.speed = speed;
        this.limit = limit;
        myHumans = new ArrayList<Human>();
        this.gameMap = gameMap;
        counter = interval;
    }
    HumanFactory(String in, Stage gameMap)
    {
        String [] tmp = in.split(" ");
        ptp = true;
        begin = gameMap.nodes.get(Integer.parseInt(tmp[0]));
        end = gameMap.nodes.get(Integer.parseInt(tmp[1]));
        interval = Integer.parseInt(tmp[2]);
        speed = Integer.parseInt(tmp[3]);
        limit = Integer.parseInt(tmp[4]);
        myHumans = new ArrayList<Human>();
        this.gameMap = gameMap;
        counter = interval;

    }
    public void move(int time)
    {
        if(ptp)
            for(Iterator i = myHumans.iterator(); i.hasNext();)
            {
                Human tmp = (Human)i.next();
                if(tmp.devnull)
                {
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
            if(counter <= 0)
            {
                if(ptp)
                {
                    Human neu = new Human(begin,end,speed);
                    myHumans.add(neu);
                    gameMap.drawables.add(neu);
                    gameMap.movables.add(neu);
                    gameMap.collisionals.add(neu);
                } else
                {
                    Human neu = new Human(base,1);
                    gameMap.drawables.add(neu);
                    gameMap.movables.add(neu);
                    gameMap.collisionals.add(neu);
                }
                limit--;
                counter = interval;
            }
        }
    }
}
