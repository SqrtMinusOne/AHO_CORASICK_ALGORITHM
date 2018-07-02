import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class Launcher extends  JFrame{

    private final JPanel graphArea;
    private final ControlArea controlArea;
    private final Toolbar toolbar;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(()-> new Launcher().setVisible(true));
    }

    private Launcher(){
        graphArea = new JPanel();
        graphArea.setBorder(new LineBorder(Color.BLACK, 5));
        graphArea.setPreferredSize(new Dimension(700, 550));
        graphArea.setBackground(Color.white); //temporary colour

        controlArea = new ControlArea();
        controlArea.setLayout(new BoxLayout(controlArea, BoxLayout.Y_AXIS)); // stretching
        controlArea.setPreferredSize(new Dimension(300, 550));

        toolbar = new Toolbar();

        JPanel rootWindow = new JPanel();
        rootWindow.setVisible(true);
        rootWindow.setLayout(new BoxLayout(rootWindow, BoxLayout.X_AXIS));
        rootWindow.setPreferredSize(new Dimension(1000, 550));
        rootWindow.add(toolbar, BorderLayout.NORTH);
        rootWindow.add(graphArea, BorderLayout.WEST);
        rootWindow.add(Box.createHorizontalStrut(5), BorderLayout.CENTER);
        rootWindow.add(controlArea, BorderLayout.EAST);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setContentPane(rootWindow);
        pack();
    }
}
