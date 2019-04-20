package babbabazrii.com.bababazri.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import babbabazrii.com.bababazri.R;

public class GooglePlacesAutocompleteAdapter extends RecyclerView.Adapter<GooglePlacesAutocompleteAdapter.PredictionHolder> implements Filterable {
    private static final String LOG_TAG = "Places Autocomplete";
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";
    private static final String API_KEY = "your_api_key";


    private ArrayList<AT_Place> myResultList;
    private GoogleApiClient myApiClient;
    private LatLngBounds myBounds;
    private AutocompleteFilter myACFilter;
    private Context mycontext;
    private int layout;


    public GooglePlacesAutocompleteAdapter(Context context,int resource,GoogleApiClient googleApiClient,
                                           LatLngBounds bounds,AutocompleteFilter filter) {
        mycontext = context;
        layout = resource;
        myApiClient = googleApiClient;
        myBounds = bounds;
        myACFilter = filter;
    }
    public void setMyBounds(LatLngBounds bounds){
        myBounds = bounds;
    }
    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    // Retrieve the autocomplete results.
                    myResultList = getAutocomplete(constraint.toString());

                    if (myResultList !=null){
                        // Assign the data to the FilterResults
                        filterResults.values = myResultList;
                        filterResults.count = myResultList.size();
                    }
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
//                    setImageVisibility();
                    notifyDataSetChanged();
                } else {
//                    notifyDataSetInvalidated();
                }
            }
        };
        return filter;
    }


    public ArrayList<AT_Place> getAutocomplete(CharSequence constraint) {

        if (myApiClient.isConnected()) {
            PendingResult<AutocompletePredictionBuffer> results =
                    Places.GeoDataApi
                            .getAutocompletePredictions(myApiClient, constraint.toString()
                                    , myBounds, myACFilter);
            AutocompletePredictionBuffer autocompletePredictions = results.await(60, TimeUnit.SECONDS);

            final Status status = autocompletePredictions.getStatus();
            if (!status.isSuccess()) {
                Toast.makeText(mycontext, "Error Contacting API: " + status.toString(), Toast.LENGTH_SHORT).show();
                Log.e("", "Error gettting autocomplete place Api Call: " + status.toString());
                autocompletePredictions.release();
                return null;
            }

            Log.i("", "Query completed Received " + autocompletePredictions.getCount()
                    + " predictions.");

            Iterator<AutocompletePrediction> iterator = autocompletePredictions.iterator();
            ArrayList resultList = new ArrayList(autocompletePredictions.getCount());
            while (iterator.hasNext()) {
                AutocompletePrediction prediction = iterator.next();
                // Get the details of this prediction and copy it into a new PlaceAutocomplete object.
                resultList.add(new AT_Place(prediction.getPlaceId(),
                        prediction.getPrimaryText(null),
                        prediction.getSecondaryText(null)));

            }
            autocompletePredictions.release();
            return resultList;
        }
        Log.e("","Google Api Client is not Connected for autocomplete query.");
        return null;

    }


    @NonNull
    @Override
    public PredictionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater)mycontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = layoutInflater.inflate(layout,parent,false);
        return new PredictionHolder(convertView);
    }

    @Override
    public void onBindViewHolder(@NonNull PredictionHolder holder, int position) {
        holder.address_1.setText(myResultList.get(position).placeAddress1);
        holder.address_2.setText(myResultList.get(position).placeAddress2);
    }

    @Override
    public int getItemCount() {
        if (myResultList !=null)
            return myResultList.size();
        else
            return 0;
    }

    public AT_Place getItem(int position){
        return myResultList.get(position);
    }

    public class PredictionHolder extends RecyclerView.ViewHolder {
        private TextView address_1,address_2;
        private RelativeLayout myRow;
        public PredictionHolder(View itemView) {
            super(itemView);

            address_1 = (TextView)itemView.findViewById(R.id.address_1);
            address_2 = (TextView)itemView.findViewById(R.id.address_2);
            myRow = (RelativeLayout)itemView.findViewById(R.id.autocomplete_row);
        }
    }

    public class AT_Place{
        public CharSequence placeId;
        public CharSequence placeAddress1,placeAddress2;

        AT_Place(CharSequence placeId,CharSequence description,CharSequence primarytext){
            this.placeId = placeId;
            this.placeAddress1 = description;
            this.placeAddress2 = primarytext;
        }

        public String getPlaceAddress1(){
            return placeAddress1.toString();
        }
        public String getPlaceAddress2(){
            return placeAddress2.toString();
        }
        public String getPlaceId(){
            return placeId.toString();
        }
    }
}
