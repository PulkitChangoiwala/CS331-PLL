newtonian_root(X, TEMP, RESULT, ACCURACY) :- abs(TEMP*TEMP - X) < ACCURACY, RESULT is TEMP. 
newtonian_root(X, TEMP, RESULT, ACCURACY) :- newtonian_root(X, (TEMP*TEMP + X)/(2*TEMP), RESULT, ACCURACY).

root(X, Result, Accuracy) :- newtonian_root(X, X/2, Res, Accuracy), Result is Res.
