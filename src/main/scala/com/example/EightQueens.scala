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


def QueenPiece(): ImageView = {
  val OPTIMAL_SIZE = 35
  val location = String.format("src/resources/images_chess/black_queen.png")
  val image: Image = new Image("file:" + location)
  val imageView: ImageView = new ImageView(image)
  imageView.setFitWidth(OPTIMAL_SIZE)
  imageView.setFitHeight(OPTIMAL_SIZE)
  imageView
}

def QueenController(board: (Array[Array[ImageView]], Boolean), x: String): (Boolean, (Array[Array[ImageView]], Boolean)) = {
  var error = 0
  if (!isValidSquareQueen(x)) {
    return (false, board)
  }
  var row: Int = Char.char2int(x.charAt(0)) - 97
  var col: Int = Char.char2int(x.charAt(1)) - 49


  if (x.length == 2) {
    // check if there is an element in the needed position
    if (board._1(row)(col) != null) {
      println("place taken")
      return (false, board)
    }

    //check if row is empty
    for (i <- 0 to 7) {
      if (board._1(row)(i) != null) {
        println("row taken")
        error = 1;
        return (false, board)
      }
    }
    //check if column is empty
    for (i <- 0 to 7) {
      if (board._1(i)(col) != null) {
        println("col taken")
        error = 1;
        return (false, board)
      }
    }
    //check if diagonals are empty

    for (i <- 0 to 7) {
      if (row + i <= 7 && col + i <= 7 && board._1(row + i)(col + i) != null) {
        println("diagonal down right taken")
        error = 1;
        return (false, board)
      }
      if (row - i >= 0 && col + i <= 7 && board._1(row - i)(col + i) != null) {
        println("diagonal up right taken")
        error = 1;
        return (false, board)
      }
      if (row + i <= 7 && col - i >= 0 && board._1(row + i)(col - i) != null) {
        println("diagonal down left taken")
        error = 1;
        return (false, board)
      }
      if (row - i >= 0 && col - i >= 0 && board._1(row - i)(col - i) != null) {
        println("diagonal up left taken")
        error = 1;
        return (false, board)
      }
    }
    if (error == 0) {
      board._1(row)(col) = QueenPiece()
      return (true, board)
    }
    return (false, board)
  }
  else if (x.length == 3) {
    if (board._1(row)(col) != null) {
      println("delete place")
      board._1(row)(col) = null
      return (true, board)
    } else {
      return (false, board)
    }
  }
  return (false, board)

}

def QueenDrawer(board: (Array[Array[ImageView]], Boolean)): Unit = {
  //draw number row
  val row = new GridPane()
  row.setMinWidth(350)
  row.setMinHeight(30)
  row.setLayoutX(60)
  row.setLayoutY(10)
  for (i <- 0 to 7) {
    var field = new StackPane()
    field.setMinWidth((350 / 8.0))
    field.setMinHeight((350 / 8.0) + 3)
    row.add(field, i, 0)
    var number = new Text(i + 1 + "")
    number.setFont(Font.font(10))
    number.setStyle("-fx-font: 20 arial;")
    number.setTextAlignment(TextAlignment.Center)
    field.getChildren.add(number)
  }
  //draw char col
  val col = new GridPane()
  col.setMinWidth(30)
  col.setMinHeight(360)
  col.setLayoutX(16)
  col.setLayoutY(60)
  for (i <- 0 to 7) {
    var field = new StackPane()
    field.setMinWidth((350 / 8.0) - 3)
    field.setMinHeight((350 / 8.0))
    col.add(field, 0, i)
    var letter = new Text((97 + i).toChar + "")
    letter.setFont(Font.font(10))
    letter.setStyle("-fx-font: 20 arial;")
    letter.setTextAlignment(TextAlignment.Center)
    field.getChildren.add(letter)
  }

  //draw grid
  val gridPane = new GridPane()
  gridPane.setMinWidth(350)
  gridPane.setMinHeight(350)
  gridPane.setLayoutX(60)
  gridPane.setLayoutY(60)
  gridPane.setBackground(new Background(Array(new BackgroundFill(Color.White, null, null))))
  gridPane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.Solid, null, new BorderWidths(3, 3, 3, 3))))
  var changer: Int = 0
  for (i <- 0 to 7) {
    for (j <- 0 to 7) {
      val field = new StackPane()
      field.setMinWidth(350 / 8.0)
      field.setMinHeight(350 / 8.0)
      field.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.Solid, null, null)))
      if (changer % 2 == 0) {
        field.setBackground(new Background(Array(new BackgroundFill(Color.White, null, null))))
      } else {
        field.setBackground(new Background(Array(new BackgroundFill(Color.Gray, null, null))))
      }
      gridPane.add(field, j, i)
      if (board._1(i)(j) != null) {
        field.getChildren.add(board._1(i)(j))
      }
      changer += 1;
    }
    changer += 1;
  }
  val stage = new Stage() {
    scene = new Scene(470, 500) {
      val pane: AnchorPane = new AnchorPane()
      pane.getChildren.add(gridPane)
      pane.getChildren.add(row)
      pane.getChildren.add(col)
      content = pane
    }
  }
  stage.show()
}

def isValidSquareQueen(s: String): Boolean = {
  val pattern = """[a-h][1-9](d)?""".r
  pattern.findFirstIn(s).isDefined
}