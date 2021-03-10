sqroot x = 
            sqfunc 0 x          
            where sqfunc :: Float -> Float -> Float
                  sqfunc a b
                    | (abs(a-b)) < 0.00001
                           = a
                    | c*c > x
                           = sqfunc a c
                    | otherwise
                           = sqfunc c b
                      where c = (a+b)/2
                      
main = do
   putStrLn "Running Testcases:"   
   putStrLn "Square Root Of 23.56"   
   print(sqroot(23.56))
   putStrLn "Square Root Of 64"   
   print(sqroot(64))
   putStrLn "Square Root Of 141"   
   print(sqroot(141))
   putStrLn "Square Root Of 999.99"   
   print(sqroot(999.99))
   putStrLn "Square Root Of 1000"   
   print(sqroot(1000))
   putStrLn "Square Root Of 1320.45"   
   print(sqroot(1320.45))
   putStrLn "Square Root Of 4000"   
   print(sqroot(4000))
  
