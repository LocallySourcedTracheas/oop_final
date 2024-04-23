//final project
//Group members: Christian Giovannetti, Vanessa Junco, Diana Ribot, Gabriela Komives-Prieto

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class ProjectDriver {
    private static College valenceCollege;
    private static Scanner scanner;

    public static void main(String[] args) {
        valenceCollege = new College();
        String selection = mainMenu();

        while (!selection.equalsIgnoreCase("0")) {
            scanner = new Scanner(System.in);
            switch (selection) {
                case "1":
                    studentMenu();
                    break;
                case "2":
                    courseMenu();
                    break;
                default:
                    System.out.println("Invalid input: Please enter either 1, 2, or 0!");
            } selection = mainMenu();
        }
        valenceCollege.writeToFile();
        System.out.println("\n\nTake Care!");
    }

    private static String mainMenu() {
        scanner = new Scanner(System.in);
        System.out.println("\nMain Menu\n");
        System.out.println("1 : Student Management");
        System.out.println("2 : Course Management");
        System.out.println("0 : Exit\n");
        System.out.print("\tEnter your selection: ");
        return scanner.nextLine();
    }

    private static String studentMenuHelper() {
        scanner = new Scanner(System.in);
        System.out.println("\nStudent Management Menu:\n");
        System.out.println("Choose one of:\n");
        System.out.println("\tA - Add a Student");
        System.out.println("\tB - Delete a Student");
        System.out.println("\tC - Print Fee Invoice");
        System.out.println("\tD - Print List of Students");
        System.out.println("\tE - Search for a Student");
        System.out.println("\tX - Back to Main Menu");
        System.out.print("\n\nEnter your selection: ");
        return scanner.nextLine();
    }

    private static void studentMenu() {
        String selection = studentMenuHelper();

        while (!selection.equalsIgnoreCase("x")) {
            scanner = new Scanner(System.in);
            switch (selection) {
                case "A":
                case "a":
                    // code for adding a student
                    valenceCollege.addStudent();
                    break;
                case "B":
                case "b":
                    // code for deleting a student
                    valenceCollege.deleteStudent();
                    break;
                case "C":
                case "c":
                    // code for printing fee invoice
                    valenceCollege.printFeeInvoice();
                    break;
                case "D":
                case "d":
                    // code for printing all student names
                    valenceCollege.printAllStudents();
                    break;
                case "E":
                case "e":
                    // code for searching for a student
                    System.out.print("Enter Student's ID: ");
                    String ID = scanner.nextLine();

                    // verify that the ID passed in is of the form
                    // LetterLetterDigitDigitDigitDigit
                    String pattern = "^[A-Za-z]{2}\\d{4}$";
                    Pattern regexPattern = Pattern.compile(pattern);
                    Matcher matcher = regexPattern.matcher(ID);

                    try {
                        if (!matcher.matches()) {
                            throw new IdException();
                        }
                    } catch (IdException e) {
                        System.out.println(e.getMessage());
                        return;
                    }

                    // now see if the ID even exists
                    if(valenceCollege.searchByID(ID)) {
                        System.out.println("Student found with ID: " + ID);
                    } else {
                        System.out.println("No student with ID: " + ID + " exists!");
                    }
                    break;
                default:
                    System.out.println("Invalid input: Please enter A, B, C, D, E or X!");

            } selection = studentMenuHelper();
        }
    }

    private static String courseMenuHelper() {
        scanner = new Scanner(System.in);
        System.out.println("\nCourse Management Menu:\n");
        System.out.println("Choose one of:\n");
        System.out.println("\tA - Search for a class or lab using the class/lab number");
        System.out.println("\tB - Delete a class");
        System.out.println("\tC - Add a lab to a class or add a Lecture");
        System.out.println("\tD - Print all classes (and their associated labs)");
        System.out.println("\tX - Back to main menu");
        System.out.print("\n\nEnter your selection: ");
        return scanner.nextLine();
    }

    private static void courseMenu() {
        String selection = courseMenuHelper();

        while (!selection.equalsIgnoreCase("x")) {
            scanner = new Scanner(System.in);
            switch (selection) {
                case "A":
                case "a":
                    // search code here
                    System.out.print("Enter the Class/Lab Number: ");
                    String ID = scanner.nextLine();
                    valenceCollege.searchByCRN(ID);
                    break;
                case "B":
                case "b":
                    // delete course code here
                    valenceCollege.deleteFromCourses();
                    break;
                case "C":
                case "c":
                    // add course code here
                    valenceCollege.addToCourses();
                    break;
                case "D":
                case "d":
                    // print course code here
                    valenceCollege.printCourses();
                    break;
                default:
                    System.out.println("Invalid input: Please enter A, B, C, D, or X!");

            } selection = courseMenuHelper();
        }
    }
}

