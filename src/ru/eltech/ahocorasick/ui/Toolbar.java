package ru.eltech.ahocorasick.ui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class Toolbar extends JToolBar {
    /**
     * Creates a new tool bar; orientation defaults to <code>HORIZONTAL</code>.
     */
    public Toolbar(GraphicAlgorithmProcessor processor) {
        super();
        this.processor = processor;
        this.setRollover(true);
        this.setFloatable(false);
        makeOpenClose();
        makeStringButton();
        makeStepsControl();
        this.addSeparator(new Dimension(10000, 10));
    }

    private void makeOpenClose(){
        this.add(makeButton(processor::openAlgorithmAction, "Open saved algorithm", "openfile.png"));
        this.add(makeButton(processor::openFileAction, "Open file with text", "opentext.png"));
        this.add(makeButton(processor::openStringsAction, "Open file with strings", "openstrings.png"));
        this.addSeparator();
    }

    private JButton makeButton(ActionListener e, String tooltip, String iconName){
        JButton button = new JButton();
        button.addActionListener(e);
        button.setToolTipText(tooltip);
        setIcon(iconName, button);
        button.setSelected(false);
        button.setFocusPainted(false);
        return button;
    }

    private void setIcon(String name, JButton button){
        try {
            Image iconFile = ImageIO.read(Launcher.class.getResourceAsStream(name));
            ImageIcon icon22 = new ImageIcon(iconFile);
            button.setIcon(icon22);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void makeStepsControl() {
        this.add(makeButton(processor::undoAction, "Undo", "undo.png"));
        this.add(makeButton(processor::redoAction, "Redo", "redo.png"));
        this.addSeparator();
        this.add(makeButton(processor::stepAction, "One algorithm step", "step.png"));
        this.add(makeButton(processor::finishAction, "Finish algorithm", "finish.png"));
        this.addSeparator();
        this.add(makeButton(processor::restartAction, "Restart algorithm", "restart.png"));
        this.add(makeButton(processor::clearAction, "Clear", "reset.png"));
    }

    private void makeStringButton(){
        this.add(makeButton(processor::addStringAction, "Add string", "addstring.png"));
    }

    private GraphicAlgorithmProcessor processor;
}
