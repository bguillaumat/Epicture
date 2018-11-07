package brice_bastien.epicture;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;



public class LoginActivity extends AppCompatActivity {

	private String Token = "";
    private String Username = "";
	private String Etc = "";
	Storage store = new Storage();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		try {
		    String[] result = store.openData(this);
		    Token = result[0];
		    Username = result[1];
		    Etc = result[2];
			Intent intent = new Intent(getApplicationContext(), MainActivity.class);
		    intent.putExtra("ACCESS_TOKEN", Token);
            intent.putExtra("ACCOUNT_USERNAME", Username);
		    finish();
		    startActivity(intent);
		    return;
		} catch (IOException ignored) {
		    onStop();
		}

		final WebView webView = findViewById(R.id.webView);
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				if (url.startsWith("https://imgur.com/#")) {
					Uri uri = Uri.parse(url.replace("#", "?"));
					Set<String> args = uri.getQueryParameterNames();
					Map<String, String> parameters = new HashMap<>();
					for (String s : args) {
						String s1 = uri.getQueryParameter(s);
						parameters.put(s, s1);
					}
					if (parameters.containsKey("access_token")) {
						Intent intent = new Intent(getApplicationContext(), MainActivity.class);
						Token = parameters.get("access_token");
                        Username = parameters.get("account_username");
						intent.putExtra("ACCESS_TOKEN", Token);
                        intent.putExtra("ACCOUNT_USERNAME", Username);
						finish();
						startActivity(intent);
						return;
					}
				}
				super.onPageStarted(view, url, favicon);
			}
		});
		store.closeData(this, Token, Username, Etc);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setDomStorageEnabled(true);
		webView.loadUrl("https://api.imgur.com/oauth2/authorize?client_id=8c94575ba123f37&response_type=token");

	}

    @Override
    protected void onPause() {
        store.closeData(this, Token, Username, Etc);
        super.onPause();
    }
}