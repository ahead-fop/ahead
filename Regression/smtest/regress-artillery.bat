@echo "************ WRFFE ARTILLERY *************"
@java dsml.Main wrffe_artillery.spec
@javac wrffe_artillery.java
@java wrffe_artillery > junk
@diff junk correct_artillery
