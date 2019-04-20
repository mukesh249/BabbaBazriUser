package babbabazrii.com.bababazri.Fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import babbabazrii.com.bababazri.Activities.Choose_Location;
import babbabazrii.com.bababazri.Adapters.After_Search_Adapter;
import babbabazrii.com.bababazri.Adapters.After_Search_AdapterHide;
import babbabazrii.com.bababazri.Api.RequestCode;
import babbabazrii.com.bababazri.Api.WebCompleteTask;
import babbabazrii.com.bababazri.Api.WebTask;
import babbabazrii.com.bababazri.Api.WebUrls;
import babbabazrii.com.bababazri.R;
import babbabazrii.com.bababazri.SharedPrefManager;
import babbabazrii.com.bababazri.models.SearchItemBinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class Sub_Product extends Fragment implements WebCompleteTask, View.OnClickListener{


    public Sub_Product() {
        // Required empty public constructor
    }

    TextView nearby_location_tv,list_empty;
    Double lat, lng;
    private static final String TAG = "Sub Product";

    private RecyclerView recyclerView_after;
    private ArrayList<SearchItemBinder> sub_product = new ArrayList<SearchItemBinder>();
    After_Search_Adapter mAdapter;
    After_Search_AdapterHide mAdapterH;
    Context mCtx;
    LinearLayout linearLayout;
    private static String add,materrialTypeId_string;

    EditText searchField_sub;
    SwitchCompat aSwitch;
    private boolean itShouldLoadMore = true;
    ProgressBar progressBar;
    String product_name,product_id;
    SwipeRefreshLayout swipeRefreshLayout;
    public static Boolean isPriceshow=true;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sub__product, container, false);

        searchField_sub = (EditText)view.findViewById(R.id.search_et_sub_product);
        aSwitch = (SwitchCompat) view.findViewById(R.id.sub_product_switch);


        linearLayout = (LinearLayout)view.findViewById(R.id.sub_product_nearby_lin);
        linearLayout.setOnClickListener(this);
        recyclerView_after = (RecyclerView) view.findViewById(R.id.sub_product_item_recycleview);
        recyclerView_after.setHasFixedSize(true);
        recyclerView_after.setLayoutManager(new LinearLayoutManager(getContext()));
        progressBar = (ProgressBar)view.findViewById(R.id.loader_sub);
        list_empty = (TextView)view.findViewById(R.id.list_empty_sub_product);


        Bundle bundle = this.getArguments();
        if (bundle != null) {
            product_name = bundle.getString("Product_Name", "");
            product_id = bundle.getString("Product_Id", "");
        }


        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(product_name);
//        materrialTypeId_string = SharedPrefManager.getInstance(getContext()).getSubPoductid();
        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.pull_refresh_subproduct);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
//                fetchAfterSearch();
                if (aSwitch.isChecked()){
                    aSwitch.setText(R.string.with_price);
                    fetchAfterSearch();
                    recyclerView_after.setAdapter(mAdapter);
                }else {
                    aSwitch.setText(R.string.without_price);
                    fetchAfterSearchHide();
                    recyclerView_after.setAdapter(mAdapterH);
                }
            }
        });

        sub_product.clear();
