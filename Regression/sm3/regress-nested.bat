@echo "************ Now testing nested state constructs ****"
@java njb2.Main nested.spec
@javac nested.java
@java nested > junk
@diff -b junk correct_nested
@echo "************ Now testing error handlers in nested state constructs ****"
@java njb2.Main nestederror.spec
@javac nestederror.java
@java nestederror > junk
@diff -b junk correct_nestederror
