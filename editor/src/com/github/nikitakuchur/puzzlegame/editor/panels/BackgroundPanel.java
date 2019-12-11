package com.github.nikitakuchur.puzzlegame.editor.panels;

import com.badlogic.gdx.graphics.Color;
import com.github.nikitakuchur.puzzlegame.game.Background;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class BackgroundPanel extends JPanel {

    private final Background background;

    public BackgroundPanel(Background background) {
        this.background = background;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JTextField startColor = new JTextField();
        startColor.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                changeStartColor(startColor.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                changeStartColor(startColor.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                changeStartColor(startColor.getText());
            }
        });

        JTextField stopColor = new JTextField();
        stopColor.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                changeStopColor(stopColor.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                changeStopColor(stopColor.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                changeStopColor(stopColor.getText());
            }
        });

        add(startColor);
        add(stopColor);
    }

    private void changeStartColor(String startColor) {
        try {
            background.setStartColor(Color.valueOf(startColor));
        } catch (Exception e) {
            // Do nothing
        }
    }

    private void changeStopColor(String stopColor) {
        try {
            background.setStopColor(Color.valueOf(stopColor));
        } catch (Exception e) {
            // Do nothing
        }
    }
}
