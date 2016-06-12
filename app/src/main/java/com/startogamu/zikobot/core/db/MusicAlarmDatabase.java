package com.startogamu.zikobot.core.db;

import com.raizlabs.android.dbflow.annotation.Database;
import com.raizlabs.android.dbflow.annotation.Migration;
import com.raizlabs.android.dbflow.sql.SQLiteType;
import com.raizlabs.android.dbflow.sql.migration.AlterTableMigration;
import com.startogamu.zikobot.module.alarm.model.Alarm;

@Database(name = MusicAlarmDatabase.NAME, version = MusicAlarmDatabase.VERSION)
public class MusicAlarmDatabase {

    public static final String NAME = "Music_Alarm_DB";

    public static final int VERSION = 2;


    @Migration(version = 2, database = MusicAlarmDatabase.class)
    public static class Migration2 extends AlterTableMigration<Alarm> {

        public Migration2(Class<Alarm> table) {
            super(table);
        }

        @Override
        public void onPreMigrate() {
            addColumn(SQLiteType.INTEGER, "randomTrack");
        }
    }
}