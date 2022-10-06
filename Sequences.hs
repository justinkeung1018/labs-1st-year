module Sequences where

import Data.Char (ord, chr)

-- Returns the first argument if it is larger than the second,
-- the second argument otherwise
maxOf2 :: Int -> Int -> Int
maxOf2 x y
    | x > y     = x
    | otherwise = y

-- Returns the largest of three Ints
maxOf3 :: Int -> Int -> Int -> Int
maxOf3 x y z
    | z > maxOf2 x y    = z
    | otherwise         = maxOf2 x y

-- Returns True if the character represents a digit '0'..'9';
-- False otherwise
isADigit :: Char -> Bool
isADigit char = '0' <= char && char <= '9'

-- Returns True if the character represents an alphabetic
-- character either in the range 'a'..'z' or in the range 'A'..'Z';
-- False otherwise
isAlpha :: Char -> Bool
isAlpha char = 'a' <= char && char <= 'z' || 'A' <= char && char <= 'Z'

-- Returns the integer [0..9] corresponding to the given character.
-- Note: this is a simpler version of digitToInt in module Data.Char,
-- which does not assume the precondition.
digitToInt :: Char -> Int
-- Pre: the character is one of '0'..'9'
digitToInt digit = ord digit - ord '0'

-- Returns the upper case character corresponding to the input.
-- Uses guards by way of variety.
toUpper :: Char -> Char
toUpper char
    | 'A' <= char && char <= 'Z'    = char
    | otherwise                     = chr (ord char - ord 'a' + ord 'A')

--
-- Sequences and series
--

-- Arithmetic sequence
arithmeticSeq :: Double -> Double -> Int -> Double
arithmeticSeq a d n = a + d * fromIntegral n

-- Geometric sequence
geometricSeq :: Double -> Double -> Int -> Double
geometricSeq a r n = a * r ^ n

-- Arithmetic series
arithmeticSeries :: Double -> Double -> Int -> Double
arithmeticSeries a d n = fromIntegral (n + 1) * (a + d * fromIntegral n / 2)

-- Geometric series
geometricSeries :: Double -> Double -> Int -> Double
geometricSeries a r n
    | r == 1    = a * fromIntegral (n + 1)
    | otherwise = a * (1 - r ^ (n + 1)) / (1 - r)