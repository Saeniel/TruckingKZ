package klippe.dev.truckkz;

import java.io.Serializable;

public class Cargo implements Serializable {

    String img;
    String from;
    String to;
    String when;
    String price;
    String machineType;
    String comment;
    boolean isChecked;
    int priceTemp;

    public Cargo() {
    }

    public Cargo(String img, String from, String to, String when,
                 String price, String machineType, String comment, boolean isChecked) {
        this.img = img;
        this.from = from;
        this.to = to;
        this.when = when;
        this.price = price;
        this.machineType = machineType;
        this.comment = comment;
        this.isChecked = isChecked;
        this.priceTemp = 0;
    }
}
