package storage.service.imp;

import storage.dao.GoodsDao;
import storage.dao.OrderDao;
import storage.dao.OrderLineItemDao;
import storage.dao.imp.GoodsDaoImpJdbc;
import storage.dao.imp.OrderDaoImpJdbc;
import storage.dao.imp.OrderLineItemDaoImpJdbc;
import storage.domain.Goods;
import storage.domain.OrderLineItem;
import storage.domain.Orders;
import storage.service.OrderService;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class OrderServiceImp implements OrderService {

    GoodsDao goodsDao = new GoodsDaoImpJdbc();
    OrderDao ordersDao = new OrderDaoImpJdbc();
    OrderLineItemDao lineItemDao = new OrderLineItemDaoImpJdbc();

    @Override
    public String submitOrders(List<Map<String, Object>> cart) {

        Orders orders = new Orders();
        Date date = new Date();

        // Generate an unique order id
        String ordersid = date.getTime() +
                String.valueOf((int) (Math.random() * 100));

        // Set order info
        orders.setId(ordersid);
        orders.setOrderDate(date);
        orders.setStatus(1); // Status code 1: waiting for payment
        orders.setTotal(0.0f); // Not yet calculated

        // Create order in database
        ordersDao.create(orders);

        // Total price
        double total = 0.0;

        for (Map item : cart) {
            Long goodsid = (Long) item.get("goodsid");
            Integer quantity = (Integer) item.get("quantity");
            Goods goods = goodsDao.findByPk(goodsid);
            double subTotal = quantity * goods.getPrice();
            total += subTotal;

            OrderLineItem lineItem = new OrderLineItem();
            lineItem.setQuantity(quantity);
            lineItem.setGoods(goods);
            lineItem.setOrders(orders);
            lineItem.setSubTotal(subTotal);

            lineItemDao.create(lineItem);
        }

        orders.setTotal(total);

        // Update database
        ordersDao.modify(orders);

        return ordersid;
    }

}
