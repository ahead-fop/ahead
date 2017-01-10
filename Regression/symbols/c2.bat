@echo off
sort %1.output > junk1
sort %1.output.correct > junk2
diff -w junk1 junk2
