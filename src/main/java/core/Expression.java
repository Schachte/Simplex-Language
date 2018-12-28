package core;

import java.util.List;

public abstract class Expression {
    public interface Visitor<T> {
        T visitBinaryExpression(Binary expression);

        T visitGroupingExpression(Grouping expression);

        T visitLiteralExpression(Literal expression);

        T visitUnaryExpression(Unary expression);
    }

    static class Binary extends Expression {
        Binary(Expression left, Token operator, Expression right) {
            this.left = left;
            this.operator = operator;
            this.right = right;
        }

        <T> T accept(Visitor<T> visitor) {
            return visitor.visitBinaryExpression(this);
        }

        final Expression left;
        final Token operator;
        final Expression right;
    }

    static class Grouping extends Expression {
        Grouping(Expression expression) {
            this.expression = expression;
        }

        <T> T accept(Visitor<T> visitor) {
            return visitor.visitGroupingExpression(this);
        }

        final Expression expression;
    }

    static class Literal extends Expression {
        Literal(Object value) {
            this.value = value;
        }

        <T> T accept(Visitor<T> visitor) {
            return visitor.visitLiteralExpression(this);
        }

        final Object value;
    }

    static class Unary extends Expression {
        Unary(Token operator, Expression right) {
            this.operator = operator;
            this.right = right;
        }

        <T> T accept(Visitor<T> visitor) {
            return visitor.visitUnaryExpression(this);
        }

        final Token operator;
        final Expression right;
    }

    abstract <T> T accept(Visitor<T> visitor);
}
