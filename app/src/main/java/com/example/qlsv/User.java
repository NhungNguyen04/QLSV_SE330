package com.example.qlsv;

public class User {
    private static int id;
    private static String username;
    private static String password;

    private static String email;
    private static String birthday;
    private static String address;
    private static int accumulatedCredits;
    private static double totalScore;
    private static double accumulatedScore;
    private static String className;
    private static int majorId;

    public User(int id, String username, String password, String email, String birthday, String address, int accumulatedCredits, double totalScore, double accumulatedScore, String className, int majorId){
        User.id = id;
        User.username = username;
        User.password = password;
        User.email = email;
        User.birthday = birthday;
        User.address = address;
        User.accumulatedCredits = accumulatedCredits;
        User.totalScore = totalScore;
        User.accumulatedScore = accumulatedScore;
        User.majorId = majorId;
        User.className = className;
    }

    public User() {
    }


    public static void setClassName(String className) {
        User.className = className;
    }

    public static String getClassName() {
        return className;
    }

    public static void setId(int id) {
        User.id = id;
    }

    public static void setUsername(String username) {
        User.username = username;
    }

    public static void setPassword(String password) {
        User.password = password;
    }

    public static void setEmail(String email) {
        User.email = email;
    }

    public static void setBirthday(String birthday) {
        User.birthday = birthday;
    }

    public static void setAddress(String address) {
        User.address = address;
    }

    public static void setAccumulatedCredits(int accumulatedCredits) {
        User.accumulatedCredits = accumulatedCredits;
    }

    public static void setTotalScore(float totalScore) {
        User.totalScore = totalScore;
    }

    public static void setAccumulatedScore(float accumulatedScore) {
        User.accumulatedScore = accumulatedScore;
    }

    public static void setMajorId(int majorId) {
        User.majorId = majorId;
    }

    public static int getId() {
        return id;
    }

    public static String getUsername() {
        return username;
    }

    public static String getPassword() {
        return password;
    }

    public static String getEmail() {
        return email;
    }

    public static String getBirthday() {
        return birthday;
    }

    public static String getAddress() {
        return address;
    }

    public static int getAccumulatedCredits() {
        return accumulatedCredits;
    }

    public static double getTotalScore() {
        return totalScore;
    }

    public static double getAccumulatedScore() {
        return accumulatedScore;
    }

    public static int getMajorId() {
        return majorId;
    }
}
