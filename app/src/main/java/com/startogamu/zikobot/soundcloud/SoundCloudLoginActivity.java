package com.startogamu.zikobot.soundcloud;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.orhanobut.logger.Logger;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.core.utils.AppPrefs;
import com.startogamu.zikobot.core.module.soundcloud.manager.SoundCloudApiManager;


public class SoundCloudLoginActivity extends AppCompatActivity {

    private static final String TAG = SoundCloudLoginActivity.class.getSimpleName();

    //todo - create a project in the SoundCloud developer portal: https://soundcloud.com/you/apps/
    private static final String STATE = SoundCloudLoginActivity.class.getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_soundcloud);

        // clear the cookies to make sure the that the user is properly logged out
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            final CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.removeAllCookies(null);
            cookieManager.flush();
        } else {
            CookieSyncManager.createInstance(getApplicationContext()).startSync();
            final CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.removeAllCookie();
            cookieManager.removeSessionCookie();
        }

        // SoundCloud oauth url
        final Uri authUri = new Uri.Builder().scheme("https")
                .authority("soundcloud.com")
                .appendPath("connect")
                .appendQueryParameter("scope", "non-expiring")
                .appendQueryParameter("response_type", "code")
                .appendQueryParameter("state", STATE)
                .appendQueryParameter("display", "popup")
                .appendQueryParameter("client_id", getString(R.string.soundcloud_id))
                .appendQueryParameter("redirect_uri", getString(R.string.api_soundcloud_callback))
                .build();

        Log.d(TAG, "https://soundcloud.com/connect?scope=non-expiring&response_type=code&state=boxset.SoundCloudLoginActivity&display=popup&client_id=6d483c5c02062da985379c36b5e7da95&redirect_uri=http%3A%2F%2Fwonder.fm%2Fincoming%2Fsoundcloud%2Fauth%2F");
        Log.d(TAG, authUri.toString());

        // we need a handle to this to add the second webview during google plus login
        final FrameLayout container = (FrameLayout) findViewById(R.id.container);

        // progress hud adds itself to the view hierarchy
        final LoadingHud loadingHud = new LoadingHud(container);

        final WebView webView = createWebView(this);
        webView.loadUrl(authUri.toString());

        final WebViewClient webViewClient = new WebViewClient() {

            // need to use the depricated method if you are supporting less than api 21
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                //GUARD - been stung by this
                if (url == null) return false;

                //GUARD - check if we have got our callback url yet
                // this occurs when navigating to facebook and google plus login screens
                if (!url.contains(getString(R.string.api_soundcloud_callback))) return false;

                final Uri uri = Uri.parse(url);

                //GUARD
                // the state query parameter is echoed back to us so we
                // know that the code is coming from a legitimate source
                final String state = uri.getQueryParameter("state");
                if (!STATE.equals(state)) return false;

                //GUARD
                final String code = uri.getQueryParameter("code");
                if (code == null) {
                    // something went wrong during the auth process
                    // you need to handle this
                    Log.d(TAG, "No code returned from auth process");
                    return false;
                }
                AppPrefs.saveSoundCloudAccessCode(code);
                getSoundCloudTokenFromCode();

                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                loadingHud.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                loadingHud.hide(true);
            }
        };
        webView.setWebViewClient(webViewClient);

        // require for google login
        // google login requires that the SoundCloud login window be open at the same time
        // as it uses cross window/site javascript injection to pass information back to
        // SoundCloud on completion
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture,
                                          Message resultMsg) {

                // this WebView has to has the same settings as the original for
                // the cross site javascript injection to work
                final WebView googleSignInWebView = createWebView(view.getContext());
                googleSignInWebView.setWebChromeClient(this);
                googleSignInWebView.setWebViewClient(webViewClient);

                container.addView(googleSignInWebView);

                // this is the glue code that wires the original webview
                // and the new webview together so they can communicate
                final WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
                transport.setWebView(googleSignInWebView);
                resultMsg.sendToTarget();

                // this advises that we have actually created and displayed the new window
                return true;
            }

            // since we added the window we also have to handle removing it
            @Override
            public void onCloseWindow(WebView window) {
                container.removeView(window);
            }
        });

        container.addView(webView);

    }


    /**
     * @param context the WebView must be given an activity context (instead of application context)
     *                or it will crash in versions less than 4.4
     * @return a {@link WebView} suitable for the soundcloud login process
     */
    private static WebView createWebView(Context context) {
        final WebView webView = new WebView(context);

        final WebSettings settings = webView.getSettings();

        // this allows the username and password validation to work
        settings.setJavaScriptEnabled(true);

        // these 2 are for login with google support
        // which needs to open a second window
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setSupportMultipleWindows(true);

        // prevent caching of user data
        settings.setSaveFormData(false);

        // prevents the webview asking the user if they want to save their password
        // needed for pre 18 devices
        settings.setSavePassword(false);

        return webView;
    }

    private static class LoadingHud {

        private final RelativeLayout container;

        public LoadingHud(ViewGroup parentView) {
            container = new RelativeLayout(parentView.getContext());
            container.setAlpha(0);
            parentView.addView(container);
            final ViewGroup.LayoutParams layoutParams = container.getLayoutParams();
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            container.setLayoutParams(layoutParams);

            addMask(container);
            addProgressBar(container);
        }

        private void addMask(RelativeLayout container) {
            final View view = new View(container.getContext());
            view.setBackgroundColor(Color.WHITE);
            view.setAlpha(.5f);
            container.addView(view);
            final RelativeLayout.LayoutParams layoutParams =
                    (RelativeLayout.LayoutParams) view.getLayoutParams();
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            view.setLayoutParams(layoutParams);
        }

        private void addProgressBar(RelativeLayout container) {

            final ProgressBar progressBar = new ProgressBar(container.getContext());
            container.addView(progressBar);
            final RelativeLayout.LayoutParams layoutParams =
                    (RelativeLayout.LayoutParams) progressBar.getLayoutParams();
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            progressBar.setLayoutParams(layoutParams);
        }

        void show() {
            container.bringToFront();
            container.animate().alpha(1f).start();
        }

        void hide(Boolean animated) {
            Float noAlpha = 0f;
            if (animated) {
                container.animate().alpha(noAlpha).start();
            } else {
                container.setAlpha(noAlpha);
            }
        }
    }

    private void getSoundCloudTokenFromCode() {
        String appId = getString(R.string.soundcloud_id);
        String appSecret = getString(R.string.soundcloud_secret);
        String redirectUrl = getString(R.string.api_soundcloud_callback);
        SoundCloudApiManager.getInstance().token(appId, appSecret, redirectUrl, AppPrefs.getSoundCloundAccesCode(), "authorization_code")
                .subscribe(soundCloudToken -> {
                    AppPrefs.saveSoundCloudAccessToken(soundCloudToken.getAccessToken());
                    finish();
                }, throwable -> {
                    Logger.e(throwable.getMessage());
                });
    }
}