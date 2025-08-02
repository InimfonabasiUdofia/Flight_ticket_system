package com.airline.gui;
import javax.swing.*;

import com.airline.gui.helper_classes.CustomFontLoader;
import com.airline.gui.helper_classes.OnClickEventHelper;
import com.airline.gui.helper_classes.OnFocusEventHelper;
import com.airline.gui.helper_classes.RoundedBorder;

import java.awt.Color;


public class WindowBuilder {
  public static void main(String[] args) {

     JFrame frame = new JFrame("My Awesome Window");
     frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
     frame.setSize(743, 400);
     JPanel panel = new JPanel();
     panel.setLayout(null);
     panel.setBackground(Color.decode("#eeeeee"));

     JButton element1 = new JButton("submit");
     element1.setBounds(170, 270, 106, 29);
     element1.setBackground(Color.decode("#ffffff"));
     element1.setForeground(Color.decode("#1b1b1b"));
     element1.setFont(CustomFontLoader.loadFont("./resources/fonts/Lexend.ttf", 14));
     element1.setBorder(new RoundedBorder(4, Color.decode("#626262"), 1));
     element1.setFocusPainted(false);
     OnClickEventHelper.setOnClickColor(element1, Color.decode("#c2c2c2"), Color.decode("#ffffff"));
     panel.add(element1);

     JButton element2 = new JButton("Click Me");
     element2.setBounds(210, 426, 106, 29);
     element2.setBackground(Color.decode("#ffffff"));
     element2.setForeground(Color.decode("#1b1b1b"));
     element2.setFont(CustomFontLoader.loadFont("./resources/fonts/Lexend.ttf", 14));
     element2.setBorder(new RoundedBorder(4, Color.decode("#626262"), 1));
     element2.setFocusPainted(false);
     OnClickEventHelper.setOnClickColor(element2, Color.decode("#c2c2c2"), Color.decode("#ffffff"));
     panel.add(element2);

     JTextArea element3 = new JTextArea("");
     element3.setBounds(169, 154, 240, 95);
     element3.setFont(CustomFontLoader.loadFont("./resources/fonts/Lexend.ttf", 14));
     element3.setBackground(Color.decode("#ffffff"));
     element3.setForeground(Color.decode("#737674"));
     element3.setBorder(new RoundedBorder(2, Color.decode("#626262"), 1));
     OnFocusEventHelper.setOnFocusText(element3, "input your message", Color.decode("#1b1b1b"),   Color.decode("#737674"));
     panel.add(element3);

     JTextField element4 = new JTextField("");
     element4.setBounds(166, 111, 238, 21);
     element4.setFont(CustomFontLoader.loadFont("./resources/fonts/Lexend.ttf", 14));
     element4.setBackground(Color.decode("#ffffff"));
     element4.setForeground(Color.decode("#737674"));
     element4.setBorder(new RoundedBorder(2, Color.decode("#626262"), 1));
     OnFocusEventHelper.setOnFocusText(element4, "Your Input!", Color.decode("#1b1b1b"),   Color.decode("#737674"));
     panel.add(element4);

     JTextField element5 = new JTextField("");
     element5.setBounds(174, 75, 235, 21);
     element5.setFont(CustomFontLoader.loadFont("./resources/fonts/Lexend.ttf", 14));
     element5.setBackground(Color.decode("#ffffff"));
     element5.setForeground(Color.decode("#737674"));
     element5.setBorder(new RoundedBorder(2, Color.decode("#626262"), 1));
     OnFocusEventHelper.setOnFocusText(element5, "Your Input!", Color.decode("#1b1b1b"),   Color.decode("#737674"));
     panel.add(element5);

     JLabel element6 = new JLabel("Username :");
     element6.setBounds(75, 78, 106, 17);
     element6.setFont(CustomFontLoader.loadFont("./resources/fonts/Lexend.ttf", 14));
     element6.setForeground(Color.decode("#1b1b1b"));
     panel.add(element6);

     JLabel element7 = new JLabel("Email :");
     element7.setBounds(106, 115, 106, 17);
     element7.setFont(CustomFontLoader.loadFont("./resources/fonts/Lexend.ttf", 14));
     element7.setForeground(Color.decode("#1b1b1b"));
     panel.add(element7);

     JLabel element8 = new JLabel("Message :");
     element8.setBounds(88, 149, 106, 17);
     element8.setFont(CustomFontLoader.loadFont("./resources/fonts/Lexend.ttf", 14));
     element8.setForeground(Color.decode("#1b1b1b"));
     panel.add(element8);

     JLabel element9 = new JLabel("Error");
     element9.setBounds(174, 53, 106, 14);
     element9.setFont(CustomFontLoader.loadFont("./resources/fonts/Lexend.ttf", 12));
     element9.setForeground(Color.decode("#1b1b1b"));
     panel.add(element9);

     JLabel element10 = new JLabel("Contact Us");
     element10.setBounds(148, 22, 106, 21);
     element10.setFont(CustomFontLoader.loadFont("./resources/fonts/Lexend.ttf", 21));
     element10.setForeground(Color.decode("#1b1b1b"));
     panel.add(element10);

     frame.add(panel);
     frame.setVisible(true);

  }
}