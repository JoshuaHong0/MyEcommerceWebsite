package storage.dao;

import storage.domain.OrderLineItem;

import java.util.List;

public interface OrderLineItemDao {

    OrderLineItem findByPk(long pk);

    List<OrderLineItem> findAll();

    void create(OrderLineItem lineItem);

    void modify(OrderLineItem lineItem);

    void remove(long pk);
}