abstract class Student {
    private String name;
    private String ID;
    private final double fee = 35.0;

    private ArrayList<String> coursesEnrolled;

    private LinkedHashMap<String, Course> courseLinkedHashMap;

    public double getFee() {
        return fee;
    }

    public LinkedHashMap<String, Course> getCourseLinkedHashMap() {
        return courseLinkedHashMap;
    }

    public void setCourseLinkedHashMap(LinkedHashMap<String, Course> courseLinkedHashMap) {
        this.courseLinkedHashMap = courseLinkedHashMap;
    }

    public ArrayList<String> getCoursesEnrolled() {
        return coursesEnrolled;
    }

    public void setCoursesEnrolled(ArrayList<String> coursesEnrolled) {
        this.coursesEnrolled = coursesEnrolled;
    }

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

    public Student (String name, String ID, ArrayList<String> coursesEnrolled) {
        this.name = name;
        this.ID = ID;
        this.coursesEnrolled = coursesEnrolled;
    }

    public void printHeader () {
        System.out.println("\nVALENCE COLLEGE");
        System.out.println("ORLANDO FL 10101");
        System.out.println("---------------------\n");
        System.out.println("Fee Invoice Prepared for Student:");
    }

    public double invoiceHelper (double studentCreditHour) {
        LinkedHashMap<String, Course> localCourseLHM = getCourseLinkedHashMap();
        printHeader();
        System.out.println(getID() + "-" + getName() + "\n");
        System.out.printf("1 Credit Hour = $ %.2f\n\n", studentCreditHour);
        System.out.println("CRN\t\tCR_PREFIX\t\tCR_HOURS");
        double total = 0;

        for (String lectureCRN : getCoursesEnrolled()) {
            Lecture lecture = (Lecture) localCourseLHM.get(lectureCRN);
            System.out.printf("%s\t%s\t\t\t%d\t\t\t$ %.2f\n",
                    lecture.getCrn(), lecture.getPrefix(), lecture.getCreditHours(), studentCreditHour * lecture.getCreditHours());
            total += studentCreditHour * lecture.getCreditHours();
        }
        System.out.printf("\n\t\t\t\tHealth & id fees\t$ %.2f\n\n", getFee());
        System.out.println("--------------------------------------------");
        return total + getFee();
    }


    abstract public void printInvoice();

}

abstract class GraduateStudent extends Student {
    public GraduateStudent (String name, String ID, ArrayList<String> coursesEnrolled) {
        super(name, ID, coursesEnrolled);
    }
}

class UndergraduateStudent extends Student {
    private double gpa;
    private boolean isResident;
    private double ugradCreditHour = 120.25;
    public UndergraduateStudent (String name, String ID, double gpa, boolean isResident, ArrayList<String> coursesEnrolled) {
        super(name, ID, coursesEnrolled);
        this.gpa = gpa;
        this.isResident = isResident;
        if (!isResident) {
            ugradCreditHour *= 2;
        }
    }
    @Override
    public void printInvoice() {
        double total = invoiceHelper(ugradCreditHour);
        if (gpa >= 3.5 && total > 500.0) {
            double discountedTotal = .25 * total;
            System.out.println("\t\t\t\t\t\t\t\t\t$ " + total);
            System.out.printf("\t\t\t\t\t\t\t\t   -$ %.2f\n",discountedTotal);
            System.out.println("\t\t\t\t\t\t\t\t  ----------");
            total -= discountedTotal;
        }
        System.out.printf("\t\t\t\tTOTAL PAYMENTS\t\t$ %.2f\n\n", total);
    }
}

class MsStudent extends GraduateStudent {

    public MsStudent (String name, String ID, ArrayList<String> coursesEnrolled) {
        super(name, ID, coursesEnrolled);
    }
    private final double msCreditHour = 300.0;
    @Override
    public void printInvoice() {
        double total = invoiceHelper(msCreditHour);
        System.out.printf("\t\t\t\tTOTAL PAYMENTS\t\t$ %.2f\n\n", total);
    }
}

