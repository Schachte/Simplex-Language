package core;

public abstract class Stmt {
    public interface Visitor<T> {
        T visitExpressionStmt(Expr stmt);

        T visitPrintStmt(Print stmt);
    }

    static class Expr extends Stmt {
        Expr(Expression expression) {
            this.expression = expression;
        }

        <T> T accept(Visitor<T> visitor) {
            return visitor.visitExpressionStmt(this);
        }

        final Expression expression;
    }

    static class Print extends Stmt {
        Print(Expression expression) {
            this.expression = expression;
        }

        <T> T accept(Visitor<T> visitor) {
            return visitor.visitPrintStmt(this);
        }

        final Expression expression;
    }

    abstract <T> T accept(Visitor<T> visitor);
}
