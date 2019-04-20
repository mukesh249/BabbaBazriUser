package babbabazrii.com.bababazri.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import babbabazrii.com.bababazri.Fragments.Home;
import babbabazrii.com.bababazri.R;
import babbabazrii.com.bababazri.models.Material_list_Model;


public class Material_List_Adapter extends RecyclerView.Adapter<Material_List_Adapter.MyViewHolder>{
    Context c;
    ArrayList<Material_list_Model> material_list_modelArrayList;
    Material_list_Model material_list_model;
    int row_index=-1,row_check;
    public static String meterial_id;
    public static Boolean material_isclick=false;
    public static Boolean isclick = false;

    public Material_List_Adapter(Context c, ArrayList<Material_list_Model> spacecrafts) {
        this.c = c;
        this.material_list_modelArrayList = spacecrafts;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.material_list_raw, parent, false);
        return new MyViewHolder(v,c,material_list_modelArrayList);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        material_list_model = this.material_list_modelArrayList.get(position);
        //BIND DATA
        holder.textView_id.setText(material_list_model.getMatrial_id());
        holder.textView_name.setText(material_list_model.getMaterial_name());
        holder.setListeners();
        holder.textView_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                row_index = position;
                notifyDataSetChanged();
            }
        });
        if(row_index==position){
            holder.textView_name.setBackground(c.getDrawable(R.drawable.login_button));
            holder.textView_name.setTextColor(Color.WHITE);
            row_check=holder.getAdapterPosition();
            Material_list_Model material_list_model = this.material_list_modelArrayList.get(row_check);
            meterial_id = material_list_model.getMatrial_id();

            if (Home.nearbyLatLng_isclick==true){
                if (Home.vehicle_isclick==true){
                    material_isclick = true;
                    Home.getInstance().getNearbyDriversVehicleWithMaterial(Home.locationObject,Home.vehical_id,meterial_id);
                }else if (material_isclick==false){
                    material_isclick = true;
                    Home.getInstance().getNearbyDriversMaterial(Home.locationObject,meterial_id);
                }else if (material_isclick==true){
                    Home.getInstance().getNearbyDriversMaterial(Home.locationObject,meterial_id);
                }

            }else {
                if (Home.vehicle_isclick==true) {
                    material_isclick = true;
                    Home.getInstance().getNearbyDriversVehicleWithMaterial(Home.locationObject,Home.vehical_id, meterial_id);
                }
                else if (material_isclick == false) {
                    material_isclick = true;
                    Home.getInstance().getNearbyDriversMaterial(Home.locationObject,meterial_id);
                }
                else if (material_isclick == true) {
                    Home.getInstance().getNearbyDriversMaterial(Home.locationObject,meterial_id);
                }
            }

        }else {
            holder.textView_name.setBackground(c.getDrawable(R.drawable.round_line));
            holder.textView_name.setTextColor(Color.GRAY);
        }

    }

    @Override
    public int getItemCount() {
        return (null != material_list_modelArrayList ? material_list_modelArrayList.size() : 0);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        Context context;
        TextView textView_id,textView_name;
        ArrayList<Material_list_Model> material_list_modelArrayList;
        int pos;

        public MyViewHolder(View v,Context context,ArrayList<Material_list_Model>material_list_modelArrayList) {
            super(v);
            this.material_list_modelArrayList = material_list_modelArrayList;
            this.context = context;
            v.setOnClickListener(this);

            textView_name = (TextView)v.findViewById(R.id.material_list_name);
            textView_id = (TextView)v.findViewById(R.id.material_list_id);
        }

        public void setListeners(){
            textView_name.setOnClickListener(MyViewHolder.this);
        }
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.material_list_name:{
                   // gotoRequest(pos);
                }
                break;
            }
        }

        private void gotoRequest(int posi) {
            posi = getAdapterPosition();
            Material_list_Model materialListModel=this.material_list_modelArrayList.get(posi);
            meterial_id = materialListModel.getMatrial_id();

            for (int i=0;i<Home.matrialName.size();i++){
                if (materialListModel.getMaterial_name().toString().trim().equalsIgnoreCase(Home.matrialName.get(i))){
                    textView_name.setBackground(c.getDrawable(R.drawable.login_button));
                    textView_name.setTextColor(Color.WHITE);
                }else {
                    textView_name.setBackground(c.getDrawable(R.drawable.round_line));
                    textView_name.setTextColor(Color.GRAY);
                }
            }

            if (Home.nearbyLatLng_isclick==true){
                if (Home.vehicle_isclick==true){
                    material_isclick = true;
                    Home.getInstance().getNearbyDriversVehicleWithMaterial(Home.locationObject,Home.vehical_id,meterial_id);
                }else if (material_isclick==false){
                    material_isclick = true;
                    Home.getInstance().getNearbyDriversMaterial(Home.locationObject,meterial_id);
                }else if (material_isclick==true){
                    Home.getInstance().getNearbyDriversMaterial(Home.locationObject,meterial_id);
                }

            }else {
                if (Home.vehicle_isclick==true) {
                    material_isclick = true;
                    Home.getInstance().getNearbyDriversVehicleWithMaterial(Home.locationObject,Home.vehical_id, meterial_id);
                }
                else if (material_isclick == false) {
                    material_isclick = true;
                    Home.getInstance().getNearbyDriversMaterial(Home.locationObject,meterial_id);
                }
                else if (material_isclick == true) {
                    Home.getInstance().getNearbyDriversMaterial(Home.locationObject,meterial_id);
                }
            }
        }
    }
}
