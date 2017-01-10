@echo off
rename %1.output.correct %1.output.correct.old
cp %1.output %1.output.correct
