package klippe.dev.azatcp;

/**
 * Created by Developer on 29.01.2018.
 */

public class Event {

    String title;
    String description;
    int image;
    boolean checked = false;

    public Event(String title, String description, int image) {
        this.title = title;
        this.description = description;
        this.image = image;
    }
}
