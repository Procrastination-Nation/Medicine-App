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

    public MedicineInfo() {
        this.id = -1;
        this.name = "";
        this.amount = 0;
        this.dosage = 0;
        this.days = "";
        this.times = "";
    }

    public MedicineInfo(String name, int amount, int dosage, String days, String times) {
        this.id = -1;
        this.name = name;
        this.amount = amount;
        this.dosage = dosage;
        this.days = days;
        this.times = times;
    }

    public MedicineInfo(int id, String name, int amount, int dosage, String days, String times) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.dosage = dosage;
        this.days = days;
        this.times = times;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getAmount() { return amount; }
    public int getDosage() { return dosage; }
    public String getDays() { return days; }
    public String getTimes() { return times; }
}
