package tacos.dataJDBC;

import tacos.Order;

public interface OrderRepository {

	Order save(Order order);

}
