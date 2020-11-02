package com.e13.multi_reminder_app;

import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.e13.multi_reminder_app.RecyclerViewParts.ManageActiveReminders;

import java.util.Calendar;

public class HelperMethods extends AppCompatActivity {

    public Class helpingNavOnClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.comingUpTabDrawer:
                return comingUpActivity.class;
            case R.id.activeReminderDrawer:
                return ManageActiveReminders.class;
            case R.id.macroButtonDrawer:
                return MacroActivity.class;
            case R.id.mesoButtonDrawer:
                return MesoActivity.class;
            case R.id.microButtonDrawer:
                return MicroActivity.class;
            case R.id.newReminderDrawer:
                return ReminderCreationActivity.class;
//                    case R.id.settings:
//                        return settings.class;
        }
        return MainPageActivity.class;
    }

    public long getTimeUntil(int year, int month, int day, int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    public String getPriority(int i) {
        switch (i) {
            case 1:
                return "Low";
            case 2:
                return "Medium";
            case 3:
                return "High";
            case 4:
                return  "Urgent";
        }
        return "No priority found";
    }

    public int getPriorityImage(int i) {
        switch (i) {
            case 1:
                return R.drawable.bluesquare;
            case 2:
                return R.drawable.greensquare;
            case 3:
                return R.drawable.orangesquare;
            case 4:
                return R.drawable.redsquare;
        }
        return R.drawable.errorsquare;
    }

    public long findNewTime(String frequency, long currentTime) {
        switch (frequency) {
            case "Hourly":
                return currentTime + 3600000;
            case "Daily":
                return currentTime + 86400000;
            case "Weekly":
                return currentTime + 604800000;
            case "Monthly":
                Calendar c = Calendar.getInstance();
                c.setTimeInMillis(currentTime);
                c.add(Calendar.MONTH, c.get(Calendar.MONTH)+1);
                return c.getTimeInMillis();
        }
        return 0;
    }


}
