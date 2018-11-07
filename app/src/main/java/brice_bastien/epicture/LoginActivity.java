package brice_bastien.epicture;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;



public class LoginActivity extends AppCompatActivity {

	private String Token = "";
	private String Etc = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		try {
		    openData();
		    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
		    intent.putExtra("ACCESS_TOKEN", Token);
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
						closeData();
						intent.putExtra("ACCESS_TOKEN", parameters.get("access_token"));
						finish();
						startActivity(intent);
						return;
					}
				}
				super.onPageStarted(view, url, favicon);
			}
		});
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setDomStorageEnabled(true);
		webView.loadUrl("https://api.imgur.com/oauth2/authorize?client_id=8c94575ba123f37&response_type=token");

	}

    @Override
    protected void onPause() {
        closeData();
        super.onPause();
    }

    private void openData() throws IOException {
        String FILENAME = "data";
        String token = "";
        String etc = "";
        FileInputStream fos = openFileInput(FILENAME);
        Scanner sc = new Scanner(fos);
        if (sc.hasNextLine()) token = sc.nextLine();
        if (sc.hasNextLine()) etc = sc.nextLine();
        fos.close();
        Token = token;
        Etc = etc;
    }

    private void closeData() {
        String FILENAME = "data";
        String token = Token;
        String etc = Etc;
        String finalStr = (token + '\n' + etc + '\n');
        try (FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE)) {
            fos.write(finalStr.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}