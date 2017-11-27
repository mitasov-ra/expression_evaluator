package roman_mitasov.expression_eval;

public class Token {
    static final int PREF = 1;
    static final int SUF = 2;
    static final int LEFT = 3;
    static final int RIGHT = 4;

    private int id;
    private int position;
    private int assoc;
    private int priority;
    private String name = null;
    private double value;

    public int getPriority() {
        return priority;
    }

    public Token setPriority(int priority) {
        this.priority = priority;
        return this;
    }

    public double getValue() {
        return value;
    }

    public Token setValue(double value) {
        this.value = value;
        return this;
    }

    public int getAssoc() {
        return assoc;
    }

    public Token setAssoc(int assoc) {
        this.assoc = assoc;
        return this;
    }

    public String getName() {
        return name;
    }

    public Token setName(String name) {
        this.name = name;
        return this;
    }

    public int getId() {
        return id;
    }

    public Token setId(byte id) {
        this.id = id;
        return this;
    }

    public int getPosition() {
        return position;
    }

    public Token setPosition(int position) {
        this.position = position;
        return this;
    }

    public Token(int id, int assoc, int position) {
        this.id = id;
        this.position = position;
        this.assoc = assoc;
    }

    public Token(int id, int assoc) {
        this.id = id;
        this.assoc = assoc;
    }
}
