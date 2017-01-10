@rm *.ser
@rm *.class
@rm *.java
@cp M.jjava M.java
@cp wrffe.jjava wrffe.java
@javac M.java wrffe.java
@call regress-refined
@call regress-fist
@call regress-fo
@call regress-mortar
@call regress-artillery
@call regress-mlrs
@call regress-repeater
@call regress-inherit
@call regress-star
@call regress-nested
