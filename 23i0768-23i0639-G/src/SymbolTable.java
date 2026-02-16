// src/SymbolTable.java
import java.util.LinkedHashMap;
import java.util.Map;

public class SymbolTable {
    private static class SymbolInfo {
        String type;
        int firstOccurrence;
        int frequency;

        SymbolInfo(String type, int line) {
            this.type = type;
            this.firstOccurrence = line;
            this.frequency = 1;
        }
    }

    private Map<String, SymbolInfo> table = new LinkedHashMap<>();

    public void insert(String name, String type, int line) {
        if (table.containsKey(name)) {
            table.get(name).frequency++;
        } else {
            table.put(name, new SymbolInfo(type, line));
        }
    }

    public void printTable() {
        System.out.println("\n--- Symbol Table ---");
        System.out.printf("%-30s | %-15s | %-15s | %-10s\n", "Identifier", "Type", "First Line", "Frequency");
        System.out.println("-".repeat(78));
        for (Map.Entry<String, SymbolInfo> entry : table.entrySet()) {
            System.out.printf("%-30s | %-15s | %-15d | %-10d\n",
                    entry.getKey(), entry.getValue().type, 
                    entry.getValue().firstOccurrence, entry.getValue().frequency);
        }
    }
}