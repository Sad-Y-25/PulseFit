package com.example.pulsefit.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pulsefit.R;
import com.example.pulsefit.database.DatabaseHelper;
import com.example.pulsefit.models.Session;
import com.example.pulsefit.utils.SessionManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

import java.lang.ref.WeakReference;
import java.util.List;

public class SessionAdapter extends RecyclerView.Adapter<SessionAdapter.SessionViewHolder> {

    private List<Session> sessionList;
    private Context context;
    private boolean isReservedOnly;

    public SessionAdapter(Context context, List<Session> sessionList, boolean isReservedOnly) {
        this.context = context;
        this.sessionList = sessionList;
        this.isReservedOnly = isReservedOnly;
    }

    @NonNull
    @Override
    public SessionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_session, parent, false);
        return new SessionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SessionViewHolder holder, int position) {
        Session session = sessionList.get(position);
        holder.tvSessionTitle.setText(session.getTitre());
        holder.tvCoach.setText("Coach: " + session.getCoach());
        holder.tvDate.setText(session.getDate());
        holder.tvSpots.setText(session.getPlaces() + " places");

        if (isReservedOnly) {
            holder.btnBook.setVisibility(View.GONE);
            holder.btnCancel.setVisibility(View.VISIBLE);
            holder.btnCancel.setOnClickListener(v -> {
                new AlertDialog.Builder(context, android.R.style.Theme_DeviceDefault_Dialog_Alert)
                        .setTitle("Annuler la réservation")
                        .setMessage("Êtes-vous sûr de vouloir annuler votre séance : " + session.getTitre() + " ?")
                        .setPositiveButton("Oui", (dialog, which) -> {
                            new CancelSessionTask(context, session, v, holder.getAdapterPosition()).execute();
                        })
                        .setNegativeButton("Non", null)
                        .show();
            });
        } else {
            holder.btnCancel.setVisibility(View.GONE);
            holder.btnBook.setVisibility(View.VISIBLE);
            holder.btnBook.setOnClickListener(v -> {
                // Launch AsyncTask for booking
                new BookSessionTask(context, session, v).execute();
            });
        }

        holder.itemView.setOnClickListener(v -> {
            new AlertDialog.Builder(context, android.R.style.Theme_DeviceDefault_Dialog_Alert)
                    .setTitle(session.getTitre())
                    .setMessage("Détails de la séance.\nCoach: " + session.getCoach() + "\nDate: " + session.getDate())
                    .setPositiveButton("Appeler Coach", (dialog, which) -> {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:0612345678"));
                        context.startActivity(intent);
                    })
                    .setNegativeButton("Fermer", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return sessionList.size();
    }

    public static class SessionViewHolder extends RecyclerView.ViewHolder {
        TextView tvSessionTitle, tvCoach, tvDate, tvSpots;
        MaterialButton btnBook, btnCancel;

        public SessionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSessionTitle = itemView.findViewById(R.id.tvSessionTitle);
            tvCoach = itemView.findViewById(R.id.tvCoach);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvSpots = itemView.findViewById(R.id.tvSpots);
            btnBook = itemView.findViewById(R.id.btnBook);
            btnCancel = itemView.findViewById(R.id.btnCancel);
        }
    }

    private static class BookSessionTask extends AsyncTask<Void, Void, Boolean> {
        private WeakReference<Context> contextRef;
        private Session session;
        private WeakReference<View> viewRef;

        BookSessionTask(Context context, Session session, View view) {
            this.contextRef = new WeakReference<>(context);
            this.session = session;
            this.viewRef = new WeakReference<>(view);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Context context = contextRef.get();
            if (context == null) return false;

            DatabaseHelper dbHelper = new DatabaseHelper(context);
            SessionManager sessionManager = new SessionManager(context);
            
            String email = sessionManager.getUserEmail();
            if (email == null) return false;

            return dbHelper.insertReservation(email, session.getId());
        }

        @Override
        protected void onPostExecute(Boolean success) {
            Context context = contextRef.get();
            View view = viewRef.get();
            
            if (context != null && view != null) {
                if (success) {
                    Snackbar snackbar = Snackbar.make(view, "Réservation confirmée !", Snackbar.LENGTH_LONG);
                    snackbar.setBackgroundTint(ContextCompat.getColor(context, R.color.vert_neon));
                    snackbar.setTextColor(ContextCompat.getColor(context, R.color.black));
                    snackbar.show();

                    // Schedule a local notification for 5 seconds later
                    scheduleReminder(context, session.getTitre());
                } else {
                    Toast.makeText(context, "Vous avez déjà réservé cette séance.", Toast.LENGTH_SHORT).show();
                }
            }
        }

        private void scheduleReminder(Context context, String sessionTitle) {
            try {
                android.app.AlarmManager alarmManager = (android.app.AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                android.content.Intent intent = new android.content.Intent(context, com.example.pulsefit.receivers.SessionReminderReceiver.class);
                intent.putExtra("SESSION_NAME", sessionTitle);
                
                android.app.PendingIntent pendingIntent = android.app.PendingIntent.getBroadcast(
                        context, 
                        session.getId(), 
                        intent, 
                        android.app.PendingIntent.FLAG_UPDATE_CURRENT | android.app.PendingIntent.FLAG_IMMUTABLE
                );
                
                // 5 seconds from now
                long triggerAtMillis = System.currentTimeMillis() + 5000;
                
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                    if (alarmManager.canScheduleExactAlarms()) {
                        alarmManager.setExact(android.app.AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
                    } else {
                        alarmManager.set(android.app.AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
                    }
                } else {
                    alarmManager.setExact(android.app.AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class CancelSessionTask extends AsyncTask<Void, Void, Boolean> {
        private WeakReference<Context> contextRef;
        private Session session;
        private WeakReference<View> viewRef;
        private int position;

        CancelSessionTask(Context context, Session session, View view, int position) {
            this.contextRef = new WeakReference<>(context);
            this.session = session;
            this.viewRef = new WeakReference<>(view);
            this.position = position;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Context context = contextRef.get();
            if (context == null) return false;

            DatabaseHelper dbHelper = new DatabaseHelper(context);
            SessionManager sessionManager = new SessionManager(context);
            
            String email = sessionManager.getUserEmail();
            if (email == null) return false;

            return dbHelper.deleteReservation(email, session.getId());
        }

        @Override
        protected void onPostExecute(Boolean success) {
            Context context = contextRef.get();
            View view = viewRef.get();
            
            if (context != null && view != null) {
                if (success) {
                    if (position >= 0 && position < sessionList.size()) {
                        sessionList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, sessionList.size());
                    }
                    
                    Snackbar snackbar = Snackbar.make(view, "Réservation annulée.", Snackbar.LENGTH_LONG);
                    snackbar.setBackgroundTint(ContextCompat.getColor(context, R.color.vert_neon));
                    snackbar.setTextColor(ContextCompat.getColor(context, R.color.black));
                    snackbar.show();
                } else {
                    Toast.makeText(context, "Erreur lors de l'annulation.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
