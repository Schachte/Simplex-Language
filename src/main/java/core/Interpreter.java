package core;

/**
 * Essentially operates as an "InterpreterVisitor" allowing us to walk the expression
 * tree in a cleaner fashion.
 */
public class Interpreter implements Expression.Visitor<Object> {
    public void interpret(Expression expr) {
        try {
            Object value = evaluate(expr);
            System.out.println(stringify(value));
        } catch (RuntimeError e) {
            Simplex.error(e);
        }
    }

    @Override
    public Object visitBinaryExpression(Expression.Binary expression) {
        Object left = evaluate(expression.left);
        Object right = evaluate(expression.right);

        switch (expression.operator.tokenType) {
            case PLUS:
                checkNumberOperands(expression.operator, left, right);
                if (left instanceof Double && right instanceof Double) {
                    return (double) left + (double) right;
                }

                if (left instanceof String && right instanceof String) {
                    return (String) left + (String) right;
                }
            case GREATER:
                checkNumberOperands(expression.operator, left, right);
                return (double) left > (double) right;
            case GREATER_EQUAL:
                checkNumberOperands(expression.operator, left, right);
                return (double) left >= (double) right;
            case LESS:
                checkNumberOperands(expression.operator, left, right);
                return (double) left < (double) right;
            case LESS_EQUAL:
                checkNumberOperands(expression.operator, left, right);
                return (double) left <= (double) right;
            case MINUS:
                checkNumberOperands(expression.operator, left, right);
                return (double) left - (double) right;
            case BANG_EQUAL:
                checkNumberOperands(expression.operator, left, right);
                return !isEqual(left, right);
            case EQUAL_EQUAL:
                checkNumberOperands(expression.operator, left, right);
                return isEqual(left, right);
            case SLASH:
                checkNumberOperands(expression.operator, left, right);
                return (double) left / (double) right;
            case STAR:
                checkNumberOperands(expression.operator, left, right);
                return (double) left * (double) right;
        }

        // Unreachable
        return null;
    }

    @Override
    public Object visitGroupingExpression(Expression.Grouping expression) {
        return evaluate(expression.expression);
    }

    @Override
    public Object visitLiteralExpression(Expression.Literal expression) {
        return expression.value;
    }

    @Override
    public Object visitUnaryExpression(Expression.Unary expression) {
        Object right = evaluate(expression.right);

        switch (expression.operator.tokenType) {
            case BANG:
                return !isTruthy(right);
            case MINUS:
                checkNumberOperand(expression.operator, right);
                return -(double) right;
        }

        // Unreachable
        return null;
    }

    private boolean isEqual(Object a, Object b) {
        if (a == null && b == null) return true;
        if (a == null) return false;

        return a.equals(b);
    }

    /**
     * We will simply treat everything that is null or false and falsey
     * Everything that is otherwise is truthy
     */
    private boolean isTruthy(Object object) {
        if (object == null) return false;
        if (object instanceof Boolean) return (boolean) object;
        return true;
    }

    /**
     * Recursively evaluate sub-expressions by visiting grouping expressions nested deeply
     */
    private Object evaluate(Expression expression) {
        return expression.accept(this);
    }

    private void checkNumberOperand(Token operator, Object operand) {
        if (operand instanceof Double) return;
        throw new RuntimeError(operator, "Operand must be a number");
    }

    private void checkNumberOperands(Token operator, Object leftOp, Object rightOp) {
        if (leftOp instanceof Double && rightOp instanceof Double) return;
        throw new RuntimeError(operator, "Operands must both be a numbers");
    }

    /**
     * Turn expression response into user output
     */
    private String stringify(Object object) {
        if (object == null) return "nil";

        if (object instanceof Double) {
            String text = object.toString();

            // Hack float -> integer conversion
            if (text.endsWith(".0")) {
                text = text.substring(0, text.length() - 2);
            }
            return text;
        }

        return object.toString();
    }
}
