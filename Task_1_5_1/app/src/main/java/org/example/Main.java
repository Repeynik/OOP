package org.example;

import org.example.enums.Alignment;

public class Main {
    public static void main(String[] args) {
        Table.Builder tableBuilder =
                new Table.Builder()
                        .withAlignments(Alignment.ALIGN_CENTER, Alignment.ALIGN_LEFT)
                        .withRowLimit(8)
                        .addRow("Index", "Random");

        for (int i = 1; i <= 5; i++) {
            final var value = (int) (Math.random() * 10);
            if (value > 5) {
                tableBuilder.addRow(i, new Text.Bold(String.valueOf(value)));
            } else {
                tableBuilder.addRow(i, (int) (Math.random() * 10));
            }
        }

        System.out.println(tableBuilder.build().serialize());

        Table.Builder tableBuilder2 =
                new Table.Builder()
                        .withAlignments(Alignment.ALIGN_RIGHT, Alignment.ALIGN_NONE)
                        .withRowLimit(2)
                        .addRow("Index", "Random")
                        .addRow(1, new Text.Bold("8"))
                        .addRow(2, "2");

        Table table = tableBuilder2.build();
        System.out.println(table.serialize());

        Quote quote = new Quote("This is a quote");
        System.out.println(quote.serialize());

        Image image = new Image("Alt text", "http://example.com/image.png");
        System.out.println(image.serialize());

        CodeBlock codeBlock = new CodeBlock("print('Hello, World!')", "python");
        System.out.println(codeBlock.serialize());

        Task task = new Task(true, "Complete the task");
        System.out.println(task.serialize());

        List_M orderedList = new List_M(true);
        orderedList.addItem("First item");
        orderedList.addItem("Second item");
        orderedList.addItem("Third item");
        System.out.println("Ordered List:");
        System.out.println(orderedList.serialize());

        List_M unorderedList = new List_M(false, "*");
        unorderedList.addItem("First item");
        unorderedList.addItem("Second item");
        unorderedList.addItem("Third item");
        System.out.println("Unordered List:");
        System.out.println(unorderedList.serialize());

        Text plainText = new Text("This is plain text.");
        Text.Bold boldText = new Text.Bold("This is bold text.");
        Text.Italic italicText = new Text.Italic("This is italic text.");
        Text.Strikethrough strikethroughText =
                new Text.Strikethrough("This is strikethrough text.");
        Text.Code codeText = new Text.Code("This is inline code.");

        StringBuilder formattedText = new StringBuilder();
        formattedText.append(plainText.serialize()).append("\n");
        formattedText.append(boldText.serialize()).append("\n");
        formattedText.append(italicText.serialize()).append("\n");
        formattedText.append(strikethroughText.serialize()).append("\n");
        formattedText.append(codeText.serialize()).append("\n");

        System.out.println(formattedText.toString());
    }
}
