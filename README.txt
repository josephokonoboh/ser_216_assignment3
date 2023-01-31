This program was tested and run using Java 17


Working directory is the one that contains the README.txt

To compile:
    javac src/core/*.java src/ui/*.java -d classes


To run:
    java -cp classes core.CheckersGame    
    
JavaDoc documentation is already in the docs folder. However, it can be
regenerated using the command below:
    javadoc -d docs/ src/core/Board.java src/core/CheckersComputerPlayer.java src/core/CheckersLogic.java src/core/package-info.java src/ui/*.java
