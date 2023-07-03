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


def getSudokuState(): (Array[Array[String]], Array[Array[String]]) = {
  var sudoku: Array[Array[String]] = Array(
    Array("1", "2", "3", "4", "5", "6", "7", "8", "9"),
    Array("4", "5", "6", "7", "8", "9", "1", "2", "3"),
    Array("7", "8", "9", "1", "2", "3", "4", "5", "6"),
    Array("2", "1", "4", "3", "6", "5", "8", "9", "7"),
    Array("3", "6", "5", "8", "9", "7", "2", "1", "4"),
    Array("8", "9", "7", "2", "1", "4", "3", "6", "5"),
    Array("5", "3", "1", "6", "4", "2", "9", "7", "8"),
    Array("6", "4", "2", "9", "7", "8", "5", "3", "1"),
    Array("9", "7", "8", "5", "3", "1", "6", "4", "2")
  )
  var initials: Array[Array[String]] = Array.ofDim[String](9, 9)
  sudoku = sudocuGenerator(sudoku)
  initialMatrix(sudoku, initials)
  (sudoku, initials)
}

def SudokuController(board: (Array[Array[String]], Array[Array[String]]), name: String): (Boolean, (Array[Array[String]], Array[Array[String]])) = {
  var found = false
  if (name.length == 3 && name.charAt(2) == 'd') {
    println("we will delete")
    name.charAt(0) match {
      case 'a' | 'b' | 'c' | 'd' | 'e' | 'f' | 'g' | 'h' | 'i' => {
        name.charAt(1) match {
          case '1' | '2' | '3' | '4' | '5' | '6' | '7' | '8' | '9' =>
          case _ => println("invalid")
        }
      }
      case _ => println("invalid")
    }
    println("the input is right")
    var row: Int = Char.char2int(name.charAt(0)) - 97
    var col: Int = Char.char2int(name.charAt(1)) - 49
    if (board._1(row)(col) == null || board._2(row)(col) != null) {
      println(board._1(row)(col) + " || " + board._2(row)(col))
      println("something wrong")
      return (false, board)
    }
    else {
      println("all is right")
      board._1(row)(col) = " "
      println("board: " + board._1(row)(col))
      return (true, board)
    }
  }
  if (name.length != 4) return (false, board)
  var trial_1 = 0
  var trial_2 = 0
  var trial_3 = 0
  name.charAt(0) match {
    case 'a' | 'b' | 'c' | 'd' | 'e' | 'f' | 'g' | 'h' | 'i' => {
      name.charAt(1) match {
        case '1' | '2' | '3' | '4' | '5' | '6' | '7' | '8' | '9' =>
        case _ => return (false, board)
      }
    }
    case _ => return (false, board)
  }

  var row: Int = Char.char2int(name.charAt(0)) - 97
  var col: Int = Char.char2int(name.charAt(1)) - 49
  var value: Int = Char.char2int(name.charAt(3)) - 48
  printf("row:%d !! col:%d", row, col)

  // check if there is an element in the needed position
  if (board._1(row)(col) != null) {
    println("can't play in this place")
    found = true
  }
  //check if the needed element in found in its row
  for (i <- 0 to 8) {
    if (board._1(row)(i) == (value + "")) {
      trial_1 = 1;
    }
  }
  //check if the needed element in found in its column
  for (i <- 0 to 8) {
    if (board._1(i)(col) == (value + "")) {
      trial_2 = 1;
    }
  }
  //check if the needed element in found in its square
  var first: Int = 0
  var second: Int = 0
  if (row > 2) {
    if (row > 5) {
      first = 6
    }
    else first = 3
  }
  if (col > 2) {
    if (col > 5) {
      second = 6
    }
    else second = 3
  }
  for (i <- first to first + 2) {
    for (j <- second to second + 2) {
      if (board._1(i)(j) == (value + "")) {
        trial_3 = 1;
      }
    }
  }
  if (trial_1 + trial_2 + trial_3 == 0) {
    board._1(row)(col) = (value + "")
    (true, board)
  }
  else
    (false, board)
}

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

def inputfun(name: String): String = {
  var value: String = ""
  val stage = new Stage() {
    title = name
    scene = new Scene(300, 100) {
      val pane: AnchorPane = new AnchorPane()
      val textField = new TextField()
      textField.setFont(new Font(20))
      textField.prefWidth = 90
      textField.prefHeight = 30
      textField.layoutX = 15
      textField.layoutY = 30


      val button: control.Button = new control.Button {
        text = "Play"
        onAction = (event: ActionEvent) => {
          value = textField.getText
          close()
        }
      }

      button.setFont(new Font(20))
      button.setMinWidth(60)
      button.setMinHeight(20)
      button.setLayoutX(120)
      button.setLayoutY(30)


      pane.getChildren.add(button)
      pane.getChildren.add(textField)


      content = List(textField, button)
    }
  }
  stage.showAndWait()
  value
}

