package CCStatistics.Logic;

import java.util.ArrayList;

public interface Logic<T> {
    ArrayList<T> getAll();

    void create(T object);

    void update(T object, String string);

    void delete(T object);
}
