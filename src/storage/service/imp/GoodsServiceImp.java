package storage.service.imp;

import storage.dao.GoodsDao;
import storage.dao.imp.GoodsDaoImpJdbc;
import storage.domain.Goods;
import storage.service.GoodsService;

import java.util.List;

public class GoodsServiceImp implements GoodsService {

    GoodsDao goodsDao = new GoodsDaoImpJdbc();

    @Override
    public List<Goods> queryAll() {
        return goodsDao.findAll();
    }

    @Override
    public List<Goods> queryByStartEnd(int start, int end) {
        return goodsDao.findStartEnd(start, end);
    }

    @Override
    public Goods queryDetail(long goodsid) {
        return goodsDao.findByPk(goodsid);
    }

}
