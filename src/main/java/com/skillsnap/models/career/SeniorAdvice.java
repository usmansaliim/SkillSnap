package com.skillsnap.models.career;


public class SeniorAdvice {
    private String seniorName;
    private String currentRole;
    private String company;
    private String university;
    private int    yearsExp;
    private String adviceText;

    public SeniorAdvice(String seniorName, String currentRole,
                        String company, String university,
                        int yearsExp, String adviceText) {
        this.seniorName  = seniorName;
        this.currentRole = currentRole;
        this.company     = company;
        this.university  = university;
        this.yearsExp    = yearsExp;
        this.adviceText  = adviceText;
    }

    public String getSeniorName()  { return seniorName; }
    public String getCurrentRole() { return currentRole; }
    public String getCompany()     { return company; }
    public String getUniversity()  { return university; }
    public int    getYearsExp()    { return yearsExp; }
    public String getAdviceText()  { return adviceText; }
}
