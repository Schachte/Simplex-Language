package core;

import static core.Expression.*;

public class AstPrinter implements Expression.Visitor<String> {

    public String print(Expression expression) {
        return expression.accept(this);
    }

    @Override
    public String visitAssignExpression(Assign expression) {
        return null;
    }

    @Override
    public String visitBinaryExpression(Binary expression) {
        return parenthesizeExpression(expression.operator.lexeme, expression.left, expression.right);
    }

    @Override
    public String visitGroupingExpression(Grouping expression) {
        return parenthesizeExpression("group", expression.expression);
    }

    @Override
    public String visitLiteralExpression(Literal expression) {
        if (expression.value == null) {
            return "nil";
        }
        return expression.value.toString();
    }

    @Override
    public String visitUnaryExpression(Unary expression) {
        return parenthesizeExpression(expression.operator.lexeme, expression.right);
    }

    @Override
    public String visitVariableExpression(Variable expression) {
        return null;
    }

    private String parenthesizeExpression(String name, Expression... expressions) {
        StringBuilder builder = new StringBuilder();

        builder.append("(").append(name);
        for (Expression expr : expressions) {
            builder.append(" ");
            builder.append(expr.accept(this));
        }
        builder.append(")");

        return builder.toString();
    }
}
