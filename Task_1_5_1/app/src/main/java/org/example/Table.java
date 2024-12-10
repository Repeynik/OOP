package org.example;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
// Table class with Builder
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
        System.out.println(columns);
        for (List<String> row : rows) {
            if (row.size() != columns) throw new IllegalArgumentException("Inconsistent number of columns.");
        }
        StringBuilder sb = new StringBuilder();
        sb.append("| ");
        for (String cell : rows.get(0)) {
            sb.append(cell).append(" | ");
        }
        sb.append("\n");
        sb.append("| ");
        int hasThing = 0;
        for (Alignment align : alignments) {
            switch (align) {
                case ALIGN_LEFT:
                    if (hasThing == 0){
                        sb.append(":");
                        hasThing = 1;
                    }
                    for(int i = 0; i < rowLimit - 1 -1 -1 -1 ; i++)
                        sb.append("-");
                    sb.append("-| ");
                    break;
                case ALIGN_CENTER:
                    if (hasThing == 0){
                        sb.append(":");
                        hasThing = 1;
                    }
                    for(int i = 0; i < rowLimit - 1 -1 -1 -1 ; i++)
                        sb.append("-");
                    // sb.append(": -:| ");
                    break;
                case ALIGN_RIGHT:
                    for(int i = 0; i < rowLimit - 1 -1 -1 -1  ; i++)
                        sb.append("-");
                    // sb.append("-:| ");
                    if (hasThing == 0){
                        sb.append("-:| ");
                        hasThing = 1;
                    }
                    else{
                        sb.append("-| ");
                    }
                    break;
            }
        }
        sb.append("\n");
        for (int i = 1; i < rows.size() && i <= rowLimit; i++) {
            sb.append("| ");
            for (String cell : rows.get(i)) {
                sb.append(cell).append(" | ");
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