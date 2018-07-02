import javafx.scene.control.ToolBar;

import javax.swing.*;

public class Toolbar extends JToolBar {
    //private final JButton file;
    //private final JButton help;

    public Toolbar(){
        super("Tools panel");
        add(new JButton("File"));
    }
}
