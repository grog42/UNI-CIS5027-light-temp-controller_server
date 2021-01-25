
:: compile the server program
call ant -f build_server.xml jar

:: Run the server program, pass port as an argument.
java -jar build/jar/Server.jar 5002

pause;