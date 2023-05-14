package order;

public class OrderResponse {
    private String name;
    private OrderNumber order;
    private boolean success;

    public OrderResponse() {

    }

    public String getName() {
        return name;
    }

    public OrderNumber getOrder() {
        return order;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOrder(OrderNumber order) {
        this.order = order;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}

