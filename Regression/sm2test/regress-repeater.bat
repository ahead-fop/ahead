@echo "************** WRFFE COMMON REPEATER ***********"
@java njb2.Main wrffe_repeater.spec
@javac wrffe_repeater.java

@echo "************** WRFFE MORTAR REPEATER ***********"
@java wrffe_mortar_repeater > junk
@diff -b junk correct-mortar-repeater

@echo "************** WRFFE ARTILLERY REPEATER ***********"
@java wrffe_artillery_repeater > junk
@diff -b junk correct-artillery-repeater

@echo "************** WRFFE MLRS REPEATER ***********"
@java wrffe_mlrs_repeater > junk
@diff -b junk correct-mlrs-repeater
