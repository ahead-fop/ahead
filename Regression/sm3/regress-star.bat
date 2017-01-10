@echo "******************* STAR test ***************"
@java njb2.Main star.spec
@javac star.java
@java star > junk
@diff -b junk correct-star
