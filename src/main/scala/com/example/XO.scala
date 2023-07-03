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


def XOController(board: (Array[Array[ImageView]], Boolean), input: String): (Boolean, (Array[Array[ImageView]], Boolean)) = {
  if (input.length != 2)
    return (false, board)
  var newBoard = board._1
  var newTurn = board._2
  val i: Int = Char.char2int(input.charAt(0)) - 49
  val j: Int = Char.char2int(input.charAt(1)) - 49
  if (!(i >= 0 && i < 3 && j >= 0 && j < 3 && newBoard(i)(j) == null))
    return (false, (newBoard, newTurn))
  newBoard(i)(j) = getXOPiece(newTurn)
  newTurn = !newTurn
  (true, (newBoard, newTurn))
}
def getXOPiece(x: Boolean): ImageView = {
  val path = String.format("src/resources/images_TicTacToe/%s.png", if (x) "x" else "o")
  val image = new Image("file:" + path);
  val piece = new ImageView(image)
  piece.setFitWidth(105)
  piece.setFitHeight(105)

  piece
}
def XODrawer(board: (Array[Array[ImageView]], Boolean)): Unit = {
  val gridPane = new GridPane()
  gridPane.padding = Insets(100, 100, 100, 100)
  gridPane.setVgap(2.5)
  gridPane.setHgap(2.5)

  for (i <- 0 to 2) {
    for (j <- 0 to 2) {
      val position: StackPane = new StackPane()
      position.setMinWidth(115)
      position.setMinHeight(115)
      position.setBackground(new Background(Array(new BackgroundFill(Color.LightGrey, null, null))))
      gridPane.add(position, i, j)
    }
  }

  for (i <- 0 to 2) {
    for (j <- 0 to 2) {
      if (board._1(i)(j) != null)
        gridPane.add(board._1(i)(j), j, i)

    }
  }

  val stage = new Stage() {
    title = "Tic Tac Toe"
    scene = new Scene(600, 600) {
      val pane: AnchorPane = new AnchorPane()
      pane.getChildren.add(gridPane)
      content = pane
    }

  }
  stage.show()
  gridPane
}
