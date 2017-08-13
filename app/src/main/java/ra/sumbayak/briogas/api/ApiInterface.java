package ra.sumbayak.briogas.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import ra.sumbayak.briogas.api.models.DeviceData;
import ra.sumbayak.briogas.api.models.LoginCredentials;
import ra.sumbayak.briogas.api.models.MethaneProduction;
import ra.sumbayak.briogas.api.models.Token;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

import static ra.sumbayak.briogas.Constant.*;

public class ApiInterface {
    
    public static class Builder {
        public static Interface build (Context context) {
            Retrofit.Builder builder = new Retrofit.Builder ()
                .baseUrl (API_BASE_URL)
                .addConverterFactory (GsonConverterFactory.create ());
            
            SharedPreferences sp = context
                .getSharedPreferences (SPNAME, Context.MODE_PRIVATE);
            
            if (sp.contains (SPKEY_TOKEN)) {
                final String token = sp.getString (SPKEY_TOKEN, "Null");
                
                builder.client (new OkHttpClient.Builder ()
                    .addInterceptor (new Interceptor () {
                        @Override
                        public Response intercept (@NonNull Chain chain) throws IOException {
                            return chain.proceed (chain.request ()
                                .newBuilder ()
                                .addHeader (AUTH_HEADER_NAME, TOKEN_PREFIX + token)
                                .build ());
                        }
                    })
                    .build ());
            }
            
            return builder.build ().create (Interface.class);
        }
    }
    
    public interface Interface {
        @POST ("token") Call<Token> token (@Body LoginCredentials credentials);
        @GET ("data/retrieve") Call<DeviceData> retrieve ();
        @POST ("toggle-katup") Call<DeviceData> toggle ();
    }
}
