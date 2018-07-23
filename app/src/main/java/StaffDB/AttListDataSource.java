package StaffDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

/**
 * Created by DocProf on 6/20/2015.
 */
public class AttListDataSource {
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = {MySQLiteHelper.COLUMN_ID, MySQLiteHelper.COLUMN_COMMENT};

    public AttListDataSource(Context context){
        dbHelper = new MySQLiteHelper(context);
    }

    public void open(){
        database = dbHelper.getWritableDatabase();
    }

    public void close(){
        dbHelper.close();
    }

    public Comment createComment(String comment, String username, String password){

        ContentValues values= new ContentValues();

        values.put(MySQLiteHelper.COLUMN_COMMENT, comment);
        long insertId= database.insert(MySQLiteHelper.TABLE_COMMENTS, null, values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_COMMENTS, allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();

        AttList newComment= new AttList(database,username, password);
        cursor.close();

        return newComment;
    }

    public void deleteComment(Comment comment){
        long id = comment.getId();
        System.out.println("Comment deleted with id " + id);
        database.delete(MySQLiteHelper.TABLE_COMMENTS, MySQLiteHelper.COLUMN_ID + " = " + id, null);
    }

}
