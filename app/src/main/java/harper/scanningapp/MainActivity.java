package harper.scanningapp;
import android.app.ListActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import java.sql.SQLException;
import java.util.List;
import java.util.Random;

import StaffDB.CommentsDataSource;
import StaffDB.Comment;

public class MainActivity extends ListActivity implements OnClickListener{

    //Global fields
    Button btn;
    Button btn2;
    Button btn3;

    EditText editLogName;

    Bundle parameters = new Bundle();

    private CommentsDataSource datasource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn2  =(Button) findViewById(R.id.next_button);
        btn2.setOnClickListener(this);

        editLogName = (EditText) findViewById(R.id.edit_logName);

        datasource = new CommentsDataSource(this);

        try {
            datasource.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        List<Comment> values = datasource.getAllComments();

        //Show the elements in the list with SimpleCursorAdapter
        ArrayAdapter<Comment> adapter = new ArrayAdapter<Comment>(this,
                android.R.layout.simple_list_item_1, values);
        setListAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        ArrayAdapter <Comment> adapter = (ArrayAdapter<Comment>) getListAdapter();
        Comment comment = null;

        switch(view.getId()){
            case R.id.next_button:
                if(validate()==true) {
                    parameters.putString("user", editLogName.getText().toString());

                    Intent intent = new Intent(this, SecondActivity.class);

                    intent.putExtras(parameters);

                    startActivity(intent);
                }
        }

        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume(){
        try {
            datasource.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        super.onResume();
    }

    @Override
    protected void onPause(){
        datasource.close();
        super.onPause();
    }

    private boolean validate(){
        if(editLogName.getText().toString().trim().equals(""))
            return false;
        else
            return true;
    }

}