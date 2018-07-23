package StaffDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DocProf on 5/12/2015.
 */
public class CommentsDataSource {

    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = {MySQLiteHelper.COLUMN_ID, MySQLiteHelper.COLUMN_COMMENT, MySQLiteHelper.COLUMN_STAFFID};
    //private String DATABASE_QUERY = "SELECT * FROM " + database;

    public CommentsDataSource(Context context){
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database= dbHelper.getWritableDatabase();
    }
    public void close() {
        dbHelper.close();
    }

    public Comment createComment(String comment){
        ContentValues values= new ContentValues();
        values.put(MySQLiteHelper.COLUMN_COMMENT, comment);
        long insertId= database.insert(MySQLiteHelper.TABLE_COMMENTS, null, values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_COMMENTS, allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        Comment newComment= cursorToComment(cursor);
        cursor.close();
        return newComment;
    }

    public void deleteComment(Comment comment){
        long id = comment.getId();
        System.out.println("Comment deleted with id " + id);
        database.delete(MySQLiteHelper.TABLE_COMMENTS, MySQLiteHelper.COLUMN_ID + " = " + id, null);
    }

    public Cursor tableCursor() {
        Cursor cursor = database.query(MySQLiteHelper.TABLE_COMMENTS, allColumns, null, null, null, null, null);
        return cursor;
    }

    public List<Comment> getAllComments(){
        List<Comment> comments = new ArrayList<Comment>();

        Cursor cursor = tableCursor();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            Comment comment = cursorToComment(cursor);
            comments.add(comment);
            cursor.moveToNext();
        }

        cursor.close();
        return comments;
    }

    private Comment cursorToComment(Cursor cursor){
        Comment comment = new Comment();
        comment.setId(cursor.getLong(0));
        comment.setComment(cursor.getString(1));

        return comment;
    }

    public JSONObject dbToJson(){

        Cursor cursor = tableCursor();
        cursor.moveToFirst();

        JSONObject jsonObject = new JSONObject();

        int entryCount = cursor.getCount();
        for(int j=0; j<entryCount-1; j++){


            int colCount = cursor.getColumnCount();

            for(int i=0; i<colCount; i++){

                //Check if columns have name and non null value
                if(cursor.getColumnName(i) != null) {
                    try {
                        if (cursor.getString(i) != null) {
                            String tag = Integer.toString(j);
                            jsonObject.put(cursor.getColumnName(i) + tag, cursor.getString(i));
                        } else {
                            jsonObject.put(cursor.getColumnName(i), "[NO VALUE]");
                        }
                    } catch (Exception e) {
                        Log.d("TAG_NAME", e.getMessage());
                    }
                }
            }
            cursor.moveToNext();
        }

        return jsonObject;
    }
}
