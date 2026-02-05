package Repositories;

import java.util.List;

public interface IRepository<T> {
    void add(T entity);
    T getById(int id);
    List<T> getAll();
    void delete(int id);
}