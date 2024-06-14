package ru.leo.search;

import java.util.List;
import java.util.Map;
import ru.leo.expression.Expression;

public interface ISearcher {
    void index(Map<Long, String> texts);
    List<Long> search(Expression expression);
}
