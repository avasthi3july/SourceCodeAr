package com.augmentedrealityapp.retrofit;

import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by prasharma on 3/24/2017.
 */

public interface RetroService {
    int GET_LOGIN = 10001;
    int GET_SIGNUP = 10002;
    int OTP_VERIFY = 10003;
    int GET_CAT_LIST = 10004;
    int GET_PRO_LIST = 10005;
    int GET_PROFILE = 10005;
    int EDIT_PROFILE = 10006;
    int ADD_TO_CART = 10007;
    int RESEND_OTP = 10008;
    int GET_CART_DATA = 10009;
    int REMOVE_ITEM = 10010;
    int GET_CHECKOUT = 10011;
    int CHECK_STATUS = 10012;
    int GET_OTP = 10013;
    int PASSWORD_UPDATE = 10014;

    @POST("user_login")
    Observable<ResponseBody> getStarredRepositories(@Body HashMap<String, String> mLoginObject);

    @POST("user_login")
    Call<ResponseBody> doLogin(
            @Body HashMap<String, String> mLoginObject);

    @POST("user_signup")
    Call<ResponseBody> signUp(
            @Body HashMap<String, String> mSignUp);

    @POST("getCheckout")
    Call<ResponseBody> checkOut(
            @Body HashMap<String, String> mSignUp);
    @POST("paymentreceipt")
    Call<ResponseBody> paymentReceipt(
            @Body HashMap<String, String> mSignUp);

    @POST("editUserProfile")
    Call<ResponseBody> editProfile(
            @Body HashMap<String, String> mSignUp);

    @POST("password")
    Call<ResponseBody> changePassword(
            @Body HashMap<String, String> mSignUp);

    @GET("getCategories")
    Call<ResponseBody> getCategoryList();

    @POST("getProducts")
    @FormUrlEncoded
    Call<ResponseBody> productList(
            @Field("category_id") String title);

    @POST("forget_password")
    @FormUrlEncoded
    Call<ResponseBody> forgotPassword(
            @Field("email") String title);

    @POST("getProductsDetail")
    @FormUrlEncoded
    Call<ResponseBody> proDetail(
            @Field("product_id") String title);

    @POST("regenerate_otp")
    @FormUrlEncoded
    Call<ResponseBody> regenerateOtp(
            @Field("email") String title);

    @POST("getCartList")
    @FormUrlEncoded
    Call<ResponseBody> getCartData(
            @Field("user_id") String title);

    @POST("getUserProfile")
    @FormUrlEncoded
    Call<ResponseBody> getUserProfile(
            @Field("user_id") String title);

    @POST("otp_verification")
    Call<ResponseBody> verifyOtp(
            @Body HashMap<String, String> mSignUp);

    @POST("addToCart")
    Call<ResponseBody> addToCart(
            @Body HashMap<String, String> mSignUp);
    @POST("removecart")
    Call<ResponseBody> removeItem(
            @Body HashMap<String, String> mSignUp);



    @GET("register_device_id/{device_id}/format")
    Call<ResponseBody> getRegisteredDeviceId(@Path("device_id") String ID,
                                             @Query("format") String responseType);


    @GET("tags_list/format")
    Call<ResponseBody> getTagList(@Query("format") String responseType);

    @GET("item_list/{item_id}/{device_id}/{category_id}/{identifier}/format")
    Call<ResponseBody> getAllOpenerItems(@Path("item_id") String item_id,
                                         @Path("device_id") String device_id,
                                         @Path("category_id") String category_id,
                                         @Path("identifier") String identifier,
                                         @Query("format") String responseType);

    @GET("item_data/{favorite}/{device_id}/format")
    Call<ResponseBody> getFavItems(@Path("favorite") String favorite,
                                   @Path("device_id") String device_id,
                                   @Query("format") String responseType);


    @GET("item_search/{searchedText}")
    Call<ResponseBody> getSearchedItems(@Path("searchedText") String searchedText,
                                        @Query("format") String responseType);


    @GET("item_copied/{copiedItemId}/{device_id}/format")
    Call<ResponseBody> getCopiedItem(@Path("copiedItemId") String copiedItemId,
                                     @Path("device_id") String device_id,
                                     @Query("format") String responseType);

    @GET("item_favorite/{favoriteItemId}/{device_id}/format")
    Call<ResponseBody> setFavoriteItem(@Path("favoriteItemId") String favoriteItemId,
                                       @Path("device_id") String device_id,
                                       @Query("format") String responseType);

    @GET("item_search/{selectedTag}/{selectedTagCopy}/format")
    Call<ResponseBody> getTagDetails(@Path("selectedTag") String selectedTag,
                                     @Path("selectedTagCopy") String selectedTagCopy,
                                     @Query("format") String responseType);

    @GET("conversation_list/{device_id}/format")
    Call<ResponseBody> getConversationList(@Path("device_id") String device_id,
                                           @Query("format") String responseType);

    @GET("dialogmessage/format")
    Call<ResponseBody> getDialogMsg(@Query("format") String responseType);
}
