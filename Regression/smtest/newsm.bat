rm -r dsml
java Bali.Main "dsml=J.sm[J.StringAST[J.Java]]"
cd dsml
javac Build.java
java dsml.Build
