package ru.leo.search.expression;

public interface IExpressionVisitor {
    boolean visitAnd(ExpressionAnd expressionAnd);

    boolean visitOr(ExpressionOr expressionOr);

    boolean visitNot(ExpressionNot expressionNot);

    boolean visitTerm(ExpressionTerm expressionTerm);
}
