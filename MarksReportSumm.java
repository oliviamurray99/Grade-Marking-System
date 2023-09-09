/** MarksReport
 * User can enter names and marks of students manually and
 * have them saved to a file, or use a file that already exists.
 * These files are used to generate a marks report of
 * the highest and lowest marks, honor roll students and
 * failed students and then display it to the user.


 *@authorOliviaMurray
 *@date 19 04 2023
 */


//imports
import java.io.*;
import java.util.Scanner;
import java.io.IOException;
import java.util.ArrayList;

//main class MarksReportSumm
public class MarksReportSumm {

    /**
     * This method asks user for file, verifies if file
     * exists and displays highest, lowest marks, honour and failed student marks
     * from file. It reads students marks from CSV file and stores them in array.
     * @param marks Students marks
     * @throws IOException
     */
    public static void readFromFile(int[] marks) throws IOException {

        //initialize variables
        int num = 0;
        Scanner scanner = new Scanner(System.in);
        int studentNum = 0;
        String text;
        BufferedReader br;


        //user enters name of file and file existence is verified
        System.out.print("\nEnter the name of the CSV file (or hit enter to end): ");
        String fileName = scanner.nextLine();
        if (fileName.equals("")) {
            return;
        }//end if
        boolean fileExistorNot = fileExists(fileName);
        if (fileExistorNot) {
            System.out.println("File EXISTS\n");
        }//end if
        if (fileExistorNot == false) {
            System.out.println("File DOES NOT EXIST");
            return;
        }//end if


        //reads all text in file and displays it
        BufferedReader in = new BufferedReader(new FileReader(fileName));
        ArrayList<String> lines = new ArrayList<>();

        //ensures that first line of file (which is a number) is not included
        boolean isFirstline = true;
        while ((text = in.readLine()) != null) {
            if( !isFirstline ){
                lines.add(text);
                num++;
                System.out.println(text);
            }//end if
            isFirstline = false;
        }//end while


        // Read the marks from the ArrayList
        for (int i = 0; i < num; i++) {
            String[] data = lines.get(i).split(",");
            int mark = Integer.parseInt(data[2]);
            if (mark < 0 || mark > 100) {
                System.err.println("INVALID mark on line " + (i+1) + ": " + mark);
                return;
            }//end if
            marks[i] = mark;
        }//end for
        in.close();

        // After reading the marks from the file, generate the mark report
        doMarkReport(fileName, studentNum);



    }//public static void readFromFile(int[]marks)throws IOException{}


    /**
     * This method allows user to manually enter
     * students marks and name into csv file.
     * @return studentNum
     * @throws IOException
     */
    public static int manualEntry() throws IOException {
        //initialize variables
        int studentNum=0;
        int marks=0;
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the name of the CSV file: ");
        System.out.println("");
        String fileName = scanner.nextLine();

        //if filename does not end with csv, add it on end
        if (fileName.endsWith(".csv") != true) {
            fileName = (fileName + ".csv");

        }//end if

        File file = new File(fileName);

        // make sure file does not exist already and create new file
        if (file.exists()) {
            System.out.println("\nFile already exists.");
        } else {
            try {
                file.createNewFile();
                System.out.println("FILE: " + fileName+ " created successfully");
                System.out.println("");
            } finally {

            }
        }//end else


        //user writes data to file (names, marks)
        PrintWriter out;
        out = new PrintWriter(new FileWriter(fileName));


            System.out.println("How many students are in your class:");
            studentNum = scanner.nextInt();

            out.println(studentNum);


        // Loop to get data for each student
        for (int i = 0; i < studentNum; i++) {
            System.out.println("Last name: \n");
            String lastName = scanner.next();

            System.out.println("First name: \n");
            String firstName = scanner.next();

            //error trap to ensure teacher only enters marks from 1-100
            do {
                System.out.println("Mark: \n");
                marks = scanner.nextInt();
                if (marks >=0 && marks <=100){
                    break;
                }else{
                    System.err.print("INVALID MARK (1-100)\n");
                }//end else
            }while(true); //end while

            // Write data to the CSV file
            out.println(lastName + "," + firstName + "," + marks);


        }//end for

        // Close PrintWriter to flush and close the file
        out.close();
        //generate marks report
        doMarkReport(fileName, studentNum);


        return studentNum;
    }//public static int manualEntry() throws IOException


