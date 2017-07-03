package d.json.parser;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Deque;
import java.util.LinkedList;

public class StreamingParser {

    public interface Handler {
        default void startObject(String name, String path) {
        }

        default void endObject(String name, String path) {
        }

        default void startArray(String name, String path) {
        }

        default void endArray(String name, String path) {
        }

        default void value(String name, String value, String path) {
        }
    }

    public void parse(InputStream inputStream, Handler handler) throws IOException {
        try (JsonReader reader = new JsonReader(new InputStreamReader(inputStream, "UTF-8"))) {
            parse(reader, handler);
        }
    }

    private void parse(JsonReader reader, Handler handler) throws IOException {
        Deque<String> names = new LinkedList<>();
        Deque<JsonToken> tokens = new LinkedList<>();
        int depth = 0;
        while (true) {
            JsonToken token = reader.peek();
            switch (token) {
                case NAME: {
                    names.push(reader.nextName());
                    break;
                }
                case BEGIN_OBJECT: {
                    String path = reader.getPath();
                    reader.beginObject();
                    tokens.push(token);
                    depth++;
                    handler.startObject(nullToEmpty(names.peek()), path);
                    break;
                }
                case END_OBJECT: {
                    depth--;
                    tokens.poll();
                    reader.endObject();
                    String path = reader.getPath();
                    handler.endObject(nullToEmpty(inArray(tokens) ? names.peek() : names.poll()), path);
                    break;
                }
                case BEGIN_ARRAY: {
                    String path = reader.getPath();
                    reader.beginArray();
                    tokens.push(token);
                    depth++;
                    handler.startArray(nullToEmpty(names.peek()), path);
                    break;
                }
                case END_ARRAY: {
                    depth--;
                    tokens.poll();
                    reader.endArray();
                    String path = reader.getPath();
                    handler.endArray(nullToEmpty(names.poll()), path);
                    break;
                }
                case BOOLEAN: {
                    String path = reader.getPath();
                    String value = String.valueOf(reader.nextBoolean());
                    handleValue(handler, names, tokens, path, value);
                    break;
                }
                case NUMBER:
                case STRING: {
                    String path = reader.getPath();
                    String value = reader.nextString();
                    handleValue(handler, names, tokens, path, value);
                    break;
                }
                case NULL: {
                    String path = reader.getPath();
                    reader.nextNull();
                    handleValue(handler, names, tokens, path, null);
                    break;
                }
                case END_DOCUMENT: {
                    return;
                }
            }
            if (depth == 0) {
                return;
            }
        }
    }

    private void handleValue(Handler handler, Deque<String> names, Deque<JsonToken> tokens, String path, String value) {
        handler.value(inArray(tokens) ? null : nullToEmpty(names.poll()), value, path);
    }

    private boolean inArray(Deque<JsonToken> tokens) {
        return tokens.peek() == JsonToken.BEGIN_ARRAY;
    }

    private String nullToEmpty(String value) {
        return value == null ? "" : value;
    }
}
