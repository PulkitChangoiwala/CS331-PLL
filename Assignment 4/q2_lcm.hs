import Data.Char
import Data.List.Split
-- Run command "cabal update" then "cabal install split" to install split package

find_lcm :: [Integer] -> Integer
find_lcm [] = 1
find_lcm (a_head : a_tail) = lcm a_head (find_lcm a_tail)

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

      let lcm_value = find_lcm integer_list
      print lcm_value