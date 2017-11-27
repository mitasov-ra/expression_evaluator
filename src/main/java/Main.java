import roman_mitasov.expression_eval.ExpressionEvaluator;
import roman_mitasov.expression_eval.Token;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import static roman_mitasov.expression_eval.Tokens.*;

public class Main {

    public static void main(String args[]) {
        DecimalFormat df = new DecimalFormat("#.#############");
        df.setRoundingMode(RoundingMode.CEILING);

        try {
        ExpressionEvaluator eval = new ExpressionEvaluator("75-69/3*96/12^8");
        System.out.println(df.format(eval.evaluate()));
            System.out.println(df.format(Math.tan(Math.PI)));
        } catch (Exception e) {
            e.printStackTrace();
        }

//         parser.getConstants().forEach((k, v) -> System.out.println(k + " : " + v));

    }
}
