@echo "************ WRFFE MLRS *************"
@java njb2.Main wrffe_mlrs.spec
@javac wrffe_mlrs.java
@java wrffe_mlrs > junk
@diff -b junk correct_mlrs
