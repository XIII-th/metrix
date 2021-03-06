package com.xiiilab.ping.viewmodel;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.stealthcopter.networktools.ping.PingResult;
import com.xiiilab.ping.R;
import com.xiiilab.ping.persistance.HostEntity;

import java.util.ArrayList;

/**
 * Created by XIII-th on 25.07.2018
 */
public class DetailViewModel extends ItemViewModel {

    private final MediatorLiveData<Entry> mLastEntry;
    private final LiveData<Boolean> mEntitySelected;
    private final LineDataSet mDataSet;
    private final int mEntryLimit;

    private String mCurrentHost;

    public DetailViewModel(@NonNull Application application) {
        super(application);

        mEntitySelected = Transformations.map(getEntity(), entity -> entity != null);

        mLastEntry = (MediatorLiveData<Entry>) Transformations.map(getPingValue(), this::addChartEntry);
        mLastEntry.addSource(getEntity(), this::notifyDataSourceChanged);

        mEntryLimit = application.getResources().getInteger(R.integer.chart_entry_limit);
        mDataSet = new LineDataSet(new ArrayList<>(), application.getString(R.string.chart_legend));
        // add initial entry
        addChartEntry(null);
        LineData lineData = new LineData(mDataSet);
        mDataSet.setDrawCircleHole(false);
        mDataSet.setCircleColor(getApplication().getResources().getColor(R.color.colorChart));
        lineData.setDrawValues(false);
    }

    public LiveData<Boolean> isEntitySelected() {
        return mEntitySelected;
    }

    public LiveData<Entry> getLastEntry() {
        return mLastEntry;
    }

    public LineData getChartData() {
        return new LineData(mDataSet);
    }

    private void notifyDataSourceChanged(@Nullable HostEntity hostEntity) {
        if (hostEntity == null)
            mCurrentHost = null;
        else if (hostEntity.getHost().equals(mCurrentHost))
            return;
        else
            mCurrentHost = hostEntity.getHost();
        // cleanup chart data
        mDataSet.clear();
        Entry entry = new Entry();
        mDataSet.addEntry(entry);
        mDataSet.notifyDataSetChanged();
        // trigger LineChart to redraw
        mLastEntry.setValue(entry);
    }

    private Entry addChartEntry(@Nullable PingResult pingResult) {
        HostEntity hostEntity = getEntity().getValue();
        float x, ping = pingResult == null ? 0F : pingResult.timeTaken;
        Entry lastEntry = mLastEntry.getValue();
        if (lastEntry == null)
            x = 0F;
        else {
            float pingOrTimeout = ping == 0F ? hostEntity.getTimeout() : ping;
            x = lastEntry.getX() + (hostEntity.getFrequency() + pingOrTimeout) / 1000F;
        }
        Entry entry = new Entry(x, ping);
        mDataSet.addEntry(entry);
        if (mDataSet.getEntryCount() == mEntryLimit)
            mDataSet.removeFirst();
        return entry;
    }
}
