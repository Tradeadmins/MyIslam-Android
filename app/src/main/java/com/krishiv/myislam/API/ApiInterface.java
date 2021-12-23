package com.krishiv.myislam.API;

import com.google.gson.JsonObject;
import com.krishiv.myislam.dto.ChangePwdModel;
import com.krishiv.myislam.dto.ChapterModel;
import com.krishiv.myislam.dto.CustomDuaModel;
import com.krishiv.myislam.dto.EventModel;
import com.krishiv.myislam.dto.HadithCountModel;
import com.krishiv.myislam.dto.HadithItemModel;
import com.krishiv.myislam.dto.LoginModel;
import com.krishiv.myislam.dto.MakeDuasModel;
import com.krishiv.myislam.dto.RamdanResponseModel;
import com.krishiv.myislam.dto.RegisterModel;
import com.krishiv.myislam.dto.RequestPrayerModel;
import com.krishiv.myislam.dto.ResultComman;
import com.krishiv.myislam.dto.ResultLanguage;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface ApiInterface {

    @Headers({"Accept: application/json"})
    @POST("/api/account/Register")
    Call<ResultComman> Register(@Body RegisterModel userModel);

    @Headers({"Accept: application/json"})
    @POST("/api/account/ExternalRegister")
    Call<ResultComman> ExternalRegister(@Body RegisterModel userModel);

    @Headers({"Accept: application/json"})
    @POST("/api/account/ExternalLogin")
    Call<ResultComman> ExternalLogin(@Body RegisterModel userModel);

    @FormUrlEncoded
    @POST("/token")
    Call<LoginModel> Login(@Field("username") String username,
                           @Field("password") String password,
                           @Field("grant_type") String grant_type,
                           @Field("client_id") String client_id);

    @Headers({"Accept: application/json"})
    @POST("/api/account/ChangePassword")
    Call<ResultComman> ChangePassword(@Body ChangePwdModel password, @Header("Authorization") String authHeader);

    @POST("/api/account/ForgotPassword")
    Call<ResultComman> ForgotPassword(@Query("email") String email);

    @Headers({"Accept: application/json"})
    @GET("/api/dailyQuote/GetDailyQuoteByLang_Date")
    Call<ResultComman> GetDailyQuoteByLang_Date(@Query("languageCode") String languageCode, @Query("utcDateTime") String utcDateTime, @Header("Authorization") String authHeader);

    @Headers({"Accept: application/json"})
    @GET("/api/jumaQuote/GetJumaQuoteByLang_Date")
    Call<ResultComman> GetJumaQuoteByLang_Date(@Query("languageCode") String languageCode, @Query("utcDateTime") String utcDateTime, @Header("Authorization") String authHeader);

    @Headers({"Accept: application/json"})
    @GET("/api/account/CheckUnique")
    Call<ResultComman> CheckUnique(@Query("email") String email);


    /*prayerRequest*/

    @Headers({"Accept: application/json"})
    @POST("/api/prayerRequest/Add")
    Call<ResultComman> AddPrayerRequest(@Body RequestPrayerModel jsonObject, @Header("Authorization") String authHeader);

    @GET("/api/prayerRequest/GetAllPrayerRequest")
    Call<ResultComman> GetAllPrayerRequest(@Query("pageIndex") String pageIndex, @Query("pageSize") String pageSize, @Header("Authorization") String authHeader);

    @GET("/api/prayerRequest/GetAllPrayerRequestByUserId")
    Call<ResultComman> GetAllPrayerRequestByUserId(@Query("prayerRequestUserId") String prayerRequestUserId, @Header("Authorization") String authHeader);

    /*MakeDuaData */

    @Headers({"Accept: application/json"})
    @POST("/api/makeDua/Add")
    Call<ResultComman> makeDua(@Body MakeDuasModel makeDuasModel, @Header("Authorization") String authHeader);

    /*myEvent */

    @GET("/api/myEvent/GetAll")
    Call<ResultComman> GetAllEvent(@Query("pageIndex") String pageIndex, @Query("pageSize") String pageSize, @Header("Authorization") String authHeader);

    @GET("/api/myEvent/GetAllEventsByUserId")
    Call<ResultComman> GetAllEventsByUserId(@Query("pageIndex") String pageIndex, @Query("pageSize") String pageSize, @Query("userId") String userId, @Header("Authorization") String authHeader);

    @GET("/api/myEvent/GetAllEventsByLatLong")
    Call<ResultComman> GetAllEventsByLatLong(@Query("pageIndex") String pageIndex, @Query("pageSize") String pageSize,
                                             @Query("latitude") String latitude, @Query("longitude") String longitude,
                                             @Header("Authorization") String authHeader);

    @POST("/api/myEvent/Add")
    Call<ResultComman> AddEvent(@Body EventModel jsonObject, @Header("Authorization") String authHeader);

    @PUT("/api/myEvent/Update")
    Call<ResultComman> UpdateEvent(@Body EventModel jsonObject, @Header("Authorization") String authHeader);

    @DELETE("/api/myEvent/DeleteById")
    Call<ResultComman> DeleteEvent(@Query("myEventId") String myEventId, @Header("Authorization") String authHeader);


    /*hadith*/

    @GET("/api/hadith/GetAllHadith")
    Call<ResultComman> GetAllHadith(@Header("Authorization") String authHeader);

    /*Instruction*/

    @GET("/api/instruction/GetAllInstruction")
    Call<ResultComman> GetAllInstruction(@Query("pageIndex") String pageIndex, @Query("pageSize") String pageSize, @Header("Authorization") String authHeader);


    /*Duas*/

    @GET("/api/duaCategory/GetAllDuaCategory")
    Call<ResultComman> GetAllDuaCategory(@Header("Authorization") String authHeader);

    @GET("/api/dua/GetAllDua")
    Call<ResultComman> GetAllDua(@Query("pageIndex") String pageIndex, @Query("pageSize") String pageSize, @Header("Authorization") String authHeader);

    @GET("/api/dua/GetAllDuaByCategoryId")
    Call<ResultComman> GetAllDuaByCategoryId(@Query("duaCategoryId") String duaCategoryId, @Header("Authorization") String authHeader);


    /*Custom duas*/
    @GET("/api/customDua/GetAllCustomDuaByUserId")
    Call<ResultComman> GetAllCustomDuaByUserId(@Query("userId") String userId, @Header("Authorization") String authHeader);

    @POST("/api/customDua/Add")
    Call<ResultComman> AddCustomDua(@Body CustomDuaModel jsonObject, @Header("Authorization") String authHeader);

    @PUT("/api/customDua/Update")
    Call<ResultComman> UpdateCustomDua(@Body CustomDuaModel jsonObject, @Header("Authorization") String authHeader);

    @DELETE("/api/customDua/DeleteById")
    Call<ResultComman> DeleteByIdCustomDua(@Query("customDuaId") String customDuaId, @Header("Authorization") String authHeader);


    /*Hajj*/
    @GET("/api/hajjTask/GetAllHajjTask")
    Call<ResultComman> GetAllHajjTask(@Query("pageIndex") String pageIndex, @Query("pageSize") String pageSize, @Header("Authorization") String authHeader);
    @POST("/api/hajjTaskComplete/Add")
    Call<ResultComman> AddHajjTaskComplete(@Body JsonObject jsonObject, @Header("Authorization") String authHeader);
    @DELETE("/api/hajjTaskComplete/DeleteById")
    Call<ResultComman> DeleteByIdHajjTaskComplete(@Query("hajjTaskCompleteId") String hajjTaskId, @Header("Authorization") String authHeader);

    /*Hajj Guide*/
    @GET("/api/hajjGuide/GetAllHajjGuide")
    Call<ResultComman> GetAllHajjGuide(@Query("pageIndex") String pageIndex, @Query("pageSize") String pageSize, @Header("Authorization") String authHeader);
    @POST("/api/hajjGuideComplete/Add")
    Call<ResultComman> AddHajjGuideTaskComplete(@Body JsonObject jsonObject, @Header("Authorization") String authHeader);
    @DELETE("/api/hajjGuideComplete/DeleteById")
    Call<ResultComman> DeleteByIdHajjGuideComplete(@Query("hajjGuideCompleteId") String hajjGuideId, @Header("Authorization") String authHeader);

    /*Umrah*/
    @GET("/api/umrahTask/GetAllUmrahTask")
    Call<ResultComman> GetAllUmrahTask(@Query("pageIndex") String pageIndex, @Query("pageSize") String pageSize, @Header("Authorization") String authHeader);
    @POST("/api/umrahTaskComplete/Add")
    Call<ResultComman> AddUmrahTaskComplete(@Body JsonObject jsonObject, @Header("Authorization") String authHeader);
    @DELETE("/api/umrahTaskComplete/DeleteById")
    Call<ResultComman> DeleteByIdUmrahTaskComplete(@Query("umrahTaskCompleteId") String umrahTaskId, @Header("Authorization") String authHeader);


    /*Umrah Guide*/
    @GET("/api/umrahGuide/GetAllUmrahGuide")
    Call<ResultComman> GetAllUmrahGuide(@Query("pageIndex") String pageIndex, @Query("pageSize") String pageSize, @Header("Authorization") String authHeader);
    @POST("/api/umrahGuideComplete/Add")
    Call<ResultComman> AddUmrahGuideTaskComplete(@Body JsonObject jsonObject, @Header("Authorization") String authHeader);
    @DELETE("/api/umrahGuideComplete/DeleteById")
    Call<ResultComman> DeleteByIdUmrahGuideComplete(@Query("umrahGuideCompleteId") String umrahGuideId, @Header("Authorization") String authHeader);


    /*inAppPurchase*/
    @POST("/api/inAppPurchase/Add")
    Call<ResultComman> AddInAppPurchase(@Body JsonObject jsonObject, @Header("Authorization") String authHeader);

    /*subscription*/
    @Headers({"Content-Type:application/json"})
    @POST("/api/subscription/Add")
    Call<ResultComman> AddSubscription(@Body JsonObject jsonObject, @Header("Authorization") String authHeader);

    //For the month record of prayer timing
//    http://api.aladhan.com/v1/calendar?latitude=22.719568&longitude=75.857727&method=1&month=2&year=2019

    @GET("/v1/calendar")
    Call<ResultComman> GetPrayerTiming(@Query("latitude") String latitude, @Query("longitude") String longitude, @Query("method") String method, @Query("month") String month, @Query("year") String year);

    /*Google Api*/
    //https://maps.googleapis.com/maps/api/place/textsearch/json?query=Mosque&sensor=true&location=22.7196,75.8577&radius=20&key=AIzaSyAiLWZ4ofCimK3dzlZRTQ3oce5d7Pmkx-Q
    @GET("/maps/api/place/textsearch/json?sensor=true&radius=20&key=AIzaSyDltnKoUiB0MlVrTGOLYpwfHIuDomEpo_k")
    Call<JsonObject> GetSearchGoogle(@Query("query") String query, @Query("location") String location);

    @GET("/svc/hadith")
    Call<List<ChapterModel>> getChapterByBookId(@Query("c") int bookid);
    @GET("/svc/hadith")
    Call<List<HadithCountModel>> gethadithbyChapterid(@Query("c") int bookid, @Query("b") int chapterid);
    @GET("/svc/hadith")
    Call<HadithItemModel> getHadith(@Query("c") int bookid, @Query("b") int chapterid, @Query("h") int hadithid);

    // quran translation

    @GET("/api/quranTranslate/GetAllQuranTranslates")
    Call<ResultLanguage> getAllQuranTranslation();

    @GET("/v1/calendar")
    Call<RamdanResponseModel> getRamdanData(@Query("latitude") double lat,@Query("longitude") double lng,@Query("method") int method,@Query("month") int month,@Query("year") int year);

}
