module Crypto where

import Data.Char

import Prelude hiding (gcd)

{-
The advantage of symmetric encryption schemes like AES is that they are efficient
and we can encrypt data of arbitrary size. The problem is how to share the key.
The flaw of the RSA is that it is slow and we can only encrypt data of size lower
than the RSA modulus n, usually around 1024 bits (64 bits for this exercise!).

We usually encrypt messages with a private encryption scheme like AES-256 with
a symmetric key k. The key k of fixed size 256 bits for example is then exchanged
via the aymmetric RSA.
-}

-------------------------------------------------------------------------------
-- PART 1 : asymmetric encryption

gcd :: Int -> Int -> Int
gcd m n
    | n == 0    = m
    | otherwise = gcd n (m `mod` n) 

phi :: Int -> Int
phi m = length [num | num <- [1..m], gcd m num == 1]

-- Calculates (u, v, d) the gcd (d) and Bezout coefficients (u and v)
-- such that au + bv = d
computeCoeffs :: Int -> Int -> (Int, Int)
computeCoeffs a b
  | b == 0    = (1, 0)
  | otherwise = (v', u' - q * v')
  where
    (u', v') = computeCoeffs b r
    (q, r)   = quotRem a b

-- Inverse of a modulo m
inverse :: Int -> Int -> Int
inverse a m = u `mod` m
  where
    (u, _) = computeCoeffs a m

-- Calculates (a^k mod m)
modPow :: Int -> Int -> Int -> Int
modPow a k m
  | k == 0 = 1 `mod` m
  | even k = modPow (a^2 `mod` m) (k `div` 2) m
  | odd k  = a * modPow a (k - 1) m `mod` m

-- Returns the smallest integer that is coprime with phi
smallestCoPrimeOf :: Int -> Int
smallestCoPrimeOf phi 
    = head [x | x <- [2..(phi + 1)], gcd x phi == 1]

-- Generates keys pairs (public, private) = ((e, n), (d, n))
-- given two "large" distinct primes, p and q
genKeys :: Int -> Int -> ((Int, Int), (Int, Int))
genKeys p q = ((e, n), (d, n))
  where
    n = p * q
    e = smallestCoPrimeOf ((p - 1) * (q - 1))
    d = inverse e ((p - 1) * (q - 1))

-- RSA encryption/decryption
rsaEncrypt :: Int -> (Int, Int) -> Int
rsaEncrypt x (e, n) = modPow x e n

rsaDecrypt :: Int -> (Int, Int) -> Int
rsaDecrypt c (d, n) = modPow c d n

-------------------------------------------------------------------------------
-- PART 2 : symmetric encryption


-- Returns position of a letter in the alphabet
toInt :: Char -> Int
toInt char = ord char - ord 'a'

-- Returns the n^th letter
toChar :: Int -> Char
toChar n = chr (n + ord 'a')

-- "adds" two letters
add :: Char -> Char -> Char
add char1 char2 = toChar ((toInt char1 + toInt char2) `mod` 26)

-- "substracts" two letters
substract :: Char -> Char -> Char
substract char1 char2 = toChar ((toInt char1 - toInt char2) `mod` 26)

-- the next functions present
-- 2 modes of operation for block ciphers : ECB and CBC
-- based on a symmetric encryption function e/d such as "add"

-- ecb (electronic codebook) with block size of a letter
--
ecbEncrypt :: Char -> String -> String
ecbEncrypt k m = [add mi k | mi <- m]

ecbDecrypt :: Char -> String -> String
ecbDecrypt k c = [substract ci k | ci <- c]

-- cbc (cipherblock chaining) encryption with block size of a letter
-- initialisation vector iv is a letter
-- last argument is message m as a string
--
cbcEncrypt :: Char -> Char -> String -> String
cbcEncrypt k iv [] = []
cbcEncrypt k iv x  = c1 : cbcEncrypt' k xs c1
  where
    c1 = add (add x1 iv) k
    (x1: xs) = x
    cbcEncrypt' :: Char -> String -> Char -> String
    cbcEncrypt' k [] prevChar = []
    cbcEncrypt' k x  prevChar = ci : cbcEncrypt' k xs ci
      where
          ci = add (add xi prevChar) k
          (xi: xs) = x

cbcDecrypt :: Char -> Char -> String -> String
cbcDecrypt k iv [] = []
cbcDecrypt k iv c  = x1 : cbcDecrypt' k cs c1
  where 
    x1 = substract (substract c1 k) iv
    (c1: cs) = c
    cbcDecrypt' :: Char -> String -> Char -> String 
    cbcDecrypt' k [] prevChar = []
    cbcDecrypt' k c  prevChar = xi : cbcDecrypt' k cs ci  
        where 
            xi = substract (substract ci k) prevChar
            (ci: cs) = c
