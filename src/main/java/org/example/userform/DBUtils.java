package org.example.userform;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class DBUtils {
    public static void changeScene(ActionEvent event, String fxmlFile, String title, String username) throws IOException {
        Parent root = null;

        if (username != null) {
            try {
//                System.out.println(fxmlFile);
//                System.out.println(new FXMLLoader(DBUtils.class.getClassLoader().getResource(fxmlFile)));
                FXMLLoader loader = new FXMLLoader(DBUtils.class.getClassLoader().getResource(fxmlFile));
                root = loader.load();
                LoggedInController loggedInController = loader.getController();
                loggedInController.setUserData(username);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
//                System.out.println("2:" + fxmlFile);
//                System.out.println(new FXMLLoader(DBUtils.class.getClassLoader().getResource(fxmlFile)));
                FXMLLoader loader = new FXMLLoader(DBUtils.class.getClassLoader().getResource(fxmlFile));
                root = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(root != null){
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle(title);
            stage.setScene(new Scene(root, 600, 400));
            stage.show();
        }
    }

    public static void register(ActionEvent event, String username, String email, String password) {
        Connection connection = null;
        PreparedStatement psInsert = null;
        PreparedStatement psCheckUserExist = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/userdbs", "root", "sirlolipop13");
            psCheckUserExist = connection.prepareStatement("SELECT * FROM users WHERE username = ?");
            psCheckUserExist.setString(1, username);
            resultSet = psCheckUserExist.executeQuery();

            if (resultSet.isBeforeFirst()) {
                System.out.println("User Already Exist");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("User Already Exist");
                alert.setContentText("This username has been taken\nPlease use another name");
                alert.show();
            } else {
                psInsert = connection.prepareStatement("INSERT INTO users (username, password, email) VALUES (?, ?, ?)");
                psInsert.setString(1, username);
                psInsert.setString(2, password);
                psInsert.setString(3, email);
                psInsert.executeUpdate();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Registration Succeed");
                alert.setContentText("Your registration has been successful");
                alert.show();

                changeScene(event, "LoginUser.fxml", "Welcome " + username, username);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (psCheckUserExist != null) {
                try {
                    psCheckUserExist.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (psInsert != null) {
                try {
                    psInsert.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void login(ActionEvent event, String username, String password) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/userdbs", "root", "sirlolipop13");
            ps = connection.prepareStatement("SELECT password FROM users WHERE username = ?");
            ps.setString(1, username);
            resultSet = ps.executeQuery();

            if (!resultSet.isBeforeFirst()) {
                System.out.println("User not found in database");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("User Not Found");
                alert.setContentText("User was not found in the database\nUsername may be incorrect or may not exist");
                alert.show();

            } else {
                while (resultSet.next()) {
                    String dbPassword = resultSet.getString("password");
                    if (dbPassword.equals(password)) {
                        changeScene(event, "FinishLogin.fxml", "Welcome " + username, username);
                    } else {
                        System.out.println("Password did not match");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Password Incorrect");
                        alert.setContentText("Password is incorrect\nPlease try again");
                        alert.show();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
