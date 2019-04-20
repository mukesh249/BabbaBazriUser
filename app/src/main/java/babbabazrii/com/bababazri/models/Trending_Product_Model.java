package babbabazrii.com.bababazri.models;

import java.io.Serializable;

public class Trending_Product_Model implements Serializable{

    private String product_name,product_cat_name;
    private String product_createdAt;
    private String product_updatedAt;
    private String product_id,product_image;

    public String getProduct_cat_name() {
        return product_cat_name;
    }

    public void setProduct_cat_name(String product_cat_name) {
        this.product_cat_name = product_cat_name;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_createdAt() {
        return product_createdAt;
    }

    public void setProduct_createdAt(String product_createdAt) {
        this.product_createdAt = product_createdAt;
    }

    public String getProduct_updatedAt() {
        return product_updatedAt;
    }

    public void setProduct_updatedAt(String product_updatedAt) {
        this.product_updatedAt = product_updatedAt;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }
}
