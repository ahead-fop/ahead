@echo "************ Now testing WRFFE_MORTAR_FIST, WRFFE_ARTILLERY_FIST ****"
@java njb2.Main wrffe_fist.spec
@javac wrffe_fist.java
@java wrffe_mortar_fist > junk
@diff -b junk correct_fist
@echo "************ Now testing WRFFE_MLRS_FIST ****"
@java wrffe_mlrs_fist > junk
@diff -b junk correct_fomlrs
@echo "************* Now testing WRFFE_MORTAR_FIST_ERROR ****"
@java wrffe_mortar_fist_error > junk
@diff -b junk correct_m_f_error
