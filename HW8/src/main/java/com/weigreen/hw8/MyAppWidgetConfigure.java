package com.weigreen.hw8;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

/**
 * Created by green on 2013/5/18.
 */
public class MyAppWidgetConfigure extends Activity {
    //Log tag
    private static final String TAG = "MyAppWidgetConfigure";

    private static final String PREFS_NAME = "com.weigreen.hw8.MyAppWidgetProvider";
    private static final String PREF_PREFIX_KEY = "prefix_";

    //Save the app id
    private int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    //The word text
   EditText inputWord;

    //The time
    EditText inputPeriod;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "conf on create");
        // Set the view layout resource to use.
        setContentView(R.layout.layout_appwidget_configure);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if they press the back button.
        setResult(RESULT_CANCELED);

        // Find the EditText
        inputWord = (EditText)findViewById(R.id.editText_word);
        inputPeriod = (EditText)findViewById(R.id.editText_period);

        // Bind the action for the save button.
        findViewById(R.id.button_submit).setOnClickListener(mOnClickListener);

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            Log.d(TAG, "widgetId = " + mAppWidgetId);
        }

        // If they gave us an intent without the widget id, just bail.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            Log.d(TAG, "INVALID");
            finish();
        }

        inputWord.setText(loadTitlePref(MyAppWidgetConfigure.this, mAppWidgetId));

    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            final Context context = MyAppWidgetConfigure.this.getApplicationContext();

            // When the button is clicked, save the string in our prefs and return that they
            // clicked OK.
            String titlePrefix = inputWord.getText().toString();
            saveTitlePref(context, mAppWidgetId, titlePrefix);

            // Push widget update to surface with newly set prefix
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            MyAppWidgetProvider.updateAppWidget(context, appWidgetManager, mAppWidgetId, titlePrefix);

            // Make sure we pass back the original appWidgetId
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            Log.d(TAG, "result id"+ mAppWidgetId);
            finish();
        }
    };




    // Write the prefix to the SharedPreferences object for this widget
    static void saveTitlePref(Context context, int appWidgetId, String text) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_PREFIX_KEY + appWidgetId, text);
        prefs.commit();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static String loadTitlePref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String prefix = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null);
        if (prefix != null) {
            return prefix;
        } else {
            return context.getString(R.string.default_string);
        }
    }


    public void finishConfigure(View view){

    }
}