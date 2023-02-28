package org.example.dbs.gui;

import org.example.dbs.Jdbc;
import org.example.dbs.models.UserData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

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
                    Jdbc jdbc = new Jdbc();
                    jdbc.getConnection();
                    String sql = "select * from `admin` where username = ?";
                    List<Object> params = new ArrayList<>();
                    params.add(username.getText());
                    try {
                        UserData findUser = jdbc.findSimpleRefResult(sql, params, UserData.class);
                        if(findUser != null) {
                            JOptionPane.showMessageDialog(frame,
                                    "Username already exist, please try different name",
                                    "Error Message",
                                    JOptionPane.ERROR_MESSAGE);
                        } else {
                            // Coding for entering data to database
                        }
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
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
