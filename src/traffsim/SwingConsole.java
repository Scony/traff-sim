package traffsim;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class SwingConsole
{
    public static void run(final Stage map, final int width, final int height)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                JFrame frame = new JFrame(); //glowna ramka
                JPanel control = new JPanel(new GridLayout(2,1)); //kontener : 2rows 1col
                final JTextArea txt = new JTextArea(); //pole tekstowe
                txt.setLineWrap(true);
                txt.setEditable(false);
                JButton btn = new JButton("Clear textarea"); //przycisk czyszczacy
                btn.setMaximumSize(new Dimension(100,20));
                btn.addActionListener(new ActionListener() //on-btn-click
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        txt.setText("");
                    }
                });
                control.add(btn);
                control.add(txt);
                map.addMouseListener(new MouseAdapter() //on-mouse-click
                {
                    Collisional any = null;
                    public void mouseClicked(MouseEvent me)
                    {
                        for(Collisional i : map.collisionals)
                        {
                            Point tmp = me.getPoint();
                            if(Math.abs(tmp.x-i.x) <= 10 && Math.abs(tmp.y-i.y) <= 10)
                            {
                                if(any!=null)
                                    any.setFocus(false,null);
                                i.setFocus(true,txt);
                                any = i;
                                break;
                            }
                        }
                    }
                });
                frame.setTitle("TraffSim Console");
                //frame.setResizable(false);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.getContentPane().add(control, BorderLayout.EAST);
                frame.getContentPane().add(map);
                frame.setSize(width, height);
                frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                frame.setVisible(true);
            }
        });
    }
}
