package babbabazrii.com.bababazri.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import babbabazrii.com.bababazri.Activities.MyOrederList;
import babbabazrii.com.bababazri.Api.RequestCode;
import babbabazrii.com.bababazri.Api.WebUrls;
import babbabazrii.com.bababazri.R;
import babbabazrii.com.bababazri.SharedPrefManager;
import babbabazrii.com.bababazri.models.RequestListWapper;

public class MyOrderListAdapter extends RecyclerView.Adapter<MyOrderListAdapter.MyCustomViewHolder> {

    Context context;
    ArrayList<RequestListWapper> requestListWapperArrayList;
    RequestListWapper requestListWapper;
    String req_id;

    public MyOrderListAdapter(ArrayList<RequestListWapper> requestListWapperArrayList, Context context) {
        this.requestListWapperArrayList = requestListWapperArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyCustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.myorder_list_item, null);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        MyCustomViewHolder viewHolder = new MyCustomViewHolder(view, context, requestListWapperArrayList);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyCustomViewHolder holder, int position) {
        requestListWapper = requestListWapperArrayList.get(position);

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        DateFormat target_date = new SimpleDateFormat("EEE,dd MMM hh:mm aaa", Locale.ENGLISH);
        SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy");
        Calendar calendar = Calendar.getInstance();
        TimeZone tz = calendar.getTimeZone();
        target_date.setTimeZone(tz);

        try {
            Date startDate = df.parse(requestListWapper.getOrderDate());
            String newDateString = df.format(startDate);
            String readReminderdate = target_date.format(startDate);
            holder.textView_date.setText(readReminderdate);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            Date startDate = df.parse(requestListWapper.getDeliveryDate());
            String newDateString = df.format(startDate);
            String readReminderdate = output.format(startDate);
            holder.date_d.setText(readReminderdate);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.textView_src.setText(requestListWapper.getP_formatedAddress());
        holder.textView_des.setText(requestListWapper.getC_formatedAddress());
        holder.textView_price.setText(requestListWapper.getPrice());

        if (SharedPrefManager.getLangId(context, RequestCode.LangId).compareTo("") != 0){
            if (SharedPrefManager.getLangId(context, RequestCode.LangId).compareTo("hi")==0){
                if (requestListWapper.getBookingStatus().equals("Confirm")){
                    holder.textView_b_status.setText(R.string.confirm);
                }else if (requestListWapper.getBookingStatus().equals("Not Confirmed")){
                    holder.textView_b_status.setText(R.string.not_confirm);
                } else if (requestListWapper.getBookingStatus().equals("Cancel")) {
                    holder.textView_b_status.setText("रद्द");
                }
                if (requestListWapper.getDeliveryStatus().compareTo("Pending")==0){
                    holder.textView_del_status.setText(R.string.pending);
                }else if (requestListWapper.getDeliveryStatus().compareTo("Cancel")==0){
                    holder.textView_del_status.setText("रद्द");
                } else if (requestListWapper.getDeliveryStatus().equals("start")) {
                    holder.textView_del_status.setText(R.string.start);
                } else if (requestListWapper.getDeliveryStatus().compareTo("end")==0){
                    holder.textView_del_status.setText(R.string.end);
                }
            }else {
                holder.textView_b_status.setText(requestListWapper.getBookingStatus());
                holder.textView_del_status.setText(requestListWapper.getDeliveryStatus());
            }
        } else {
            holder.textView_b_status.setText(requestListWapper.getBookingStatus());
            holder.textView_del_status.setText(requestListWapper.getDeliveryStatus());
        }

        holder.textView_quantity.setText(requestListWapper.getOrderQuantity());
        holder.textView_mat.setText(requestListWapper.getMaterial_name() + ": ");
        holder.textView_mattype.setText(requestListWapper.getMaterialType_name());
        holder.textView_distance.setText(requestListWapper.getDeliveryDistance());
        Glide.with(context)
                .applyDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.logo))
                .load(WebUrls.BASE_URL + requestListWapper.getVehicleTpye_image())
                .into(holder.veh_im);
        if (requestListWapper.getDeliveryStatus().equals(context.getString(R.string.start))) {
            holder.viewTrack.setText(R.string.track_your_order);
        }else if (requestListWapper.getDeliveryStatus().equals(context.getString(R.string.end)) && !requestListWapper.getRatingDone()){
            holder.viewTrack.setText(R.string.give_rating);
        }else if (requestListWapper.getDeliveryStatus().equals(context.getString(R.string.end)) && requestListWapper.getRatingDone()){
            holder.viewTrack.setText(R.string.completed);
//            holder.viewTrack.setBackgroundResource(R.drawable.btn_price);
        }
        holder.textView_vehicle_name_no.setText(String.format("%s %s", requestListWapper.getVehicleTpye_name(), requestListWapper.getTruckNumber()));
        holder.setListeners();

    }

    @Override
    public int getItemCount() {
        return (null != requestListWapperArrayList ? requestListWapperArrayList.size() : 0);
    }

    public class MyCustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textView_date, textView_src, textView_des, textView_quantity, textView_price, textView_mat, textView_distance,
                textView_b_status, textView_del_status, textView_vehicle_name_no, textView_mattype,date_d;
        Button viewTrack;
        Button delete_item;
        ImageView veh_im;
        ArrayList<RequestListWapper> requestListWapperArrayList = new ArrayList<>();
        Context context;
        int pos;

        public MyCustomViewHolder(View view, Context context, ArrayList<RequestListWapper> requestListWapperArrayList) {
            super(view);
            this.context = context;
            this.requestListWapperArrayList = requestListWapperArrayList;

            view.setOnClickListener(this);
            textView_date = (TextView) view.findViewById(R.id.date_myorder_list);
            textView_src = (TextView) view.findViewById(R.id.src_address_myorder_list);
            textView_des = (TextView) view.findViewById(R.id.destination_address_myorder_list);
            textView_quantity = (TextView) view.findViewById(R.id.orderQuantity_myorder_list);
            textView_price = (TextView) view.findViewById(R.id.price_myorder_list);
            textView_mat = (TextView) view.findViewById(R.id.material_myorder_list);
            textView_b_status = (TextView) view.findViewById(R.id.booking_status_myorder_list);
            textView_del_status = (TextView) view.findViewById(R.id.delivery_status_myorder_list);
            textView_distance = (TextView) view.findViewById(R.id.distance_myorder_list);
            textView_vehicle_name_no = (TextView) view.findViewById(R.id.vehicle_naeme_no_myorder_list);
            textView_mattype = (TextView) view.findViewById(R.id.materialtypr_myorder_list);
            viewTrack = (Button) view.findViewById(R.id.btn_viewdetail);
            delete_item = (Button) view.findViewById(R.id.delete_item);
            veh_im = (ImageView)view.findViewById(R.id.my_order_list_vehicle_image);
            date_d =(TextView)view.findViewById(R.id.date_d_myorder_list);

        }

        public void setListeners() {
            viewTrack.setOnClickListener(MyCustomViewHolder.this);
            delete_item.setOnClickListener(MyCustomViewHolder.this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_viewdetail:
                    TrackingYourOrder(pos);
                    break;
                case R.id.delete_item:
                    TrackingItemDelete(pos);
                    break;
            }
        }

        private void TrackingYourOrder(int posi) {
            posi = getAdapterPosition();
            RequestListWapper requestListWapper = this.requestListWapperArrayList.get(posi);
//            Long tsLong = System.currentTimeMillis() / 1000;
//            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Locations");
//            FirebaseLocation firebaseLocation = new FirebaseLocation(requestListWapper.getId(),"0.0","0.0", "pending", tsLong);
//            mDatabase.child(req_id).setValue(firebaseLocation);
//            Toast.makeText(context, "Booking confirm successfully", Toast.LENGTH_SHORT).show();
            if (requestListWapper.getBookingStatus().equals("Not Confirmed")) {
                Toast.makeText(itemView.getContext(), R.string.booking_not_confired, Toast.LENGTH_LONG).show();
            } else if (requestListWapper.getBookingStatus().equals("Cancel")) {
                Toast.makeText(itemView.getContext(), R.string.your_order_cancel, Toast.LENGTH_LONG).show();
            } else if (requestListWapper.getDriverId().equals("")){
                Toast.makeText(itemView.getContext(), R.string.driver_not_assign, Toast.LENGTH_LONG).show();
            }  else {
                if (requestListWapper.getDeliveryStatus().equals("end")) {
                    if (requestListWapper.getRatingDone()) {
                        Toast.makeText(itemView.getContext(), R.string.your_order_completed, Toast.LENGTH_SHORT).show();
                    } else {
                        String id = requestListWapper.getId();
                        String price = requestListWapper.getPrice();
                        String src = requestListWapper.getP_formatedAddress();
                        String des = requestListWapper.getC_formatedAddress();
                        MyOrederList.getInstance().RaitingIntent(id, price, src, des);
                    }

                } else if (requestListWapper.getDeliveryStatus().equals("start")) {

                    MyOrederList.getInstance().TrakMethod(requestListWapper);
//                   // ((MyOrederList) context).finish();
//                    Intent intent = new Intent(context, Tracking.class);
//                    intent.putExtra("data", requestListWapper);
////                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
////                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//                    //  intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    context.startActivity(intent);
////                    ((MyOrederList)context).finish();
                }else {
                    Toast.makeText(itemView.getContext(), R.string.not_dispatched, Toast.LENGTH_SHORT).show();
                }
            }

        }

        private void TrackingItemDelete(int position) {
            position = getAdapterPosition();
            RequestListWapper requestListWapper = this.requestListWapperArrayList.get(position);
//            MyOrederList.getInstance().GetPricekMethod(requestListWapper);
            MyOrederList.getInstance().updateBottom(requestListWapper);
        }
    }
}
