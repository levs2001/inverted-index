package ru.leo.index;

import java.util.List;
import ru.leo.doc.Document;
import ru.leo.expression.Expression;

public interface IInvertedIndex {
    List<Long> find(Expression expression);
}
