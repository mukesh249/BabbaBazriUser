package babbabazrii.com.bababazri.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import babbabazrii.com.bababazri.Api.WebUrls;
import babbabazrii.com.bababazri.Fragments.Home;
import babbabazrii.com.bababazri.R;
import babbabazrii.com.bababazri.models.Vehicle_Model;

public class Vehicle_List_Adapter extends RecyclerView.Adapter<Vehicle_List_Adapter.MyViewHolder>{
    Context c;
    ArrayList<Vehicle_Model> vehicle_modelArrayList;
    Vehicle_Model vehicle_model;
    int row_index=-1,row_check;
    public static String vehicle_id;
    public static Boolean vehicle_isclick=false;
    public static Boolean isclick = false;

    public Vehicle_List_Adapter(Context c, ArrayList<Vehicle_Model> vehicle_modelArrayList) {
        this.c = c;
        this.vehicle_modelArrayList = vehicle_modelArrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.vehicle_row, parent, false);
        return new MyViewHolder(v,c,vehicle_modelArrayList);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        vehicle_model = this.vehicle_modelArrayList.get(position);
        //BIND DATA
        holder.textView_id.setText(vehicle_model.getVehicle_id());
        holder.textView_name.setText(vehicle_model.getVehicle_name());
        Glide.with(c)
                .applyDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.truck))
                .load(WebUrls.BASE_URL+vehicle_model.getVehicle_image())
                .into(holder.imageView);

        holder.setListeners();
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                row_index = position;
                notifyDataSetChanged();
            }
        });
        if(row_index==position){
            Glide.with(c)
                    .applyDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.truck))
                    .load(WebUrls.BASE_URL+vehicle_model.getVehicle_click_image())
                    .into(holder.imageView);
            row_check=holder.getAdapterPosition();
            Vehicle_Model vehicle_list_model = this.vehicle_modelArrayList.get(row_check);
            vehicle_id = vehicle_list_model.getVehicle_id();

            if (Home.nearbyLatLng_isclick==true){
                if (vehicle_isclick == false && Material_List_Adapter.material_isclick==true){
                    vehicle_isclick = true;
                    Home.getInstance().getNearbyDriversVehicleWithMaterial(Home.locationObject,
                            vehicle_id,Material_List_Adapter.meterial_id);
                }else if (vehicle_isclick == true && Material_List_Adapter.material_isclick==true ){
                    Home.getInstance().getNearbyDriversVehicleWithMaterial(Home.locationObject,vehicle_id,Material_List_Adapter.meterial_id);
                }else {
                    vehicle_isclick = true;
                    Home.getInstance().getNearbyDriversVehicleLatLng(Home.locationObject,vehicle_id);
                }
            }else {
                if (vehicle_isclick == false && Material_List_Adapter.material_isclick==true){
                    vehicle_isclick = true;
                    Home.getInstance().getNearbyDriversVehicleWithMaterial(Home.locationObject,vehicle_id, Material_List_Adapter.meterial_id);
                }else if (vehicle_isclick == true && Material_List_Adapter.material_isclick==true ){
                    Home.getInstance().getNearbyDriversVehicleWithMaterial(Home.locationObject,vehicle_id,Material_List_Adapter.meterial_id);
                }else {
                    vehicle_isclick = true;
                    Home.getInstance().getNearbyDriversVehicle(Home.locationObject,vehicle_id);
                }
            }

        }else {
            Glide.with(c)
                    .applyDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.truck))
                    .load(WebUrls.BASE_URL+vehicle_model.getVehicle_image())
                    .into(holder.imageView);
        }

    }

    @Override
    public int getItemCount() {
        return (null != vehicle_modelArrayList ? vehicle_modelArrayList.size() : 0);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        Context context;
        TextView textView_id,textView_name;
        ImageView imageView;
        LinearLayout linearLayout;
        ArrayList<Vehicle_Model> vehicle_modelArrayList;
        int pos;

        public MyViewHolder(View v,Context context,ArrayList<Vehicle_Model>vehicle_modelArrayList) {
            super(v);
            this.vehicle_modelArrayList = vehicle_modelArrayList;
            this.context = context;
            v.setOnClickListener(this);

            textView_name = (TextView)v.findViewById(R.id.vehicle_list_name);
            textView_id = (TextView)v.findViewById(R.id.vehicle_list_id);
            imageView = (ImageView)v.findViewById(R.id.vehicle_list_image);
            linearLayout = (LinearLayout)v.findViewById(R.id.vehicle_item);
        }

        public void setListeners(){
            textView_name.setOnClickListener(MyViewHolder.this);
        }
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.vehicle_item:{
                   // gotoRequest(pos);
                }
                break;
            }
        }

    }
}
