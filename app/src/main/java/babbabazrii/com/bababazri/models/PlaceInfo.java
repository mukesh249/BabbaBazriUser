package babbabazrii.com.bababazri.models;

import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;

public class PlaceInfo {

    private String name;
    private String address;
    private String id;
    private String phoneNumber;
    private Uri websiteuri;
    private LatLng latLng;
    private float rating;
    private String attributions;

    public PlaceInfo(String name, String address, String id, String phoneNumber, Uri websiteuri, LatLng latLng, float rating, String attributions) {
        this.name = name;
        this.address = address;
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.websiteuri = websiteuri;
        this.latLng = latLng;
        this.rating = rating;
        this.attributions = attributions;
    }
    public PlaceInfo() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Uri getWebsiteuri() {
        return websiteuri;
    }

    public void setWebsiteuri(Uri websiteuri) {
        this.websiteuri = websiteuri;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getAttributions() {
        return attributions;
    }

    public void setAttributions(String attributions) {
        this.attributions = attributions;
    }

    @Override
    public String toString() {
        return "PlaceInfo{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", id='" + id + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", websiteuri=" + websiteuri +
                ", latLng=" + latLng +
                ", rating=" + rating +
                ", attributions='" + attributions + '\'' +
                '}';
    }
}
