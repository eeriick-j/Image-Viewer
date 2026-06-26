package presenter;

import java.util.ArrayList;
import java.util.List;

public class CircularIterator<T> {
    private final List<T> items = new ArrayList<>();
    private int index = 0;

    public void add(T item) {
        items.add(item);
        index = items.size() - 1;
    }

    public T current() {
        if (items.isEmpty()) return null;
        return items.get(index);
    }

    public T next() {
        if (items.isEmpty()) return null;
        index = (index + 1) % items.size();
        return current();
    }

    public T prev() {
        if (items.isEmpty()) return null;
        index = (index - 1 + items.size()) % items.size();
        return current();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public void addAll(List<T> list) {
        items.addAll(list);
    }
}
