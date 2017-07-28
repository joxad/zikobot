package com.joxad.zikobot.data.db;

import com.joxad.zikobot.data.db.model.ZikoArtist;
import com.joxad.zikobot.data.db.model.ZikoArtist_Table;
import com.raizlabs.android.dbflow.annotation.Database;
import com.raizlabs.android.dbflow.sql.language.SQLOperator;
import com.raizlabs.android.dbflow.sql.language.SQLite;

@Database(name = ZikoDB.NAME, version = ZikoDB.VERSION)
public class ZikoDB {

    public static final String NAME = "ZikoDB";

    public static final int VERSION = 1;


    public static boolean exists(Class c, SQLOperator sqlOperator) {
        long count = SQLite.select().
                from(c).
                where(sqlOperator).
                count();

        return count>=0;
    }

}

