package babbabazrii.com.bababazri.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import babbabazrii.com.bababazri.Activities.After_Request_Btn;
import babbabazrii.com.bababazri.Api.WebUrls;
import babbabazrii.com.bababazri.Interface.OnLoadMoreListener;
import babbabazrii.com.bababazri.R;
import babbabazrii.com.bababazri.SharedPrefManager;
import babbabazrii.com.bababazri.models.SearchItemBinder;

public class After_Search_Adapter extends RecyclerView.Adapter<After_Search_Adapter.CustomViewHolder> implements Filterable{

    private Context context;
    private ArrayList<SearchItemBinder> afterSearch_product_models_list;
    private ArrayList<SearchItemBinder> afterSearch_product_models_list_filter;
    SearchItemBinder afterSearch_product_model;
    public OnLoadMoreListener onLoadMoreListener;


    public After_Search_Adapter (ArrayList<SearchItemBinder> afterSearch_product_models_list,Context context){
        this.afterSearch_product_models_list = afterSearch_product_models_list;
        this.afterSearch_product_models_list_filter = afterSearch_product_models_list;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.after_search_row,null);
        return new CustomViewHolder(view,context,afterSearch_product_models_list_filter);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        afterSearch_product_model = afterSearch_product_models_list_filter.get(position);

        holder.aft_s_name.setText(afterSearch_product_model.getAft_s_mat_type_name());
        holder.aft_s_type.setText(afterSearch_product_model.getAft_s_mat_name());
        holder.aft_s_v_name.setText(afterSearch_product_model.getAft_s_name());
        holder.aft_s_location.setText(afterSearch_product_model.getAft_s_formattedAddress());
        holder.aft_s_subtype.setText(afterSearch_product_model.getAft_s_subtype());
        holder.aft_s_price.setText(afterSearch_product_model.getAft_s_price()+"/"+afterSearch_product_model.getAft_s_unit_show());
        Glide.with(context)
                .applyDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.logo))
                .load(WebUrls.BASE_URL+afterSearch_product_model.getAfter_s_img())
                .into(holder.aft_s_imag);
        holder.setListeners();
    }

    @Override
    public int getItemCount() {
        return (null != afterSearch_product_models_list_filter ? afterSearch_product_models_list_filter.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,OnLoadMoreListener{
        TextView aft_s_v_name,aft_s_name,aft_s_type,aft_s_location,aft_s_price,aft_s_id,aft_s_subtype;
        ImageView aft_s_imag;
        TextView aft_rst_btn;
        ArrayList<SearchItemBinder> afterSearch_product_models_list_filter;
        Context context;
        int position;

        public CustomViewHolder(View itemView,Context context,ArrayList<SearchItemBinder> afterSearch_product_models_list_filter) {
            super(itemView);
            this.context = context;
            this.afterSearch_product_models_list_filter= afterSearch_product_models_list_filter;
            itemView.setOnClickListener(this);

            aft_s_v_name = (TextView)itemView.findViewById(R.id.after_search_vendor_name);
            aft_s_name = (TextView)itemView.findViewById(R.id.after_search_product_name);
            aft_s_type = (TextView)itemView.findViewById(R.id.after_search_product_type);
            aft_s_location = (TextView)itemView.findViewById(R.id.after_search_product_location);
            aft_s_price = (TextView)itemView.findViewById(R.id.after_search_product_price);
            aft_s_imag = (ImageView)itemView.findViewById(R.id.after_search_product_image);
            aft_s_id = (TextView)itemView.findViewById(R.id.after_search_product_id);
            aft_rst_btn = (TextView)itemView.findViewById(R.id.after_search_product_Request_btn);
            aft_s_subtype = (TextView)itemView.findViewById(R.id.grain);


        }
        public void setListeners(){
            aft_rst_btn.setOnClickListener(CustomViewHolder.this);
        }

        @Override
        public void onClick(View view) {

            switch(view.getId()){
                case R.id.after_search_product_Request_btn:
                    gotoRequest(position);
                    break;
            }
        }

        @Override
        public void HidePriceMethod() {
            aft_s_price.setVisibility(View.GONE);
        }

        public void ShowPriceMethod(){
            aft_s_price.setVisibility(View.VISIBLE);
        }
        private void gotoRequest(int position) {
            position=getAdapterPosition();
            Bundle bundle = new Bundle();
            SearchItemBinder afterSearch_product_model=this.afterSearch_product_models_list_filter.get(position);
            SharedPrefManager.getInstance(context).storeProductId(afterSearch_product_model.getAft_s_id());
            SharedPrefManager.getInstance(context).storeProductName(afterSearch_product_model.getAft_s_mat_type_name());
            Intent intent=new Intent(context,After_Request_Btn.class);
//            ArrayList<String> list = afterSearch_product_model.getListdata();
            intent.putExtra("data_after",afterSearch_product_model);
            intent.putExtra("data",afterSearch_product_model.getListdata());
            Log.d("spiner data",afterSearch_product_model.getListdata()+"");
//            intent.putExtra("Url",jobsItem.getJobs_apply());
            context.startActivity(intent);
        }

    }
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    afterSearch_product_models_list_filter = afterSearch_product_models_list;
                } else {
                    ArrayList<SearchItemBinder> filteredList = new ArrayList<>();
                    for (SearchItemBinder row : afterSearch_product_models_list) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getAft_s_mat_name().toLowerCase().contains(charString.toLowerCase())
                                || row.getAft_s_street().toLowerCase().contains(charString.toLowerCase())
                                || row.getAft_s_mat_type_name().toLowerCase().contains(charString.toLowerCase())
                                ||row.getAft_s_price().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    afterSearch_product_models_list_filter = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = afterSearch_product_models_list_filter;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                afterSearch_product_models_list_filter = (ArrayList<SearchItemBinder>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
