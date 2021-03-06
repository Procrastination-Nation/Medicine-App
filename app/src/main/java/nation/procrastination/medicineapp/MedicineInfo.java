package nation.procrastination.medicineapp;

/**
 * Created by Mathieu Morin on 11/24/2017.
 */

public class MedicineInfo {
    private int id;
    private String name;
    private int amount;
    private int dosage;
    private String days;
    private String times;
    private String alarmIDs;

    public MedicineInfo() {
        this.id = -1;
        this.name = "";
        this.amount = 0;
        this.dosage = 0;
        this.days = "";
        this.times = "";
        this.alarmIDs = ";";
    }

    public MedicineInfo(String name, int amount, int dosage, String days, String times, String alarmIDs) {
        this.id = -1;
        this.name = name;
        this.amount = amount;
        this.dosage = dosage;
        this.days = days;
        this.times = times;
        this.alarmIDs = alarmIDs;
    }

    public MedicineInfo(int id, String name, int amount, int dosage, String days, String times, String alarmIDs) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.dosage = dosage;
        this.days = days;
        this.times = times;
        this.alarmIDs = alarmIDs;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getAmount() { return amount; }
    public int getDosage() { return dosage; }
    public String getDays() { return days; }
    public String getTimes() { return times; }
    public String getAlarmIDs() { return alarmIDs; }

}
