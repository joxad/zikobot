package com.startogamu.zikobot.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.f2prateek.dart.Dart;
import com.f2prateek.dart.InjectExtra;
import com.f2prateek.dart.henson.Bundler;
import com.orhanobut.logger.Logger;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.core.utils.EXTRA;

/**
 * Created by josh on 10/06/16.
 */
public class FragmentWebView extends DialogFragment {

    public static final String TAG = FragmentWebView.class.getSimpleName();
    @InjectExtra(EXTRA.URL)
    String url;

    WebView webView;

    public static FragmentWebView newInstance(String url) {
        FragmentWebView fragment = new FragmentWebView();
        fragment.setArguments(Bundler.create().put(EXTRA.URL, url).get());
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        webView = (WebView) inflater.inflate(R.layout.fragment_web_view, container, false);
        Dart.inject(this, getArguments());
        WebChromeClient webChromeClient = new WebChromeClient();
        WebViewClient webViewClient = new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {

                if (url.equals(getContext().getString(R.string.api_soundcloud_callback))) {
                    Logger.d("Callback!!!");
                }
                super.onPageFinished(view, url);
            }
        };
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebChromeClient(webChromeClient);
        webView.setWebViewClient(webViewClient);

        webView.loadUrl(url);
        return webView;
    }

    public void onBackPressed() {
        webView.goBack();
    }
}
