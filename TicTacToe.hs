module TicTacToe where

import Data.Char
import Data.Maybe
import Data.List
import Text.Read

-------------------------------------------------------------------
data Player = O | X
            deriving (Eq, Show)

data Cell = Empty | Taken Player
          deriving (Eq, Show)

type Board = ([Cell], Int)

type Position = (Int, Int)

-------------------------------------------------------------------

--
-- Some useful functions from, or based on, the unassessed problem sheets...
--

-- Preserves Just x iff x satisfies the given predicate. In all other cases
-- (including Nothing) it returns Nothing.
filterMaybe :: (a -> Bool) -> Maybe a -> Maybe a
filterMaybe p m@(Just x)
  | p x = m
filterMaybe p _
  = Nothing

-- Replace nth element of a list with a given item.
replace :: Int -> a -> [a] -> [a]
replace 0 p (c : cs)
  = p : cs
replace _ p []
  = []
replace n p (c : cs)
  = c : replace (n - 1) p cs

-- Returns the rows of a given board.
rows :: Board -> [[Cell]]
rows (cs , n)
  = rows' cs
  where
    rows' []
      = []
    rows' cs
      = r : rows' rs
      where
        (r, rs) = splitAt n cs

-- Returns the columns of a given board.
cols :: Board -> [[Cell]]
cols
  = transpose . rows

-- Returns the diagonals of a given board.
diags :: Board -> [[Cell]]
diags (cs, n)
  = map (map (cs !!)) [[k * (n + 1) | k <- [0 .. n - 1]],
                      [k * (n - 1) | k <- [1 .. n]]]

-------------------------------------------------------------------

gameOver :: Board -> Bool
gameOver b
  = rWin || cWin || dWin
  where 
    rWin = any (isSingleton . nub) (rows b)
    cWin = any (isSingleton . nub) (cols b)
    dWin = any (isSingleton . nub) (diags b)

    isSingleton [Taken _] 
      = True
    isSingleton  _  
      = False

-------------------------------------------------------------------

--
-- Moves must be of the form "row col" where row and col are integers
-- separated by whitespace. Bounds checking happens in tryMove, not here.
--
parsePosition :: String -> Maybe Position
parsePosition s
  = readMaybe pos :: Maybe Position
  where
    pos = "(" ++ replace (indexOf ' ') ',' s  ++ ")"

    indexOf chr 
      = indexOf' chr s 0
      where
        indexOf' _ "" _ 
          = -1
        indexOf' chr (c : cs) idx
          | chr == c  = idx
          | otherwise = indexOf' chr cs (idx + 1)

tryMove :: Player -> Position -> Board -> Maybe Board
tryMove p (row, col) (cs, n)
  | oob                = Nothing
  | cs !! idx == Empty = Just (replace idx (Taken p) cs, n)
  | otherwise          = Nothing
  where
    oob = row < 0 || row >= n || col < 0 || col >= n
    idx = row * n + col

-------------------------------------------------------------------
-- I/O Functions

prettyPrint :: Board -> IO ()
prettyPrint b
  = mapM_ putStrLn rs
  where
    rs = map format (rows b)

    format row
      = intersperse ' ' (concatMap cellToStr row)
    
    cellToStr (Taken p) 
      = show p
    cellToStr _         
      = "-"

-- The following reflect the suggested structure, but you can manage the game
-- in any way you see fit.

doParseAction :: (String -> Maybe a) -> String -> IO a
doParseAction f msg
  = do
      input <- getLine
      case f input of
        Nothing -> do
                     putStr msg
                     doParseAction f msg
        Just output -> return output

-- Repeatedly read a target board position and invoke tryMove until
-- the move is successful (Just ...).
takeTurn :: Board -> Player -> IO Board
takeTurn b p
  = do
      pos <- doParseAction parsePosition "Invalid move, try again: "
      case tryMove p pos b of
        Nothing -> do 
                     putStr "Invalid move, try again: "
                     takeTurn b p
        Just b' -> return b'

-- Manage a game by repeatedly: 1. printing the current board, 2. using
-- takeTurn to return a modified board, 3. checking if the game is over,
-- printing the board and a suitable congratulatory message to the winner
-- if so.
playGame :: Board -> Player -> IO ()
playGame b p
  = do
      prettyPrint b
      putStr ("Player " ++ show p ++ ", make your move (row, col): ")
      b' <- takeTurn b p
      if gameOver b' then do 
                            putStrLn ("Player " ++ show p ++ " has won!")
                            putStrLn "Thank you for playing"
                     else playGame b' (other p)
  where
    other X = O
    other O = X

-- Print a welcome message, read the board dimension, invoke playGame and
-- exit with a suitable message.
main :: IO ()
main
  = do
      putStrLn "Welcome to tic tac toe on an N x N board"
      putStr "Enter the board size (N): "
      n <- doParseAction (readMaybe :: String -> Maybe Int) "Invalid input, try again: "
      let b = ([Empty | _ <- [1..n * n]], n)
      playGame b X

-------------------------------------------------------------------

testBoard1, testBoard2, testBoard3, testBoard4 :: Board

testBoard1
  = ([Taken O,Taken X,Empty,Taken O,
      Taken O,Empty,Taken X,Taken X,
      Taken O,Empty,Empty,Taken X,
      Taken O,Taken X,Empty,Empty],
      4)

testBoard2
  = ([Taken X,Empty,
      Empty,Empty],
      2)

testBoard3
  = ([Taken O,Taken X,Empty,Taken O,Taken X,
      Taken O,Empty,Taken X,Taken X,Empty,
      Empty,Empty,Taken X,Taken O,Taken O,
      Taken O,Taken X,Empty,Empty,Taken X,
      Taken X,Empty,Taken O,Empty,Empty],
      5)

testBoard4
  = ([Empty],
     1)
