@echo "******************* State refinement test ***************"
@java njb2.Main refined.spec
@javac refined.java
@java refined > junk
@diff -b junk correct-refined

@echo "******************* State refinement with proceeds test ***************"
@java njb2.Main r.spec
@javac r.java
@java r > junk
@diff -b junk correct-refined

@echo "******************* Edge refinement test ***************"
@java njb2.Main edgeref.spec
@javac edgeref.java
@java edgeref > junk
@diff -b junk correct-edgeref

@echo "******************* Edge refinement with proceeds test ***************"
@java njb2.Main er.spec
@javac er.java
@java er > junk
@diff -b junk correct-edgeref