class PhdStudent extends GraduateStudent {
    private String advisor;
    private String researchTopic;
    private double researchFee = 700.0;
    public PhdStudent (String name, String ID, String advisor, String researchTopic, ArrayList<String> labsSupervising) {
        super(name, ID,null);
        this.advisor = advisor;
        this.researchTopic = researchTopic;
        this.labsSupervising = labsSupervising;
    }
    private ArrayList<String> labsSupervising;

    public ArrayList<String> getLabsSupervising() {
        return labsSupervising;
    }

    public void setLabsSupervising(ArrayList<String> labsSupervising) {
        this.labsSupervising = labsSupervising;
    }

    @Override
    public void printInvoice() {
        printHeader();
        System.out.println(getID() + "-" + getName() + "\n");
        System.out.println("RESEARCH");
        if (labsSupervising.size() > 2) {
            researchFee = 0;
        } else if (labsSupervising.size() == 2) {
            researchFee *= .5;
        }

        System.out.println(researchTopic + "\t\t\t\t\t\t\t\t$ " + researchFee);
        System.out.printf("\n\t\t\t\tHealth & id fees\t$ %.2f\n\n", getFee());
        System.out.println("--------------------------------------------");
        System.out.printf("\t\t\t\tTOTAL PAYMENTS\t\t$ %.2f\n\n", getFee() + researchFee);
    }
}

abstract class Course {
    // a class that enables lectures and labs to be added to a single hashMap
}

class Lecture extends Course{

    private String crn;
    private String prefix;
    private String name;

    private String type; // is it grad or ugrad
    private String modality;
    private boolean hasLabs;
    private int creditHours;

    private ArrayList<String> labCRNS; // store only the CRNS of labs here, not actual Lab objects
    private ArrayList<String> studentsTakingCourse;

    private String classroom;

    public ArrayList<String> getStudentsTakingCourse() {
        return studentsTakingCourse;
    }

    public void setStudentsTakingCourse(ArrayList<String> studentsTakingCourse) {
        this.studentsTakingCourse = studentsTakingCourse;
    }

    public String getCrn() {
        return crn;
    }

