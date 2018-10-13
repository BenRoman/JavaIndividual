import java.io.*;
import java.io.FileInputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Date;


class Person {

    // region fields
    private String Name;

    private String Email;
    private String Login;
    private String Pass;
    private String Org;
    private Date Birth;

    //endregion
    //region Get Set
    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getLogin() {
        return Login;
    }

    public void setLogin(String login) {
        Login = login;
    }

    public String getPass() {
        return Pass;
    }

    public void setPass(String pass) {
        Pass = pass;
    }

    public String getOrg() {
        return Org;
    }

    public void setOrg(String org) {
        Org = org;
    }

    public Date getBirth() {
        return Birth;
    }

    public void setBirth(Date birth) {
        Birth = birth;
    }
    //endregion

    @Override
    public String toString() {
        return Name +" - "+ Email+" - "+Login +" - "+ Pass+" - "+Org+" - "+ Birth;
    }
    public Person(String name , String email , String login , String pass , String org , Date birth){
        this.Name = name;
        this.Email = email;
        this.Login =login;
        this.Pass = pass;
        this.Org = org;
        this.Birth = birth;
    }
    public static Comparator<? super Person> NameLogin = new Comparator<Person>() {
        @Override
        public int compare(Person o1, Person o2) {
            if(o1.getName().equals(o2.getName()))
                return o1.getLogin().compareTo(o2.getLogin());
            return o1.getName().compareTo(o2.getName());

        }
    } ;
    public static Comparator<? super Person> LoginPass = new Comparator<Person>() {
        @Override
        public int compare(Person o1, Person o2) {
            if(!o1.getLogin().equals(o2.getLogin()))
                return o1.getLogin().compareTo(o2.getLogin());
            return o1.getPass().compareTo(o2.getPass());

        }
    } ;
    public static Comparator<? super Person> BirthName = new Comparator<Person>() {
        @Override
        public int compare(Person o1, Person o2) {
            if(o1.getBirth().equals(o2.getBirth()))
                return o1.getName().compareTo(o2.getName());
            return o1.getBirth().compareTo(o2.getBirth());

        }
    } ;
}

public class Main {

    public static ArrayList<Person> Persons = new ArrayList<Person>();
    public static ArrayList<Person> ValidatedPersons = new ArrayList<Person>();
    public static Map<Person , String> InvalidatedPersons = new HashMap<Person , String>();

    public static void Read() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d yyyy", Locale.ENGLISH);
        try (FileInputStream fin = new FileInputStream("C:\\Users\\rombe\\Desktop\\forJava.txt")) {
            BufferedReader br = new BufferedReader(new InputStreamReader(fin));
            String s;
            Date itemDate = new Date();
            while ((s = br.readLine()) != null) {
                s = s.replaceAll("\\s{2,}", " ").trim();
                String[] var = s.split(" ");
                try{
                    itemDate = java.sql.Date.valueOf(LocalDate.parse( var[6]+" "+var[7]+" "+var[8], formatter));
                }
                catch(Exception ex){
                    System.out.println(ex.getMessage());
                }
                Persons.add(new Person(var[0] + " " + var[1], var[2], var[3], var[4], var[5], itemDate));
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        Separate();
    }
    public static void Separate(){
        for (Person item: Persons) {
            String res = Validator(item);
            if(res == "")
                ValidatedPersons.add(item);
            else
                InvalidatedPersons.put(item, res);
        }
    }
    public static void SortAndWrite(){
        final String ANSI_RED = "\u001B[31m";
        final String ANSI_RESET = "\u001B[0m";
        ValidatedPersons.sort(Person.NameLogin);
        System.out.println(ANSI_RED +"Valid : ("+ValidatedPersons.size()+")" + ANSI_RESET);
        for (Person itm : ValidatedPersons)
            System.out.println(itm);

        System.out.println(ANSI_RED +"Invalid : ("+ InvalidatedPersons.size()+")" + ANSI_RESET);
        for (Map.Entry<Person, String> entry : InvalidatedPersons.entrySet()) {
            Person key = entry.getKey();
            String value = entry.getValue();

            System.out.println ("Key: " + key +"\n" +  "Value: " + value +"\n") ;
        }
    }
    public static String Validator(Person test ){
        String reasons ="";
        Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(test.getEmail());
        if(!matcher.find())
            reasons+="email ";

        Pattern VALID_LOGIN_REGEX = Pattern.compile("^[a-z0-9]+$");
        matcher = VALID_LOGIN_REGEX .matcher(test.getLogin());
        if(!matcher.find())
            reasons+= "login ";

        Pattern VALID_PASS_REGEX = Pattern.compile("[A-Z0-9_.%+?!-]{10,}", Pattern.CASE_INSENSITIVE);
        matcher = VALID_PASS_REGEX .matcher(test.getPass());
        if(!matcher.find())
            reasons+= "pass ";
        Pattern VALID_NAME_REGEX = Pattern.compile("[A-Zа-я]{1,}", Pattern.CASE_INSENSITIVE);
        matcher = VALID_NAME_REGEX .matcher(test.getName());
        if(!matcher.find())
            reasons+= "name";
        return reasons;
    }

    public static void main(String[] args) {
        Read();
        SortAndWrite();
    }
}
