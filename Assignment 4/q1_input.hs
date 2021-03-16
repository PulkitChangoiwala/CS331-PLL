import Data.Char
import Data.List.Split
-- Run command "cabal update" then "cabal install split" to install split package

main :: IO ()
main = do 
       
      -- Prompting the user to enter numbers seperated by commas
      putStrLn ""
      putStrLn "Enter comma separated list of numbers: "
      str <- getLine
      putStrLn ""

      -- Numbers entered by the user, converted into list form are present in my_list
      let integer_list = map (read :: String -> Integer) (splitOn "," str) 
      print integer_list
