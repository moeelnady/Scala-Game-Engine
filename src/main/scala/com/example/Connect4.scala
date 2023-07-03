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

def getPieceConnect4(color: Boolean): ImageView = {
  val location = String.format("src/resources/images_connect4/%s.png", if (color) "red" else "yellow")
  val image: Image = new Image("file:" + location)

  val imageView: ImageView = new ImageView(image)
  imageView.setFitWidth(75)
  imageView.setFitHeight(75)

  imageView
}

def connect4Controller(board: (Array[Array[ImageView]], Boolean), x: String): (Boolean, (Array[Array[ImageView]], Boolean)) = {
  var found = false
  if (x.length != 1) return (false, board)
  var newBoard = board._1
  var newTurn = board._2

  val a: Int = Char.char2int(x.charAt(0)) - 48
  println("a : " + a)
  if (!(a > 0 && a <= 7)) {
    return (false, (newBoard, newTurn))
  }
  else {
    var i = 0
    while (!found && i != 6) {
      if (newBoard(5 - i)(a - 1) == null) {
        newBoard(5 - i)(a - 1) = getPieceConnect4(newTurn)
        newTurn = !newTurn
        found = true
      }
      else
        i += 1
    }
    println("found: " + found)
    if (found == true) {
      (true, (newBoard, newTurn))
    }
    else (false, (newBoard, newTurn))
  }

}

def connect4Drawer(board: (Array[Array[ImageView]], Boolean)): Unit = {
  val gridPane: GridPane = new GridPane()
  // Properties for the GridPane
  gridPane.padding = Insets(25, 25, 25, 25)
  gridPane.hgap = 0.5
  gridPane.vgap = 0.5

  // Numbers and Letters in the Board
  for (i <- 0 until 7) {
    gridPane.add(newColLabelConnect4(i), i, 7, 1, 1)
  }

  // For the Background Colors
  for (i <- 0 to 5) {
    for (j <- 0 to 6) {
      val field: StackPane = new StackPane()
      field.setMinWidth(75)
      field.setMinHeight(75)
      field.setBackground(new Background(Array(new BackgroundFill(Color.Gray, null, null))))
      field.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.Solid, null, null)))
      var circle = new Circle()
      circle.setRadius(75 / 2)
      circle.setFill(javafx.scene.paint.Color.WHITE)
      field.getChildren.add(circle)
      gridPane.add(field, j, i)
      if (board._1(i)(j) != null) {
        gridPane.add(board._1(i)(j), j, i)
      }

    }
  }

  //      val controller = new Connect4Controller;
  //      var turn: Boolean = true
  val stage = new Stage() {
    title = "Connect4"
    scene = new Scene(580, 640) {
      val pane: AnchorPane = new AnchorPane()
      pane.getChildren.add(gridPane)
      content = pane
    }
  }
  stage.show()
}

def newColLabelConnect4(i: Int): Label = {
  val l: Label = new Label(i + 1 + "")
  l.setMinSize(75, 75)
  l.setAlignment(Pos.Center)
  l
}
