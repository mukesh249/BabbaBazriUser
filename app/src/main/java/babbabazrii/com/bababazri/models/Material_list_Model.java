package babbabazrii.com.bababazri.models;

import java.io.Serializable;

public class Material_list_Model implements Serializable {

    String material_name,matrial_id;

    public String getMaterial_name() {
        return material_name;
    }

    public void setMaterial_name(String material_name) {
        this.material_name = material_name;
    }

    public String getMatrial_id() {
        return matrial_id;
    }

    public void setMatrial_id(String matrial_id) {
        this.matrial_id = matrial_id;
    }
}
