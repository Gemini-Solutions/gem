package com.gemini.gembook.model;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

public class EducationId implements Serializable {

    @NotNull
    public int employee;

    @NotNull
    public String degree;

    public EducationId() {
    }

    public EducationId(@NotNull int employee, @NotNull String degree) {
        this.employee = employee;
        this.degree = degree;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EducationId)) return false;
        EducationId that = (EducationId) o;
        return employee == that.employee &&
                degree.equals(that.degree);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employee, degree);
    }
}
