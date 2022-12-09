package pawnrace

import java.io.PrintWriter
import java.io.InputStreamReader
import java.io.BufferedReader

// You should not add any more member values or member functions to this class
// (or change its name!). The autorunner will load it in via reflection and it
// will be safer for you to just call your code from within the playGame member
// function, without any unexpected surprises!
class PawnRace {
  // Don't edit the type or the name of this method
  // The colour can take one of two values: 'W' or 'B', this indicates your player colour
  fun playGame(colour: Char, output: PrintWriter, input: BufferedReader) {
    // You should call your code from within here
    // Step 1: If you are the black player, you should send a string containing the gaps
    // It should be of the form "wb" with the white gap first and then the black gap: i.e. "AH"

    var verifiedGaps = "ah" // Dummy

    if (colour == 'B') {
      while (true) {
        print("Please choose the gaps: ")
        val gaps = readLine()
        if (gaps != null && gaps.length == 2) {
          output.println(gaps)
          // Uncomment the following line when running the main method
          verifiedGaps = gaps
          break
        }
      }
    }

    // Regardless of your colour, you should now receive the gaps verified by the autorunner
    // (or by the human if you are using your own main function below), these are provided
    // in the same form as above ("wb"), for example: "AH"

    // Uncomment the following line when using the autorunner
//    verifiedGaps = input.readLine()

    // Now you may construct your initial board
    val board = Board(File(verifiedGaps[0]), File(verifiedGaps[1]))
    val white = Player(Piece.WHITE)
    val black = Player(Piece.BLACK)
    white.opponent = black
    black.opponent = white
    val game = Game(board, white)

    // If you are the white player, you are now allowed to move
    // you may send your move, once you have decided what it will be, with output.println(move)
    // for example: output.println("axb4")

    fun playerMove(ai: Boolean = true) {
      // Uncomment the line below if running the main method
      game.printBoard()
      if (ai) {
        val move = game.player.makeMove(game)
        if (move != null) {
          output.println(move)
          game.applyMove(move)
        }
      } else {
        while (true) {
          println("Please send your move: ")
          val move = readLine()
          if (move != null) {
            val parsed = game.parseMove(move)
            if (parsed != null) {
              output.println(move)
              game.applyMove(parsed)
              return
            }
          }
        }
      }
    }

    if (colour == 'W') {
      playerMove()
    }

    // After point, you may create a loop which waits to receive the other players move
    // (via input.readLine()), updates the state, checks for game over and, if not, decides
    // on a new move and again send that with output.println(move). You should check if the
    // game is over after every move.

    while (!game.over()) {
      // Uncomment the block below when using the autorunner
//      val opponent = input.readLine()
//      val move = game.parseMove(opponent)
//      if (move != null) {
//        game.applyMove(move)
//      }

      // Uncomment the line below when running the main method
      playerMove()

      if (game.over()) {
        break
      }

      playerMove()
    }


    // Once the loop is over, the game has finished and you may wish to print who has won
    // If your advanced AI has used any files, make sure you close them now!

    game.printBoard()
    println("${game.winner()} has won!")
  }
}

// When running the command, provide an argument either W or B, this indicates your player colour
fun main(args: Array<String>) {
  PawnRace().playGame(args[0][0], PrintWriter(System.out, true), BufferedReader(InputStreamReader(System.`in`)))
}
