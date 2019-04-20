package babbabazrii.com.bababazri.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import babbabazrii.com.bababazri.Api.WebUrls;
import babbabazrii.com.bababazri.Fragments.Sub_Product;
import babbabazrii.com.bababazri.R;
import babbabazrii.com.bababazri.models.Trending_Product_Model;

/**
 * Created by mukku on 12/30/2017.
 */

public class Trending_Product_Adapter extends RecyclerView.Adapter<Trending_Product_Adapter.CustomViewHolder> implements Filterable{

    private Context context;
    public ArrayList<Trending_Product_Model> trendingItemsList;
    public static ArrayList<Trending_Product_Model> trendingItemsList_filter;
    ImageLoader imageLoader;
    Trending_Product_Model trending_product_model;
    private int lastPosition = -1;


    public Trending_Product_Adapter (ArrayList<Trending_Product_Model> trendingItemsList,Context context){
        this.trendingItemsList = trendingItemsList;
        this.trendingItemsList_filter = trendingItemsList;
        this.context = context;
    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.book_later_raw, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view,context,trendingItemsList_filter);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder customViewHolder, int i) {
        trending_product_model = trendingItemsList_filter.get(i);

//        imageLoader = ImageLoader.getInstance();
//        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
//        DisplayImageOptions options = new DisplayImageOptions.Builder()
//                .displayer(new RoundedBitmapDisplayer(10))
//                .cacheInMemory(true)
//                .cacheOnDisk(true)
//                .build();
////        String Url="http://demo.rajasthankayamkhanimahasabha.com//syspanel/action/upload/"+newsItem.getNews_thumbimg();
//        ImageLoader.getInstance().displayImage(newsItem.getNews_thumbimg(),customViewHolder.imageView,options);
//        Log.e("Url",""+customViewHolder.imageView);

//        customViewHolder.txt_news_id.setText(newsItem.getId());
        customViewHolder.txt_news_name.setText(trending_product_model.getProduct_name());
        customViewHolder.txt_cat_prod.setText(trending_product_model.getProduct_cat_name());
        Glide.with(context)
                .applyDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.logo))
                .load(WebUrls.BASE_URL+trending_product_model.getProduct_image())
                .into(customViewHolder.imageView);
    }

    @Override
    public int getItemCount() {
        return (null != trendingItemsList_filter ? trendingItemsList_filter.size() : 0);
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_cat_prod,txt_news_name,txt_news_content;
        ImageView imageView;
        ArrayList<Trending_Product_Model>newsItemsList=new ArrayList<>();
        Context context;

        public CustomViewHolder(View view, final Context context,ArrayList<Trending_Product_Model>trendingItemsList_filter){
            super(view);
            this.context=context;
            this.newsItemsList=trendingItemsList_filter;
            view.setOnClickListener(this);

            this.imageView = (ImageView)view.findViewById(R.id.trending_product_image);
            this.txt_news_name=(TextView)view.findViewById(R.id.trendig_product_name);
            this.txt_cat_prod=(TextView)view.findViewById(R.id.trendig_product_name_cat);
        }

        @Override
        public void onClick(View view) {
            int position=getAdapterPosition();
            Sub_Product fragment= new Sub_Product();

            Trending_Product_Model trending_product_model=trendingItemsList_filter.get(position);
            Bundle bundle = new Bundle();
            bundle.putString("Product_Name", trending_product_model.getProduct_name());
            bundle.putString("Product_Id", trending_product_model.getProduct_id());
            fragment.setArguments(bundle);
            ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                    .add(R.id.content_frame,fragment )
                    .addToBackStack(null)
                    .commitAllowingStateLoss();
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    trendingItemsList_filter = trendingItemsList;
                } else {
                    ArrayList<Trending_Product_Model> filteredList = new ArrayList<>();
                    for (Trending_Product_Model row : trendingItemsList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getProduct_cat_name().toLowerCase().contains(charString.toLowerCase())
                                || row.getProduct_name().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    trendingItemsList_filter = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = trendingItemsList_filter;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                trendingItemsList_filter = (ArrayList<Trending_Product_Model>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
