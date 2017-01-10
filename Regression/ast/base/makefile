Prj=foo

all: $(Prj)Sym.class $(Prj).class $(Prj)Scanner.class $(Prj)Parser.class

$(Prj)Sym.class : $(Prj)Sym.java
	javac -g $(Prj)Sym.java

$(Prj)Scanner.class : $(Prj)Scanner.java
	javac -g $(Prj)Scanner.java

$(Prj).class : $(Prj).java
	javac -g $(Prj).java

$(Prj)Parser.java $(Prj)Sym.java : $(Prj).cup
	java -mx32m java_cup.Main -expect 8 -parser $(Prj)Parser -symbols JakartaSym <Jakarta.cup

$(Prj)Scanner.java : $(Prj).lex
	java JLex.JLex $(Prj).lex

$(Prj).java $(Prj).lex $(Prj).cup : $(Prj).b
	java BaliC jakarta.b 

