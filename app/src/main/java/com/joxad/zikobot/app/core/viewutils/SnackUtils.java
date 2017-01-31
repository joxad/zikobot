package com.joxad.zikobot.app.core.viewutils;

import android.graphics.Typeface;
import android.support.annotation.StringRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.github.andrewlord1990.snackbarbuilder.SnackbarBuilder;
import com.github.andrewlord1990.snackbarbuilder.callback.SnackbarDismissCallback;
import com.joxad.zikobot.app.R;

public class SnackUtils {

    public static void showCancelable(View view, @StringRes int message, View.OnClickListener actionClickListener, SnackbarDismissCallback dismissCallback) {
        Snackbar snackbar = new SnackbarBuilder(view)
                .backgroundColorRes(R.color.colorPrimary)
                .message(message)
                .actionTextColor(ContextCompat.getColor(view.getContext(), android.R.color.white))
                .actionText(R.string.cancel)
                .actionClickListener(actionClickListener)
                .dismissCallback(dismissCallback)
                .build();
        View snackbarView = snackbar.getView();
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) snackbarView.getLayoutParams();
        params.bottomMargin = view.getContext().getResources().getDimensionPixelSize(R.dimen.view_player_height);
        snackbarView.setLayoutParams(params);
        TextView snackbarActionTextView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_action);
        snackbarActionTextView.setTypeface(snackbarActionTextView.getTypeface(), Typeface.BOLD);

        snackbar.show();
    }

    public static void showConfirm(View view, @StringRes int message) {
        Snackbar snackbar = new SnackbarBuilder(view)
                .backgroundColorRes(R.color.colorPrimary)
                .message(message)
                .build();
        View snackbarView = snackbar.getView();
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) snackbarView.getLayoutParams();
        params.bottomMargin = view.getContext().getResources().getDimensionPixelSize(R.dimen.view_player_height);
        snackbarView.setLayoutParams(params);
        snackbar.show();
    }
}
