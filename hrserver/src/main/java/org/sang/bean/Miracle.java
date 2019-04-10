package org.sang.bean;

import java.io.Serializable;

/**
 * Created by Rick on 2019/4/8.
 *
 * @ Description：spring自定义标签映射实体
 */
public class Miracle implements Serializable{

    private String id;

    private String name;

    private String email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
