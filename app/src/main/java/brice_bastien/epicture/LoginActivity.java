package brice_bastien.epicture;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import androidx.appcompat.app.AppCompatActivity;


public class LoginActivity extends AppCompatActivity {

	SharedPreferences sharedPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		sharedPreferences = getSharedPreferences(getString(R.string.user_info_pref), Context.MODE_PRIVATE);
		if (sharedPreferences.contains("User_Token") && sharedPreferences.contains("Username")) {
			Intent intent = new Intent(getApplicationContext(), MainActivity.class);
			finish();
			startActivity(intent);
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
						if (s1 != null)
							parameters.put(s, s1);
					}
					if (parameters.containsKey("access_token") && parameters.containsKey("account_username")) {
						Intent intent = new Intent(getApplicationContext(), MainActivity.class);
						String Token = parameters.get("access_token");
						String Username = parameters.get("account_username");
						CookieManager cookieManager = CookieManager.getInstance();
						intent.putExtra("ACCESS_TOKEN", Token);
						intent.putExtra("ACCOUNT_USERNAME", Username);
						sharedPreferences.edit()
								.putString("User_Token", Token)
								.putString("Username", Username)
								.apply();
						cookieManager.removeAllCookies(null);
						finish();
						startActivity(intent);
						return;
					}
				}
				super.onPageStarted(view, url, favicon);
			}
		});
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			webView.getSettings().setSafeBrowsingEnabled(false);
		}
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setDomStorageEnabled(true);
		webView.clearCache(true);
		webView.clearFormData();
		webView.loadUrl("https://api.imgur.com/oauth2/authorize?client_id=" + BuildConfig.IMGUR_API_KEY + "&response_type=token");
	}

	@Override
	protected void onPause() {
		super.onPause();
		WebView webView = findViewById(R.id.webView);

		webView.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		WebView webView = findViewById(R.id.webView);

		webView.onResume();
	}
}