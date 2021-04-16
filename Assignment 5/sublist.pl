sublist( [], _ ).
sublist( [X|TAIL1], [X|TAIL2] ) :- sublist( TAIL1, TAIL2 ).
sublist( [X|TAIL1], [_|TAIL2] ) :- sublist( [X|TAIL1], TAIL2 ).