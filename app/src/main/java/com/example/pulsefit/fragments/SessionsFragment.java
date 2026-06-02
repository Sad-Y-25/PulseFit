package com.example.pulsefit.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pulsefit.R;
import com.example.pulsefit.adapters.SessionAdapter;
import com.example.pulsefit.database.DatabaseHelper;
import com.example.pulsefit.models.Session;
import com.example.pulsefit.network.ApiService;
import com.example.pulsefit.network.RetrofitClient;
import com.example.pulsefit.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SessionsFragment extends Fragment {

    private static final String ARG_IS_RESERVED = "is_reserved";
    private boolean isReservedOnly;

    private RecyclerView rvSessions;
    private SessionAdapter sessionAdapter;

    public static SessionsFragment newInstance(boolean isReservedOnly) {
        SessionsFragment fragment = new SessionsFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_IS_RESERVED, isReservedOnly);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isReservedOnly = getArguments().getBoolean(ARG_IS_RESERVED, false);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sessions, container, false);
        
        rvSessions = view.findViewById(R.id.rvSessions);
        rvSessions.setLayoutManager(new LinearLayoutManager(getContext()));

        fetchSessions();

        return view;
    }

    private void fetchSessions() {
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<List<Session>> call = apiService.getSessions();
        
        call.enqueue(new Callback<List<Session>>() {
            @Override
            public void onResponse(Call<List<Session>> call, Response<List<Session>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Session> allSessions = response.body();
                    List<Session> filteredSessions = new ArrayList<>();

                    DatabaseHelper dbHelper = new DatabaseHelper(getContext());
                    SessionManager sessionManager = new SessionManager(getContext());
                    String email = sessionManager.getUserEmail();

                    if (email != null) {
                        for (Session session : allSessions) {
                            boolean isReserved = dbHelper.checkReservationExists(email, session.getId());
                            if (isReservedOnly && isReserved) {
                                filteredSessions.add(session);
                            } else if (!isReservedOnly && !isReserved) {
                                filteredSessions.add(session);
                            }
                        }
                    }

                    sessionAdapter = new SessionAdapter(getContext(), filteredSessions, isReservedOnly);
                    rvSessions.setAdapter(sessionAdapter);
                } else {
                    Toast.makeText(getContext(), "Erreur serveur", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Session>> call, Throwable t) {
                Log.e("SessionsFragment", "Erreur: " + t.getMessage());
                Toast.makeText(getContext(), "Erreur de connexion", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
