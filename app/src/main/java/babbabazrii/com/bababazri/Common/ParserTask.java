package babbabazrii.com.bababazri.Common;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Advosoft2 on 2/16/2018.
 */

public class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
    ParserCallback callback;
    DirectionsJSONParser parser;

    public ParserTask(ParserCallback callback) {
        this.callback = callback;
        parser = new DirectionsJSONParser();
    }

    // Parsing the data in non-ui thread
    @Override
    protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

        JSONObject jObject;
        List<List<HashMap<String, String>>> routes = null;

        try {
            jObject = new JSONObject(jsonData[0]);
            parser = new DirectionsJSONParser();

            routes = parser.parse(jObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return routes;
    }

    @Override
    protected void onPostExecute(List<List<HashMap<String, String>>> result) {

        callback.onParserResult(result, parser);


    }

    public interface ParserCallback {
        void onParserResult(List<List<HashMap<String, String>>> result, DirectionsJSONParser parser);
    }
}