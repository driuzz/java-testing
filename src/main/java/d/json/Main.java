package d.json;

import d.json.parser.StreamingParser;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.err.println("File is required");
            return;
        }

        try (InputStream is = new FileInputStream(args[0])) {
            new StreamingParser().parse(is, new PrintingHandler());
        }
    }

    private static class PrintingHandler implements StreamingParser.Handler {
        private int level = 0;

        @Override
        public void startObject(String name, String path) {
            print("{", name, null, path);
            level++;
        }

        @Override
        public void endObject(String name, String path) {
            level--;
            print("}", name, null, path);
        }

        @Override
        public void startArray(String name, String path) {
            print("[", name, null, path);
            level++;
        }

        @Override
        public void endArray(String name, String path) {
            level--;
            print("]", name, null, path);
        }

        @Override
        public void value(String name, String value, String path) {
            print("|", name, value, path);
        }

        private void print(String tag, String name, String value, String path) {
            String text = tabs() + tag;
            if (name != null) {
                text += " " + name;
            }
            if (name != null && value != null) {
                text += " :=";
            }
            if (value != null) {
                text += " " + value;
            }
            System.out.println(text + "  --> " + path);
        }

        private String tabs() {
            return Stream.generate(() -> "\t").limit(level).reduce(String::concat).orElse("");
        }
    }
}
