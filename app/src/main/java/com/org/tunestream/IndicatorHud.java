package com.org.tunestream;

import android.app.Activity;
import android.app.Application;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

public class IndicatorHud extends FrameLayout {

    private static IndicatorHud view;
    private final View loadingView;
    private final View backgroundView;

    public IndicatorHud(Activity activity) {
        super(activity);
        view = this;

        backgroundView = new View(activity);
        loadingView = new ProgressBar(activity, null, android.R.attr.progressBarStyleSmall);

        initBackgroundView();
        initLoadingView();
        initIndicatorView();
    }

    private void initBackgroundView() {
        backgroundView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        backgroundView.setBackgroundColor(Color.TRANSPARENT);
        backgroundView.setAlpha(0);
        addView(backgroundView);
    }

    private void initIndicatorView() {
        int imageSide = loadingView.getLayoutParams().width / 2;
        int imageX = loadingView.getLayoutParams().width / 2 - (imageSide / 2);
        int imageY = loadingView.getLayoutParams().height / 2 - (imageSide / 2);

        loadingView.setLayoutParams(new LayoutParams(imageSide, imageSide));
        loadingView.setX(imageX);
        loadingView.setY(imageY);
        addView(loadingView);
    }

    private void initLoadingView() {
        int side = getLayoutParams().width / 4;
        int x = getLayoutParams().width / 2 - (side / 2);
        int y = getLayoutParams().height / 2 - (side / 2);

        loadingView.setLayoutParams(new LayoutParams(side, side));
        loadingView.setBackgroundColor(Color.TRANSPARENT);
        addView(loadingView);
    }

    public static void show(Activity activity) {
        new Handler(Looper.getMainLooper()).post(() -> {
            if (view == null) {
                view = new IndicatorHud(activity);
                activity.addContentView(view, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            }
        });
    }

    public static void dismiss() {
        new Handler(Looper.getMainLooper()).post(() -> {
            if (view != null && view.getParent() != null) {
                ((ViewGroup) view.getParent()).removeView(view);
                view = null;
            }
        });
    }
}
