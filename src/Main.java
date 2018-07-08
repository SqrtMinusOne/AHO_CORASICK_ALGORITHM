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
        SwingUtilities.invokeLater(()-> lnc.setVisible(true));
    }
}
