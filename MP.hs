module MP where

import System.Environment
import Distribution.PackageDescription.Check (CheckPackageContentOps(getFileContents))
import Data.List

type FileContents = String

type Keyword      = String
type KeywordValue = String
type KeywordDefs  = [(Keyword, KeywordValue)]

separators :: String
separators = " \n\t.,:;!\"\'()<>/\\"
pageSeparator = "-----\n"

-----------------------------------------------------

lookUp :: String -> [(String, a)] -> [a]
lookUp searchStr pairs = [item | (str, item) <- pairs, searchStr == str]

splitText :: [Char] -> String -> (String, [String])
splitText validSeparators "" = ("", [""])
splitText validSeparators (char:chars)
  | char `elem` validSeparators = (char:separatorChars, "":splitWords)
  | otherwise                   = (separatorChars, (char:word):words)
    where
      (separatorChars, splitWords) = splitText validSeparators chars
      (word:words) = splitWords

combine :: String -> [String] -> [String]
combine "" word = word
combine (separatorChar:separatorChars) (word:words)
  = word:[separatorChar]:combine separatorChars words

getKeywordDefs :: [String] -> KeywordDefs
getKeywordDefs = map getKeywordDef
  where
    getKeywordDef :: String -> (Keyword, KeywordValue)
    getKeywordDef ""   = ("", "")
    getKeywordDef line = (keyword, def)
      where
        (keyword:space:defWords) = uncurry combine (splitText " " line)
        def = concat defWords

expand :: FileContents -> FileContents -> FileContents
expand text info
  | length expansions == 1 = concat expansions
  | otherwise              = concatMap appendPageSeparator expansions
  where
    (hashtags, keywordDefsSets) = splitText "#" info
    expansions = map (expandSingle text) keywordDefsSets

    -- Expands the given text based on the specified set of keyword definitions.
    expandSingle :: FileContents -> FileContents -> FileContents
    expandSingle text info = concat (combine separatorChars processedWords)
      where
        (separatorChars, words) = splitText separators text
        (newlines, lines) = splitText "\n" info
        keywordDefs = getKeywordDefs lines
        processedWords = map (`replaceWord` keywordDefs) words

    -- Returns the first substitution of the keyword, if there are any substitutions.
    replaceWord :: String -> KeywordDefs -> String
    replaceWord "" keywordDefs = ""
    replaceWord word []        = word
    replaceWord word keywordDefs
      | null defs = word
      | otherwise = head defs
      where
        defs = lookUp word keywordDefs
    
    -- Appends a page separator after each expansion.
    -- Only relevant when there are two or more expansions,
    -- i.e. multiple sets of keyword definitions.
    appendPageSeparator :: String -> String
    appendPageSeparator expansion = expansion ++ pageSeparator

-----------------------------------------------------

-- The provided main program which uses your functions to merge a
-- template and source file.
main :: IO ()
main = do
  args <- getArgs
  main' args

  where
    main' :: [String] -> IO ()
    main' [template, source, output] = do
      t <- readFile template
      i <- readFile source
      writeFile output (expand t i)
    main' _ = putStrLn ("Usage: runghc MP <template> <info> <output>")
