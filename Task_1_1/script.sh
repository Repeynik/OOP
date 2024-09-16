#!/bin/bash

SRC_DIR="app/src/main/java/org/task_1"
BIN_DIR="app/bin"
DOC_DIR="build/docs/javadoc"
MAIN_CLASS="org.task_1.Main"

echo "Компиляция исходников..."
javac -d "$BIN_DIR" "$SRC_DIR/Main.java" "$SRC_DIR/HeapSort.java" "$SRC_DIR/ArrayHelper.java" "$SRC_DIR/PerformanceTest.java"

echo "Генерация документации..."
javadoc -d "$DOC_DIR" "$SRC_DIR/Main.java" "$SRC_DIR/HeapSort.java" "$SRC_DIR/ArrayHelper.java" "$SRC_DIR/PerformanceTest.java"

echo "Создание jar файла..."
jar cf myApp.jar -C "$BIN_DIR" .

echo "Запуск приложения..."
java -jar myApp.jar