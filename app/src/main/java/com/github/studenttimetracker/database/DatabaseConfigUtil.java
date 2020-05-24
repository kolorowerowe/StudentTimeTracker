package com.github.studenttimetracker.database;

import com.github.studenttimetracker.models.Project;
import com.github.studenttimetracker.models.Task;
import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;

import java.io.IOException;
import java.sql.SQLException;

public class DatabaseConfigUtil extends OrmLiteConfigUtil {
    private static final Class<?>[] clases = new Class[]{Project.class, Task.class};

    public static void main(String[] args) throws SQLException, IOException {
        writeConfigFile("ormlite_config.txt", clases);
    }
}
