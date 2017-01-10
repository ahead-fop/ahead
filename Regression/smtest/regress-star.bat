@echo "******************* STAR test ***************"
@java dsml.Main star.spec
@javac star.java
@java star > junk
@diff junk correct-star
