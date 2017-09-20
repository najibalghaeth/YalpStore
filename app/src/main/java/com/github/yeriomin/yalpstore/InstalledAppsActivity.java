package com.github.yeriomin.yalpstore;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.github.yeriomin.yalpstore.model.App;
import com.github.yeriomin.yalpstore.task.ForegroundInstalledAppsTask;
import com.github.yeriomin.yalpstore.view.ListItem;
import com.github.yeriomin.yalpstore.view.UpdatableAppBadge;

public class InstalledAppsActivity extends AppListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.activity_title_updates_and_other_apps);
        Button button = findViewById(R.id.main_button);
        button.setEnabled(true);
        button.setVisibility(View.VISIBLE);
        button.setText(R.string.list_check_updates);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToUpdates();
            }
        });
        ((YalpStoreApplication) getApplicationContext()).setAppListNeedsUpdate(true);
        if (isAppLaunch() && PreferenceActivity.getBoolean(this, PreferenceActivity.PREFERENCE_UPDATES_ONLY)) {
            goToUpdates();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        YalpStoreApplication application = (YalpStoreApplication) getApplicationContext();
        if (application.appListNeedsUpdate()) {
            loadApps();
            application.setAppListNeedsUpdate(false);
        }
    }

    @Override
    protected void loadApps() {
        new ForegroundInstalledAppsTask(this).execute();
    }

    @Override
    protected ListItem getListItem(App app) {
        UpdatableAppBadge appBadge = new UpdatableAppBadge();
        appBadge.setApp(app);
        return appBadge;
    }

    private boolean isAppLaunch() {
        Intent intent = getIntent();
        String action = intent.getAction();
        return !TextUtils.isEmpty(action)
            && action.equals(Intent.ACTION_MAIN)
            && (intent.hasCategory(Intent.CATEGORY_LAUNCHER)
                || intent.hasCategory(Intent.CATEGORY_LEANBACK_LAUNCHER
            )
        );
    }

    private void goToUpdates() {
        startActivity(new Intent(this, UpdatableAppsActivity.class));
    }
}
