package dao;

import java.util.List;

public interface BaseDao<T> {

    List<T> findAll();
    T findById(int id) throws Exception;
    T create(T t);
    T update(T t);
    boolean delete(int id);




}
