import javax.swing.*;
import javax.swing.border.TitledBorder;

public class ControlArea extends JPanel {

    private final JScrollPane srcPane = createPane("Source text", 0, 10);
    private final JScrollPane outPane = createPane("Output text", 0, 170);

    public ControlArea(){
        super(null);
        add(srcPane);
        add(outPane);
        Box buttons = Box.createVerticalBox();
        Box buttons1 = Box.createHorizontalBox();
        Box buttons2 = Box.createHorizontalBox();
        Box buttons3 = Box.createHorizontalBox();
        //buttons1.setAlignmentX(RIGHT_ALIGNMENT);
        //buttons.setBounds(0, 330, 290, 200); //w/o stretching
        buttons.add(buttons1);
        buttons.add(buttons2);
        buttons.add(buttons3);
        JButton file = new JButton("Choose a file");
        JButton start = new JButton("Start algorithm");
        JButton prev = new JButton("<<");
        JButton stop = new JButton("Pause / Resume");
        JButton next = new JButton(">>");
        JButton clear = new JButton("Clear");
        JButton exit = new JButton("Exit");
        //lol.setAlignmentX(CENTER_ALIGNMENT); //same
        buttons1.add(Box.createHorizontalGlue());
        buttons1.add(file);
        buttons1.add(Box.createHorizontalGlue());
        buttons1.add(start);
        buttons1.add(Box.createHorizontalGlue());
        buttons2.add(prev);
        buttons2.add(Box.createHorizontalGlue());
        buttons2.add(stop);
        buttons2.add(Box.createHorizontalGlue());
        buttons2.add(next);
        buttons3.add(Box.createHorizontalGlue());
        buttons3.add(clear);
        buttons3.add(Box.createHorizontalGlue());
        buttons3.add(exit);
        buttons3.add(Box.createHorizontalGlue());
        buttons.setBorder(new TitledBorder("Control components"));
        buttons1.setBorder((new TitledBorder("Initial actions")));
        buttons2.setBorder((new TitledBorder("Steps control")));
        buttons3.setBorder((new TitledBorder("Clear and exit")));
        add(buttons);


    }

    private JScrollPane createPane(String areaName, int x, int y){
        /*JLabel label = new JLabel(areaName);
        label.setBounds(x, y, 290, 20);
        add(label);*/ //labels
        JTextArea area = new JTextArea();
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        JScrollPane pane = new JScrollPane(area);
        //pane.setBounds(x, y, 290, 150); //same
        pane.setBorder(new TitledBorder(areaName));
        return pane;
    }
}