    /**
     * This method makes sure a file exists
     * @param fileName name of file
     * @return fleExists
     */
    public static boolean fileExists(String fileName) {
        boolean fileExists = new File(fileName).exists();
        return fileExists;

    }//public static boolean fileExists(String fileName)


    /**
     * This method shows the marks report for the csv file
     * @param fileName name of file
     * @param studentNum number of students
     * @throws IOException
     */
    public static void doMarkReport(String fileName, int studentNum) throws IOException {
        //initializes variables
        int num;
        String text = null;

        // reads from file
        BufferedReader in = new BufferedReader((new FileReader(fileName)));
        num = Integer.parseInt(in.readLine());

        // a series of arrays to hold students information
        String[] lastName = new String[num];
        String[] firstName = new String[num];
        int[] classAverage = new int[num];
        int[] highestMark = new int[num];
        int[] lowestMark = new int[num];
        int[] HonourRoll = new int[num];
        int[] FailedMarks = new int[num];
        int[] marks = new int[num];


        // This loop reads data from a file and stores it in arrays for further processing
        for (int i = 0; i < num; ++i) {


            text = in.readLine();
            String[] data = text.split(",");

            lastName[i] = data[0];
            firstName[i] = data[1];
            marks[i] = Integer.parseInt(data[2]);


            // check if there are more data fields in the line, and if so, store them in their proper arrays
            if (data.length >= 3) {
                classAverage[i] = Integer.parseInt(data[2]);
            }
            if (data.length >= 4) {
                highestMark[i] = Integer.parseInt(data[3]);
            }
            if (data.length >= 5) {
                lowestMark[i] = Integer.parseInt(data[4]);
            }
            if (data.length >= 6) {
                HonourRoll[i] = Integer.parseInt(data[5]);
            }
            if (data.length >= 7) {
                FailedMarks[i] = Integer.parseInt(data[6]);
            }
        }//end for


        in.close();



    //call methods to display marks report
    double avg = calculateClassAverage(classAverage, num);
        System.out.println("");
        System.out.println("-----------------------------------");
        System.out.printf("Class Average: %.2f %% %n", avg );


    displayHighestMark(lastName, firstName, classAverage, num);

    displayLowestMark(lastName, firstName, classAverage, num);

    int honorRoll1=displayHonorRoll(marks, lastName, firstName);
    System.out.println("");
    System.out.println(honorRoll1 + " honor roll student(s)");
    System.out.println("-----------------------------------");

    int failedMarks1=displayFailedMarks(marks, lastName, firstName);
    System.out.println("");
    System.out.println(failedMarks1 + " failed student(s)");
    System.out.println("-----------------------------------");



}//public static void doMarkReport(String fileName, int studentNum) throws IOException

    /**
     *This method calculates class average
     * @param marks student marks
     * @param num number of marks entered
     * @return marks average
     */
    public static double calculateClassAverage(int[] marks, int num) {
        //initialize variables
        double totalMarks, average;
        totalMarks=0;
        // Update the value of num to match the length of the marks array
        num= marks.length;
        // Loop through each mark in the array and add it to the total mark
        for (int i = 0; i < num; i++){
            totalMarks = totalMarks + marks[i];
        }
        // Calculate the average by dividing the total marks by the number of marks
        average = (double)totalMarks/num;
        //return average
        return (average);
    }//public static double calculateClassAverage(int[] marks, int num)


    /**
     *This method calculates the highest marks
     * @param lastName last name
     * @param firstName first name
     * @param marks students marks
     * @param num number of marks entered
     * @return higher mark
     */
    public static int displayHighestMark(String[] lastName, String[] firstName,int[] marks,int num) {
        //initialize variables
        int mark, higher;
        mark=0;
        higher=0;


        // iterate over the array of marks to find the highest mark
        for (int i = 0; i < num; i++){
            mark = marks[i];
            if (mark > higher){
                higher=mark;

            }//end if
        }//end for

        // iterate over the array of marks again to find the highest mark
        for (int i =0; i < num; i++){
            mark = marks[i];
            //if current mark is equal to the highest mark, print out name and mark
            if (mark ==higher){
                System.out.printf("Highest Mark: %-1s %-1s  %3d %% %n", lastName[i], firstName[i], marks[i]);

        }//end if

        }//end for
        //return highest mark
        return (higher);

    }//public static int displayHighestMark(String[] lastName, String[] firstName,int[] marks,int num)


