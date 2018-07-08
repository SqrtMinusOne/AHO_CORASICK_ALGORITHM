import ru.eltech.ahocorasick.ui.Launcher;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Launcher lnc = new Launcher();
        try {
            Image icon = ImageIO.read(Launcher.class.getResourceAsStream("icon.png"));
            lnc.setIconImage(icon);
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int y = (int)(screen.getHeight()-550)/2;
        int x = (int)(screen.getWidth()-1000)/2;
        lnc.setLocation(x,y);
        SwingUtilities.invokeLater(()-> lnc.setVisible(true));
    }
}