    public void setCrn(String crn) {
        this.crn = crn;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getModality() {
        return modality;
    }

    public void setModality(String modality) {
        this.modality = modality;
    }

    public boolean isHasLabs() {
        return hasLabs;
    }

    public void setHasLabs(boolean hasLabs) {
        this.hasLabs = hasLabs;
    }

    public int getCreditHours() {
        return creditHours;
    }

    public void setCreditHours(int creditHours) {
        this.creditHours = creditHours;
    }

    public ArrayList<String> getLabCRNS() {
        return labCRNS;
    }

    public void setLabCRNS(ArrayList<String> labCRNS) {
        this.labCRNS = labCRNS;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    // Non-online with Labs
    public Lecture(String crn, String prefix, String name, String type, String modality,
                   String classroom, boolean hasLabs, int creditHours, ArrayList<String> labCRNS) {
        // start here
        this.crn = crn;
        this.prefix = prefix;
        this.name = name;
        this.type = type;
        this.modality = modality;
        this.hasLabs = hasLabs;
        this.classroom = classroom;
        this.creditHours = creditHours;
        this.labCRNS = labCRNS;
        this.studentsTakingCourse = new ArrayList<>();
    }

    // Non-online without Labs
    public Lecture(String crn, String prefix, String name, String type, String modality,
                   String classroom, boolean hasLabs, int creditHours) {
        // start here
        this.crn = crn;
        this.prefix = prefix;
        this.name = name;
        this.type = type;
        this.modality = modality;
        this.hasLabs = hasLabs;
        this.classroom = classroom;
        this.creditHours = creditHours;
        this.labCRNS = null;
        this.studentsTakingCourse = new ArrayList<>();
    }

    // Online Lectures
    public Lecture(String crn, String prefix, String name, String type, String modality, int creditHours) {
        this.crn = crn;
        this.prefix = prefix;
        this.name = name;
        this.type = type;
        this.modality = modality;
        this.creditHours = creditHours;
        this.labCRNS = null;
        this.studentsTakingCourse = new ArrayList<>();
        this.hasLabs = false;
    }

    // 89745,COT6578,Advanced Computer theory,Graduate,F2F,PSY-108,No,4
    public String toString() {
        if (modality.replaceAll("\\s", "").equalsIgnoreCase("online")) {
            return crn + "," + prefix + "," + name + "," + type + "," + modality + "," + creditHours;
        } else {
            return crn + "," + prefix + "," + name + "," + type + "," + modality + "," + classroom
                    + "," + (hasLabs ? "Yes" : "No") + "," + creditHours;
        }
    }
}

class Lab extends Course {
    private String courseCRN;
    private String labCRN;

    private String location;

    private ArrayList<String> phdSupervising;

    public ArrayList<String> getPhdSupervising() {
        return phdSupervising;
    }

    public void setPhdSupervising(ArrayList<String> phdSupervising) {
        this.phdSupervising = phdSupervising;
    }

    public String getLabCRN() {
        return labCRN;
    }

    public void setLabCRN(String labCRN) {
        this.labCRN = labCRN;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCourseCRN() {
        return courseCRN;
    }

    public void setCourseCRN(String courseCRN) {
        this.courseCRN = courseCRN;
    }

    public Lab (String labCRN, String location, String courseCRN) {
        this.labCRN = labCRN;
        this.location = location;
        this.courseCRN = courseCRN;
        this.phdSupervising = new ArrayList<>();
    }

    public String toString () {
        return (labCRN + "," + location).replaceAll("\\s", "");
    }
}

class College {
    private LinkedHashMap<String, Course> courseHashMap;
    private LinkedHashMap<String, Student> studentHashMap;
    private Scanner scanner;

    public College () {
        courseHashMap = new LinkedHashMap<>();
        studentHashMap = new LinkedHashMap<>();
        scanInFile();
    }

    private void scanInFile() {
        String line;
        String[] items;
        String tempLine, lastLabCRN = null;
        String[] tempItems;
        ArrayList<String> tempList = new ArrayList<>();
        try {
            scanner = new Scanner(new File("lec.txt"));
        } catch (FileNotFoundException e) {
            System.out.println("Is the file named \"lec.txt\" and is the file inside the correct directory?");
        }

        while (scanner.hasNextLine()) {
            tempList = new ArrayList<>();
            line = scanner.nextLine();
            items = line.split(",");
            if (items[4].replaceAll("\\s", "").equalsIgnoreCase("online")) {
                courseHashMap.put(items[0], new Lecture(items[0], items[1],
                        items[2], items[3], items[4], Integer.parseInt(items[5])));
            } else if (items[6].replaceAll("\\s", "").equalsIgnoreCase("no")) {
                courseHashMap.put(items[0],
                        new Lecture(items[0], items[1], items[2],
                                items[3], items[4], items[5], false, Integer.parseInt(items[7])));
            } else {
                // add the labs and stop when a course is found
                while (scanner.hasNextLine()) {
                    tempLine = scanner.nextLine();
                    tempItems = tempLine.split(",");
                    if (tempItems.length > 2) {
                        break; // we just encountered a course, so get out
                    }
                    courseHashMap.put(tempItems[0], new Lab(tempItems[0], tempItems[1], items[0]));
                    tempList.add(tempItems[0]); // add it also to the lab list
                    lastLabCRN = tempItems[0];
                }

                // add the actual lecture
                courseHashMap.put(items[0],
                        new Lecture(items[0], items[1], items[2], items[3],
                                items[4], items[5], true, Integer.parseInt(items[7]), tempList));

                scanner.close();
                // reopen the scanner
                try {
                    scanner = new Scanner(new File("lec.txt"));
                } catch (FileNotFoundException e) {
                    System.out.println("Is the file named \"lec.txt\" and is the file inside the correct directory?");
                }
                // get to the correct place in the file
                while (scanner.hasNextLine()) {
                    line = scanner.nextLine();
                    items = line.split(",");
                    if (items[0].equals(lastLabCRN)) {
                        break; // we are at the correct line
                    }
                }
            }
        }
        scanner.close();

    }

    public void searchByCRN (String CRN) {
        if (courseHashMap.containsKey(CRN)) {
            if (courseHashMap.get(CRN) instanceof Lecture) {
                Lecture lec = (Lecture) courseHashMap.get(CRN);
                System.out.println("[ " + lec.getCrn() + ", " + lec.getPrefix() + ", " + lec.getName() + " ]");
            } else {
                Lab lab = (Lab) courseHashMap.get(CRN);
                Lecture lec = (Lecture) courseHashMap.get(lab.getCourseCRN());
                System.out.println("Lab for " + "[ " + lec.getCrn() + ", " + lec.getPrefix() + ", " + lec.getName() + " ]");
                System.out.println("Lab Room " + lab.getLocation());
            }
        } else {
            System.out.println("[ ]");
        }
    }

    public void addToCourses () {
        scanner = new Scanner(System.in);
        System.out.print("Enter Lab or Lecture: ");
        String choice = scanner.nextLine();
        String lecCRN;
        String input;
        String[] items;
        if (choice.equalsIgnoreCase("lab")) {
            System.out.println("Enter the Lecture Number to add a Lab to: ");
            lecCRN = scanner.nextLine();
            if (courseHashMap.containsKey(lecCRN) && courseHashMap.get(lecCRN) instanceof Lecture) {
                // add to lecture
                Lecture lec = (Lecture) courseHashMap.get(lecCRN);

                if (!lec.isHasLabs()) {
                    System.out.println("Cannot add a lab to a course with labs disabled!");
                    return;
                }

                System.out.println(lecCRN + " (" + lec.getName() + ") is a valid course. " +
                        "Enter the rest of the information in the form of labCRN,location." );
                input = scanner.nextLine();
                items = input.split(",");
                // add to hashMap
                courseHashMap.put(items[0], new Lab(items[0], items[1], lecCRN));
                // update the lecture object's lab list
                lec.getLabCRNS().add(items[0]);
                System.out.println("Lab added.");
            } else {
                System.out.println("No Such Lecture Exists!");
            }
        } else if (choice.equalsIgnoreCase("lecture")) {
            System.out.print("Enter the Lecture Number: ");
            lecCRN = scanner.nextLine();
            if (!courseHashMap.containsKey(lecCRN)) {
                System.out.println(lecCRN + " is valid. " +
                        "Enter the rest of the information in form of one of the following:\n");
                System.out.println("If course is not online: \tprefix,name,type,modality,location,yes/no,creditHours");
                System.out.println("If course is online: \tprefix,name,type,modality,creditHours");
                input = scanner.nextLine();
                items = input.split(",");
                if (items[3].replaceAll("\\s", "").equalsIgnoreCase("online")) {
                    courseHashMap.put(lecCRN, new Lecture(lecCRN, items[0], items[1],
                            items[2], items[3], Integer.parseInt(items[4].replaceAll("\\s", ""))));
                } else {
                    boolean hasLab;
                    hasLab = items[5].replaceAll("\\s", "").equalsIgnoreCase("yes");
                    courseHashMap.put(lecCRN, new Lecture(lecCRN, items[0], items[1],
                            items[2], items[3], items[4], hasLab, Integer.parseInt(items[6].replaceAll("\\s", ""))));
                }
                System.out.println("\nCourse added.");
            } else {
                System.out.println("A lecture with that number already exists!");
            }
        } else {
            System.out.println("Invalid input: Please enter either Lecture or Lab!");
        }
    }

    public void printCourses () {
        for (Map.Entry<String, Course> entry : courseHashMap.entrySet()) {
            Course temp1 = entry.getValue();
            if (temp1 instanceof Lecture) {
                Lecture tempLec = (Lecture) temp1;
                System.out.println(tempLec);
                if (tempLec.isHasLabs()) {
                    for (String crn : tempLec.getLabCRNS()) {
                        System.out.println(courseHashMap.get(crn));
                    }
                }
            }
        }
    }

    public void deleteFromCourses () {
        scanner = new Scanner(System.in);
        System.out.print("Enter the Class/Lab Number: ");
        String courseCRN = scanner.nextLine();

        Course course = courseHashMap.get(courseCRN);
        if (course == null) {
            System.out.println("No Lecture or Lab exists with that CRN!");
            return;
        }

        if (course instanceof Lecture) {
            Lecture lecture = (Lecture) course;
            if (lecture.getStudentsTakingCourse().isEmpty()) {
                if (lecture.isHasLabs()) {
                    System.out.println("\nCannot delete course with labs enabled!");
                    return;
                } else {
                    courseHashMap.remove(courseCRN);
                    System.out.println("\nCourse deleted.");
                }
            } else {
                System.out.println("\nCannot delete course when one or more students are enrolled in it!");
            }
        } else {
            deleteLab(courseCRN);
        }

    }

    public void deleteLab (String labCrn) {
        Lab lab = (Lab) courseHashMap.get(labCrn);
        // del from phd coursesSupervising list
        for (String phdCRN : lab.getPhdSupervising()) {
            ((PhdStudent) studentHashMap.get(phdCRN)).getLabsSupervising().remove(labCrn);
        }
        courseHashMap.remove(labCrn);
        System.out.println("Lab deleted.");
    }

    // Student stuff starts here
    //_________________________________________________________________________________________________________________

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
            System.out.println(e.getMessage());
            return;
        }

        // now that the ID has been verified, we may add it to the appropriate lists:
        System.out.print("Student Type (PhD, MS, or Undergrad): ");
        String type = scanner.nextLine();
        String input = null;
        String name, advisor, researchTopic;
        boolean isResident, badInput = true;
        double gpa = 0;
        String[] items, labCRNS, courseCRNS;

        while (badInput) {
            scanner = new Scanner(System.in);
            if (type.equalsIgnoreCase("phd")) {
                System.out.println("Enter Remaining information in the form: name|advisor|researchTopic|labCRN1,labCRN2,...,labCRNX");
                input = scanner.nextLine();
                // get rid of unnecessary spaces
                input = input.replaceAll("\\s\\|", "|").replaceAll("\\|\\s", "|");
                items = input.split("\\|");
                labCRNS = items[3].replaceAll("\\s", "").split(",");
                ArrayList<String> labArrayList = new ArrayList<>(Arrays.asList(labCRNS));
                studentHashMap.put(ID, new PhdStudent(items[0], ID, items[1], items[2], labArrayList));

                // update labs
                for (String labCRN : labArrayList) {
                    Lab lab = (Lab) courseHashMap.get(labCRN);
                    lab.getPhdSupervising().add(ID);
                }

                badInput = false;
            } else if (type.equalsIgnoreCase("ms")) {
                System.out.println("Enter Remaining information in the form: name|lectureCRN1,lectureCRN2,...,lectureCRNX");
                System.out.println("Note that course CRNs that belong to undergraduate courses will be ignored.");
                input = scanner.nextLine();
                // get rid of unnecessary spaces
                input = input.replaceAll("\\s\\|", "|").replaceAll("\\|\\s", "|");
                items = input.split("\\|");
                courseCRNS = items[1].replaceAll("\\s", "").split(",");
                ArrayList<String> lectureArrayList = new ArrayList<>();

                // check to see if all of these courses are indeed grad courses
                for (String courseCRN : courseCRNS) {
                    Lecture lecture = (Lecture) courseHashMap.get(courseCRN);
                    if (lecture.getType().replaceAll("\\s", "").equalsIgnoreCase("graduate")) {
                        lectureArrayList.add(courseCRN);
                    }
                }

                // update courses
                for (String lectureCRN : lectureArrayList) {
                    Lecture lecture = (Lecture) courseHashMap.get(lectureCRN);
                    lecture.getStudentsTakingCourse().add(ID);
                }

                studentHashMap.put(ID, new MsStudent(items[0], ID, lectureArrayList));

                badInput = false;
            } else if (type.equalsIgnoreCase("undergrad")) {
                System.out.println("Enter Remaining information in the form: name|gpa|true/false|lectureCRN1,lectureCRN2,...,lectureCRNX");
                System.out.println("Note that course CRNs that belong to graduate courses will be ignored.");
                input = scanner.nextLine();
                // get rid of unnecessary spaces
                input = input.replaceAll("\\s\\|", "|").replaceAll("\\|\\s", "|");
                items = input.split("\\|");
                courseCRNS = items[3].replaceAll("\\s", "").split(",");
                ArrayList<String> lectureArrayList = new ArrayList<>();

                // check to see if all of these courses are indeed ugrad courses
                for (String courseCRN : courseCRNS) {
                    Lecture lecture = (Lecture) courseHashMap.get(courseCRN);
                    if (lecture.getType().replaceAll("\\s", "").equalsIgnoreCase("undergraduate")) {
                        lectureArrayList.add(courseCRN);
                    }
                }

                // update courses
                for (String lectureCRN : lectureArrayList) {
                    Lecture lecture = (Lecture) courseHashMap.get(lectureCRN);
                    lecture.getStudentsTakingCourse().add(ID);
                }

                studentHashMap.put(ID, new UndergraduateStudent(items[0], ID, Double.parseDouble(items[1]),
                        Boolean.parseBoolean(items[2].replaceAll("\\s", "")), lectureArrayList));

                badInput = false;
            } else {
                System.out.println("Invalid selection, please enter either PhD, MS, or Undergrad!");
            }
        }
    }

    public void deleteStudent() {
        scanner = new Scanner(System.in);
        System.out.print("Enter Student's ID: ");
        String ID = scanner.nextLine();

        // verify that the ID passed in is of the form
        // LetterLetterDigitDigitDigitDigit
        String pattern = "^[A-Za-z]{2}\\d{4}$";
        Pattern regexPattern = Pattern.compile(pattern);
        Matcher matcher = regexPattern.matcher(ID);

        try {
            if (!matcher.matches()) {
                throw new IdException();
            }
        } catch (IdException e) {
            System.out.println(e.getMessage());
            return;
        }

        // now see if the ID even exists
        if(!searchByID(ID)) {
            System.out.println("No student with ID: " + ID + " exists!");
            return;
        }

        // deal with both the phd case and the ms/ugrad case

        Student student = studentHashMap.get(ID);
        // phd case
        if (student instanceof PhdStudent) {
            PhdStudent phdStudent = (PhdStudent) student;

            // update Labs this student is supervising
            for (String labCRN : phdStudent.getLabsSupervising()) {
                Lab lab = (Lab) courseHashMap.get(labCRN);
                lab.getPhdSupervising().remove(ID);
            }

            studentHashMap.remove(ID);
        } else {
            // ms/ugrad case
            for (String lectureCRN : student.getCoursesEnrolled()) {
                Lecture lecture = (Lecture) courseHashMap.get(lectureCRN);
                lecture.getStudentsTakingCourse().remove(ID);
            }

            studentHashMap.remove(ID);
        }

    }

    public boolean searchByID (String ID) {
        return studentHashMap.containsKey(ID);
    }

    public void printAllStudents () {
        System.out.println("PhD Students");
        System.out.println("------------");
        for (Map.Entry<String, Student> entry : studentHashMap.entrySet()) {
            Student student = entry.getValue();
            if (student instanceof PhdStudent) {
                System.out.println("\t- " + student.getName());
            }
        }

        System.out.println("\nMS Students");
        System.out.println("-----------");
        for (Map.Entry<String, Student> entry : studentHashMap.entrySet()) {
            Student student = entry.getValue();
            if (student instanceof MsStudent) {
                System.out.println("\t- " + student.getName());
            }
        }

        System.out.println("\nUndergraduate Students");
        System.out.println("----------------------");
        for (Map.Entry<String, Student> entry : studentHashMap.entrySet()) {
            Student student = entry.getValue();
            if (student instanceof UndergraduateStudent) {
                System.out.println("\t- " + student.getName());
            }
        }
    }

    public void printFeeInvoice () {
        scanner = new Scanner(System.in);
        System.out.print("Enter the ID of the student: ");
        String studentID = scanner.nextLine();
        if (studentHashMap.containsKey(studentID)) {
            Student student = studentHashMap.get(studentID);
            student.setCourseLinkedHashMap(courseHashMap);
            student.printInvoice();
        } else {
            System.out.println("No student exists with that ID!");
        }
    }

    public void writeToFile () {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("lec.txt"));
            for (Map.Entry<String, Course> entry : courseHashMap.entrySet()) {
                Course course = entry.getValue();
                if (course instanceof Lecture) {
                    Lecture lecture = (Lecture) course;
                    writer.write(lecture.toString());
                    writer.newLine();

                    if (lecture.isHasLabs()) {
                        for (String crn : lecture.getLabCRNS()) {
                            Course labCourse = courseHashMap.get(crn);
                            if (labCourse != null) {
                                writer.write(labCourse.toString());
                                writer.newLine();
                            }
                        }
                    }
                }
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("There was a problem writing to \"lec.txt\"");
        }
    }

}

class IdException extends Exception {
    @Override
    public String getMessage() {
        return "Invalid id format or ID Already exists\nTry again later!";
    }
}