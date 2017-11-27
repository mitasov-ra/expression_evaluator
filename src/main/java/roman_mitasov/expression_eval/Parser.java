package roman_mitasov.expression_eval;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import static roman_mitasov.expression_eval.Tokens.*;

public class Parser {
    private Stack<Token> tokenStack;
    private RPNCodeGen codeGen;

    public Parser(RPNCodeGen codeGen) {
        this.codeGen = codeGen;
        tokenStack = new Stack<>();
    }


    private boolean isOperand(int id) {
        return id == FACT ||
                id == PERSENT ||
                id == NUMBER ||
                id == CONST ||
                id == RPAREN ||
                id == PI ||
                id == E;
    }

    private void pushOperator(Token token) throws Exception {
        int priority = token.getAssoc() != Token.RIGHT ? token.getPriority() : token.getPriority() + 1;
        while (!tokenStack.empty() && priority <= tokenStack.peek().getPriority()) {
            codeGen.push(tokenStack.pop());
        }
        tokenStack.push(token);
    }

    public void parse(String expression, Lexer lexer) throws Exception {
        Token token;
        int prevTokenId = -2;
        do {
            token = lexer.nextToken();
            switch (token.getId()) {
                case CONST:
                case NUMBER:
                case PI:
                case E:
                    if (isOperand(prevTokenId)) {
                        pushOperator(new Token(MUL, Token.LEFT).setPriority(2));
                    }
                    codeGen.push(token);
                    break;

                case RPAREN:
                    if (!isOperand(prevTokenId)) {
                        throw new Exception("unexpected )");
                    }
                    while (!tokenStack.empty() && tokenStack.peek().getId() != LPAREN) {
                        codeGen.push(tokenStack.pop());
                    }
                    tokenStack.pop();

                    break;

                case END:
                    if (!isOperand(prevTokenId)) {
                        throw new Exception("unexpected END");
                    }
                    while (!tokenStack.empty()) {
                        if (tokenStack.peek().getId() == RPAREN) {
                            throw new Exception("unexpected )");
                        }
                        codeGen.push(tokenStack.pop());
                    }
                    break;
                case LPAREN:
                default:

                    if (token.getAssoc() == Token.PREF) {
                        if (isOperand(prevTokenId)) {
                            pushOperator(new Token(MUL, Token.LEFT).setPriority(2));
                        }
                        tokenStack.push(token);
                        break;
                    }
                    if (!isOperand(prevTokenId)) {
                        if (token.getId() == MINUS) {
                            token = new Token(UN_MINUS, Token.PREF).setPriority(3);
                            tokenStack.push(token);
                            break;
                        } else if (token.getId() == PLUS) {
                            continue;
                        }
                        throw new Exception("operator without operand");
                    }

                    pushOperator(token);
            }
            prevTokenId = token.getId();

        } while (token.getId() != END);
    }
}
