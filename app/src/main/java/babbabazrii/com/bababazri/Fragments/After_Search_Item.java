package babbabazrii.com.bababazri.Fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

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
public class After_Search_Item extends Fragment implements WebCompleteTask, View.OnClickListener {

    TextView nearby_location_tv;
    Double lat, lng;
    private static final String TAG = "After Search Item";

    private RecyclerView recyclerView_after;
    private ArrayList<SearchItemBinder>  after_search_product = new ArrayList<>();
    After_Search_Adapter mAdapter;
    After_Search_AdapterHide mAdapterH;
    LinearLayout after_search_nearby_lin;
    Context mCtx;
    private static String add;

    private ArrayList<String> listdata = new ArrayList<String>();
    private boolean itShouldLoadMore = true;
    ProgressBar progressBar;
    EditText searchField_Aft_srch;
    SwipeRefreshLayout swipeRefreshLayout;
    SwitchCompat after_search_swtich;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_after__search__item, container, false);

        searchField_Aft_srch = (EditText)view.findViewById(R.id.search_et_after_search);
        recyclerView_after = (RecyclerView) view.findViewById(R.id.after_search_item_recycleview);
        recyclerView_after.setHasFixedSize(true);
        recyclerView_after.setLayoutManager(new LinearLayoutManager(getContext()));
        progressBar = (ProgressBar)view.findViewById(R.id.loader_ats);
        after_search_swtich = (SwitchCompat)view.findViewById(R.id.after_search_swtich);




        SharedPreferences sharedPrefere = getContext().getSharedPreferences("Address", Context.MODE_PRIVATE);
        SharedPreferences sharedPreferencesReg = getContext().getSharedPreferences("LatLngNearBy", Context.MODE_PRIVATE);
        lat = Double.valueOf(sharedPreferencesReg.getString("LATn", ""));
        lng = Double.valueOf(sharedPreferencesReg.getString("LNGn", ""));
        getAddress(lat, lng);

        nearby_location_tv = (TextView) view.findViewById(R.id.after_search_nearby_tv);
        nearby_location_tv.setText( sharedPrefere.getString("Address_key",""));
        after_search_nearby_lin = (LinearLayout) view.findViewById(R.id.after_search_nearby_lin);
        after_search_nearby_lin.setOnClickListener(this);

        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.pull_refresh_after_searchitem);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                if (after_search_swtich.isChecked()){
                    after_search_swtich.setText(R.string.with_price);
                    fetchAfterSearch();
                    recyclerView_after.setAdapter(mAdapter);
                }else {
                    after_search_swtich.setText(R.string.without_price);
                    fetchAfterSearchHide();
                    recyclerView_after.setAdapter(mAdapterH);
                }
            }
        });

        after_search_product.clear();
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

        mAdapter = new After_Search_Adapter(after_search_product, getActivity());
        mAdapterH = new After_Search_AdapterHide(after_search_product, getActivity());
//        recyclerView_after.setAdapter(mAdapter);
        //-----------------------------------------Searching prduct-------------------------------------
        searchField_Aft_srch.addTextChangedListener(new TextWatcher() {
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
                mAdapter.getFilter().filter(searchField_Aft_srch.getText());
                mAdapterH.getFilter().filter(searchField_Aft_srch.getText());
                //you can use runnable postDelayed like 500 ms to delay search text
            }
        });
        //-----------------------------------------Searching prduct-------------------------------------
        if (after_search_swtich.isChecked()){
            after_search_swtich.setText(R.string.with_price);
            fetchAfterSearch();
            recyclerView_after.setAdapter(mAdapter);
        }else {
            after_search_swtich.setText(R.string.without_price);
            fetchAfterSearchHide();
            recyclerView_after.setAdapter(mAdapterH);
        }
        //-----------------------------------------Searching prduct-------------------------------------
        after_search_swtich.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

//                Snackbar.make(buttonView, "Switch state checked "+isChecked, Snackbar.LENGTH_LONG)
//                        .setAction("ACTION",null).show();
                if (after_search_swtich.isChecked()){
                    after_search_swtich.setText(R.string.with_price);
                    fetchAfterSearch();
                    recyclerView_after.setAdapter(mAdapter);
                }else {
                    after_search_swtich.setText(R.string.without_price);
                    fetchAfterSearchHide();
                    recyclerView_after.setAdapter(mAdapterH);
                }
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar().setTitle(R.string.near_by_product);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.after_search_nearby_lin:
                Intent intent = new Intent(getActivity(), Choose_Location.class);
                startActivity(intent);
        }
    }

//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//    }

    public void getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            add = obj.getAddressLine(0);
