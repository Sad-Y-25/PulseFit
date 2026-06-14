package com.example.pulsefit.workers;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.pulsefit.database.DatabaseHelper;

public class MigrationWorker extends Worker {

    public MigrationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            // Initiate the DatabaseHelper
            DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
            
            // Execute the automated history migration
            dbHelper.migratePastReservations();
            
            // Indicate success
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure();
        }
    }
}
