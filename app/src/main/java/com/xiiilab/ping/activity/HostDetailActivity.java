package com.xiiilab.ping.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import com.xiiilab.ping.R;
import com.xiiilab.ping.ping.PingRequestExecutor;
import com.xiiilab.ping.repository.Repository;
import com.xiiilab.ping.viewmodel.DetailViewModel;

/**
 * Created by XIII-th on 20.07.2018
 */
public class HostDetailActivity extends AppCompatActivity {

    public static final String HOST = "com.xiiilab.ping.activity.HostDetailActivity HOST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String host = getSelectedHost();
        DetailViewModel viewModel = ViewModelProviders.of(this).get(DetailViewModel.class);
        viewModel.setPingValueProvider(PingRequestExecutor.getInstance()::getPingValue);
        viewModel.setEntity(Repository.getInstance().get(host));

        setContentView(R.layout.host_detail_activity);

        // setup toolbar controls and title
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private String getSelectedHost() {
        String host = null;
        if (getIntent() != null)
            host = getIntent().getStringExtra(HOST);

        if (host == null)
            throw new IllegalStateException("Unable to obtain host entity from intent");

        return host;
    }
}
