package com.example

import javafx.application.Platform
import jdk.internal.util.xml.impl.Input
import scalafx.scene.control.Button
import scalafx.Includes.*
import scalafx.application.JFXApp3
import scalafx.event.ActionEvent
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.{Scene, control}
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control.{Alert, Button, Label, TextField}
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.layout.{AnchorPane, Background, BackgroundFill, Border, BorderStroke, BorderStrokeStyle, BorderWidths, GridPane, StackPane}
import scalafx.scene.paint.Color
import scalafx.scene.text.{Font, Text, TextAlignment}
import scalafx.stage.Stage
import scalafx.scene.shape.{Circle, Line}
import scalafx.stage.WindowEvent

import scala.math._

object HelloWorld extends JFXApp3 {
  override def start(): Unit = {
    def start[U](name: String, draw: (U) => Unit, control: (U, String) => (Boolean, U), board: U): Unit = {
      var state: U = board
      draw(state);
      while (true) {

        var input = inputfun(name)
        var tmp = control(state, input)
        state = tmp._2
        if (!tmp._1) {
          new Alert(AlertType.Information) {
            title = "Ooops, Warning!"
            headerText = "Input Provided Error"
            contentText = "Please, provide a valid input"
          }.showAndWait()
        }
        else {
          draw(state)
        }
      }

    }
    stage = new JFXApp3.PrimaryStage {
      title = "Game Engine :)"



      scene = new Scene(500, 500) {

        val label = new Label("Game Menu")
        label.setFont(new Font(40))
        label.layoutX = 150
        label.layoutY = 50


        val button_1: Button = new Button("Sudoku")
        button_1.setLayoutX(200)
        button_1.setLayoutY(130)
        button_1.setFont(new Font(20))
        button_1.setMinWidth(110)
        button_1.setBorder(new Border())

        val button_2: Button = new Button("Connect4")
        button_2.setLayoutX(200)
        button_2.setLayoutY(180)
        button_2.setFont(new Font(20))
        button_2.setMinWidth(110)


        val button_3: Button = new Button("XO")
        button_3.setLayoutX(200)
        button_3.setLayoutY(230)
        button_3.setFont(new Font(20))
        button_3.setMinWidth(110)

        val button_4: Button = new Button("Checkers")
        button_4.setLayoutX(200)
        button_4.setLayoutY(280)
        button_4.setFont(new Font(20))
        button_4.setMinWidth(110)

        val button_5: Button = new Button("8 Queens")
        button_5.setLayoutX(200)
        button_5.setLayoutY(330)
        button_5.setFont(new Font(20))
        button_5.setMinWidth(110)

        val button_6: Button = new Button("Chess")
        button_6.setLayoutX(200)
        button_6.setLayoutY(380)
        button_6.setFont(new Font(20))
        button_6.setMinWidth(110)

        button_1.onAction = (_: ActionEvent) => {
          start("Sudoku",SudokuDrawer,SudokuController,getSudokuState())
        }

        button_2.onAction = (_: ActionEvent) => {
          start("Connect4", connect4Drawer, connect4Controller, (Array.ofDim[ImageView](6, 7), true))

        }
        button_3.onAction = (_: ActionEvent) => {
          start("XO",XODrawer,XOController,(Array.ofDim[ImageView](3,3),true))
        }
        button_4.onAction = (_: ActionEvent) => {
          start("Checkers" ,CheckersDrawer, CheckersController,initialize((Array.ofDim[(ImageView,Boolean)](8, 8)) , true))
        }
        button_5.onAction = (_: ActionEvent) => {
          start("8Queen", QueenDrawer, QueenController,(Array.ofDim[ImageView](8, 8),true))
        }
        button_6.onAction = (_: ActionEvent) => {
          start("Chess", ChessDrawer, ChessController, Chessboard())
        }



        content = List(label, button_1, button_2, button_3, button_4, button_5, button_6)
      }
    }




































  }
}

