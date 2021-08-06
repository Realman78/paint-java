package sample;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.scene.paint.Color;


import javax.imageio.ImageIO;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Stack;

public class Controller implements Initializable {
    @FXML
    Canvas canvas;
    @FXML
    TextField a;
    @FXML
    ColorPicker colorPicker;
    @FXML
    CheckBox eraserCheck;
    @FXML
    ToggleGroup toggleGroup;
    @FXML
    RadioMenuItem roundID;
    @FXML
    RadioMenuItem blockID;
    @FXML
    RadioMenuItem textID;
    @FXML
    RadioMenuItem rectID;
    @FXML
    RadioMenuItem drawID;

    double sx,sy,ex,ey;

    Stack<Image> stack;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colorPicker.setValue(Color.BLACK);
        toggleGroup = new ToggleGroup();
        roundID.setToggleGroup(toggleGroup);
        blockID.setToggleGroup(toggleGroup);
        textID.setToggleGroup(toggleGroup);
        rectID.setToggleGroup(toggleGroup);
        drawID.setToggleGroup(toggleGroup);
        toggleGroup.selectToggle(roundID);
        canvas.setOnMouseDragged(Controller.this::draw);
        canvas.setOnMousePressed(Controller.this::draw);

        toggleGroup.selectedToggleProperty().addListener((observableValue, toggle, t1) -> {
            System.out.println(t1.toString());
            if (t1 != rectID && t1 != drawID){
                canvas.setOnMouseReleased(null);
                canvas.setOnMouseDragged(Controller.this::draw);
                canvas.setOnMousePressed(Controller.this::draw);

            }else{
                canvas.setOnMouseDragged(null);
                canvas.setOnMousePressed(Controller.this::drawShapeStart);
                canvas.setOnMouseDragged(Controller.this::drawTempLine);
                canvas.setOnMouseReleased(Controller.this::drawShapeEnd);

            }
        });

        canvas.setOnKeyPressed(keyEvent -> {
            try {
                if (keyEvent.getCode() == KeyCode.Z)
                    Undo();
            }catch (Exception ignored){
                System.out.println("ej... just ewamted to let u know stack iy semtpoy");
            }
        });
        stack = new Stack<>();
        stack.push(canvas.snapshot(null, null));
    }



    public void draw(MouseEvent e){
        canvas.requestFocus();
        GraphicsContext g = canvas.getGraphicsContext2D();
        double size = Double.parseDouble(a.getText());
        double x = e.getX() - size/2;
        double y = e.getY() - size/2;
        if (!eraserCheck.isSelected()){
            g.setFill(colorPicker.getValue());
            if (roundID.isSelected())
                g.fillOval(x, y, size, size);
             else if(blockID.isSelected())
                g.fillRect(x, y, size, size);
             else if (textID.isSelected())
                 g.fillText(TextFill.Display(), x,y);
        }else
            g.clearRect(x,y,size,size);
        handleStack();
    }

    private void handleStack() {
        try {
            stack.push(canvas.snapshot(null, null));
        }catch (Exception ignored){

        }
        if (stack.size() > 200){
            stack.remove(0);
        }
    }

    public void drawShapeStart(MouseEvent e){
        canvas.requestFocus();
        sx = e.getX();
        sy = e.getY();
        GraphicsContext g = canvas.getGraphicsContext2D();
        g.beginPath();
    }

    public void drawTempLine(MouseEvent e){
        ex = e.getX();
        ey = e.getY();
        GraphicsContext g = canvas.getGraphicsContext2D();

        g.setStroke(colorPicker.getValue());
        g.setLineWidth(Double.parseDouble(a.getText()));
        if (drawID.isSelected()){
            g.lineTo(ex,ey);
            g.stroke();
            handleStack();
        }

    }

    public void drawShapeEnd(MouseEvent e){
        ex = e.getX();
        ey = e.getY();
        GraphicsContext g = canvas.getGraphicsContext2D();
        g.setStroke(colorPicker.getValue());
        if (!drawID.isSelected()){
            g.strokeLine(sx,sy,sx,ey);
            g.strokeLine(sx,sy,ex,sy);
            g.strokeLine(ex,ey,sx,ey);
            g.strokeLine(ex,ey,ex,sy);
            handleStack();
        }

    }

    public void onSave() {
        Image image = canvas.snapshot(null,null);
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", new File("paint.png"));
        }catch (Exception ignore){
            System.out.println("Nis od toga");
        }
    }

    public void clearCanvas(){
        canvas.getGraphicsContext2D().clearRect(0,0,canvas.getWidth(),canvas.getHeight());
    }

    public void Undo() {
        stack.pop();
        canvas.getGraphicsContext2D().drawImage(stack.peek(), 0,0);
    }

    public void onExit() {
        Platform.exit();
    }
}
