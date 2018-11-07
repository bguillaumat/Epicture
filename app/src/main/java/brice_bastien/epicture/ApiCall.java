package brice_bastien.epicture;

import android.content.Context;
import android.util.Base64;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

class ApiCall {

    private String username;
    private String client_id;
    private String token;

    ApiCall(String username, String client_id, String token) {
        this.client_id = client_id;
        this.username = username;
        this.token = token;
    }

    void uploadImg() {
        //https://api.imgur.com/3/image
    }

    void getUserImg(final Context context) {
        String url = "https://api.imgur.com/3/account/" + username + "/images";


        RequestQueue queue = Volley.newRequestQueue(context);

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(context, response.toString(), Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {

            /**
             * Passing some request headers
             */
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=UTF-8");
                String credentials = "Bearer " + token;
                headers.put("Authorization", credentials);
                return headers;
            }
        };

        queue.add(req);

    }

    void getRecentImg() {

    }

    void searchImg() {

    }

    void getFavorites() {
        //https://api.imgur.com/3/account/{{username}}/favorites/{{page}}/{{favoritesSort}}
    }

}
