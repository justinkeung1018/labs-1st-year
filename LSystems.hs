module LSystems where

import IC.Graphics

type Rule
  = (Char, String)

type Rules
  = [Rule]

type Angle
  = Float

type Axiom
  = String

type LSystem
  = (Angle, Axiom, Rules)

type Vertex
  = (Float, Float)

type TurtleState
  = (Vertex, Float)

type Command
  = Char

type Commands
  = [Command]

type Stack
  = [TurtleState]

type ColouredLine
  = (Vertex, Vertex, Colour)

----------------------------------------------------------
-- Functions for working with systems.

-- Returns the rotation angle for the given system.
angle :: LSystem -> Float
angle (angle, axiom, rules) = angle

-- Returns the axiom string for the given system.
axiom :: LSystem -> String
axiom (angle, axiom, rules) = axiom

-- Returns the set of rules for the given system.
rules :: LSystem -> Rules
rules (angle, axiom, rules) = rules

-- Return the binding for the given character in the list of rules
lookupChar :: Char -> Rules -> String
-- Pre: the character has a binding in the Rules list
lookupChar char [] = []
lookupChar char ((ruleChar, ruleString):rules)
  | char == ruleChar = ruleString
  | otherwise        = lookupChar char rules

-- Expand command string s once using rule table r
expandOne :: String -> Rules -> String
expandOne s r = concatMap (`lookupChar` r) s

-- Expand command string s n times using rule table r
expand :: String -> Int -> Rules -> String
expand s 0 r = s
expand s 1 r = expandOne s r
expand s n r = expand (expandOne s r) (n - 1) r

-- Move a turtle
move :: Command -> Angle -> TurtleState -> TurtleState
move 'F' angle ((x, y), currAngle) = ((x + cos rads, y + sin rads), currAngle)
  where
    rads = currAngle * (pi / 180)
move 'L' angle (currCoords, currAngle) = (currCoords, currAngle + angle)
move 'R' angle (currCoords, currAngle) = (currCoords, currAngle - angle)
move undefinedCommand angle currState = currState -- Defensive handling

--
-- Trace lines drawn by a turtle using the given colour, following the
-- commands in `cs' and assuming the given angle of rotation.
--
initialState = ((0, 0), 90)

trace1 :: Commands -> Angle -> Colour -> [ColouredLine]
trace1 cmds angle colour = trace
  where
    (trace, _) = trace1' cmds angle colour initialState

    -- Helper function that returns a pair containing the trace and
    -- the unprocessed commands after the next ']' character, if there are any.
    trace1' :: Commands -> Angle -> Colour -> TurtleState -> ([ColouredLine], Commands)
    trace1' [] angle colour currState = ([], [])
    trace1' ('[':cmds) angle colour currState 
      = (traceInBrackets ++ traceAfterBrackets, cmds)
      where
        (traceInBrackets, cmdsAfterBrackets) = trace1' cmds angle colour currState
        (traceAfterBrackets, _) = trace1' cmdsAfterBrackets angle colour currState
    trace1' (']':cmds) angle colour currState = ([], cmds)
    trace1' (cmd:cmds) angle colour currState
      | cmd == 'F' = (((currX, currY), (nextX, nextY), colour) : trace, unprocessedCmds)
      | otherwise  = (trace, unprocessedCmds)
      where
        nextState@((nextX, nextY), nextAngle) = move cmd angle currState
        (trace, unprocessedCmds) = trace1' cmds angle colour nextState
        ((currX, currY), currAngle) = currState

trace2 :: Commands -> Angle -> Colour -> [ColouredLine]
trace2 cmds angle colour = trace2' cmds angle colour initialState []
  where
    -- Helper function that keeps track of the current state of the turtle
    -- and passes around the stack of turtle states.
    trace2' :: Commands -> Angle -> Colour -> TurtleState -> [TurtleState] -> [ColouredLine]
    trace2' [] angle colour currState stack = []
    trace2' ('[':cmds) angle colour currState stack
      = trace2' cmds angle colour currState (currState:stack)
    trace2' (']':cmds) angle colour currState (prevState:stack)
      = trace2' cmds angle colour prevState stack
    trace2' (cmd:cmds) angle colour currState stack
      | cmd == 'F' = ((currX, currY), (nextX, nextY), colour) : trace
      | otherwise  = trace
      where
        trace = trace2' cmds angle colour nextState stack
        ((currX, currY), currAngle) = currState
        nextState@((nextX, nextY), nextAngle) = move cmd angle currState
      
----------------------------------------------------------
-- Some given functions

expandLSystem :: LSystem -> Int -> String
expandLSystem (_, axiom, rs) n
  = expandOne (expand axiom n rs) commandMap

drawLSystem1 :: LSystem -> Int -> Colour -> IO ()
drawLSystem1 system n colour
  = drawLines (trace1 (expandLSystem system n) (angle system) colour)

drawLSystem2 :: LSystem -> Int -> Colour -> IO ()
drawLSystem2 system n colour
  = drawLines (trace2 (expandLSystem system n) (angle system) colour)

----------------------------------------------------------
-- Some test systems.

cross, triangle, arrowHead, peanoGosper, dragon, snowflake, tree, bush, canopy, galaxy :: LSystem

cross
  = (90,
     "M-M-M-M",
     [('M', "M-M+M+MM-M-M+M"),
      ('+', "+"),
      ('-', "-")
     ]
    )

triangle
  = (90,
     "-M",
     [('M', "M+M-M-M+M"),
      ('+', "+"),
      ('-', "-")
     ]
    )

arrowHead
  = (60,
     "N",
     [('M', "N+M+N"),
      ('N', "M-N-M"),
      ('+', "+"),
      ('-', "-")
     ]
    )

peanoGosper
  = (60,
     "M",
     [('M', "M+N++N-M--MM-N+"),
      ('N', "-M+NN++N+M--M-N"),
      ('+', "+"),
      ('-', "-")
     ]
    )

dragon
  = (45,
     "MX",
     [('M', "A"),
      ('X', "+MX--MY+"),
      ('Y', "-MX++MY-"),
      ('A', "A"),
      ('+', "+"),
      ('-', "-")
     ]
    )

snowflake
  = (60,
     "M--M--M",
     [('M', "M+M--M+M"),
      ('+', "+"),
      ('-', "-")
     ]
    )

tree
  = (45,
     "M",
     [('M', "N[-M][+M][NM]"),
      ('N', "NN"),
      ('[', "["),
      (']', "]"),
      ('+', "+"),
      ('-', "-")
     ]
    )

bush
  = (22.5,
     "X",
     [('X', "M-[[X]+X]+M[+MX]-X"),
      ('M', "MM"),
      ('[', "["),
      (']', "]"),
      ('+', "+"),
      ('-', "-")
     ]
    )

canopy
  = (30.0,
     "M",
     [('M', "M[+MM][-MM]M[-M][+M]M"),
      ('[', "["),
      (']', "]"),
      ('+', "+"),
      ('-', "-")
     ]
    )

galaxy
  = (36.0,
     "[M]++[M]++[M]++[M]++[M]",
     [('M', "+M--M---M"),
      ('[', "["),
      (']', "]"),
      ('+', "+"),
      ('-', "-")
     ]
    )

commandMap :: Rules
commandMap
  = [('M', "F"),
     ('N', "F"),
     ('X', ""),
     ('Y', ""),
     ('A', ""),
     ('[', "["),
     (']', "]"),
     ('+', "L"),
     ('-', "R")
    ]
