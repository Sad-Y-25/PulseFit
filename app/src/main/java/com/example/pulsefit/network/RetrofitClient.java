package com.example.pulsefit.network;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL = "https://mockapi.pulsefit.com/";
    private static Retrofit retrofit;

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            // Interceptor for mocking the backend
            Interceptor mockInterceptor = new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    if (chain.request().url().encodedPath().endsWith("/sessions")) {
                        String mockResponse = "[\n" +
                                "  {\n" +
                                "    \"id\": 1,\n" +
                                "    \"titre\": \"CrossFit Intense\",\n" +
                                "    \"coach\": \"Alex\",\n" +
                                "    \"date\": \"Aujourd'hui, 18:00\",\n" +
                                "    \"places\": 5\n" +
                                "  },\n" +
                                "  {\n" +
                                "    \"id\": 2,\n" +
                                "    \"titre\": \"Yoga Zen\",\n" +
                                "    \"coach\": \"Sophie\",\n" +
                                "    \"date\": \"Demain, 07:00\",\n" +
                                "    \"places\": 12\n" +
                                "  },\n" +
                                "  {\n" +
                                "    \"id\": 3,\n" +
                                "    \"titre\": \"Boxe Thaï\",\n" +
                                "    \"coach\": \"Marc\",\n" +
                                "    \"date\": \"Demain, 19:30\",\n" +
                                "    \"places\": 2\n" +
                                "  },\n" +
                                "  {\n" +
                                "    \"id\": 4,\n" +
                                "    \"titre\": \"Pilates Core\",\n" +
                                "    \"coach\": \"Emma\",\n" +
                                "    \"date\": \"Jeu, 12:15\",\n" +
                                "    \"places\": 8\n" +
                                "  }\n" +
                                "]";

                        return new Response.Builder()
                                .code(200)
                                .message("OK")
                                .protocol(Protocol.HTTP_1_1)
                                .request(chain.request())
                                .body(ResponseBody.create(MediaType.parse("application/json"), mockResponse.getBytes()))
                                .build();
                    }
                    return chain.proceed(chain.request());
                }
            };

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(mockInterceptor)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
