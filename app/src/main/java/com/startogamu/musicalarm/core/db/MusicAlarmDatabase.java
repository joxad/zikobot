package com.startogamu.musicalarm.core.db;

import com.raizlabs.android.dbflow.annotation.Database;

@Database(name = MusicAlarmDatabase.NAME, version = MusicAlarmDatabase.VERSION)
public class MusicAlarmDatabase {

  public static final String NAME = "MusicAlarmDB";

  public static final int VERSION = 1;
}