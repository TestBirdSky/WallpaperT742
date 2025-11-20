package wall;

import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;


/**
 * Date：2025/7/28
 * Describe:
 */
public class Wcc1 extends WebChromeClient {
    @Override
    public void onProgressChanged(WebView webView, int i10) {
        super.onProgressChanged(webView, i10);
        if (i10 == 100) {
            A.d(i10);
        }
    }
}
