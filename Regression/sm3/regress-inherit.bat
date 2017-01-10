@echo "******************* otherwise-inherit test ***************"
@java njb2.Main inherit.spec
@javac inherit.java
@java test2 > junk
@diff -b junk correct-inherited
