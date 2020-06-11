package com.github.studenttimetracker.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.github.studenttimetracker.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.button.MaterialButton;

import static com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_SHORT;

public class SettingsActivity extends AppCompatActivity {

    // keys to get right preference
    public static final String MY_PREFERENCES = "MY_PREFS";
    public static final String YOUR_NAME_KEY = "YOUR_NAME";
    public static final String NOTIFICATIONS_KEY = "NOTIFICATIONS";

    SharedPreferences sharedpreferences;

    TextInputLayout yourNameText;
    SwitchMaterial notificationSwitch;
    MaterialButton saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        sharedpreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);

        yourNameText = findViewById(R.id.settings_name_textfield);
        notificationSwitch = findViewById(R.id.settings_notification_switch);
        saveButton = findViewById(R.id.settings_button_save);

        yourNameText.getEditText().setText(sharedpreferences.getString(YOUR_NAME_KEY, "-"));
        notificationSwitch.setChecked(sharedpreferences.getBoolean(NOTIFICATIONS_KEY, true));

        saveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                String name = yourNameText.getEditText().getText().toString();
                boolean notifications = notificationSwitch.isChecked();

                // update preferences
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(YOUR_NAME_KEY, name);
                editor.putBoolean(NOTIFICATIONS_KEY, notifications);
                editor.apply();

                // small notification
                View contextView = findViewById(R.id.settings_activity);
                Snackbar.make(contextView, R.string.settings_saved_message, LENGTH_SHORT).show();
            }
        });

    }

}