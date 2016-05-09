package edu.rit.LightBikesServer;

/**
 * Created by Tim Endersby
 * ISTE 121-01 Homework 9
 */

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    public TextArea player1Text;
    @FXML
    public TextArea errorText;
    @FXML
    public TextArea player2Text;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        player1Text.setText("false");
        //errorText.setEditable(false);
        //player2Text.setEditable(false);
    }
}
