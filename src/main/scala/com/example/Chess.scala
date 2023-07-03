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



def ChessPiece(cart: Int): (ImageView, Int) = {
  val OPTIMAL_SIZE = 35
  var card: Int = cart
  var location = String.format("src/resources/images_chess/black_pawn.png")
  if (card == 1) {
    location = String.format("src/resources/images_chess/black_pawn.png")
  } else if (card == 11) {
    location = String.format("src/resources/images_chess/white_pawn.png")
  } else if (card == 2) {
    location = String.format("src/resources/images_chess/black_rook.png")
  } else if (card == 22) {
    location = String.format("src/resources/images_chess/white_rook.png")
  } else if (card == 3) {
    location = String.format("src/resources/images_chess/black_bishop.png")
  } else if (card == 33) {
    location = String.format("src/resources/images_chess/white_bishop.png")
  } else if (card == 4) {
    location = String.format("src/resources/images_chess/black_knight.png")
  } else if (card == 44) {
    location = String.format("src/resources/images_chess/white_knight.png")
  } else if (card == 5) {
    location = String.format("src/resources/images_chess/black_queen.png")
  } else if (card == 55) {
    location = String.format("src/resources/images_chess/white_queen.png")
  } else if (card == 6) {
    location = String.format("src/resources/images_chess/black_king.png")
  } else if (card == 66) {
    location = String.format("src/resources/images_chess/white_king.png")
  }
  val image: Image = new Image("file:" + location)
  val imageView: ImageView = new ImageView(image)
  imageView.setFitWidth(OPTIMAL_SIZE)
  imageView.setFitHeight(OPTIMAL_SIZE)
  return (imageView, card)
}

def Chessboard(): (Array[Array[(ImageView, Int)]], Int) = {
  val board: Array[Array[(ImageView, Int)]] = Array.ofDim[(ImageView, Int)](8, 8)
  var turn: Int = 1
  //pawn
  for (i <- 0 to 7) {
    board(1)(i) = ChessPiece(1)
    board(6)(i) = ChessPiece(11)
  }
  //rock
  board(0)(0) = ChessPiece(2)
  board(0)(7) = ChessPiece(2)
  board(7)(0) = ChessPiece(22)
  board(7)(7) = ChessPiece(22)
  //knight
  board(0)(1) = ChessPiece(4)
  board(0)(6) = ChessPiece(4)
  board(7)(1) = ChessPiece(44)
  board(7)(6) = ChessPiece(44)
  //bishop
  board(0)(2) = ChessPiece(3)
  board(0)(5) = ChessPiece(3)
  board(7)(2) = ChessPiece(33)
  board(7)(5) = ChessPiece(33)
  //king
  board(0)(3) = ChessPiece(5)
  board(7)(3) = ChessPiece(55)
  //Queen
  board(0)(4) = ChessPiece(6)
  board(7)(4) = ChessPiece(66)
  (board, turn)
}

def ChessController(board: (Array[Array[(ImageView, Int)]], Int), x: String): (Boolean, (Array[Array[(ImageView, Int)]], Int)) = {
  if (!isValidSquareChess(x)) {
    println("invalid input")
    return (false, board)
  }
  val row: Int = Char.char2int(x.charAt(0)) - 97
  val col: Int = Char.char2int(x.charAt(1)) - 49
  val row2: Int = Char.char2int(x.charAt(3)) - 97
  val col2: Int = Char.char2int(x.charAt(4)) - 49
  var turn = board._2
  println(row + " " + col + " " + row2 + " " + col2)
  if (board._1(row)(col) == null) {
    println("empty cell")
    return (false, board)
  }
  //the access taken send the new state
  println("try to validate")
  if (validmoveChess(row, col, row2, col2, board)) {
    println("valid move")
    board._1(row2)(col2) = board._1(row)(col)
    board._1(row)(col) = null
    turn += 1
    if (turn > 2) {
      turn -= 2
    }
    println("turn" + turn)
    return (true, (board._1, turn))
  }
  return (false, board)

}

