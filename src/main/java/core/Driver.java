package core;

/**
 * Test driver that is used to test the visitor evaluation for a particular expression
 */
public class Driver {
    public static void main(String[] args) {
        Expression expression = new Expression.Binary(
                new Expression.Unary(
                        new Token(TokenType.MINUS, "-", null, 1),
                        new Expression.Literal(123)),
                new Token(TokenType.STAR, "*", null, 1),
                new Expression.Grouping(
                        new Expression.Literal(45.67)));

        System.out.println(new AstPrinter().print(expression));
    }

}
