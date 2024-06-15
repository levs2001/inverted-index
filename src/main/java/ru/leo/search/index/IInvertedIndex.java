package ru.leo.search.index;

import java.util.List;
import ru.leo.search.expression.Expression;

public interface IInvertedIndex {
    List<Long> find(Expression expression);
}
