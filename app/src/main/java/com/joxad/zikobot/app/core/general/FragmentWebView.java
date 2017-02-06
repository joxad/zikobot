package com.joxad.zikobot.app.core.general;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.joxad.zikobot.app.R;
import com.joxad.zikobot.app.core.utils.EXTRA;
import com.orhanobut.logger.Logger;

import lombok.Setter;

/**
 * Created by josh on 10/06/16.
 */
public class FragmentWebView extends BottomSheetDialogFragment {

    public static final String TAG = FragmentWebView.class.getSimpleName();
    String url;

    WebView webView;
    ProgressBar progressBar;
    @Setter
    IntentListener intentListener;

    public static FragmentWebView newInstance(String url) {
        FragmentWebView fragment = new FragmentWebView();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA.URL, url);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        dialog.setOnShowListener(dialog1 -> {
            BottomSheetDialog d = (BottomSheetDialog) dialog1;

            FrameLayout bottomSheet = (FrameLayout) d.findViewById(android.support.design.R.id.design_bottom_sheet);
            BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);
        });
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_web_view, null, false);
        webView = (WebView) view.findViewById(R.id.web_view);
        progressBar = (ProgressBar) view.findViewById(R.id.progress);

        url = getArguments().getString(EXTRA.URL);
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

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                new Handler(Looper.getMainLooper()).post(() -> progressBar.setVisibility(View.GONE));
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
        void onReceive(Intent intent);
    }
}
