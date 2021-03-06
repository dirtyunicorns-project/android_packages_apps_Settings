/*
 * Copyright (C) 2012 The CyanogenMod Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.settings.dirtyunicorns.profiles.actions.item;

import android.content.Context;
import android.media.AudioManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.settings.R;
import com.android.settings.dirtyunicorns.profiles.actions.ItemListAdapter;

import com.android.internal.util.lineageos.profiles.StreamSettings;

public class VolumeStreamItem extends BaseItem {
    private int mStreamId;
    private StreamSettings mStreamSettings;
    private boolean mEnabled = true;

    public VolumeStreamItem(int streamId, StreamSettings streamSettings) {
        mStreamId = streamId;
        mStreamSettings = streamSettings;
    }

    @Override
    public ItemListAdapter.RowType getRowType() {
        return ItemListAdapter.RowType.VOLUME_STREAM_ITEM;
    }

    @Override
    public boolean isEnabled() {
        return mEnabled;
    }

    @Override
    public String getTitle() {
        return getString(getNameForStream(mStreamId));
    }

    @Override
    public String getSummary() {
        if (mStreamSettings.isOverride()) {
            final AudioManager am =
                    (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);

            int denominator = mStreamSettings.getValue();
            int numerator = am.getStreamMaxVolume(mStreamId);
            return getContext().getResources().getString(
                    R.string.sp_volume_override_summary,
                    denominator, numerator);
        }
        return getString(R.string.sp_profile_action_none);
    }

    @Override
    public View getView(LayoutInflater inflater, View convertView, ViewGroup parent) {
        View view = super.getView(inflater, convertView, parent);
        /*
        final boolean volumeLinkNotification = Settings.Secure.getInt(inflater.getContext()
                .getContentResolver(), Settings.Secure.VOLUME_LINK_NOTIFICATION, 1) == 1;
        */
        final boolean volumeLinkNotification = false;
        if (mStreamId == AudioManager.STREAM_NOTIFICATION && volumeLinkNotification) {
            view.findViewById(android.R.id.title).setEnabled(false);
            view.findViewById(android.R.id.summary).setEnabled(false);
            mEnabled = false;
        }
        return view;
    }

    public static int getNameForStream(int stream) {
        switch (stream) {
            case AudioManager.STREAM_ALARM:
                return R.string.sp_alarm_volume_title;
            case AudioManager.STREAM_MUSIC:
                return R.string.sp_media_volume_title;
            case AudioManager.STREAM_RING:
                return R.string.sp_incoming_call_volume_title;
            case AudioManager.STREAM_NOTIFICATION:
                return R.string.sp_notification_volume_title;
            default: return 0;
        }
    }

    public int getStreamType() {
        return mStreamId;
    }

    public StreamSettings getSettings() {
        return mStreamSettings;
    }
}