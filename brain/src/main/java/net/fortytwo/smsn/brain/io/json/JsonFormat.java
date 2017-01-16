package net.fortytwo.smsn.brain.io.json;

import net.fortytwo.smsn.brain.io.Format;

public class JsonFormat extends Format {

    public static final String VALUE_TRUNCATOR = " [...]";

    public static final String CHILDREN = "children";
    public static final String HAS_CHILDREN = "hasChildren";
    public static final String ID = "id";
    public static final String META = "meta";

    private static final JsonFormat instance = new JsonFormat();

    private JsonFormat() {
        super("JSON", Type.InMemory);
    }

    public static JsonFormat getInstance() {
        return instance;
    }
}