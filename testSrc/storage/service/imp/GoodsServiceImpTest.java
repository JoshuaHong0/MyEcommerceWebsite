package storage.service.imp;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import storage.dao.GoodsDao;
import storage.domain.Goods;
import storage.service.GoodsService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GoodsServiceImpTest {

    GoodsService goodsService;

    @BeforeEach
    void setUp() {
        goodsService = new GoodsServiceImp();
    }

    @AfterEach
    void tearDown() {
        goodsService = null;
    }

    @Test
    void queryAll() {
        List<Goods> list = goodsService.queryAll();
        assertEquals(34, list.size());

        Goods goods = list.get(2);
        assertEquals( "联想天逸510S", goods.getName());
    }

    @Test
    void queryByStartEnd() {
        List<Goods> list = goodsService.queryByStartEnd(0,10);
        assertEquals(10, list.size());
        Goods goods = list.get(2);
        assertEquals( "联想天逸510S", goods.getName());
    }

    @Test
    void queryDetail() {
        Goods goods = goodsService.queryDetail(3L);
        assertNotNull(goods);
        assertEquals( "联想天逸510S", goods.getName());
    }
}