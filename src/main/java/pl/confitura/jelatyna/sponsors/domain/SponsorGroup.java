package pl.confitura.jelatyna.sponsors.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

public class SponsorGroup {
    private String name;
    private String label;
    private List<Sponsor> sponsors = new ArrayList<>();

    SponsorGroup() {
    }

    public SponsorGroup(String name) {
        this.name = name;
    }

    public SponsorGroup withLabel(String label) {
        this.label = label;
        return this;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