//        recyclerView_after.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
//                            fetchAfterSearch();
//                        }
//                    }
//
//                }
//            }
//        });
//        fetchAfterSearch();

        mAdapter = new After_Search_Adapter(sub_product, getActivity());
        mAdapterH = new After_Search_AdapterHide(sub_product,getActivity());
        //-----------------------------------------Searching prduct-------------------------------------
        searchField_sub.addTextChangedListener(new TextWatcher() {
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
                mAdapter.getFilter().filter(searchField_sub.getText());
                mAdapterH.getFilter().filter(searchField_sub.getText());
                //you can use runnable postDelayed like 500 ms to delay search text
            }
        });
        if (aSwitch.isChecked()){
            aSwitch.setText(R.string.with_price);
            fetchAfterSearch();
            recyclerView_after.setAdapter(mAdapter);
        }else {
            aSwitch.setText(R.string.without_price);
            fetchAfterSearchHide();
            recyclerView_after.setAdapter(mAdapterH);
        }
        //-----------------------------------------Searching prduct-------------------------------------
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (aSwitch.isChecked()){
                    aSwitch.setText(R.string.with_price);
                    fetchAfterSearch();
                    recyclerView_after.setAdapter(mAdapter);
                }else {
                    aSwitch.setText(R.string.without_price);
                    fetchAfterSearchHide();
                    recyclerView_after.setAdapter(mAdapterH);
                }
            }
        });
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sub_product_nearby_lin:
                Intent intent = new Intent(getActivity(), Choose_Location.class);
                startActivity(intent);
        }
    }

    private void fetchAfterSearch() {
        HashMap objectNew = new HashMap();
//        progressBar.setVisibility(View.VISIBLE);
        new WebTask(getActivity(), WebUrls.BASE_URL + WebUrls.sub_product+"materialTypeId="+product_id, objectNew, Sub_Product.this, RequestCode.CODE_Sub_Product, 3);
    }
    private void fetchAfterSearchHide() {
        HashMap objectNew = new HashMap();
//        progressBar.setVisibility(View.VISIBLE);
        new WebTask(getActivity(), WebUrls.BASE_URL + WebUrls.sub_product+"materialTypeId="+product_id, objectNew, Sub_Product.this, RequestCode.CODE_Sub_ProductHide, 3);
    }
    @Override
    public void onComplete(String response, int taskcode) {
        android.util.Log.d("response", response);
        if (taskcode == RequestCode.CODE_Sub_Product) {
            try {
                sub_product.clear();
                JSONObject jsonObject = new JSONObject(response);
                JSONObject successObj = jsonObject.optJSONObject("success");
                JSONArray jsonArray = successObj.optJSONArray("data");
                if (jsonArray != null && jsonArray.length()>0){
                    if (aSwitch.isChecked()){
                        aSwitch.setText(R.string.with_price);
                    }else {
                        aSwitch.setText(R.string.without_price);
                    }
                    recyclerView_after.setVisibility(View.VISIBLE);
                    list_empty.setVisibility(View.GONE);
//                if (sub_product.size()<=jsonArray.length()) {
//                    sub_product.add(null);
//                    mAdapter.notifyItemInserted(sub_product.size() - 1);
//                    progressBar.setVisibility(View.GONE);
//                    sub_product.remove(sub_product.size() - 1);
//                    mAdapter.notifyItemRemoved(sub_product.size());
//                    int index = sub_product.size();
//                    int end = index + 3;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        SearchItemBinder searchItemBinder=new SearchItemBinder();
                        JSONObject o = jsonArray.optJSONObject(i);
                        JSONObject locationObj = o.optJSONObject("location");
                        JSONObject addressObj = o.optJSONObject("address");
                        JSONObject materialObj = o.optJSONObject("material");
                        JSONObject materialTypeObj = o.optJSONObject("materialType");
                        JSONArray unitsArray = materialObj.getJSONArray("units");
                        ArrayList<String> listdata = new ArrayList<>();
                        for (int j = 0; j < unitsArray.length(); j++) {
                            listdata.add(unitsArray.optString(j));
                        }
                        searchItemBinder.setAft_s_name(o.optString("name"));
                        searchItemBinder.setAfter_s_img(o.optString("image"));
                        searchItemBinder.setAft_s_lat(locationObj.optString("lat"));
                        searchItemBinder.setAft_s_lng(locationObj.optString("lng"));
                        searchItemBinder.setAft_s_street(addressObj.optString("street"));
                        searchItemBinder.setAft_s_formattedAddress(addressObj.optString("formattedAddress"));
                        searchItemBinder.setAft_s_mat_name(materialObj.optString("name"));
                        searchItemBinder.setAft_s_mat_id(materialObj.optString("id"));
                        searchItemBinder.setAft_s_mat_type_name(materialTypeObj.optString("name"));
                        searchItemBinder.setAft_s_mat_type_id( materialTypeObj.optString("id"));
                        searchItemBinder.setAft_s_price(o.optString("price"));
                        searchItemBinder.setAft_s_unit( o.optString("unit"));

                        searchItemBinder.setAft_s_materialId( o.optString("materialId"));
                        searchItemBinder.setAft_s_materialTypeId(o.optString("materialTypeId"));
                        searchItemBinder.setAft_s_id(o.optString("id"));

                        if (SharedPrefManager.getLangId(getActivity(),RequestCode.LangId).compareTo("hi") == 0) {
                            for (Map.Entry<String, String> entry : Book_Later.subtype_and_unit.entrySet()) {
//                                Log.d("Sub_type_product", entry.getKey() + "=" + entry.getValue());
                                if (o.optString("subType").equalsIgnoreCase(entry.getKey())){
                                    searchItemBinder.setAft_s_subtype(entry.getValue());
                                }
                            }
                            for (Map.Entry<String, String> entry : Book_Later.subtype_and_unit.entrySet()) {
                                if (o.optString("unit").equalsIgnoreCase(entry.getKey())){
                                    searchItemBinder.setAft_s_unit_show( entry.getValue());
                                }
                            }
                        } else {
                            searchItemBinder.setAft_s_subtype(o.optString("subType"));
                            searchItemBinder.setAft_s_unit_show( o.optString("unit"));
                        }

                        searchItemBinder.setListdata(listdata);

                        sub_product.add(searchItemBinder);
                    }
                    mAdapter.notifyDataSetChanged();
//                    mAdapter = new After_Search_Adapter(after_search_product, getContext());
//                    recyclerView_after.setAdapter(mAdapter);

                }
                else {
                    if (aSwitch.isChecked()){
                        aSwitch.setText(R.string.with_price);
                    }else {
                        aSwitch.setText(R.string.without_price);
                    }
                    recyclerView_after.setVisibility(View.GONE);
                    list_empty.setVisibility(View.VISIBLE);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (taskcode == RequestCode.CODE_Sub_ProductHide) {
            try {
                sub_product.clear();
                JSONObject jsonObject = new JSONObject(response);
                JSONObject successObj = jsonObject.optJSONObject("success");
                JSONArray jsonArray = successObj.optJSONArray("data");
//                if (sub_product.size()<=jsonArray.length()) {
//                    sub_product.add(null);
//                    mAdapter.notifyItemInserted(sub_product.size() - 1);
//                    progressBar.setVisibility(View.GONE);
//                    sub_product.remove(sub_product.size() - 1);
//                    mAdapter.notifyItemRemoved(sub_product.size());
//                    int index = sub_product.size();
//                    int end = index + 3;
                for (int i = 0; i < jsonArray.length(); i++) {
                    SearchItemBinder searchItemBinder=new SearchItemBinder();
                    JSONObject o = jsonArray.optJSONObject(i);

                    JSONObject locationObj = o.optJSONObject("location");
                    JSONObject addressObj = o.optJSONObject("address");
                    JSONObject materialObj = o.optJSONObject("material");
                    JSONObject materialTypeObj = o.optJSONObject("materialType");
                    JSONArray unitsArray = materialObj.getJSONArray("units");
                    ArrayList<String> listdata = new ArrayList<>();
                    for (int j = 0; j < unitsArray.length(); j++) {
                        listdata.add(unitsArray.optString(j));
                    }
                    searchItemBinder.setAft_s_name(o.optString("name"));
                    searchItemBinder.setAfter_s_img(o.optString("image"));
                    searchItemBinder.setAft_s_lat(locationObj.optString("lat"));
                    searchItemBinder.setAft_s_lng(locationObj.optString("lng"));
                    searchItemBinder.setAft_s_street(addressObj.optString("street"));
                    searchItemBinder.setAft_s_formattedAddress(addressObj.optString("formattedAddress"));
                    searchItemBinder.setAft_s_mat_name(materialObj.optString("name"));
                    searchItemBinder.setAft_s_mat_id(materialObj.optString("id"));
                    searchItemBinder.setAft_s_mat_type_name(materialTypeObj.optString("name"));
                    searchItemBinder.setAft_s_mat_type_id( materialTypeObj.optString("id"));
                    searchItemBinder.setAft_s_price(o.optString("price"));
//                    searchItemBinder.setAft_s_unit( o.optString("unit"));
                    searchItemBinder.setAft_s_materialId( o.optString("materialId"));
                    searchItemBinder.setAft_s_materialTypeId(o.optString("materialTypeId"));
                    searchItemBinder.setAft_s_id(o.optString("id"));
                    searchItemBinder.setAft_s_unit( o.optString("unit"));

                    if (SharedPrefManager.getLangId(getActivity(),RequestCode.LangId).compareTo("hi") == 0) {
                        for (Map.Entry<String, String> entry : Book_Later.subtype_and_unit.entrySet()) {
//                            Log.d("Sub_type_product", entry.getKey() + "=" + entry.getValue());
                            if (o.optString("subType").equalsIgnoreCase(entry.getKey())){
                                searchItemBinder.setAft_s_subtype(entry.getValue());
                            }
                        }
                        for (Map.Entry<String, String> entry : Book_Later.subtype_and_unit.entrySet()) {
                            if (o.optString("unit").equalsIgnoreCase(entry.getKey())){
                                searchItemBinder.setAft_s_unit_show( entry.getValue());
                            }
                        }
                    } else {
                        searchItemBinder.setAft_s_subtype(o.optString("subType"));
                        searchItemBinder.setAft_s_unit_show( o.optString("unit"));
                    }

                    searchItemBinder.setListdata(listdata);

                    sub_product.add(searchItemBinder);
                }
                mAdapterH.notifyDataSetChanged();
//                    mAdapter = new After_Search_Adapter(after_search_product, getContext());
//                    recyclerView_after.setAdapter(mAdapter);

//                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onDestroyView() {
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.book_latter);
        super.onDestroyView();
    }
}
