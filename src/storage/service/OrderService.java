package storage.service;

import java.util.List;
import java.util.Map;

public interface OrderService {

    String submitOrders(List<Map<String, Object>> cart);

}
