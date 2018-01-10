@echo off
:: attrib -r *.* /s
if not [%1]==[] if not exist %1 goto usage
call projects %1
for /f "usebackq delims=" %%a in (%PROJECTSLIST%) do call clean_single "%%a" %2
goto:eof
:usage
echo usage: %0 folder[opt]