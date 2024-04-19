import java.io.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class ProjectDriver {
    private static College valenceCollege;
    private static Scanner scanner;

    public static void main(String[] args) {

    }
}
public void courseManagement(boolean value){
    System.out.println("Course Management Menu: ");
    System.out.println("Choose one of the following: ");
    System.out.println("A - Search for a class or lab using the class/lab number");
    System.out.println("B - Delete a class");
    System.out.println("C - Add a lab to a class");
    System.out.println("X - Back to Main Menu");
    while (!val){
        try{
            System.out.println("\t Please enter your selection:");
            String courseSelection = scanner.nextLine();
            val = true;
        }
        catch (Exception S){
            System.out.println("Invalid input given, please try again...");
            scanner.next();
        }
    }

}

abstract class Student {
    private String name;
    private String ID;

    public ArrayList<Course> getCoursesEnrolled() {
        return coursesEnrolled;
    }

    public void setCoursesEnrolled(ArrayList<Course> coursesEnrolled) {
        this.coursesEnrolled = coursesEnrolled;
    }

    private ArrayList<Course> coursesEnrolled;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    abstract public void printInvoice();

}

abstract class GraduateStudent extends Student {

}

class UndergraduateStudent extends Student {
    @Override
    public void printInvoice() {

    }
}

class MsStudent extends GraduateStudent {
    @Override
    public void printInvoice() {

    }
}

class PhdStudent extends GraduateStudent {
    @Override
    public void printInvoice() {

    }
}

class Course {
    private int classNumber, creditHours, numEnrolled;
    private String prefix, title, lectureLocation, modality, labLocation;
    private boolean isGrad, hasLab;


    public int getClassNumber() {
        return classNumber;
    }

    public void setClassNumber(int classNumber) {
        this.classNumber = classNumber;
    }

    public int getCreditHours() {
        return creditHours;
    }

    public void setCreditHours(int creditHours) {
        this.creditHours = creditHours;
    }

    public int getNumEnrolled() {
        return numEnrolled;
    }

    public void setNumEnrolled(int numEnrolled) {
        this.numEnrolled = numEnrolled;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLectureLocation() {
        return lectureLocation;
    }

    public void setLectureLocation(String lectureLocation) {
        this.lectureLocation = lectureLocation;
    }

    public String getModality() {
        return modality;
    }

    public void setModality(String modality) {
        this.modality = modality;
    }

    public String getLabLocation() {
        return labLocation;
    }

    public void setLabLocation(String labLocation) {
        this.labLocation = labLocation;
    }

    public boolean isGrad() {
        return isGrad;
    }

    public void setGrad(boolean grad) {
        isGrad = grad;
    }

    public boolean isHasLab() {
        return hasLab;
    }

    public void setHasLab(boolean hasLab) {
        this.hasLab = hasLab;
    }
    // constructor for non-online courses
    public Course (int classNumber, String prefix, String title, boolean isGrad,
                   String modality, String lectureLocation, boolean hasLab, int creditHours) {
        this.classNumber = classNumber;
        this.prefix = prefix;
        this.title = title;
        this.isGrad = isGrad;
        this.modality = modality;
        this.lectureLocation = lectureLocation;
        this.hasLab = hasLab;
        this.creditHours = creditHours;
        //implement lab addition feature here
    }

    // constructor for online courses
    public Course (int classNumber, String prefix, String title, boolean isGrad,
                   String modality, int creditHours) {
        this.classNumber = classNumber;
        this.prefix = prefix;
        this.title = title;
        this.isGrad = isGrad;
        this.modality = modality;
        this.creditHours = creditHours;
        // no need for lab addition bc class is online
    }

}

class IdException extends Exception {
    @Override
    public String getMessage() {
        return "Invalid id format or ID Already exists\nTry again later!";
    }
}

class College {
    private Scanner scanner;
    private ArrayList<Student> allStudents;
    private ArrayList<Student> phdStudents;
    private ArrayList<Student> msStudents;
    private ArrayList<Student> ugradStudents;
    private ArrayList<Course> courses;

    public College () {
        allStudents = new ArrayList<>();
        phdStudents = new ArrayList<>();
        msStudents = new ArrayList<>();
        ugradStudents = new ArrayList<>();
        courses = new ArrayList<>();
    }

    public void addStudent () {
        scanner = new Scanner(System.in);
        System.out.print("Enter Student's ID: ");
        String ID = scanner.nextLine();

        // verify that the ID passed in is both of the form
        // LetterLetterDigitDigitDigitDigit, and doesn't already exist
        String pattern = "^[A-Za-z]{2}\\d{4}$";
        Pattern regexPattern = Pattern.compile(pattern);
        Matcher matcher = regexPattern.matcher(ID);

        try {
            if (!matcher.matches()) {
                throw new IdException();
            } else if (searchByID(ID)) {
                throw new IdException();
            }
        } catch (IdException e) {
            e.getMessage();
            return;
        }

        // now that the ID has been verified, we may add it to the appropriate lists:
        System.out.print("Student Type (PhD, MS, or Undergrad ");
        String type = scanner.nextLine();
        String input = null, name, advisor, researchTopic, courseNums;
        String[] info;

        // check what type was inputted
        boolean badInput = true;
        while (badInput) {
            scanner = new Scanner(System.in);
            if (type.equals("PhD")) {
                System.out.println("Enter Remaining information");
                input = scanner.nextLine();
                info = input.split("\\|"); // ended here, give meaning to each array index to pass into phd
                name = info[0];
                advisor = info[1];
                researchTopic = info[2];
                courseNums = info[3];
                PhdStudent phdStudent = new PhdStudent();
                allStudents.add(phdStudent);
                phdStudents.add(phdStudent); //add remaining info before setting bI to false
                badInput = false;
            } else if (type.equals("MS")) {
                System.out.println("Enter Remaining information");
                input = scanner.nextLine();
                info = input.split("\\|");
            }
            // keep bI as true if type isn't one of the supported types
        }

    }

    public boolean searchByID (String ID) {
        for (Student student : allStudents) {
            if (student != null && student.getID().equals(ID)) {
                return true;
            }
        }
        return false;
    }
}