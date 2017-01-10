@echo "************ WRFFE MLRS *************"
@java dsml.Main wrffe_mlrs.spec
@javac wrffe_mlrs.java
@java wrffe_mlrs > junk
@diff junk correct_mlrs
