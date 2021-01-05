:: Updated bat files to resolve typing issues in the command line

:: compile the server program
call ant -f build_server.xml jar

:: Run the server program, pass port as an argument.
java -jar build/jar/Server.jar 5002

pause;