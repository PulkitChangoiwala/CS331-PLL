updateTuple :: (Int, Int, Integer, Integer) -> (Int, Int, Integer, Integer)
updateTuple (itr, n, a, b)
                                 = if itr < n then updateTuple(itr+1, n, b, a+b)
                                   else (itr, n, a, b)
                                
                                
                                                                                                                                     
lastElementOfTuple :: (a,b,c,d) -> d
lastElementOfTuple (a, b, c, d) = d                            
                                

fib :: Int -> Integer
fib n = lastElementOfTuple (updateTuple(1, n, 0, 1))

main = do
   putStrLn "Running Testcases:"   
   putStrLn "1st Fibonacci"   
   print(fib(1))
   putStrLn "5th Fibonacci"   
   print(fib(5))
   putStrLn "10th Fibonacci"   
   print(fib(10))
   putStrLn "11th Fibonacci"   
   print(fib(11))
   putStrLn "12th Fibonacci"   
   print(fib(12))
   putStrLn "55th Fibonacci"   
   print(fib(55))
   putStrLn "200th Fibonacci"   
   print(fib(200))

