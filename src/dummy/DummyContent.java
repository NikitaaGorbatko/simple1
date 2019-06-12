package dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DummyContent {
    public static final List<WordBlock> ITEMS = new ArrayList<WordBlock>();
    public static final Map<String, WordBlock> ITEM_MAP = new HashMap<String, WordBlock>();
    private static final int COUNT = 25;

    DummyContent(ArrayList<WordBlock> dummyItemArrayList)  {
        for (int i = 1; i <= COUNT; i++) {
            String[] as = {"f","f"};
            //addItem(createDummyItem(i + "", "asdf", " fff", 2, "fff", as));
        }
    }

    private static void addItem(WordBlock item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static WordBlock createDummyItem(String id, String name, String description, String topic, int cost, String language, String data) {
        return new WordBlock(id, name, description, topic, cost, language, data);
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }


}
