module MP where

import System.Environment

type FileContents = String

type Keyword      = String
type KeywordValue = String
type KeywordDefs  = [(Keyword, KeywordValue)]

separators :: String
separators = " \n\t.,:;!\"\'()<>/\\"

-----------------------------------------------------

lookUp :: String -> [(String, a)] -> [a]
lookUp searchString pairs = [item | (string, item) <- pairs, searchString == string]

splitText :: [Char] -> String -> (String, [String])
splitText validSeparators "" = ("", [""])
splitText validSeparators (char : chars)
  | char `elem` validSeparators = (char : separatorChars, "" : splitWords)
  | otherwise                   = (separatorChars, (char : word) : words)
    where
      (separatorChars, splitWords) = splitText validSeparators chars
      (word : words) = splitWords

combine :: String -> [String] -> [String]
combine "" word = word
combine (separatorChar : separatorChars) (word : words)
  = word : [separatorChar] : combine separatorChars words

getKeywordDefs :: [String] -> KeywordDefs
getKeywordDefs = map getKeywordDef
  where
    getKeywordDef :: String -> (Keyword, KeywordValue)
    getKeywordDef []   = ("", "")
    getKeywordDef line = (keyword, def)
      where
        (keyword : space : defWords) = uncurry combine (splitText " " line)
        def = concat defWords

expand :: FileContents -> FileContents -> FileContents
expand text info = concat (combine separatorChars processedWords)
  where
    processedWords = [replaceWord word keywordDefs | word <- words]
    (separatorChars, words) = splitText separators text
    (newlines, lines) = splitText "\n" info
    keywordDefs = getKeywordDefs lines
    replaceWord :: String -> KeywordDefs -> String
    replaceWord "" keywordDefs = ""
    replaceWord word [] = word
    replaceWord word ((keyword, def) : keywordDefs)
      | word == keyword = def
      | otherwise       = replaceWord word keywordDefs

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
