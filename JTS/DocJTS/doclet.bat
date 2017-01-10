del .\DOCLETJTS\*.class
del .\DOCLETJTS\*.java
java JakBasic.Main buildDoclet.jak
cd DOCLETJTS
del Main.java
javac *.java
cd ..
