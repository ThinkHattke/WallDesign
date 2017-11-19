package com.gaurav.walldesign;

import java.util.ArrayList;

/**
 * Created by raiga on 11/19/2017.
 */

class CustomPojo {

    //POJO class consists of get method and set method
    public String id;
    public ArrayList<CustomPojo> customPojo =new ArrayList<>();

    public CustomPojo() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
