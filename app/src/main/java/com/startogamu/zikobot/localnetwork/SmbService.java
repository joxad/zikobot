package com.startogamu.zikobot.localnetwork;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.net.UnknownHostException;

import jcifs.UniAddress;
import jcifs.netbios.NbtAddress;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import jcifs.smb.SmbSession;

/**
 * Created by josh on 21/08/16.
 */
public class SmbService extends Service {


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        connectingWithSmbServer();
        return START_STICKY;
    }
    public void connectingWithSmbServer() {
        Handler looper = new Handler(getMainLooper());

        try {
            String yourPeerPassword = "";
            String yourPeerName = "";
            String yourPeerIP = "192.168.1.24";
            String path = "smb://" + yourPeerIP;
            NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(
                    null, yourPeerName, yourPeerPassword);
            Log.e("Connected", "Yes");
            looper.post(() -> {
                try {
                    SmbSession.logon(UniAddress.getByName(path), auth);
                } catch (SmbException e) {
                    e.printStackTrace();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            });

         /*   SmbFile smbFile = new SmbFile(path, auth);
            /** Printing Information about SMB file which belong to your Peer **/
          //  String nameoffile = smbFile.getName();
            //String pathoffile = smbFile.getPath();
            //Log.e(nameoffile, pathoffile);

            //listSMB(smbFile);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Connected", e.getMessage());
        }
    }

    public void downloadFileFromPeerToSdcard(File mLocalFile, SmbFile mFile) {
        try {
            SmbFileInputStream mFStream = new SmbFileInputStream(mFile);
            mLocalFile = new File(Environment.getExternalStorageDirectory(),
                    mFile.getName());
            FileOutputStream mFileOutputStream = new FileOutputStream(
                    mLocalFile);
            byte[] buffer = new byte[1024];
            int len1 = 0;
            while ((len1 = mFStream.read(buffer)) > 0) {
                mFileOutputStream.write(buffer, 0, len1);
            }
            mFileOutputStream.close();
            mFStream.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e("MalformURL", e.getMessage());
        } catch (SmbException e) {
            e.printStackTrace();
            Log.e("SMBException", e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Exception", e.getMessage());
        }
    }


    public void listSMB(SmbFile root) throws SmbException {
        SmbFile[] domains = root.listFiles();
        for (int i = 0; i < domains.length; i++) {
            Logger.d(domains[i].getName() + " - domain, Path = " + domains[i].getPath());
            Logger.d("File Listing for " + domains[i].getName());
            SmbFile[] servers = null;
            try {
                servers = domains[i].listFiles();
            } catch (SmbException e) {
                e.printStackTrace();
            }

            if (servers != null) {
                if (servers.length > 0) {
                    for (int j = 0; j < servers.length; j++) {
                        Log.d("debug", servers[j].getName() + " - server, Path = " + servers[i].getPath());
                        listShares(servers[j].getName());
                    }
                } else
                    listShares(domains[i].getName());
            } else
                listShares(domains[i].getName());
        }
        Logger.d("Scan finished !");
    }

    private void listShares(String name) {
        Log.d("debug", "File Listing for " + name);
        String host;
        try {
            host = name;
            if (host.endsWith("/"))
                host = host.substring(0, host.length() - 1);
            NbtAddress addrs = NbtAddress.getByName(host);

            SmbFile test = new SmbFile("smb://" + addrs.getHostAddress());
            SmbFile[] files = test.listFiles();
            for (SmbFile s : files) {
                Log.d("debug", s.getName());
            }
        } catch (UnknownHostException e) {
            Log.d("debug", "ERROR");
        } catch (SmbException e) {
            Log.d("debug", "ERROR");
        } catch (MalformedURLException e) {
            Log.d("debug", "ERROR");
        }

    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
