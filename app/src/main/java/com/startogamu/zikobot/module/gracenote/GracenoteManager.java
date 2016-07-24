package com.startogamu.zikobot.module.gracenote;

import android.content.Context;

import com.gracenote.gnsdk.GnError;
import com.gracenote.gnsdk.GnException;
import com.gracenote.gnsdk.GnLicenseInputMode;
import com.gracenote.gnsdk.GnLookupData;
import com.gracenote.gnsdk.GnManager;
import com.gracenote.gnsdk.GnMusicIdStream;
import com.gracenote.gnsdk.GnMusicIdStreamIdentifyingStatus;
import com.gracenote.gnsdk.GnMusicIdStreamPreset;
import com.gracenote.gnsdk.GnMusicIdStreamProcessingStatus;
import com.gracenote.gnsdk.GnResponseAlbums;
import com.gracenote.gnsdk.GnStatus;
import com.gracenote.gnsdk.GnStorageSqlite;
import com.gracenote.gnsdk.GnUser;
import com.gracenote.gnsdk.GnUserStore;
import com.gracenote.gnsdk.IGnCancellable;
import com.gracenote.gnsdk.IGnMusicIdStreamEvents;
import com.orhanobut.logger.Logger;
import com.startogamu.zikobot.R;

/**
 * Created by josh on 22/07/16.
 */
public class GracenoteManager {

    private static GnManager gnManager;
    private static GnUser gnUser;
    private static GnMusicIdStream gnMusicIdStream;

    public static void init(Context context) throws GnException {
        gnManager = new GnManager(context, context.getString(R.string.gracenote_license_text), GnLicenseInputMode.kLicenseInputModeString);

        // get a user, if no user stored persistently a new user is registered and stored
        // Note: Android persistent storage used, so no GNSDK storage provider needed to store a user
        gnUser = new GnUser(new GnUserStore(context), context.getString(R.string.gracenote_id), context.getString(R.string.gracenote_tag), "Zikobot");

        // enable storage provider allowing GNSDK to use its persistent stores
        GnStorageSqlite.enable();

        gnMusicIdStream = new GnMusicIdStream(gnUser, GnMusicIdStreamPreset.kPresetMicrophone, new IGnMusicIdStreamEvents() {
            @Override
            public void musicIdStreamProcessingStatusEvent(GnMusicIdStreamProcessingStatus status, IGnCancellable iGnCancellable) {
                Logger.d("Processing status " + status.name());
                if (GnMusicIdStreamProcessingStatus.kStatusProcessingAudioStarted.compareTo(status) == 0) {
                    Logger.d("GraceNote Ready");
                }

            }

            @Override
            public void musicIdStreamIdentifyingStatusEvent(GnMusicIdStreamIdentifyingStatus status, IGnCancellable iGnCancellable) {
                Logger.d("Identify : " + status.name());
            }

            @Override
            public void musicIdStreamAlbumResult(GnResponseAlbums gnResponseAlbums, IGnCancellable iGnCancellable) {
                Logger.d("Results " + gnResponseAlbums.albums().count());
            }

            @Override
            public void musicIdStreamIdentifyCompletedWithError(GnError gnError) {
                Logger.d(gnError.errorDescription());
            }

            @Override
            public void statusEvent(GnStatus gnStatus, long l, long l1, long l2, IGnCancellable iGnCancellable) {
                Logger.d(gnStatus.name());
            }
        });
        gnMusicIdStream.options().lookupData(GnLookupData.kLookupDataContent, true);
        gnMusicIdStream.options().lookupData(GnLookupData.kLookupDataSonicData, true);
        gnMusicIdStream.options().resultSingle(true);


    }

    public static void identify() throws GnException {
        gnMusicIdStream.identifyAlbumAsync();
    }
}