//            add = add + "\n" + obj.getCountryName();
//            add = add + "\n" + obj.getCountryCode();
//            add = add + "\n" + obj.getAdminArea();
//            add = add + "\n" + obj.getPostalCode();
//            add = add + "\n" + obj.getSubAdminArea();
//            add = add + "\n" + obj.getLocality();
//            add = add + "\n" + obj.getSubThoroughfare();
//            add = add + "\n" + obj.getAdminArea();
//            add = add + "\n" + obj.getFeatureName();
//            add = add + "\n" + obj.getPhone();

            Log.v(TAG, "Address: " + add);
            // Toast.makeText(this, "Address=>" + add,
            // Toast.LENGTH_SHORT).show();

            // TennisAppActivity.showDialog(add);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(this.mCtx, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private void fetchAfterSearch() {
        HashMap objectNew = new HashMap();

        JSONObject location = new JSONObject();
        try {
            location.put("lat", lat);
            location.put("lng", lng);
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        progressBar.setVisibility(View.VISIBLE);
        new WebTask(getActivity(), WebUrls.BASE_URL + WebUrls.afterSearchProduct_api +"location="+ URLEncoder.encode(location.toString()), objectNew, After_Search_Item.this, RequestCode.CODE_AfterSearch, 3);
    }
    private void fetchAfterSearchHide() {
        HashMap objectNew = new HashMap();
        JSONObject location = new JSONObject();
        try {
            location.put("lat", lat);
            location.put("lng", lng);
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        progressBar.setVisibility(View.VISIBLE);
        new WebTask(getActivity(), WebUrls.BASE_URL + WebUrls.afterSearchProduct_api +"location="+ URLEncoder.encode(location.toString()), objectNew, After_Search_Item.this, RequestCode.CODE_AfterSearchHide, 3);
    }
    @Override
    public void onComplete(String response, int taskcode) {
        android.util.Log.d(" AftS response", response);
        if (taskcode == RequestCode.CODE_AfterSearch) {
            try {
                after_search_product.clear();
                JSONObject jsonObject = new JSONObject(response);
                JSONObject successObj = jsonObject.optJSONObject("success");
                JSONArray jsonArray = successObj.optJSONArray("data");
//                if (after_search_product.size()<=jsonArray.length()) {
//                    after_search_product.add(null);
//                    mAdapter.notifyItemInserted(after_search_product.size() - 1);
//                    progressBar.setVisibility(View.GONE);
//                    after_search_product.remove(after_search_product.size() - 1);
//                    mAdapter.notifyItemRemoved(after_search_product.size()-1);
//                    int index = after_search_product.size();
//                    int end = index + 3;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        SearchItemBinder searchItemBinder=new SearchItemBinder();
                        JSONObject o = jsonArray.optJSONObject(i);
                        JSONObject locationObj = o.optJSONObject("location");
                        JSONObject addressObj = o.optJSONObject("address");
                        JSONObject materialObj = o.optJSONObject("material");
                        JSONObject materialTypeObj = o.optJSONObject("materialType");
                        JSONArray unitsArray = materialObj.getJSONArray("units");
                       // listdata.clear();
                        ArrayList<String> list_new=new ArrayList<>();
                        for (int j = 0; j < unitsArray.length(); j++) {
                           // listdata.add(unitsArray.optString(j));
                            list_new.add(unitsArray.optString(j));
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
//                        searchItemBinder.setAft_s_subtype(o.optString("subType"));

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

                        searchItemBinder.setListdata(list_new);
                        after_search_product.add(searchItemBinder);

                    }
                    mAdapter.notifyDataSetChanged();

//                    mAdapter = new After_Search_Adapter(after_search_product, getContext());
//                    recyclerView_after.setAdapter(mAdapter);

//                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (taskcode == RequestCode.CODE_AfterSearch) {
            try {
                after_search_product.clear();
                JSONObject jsonObject = new JSONObject(response);
                JSONObject successObj = jsonObject.optJSONObject("success");
                JSONArray jsonArray = successObj.optJSONArray("data");
//                if (after_search_product.size()<=jsonArray.length()) {
//                    after_search_product.add(null);
//                    mAdapter.notifyItemInserted(after_search_product.size() - 1);
//                    progressBar.setVisibility(View.GONE);
//                    after_search_product.remove(after_search_product.size() - 1);
//                    mAdapter.notifyItemRemoved(after_search_product.size()-1);
//                    int index = after_search_product.size();
//                    int end = index + 3;
                for (int i = 0; i < jsonArray.length(); i++) {
                    SearchItemBinder searchItemBinder=new SearchItemBinder();
                    JSONObject o = jsonArray.optJSONObject(i);
                    JSONObject locationObj = o.optJSONObject("location");
                    JSONObject addressObj = o.optJSONObject("address");
                    JSONObject materialObj = o.optJSONObject("material");
                    JSONObject materialTypeObj = o.optJSONObject("materialType");
                    JSONArray unitsArray = materialObj.getJSONArray("units");
                    // listdata.clear();
                    ArrayList<String> list_new=new ArrayList<>();
                    for (int j = 0; j < unitsArray.length(); j++) {
                        // listdata.add(unitsArray.optString(j));
                        list_new.add(unitsArray.optString(j));
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
//                    searchItemBinder.setAft_s_subtype(o.optString("subType"));
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
                    searchItemBinder.setListdata(list_new);
                    after_search_product.add(searchItemBinder);

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


}
