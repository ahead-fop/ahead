@echo "************ WRFFE MORTAR *************"
@java dsml.Main wrffe_mortar.spec
@javac wrffe_mortar.java
@java wrffe_mortar > junk
@diff junk correct_mortar
