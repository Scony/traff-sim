package traffsim;

public class Coords
{
    public int x;
    public int y;
    public int r;
    Coords(int x, int y)
    {
        this.x = x;
        this.y = y;
        r = 0;
    }
    Coords(int x, int y, int r)
    {
        this.x = x;
        this.y = y;
        this.r = r;
    }
}
