package com.skripsi.sistemrumah.api.rest;

import com.skripsi.sistemrumah.BuildConfig;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.skripsi.sistemrumah.api.ApiListUser;
import com.skripsi.sistemrumah.api.ApiMonitoring;
import com.skripsi.sistemrumah.api.LoginResponse;
import com.skripsi.sistemrumah.api.MultiResponse;
import com.skripsi.sistemrumah.storage.Common;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.ConnectionSpec;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;

public class REST_Controller {
    public static AlisanAPI CLIENT;
    public static AlisanAPI CLIENT_LONG;
    public static Gson GSON = null;
    public static Retrofit retrofit;
    public static Retrofit retrofit_long_time_out;

    public static void setUp() {
        retrofit = new Retrofit.Builder()
                .baseUrl(Common.BASE_URL)
                .addConverterFactory(getConverterFactoryGson())
                .client(getClient()).build();

        retrofit_long_time_out = new Retrofit.Builder()
                .baseUrl(Common.BASE_URL)
                .addConverterFactory(getConverterFactoryGson())
                .client(new_client()).build();

        CLIENT      = retrofit.create(AlisanAPI.class);
        CLIENT_LONG = retrofit_long_time_out.create(AlisanAPI.class);
    }

    public static Converter.Factory getConverterFactoryGson() {
        return GsonConverterFactory.create(new GsonBuilder()
                .setDateFormat("yyyy-MM-dd")
                .setPrettyPrinting()
                .setLenient()
                .serializeNulls()
                .create());
    }

    public static OkHttpClient getClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES)
                .addInterceptor(getLoggingInterceptor())
                .connectionSpecs(Arrays.asList(ConnectionSpec.MODERN_TLS, ConnectionSpec.CLEARTEXT, ConnectionSpec.COMPATIBLE_TLS))
                .addInterceptor(getInceptorRequestHeader())
                .build();
    }

    public static OkHttpClient new_client () {
        return new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES)
                .addInterceptor(getLoggingInterceptor())
                .connectionSpecs(Arrays.asList(ConnectionSpec.MODERN_TLS, ConnectionSpec.CLEARTEXT, ConnectionSpec.COMPATIBLE_TLS))
                .addInterceptor(getInceptorRequestHeader())
                .build();
    }




    public static Interceptor getInceptorRequestHeader() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request.Builder builder = chain.request().newBuilder();
                builder.addHeader("Authorization", "Authorization KEY");
//                builder.addHeader("Content-Type", "application/json");

                Request request = builder.build();
                return chain.proceed(request);
            }
        };
    }

    public static HttpLoggingInterceptor getLoggingInterceptor() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(getInterceptorLevel());
        return httpLoggingInterceptor;
    }

    public static HttpLoggingInterceptor.Level getInterceptorLevel() {
        if (BuildConfig.DEBUG) return HttpLoggingInterceptor.Level.BODY;
        else return HttpLoggingInterceptor.Level.NONE;
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

    public interface AlisanAPI {

        @FormUrlEncoded
        @POST(Common.SUB_PATH + "/Session/login")
        Call<LoginResponse> getLogin(@FieldMap Map<String, String> bodyMap);

        @Multipart
        @POST(Common.SUB_PATH + "/Session/register")
        Call<MultiResponse> getRegister(@PartMap Map<String, RequestBody> names);

        @Multipart
        @POST(Common.SUB_PATH + "/Sensor/loker")
        Call<MultiResponse> getDaftarKartu(@PartMap Map<String, RequestBody> names);

        @GET(Common.SUB_PATH + "/Sensor/monitoring")
        Call<ApiMonitoring> getKartu();

        @GET(Common.SUB_PATH + "/Session/user_terdaftar")
        Call<ApiListUser> getDataUser();

    }
}