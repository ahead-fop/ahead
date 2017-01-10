@echo "************ WRFFE ARTILLERY *************"
@java njb2.Main wrffe_artillery.spec
@javac wrffe_artillery.java
@java wrffe_artillery > junk
@diff -b junk correct_artillery
