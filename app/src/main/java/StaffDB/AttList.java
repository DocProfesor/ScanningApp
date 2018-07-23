package StaffDB;

import org.apache.http.entity.StringEntity;

import java.util.ArrayList;

/**
 * Created by DocProf on 5/11/2015.
 */
public class AttList {

    private int listLenght;
    private boolean completed;
    private String username;
    private String password;
    private String DATABASE_NAME;

    private ArrayList<Profile> AttendanceSheet;

    public AttList(String DATABASE_NAME, String username, String password){
        this.username = username;
        this.password = password;
        this.DATABASE_NAME = DATABASE_NAME;
    }

    public void addStaff (Profile staff){
        AttendanceSheet.add(staff);
    }

    public void addStaff (String staff){

    }

    public int getListLenght() {
        return listLenght;
    }

    public void setListLenght(int listLenght) {
        this.listLenght = listLenght;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDATABASE_NAME() {
        return DATABASE_NAME;
    }

    public void setDATABASE_NAME(String DATABASE_NAME) {
        this.DATABASE_NAME = DATABASE_NAME;
    }
}
