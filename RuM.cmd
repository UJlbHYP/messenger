cd "/media/ilnur/disk/For_Ilnur/Practise/Messager_3_in_1_edition"
cd sources
javac -d ../classes  *.java
cd ../classes
jar -cvmf ../tesr ../../Main.jar com images
cd ../../
java -jar Main.jar