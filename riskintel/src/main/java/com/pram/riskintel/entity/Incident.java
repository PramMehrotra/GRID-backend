package com.pram.riskintel.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "incident")
public class Incident {

    @Id
    private Long eventid;

    private Integer iyear;
    private Integer imonth;
    private String countryTxt;
    private String regionTxt;
    private String city;
    private Double latitude;
    private Double longitude;
    private Integer nkill;
    private Integer nwound;

    // Getters and Setters

    public Long getEventid() {
        return eventid;
    }

    public void setEventid(Long eventid) {
        this.eventid = eventid;
    }

    public Integer getIyear() {
        return iyear;
    }

    public void setIyear(Integer iyear) {
        this.iyear = iyear;
    }

    public Integer getImonth() {
        return imonth;
    }

    public void setImonth(Integer imonth) {
        this.imonth = imonth;
    }

    public String getCountryTxt() {
        return countryTxt;
    }

    public void setCountryTxt(String countryTxt) {
        this.countryTxt = countryTxt;
    }

    public String getRegionTxt() {
        return regionTxt;
    }

    public void setRegionTxt(String regionTxt) {
        this.regionTxt = regionTxt;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Integer getNkill() {
        return nkill;
    }

    public void setNkill(Integer nkill) {
        this.nkill = nkill;
    }

    public Integer getNwound() {
        return nwound;
    }

    public void setNwound(Integer nwound) {
        this.nwound = nwound;
    }
}
