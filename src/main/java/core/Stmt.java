package core;

import java.util.List;

public abstract class Stmt {
	public interface Visitor<T> {
	T visitBlockStmt(Block stmt);
	T visitExpressionStmt(Expr stmt);
	T visitPrintStmt(Print stmt);
	T visitVarStmt(Var stmt);
    }
 static class Block extends Stmt {
	Block(List<Stmt> statements) {
		this.statements = statements;
	}

	<T> T accept(Visitor<T> visitor) {
		 return visitor.visitBlockStmt(this);
	}

	final List<Stmt> statements;
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
 static class Var extends Stmt {
	Var(Token name, Expression initializer) {
		this.name = name;
		this.initializer = initializer;
	}

	<T> T accept(Visitor<T> visitor) {
		 return visitor.visitVarStmt(this);
	}

	final Token name;
	final Expression initializer;
  }

  abstract <T> T accept(Visitor<T> visitor);
}
