#!/bin/bash

SRC_DIR="app/src/main/java/org/task_1"
BIN_DIR="app/bin"
DOC_DIR="build/docs/javadoc"
MAIN_CLASS="Main"

echo "Компиляция исходников..."
javac -d "$BIN_DIR" "$SRC_DIR/Main.java" "$SRC_DIR/HeapSort.java"

echo "Генерация документации..."
javadoc -d "$DOC_DIR" "$SRC_DIR/Main.java" "$SRC_DIR/HeapSort.java"

echo "Запуск приложения..."
java -cp "$BIN_DIR" "$MAIN_CLASS"
