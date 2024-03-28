package com.huflit.doanmobile.classs;
import java.util.ArrayList;

public class Category  implements Component {
    private int cateId;
    private String catename;
    private ArrayList<Component> children;

    public Category(int cateId, String catename) {
        this.cateId = cateId;
        this.catename = catename;
    }

    public int getCateId() {
        return cateId;
    }

    public void setCateId(int cateId) {
        this.cateId = cateId;
    }

    public String getCatename() {
        return catename;
    }

    public void setCatename(String catename) {
        this.catename = catename;
    }

    public void add(Component component) {
        children.add(component);
    }

    public void remove(Component component) {
        children.remove(component);
    }
    @Override
    public void displayInfo() {

        for (Component component : children) {
            component.displayInfo();
        }
    }
}
