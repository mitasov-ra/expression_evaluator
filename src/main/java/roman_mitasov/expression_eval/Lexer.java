package roman_mitasov.expression_eval;

import java.util.HashMap;

import static roman_mitasov.expression_eval.Tokens.*;

public class Lexer {

    private static HashMap<String, Token> predeclared = new HashMap<>();

    static {
        predeclared.put("sin", new Token(SIN, Token.PREF));
        predeclared.put("cos", new Token(COS, Token.PREF));
        predeclared.put("tan", new Token(TAN, Token.PREF));
        predeclared.put("log", new Token(LOG, Token.PREF));
        predeclared.put("ln", new Token(LN, Token.PREF));
        predeclared.put("exp", new Token(EXP, Token.PREF));
        predeclared.put("sqrt", new Token(SQRT, Token.PREF));
        predeclared.put("abs", new Token(ABS, Token.PREF));
        predeclared.put("sgn", new Token(SGN, Token.PREF));
        predeclared.put("pi", new Token(PI, 0));
    }

    private int pos;
    private char[] buffer;
    private final char POINT;
    private static final char ENDING_CHAR = '$';
    private ExpressionEvaluator.Constants constants;
    private Token savedToken = null;

    public Lexer(String expression, ExpressionEvaluator.Constants constants, char POINT) {
        this.POINT = POINT;
        this.constants = constants;

        buffer = new char[expression.length() + 1];
        expression.getChars(0, expression.length(), buffer, 0);
        buffer[expression.length()] = ENDING_CHAR;
        pos = 0;
    }

    public Lexer(String expression, ExpressionEvaluator.Constants constants) {
        this(expression, constants, '.');
    }

    public Token lookForToken() throws Exception {
        if (savedToken == null) {
            savedToken = nextToken();
            return savedToken;
        }

        return savedToken;
    }

    public Token nextToken() throws Exception {
        if (savedToken != null) {
            Token temp = savedToken;
            savedToken = null;
            return temp;
        }


        int state = 0;
        char c;
        int begin = pos;
        while (true) {
            c = buffer[pos];
            switch (state) {
                case 0:
                    if (isWhitespace(c)) {
                        break;
                    }

                    if (isConstant(c)) {
                        begin = pos;
                        state = 1;
                        break;
                    }

                    if (isNumber(c)) {
                        begin = pos;
                        state = 2;
                        break;
                    }

                    switch (buffer[pos++]) {
                        case '+':
                            return new Token(PLUS, Token.LEFT, pos - 1).setPriority(1);
                        case '-':
                            return new Token(MINUS, Token.LEFT, pos - 1).setPriority(1);
                        case '*':
                            return new Token(MUL, Token.LEFT, pos - 1).setPriority(2);
                        case '/':
                            return new Token(DIV, Token.LEFT, pos - 1).setPriority(2);
                        case '^':
                            return new Token(POW, Token.RIGHT, pos - 1).setPriority(4);
                        case '(':
                            return new Token(LPAREN, Token.PREF, pos - 1);
                        case ')':
                            return new Token(RPAREN, 0, pos - 1);
                        case '\u221a':
                            return new Token(SQRT, Token.PREF, pos - 1).setPriority(7);
                        case '!':
                            return new Token(FACT, Token.PREF, pos - 1).setPriority(5);
                        case '%':
                            return new Token(PERSENT, Token.SUF, pos - 1).setPriority(6);
                        case ENDING_CHAR:
                            return new Token(END, 0, pos);
                        default:
                            throw new Exception("Invalid character");

                    }
                case 1:
                    if (isConstant(c)) {
                        break;
                    } else {
                        String word = String.valueOf(buffer, begin, pos - begin);
                        if (predeclared.containsKey(word.toLowerCase())) {
                            return predeclared.get(word.toLowerCase()).setPosition(begin);
                        }
                        constants.addConst(word, 0);
                        return (new Token(CONST, 0, begin)).setName(word);
                    }
                case 2:
                    if (isNumber(c)) {
                        break;
                    } else {
                        String word = String.valueOf(buffer, begin, pos - begin);
                        Token tok = new Token(NUMBER, 0, begin);
                        if (word.equals("" + POINT)) {
                            return tok.setValue(0d);
                        } else {
                            return tok.setValue(Double.parseDouble(word.replace(POINT, '.')));
                        }
                    }
            }
            pos++;
        }
    }

    private boolean isNumber(char n) {
        return n >= '0' && n <= '9' || n == POINT;
    }

    private boolean isWhitespace(char w) {
        return " \n\r\t".indexOf(w) != -1;
    }

    private boolean isConstant(char c) {
        return "_#@'~&".indexOf(c) != -1 || c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z';
    }
}
