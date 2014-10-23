package traffsim;

import java.util.ArrayList;

//obiekt stymulujacy sygnalizacje swietlna (interwal 1337 sluzy do obslugi parkingow [wydluza czas czekania])

public class Controller implements Movable
{
    public ArrayList<Path> paths;
    public int actual; //aktualny odmrozony
    public int sleep; //czas przejsciowy
    public int warm; //czas odmrozenia
    public int warmTime;
    Controller(String in, ArrayList<Path> paths)
    {
        this.paths = new ArrayList<Path>();
        String [] tmp = in.split(" ");
        warmTime = Integer.parseInt(tmp[0]);
        for(int i = 1; i < tmp.length; i++)
            this.paths.add(paths.get(Integer.parseInt(tmp[i])));
        for(Path p : this.paths)
            p.frozen = true;
        actual = 0;
        sleep = 5;
        warm = 0;
    }
    public void move(int time)
    {
        if(warm==0)
        {
            sleep -= time;
            if(sleep<=0)
            {
                sleep = 0;
                actual++;
                if(actual>=paths.size())
                    actual = 0;
                paths.get(actual).frozen = false;
                if(warmTime!=1337)
                    warm = warmTime;
                else
                    warm = 10;
            }
        }
        if(sleep==0)
        {
            warm -= time;
            if(warm<=0)
            {
                warm = 0;
                sleep = (int)Math.round(paths.get(actual).length) + 1;
                if(warmTime==1337)
                    sleep += 1000;
                paths.get(actual).frozen = true;
            }
        }
    }
}
