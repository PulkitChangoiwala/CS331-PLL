quicksort :: [Integer] -> [Integer]
quicksort [] = []
quicksort (x : xs) = (quicksort left) ++
                      [pivot] ++
                      (quicksort right)
     where
     pivot = x
     left = [ y | y <-xs, y<=x]
     right = [ y | y <-xs, y>x] 
     
main = do
   putStrLn "Running Testcases:"   
   putStrLn "Sorting List [5,4,3,2,1]"   
   print(quicksort([5,4,3,2,1]))
   putStrLn "Sorting List [55,450,-30,2,100, -789]"   
   print(quicksort([55,450,-30,2,100, -789]))
   putStrLn "Sorting List [1,1,1,1,1]"   
   print(quicksort([1,1,1,1,1]))
   putStrLn "Sorting List [-5,-4,-3,-2,-1]"   
   print(quicksort([-5,-4,-3,-2,-1]))
   putStrLn "Sorting List [400]"   
   print(quicksort([400]))
   putStrLn "Sorting List [89, -56, 34, 69, -90, 100, -700]"   
   print(quicksort([89, -56, 34, 69, -90, 100, -700]))
   
   
