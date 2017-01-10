del .\DOCLETImpl\*.class
del .\DOCLETImpl\*.java
java JakBasic.Main buildDocletImpl.jak
cd DOCLETImpl
del Main.java
javac *.java
cd ..
