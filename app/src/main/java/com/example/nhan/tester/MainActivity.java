package com.example.nhan.tester;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends ActionBarActivity {
    private static final String API_KEY = "AIzaSyAiSOOzgTo9ItnSRQ1TRJI1FHOGJTZg7es";
    private static final String ROOT_URL = "https://maps.googleapis.com/maps/api/geocode/json?";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void callAPI(View view) {
        EditText address = (EditText) findViewById(R.id.inputAddress);
        String addressStr = address.getText().toString();

        // Hiding soft keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(address.getWindowToken(), 0);
        
        // Now call the API
        if (addressStr != null && !addressStr.isEmpty()) {
            new CallAPI().execute(addressStr);
        }
    }

    private class CallAPI extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String resultToDisplay = "";
            List<NameValuePair> queryParams = new ArrayList<>();
            queryParams.add(new BasicNameValuePair("key", API_KEY));
            queryParams.add(new BasicNameValuePair("address", params[0]));

            String urlString = ROOT_URL + URLEncodedUtils.format(queryParams, "UTF-8");

            try {
                URL url = new URL(urlString);

                ObjectMapper mapper = new ObjectMapper();
                JsonNode jNode = mapper.readTree(url);

                String status = jNode.get("status").asText();
                System.out.println(jNode.get("status").asText());
                switch (status) {
                    case "OK":
                        JsonNode data = jNode.get("results");
                        Iterator<JsonNode> it = data.iterator();
                        // Only care about the first result
                        if(it.hasNext()) {
                            JsonNode currentResult = it.next();
                            String formattedAddress = currentResult.get("formatted_address").asText();

                            JsonNode location = currentResult.get("geometry").get("location");
                            String latLong = location.get("lat").asText() + " " +
                                             location.get("lng").asText();


                            resultToDisplay += "Formatted Address: " + formattedAddress;
                            resultToDisplay += "\n";
                            resultToDisplay += "Lat Long: " + latLong;

                            System.out.println("Formatted Address: " + formattedAddress);
                            System.out.println("Lat Long: " + latLong);
                        }

                        break;
                    default:
                        System.out.println("Status is not okay" + status);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return resultToDisplay;
        }

        @Override
        protected void onPostExecute(String result) {
            TextView resultView = (TextView) findViewById(R.id.resultStr);
            resultView.setText(result);
        }
    }
}
