package traffsim;

import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import java.io.IOException;
import static traffsim.SwingConsole.*;

public class Main
{
    public static void main(String[] args)
    {
        try
        {
            JOptionPane.showMessageDialog(null, "Select *.map file to load...");
            final JFileChooser fc = new JFileChooser();
            fc.showOpenDialog(null);
            if(fc.getSelectedFile()==null)
                System.exit(0);
            Stage gameMap = new Stage(100,100);
            gameMap.loadFromFile(fc.getSelectedFile().getAbsolutePath());
            run(gameMap, 500, 500);
            while(true) //glowna petla (teoretycznie 20 FPS)
            {
                Thread.sleep(50);
                gameMap.move(1);
            }
        }
        catch(IOException any)
        {
            System.out.println("File does not exists !");
        }
        catch(Exception any)
        {
            System.out.println("Processing error ! :: " + any.toString());
        }
    }
}
