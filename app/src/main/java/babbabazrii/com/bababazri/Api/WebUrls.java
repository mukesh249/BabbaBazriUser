package babbabazrii.com.bababazri.Api;


/**
 * Created by Advosoft2 on 6/28/2017.
 */
public class WebUrls {
    public static final String BASE_URL = "https://api.babbabazri.com";
//    public static final String BASE_URL = "http://139.59.71.150:3010";
    public final static String login_api = "/api/People/login";
    public final static String signup_api = "/api/People/signup";
    public final static String resetPassRequest = "/api/People/resetPassRequest";
    public final static String new_resetPassword = "/api/People/resetPassword";
    public final static String update_profile_api = "/api/People/updateProfile";
    public final static String checkResetOtp = "/api/Otps/checkResetOtp";
    public static final String verifiy_resend_api = "/api/Otps/resendOtp";
    public static final String trendingProduct_api = "/api/Products/trendingProduct";
    public static final String afterSearchProduct_api = "/api/Products/getProduct?";
    public static final String sub_product ="/api/Products/getProduct?";
    public static final String afterBtnplaceOrder_api = "/api/OrderRequests/placeOrder";
    public static final String getMaterials_Name_Id="/api/Materials/getMaterials";
    public static final String getVehicles_Name_Id="/api/VehicleTypes/getTypes";
    public static final String nearByDrivers_api="/api/People/nearByDrivers?";
    public static final String orderlist_api="/api/OrderRequests/customerOrderList";
    public static final String bookNow_Api="/api/OrderRequests/bookNow";
    public static final String verifiyMobileNo_Api="/api/Otps/verifyMobile";
    public static final String logout_Api="/api/People/logout";
    public static final String Notification_Api="/api/Notifications/getNotificationList";
    public static final String GetBadgeCount_Api="/api/Notifications/getBadgeCount";
    public static final String NotificationRead_Api="/api/Notifications/readAllNotifications";
    public static final String Rating_Api="/api/Ratings/giveRating";
    public static final String UserProfile_Api="/api/People/userById";
    public static final String Update_UserName_Api = "/api/People/updateName";
    public static final String Update_UserEmail_Api = "/api/People/updateEmail";
    public static final String ChangsePassword_Api = "/api/People/change-password";
    public static final String GetByUnitQty_Api = "/api/VehicleTypes/getByUnitQty?";
    public static final String GetPricing_Api = "/api/OrderRequests/getPricingV2?";
    public static final String GetCurrentOder_Api = "/api/OrderRequests/getCustCurrentJob";
    public static final String ChangeLanguage_Api ="/api/People/changeLanguage";
    public static final String getSubTypesObj ="/api/SubTypes/getSubTypesObj";
    //Google api for Tracking
    public static final String baseURL = "https://maps.googleapis.com";

    public static ApiConfig getGoogleAPI(){
        return RetrofitClient.getRetrofitClient(baseURL).create(ApiConfig.class);
    }

}
