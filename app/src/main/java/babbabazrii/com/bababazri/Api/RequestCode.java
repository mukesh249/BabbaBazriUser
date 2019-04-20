package babbabazrii.com.bababazri.Api;

/**
 * Created by suarebits on 3/12/15.
 */
public class RequestCode {
    public static final int CODE_Register = 1;
    public static final int CODE_Login = 2;
    public static final int CODE_Signup = 3;
    public static final int CODE_ResetPassRequest = 4;
    public static final int CODE_new_resetPassword = 5;
    public static final int LOCATION_PERMISSION_REQUEST_CODE = 6;
    public static final int ERROR_DIALOG_REQUEST = 7;
    public static final int CODE_checkResetOtp = 8;
    public static final int CODE_AfterSearch = 9;
    public static final int CODE_AfterBtn = 10;
    public static final int CODE_Sub_Product = 11;
    public static final int CODE_TrendingProduct=12;
    public static final int CODE_GET_NearbyDrivers=13;
    public static final int CODE_GET_NearbyDriversVehicle=13;
    public static final int CODE_Direction_Api=14;
    public static final int CODE_BookNow_Api = 15;
    public static final int CODE_MyOrderlist_Api = 16;
    public static final int CODE_VerifiyMobileNo_Api = 17;
    public static final int CODE_Resend_VerifiyMobileNo_Api=18;
    public static final int CODE_Logout_Api=19;
    public static final int CODE_Notification_Api=20;
    public static final int CODE_BadgeCount=21;
    public static final int CODE_ReadNotification=22;
    public static final int CODE_Rating=23;
    public static final int CODE_UserProfile=24;
    public static final int CODE_UpdateUserName=25;
    public static final int CODE_UpdateUserEmail=26;
    public static final int CODE_ChangePassword=27;
    public static final int CODE_Sub_ProductHide=28;
    public static final int CODE_AfterSearchHide = 29;
    public static final int CODE_fetchGetMaterials=30;
    public static final int CODE_fetchGetVehicles =31;
    public static final int CODE_updateHotCountn =32;
    public static final int CODE_GetByUnitQty =33;
    public static final int CODE_GetPriceing =34;
    public static final int CODE_GetCurrentOrder=35;
    public static final int CODE_ChangeLanguage=36;
    public static final int CODE_GetSubTypesObj=37;
    //Constants
    public static String SP_CURRENT_LAT = "lat";
    public static String SP_CURRENT_LONG = "lng";
    public static String SP_NEW_LAT = "newLat";
    public static String SP_NEW_LONG = "newLng";
    public static String SP_DriverStatus = "driverStatus";
    public static final String UserID = "userID";
    public static final String userType = "user_type";
    public static final String KEY_ANIM_TYPE="anim_type";
    public static final String KEY_TITLE="anim_title";

    public static final String LangId = "langid";


    public enum TransitionType{
        ExplodeJava,ExplodeXML,SlideJava,SlideXML,FadeJava,FadeXML;
    }

}
