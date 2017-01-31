package com.joxad.zikobot.app.core.db;

import com.raizlabs.android.dbflow.annotation.Database;
import com.raizlabs.android.dbflow.annotation.Migration;
import com.raizlabs.android.dbflow.sql.SQLiteType;
import com.raizlabs.android.dbflow.sql.migration.AlterTableMigration;
import com.joxad.zikobot.app.core.model.Alarm;
import com.joxad.zikobot.app.core.model.Track;

@Database(name = MusicAlarmDatabase.NAME, version = MusicAlarmDatabase.VERSION)
public class MusicAlarmDatabase {

    public static final String NAME = "Music_Alarm_DB";

    public static final int VERSION = 5;


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

    @Migration(version = 3, database = MusicAlarmDatabase.class)
    public static class Migration3 extends AlterTableMigration<Alarm> {

        public Migration3(Class<Alarm> table) {
            super(table);
        }

        @Override
        public void onPreMigrate() {
            addColumn(SQLiteType.INTEGER, "volume");
        }
    }

    @Migration(version = 4, database = MusicAlarmDatabase.class)
    public static class Migration4 extends AlterTableMigration<Track> {

        public Migration4(Class<Track> table) {
            super(table);
        }

        @Override
        public void onPreMigrate() {
            addColumn(SQLiteType.INTEGER, "activated");
        }
    }


    @Migration(version = 5, database = MusicAlarmDatabase.class)
    public static class Migration5 extends AlterTableMigration<Alarm> {

        public Migration5(Class<Alarm> table) {
            super(table);
        }

        @Override
        public void onPreMigrate() {
            addColumn(SQLiteType.INTEGER, "timeInMillis");
        }
    }

}

