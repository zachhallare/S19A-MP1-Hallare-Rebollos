package aMCO2;

import java.time.LocalDate;

public class Journal extends Entry {
    public Journal(String title, LocalDate date, String description) {
        super(title, date, description);
    }

    @Override
    public String getType() {
        return "Journal"; 
    }

    @Override
    public String toDisplayString() {
        return "[Journal] " + title;
    }

    @Override
    public Journal copy() {
        return new Journal(title, date, description);
    }
}
