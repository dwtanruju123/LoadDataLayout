package com.sky.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

public class CustomLoadDataLayout extends FrameLayout {
    private final static int WAIT = 0;
    public final static int LOADING = 1;
    public final static int SUCCESS = 2;
    public final static int EMPTY = 3;
    public final static int ERROR = 4;
    public final static int NO_NETWORK = 5;
    protected int currentState = WAIT;
    private int loadingViewLayoutId, emptyViewLayoutId, errorViewLayoutId, netWorkViewLayoutId;
    private View loadingView, emptyView, errorView, netWorkView, contentView;
    private Context mContext;

    public CustomLoadDataLayout(Context context) {
        this(context, null);
    }

    public CustomLoadDataLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomLoadDataLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomLoadDataLayout);
        loadingViewLayoutId = a.getResourceId(R.styleable.CustomLoadDataLayout_loadingViewLayoutId, NO_ID);
        emptyViewLayoutId = a.getResourceId(R.styleable.CustomLoadDataLayout_emptyViewLayoutId, NO_ID);
        errorViewLayoutId = a.getResourceId(R.styleable.CustomLoadDataLayout_errorViewLayoutId, NO_ID);
        netWorkViewLayoutId = a.getResourceId(R.styleable.CustomLoadDataLayout_netWorkViewLayoutId, NO_ID);
        if (a != null)
            a.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() > 1) {
            throw new IllegalStateException(getClass().getSimpleName() + " can host only one direct child");
        }
        build();
    }

    private void build() {
        if (getChildCount() > 0)
            contentView = getChildAt(0);
        loadingView = addChildLayout(loadingViewLayoutId);
        emptyView = addChildLayout(emptyViewLayoutId);
        errorView = addChildLayout(errorViewLayoutId);
        netWorkView = addChildLayout(netWorkViewLayoutId);
        setStatus(loadingView != null ? LOADING : WAIT);
    }

    public void setOnLoadingClickListener(final LoadDataLayoutClick listener, int... ids) {
        setOnClickListener(loadingView, listener, ids);
    }

    public void setOnEmptyClickListener(final LoadDataLayoutClick listener, int... ids) {
        setOnClickListener(emptyView, listener, ids);
    }

    public void setOnErrorClickListener(final LoadDataLayoutClick listener, int... ids) {
        setOnClickListener(errorView, listener, ids);
    }

    public void setOnNetWorkClickListener(final LoadDataLayoutClick listener, int... ids) {
        setOnClickListener(netWorkView, listener, ids);
    }

    private void setOnClickListener(final View view, final LoadDataLayoutClick listener, int... ids) {
        if (listener != null && view != null)
            for (int id : ids)
                view.findViewById(id).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onClick(v.getId(), view);
                    }
                });
    }

    private void setVisibilityGone(View... views) {
        for (View view : views)
            if (view != null)
                view.setVisibility(GONE);
    }

    private void setVisibilityVisible(View view) {
        if (view != null)
            view.setVisibility(VISIBLE);
    }

    private View addChildLayout(int layoutId) {
        if (layoutId != NO_ID) {
            View view = LayoutInflater.from(mContext).inflate(layoutId, null);
            addView(view);
            return view;
        } else return null;
    }

    public interface LoadDataLayoutClick {
        void onClick(int id, View view);
    }

    public int getStatus() {
        return currentState;
    }

    public void setStatus(int state) {
        this.currentState = state;
        switch (state) {
            case WAIT:
                setVisibilityGone(contentView, loadingView, emptyView, errorView, netWorkView);
                break;
            case SUCCESS:
                setVisibilityVisible(contentView);
                setVisibilityGone(loadingView, emptyView, errorView, netWorkView);
                break;
            case LOADING:
                if (loadingView == null) return;
                setVisibilityVisible(loadingView);
                setVisibilityGone(contentView, emptyView, errorView, netWorkView);
                break;
            case EMPTY:
                if (emptyView == null) return;
                setVisibilityVisible(emptyView);
                setVisibilityGone(contentView, loadingView, errorView, netWorkView);
                break;
            case ERROR:
                if (errorView == null) return;
                setVisibilityVisible(errorView);
                setVisibilityGone(contentView, loadingView, emptyView, netWorkView);
                break;
            case NO_NETWORK:
                if (netWorkView == null) return;
                setVisibilityVisible(netWorkView);
                setVisibilityGone(contentView, loadingView, emptyView, errorView);
                break;
        }
    }

    public void onAttachNetwork(boolean isNetwork) {
        if (isNetwork) {
            if (currentState == WAIT)
                setStatus(loadingViewLayoutId == NO_ID ? SUCCESS : LOADING);
            else
                setStatus(currentState);
        } else
            setStatus(NO_NETWORK);
    }

}
