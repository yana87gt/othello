default:	compile clean

compile:
	javac -encoding UTF-8 Main.java
	jar cvf Othello.jar ./*.class ./image/*.png ./image/*.jpg
	jar uvfm Othello.jar ./Manifest.txt
	mv Othello.jar ../

clean:
	rm -f ./*.class