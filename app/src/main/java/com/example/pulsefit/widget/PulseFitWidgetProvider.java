package com.example.pulsefit.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.pulsefit.R;
import com.example.pulsefit.activities.MainActivity;
import com.example.pulsefit.database.DatabaseHelper;
import com.example.pulsefit.utils.SessionManager;

public class PulseFitWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    private void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_pulsefit);

        // Access the database to get the next session
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SessionManager sessionManager = new SessionManager(context);
        String email = sessionManager.getUserEmail();

        String sessionName = "Aucune séance";
        String sessionTime = "Réservez dans l'app";

        if (email != null) {
            // For the sake of this mock, just fetch count to see if they have any bookings
            int count = dbHelper.getReservationCount(email);
            if (count > 0) {
                sessionName = "Prochaine Séance"; // In a real app we'd fetch the specific upcoming session details via a JOIN
                sessionTime = "Bientôt ! (" + count + " réservées)";
            }
        }

        views.setTextViewText(R.id.tvWidgetSessionTitle, sessionName);
        views.setTextViewText(R.id.tvWidgetSessionTime, sessionTime);

        // Clicking the widget opens the MainActivity
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        views.setOnClickPendingIntent(R.id.tvWidgetSessionTitle, pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
}
