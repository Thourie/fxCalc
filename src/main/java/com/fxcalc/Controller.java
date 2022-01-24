package com.fxcalc;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private Button closebutton;
    @FXML
    private AnchorPane mainAP;
    @FXML
    private AnchorPane rightAP;
    @FXML
    private Label prevnum;
    @FXML
    private Label result;
    @FXML
    private ListView<String> history;

    private boolean hide = true;
    private double num1 = 0.0;
    private double num2 = 0.0;
    private double total = 0.0;
    private boolean check = true;
    private String operator = "";

    private DBConnecton dbConnecton;
    private Connection dbconn;
    private Statement stat;

    private double calc(double num1, double num2, String operator){
        switch(operator){
           case "+": return num1 + num2;
           case "-": return num1 - num2;
           case "*": return num1 * num2;
           case "/": return (num2 == 0) ? 0.0: num1 / num2;
           case "POW": return Math.pow(num1, 2);
           case "ROOT": return Math.pow(num1, (num2 == 0) ? 1: 1 / num2);
           default: break;
        }
            return 0.0;
    }

    private void number(String number){
        result.setText(result.getText() + number);
    }

    private void prevNumber(String number){
        prevnum.setText(prevnum.getText() + number);
    }

    private void prevOperator(String operator){
        prevnum.setText(prevnum.getText() + " " + operator + " ");
    }

    public void handleCloseButtonAction(ActionEvent event) {
        Stage stage = (Stage) closebutton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void clear(){
        prevnum.setText("");
        result.setText("0");
        operator = "";
        num1 = 0.0;
        num2 = 0.0;
        check = true;
    }
    @FXML
    void computeProcess(ActionEvent event)    {
        if(check){
            result.setText("");
            prevnum.setText("");
            check = false;
        }

        Button button = (Button)event.getSource();
        String value = button.getText();

        number(value);
        prevNumber(value);
    }
    @FXML
    void operatorProcess(ActionEvent event) throws SQLException {


        Button button = (Button)event.getSource();
        String value = button.getText();

        if(!value.equals("=")){
            if(!operator.isEmpty()) return;
            operator = value;
            prevOperator(operator);
            num1 = Double.parseDouble(result.getText());
            result.setText("");
        } else{
            if(operator.isEmpty()) return;
            num2 = Double.parseDouble(result.getText());
            total = calc(num1, num2, operator);
            result.setText(String.valueOf(total));
            history.getItems().add(prevnum.getText() + "=" + result.getText());

            try {
            String valueStr = prevnum.getText() + " = " + result.getText();
            stat.executeUpdate("INSERT INTO CALC(Calculation) VALUES ('" +valueStr+ "')");

          } catch(SQLException e)
         {
               e.printStackTrace();
         }
            operator = "";
            check = true;
        }
    }
    @FXML
    void hideHistory(ActionEvent event) {
        if(hide) {
            mainAP.getChildren().remove(rightAP);
            mainAP.setPrefWidth(mainAP.getPrefWidth() - 222);
            hide = false;
        }else{
            mainAP.getChildren().add(rightAP);
            mainAP.setPrefWidth(mainAP.getPrefWidth() + 222);
            hide = true;
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dbConnecton = new DBConnecton();
        dbconn = dbConnecton.getConnection();
        try{
            stat = dbconn.createStatement();
            ResultSet query = stat.executeQuery("SELECT Calculation FROM Calc LIMIT 10");
            String calculation = "";
            while(query.next())
            {
                calculation = query.getString("calculation");
                history.getItems().add(calculation);

            }
        }catch(SQLException e)
        {
            e.printStackTrace();
        }
    }
}