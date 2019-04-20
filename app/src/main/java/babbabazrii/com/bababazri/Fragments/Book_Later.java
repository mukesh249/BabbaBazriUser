package babbabazrii.com.bababazri.Fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;

import net.bohush.geometricprogressview.GeometricProgressView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import babbabazrii.com.bababazri.Activities.Choose_Location;
import babbabazrii.com.bababazri.Adapters.Trending_Product_Adapter;
import babbabazrii.com.bababazri.Api.RequestCode;
import babbabazrii.com.bababazri.Api.WebCompleteTask;
import babbabazrii.com.bababazri.Api.WebTask;
import babbabazrii.com.bababazri.Api.WebUrls;
import babbabazrii.com.bababazri.R;
import babbabazrii.com.bababazri.models.Trending_Product_Model;

/**
 * A simple {@link Fragment} subclass.
 */
public class Book_Later extends Fragment implements WebCompleteTask {


    LinearLayout nearby_lin;
    private static final String TAG = Book_Later.class.getSimpleName();
    private RecyclerView recyclerView;
    private ArrayList<Trending_Product_Model> trending_product = new ArrayList<>();
    Trending_Product_Adapter mAdapter;
    private SearchView searchView;
    GeometricProgressView progressBar;
    private boolean itShouldLoadMore = true;
    EditText searchField;
    ImageView searchicon;
    View view;
    SwipeRefreshLayout swipeRefreshLayout;

    public static final Map<String, String> subtype_and_unit = new HashMap<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view == null){
            view = inflater.inflate(R.layout.fragment_book__later, container, false);
        }

        nearby_lin = (LinearLayout) view.findViewById(R.id.nearby_lin);
        recyclerView = (RecyclerView)view.findViewById(R.id.trending_prod_recycleview);
        progressBar = (GeometricProgressView) view.findViewById(R.id.loader);
        searchField = (EditText)view.findViewById(R.id.search_et_book_later);
        FetchSubtypeAndUnity();
        recyclerView.setHasFixedSize(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.book_latter);
        trending_product.clear();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.pull_refresh_booklater);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                fetchTrendingProducts();
            }
        });
//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//
//            // for this tutorial, this is the ONLY method that we need, ignore the rest
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                if (dy > 0) {
//                    // Recycle view scrolling downwards...
//                    // this if statement detects when user reaches the end of recyclerView, this is only time we should load more
//                    if (!recyclerView.canScrollVertically(RecyclerView.FOCUS_DOWN)) {
//                        // remember "!" is the same as "== false"
//                        // here we are now allowed to load more, but we need to be careful
//                        // we must check if itShouldLoadMore variable is true [unlocked]
//                        if (itShouldLoadMore) {
//                            fetchTrendingProducts();
//                        }
//                    }
//
//                }
//            }
//        });
        fetchTrendingProducts();


        mAdapter = new Trending_Product_Adapter(trending_product, getActivity());
        recyclerView.setAdapter(mAdapter);

        nearby_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Choose_Location.class);
                startActivity(intent);
            }
        });



//-----------------------------------------Searching prduct-------------------------------------
        searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                // filter your list from your input
                mAdapter.getFilter().filter(searchField.getText());
//                filter(s.toString());
                //you can use runnable postDelayed like 500 ms to delay search text
            }
        });
//-----------------------------------------Searching prduct-------------------------------------

        return view;
    }

    //-----------------------------------------Searching prduct-------------------------------------
    //Trending_Product_Adapter adapter;
//    public void filter(String text){
//        ArrayList<Trending_Product_Model> temp = new ArrayList();
//        for(Trending_Product_Model d: trending_product){
//            //or use .equal(text) with you want equal match
//            //use .toLowerCase() for better matches
//            if(d.getProduct_name().toLowerCase().contains(text.toLowerCase()) ){
//                temp.add(d);
//            }
//        }
//        //update recyclerview
//        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
//        mAdapter = new Trending_Product_Adapter(temp, getContext());
//        recyclerView.setAdapter(mAdapter);
//        mAdapter.notifyDataSetChanged();  // data set changed
//    }
    //-----------------------------------------Searching prduct-------------------------------------

    private void FetchSubtypeAndUnity(){
        HashMap objectNew = new HashMap();
        new WebTask(getActivity(),WebUrls.BASE_URL+WebUrls.getSubTypesObj,objectNew,Book_Later.this,RequestCode.CODE_GetSubTypesObj,0);
    }

    private void fetchTrendingProducts(){
        HashMap objectNew =new HashMap();
        new WebTask(getActivity(),WebUrls.BASE_URL+WebUrls.trendingProduct_api,objectNew,Book_Later.this, RequestCode.CODE_TrendingProduct,0);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onComplete(String response, int taskcode) {
        if (taskcode == RequestCode.CODE_TrendingProduct){
            trending_product.clear();
            try {
                JSONObject jsonObject=new JSONObject(response);
                JSONObject successObj = jsonObject.optJSONObject("success");
                JSONArray jsonArray=successObj.optJSONArray("data");
//                if (trending_product.size()<=jsonArray.length()){
//                    trending_product.add(null);
//                    mAdapter.notifyItemInserted(trending_product.size()-1);
//                    progressBar.setVisibility(View.GONE);
//                    trending_product.remove(trending_product.size()-1);
//                    mAdapter.notifyItemRemoved(trending_product.size());
//                    int index = trending_product.size();
//                    int end = index + 16;

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject o = jsonArray.optJSONObject(i);
                        Trending_Product_Model item=new Trending_Product_Model();
                        item.setProduct_image(o.optString("image"));
                        item.setProduct_name(o.optString("name"));
                        item.setProduct_createdAt(o.optString("createdAt"));
                        item.setProduct_updatedAt(o.optString("updatedAt"));
                        item.setProduct_id(o.optString("id"));
                        item.setProduct_cat_name(o.optJSONObject("material").optString("name"));
                        trending_product.add(item);
                    }
                    mAdapter.notifyDataSetChanged();

//                }else {
//                    Toast.makeText(getActivity(), "Loading data completed", Toast.LENGTH_SHORT).show();
//                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else if (taskcode == RequestCode.CODE_GetSubTypesObj){
            try {
                JSONObject jsonObject=new JSONObject(response);
                JSONObject successObj = jsonObject.optJSONObject("success");
                JSONObject dataObject=successObj.optJSONObject("data");
//                SearchItemBinder searchItemBinder=new SearchItemBinder();



                Iterator<String> iterator = dataObject.keys();
                while (iterator.hasNext()){
                    String key = iterator.next();
                    Object value = dataObject.get(key);
//                    searchItemBinder.setAft_s_unit(dataObject.optString(key));
//                    searchItemBinder.setAft_s_subtype(dataObject.optString(key));
                    subtype_and_unit.put(key,value+"");
                    Log.d("Subtype_: ",key +" "+ value);
                }
            }catch (JSONException e){
                e.printStackTrace();
            }

        }
    }

}
