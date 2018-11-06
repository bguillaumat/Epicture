package brice_bastien.epicture;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class LoginActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		final WebView webView = findViewById(R.id.webView);
		webView.setWebViewClient(new WebViewClient() {
									 @Override
									 public void onPageStarted(WebView view, String url, Bitmap favicon) {
										 String tmp = url.replace("#", "?");
										 Uri uri = Uri.parse(tmp);
										 Set<String> args = uri.getQueryParameterNames();
										 Map<String, String> parameters = new HashMap<>();
										 for (String s : args) {
											 String s1 = uri.getQueryParameter(s);
											 parameters.put(s, s1);
										 }
										 if (parameters.containsKey("access_token")) {
											 Intent intent = new Intent(getApplicationContext(), MainActivity.class);
											 intent.putExtra("ACCESS_TOKEN", parameters.get("access_token"));
											 startActivity(intent);
											 return;
										 }
										 super.onPageStarted(view, url, favicon);
									 }
								 });
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setDomStorageEnabled(true);
		webView.loadUrl("https://api.imgur.com/oauth2/authorize?client_id=8c94575ba123f37&response_type=token");

	}
}