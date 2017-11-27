package roman_mitasov.expression_eval;

import java.util.Collections;
import java.util.Map;

public class ExpressionEvaluator {

    private RPNCodeGen codeGen;
    private String expression;
    private Constants constants;

    public ExpressionEvaluator(String expression, char POINT) throws Exception {
        this.expression = expression;
        constants = new Constants();
        codeGen = new RPNCodeGen(constants);

        Parser parser = new Parser(codeGen);
        parser.parse(expression, new Lexer(expression, constants, POINT));
    }

    public ExpressionEvaluator(String expression) throws Exception {
        this(expression, '.');
    }

    public String getExpression() {
        return expression;
    }

    public Constants getConstants() {
        return constants;
    }

    public double evaluate() {
        return codeGen.eval();
    }

    public static class Constants {
//        boolean wasModified = false;
        private Map<String, Double> constants;

        public void addConst(String name, double val) {
            constants.put(name, val);
        }

//        public void resetModified() {
//            wasModified = false;
//        }

        public double getConstValue(String name) {
            return constants.get(name);
        }

        public void setConstValue(String name, double value) throws Exception {
            if (constants.containsKey(name)) {
//                wasModified = true;
                constants.put(name, value);
            } else {
                throw new Exception("No such constant in set");
            }
        }

        /**
         * Returns unmodifiable map of constants
         * To change constants' values use @link(setConstValue())
         *
         * @return Map
         */
        public Map<String, Double> getConstants() {
            return Collections.unmodifiableMap(constants);
        }
    }
}
