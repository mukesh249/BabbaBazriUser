package babbabazrii.com.bababazri.Api;


import com.google.gson.JsonObject;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

public interface ApiConfig {

//    @GET
//    Call<JSONObject>getPath(@Url String url);

//  /*  @Multipart
//    @POST("retrofit_example/upload_image.php")
//    Call<JsonObject> uploadFile(@Part MultipartBody.Part file, @Part("file") RequestBody name);
//    //String  token=PostPropertyFragment.accesstokrn;
//
//
//    @Multipart
//    @POST(WebUrls.UpdateProfile)
//    Call<JsonObject> PostEditprofile(@Part MultipartBody.Part profileImage,
//                                     @Part("data") RequestBody lookingFor,
//                                     @Query("access_token") String accessToken
//    );
//
//    @Multipart
//    @POST(WebUrls.Add_SendImage)
//    Call<JsonObject> PostChatImage(@Part MultipartBody.Part profileImage,
//                                   @Part("data") RequestBody lookingFor,
//                                   @Query("access_token") String accessToken
//    );
//
//    @Multipart
//    @POST(WebUrls.Add_Post)
//    Call<JsonObject> PostContract(@Part MultipartBody.Part postImage,
//                                  @Part("data") RequestBody lookingFor,
//                                  @Query("access_token") String accessToken
//    );
//
//    @Multipart
//    @POST(WebUrls.UpdateProfile)
//    Call<JsonObject> Updatepost(@Part MultipartBody.Part postImage,
//                                @Part("request") RequestBody lookingFor
//
//    );
//
//    @Multipart
//    @POST(WebUrls.UpdateProfile)
//    Call<JsonObject> addImage(@Part MultipartBody.Part files,
//                              @Part("request") RequestBody lookingFor
//
//    );*/

    @Multipart
    @POST(WebUrls.update_profile_api)
    Call<JsonObject> submitData(@PartMap Map<String, RequestBody> files,
                                @Part("data") RequestBody lookingFor,
                                @Query("access_token") String access_token
    );
   /* @Multipart
    @POST(WebUrls.UpdateProfile)
    Call<JsonObject> uploadProfile(@Part MultipartBody.Part files,
                                   @Part("request") RequestBody lookingFor
    );

    @Multipart
    @POST(WebUrls.UpdateProfile)
    Call<JsonObject> addProductNew(@PartMap Map<String, RequestBody> files,
                                   @Part("request") RequestBody lookingFor
    );



    @Multipart
    @POST(WebUrls.UpdateProfile)
    Call<JsonObject> Updatepostdata(@Part("request") RequestBody lookingFor

    );*/


}
