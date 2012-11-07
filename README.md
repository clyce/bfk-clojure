#BFK-Clojure
##	--A Brain Fxck Interpreter on clojure seqs

Unlike other brain fxck interpreters, bfk-clojure is an embedded toolbox, aimming at using brainfxck to proceed existing datas, not programming in pure brainfxck.

###Seq Processing:
bfk-clojure provides three functions to proceed a clojure seq: brainfk, bfk-proceed and bfk-spawn
* bfk-spawn is a function works like the original brainfxck language,
	it takes a seq as a brainfxck machine, and a string as an brainfxck expression, evaluating the expression on the seq and collect each output (using brainfxck cmd '.') into a list.
	e.g
	```clojure
	(bfk-spawn [1 2 3 4 a b c d] ".>>++.>>.")
	; (1 5 a)
	(->> (bfk-spawn [65 97 32 10] "+++++++.>++++.+++++++..+++.>.<<+++++++++++++++.>.+++.------.--------.>+.>.") (map char) (apply str))
	; "Hello World!\n"
	```

* bfk-proceed is a function to process existing clojure seq,
	it takes a seq as a brainfxck machine, and a string as an brainfxck expression, evaluating the expression on the seq and returns a new seq as the result.
	e.g
	```clojure
	(bfk-proceed [0] "++")
	; (2)
	(bfk-proceed [1 2 3] ">+>++")
	; (1 3 5)
	(bfk-proceed [0] ">>+++")
	; (0 0 3)
	(bfk-proceed [1 1 1 1 1] ">[+>>]")
	; (1 2 1 2 1 0)
	```

* brainfk is a function provides the features of both bfk-proceed and bfk-spawn
	e.g
	```clojure
	(brain-phucker/brainfk [10] "[>+++++++>++++++++++>+++>+<<<<-]>++.>+.+++++++..+++.>++.<<+++++++++++++++.>.+++.------.--------.>+.>.")
	; {:result (0 87 100 33 10), :output (72 101 108 108 111 32 87 111 114 108 100 33 10)}
	```

###String Processing:
* All the functions works on string as well.
e.g
```clojure
(brain-phucker/bfk-proceed "teststring" "[-------------------------------->]")
; "TESTSTRING"
(brain-phucker/bfk-proceed "teststring" "[-------------------------------->>]")
; "TeStStRiNg"
(brain-phucker/bfk-proceed "teststring" "[-------------------------------->>>]")
; "TesTstRinG"
```

###Introduction To Brain Fxck Language
	Brain Fxck is a minimize language with 8 commands, which runs on a machine containing an array and a pointer, the pointer is initially pointing at the 0th element
	The 8 commands are:
	+ increases the value of the element pointed
	- decreases the value of the element pointed
	> move the pointer right by an element
	< move the pointer right by an element
	. output the value of the element pointed
	, input as the value of the element pointed

	The ',' command is not included in this version of bfk-clojure
