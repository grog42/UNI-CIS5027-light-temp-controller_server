
:: Compile the client program
call ant -f build_controller.xml jar


:: Run the server program, pass port as an argument.
java -jar build/jar/Controller.jar 127.0.01 5002

::call ant -f build_controller.xml run
pause;