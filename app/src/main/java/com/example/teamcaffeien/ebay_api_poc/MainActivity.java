package com.example.teamcaffeien.ebay_api_poc;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    private Button btnSearch;
    private EditText edtItem;
    private TextView txtResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSearch = (Button) findViewById(R.id.btnSearch);
        edtItem = (EditText) findViewById(R.id.edtItem);
        txtResult = (TextView) findViewById(R.id.txtResult);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String item = edtItem.getText().toString();
                try {
                    searchEbayFor(item);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void searchEbayFor(String item) throws IOException {
        if (item.isEmpty()) {
            return;
        }
        String keywords = URLEncoder.encode(item, "UTF-8");

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://svcs.ebay.com/services/search/FindingService/v1?OPERATION-NAME=findItemsByKeywords&SERVICE-VERSION=1.0.0&SECURITY-APPNAME=Alexande-HotSwap-PRD-a134e8f72-c39570f3&RESPONSE-DATA-FORMAT=JSON&keywords=" + keywords)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                // do nothing, POC
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String jsonData = response.body().string();
                Gson gson = new Gson();

                JsonObject jsonObject = gson.fromJson(jsonData, JsonObject.class);
                final String price = jsonObject.getAsJsonArray("findItemsByKeywordsResponse").get(0).getAsJsonObject().getAsJsonArray("searchResult").get(0).getAsJsonObject().getAsJsonArray("item").get(0).getAsJsonObject().getAsJsonArray("sellingStatus").get(0).getAsJsonObject().getAsJsonArray("currentPrice").get(0).getAsJsonObject().get("__value__").getAsString();

                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txtResult.setText(price);
                    }
                });

            }
        });
    }

  /*  Request request = new Request.Builder()
            .url("http://svcs.ebay.com/services/search/FindingService/v1?OPERATION-NAME=findItemsByKeywords&SERVICE-VERSION=1.0.0&SECURITY-APPNAME=Alexande-HotSwap-PRD-a134e8f72-c39570f3&RESPONSE-DATA-FORMAT=JSON&REST-PAYLOAD=&keywords=harry%20potter%20phoenix")
            .get()
            .addHeader("cache-control", "no-cache")
            .addHeader("postman-token", "9243952e-9c6d-de60-be90-d782c3c65578")
            .build();

    Response response = client.newCall(request).execute();
*/
}


