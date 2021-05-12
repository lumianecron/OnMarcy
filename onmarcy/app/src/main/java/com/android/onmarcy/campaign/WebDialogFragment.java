package com.android.onmarcy.campaign;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.android.onmarcy.R;

import java.util.ArrayList;

public class WebDialogFragment extends DialogFragment {
    public static final String TAG = "create_dialog";
    public static final String STATE_LINK = "link";
    private FragmentManager fragmentManager;
    private WebView webView;
    private Toolbar toolbar;
    private String link;

    public WebDialogFragment() {

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(STATE_LINK, link);
    }

    public WebDialogFragment(FragmentManager fragmentManager, String link) {
        this.fragmentManager = fragmentManager;
        this.link = link;
    }

    public static WebDialogFragment display(FragmentManager fragmentManager, String link) {
        WebDialogFragment webDialogFragment = new WebDialogFragment(fragmentManager, link);
        webDialogFragment.show(fragmentManager, TAG);
        return webDialogFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_FullScreenDialog);
        if (savedInstanceState != null) {
            link = savedInstanceState.getString(STATE_LINK);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_web_dialog, container, false);
        toolbar = view.findViewById(R.id.toolbar);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindView(getView());
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        toolbar.setTitle(R.string.payment);

        webView.loadUrl(link);
        webView.setWebViewClient(new MyWebViewClient());
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true); // mengaktifkan javascript yang ada
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setWindowAnimations(R.style.AppTheme_Slide);
        }
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // Kalau host URL cocok maka return false agar tidak mengganti pemuatan URL
            if(getString(R.string.url_host).equals(Uri.parse(url).getHost())){
                return false;
            }
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
            return true;
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new Dialog(getActivity(), getTheme()){
            @Override
            public void onBackPressed() {
                if (webView.canGoBack()) {
                    webView.goBack();
                }else{
                    dismiss();
                }
            }
        };
    }

    private void bindView(View view) {
        webView = view.findViewById(R.id.web_view);
    }
}
