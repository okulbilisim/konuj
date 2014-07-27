rm -rf classes
mkdir classes

class_path=`echo ./lib/*.jar . | sed 's/ /:/g'`

echo "Compiling source code..."
/opt/jdk1.6.0_17/bin/javac -d classes -nowarn -classpath "$class_path" src/com/konuj/**/*.java src/com/**/*.java

echo "Goging to start the server..."
java -classpath "$class_path:./classes" com.konuj.ChatServer

