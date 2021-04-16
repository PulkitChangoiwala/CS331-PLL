% making faultynode dynamic 
:- dynamic faultynode/1.


check(X, Y, List) :- mazelink(X, Y), 
	\+ faultynode(Y), 
	\+ member(Y,List).


%find minimum path length
minimum_len(Mn, [], Mn).
minimum_len(Mn, [[Len|_]|Taillists], Length) :- Length >= Len, minimum_len(Mn, Taillists, Len).
minimum_len(Mn, [[Len|_]|Taillists], Length) :- Length < Len, minimum_len(Mn,Taillists, Length).
minimum_len(Mn, [[Len|_]|Taillists]) :- minimum_len(Mn, Taillists, Len).


%find all paths from source to destination
paths(Path,Len,[Dest|Taillist],Dest) :- Path = [Len, Dest|Taillist].
paths(Path,Len,[Curr|Taillist],Dest) :-
	check(Curr, New_node, Taillist),
	Len1 is Len +1,
	paths(Path,Len1,[New_node,Curr|Taillist],Dest).


%find path with minimum length
path_with_length(Len, [[Len|Taillist]|_], Ans) :- Ans = Taillist.
path_with_length(Len, [[_|_]|Taillist], Ans) :- path_with_length(Len, [Taillist], Ans).


valid :-
	writeln('Enter node you want to make valid: '),
	read(Node), nl,
	retract(faultynode(Node)).

fault :-
	writeln('Enter node you want to make faulty: '),
	read(Node), nl, 
	assert(faultynode(Node)).



% printing

print([]).
print([Head]) :- write(Head).
print([Head|Tail]) :- write(Head), write(' =>> '), print(Tail).
	


shortest_path(Src, Dest, Result) :-
	writeln("Processing All Paths, wait for some time:"),
	setof(Path, paths(Path,1,[Src],Dest), Set),
	nl, write("Length of minimum path: "),
	minimum_len(Len,Set),
	writeln(Len), nl,
	path_with_length(Len, Set, Ans),
	reverse(Ans,Result),
	writeln("Printing Minimum Path"),
	print(Result), nl.
	