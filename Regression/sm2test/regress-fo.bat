echo "******** now testing MORTAR & ARTILLERY FO protocol ********"
@java njb2.Main wrffe_fo.spec
@javac wrffe_fo.java
@java wrffe_mortar_fo > junk
@diff -b junk correct_fo
@echo "******** now testing MLRS FO protocol ********"
@java wrffe_mlrs_fo > junk
@diff -b junk correct_fomlrs
