package org.example;

import org.example.enums.Alignment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Table extends Element {
    private List<List<String>> rows;
    private List<Alignment> alignments;
    private int rowLimit;

    private Table(Builder builder) {
        this.rows = builder.rows;
        this.alignments = builder.alignments;
        this.rowLimit = builder.rowLimit;
    }

    @Override
    public String serialize() {
        if (rows.isEmpty()) return "";
        int columns = rows.get(0).size();
        int[] columnWidths = new int[columns];

        // Calculate the maximum width for each column
        for (List<String> row : rows) {
            for (int i = 0; i < columns; i++) {
                columnWidths[i] = Math.max(columnWidths[i], row.get(i).length());
            }
        }

        StringBuilder sb = new StringBuilder();

        // Serialize the header row
        sb.append("| ");
        for (int i = 0; i < columns; i++) {
            sb.append(String.format("%-" + columnWidths[i] + "s", rows.get(0).get(i)))
                    .append(" | ");
        }
        sb.append("\n");

        // Serialize the alignment row
        sb.append("| ");
        for (int i = 0; i < columns; i++) {
            switch (alignments.get(i)) {
                case ALIGN_LEFT:
                    sb.append(":").append("-".repeat(columnWidths[i] - 1)).append(" | ");
                    break;
                case ALIGN_CENTER:
                    sb.append(":").append("-".repeat(columnWidths[i] - 2)).append(": | ");
                    break;
                case ALIGN_RIGHT:
                    sb.append("-".repeat(columnWidths[i] - 1)).append(": | ");
                    break;
                case ALIGN_NONE:
                    sb.append("-".repeat(columnWidths[i])).append(" | ");
                    break;
            }
        }
        sb.append("\n");

        // Serialize the data rows
        for (int i = 1; i < rows.size() && i <= rowLimit; i++) {
            sb.append("| ");
            for (int j = 0; j < columns; j++) {
                String cell = rows.get(i).get(j);
                int padding = columnWidths[j] - cell.length();
                switch (alignments.get(j)) {
                    case ALIGN_LEFT:
                        sb.append(String.format("%-" + columnWidths[j] + "s", cell)).append(" | ");
                        break;
                    case ALIGN_CENTER:
                        int leftPadding = padding / 2;
                        int rightPadding = padding - leftPadding;
                        sb.append(" ".repeat(leftPadding))
                                .append(cell)
                                .append(" ".repeat(rightPadding))
                                .append(" | ");
                        break;
                    case ALIGN_RIGHT:
                        sb.append(String.format("%" + columnWidths[j] + "s", cell)).append(" | ");
                        break;
                    case ALIGN_NONE:
                        sb.append(cell).append(" | ");
                        break;
                }
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    public static class Builder {
        private List<List<String>> rows = new ArrayList<>();
        private List<Alignment> alignments = new ArrayList<>();
        private int rowLimit = Integer.MAX_VALUE;

        public Builder withAlignments(Alignment... alignments) {
            this.alignments = Arrays.asList(alignments);
            return this;
        }

        public Builder withRowLimit(int rowLimit) {
            this.rowLimit = rowLimit;
            return this;
        }

        public Builder addRow(Object... cells) {
            List<String> row = new ArrayList<>();
            for (Object cell : cells) {
                if (cell instanceof Element) {
                    row.add(((Element) cell).serialize());
                } else {
                    row.add(cell.toString());
                }
            }
            rows.add(row);
            return this;
        }

        public Table build() {
            return new Table(this);
        }
    }
}
