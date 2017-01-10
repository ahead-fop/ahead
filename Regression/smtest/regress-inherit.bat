@echo "******************* otherwise-inherit test ***************"
@java dsml.Main inherit.spec
@javac inherit.java
@java test2 > junk
@diff junk correct-inherited
