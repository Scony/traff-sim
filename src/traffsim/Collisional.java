package traffsim;

import javax.swing.JTextArea;

/*klasointerfejs dajacy mozliwosc dziedziczenia po sobie, dzieki czemu przy obsludze kolizji
  mozna odwolywac sie bezposrednio do x y tudziez r (dla wygody polaczony z interfejsem Focusable)*/

public abstract class Collisional extends Coords
{
    Collisional(int x, int y)
    {
        super(x,y);
    }
    Collisional(int x, int y, int r)
    {
        super(x,y,r);
    }
    abstract boolean isCauser();
    abstract void addCoColliding(Collisional c);
    abstract void setDevnull(boolean devnull);
    abstract void setFocus(boolean focus, JTextArea txt);
    abstract void setEnabled(boolean enabled);
    abstract boolean getEnabled();
}
