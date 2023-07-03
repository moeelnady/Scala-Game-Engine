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



def CheckersDrawer(board: (Array[Array[(ImageView, Boolean)]], Boolean)): Unit = {
  val gridPane = new GridPane()
  gridPane.padding = Insets(25, 25, 30, 25)
  gridPane.hgap = 0.5
  gridPane.vgap = 0.5

  for (i <- 0 until 8) {
    gridPane.add(newRowLabelCheckers(i), 0, i + 1, 1, 1)
    gridPane.add(newRowLabelCheckers(i), 9, i + 1, 1, 1)
    gridPane.add(newColLabelCheckers(i), i + 1, 0, 1, 1)
    gridPane.add(newColLabelCheckers(i), i + 1, 9, 1, 1)
  }
  for (i <- 1 to 8) {
    for (j <- 1 to 8) {
      val field: StackPane = new StackPane()
      field.setBackground(if (((i + j) & 1) == 0) new Background(Array(new BackgroundFill(Color.White, null, null)))
      else new Background(Array(new BackgroundFill(Color.Gray, null, null))))
      gridPane.add(field, i, j)
    }
  }
  for (i <- 0 until 8) {
    for (j <- 0 until 8) {
      if (board._1(i)(j) != null)
        gridPane.add(board._1(i)(j)._1, j + 1, i + 1)
    }
  }
  val stage = new Stage() {
    title = "Checkers"
    scene = new Scene(580, 640) {
      val pane: AnchorPane = new AnchorPane()
      pane.getChildren.add(gridPane)
      content = pane
    }
  }
  stage.show()
  gridPane
}
def CheckersController(board: (Array[Array[(ImageView, Boolean)]], Boolean), input: String): (Boolean, (Array[Array[(ImageView, Boolean)]], Boolean)) = {
  if (input.length != 5) {
    return (false, board)
  }
  //println(input.length)
  var newBoard = board._1
  var red = board._2
  val a = input.charAt(0)
  val b = input.charAt(1)
  val c = input.charAt(3)
  val d = input.charAt(4)
  val x_src: Int = a match {
    case '1' => 7
    case '2' => 6
    case '3' => 5
    case '4' => 4
    case '5' => 3
    case '6' => 2
    case '7' => 1
    case '8' => 0
  }
  val y_src: Int = b match {
    case 'a' => 0
    case 'b' => 1
    case 'c' => 2
    case 'd' => 3
    case 'e' => 4
    case 'f' => 5
    case 'g' => 6
    case 'h' => 7
  }
  val x_dest: Int = c match {
    case '1' => 7
    case '2' => 6
    case '3' => 5
    case '4' => 4
    case '5' => 3
    case '6' => 2
    case '7' => 1
    case '8' => 0
  }
  val y_dest: Int = d match {
    case 'a' => 0
    case 'b' => 1
    case 'c' => 2
    case 'd' => 3
    case 'e' => 4
    case 'f' => 5
    case 'g' => 6
    case 'h' => 7
  }
  println("src x = " + x_src)
  println("src y = " + y_src)
  println("dest x = " + x_dest)
  println("dest y = " + y_dest)
  if (x_src < 0 || y_src > 7 || newBoard(x_dest)(y_dest) != null || newBoard(x_src)(y_src) == null) {
    return (false, board)
  }
  if (newBoard(x_src)(y_src)._2 != red) {
    return (false, board)
  }
  val rowDiff = x_dest - x_src
  val colDiff = y_dest - y_src
  if (Math.abs(colDiff) == 1 && rowDiff == 1 && !red) {
    newBoard(x_src)(y_src) = null
    newBoard(x_dest)(y_dest) = getCheckerPiece(red)
    red = (!red)
    return (true, (newBoard, red))
  }
  if (Math.abs(colDiff) == 1 && rowDiff == -1 && red) {
    newBoard(x_src)(y_src) = null
    newBoard(x_dest)(y_dest) = getCheckerPiece(red)
    red = (!red)
    return (true, (newBoard, red))
  }
  if (Math.abs(colDiff) == 2 && rowDiff == 2 && !red) {
    if (newBoard(x_src + 1)(y_src + Integer.signum(colDiff))._2) {
      newBoard(x_src)(y_src) = null
      newBoard(x_src + 1)(y_src + Integer.signum(colDiff)) = null;
      newBoard(x_dest)(y_dest) = getCheckerPiece(red)
      red = (!red)
      return (true, (newBoard, red))
    }
  }
  if (Math.abs(colDiff) == 2 && rowDiff == -2 && red) {
    if (!newBoard(x_src - 1)(y_src + Integer.signum(colDiff))._2) {
      newBoard(x_src)(y_src) = null

      newBoard(x_src - 1)(y_src + Integer.signum(colDiff)) = null
      newBoard(x_dest)(y_dest) = getCheckerPiece(red)
      red = (!red)
      return (true, (newBoard, red))
    }

  }


  (false, (newBoard, red))
}
def getCheckerPiece(red: Boolean): (ImageView, Boolean) = {
  val path = String.format("src/resources/images_checkers/%s.png", if (red) "red" else "black")
  val image = new Image("file:" + path);
  val piece = new ImageView(image)
  piece.setFitWidth(60)
  piece.setFitHeight(60)

  (piece, red)
}
def newRowLabelCheckers(i: Int): Label = {
  val l: Label = new Label(8 - i + "")
  l.setMinSize(20, 64)
  l.setAlignment(Pos.Center)
  l
}
def newColLabelCheckers(i: Int): Label = {
  val l = new Label((i + 65).toChar + "")
  l.setMinSize(64, 20)
  l.setAlignment(Pos.Center)
  l
}
def initialize(board: (Array[Array[(ImageView, Boolean)]], Boolean)): (Array[Array[(ImageView, Boolean)]], Boolean) = {
  for (i <- 0 to 2) {
    for (j <- 0 to 7) {
      if ((j % 2 == 0 && i % 2 == 1) || (i % 2 == 0 && j % 2 == 1)) {
        board._1(i)(j) = getCheckerPiece(false)
      }
    }
  }
  for (i <- 5 to 7) {
    for (j <- 0 to 7) {
      if ((j % 2 == 0 && i % 2 == 1) || (i % 2 == 0 && j % 2 == 1)) {
        board._1(i)(j) = getCheckerPiece(true)
      }
    }
  }
  (board)
}