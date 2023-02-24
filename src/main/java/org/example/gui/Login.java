package org.example.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Login extends Frame {
    TextField username, password;

    Login() {
        addWindowListener (new WindowAdapter() {
            public void windowClosing (WindowEvent e) {
                dispose();
            }
        });

                username = new TextField();
        username.setBounds(35, 70, 180, 30);
        Label usernameLabel = new Label("Username");
        usernameLabel.setBounds(35, 30, 100, 30 );
        password = new TextField();
        password.setBounds(35, 140, 180, 30);
        Label passwordLabel = new Label("Password");
        passwordLabel.setBounds(35,110, 100, 30 );
        Button click = new Button("Login");
        Frame frame = this;

        click.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!username.getText().equals("") && !password.getText().equals("")){

                } else {
                    JOptionPane.showMessageDialog(frame,
                            "Username or Password is empty",
                            "Empty Parameter",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        click.setBounds(35, 180, 60, 30 );
        add(username);
        add(password);
        add(usernameLabel);
        add(passwordLabel);
        add(click);
        setSize(300, 280);
        setTitle("Login Form");
        setLayout(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        new Login();
    }
}
