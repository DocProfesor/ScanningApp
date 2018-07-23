package StaffDB;

/**
 * Created by DocProf on 3/24/2015.
 */
public class Profile {
    private String name;
    private String company;
    private int id;

    //Setters and getters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id= id;
    }
}
