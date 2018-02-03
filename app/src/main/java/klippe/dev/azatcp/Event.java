package klippe.dev.azatcp;

import java.io.Serializable;

public class Event implements Serializable {

    String title;
    String descriptin;
    String img;
    String place;
    String date;
    String category;
    boolean checked;

    public Event() {
    }

    public Event(String title, String descriptin, String img, String place, String category, boolean checked) {
        this.title = title;
        this.descriptin = descriptin;
        this.img = img;
        this.place = place;
        this.category = category;
        this.checked = checked;
    }
}
