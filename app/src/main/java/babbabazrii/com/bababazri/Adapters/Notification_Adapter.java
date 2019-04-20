package babbabazrii.com.bababazri.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import babbabazrii.com.bababazri.Activities.Notification;
import babbabazrii.com.bababazri.R;
import babbabazrii.com.bababazri.models.Notification_Model;

public class Notification_Adapter extends RecyclerView.Adapter<Notification_Adapter.MyCustomViewHolder> {

    Context context;
    ArrayList<Notification_Model> notificationModelArrayList;
    Notification_Model notification_model;
    String req_id;
    Activity activity;

    public Notification_Adapter(ArrayList<Notification_Model>notificationModelArrayList,Context context){
        this.notificationModelArrayList = notificationModelArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyCustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notificaion_raw,null);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        MyCustomViewHolder viewHolder = new MyCustomViewHolder(view,context,notificationModelArrayList);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyCustomViewHolder holder, int position) {
        notification_model = notificationModelArrayList.get(position);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        DateFormat target_date = new SimpleDateFormat("EEE,dd MMM hh:mm aaa", Locale.ENGLISH);
        SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy");
        Calendar calendar = Calendar.getInstance();
        TimeZone tz = calendar.getTimeZone();
        target_date.setTimeZone(tz);

        try {
            Date startDate = df.parse(notification_model.getNotificatin_createdAt());
            String newDateString = df.format(startDate);
            String readReminderdate = target_date.format(startDate);
            holder.textView_date.setText(readReminderdate);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.textView_title.setText(notification_model.getNotification_Title());
        holder.textView_message.setText(notification_model.getNotification_message());
    }

    @Override
    public int getItemCount() {
        return (null != notificationModelArrayList ? notificationModelArrayList.size() : 0);
    }

    public class MyCustomViewHolder extends RecyclerView.ViewHolder{
        TextView textView_title,textView_message,textView_date;
        ArrayList<Notification_Model> notificationModelArrayList = new ArrayList<>();
        Context context;
        int pos;

        public MyCustomViewHolder(View view, Context context, ArrayList<Notification_Model> notificationModelArrayList) {
            super(view);
            this.context = context;
            this.notificationModelArrayList = notificationModelArrayList;

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notificationMethod(pos);
                }
            });
            textView_title = view.findViewById(R.id.material_name_notification);
            textView_message = view.findViewById(R.id.message_notification);
            textView_date = view.findViewById(R.id.date_noti);
        }
        private void notificationMethod(int pos) {
            pos = getAdapterPosition();
            notification_model = this.notificationModelArrayList.get(pos);
//            if (notification_model.getNotification_Title().equals("Order delivered")){
//                Notification.getInstance().RaitingIntent();
//            }
//            else {
                Notification.getInstance().PopMessage(notification_model.getNotification_Title(),notification_model.getNotification_message());
//            }
        }
    }


}
