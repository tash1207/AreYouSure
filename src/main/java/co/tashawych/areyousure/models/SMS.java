package co.tashawych.areyousure.models;

import java.util.Date;

/**
 * Model for an SMS message.
 */
public class SMS {
    public String from;
    public String to;
    public String message;
    public Date date;

    public SMS(String from, String to, String message, Date date) {
        this.from = from;
        this.to = to;
        this.message = message;
        this.date = date;
    }
}
