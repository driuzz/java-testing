package d.json.parser;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StreamingParserTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void test() throws IOException {
        InputStream inputStream = this.getClass().getResourceAsStream("/streamingParser.json");

        final String[] values = {""};
        final List<List<String>> lists = new ArrayList<>();

        new StreamingParser().parse(inputStream, new StreamingParser.Handler() {
            private List<String> list = null;

            @Override
            public void startObject(String name, String path) {
                if (name.equals("array3")) {
                    list = new ArrayList<>();
                }
            }

            @Override
            public void endObject(String name, String path) {
                if (name.equals("array3")) {
                    lists.add(list);
                    list = null;
                }
            }

            @Override
            public void value(String name, String value, String path) {
                if (list != null) {
                    if ("subname4".equals(name)) {
                        list.add("X=" + value);
                    } else if ("subsubname1".equals(name)) {
                        list.add("Y=" + value);
                    }
                } else if (path.equals("$.single1.single2.single3")) {
                    values[0] = value;
                }
            }
        });

        System.out.println(Arrays.toString(values));
        System.out.println(lists);
    }
}