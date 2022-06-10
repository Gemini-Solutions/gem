package com.gemini.gembook.model;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

public class ProfileLinksId implements Serializable {

    @NotNull
    public int employeeId;

    @NotNull
    public String linkName;

    public ProfileLinksId() {
    }

    public ProfileLinksId(@NotNull int employeeId, @NotNull String linkName) {
        this.employeeId = employeeId;
        this.linkName = linkName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProfileLinksId)) return false;
        ProfileLinksId that = (ProfileLinksId) o;
        return employeeId == that.employeeId &&
                linkName.equals(that.linkName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employeeId, linkName);
    }
}