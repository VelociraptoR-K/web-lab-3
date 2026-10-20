package web.lab.backend.dto;

import java.time.LocalDate;

public interface DateRangeValidatable {

    public LocalDate getMoveInDateFrom();

    public LocalDate getMoveInDateTo();
}
