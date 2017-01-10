@echo "************ WRFFE MORTAR *************"
@java njb2.Main wrffe_mortar.spec
@javac wrffe_mortar.java
@java wrffe_mortar > junk
@diff -b junk correct_mortar
