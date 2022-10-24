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
angle (a, ax, rs) = a

-- Returns the axiom string for the given system.
axiom :: LSystem -> String
axiom (a, ax, rs) = ax

-- Returns the set of rules for the given system.
rules :: LSystem -> Rules
rules (a, ax, rs) = rs

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
move c a state@(coords@(x, y), angle)
  | c == 'F'  = ((x + cos rads, y + sin rads), angle)
  | c == 'L'  = (coords, angle + a)
  | c == 'R'  = (coords, angle - a)
  | otherwise = state -- Defensive handling
  where
    rads = degreesToRads angle

    -- Converts from degrees to radians.
    degreesToRads :: Float -> Float 
    degreesToRads degs = pi * (degs / halfCircle)
      where
        halfCircle = 180

    -- Modulus function for floating points.
    -- This keeps the angle (in degrees) from -360 to 360
    -- to avoid overflowing when the turtle rotates too many times.
    modFloat :: Float -> Float -> Float
    modFloat num mod
      | num <= negate mod = modFloat (num + mod) mod
      | num >= mod        = modFloat (num - mod) mod 
      | otherwise         = num
    
--
-- Trace lines drawn by a turtle using the given colour, following the
-- commands in `cs' and assuming the given angle of rotation.
--
initial = ((0, 0), 90)

trace1 :: Commands -> Angle -> Colour -> [ColouredLine]
trace1 cs a cl = trace
  where
    (trace, _) = trace1' cs a cl initial

    -- Helper function that returns a pair containing the trace and
    -- the unprocessed commands after the next ']' character, if there are any.
    trace1' :: Commands -> Angle -> Colour -> TurtleState 
      -> ([ColouredLine], Commands)
    trace1' [] a cl state = ([], [])
    trace1' (c:cs) a cl state@((x, y), angle)
      | c == '['  = (between ++ after, cs)
      | c == ']'  = ([], cs)
      | c == 'F'  = (((x, y), (x', y'), cl):trace, cs')
      | otherwise = (trace, cs')
      where
        (between, cs') = trace1' cs a cl state
        (after, _) = trace1' cs' a cl state
        (trace, _) = trace1' cs a cl state'
        state'@((x', y'), _) = move c a state

trace2 :: Commands -> Angle -> Colour -> [ColouredLine]
trace2 cs a cl = trace2' cs a cl initial []
  where
    -- Helper function that keeps track of the current state of the turtle
    -- and passes around the stack of turtle states.
    trace2' :: Commands -> Angle -> Colour -> TurtleState -> [TurtleState] 
      -> [ColouredLine]
    trace2' [] a cl state stack = []
    trace2' (c:cs) a cl state@((x, y), angle) stack
      | c == '['  = trace2' cs a cl state (state:stack)
      | c == ']'  = trace2' cs a cl pop rest
      | c == 'F'  = ((x, y), (x', y'), cl):trace
      | otherwise = trace
      where
        trace = trace2' cs a cl state' stack
        state'@((x', y'), _) = move c a state
        (pop:rest) = stack
      
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
