package dummy;

import java.io.Serializable;

public class DummyItem implements Serializable {
    public final String name, description, language, id;
    public final String[] data;
    public final int cost;

    public DummyItem(String id, String name, String description, int cost, String language, String[] data) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.cost = cost;
        this.language = language;
        this.data = data;
    }

    @Override
    public String toString() {
        return name;
    }
}