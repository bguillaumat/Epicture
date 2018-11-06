package brice_bastien.epicture;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.Set;

public class MyWebViewClient extends WebViewClient {
	@Override
	public void onPageStarted(WebView view, String url, Bitmap favicon) {
		Log.d("MyActivity", url);

		String tmp = url.replace("#", "?");
		Uri uri = Uri.parse(tmp);
		Set<String> args = uri.getQueryParameterNames();
		for (String s : args) {
			Log.d("MyActivity", s);
		}
		super.onPageStarted(view, url, favicon);
	}
}
