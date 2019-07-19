package storage.dao;

import storage.domain.Goods;

import java.util.List;

public interface GoodsDao {

    Goods findByPk(long pk);

    List<Goods> findAll();

    /**
     * Query when switch web pages
     *
     * @param start
     * @param end
     * @return Goods list
     */
    List<Goods> findStartEnd(int start, int end);

    void create(Goods goods);

    void modify(Goods goods);

    void remove(long pk);

}
