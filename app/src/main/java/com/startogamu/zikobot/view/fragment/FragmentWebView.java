package com.startogamu.zikobot.view.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.f2prateek.dart.Dart;
import com.f2prateek.dart.InjectExtra;
import com.f2prateek.dart.henson.Bundler;
import com.orhanobut.logger.Logger;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.core.utils.EXTRA;

import lombok.Setter;

/**
 * Created by josh on 10/06/16.
 */
public class FragmentWebView extends BottomSheetDialogFragment {

    public static final String TAG = FragmentWebView.class.getSimpleName();
    @InjectExtra(EXTRA.URL)
    String url;

    WebView webView;

    @Setter
    IntentListener intentListener;

    public static FragmentWebView newInstance(String url) {
        FragmentWebView fragment = new FragmentWebView();
        fragment.setArguments(Bundler.create().put(EXTRA.URL, url).get());
        return fragment;
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        webView = (WebView) LayoutInflater.from(getContext()).inflate(R.layout.fragment_web_view, null, false);
        Dart.inject(this, getArguments());
        WebViewClient webViewClient = new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains(getContext().getString(R.string.api_spotify_callback_web_view))) {
                    Intent spotifyIntent = new Intent(Intent.ACTION_VIEW);
                    spotifyIntent.setData(Uri.parse(url));
                    intentListener.onReceive(spotifyIntent);
                    Logger.d("Callback spotify");
                    return true;
                }
                if (url.contains(getContext().getString(R.string.api_soundcloud_callback))) {
                    Intent soundcloudIntent = new Intent(Intent.ACTION_VIEW);
                    soundcloudIntent.setData(Uri.parse(url));
                    intentListener.onReceive(soundcloudIntent);
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, url);
            }
        };
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setSupportMultipleWindows(true);
        webView.setWebViewClient(webViewClient);

        webView.loadUrl(url);
        dialog.setContentView(webView);

    }


    public void onBackPressed() {
        webView.goBack();
    }


    public interface IntentListener {
        public void onReceive(Intent intent);
    }
}
