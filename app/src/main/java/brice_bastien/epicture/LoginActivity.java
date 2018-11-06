package brice_bastien.epicture;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;
import android.widget.Toast;


public class LoginActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		final WebView webView = findViewById(R.id.webView);
		webView.setWebViewClient(new MyWebViewClient());
		webView.getSettings().setJavaScriptEnabled(true);
//		webView.getSettings().setDomStorageEnabled(true);
		webView.loadUrl("https://api.imgur.com/oauth2/authorize?client_id=8c94575ba123f37&response_type=token");
		Toast.makeText(this, webView.getUrl(), Toast.LENGTH_SHORT).show();

	}
}