def SudokuDrawer(board: (Array[Array[String]], Array[Array[String]])): Unit = {


  val row = new GridPane()
  row.setMinWidth(350)
  row.setMinHeight(30)
  row.setLayoutX(60)
  row.setLayoutY(10)

  for (i <- 0 to 8) {
    var field = new StackPane()
    field.setMinWidth((350 / 9.0))
    field.setMinHeight((350 / 9.0) + 3)
    row.add(field, i, 0)
    var number = new Text(i + 1 + "")
    number.setFont(Font.font(10))
    number.setStyle("-fx-font: 20 arial;")
    number.setTextAlignment(TextAlignment.Center)
    field.getChildren.add(number)
  }


  val col = new GridPane()
  col.setMinWidth(30)
  col.setMinHeight(360)
  col.setLayoutX(16)
  col.setLayoutY(60)
  for (i <- 0 to 8) {
    var field = new StackPane()
    field.setMinWidth((350 / 9.0) - 3)
    field.setMinHeight((350 / 9.0))
    col.add(field, 0, i)
    var letter = new Text((97 + i).toChar + "")
    letter.setFont(Font.font(10))
    letter.setStyle("-fx-font: 20 arial;")
    letter.setTextAlignment(TextAlignment.Center)
    field.getChildren.add(letter)
  }


  val gridPane = new GridPane()
  gridPane.setMinWidth(350)
  gridPane.setMinHeight(350)
  gridPane.setLayoutX(60)
  gridPane.setLayoutY(60)
  gridPane.setBackground(new Background(Array(new BackgroundFill(Color.SkyBlue, null, null))))
  gridPane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.Solid, null, new BorderWidths(3, 3, 3, 3))))

  for (i <- 0 to 8; j <- 0 to 8) {
    val field = new StackPane()
    field.setMinWidth(350 / 9.0)
    field.setMinHeight(350 / 9.0)
    field.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.Solid, null, null)))
    gridPane.add(field, j, i)
    if (board._1(i)(j) != null) {
      var text = new Text(board._1(i)(j))
      text.setFont(Font.font(10))
      text.setStyle("-fx-font: 24 arial;")
      text.setTextAlignment(TextAlignment.Center)
      field.getChildren.add(text)
    }

  }
  val stage = new Stage() {
    scene = new Scene(470, 500) {
      val pane: AnchorPane = new AnchorPane()
      pane.getChildren.add(gridPane)
      pane.getChildren.add(row)
      pane.getChildren.add(col)
      val line_1 = new Line()
      line_1.startX = 530 / 3.0 + 2
      line_1.startY = 65
      line_1.endX = 530 / 3.0 + 2
      line_1.endY = 415
      line_1.setStrokeWidth(5)
      pane.getChildren.add(line_1)

      val line_2 = new Line()
      line_2.startX = (530 / 3.0 + 2) + (350 / 3.0)
      line_2.startY = 65
      line_2.endX = (530 / 3.0 + 2) + (350 / 3.0)
      line_2.endY = 415
      line_2.setStrokeWidth(5)
      pane.getChildren.add(line_2)

      val line_3 = new Line()
      line_3.startX = 63
      line_3.startY = (530 / 3.0 + 3)
      line_3.endX = 413
      line_3.endY = (530 / 3.0 + 3)
      line_3.setStrokeWidth(5)
      pane.getChildren.add(line_3)

      val line_4 = new Line()
      line_4.startX = 63
      line_4.startY = (530 / 3.0 + 3) + (350 / 3)
      line_4.endX = 413
      line_4.endY = (530 / 3.0 + 3) + (350 / 3)
      line_4.setStrokeWidth(5)
      pane.getChildren.add(line_4)
      content = pane
    }
  }
  stage.setTitle("Sudoku")
  stage.show()

}

def sudocuGenerator(smatrix: Array[Array[String]], x: Int = 50): Array[Array[String]] = {
  val rand_1 = new scala.util.Random
  val rand_2 = new scala.util.Random
  for (i <- 0 until 20) {
    var value_1 = rand_1.nextInt(9)
    var value_2 = rand_2.nextInt(9)
    smatrix(value_1)(value_2) = null
  }
  smatrix
}

def initialMatrix(mat: Array[Array[String]], initials: Array[Array[String]]): Unit = {
  for (i <- 0 to 8; j <- 0 to 8) {
    if (mat(i)(j) != null) {
      initials(i)(j) = mat(i)(j)
    }
  }

}