package com.example.pulsefit.network;

import com.example.pulsefit.models.Session;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("sessions")
    Call<List<Session>> getSessions();
}
