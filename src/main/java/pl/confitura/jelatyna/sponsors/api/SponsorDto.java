package pl.confitura.jelatyna.sponsors.api;

public class SponsorDto {
    private String name;
    private String description;
    private String sponsorGroupName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSponsorGroupName() {
        return sponsorGroupName;
    }

    public void setSponsorGroupName(String sponsorGroupName) {
        this.sponsorGroupName = sponsorGroupName;
    }
}