def ChessDrawer(board: (Array[Array[(ImageView, Int)]], Int)): Unit = {
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
        field.getChildren.add(board._1(i)(j)._1)
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

def validmoveChess(row: Int, col: Int, row2: Int, col2: Int, board: (Array[Array[(ImageView, Int)]], Int)): Boolean = {
  val player = getTurn(row, col, board)
  println("player r1c1 " + player)
  println("boardturn " + board._2)
  println("turn r2c2 " + getTurn(row2, col2, board))

  if (getTurn(row2, col2, board) == player || player != board._2) {
    return false
  }
  val piece = board._1(row)(col)
  if (piece._2 == 1 || piece._2 == 11) {
    println("enter pawn code")
    return isPawnStep(row, col, row2, col2, board)
  }
  if (piece._2 == 2 || piece._2 == 22) {
    println("enter rock code")
    return isHorizontal(row, col, row2, col2, board) || isVertical(row, col, row2, col2, board)
  }
  if (piece._2 == 3 || piece._2 == 33) {
    println("enter bishop code")
    return isDiagonal(row, col, row2, col2, board)
  }
  if (piece._2 == 4 || piece._2 == 44) {
    println("enter knight code")
    return isLStep(row, col, row2, col2)
  }
  if (piece._2 == 5 || piece._2 == 55) {
    println("enter king code")
    return isHorizontal(row, col, row2, col2, board) || isVertical(row, col, row2, col2, board) || isDiagonal(row, col, row2, col2, board)
  }
  if (piece._2 == 6 || piece._2 == 66) {
    println("enter queen code")
    return isKingStep(row, col, row2, col2)
  }
  return false
}

def getTurn(r: Int, c: Int, board: (Array[Array[(ImageView, Int)]], Int)): Int = {
  if (board._1(r)(c) == null) {
    return 0
  }
  val box = board._1(r)(c)._2
  if (box < 7) {
    return 2
  }
  else if (box > 7) {
    return 1
  }
  return 0
}

def isValidSquareChess(s: String): Boolean = {
  val pattern = """[a-h][1-8] [a-h][1-8]""".r
  pattern.findFirstIn(s).isDefined
}

def isPawnStep(row: Int, col: Int, row2: Int, col2: Int, board: (Array[Array[(ImageView, Int)]], Int)): Boolean = {
  val rowDiff = row2 - row
  val colDiff = col2 - col
  val player = getTurn(row, col, board)
  if (player == 1) {
    if (rowDiff == -2 && row == 6 && getTurn(row2, col2, board) == 0)
      return true;
    if (rowDiff != -1)
      return false;
    if (colDiff == 0) {
      if (getTurn(row2, col2, board) != 0)
        return false;
    }
    else if (colDiff == 1 || colDiff == -1) {
      if (getTurn(row2, col2, board) != 2)
        return false;
    }

  } else {
    if (rowDiff == 2 && row == 1 && getTurn(row2, col2, board) == 0)
      return true;
    if (rowDiff != 1)
      return false;

    if (colDiff == 0) {
      if (getTurn(row2, col2, board) != 0)
        return false;
    }
    else if (colDiff == 1 || colDiff == -1) {
      if (getTurn(row2, col2, board) != 1)
        return false;
    }

  }
  return true
}

def isHorizontal(row: Int, col: Int, row2: Int, col2: Int, board: (Array[Array[(ImageView, Int)]], Int)): Boolean = {
  val rowDiff = row2 - row
  val colDiff = col2 - col
  if (rowDiff != 0)
    return false;
  var step: Int = 0
  if (colDiff > 0)
    step = 1
  else
    step = -1

  var newColDiff: Int = step
  while (newColDiff != colDiff) {
    if (board._1(row)(col + newColDiff) != null) {
      return false
    }
    newColDiff += step
  }
  return true
}

def isVertical(row: Int, col: Int, row2: Int, col2: Int, board: (Array[Array[(ImageView, Int)]], Int)): Boolean = {
  val rowDiff = row2 - row
  val colDiff = col2 - col
  if (colDiff != 0)
    return false;
  var step: Int = 0
  if (rowDiff > 0) {
    step = 1
  }
  else {
    step = -1
  }
  var newRowDiff: Int = step
  while (newRowDiff != rowDiff) {
    if (board._1(row + newRowDiff)(col) != null) {
      return false
    }
    newRowDiff += step
  }
  return true
}

def isDiagonal(row: Int, col: Int, row2: Int, col2: Int, board: (Array[Array[(ImageView, Int)]], Int)): Boolean = {
  val rowDiff = row2 - row
  val colDiff = col2 - col
  if (math.abs(rowDiff) != math.abs(colDiff)) {
    return false
  }

  var rowStep: Int = 0
  var colStep: Int = 0

  if (rowDiff > 0) {
    rowStep = 1
  }
  else {
    rowStep = -1
  }

  if (colDiff > 0) {
    colStep = 1
  }
  else {
    colStep = -1
  }

  var newRowDiff: Int = rowStep
  var newColDiff: Int = colStep

  while (newRowDiff != rowDiff) {
    if (board._1(row + newRowDiff)(col + newColDiff) != null) {
      return false
    }
    newRowDiff += rowStep
    newColDiff += colStep
  }
  return true
}

def isLStep(row: Int, col: Int, row2: Int, col2: Int): Boolean = {
  val rowDiff = row2 - row
  val colDiff = col2 - col
  if (rowDiff == 0 || colDiff == 0) {
    return false
  }
  if (math.abs(rowDiff) + math.abs(colDiff) == 3) {
    return true
  }
  return false
}

def isKingStep(row: Int, col: Int, row2: Int, col2: Int): Boolean = {
  val rowDiff = row2 - row
  val colDiff = col2 - col
  if (math.abs(rowDiff) > 1 || Math.abs(colDiff) > 1) {
    return false
  }
  else {
    return true
  }
  return false
}
