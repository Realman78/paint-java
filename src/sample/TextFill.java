package sample;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class TextFill {
    public static String Display(){
        Stage stage = new Stage();
        stage.setTitle("Enter Text");
        TextField tf = new TextField();
        tf.setAlignment(Pos.CENTER);
        VBox vBox = new VBox();
        vBox.getChildren().add(tf);
        stage.setScene(new Scene(vBox));
        tf.setOnAction(e ->
                stage.close());
        stage.showAndWait();
        return tf.getText();
    }
}
