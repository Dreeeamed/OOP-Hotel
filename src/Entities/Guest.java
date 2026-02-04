package Entities;
public class Guest {
    private int id;
    private String fullName;
    private String email;

    //DEFAULT CONSTRUCTOR
    public Guest() {
        this.id = 0;
    } //Etot Part useless tbh, no ok

    //PARAMETER CONSTRUCTOR
    public Guest(String fullName, String email) {
        this();
        setName(fullName);
        setEmail(email);
    }

    //ENCAPSULATION
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return fullName; }
    public void setName(String fullName) {
        if(fullName == null || fullName.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        this.fullName = fullName; }

    public String getEmail() { return email; }

    public void setEmail(String email) {
        if(email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        this.email = email; }


    //OTHER THINGS
    @Override
    public String toString() {
        return "id=" + id + ", name=" + fullName;
    }
}