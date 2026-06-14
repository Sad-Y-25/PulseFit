package com.example.pulsefit.activities;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pulsefit.R;
import com.example.pulsefit.adapters.LeaderboardAdapter;
import com.example.pulsefit.models.LeaderboardUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LeaderboardActivity extends AppCompatActivity {

    private RecyclerView rvLeaderboard;
    private ImageView btnBackLeaderboard;
    private LeaderboardAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Window window = getWindow();
        window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );

        setContentView(R.layout.activity_leaderboard);

        rvLeaderboard = findViewById(R.id.rvLeaderboard);
        btnBackLeaderboard = findViewById(R.id.btnBackLeaderboard);

        btnBackLeaderboard.setOnClickListener(v -> finish());

        setupRecyclerView();
    }

    private void setupRecyclerView() {
        rvLeaderboard.setLayoutManager(new LinearLayoutManager(this));
        
        List<LeaderboardUser> users = generateMockLeaderboard();
        adapter = new LeaderboardAdapter(users);
        rvLeaderboard.setAdapter(adapter);
    }

    private List<LeaderboardUser> generateMockLeaderboard() {
        List<LeaderboardUser> list = new ArrayList<>();
        list.add(new LeaderboardUser("Alexandre D.", 3450));
        list.add(new LeaderboardUser("Sophie M.", 2890));
        list.add(new LeaderboardUser("Thomas R.", 4120));
        list.add(new LeaderboardUser("Julie B.", 1950));
        list.add(new LeaderboardUser("Nicolas P.", 3100));
        list.add(new LeaderboardUser("Emma L.", 2750));
        list.add(new LeaderboardUser("Lucas V.", 3800));
        list.add(new LeaderboardUser("Sarah C.", 2200));
        list.add(new LeaderboardUser("Antoine F.", 1800));
        list.add(new LeaderboardUser("Marie T.", 2950));

        // Sort descending by calories
        Collections.sort(list, new Comparator<LeaderboardUser>() {
            @Override
            public int compare(LeaderboardUser u1, LeaderboardUser u2) {
                return Integer.compare(u2.getCaloriesBurned(), u1.getCaloriesBurned());
            }
        });

        return list;
    }
}
