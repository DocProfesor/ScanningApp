package StaffDB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
/**
 * Created by DocProf on 5/12/2015.
 */
public class MySQLiteHelper extends SQLiteOpenHelper{

    public static final String TABLE_COMMENTS = "comments";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_COMMENT = "comment";
    public static final String COLUMN_STAFFID= "staffId";

    private static final String DATABASE_NAME = "comments.db";
    public static final int DATABASE_VERSION= 1;

    String TABLE_ATTLISTS= "attlists";
    String COLUMN_PROFILE = "";

    //Database creation SQL statement
    private static final String DATABASE_CREATE = "create table if not exists " +
            TABLE_COMMENTS + "(" + COLUMN_ID +
            " integer primary key autoincrement, " + COLUMN_COMMENT +
            " text not null, " + COLUMN_STAFFID +" integer);";

    private String CREATE_CUSTOM = "create table if not exists " +
            TABLE_ATTLISTS + "(" + COLUMN_ID + " integer primary key autoincrement, username text not null, password text not null, completed integer);";

    public MySQLiteHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.execSQL(DATABASE_CREATE);
        db.execSQL(CREATE_CUSTOM);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version" + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMENTS);
    }
}
