package com.e13.multi_reminder_app;

import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

public class HelperMethods extends AppCompatActivity {

    public Class helpingNavOnClick(MenuItem item) {
        switch (item.getItemId()) {
//                    case R.id.comingUpTabDrawer:
//                        return coming_up_activity.class;
            case R.id.newReminderDrawer:
                return ReminderCreationActivity.class;
            case R.id.macroButtonDrawer:
                return MacroActivity.class;
            case R.id.mesoButtonDrawer:
                return MesoActivity.class;
            case R.id.microButtonDrawer:
                return MicroActivity.class;
//                    case R.id.settings:
//                        return settings.class;
        }
        return MainPageActivity.class;
    }
}
