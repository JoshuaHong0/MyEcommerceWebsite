package storage.dao.imp;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import storage.dao.GoodsDao;
import storage.domain.Goods;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GoodsDaoImpJdbcTest {

    GoodsDao dao;

    @BeforeEach
    void setUp() {
        dao = new GoodsDaoImpJdbc();
    }

    @AfterEach
    void tearDown() {
        dao = null;
    }

    @Test
    void findByPk() {
        Goods goods = dao.findByPk(1);
        assertNotNull(goods);
        assertEquals("戴尔(DELL)成就3470高性能商用办公台式电脑整机",goods.getName());
    }

    @Test
    void findAll() {

        List<Goods> list = dao.findAll();
        assertEquals(34, list.size());

        Goods goods = list.get(0);
        assertNotNull(goods);
        assertEquals("戴尔(DELL)成就3470高性能商用办公台式电脑整机",goods.getName());
    }

    @Test
    void findStartEnd() {
        List<Goods> list = dao.findStartEnd(0, 10);
        assertEquals(10, list.size());

        Goods goods = list.get(0);
        assertNotNull(goods);
        assertEquals("戴尔(DELL)成就3470高性能商用办公台式电脑整机",goods.getName());
    }

    @Test
    void create() {

        Goods goods = new Goods();
        goods.setId(9999);
        goods.setName("Apple Mac Mini 2018");
        goods.setBrand("Apple");
        goods.setCpuBrand("Intel");
        goods.setCpuType("i5");
        goods.setMemoryCapacity("8G");
        goods.setHdCapacity("500G");
        goods.setCardModel("GTX 9/7");
        goods.setDisplaysize("NULL");
        goods.setImage("aaa.jpg");

        dao.create(goods);

        Goods result = dao.findByPk(9999);
        assertNotNull(result);
        assertEquals("i5",result.getCpuType());
    }

    @Test
    void modify() {

        Goods goods = new Goods();
        goods.setId(9999);
        goods.setName("Apple Mac Mini 2018");
        goods.setBrand("Apple");
        goods.setCpuBrand("Intel");
        goods.setCpuType("i5");
        goods.setMemoryCapacity("8G");
        goods.setHdCapacity("500G");
        goods.setCardModel("GTX 9/7");
        goods.setDisplaysize("NULL");
        goods.setImage("bbb.jpg");

        dao.modify(goods);

        Goods result = dao.findByPk(9999);
        assertNotNull(result);
        assertEquals("bbb.jpg",result.getImage());

    }

    @Test
    void remove() {

        dao.remove(9999);

        Goods result = dao.findByPk(9999);

        assertNull(result);

    }
}