    /**
     * This method calculates the lowest marks
     * @param lastName last name
     * @param firstName first name
     * @param marks marks of student
     * @param num number of marks entered
     * @return lowest mark
     */
    public static int displayLowestMark( String[] lastName, String[] firstName,int[] marks, int num) {
        //initialize variables
        int mark, lower;
        mark=0;
        lower=100;

        // iterate over the array of marks to find the lowest mark
        for (int i = 0; i < num; i++){
            mark = marks[i];
            if (mark < lower){
                lower=mark;

            }
        }

        for (int i =0; i < num; i++){
            mark = marks[i];
            //if current mark is equal to the lowest mark, print out name and mark
            if (mark == lower){
                System.out.printf("Lowest Mark: %-1s %-1s  %3d %% %n", lastName[i], firstName[i], marks[i]);
                System.out.println("-----------------------------------");
            }//end if


        }//end for
        //return highest mark
        return (lower);




       }//public static int displayLowestMark( String[] lastName, String[] firstName,int[] marks, int num)


    /**
     *This method calculates which students are on honor roll
     * @param marks students marks
     * @param lastName last name
     * @param firstName first name
     * @return honor roll marks
     */
    public static int displayHonorRoll(int[] marks, String[] lastName, String[] firstName) {
        //counter for the number of people who made honor roll
        int honorList = 0;
        System.out.println("HONOR ROLL: \n");
        // iterate over the array of marks to find students who made the honor roll
        for (int i = 0; i < marks.length; i++) {
            if (marks[i] >= 80) {

                //if marks is 80 or above, print out name and mark
                System.out.println(lastName[i] + ", " + firstName[i] + ": "+ marks[i] + " %");

                //counter for students who made honor roll
                honorList++;
            }//end if

        }//end for

        return honorList;
    }//public static int displayHonourRoll(int[] marks, String[] lastName, String[] firstName)


    /**
     *This method calculates failed marks
     * @param marks student marks
     * @param lastName last name
     * @param firstName first name
     * @return failed marks
     */
    public static int displayFailedMarks(int[] marks, String[] lastName, String[] firstName) {

        System.out.println("FAILED MARKS: \n");
        // counter for students who have failed marks (under 50)
        int failList = 0;
        // iterate over the array of marks to find students who failed
        for (int i = 0; i < marks.length; i++) {
            if (marks[i] <= 50) {

                //if marks are 50 or below, print out name and failed mark
                System.out.println(lastName[i] + ", " + firstName[i] + ": "+ marks[i] + " %");

                //counter for failed students
                failList++;
            }//end if
        }//end for

        return failList;


    }//public static int displayFailedMarks(int[] marks, String[] lastName, String[] firstName)



    //scanner for input
    static Scanner input = new Scanner(System.in);

    /**
     *This is the main method
     * @param args
     * @throws IOException
     */
    public static void main(String[]args) throws IOException {
        // initialize an array of marks with a capacity of 100
        int[] marks = new int[100];
        // number of students, for example
        int num = 0;
        int choice1=0;
        String fileName=null;

        //marks report art
        System.out.println("--------------------------------");
        System.out.println("          MARKS REPORT          ");
        System.out.println("---------------------------------");
        System.out.println("");

        //instructions
        System.out.println("Welcome to MARKS REPORT\n");
        System.out.println("This program is designed for teachers to either manually input class size, student names and");
        System.out.println("marks or use an an existing file. Teachers can then read highest and lowest marks,");
        System.out.println("as well as honour roll students and failed students. ");

        //main while loop
        while(true){
            //user chooses option
            System.out.printf("%-17s %n", "\nMARKS REPORT - Choose an option: ");
            System.out.printf("%-17s %n", "1 - Read mark file");
            System.out.printf("%-17s %n", "2 - Enter marks manually ");
            System.out.printf("%-17s %n", "3 - Exit program");

            //error trap choice (1-3)
            do {
                System.out.println("");
                System.out.print("Choose an option: ");
                choice1= input.nextInt();
                if (choice1==1|choice1==2|choice1==3){
                    break;
                }else{
                    System.out.println("");
                    System.err.print("INVALID, TRY AGAIN! (1-3)\n");
                    System.out.println("");
                }
            }while(true);//while true

            //based on number choice, user will call a different method
            if (choice1==1){
                readFromFile(marks);

            }

            if (choice1==2){
                manualEntry();

            }

            if (choice1==3){
                System.err.print("*PROGRAM ENDED*");
                break;
            }

            //doMarkReport(fileName);


        }//end while(true)

    }//public static void main(String[]args) throws IOException
}//public class MarksReportSumm









