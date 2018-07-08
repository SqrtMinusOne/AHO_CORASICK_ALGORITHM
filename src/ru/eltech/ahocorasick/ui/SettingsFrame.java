package ru.eltech.ahocorasick.ui;

import ru.eltech.ahocorasick.Settings;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;

@FunctionalInterface
interface Setter<T>{
    void set(T newValue);
}

@FunctionalInterface
interface Getter<T>{
    T get();
}

public class SettingsFrame extends JFrame {
    public SettingsFrame(){
        super();
        Image iconFile = null;
        try {
            iconFile = ImageIO.read(Launcher.class.getResourceAsStream("settings.png"));
        }
        catch (Exception e){
            e.printStackTrace();
        }
        resetButton = new JButton("Reset to defaults");
        this.setIconImage(iconFile);
        this.setTitle("Settings");
        Point p = this.getRootPane().getLocation();
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int y = (int)(screen.getHeight()-400)/2;
        int x = (int)(screen.getWidth()-400)/2;
        this.setLocation(x,y);
        this.setResizable(false);
        Box box = Box.createVerticalBox();
        font = new Font("Consolas", Font.BOLD, 12);
        this.setMinimumSize(new Dimension(400, 400));

        Box vertexBox = Box.createVerticalBox();
        makeBorder("Vertex settings", vertexBox);
        vertexBox.setFont(font);
        vertexBox.add(new SettingSlider<>(Settings::setVertexSize, Settings::vertexSize, "Vertex size",
                0, 50));
        vertexBox.add(new SettingSlider<>(Settings::setVertexWeight, Settings::vertexWeight, "Vertex weight",
                0, 500));
        box.add(vertexBox);

        Box arrowBox = Box.createVerticalBox();
        makeBorder("Edge settings", arrowBox);
        arrowBox.setFont(font);
        arrowBox.add(new SettingSlider<>(Settings::setArrowSize, Settings::intArrowSize, "Arrow size",
                0, 50));
        arrowBox.add(new SettingSlider<>(Settings::setTextDistance, Settings::intTextDistance, "Text distance",
                0, 50));
        arrowBox.add(new SettingSlider<>(Settings::setCurveCoef, Settings::intCurveCoef, "Сurvature",
                0, 36));
        box.add(arrowBox);

        Box gravityBox = Box.createVerticalBox();
        makeBorder("Gravity settings", gravityBox);
        gravityBox.setFont(font);
        gravityBox.add(new SettingSlider<>(Settings::setBorderCoef, Settings::intBorderCoef, "Border repulsion",
                1, 40));

        box.add(gravityBox);

        Box buttonBox = Box.createHorizontalBox();
        buttonBox.add(resetButton);
        buttonBox.add(Box.createHorizontalStrut(250));
        box.add(buttonBox);

        resetButton.addActionListener(this::resetListener);
                this.add(box);
    }

    private void makeBorder(String str, Box box){
        box.setBorder(new TitledBorder(new LineBorder(Color.black), str, TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, font));
    }

    private static Font font;

    private void resetListener(ActionEvent e){
        Settings.reset();
    }

    static JButton resetButton;
}


class SettingSlider<T> extends JPanel{
    public SettingSlider(Setter<Integer> setter, Getter<Integer> getter, String text, int minValue, int maxValue) {
        super();
        text = prName(text);
        this.getter = getter;
        this.setter = setter;
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        JLabel label = new JLabel(text);
        label.setFont(new Font("Consolas", Font.PLAIN, 12));
        slider = new JSlider(minValue, maxValue, (Integer) getter.get());
        this.add(label, BorderLayout.EAST);
        this.add(slider, BorderLayout.WEST);
        slider.addChangeListener(new SettingsChangeListener<T>(this));
        slider.setFont(new Font("Consolas", Font.PLAIN, 12));
        slider.setMinorTickSpacing((maxValue - minValue)/50);
        slider.setMajorTickSpacing((maxValue - minValue)/5);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        SettingsFrame.resetButton.addActionListener(this::reset);
    }
    private String prName(String name){
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        while (sb.length() < 17)
            sb.append(" ");
        return sb.toString();
    }

    public void reset(ActionEvent e){
        slider.setValue(getter.get());
    }

    public JSlider slider;
    public Getter<Integer> getter;
    public Setter<Integer> setter;

}

class SettingsChangeListener<T> implements ChangeListener{
    public SettingsChangeListener(SettingSlider<T> source) {
        this.source = source;
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        source.setter.set(new Integer(source.slider.getValue()));
    }

    SettingSlider<T> source;
}
