merge(nil, T, T) :- !.
merge(T, nil, T) :- !.

merge(tree((X1, Y1, Value1), L1, R1),
      tree((X2, Y2, Value2), L2, R2),
	  tree((X1, Y1, Value1), L1, New)) :-
		Y1 > Y2,
		merge(R1, tree((X2, Y2, Value2), L2, R2), New).
merge(tree((X1, Y1, Value1), L1, R1),
			tree((X2, Y2, Value2), L2, R2),
			tree((X2, Y2, Value2), New, R2)) :- 
		Y1 =< Y2,
		merge(tree((X1, Y1, Value1), L1, R1), L2, New).

split(nil, _, (nil, nil, nil)).
split(tree((Key, Y, Value), L, R), Key, (LeftSplit, tree((Key, Y, Value), nil, nil), RightSplit)) :-
		Key = X,
		split(L, Key, (LL, _, LR)),
		split(R, Key, (RL, _, RR)),
		merge(LL, RL, LeftSplit),
		merge(LR, RR, RightSplit).
split(tree((X, Y, Value), L, R), Key, (tree((X, Y, Value), L, LeftSplit), MidSplit, RightSplit)) :-
		Key > X,
		split(R, Key, (LeftSplit, MidSplit, RightSplit)).
split(tree((X, Y, Value), L, R), Key, (LeftSplit, MidSplit, tree((X, Y, Value), RightSplit, R))) :-
		Key < X,
		split(L, Key, (LeftSplit, MidSplit, RightSplit)).

map_build([], nil).
map_build([(Key, Value) | Tail], T) :-
		map_build(Tail, CurrT),
		map_put(CurrT, Key, Value, T).

map_get(tree((Key, Y, Value), L, R), Key, Value).
map_get(tree((X, Y, _), L, R), Key, Value) :-
		Key > X, map_get(R, Key, Value).
map_get(tree((X, Y, _), L, R), Key, Value) :-
		Key < X, map_get(L, Key, Value).

map_put(T, Key, Value, Res) :-
		split(T, Key, (T1, _, T2)),
		rand_int(10000000, Y),
		merge(T1, tree((Key, Y, Value), nil, nil), T5),
		merge(T5, T2, Res).

map_remove(T, Key, Res) :-
		split(T, Key, (T1, _, T2)),
		merge(T1, T2, Res).

rightmost(tree((X, Y, Value), L, nil), X).
rightmost(tree((X, Y, Value), L, R), CeilingKey) :- rightmost(R, CeilingKey).
map_floorKey(T, Key, CeilingKey) :- split(T, Key, (L, M, R)), merge(L, M, NewT), rightmost(NewT, CeilingKey).
