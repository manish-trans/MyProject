package com.e.cartapplication;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.constraint.Constraints;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {
    WebView testwebview;
   // String url="https://www.mfadirect.com/jtest.php";
    String url="http://baymanagement.azurewebsites.net";
   //String url="http://an-server/";
    String postData;
    ImageView refresh_button;
    ProgressBar progressbar;
    String email="";
    Context context=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        testwebview=findViewById(R.id.testwebview);
        refresh_button=findViewById(R.id.refresh_button);
        progressbar=findViewById(R.id.progressbar);
        //testwebview.loadData(url, "text/html; charset=UTF-8", null);
        progressbar.setVisibility(View.VISIBLE);
        String android_id = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
        String device_name=android.os.Build.MODEL;
      /*  SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        String location_id = settings.getString("location_id", "defaultValue");
*/
        WebSettings webSettings = testwebview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        testwebview.getSettings().setPluginState(WebSettings.PluginState.ON);

        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        testwebview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        getEmail();
      //  testwebview.loadUrl(url);

        try {//+"&_email" + email
            postData = "_deviceid=" + URLEncoder.encode(android_id, "UTF-8")+"&_devicename=" + URLEncoder.encode(device_name,"UTF-8")+"&_osname=" + getAndroidOs()+"&_email=" + URLEncoder.encode(email,"UTF8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (!DetectConnection.checkInternetConnection(this)) {
            testwebview.loadUrl("file:///android_asset/error.html"); //Change path if it is not correct
            refresh_button.setVisibility(View.VISIBLE);

        } else {
            refresh_button.setVisibility(View.GONE);
          /*  testwebview.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Log.e("RequestUrl", request.getUrl().toString());
                        //view.loadUrl(request.getUrl().toString());
                        view.postUrl(url, postData.getBytes());
                    }
                    return false;
                }
            });*/
            testwebview.setWebViewClient(new AppWebViewClient());

            testwebview.postUrl(url, postData.getBytes());
        }
        refresh_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testwebview.setWebViewClient(new AppWebViewClient());
                testwebview.postUrl(url, postData.getBytes());
                //testwebview.reload();
            }
        });
    }

    private String getAndroidOs() {

        StringBuilder builder = new StringBuilder();
        builder.append("android : ").append(Build.VERSION.RELEASE);

        Field[] fields = Build.VERSION_CODES.class.getFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            int fieldValue = -1;

            try {
                fieldValue = field.getInt(new Object());
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            if (fieldValue == Build.VERSION.SDK_INT) {
                builder.append(" : ").append(fieldName).append(" : ");
                builder.append("sdk=").append(fieldValue);
            }
        }
return (builder.toString())
       ;
    }
    private void getEmail()
    {
        String possibleEmail = "";
        final Account[] accounts = AccountManager.get(MainActivity.this).getAccounts();
        //Log.e("accounts","->"+accounts.length);
        for (Account account : accounts) {
            if (Patterns.EMAIL_ADDRESS.matcher(account.name).matches()) {
                possibleEmail = account.name;
            }
        }

        email=possibleEmail;

    }

    public class AppWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
            //setProgressBar(true);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            if (url != null && url.contains("whatsapp://") || url.contains("mailto:")) {
                view.getContext().startActivity(
                        new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                return true;
            }
         else if(url.contains("baymanagement"))
            {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(i);
            }
       /*  else if(url.contains("an-server"))
            {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(i);
            }*/
            else {
                view.loadUrl(url);

            }
            return true;

        }


        @Override
        public void onPageFinished(WebView view, String url) {
            //Page load finished
            super.onPageFinished(view, url);
            progressbar.setVisibility(View.GONE);

        }
    }
        @Override
    public void onBackPressed() {
        if (testwebview.canGoBack()) {
            testwebview.goBack();
        } else {
            super.onBackPressed();
        }
    }

}
