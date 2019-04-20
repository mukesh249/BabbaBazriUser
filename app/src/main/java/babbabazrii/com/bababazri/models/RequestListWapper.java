package babbabazrii.com.bababazri.models;

import java.io.Serializable;

public class RequestListWapper implements Serializable{
    private String orderQuantity,unit,id,materialId,materialTypeId,vehicleId,customerId,orderDate,deliveryDate,driverId,ownerId,deliveryDistance,
            vehicleTypeId,deliveryStatus,bookingStatus,price,p_formatedAddress,p_lat,p_lng,c_formatedAddress,c_lat,c_lng,material_name,
            materialType_name,driver_profile;
    private String src_location,latlng;
    private String truckNumber,vehicleTpye_name,vehicleTpye_image,driver_fullname,driver_mobile,rating,total_user_rating,unitPrice;

    private String price_get,loading_get,unloading_get,loadKM_get,unloadKM_get,driverSalary_get,driverDailyExp_get,helper_get,royalty_get,tollTax,_get,driverSaving_get,nightStay_get,minCharge_get,totalPrice_get,tyres_get,days_get,totalKM_get;
    private Boolean isRatingDone;

    public String getVehicleTpye_image() {
        return vehicleTpye_image;
    }

    public void setVehicleTpye_image(String vehicleTpye_image) {
        this.vehicleTpye_image = vehicleTpye_image;
    }

    public String getPrice_get() {
        return price_get;
    }

    public void setPrice_get(String price_get) {
        this.price_get = price_get;
    }

    public String getLoading_get() {
        return loading_get;
    }

    public void setLoading_get(String loading_get) {
        this.loading_get = loading_get;
    }

    public String getUnloading_get() {
        return unloading_get;
    }

    public void setUnloading_get(String unloading_get) {
        this.unloading_get = unloading_get;
    }

    public String getLoadKM_get() {
        return loadKM_get;
    }

    public void setLoadKM_get(String loadKM_get) {
        this.loadKM_get = loadKM_get;
    }

    public String getUnloadKM_get() {
        return unloadKM_get;
    }

    public void setUnloadKM_get(String unloadKM_get) {
        this.unloadKM_get = unloadKM_get;
    }

    public String getDriverSalary_get() {
        return driverSalary_get;
    }

    public void setDriverSalary_get(String driverSalary_get) {
        this.driverSalary_get = driverSalary_get;
    }

    public String getDriverDailyExp_get() {
        return driverDailyExp_get;
    }

    public void setDriverDailyExp_get(String driverDailyExp_get) {
        this.driverDailyExp_get = driverDailyExp_get;
    }

    public String getHelper_get() {
        return helper_get;
    }

    public void setHelper_get(String helper_get) {
        this.helper_get = helper_get;
    }

    public String getRoyalty_get() {
        return royalty_get;
    }

    public void setRoyalty_get(String royalty_get) {
        this.royalty_get = royalty_get;
    }

    public String getTollTax() {
        return tollTax;
    }

    public void setTollTax(String tollTax) {
        this.tollTax = tollTax;
    }

    public String get_get() {
        return _get;
    }

    public void set_get(String _get) {
        this._get = _get;
    }

    public String getDriverSaving_get() {
        return driverSaving_get;
    }

    public void setDriverSaving_get(String driverSaving_get) {
        this.driverSaving_get = driverSaving_get;
    }

    public String getNightStay_get() {
        return nightStay_get;
    }

    public void setNightStay_get(String nightStay_get) {
        this.nightStay_get = nightStay_get;
    }

    public String getMinCharge_get() {
        return minCharge_get;
    }

    public void setMinCharge_get(String minCharge_get) {
        this.minCharge_get = minCharge_get;
    }

    public String getTotalPrice_get() {
        return totalPrice_get;
    }

    public void setTotalPrice_get(String totalPrice_get) {
        this.totalPrice_get = totalPrice_get;
    }

    public String getTyres_get() {
        return tyres_get;
    }

    public void setTyres_get(String tyres_get) {
        this.tyres_get = tyres_get;
    }

    public String getDays_get() {
        return days_get;
    }

    public void setDays_get(String days_get) {
        this.days_get = days_get;
    }

    public String getTotalKM_get() {
        return totalKM_get;
    }

    public void setTotalKM_get(String totalKM_get) {
        this.totalKM_get = totalKM_get;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getDriver_profile() {
        return driver_profile;
    }

    public void setDriver_profile(String driver_profile) {
        this.driver_profile = driver_profile;
    }

    public Boolean getRatingDone() {
        return isRatingDone;
    }

    public void setRatingDone(Boolean isRatingDone) {
        this.isRatingDone = isRatingDone;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getTotal_user_rating() {
        return total_user_rating;
    }

    public void setTotal_user_rating(String total_user_rating) {
        this.total_user_rating = total_user_rating;
    }

    public String getDriver_fullname() {
        return driver_fullname;
    }

    public void setDriver_fullname(String driver_fullname) {
        this.driver_fullname = driver_fullname;
    }

    public String getDriver_mobile() {
        return driver_mobile;
    }

    public void setDriver_mobile(String driver_mobile) {
        this.driver_mobile = driver_mobile;
    }

    public String getTruckNumber() {
        return truckNumber;
    }

    public void setTruckNumber(String truckNumber) {
        this.truckNumber = truckNumber;
    }

    public String getVehicleTpye_name() {
        return vehicleTpye_name;
    }

    public void setVehicleTpye_name(String vehicleTpye_name) {
        this.vehicleTpye_name = vehicleTpye_name;
    }

    public String getSrc_location() {
        return src_location;
    }

    public void setSrc_location(String src_location) {
        this.src_location = src_location;
    }

    public String getLatlng() {
        return latlng;
    }

    public void setLatlng(String latlng) {
        this.latlng = latlng;
    }

    public String getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(String orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getMaterialTypeId() {
        return materialTypeId;
    }

    public void setMaterialTypeId(String materialTypeId) {
        this.materialTypeId = materialTypeId;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getDeliveryDistance() {
        return deliveryDistance;
    }

    public void setDeliveryDistance(String deliveryDistance) {
        this.deliveryDistance = deliveryDistance;
    }

    public String getVehicleTypeId() {
        return vehicleTypeId;
    }

    public void setVehicleTypeId(String vehicleTypeId) {
        this.vehicleTypeId = vehicleTypeId;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getP_formatedAddress() {
        return p_formatedAddress;
    }

    public void setP_formatedAddress(String p_formatedAddress) {
        this.p_formatedAddress = p_formatedAddress;
    }

    public String getP_lat() {
        return p_lat;
    }

    public void setP_lat(String p_lat) {
        this.p_lat = p_lat;
    }

    public String getP_lng() {
        return p_lng;
    }

    public void setP_lng(String p_lng) {
        this.p_lng = p_lng;
    }

    public String getC_formatedAddress() {
        return c_formatedAddress;
    }

    public void setC_formatedAddress(String c_formatedAddress) {
        this.c_formatedAddress = c_formatedAddress;
    }

    public String getC_lat() {
        return c_lat;
    }

    public void setC_lat(String c_lat) {
        this.c_lat = c_lat;
    }

    public String getC_lng() {
        return c_lng;
    }

    public void setC_lng(String c_lng) {
        this.c_lng = c_lng;
    }

    public String getMaterial_name() {
        return material_name;
    }

    public void setMaterial_name(String material_name) {
        this.material_name = material_name;
    }

    public String getMaterialType_name() {
        return materialType_name;
    }

    public void setMaterialType_name(String materialType_name) {
        this.materialType_name = materialType_name;
    }
}
