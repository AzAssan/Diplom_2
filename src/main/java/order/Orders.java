package order;

import java.util.Date;
import java.util.List;

public class Orders {
    private List<String> ingredients;
    private String _id;
    private String status;
    private int number;
    private Date createdAt;
    private Date updatedAt;

    public Orders() {
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public String get_id() {
        return _id;
    }

    public String getStatus() {
        return status;
    }

    public int getNumber() {
        return number;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }
}
