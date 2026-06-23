package src;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class StorageManager {
    private static final String FILE_NAME = "tasks.json";
    private static final Path FILE_PATH = Paths.get(System.getProperty("user.home"), ".gemini", "antigravity", "scratch", "todo-list-java", FILE_NAME);

    public static void saveTasks(List<Task> tasks) {
        try {
            // Ensure parent directory exists
            Files.createDirectories(FILE_PATH.getParent());
            
            StringBuilder sb = new StringBuilder();
            sb.append("[\n");
            for (int i = 0; i < tasks.size(); i++) {
                Task t = tasks.get(i);
                sb.append("  {\n");
                sb.append("    \"id\": \"").append(escape(t.getId())).append("\",\n");
                sb.append("    \"title\": \"").append(escape(t.getTitle())).append("\",\n");
                sb.append("    \"description\": \"").append(escape(t.getDescription())).append("\",\n");
                sb.append("    \"category\": \"").append(escape(t.getCategory())).append("\",\n");
                sb.append("    \"priority\": \"").append(escape(t.getPriority())).append("\",\n");
                sb.append("    \"dueDate\": \"").append(escape(t.getDueDate())).append("\",\n");
                sb.append("    \"completed\": ").append(t.isCompleted()).append("\n");
                sb.append("  }");
                if (i < tasks.size() - 1) {
                    sb.append(",");
                }
                sb.append("\n");
            }
            sb.append("]");
            
            Files.writeString(FILE_PATH, sb.toString());
        } catch (IOException e) {
            System.err.println("Error saving tasks: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static List<Task> loadTasks() {
        List<Task> tasks = new ArrayList<>();
        if (!Files.exists(FILE_PATH)) {
            return tasks;
        }
        
        try {
            String json = Files.readString(FILE_PATH);
            tasks = parse(json);
        } catch (IOException e) {
            System.err.println("Error loading tasks: " + e.getMessage());
            e.printStackTrace();
        }
        return tasks;
    }

    private static String escape(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }

    private static List<Task> parse(String json) {
        List<Task> tasks = new ArrayList<>();
        if (json == null || json.trim().isEmpty()) return tasks;
        
        int i = 0;
        int len = json.length();
        
        while (i < len) {
            char c = json.charAt(i);
            if (c == '{') {
                String id = "";
                String title = "";
                String description = "";
                String category = "General";
                String priority = "Medium";
                String dueDate = "No Date";
                boolean completed = false;
                
                i++;
                while (i < len && json.charAt(i) != '}') {
                    i = skipWhitespace(json, i);
                    if (i < len && json.charAt(i) == '"') {
                        StringBuilder keyBuilder = new StringBuilder();
                        i = readString(json, i, keyBuilder);
                        String key = keyBuilder.toString();
                        
                        i = skipWhitespace(json, i);
                        if (i < len && json.charAt(i) == ':') {
                            i++; // skip ':'
                            i = skipWhitespace(json, i);
                            if (i < len && json.charAt(i) == '"') {
                                StringBuilder valBuilder = new StringBuilder();
                                i = readString(json, i, valBuilder);
                                String val = valBuilder.toString();
                                
                                switch (key) {
                                    case "id" -> id = val;
                                    case "title" -> title = val;
                                    case "description" -> description = val;
                                    case "category" -> category = val;
                                    case "priority" -> priority = val;
                                    case "dueDate" -> dueDate = val;
                                }
                            } else {
                                // Boolean or raw value
                                StringBuilder valBuilder = new StringBuilder();
                                while (i < len && json.charAt(i) != ',' && json.charAt(i) != '}') {
                                    valBuilder.append(json.charAt(i));
                                    i++;
                                }
                                String val = valBuilder.toString().trim();
                                if (key.equals("completed")) {
                                    completed = Boolean.parseBoolean(val);
                                }
                                continue; // Skip string read index increment since we already read past the character
                            }
                        }
                    }
                    i = skipWhitespace(json, i);
                    if (i < len && json.charAt(i) == ',') {
                        i++;
                    }
                }
                tasks.add(new Task(id, title, description, category, priority, dueDate, completed));
            }
            i++;
        }
        return tasks;
    }

    private static int skipWhitespace(String s, int i) {
        while (i < s.length() && Character.isWhitespace(s.charAt(i))) {
            i++;
        }
        return i;
    }

    private static int readString(String s, int i, StringBuilder sb) {
        i++; // Skip leading quote
        boolean escaped = false;
        while (i < s.length()) {
            char c = s.charAt(i);
            if (escaped) {
                if (c == 'n') sb.append('\n');
                else if (c == 'r') sb.append('\r');
                else if (c == 't') sb.append('\t');
                else sb.append(c);
                escaped = false;
            } else if (c == '\\') {
                escaped = true;
            } else if (c == '"') {
                i++; // Skip closing quote
                break;
            } else {
                sb.append(c);
            }
            i++;
        }
        return i;
    }
}
