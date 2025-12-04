@echo off
echo Running Parent Signup Test for first CSV row only...
echo.

REM Set JAVA_HOME if not already set
if "%JAVA_HOME%"=="" (
    echo Setting JAVA_HOME to default location...
    set JAVA_HOME=C:\Program Files\Java\jdk-11
)

REM Run the test using Maven wrapper
echo Starting test execution...
.\mvnw.cmd test -Dtest=ParentSignupTest#testCompleteParentSignupFlow

echo.
echo Test execution completed!
pause
