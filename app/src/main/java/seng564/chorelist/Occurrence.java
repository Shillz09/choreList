package seng564.chorelist;

import java.util.Date;

public class Occurrence {

    private Boolean type;
    private static final Boolean SINGLE = Boolean.FALSE;
    private static final Boolean RECURRING = Boolean.TRUE;

    private String frequency;
    private static final String DAILY = "DAILY";
    private static final String WEEKLY = "WEEKLY";
    private static final String MONTHLY = "MONTHLY";
    private static final String QUARTERLY = "QUARTERLY";
    private static final String SEMIANNUAL = "SEMIANNUAL";

    private int recurrence;

    //Dates include both date and time values
    private Date startDate;
    private Date endDate;

    public Occurrence(Boolean type) {
        if(type==SINGLE) {
            this.type = type;
        } else{
            throw new IllegalArgumentException();
        }
    }

    public Occurrence(Boolean type, String frequency, int recurrence, Date startDate, Date endDate) {
        if(type==RECURRING) {
            this.type = type;
        } else {
            throw new IllegalArgumentException();
        }
        this.frequency = frequency;
        this.recurrence = recurrence;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
