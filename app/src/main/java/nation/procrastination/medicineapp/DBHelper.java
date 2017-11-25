package nation.procrastination.medicineapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mathieu Morin on 11/24/2017.
 */

public class DBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "MedicineAppDB";
    private static final String TABLE_NAME = "medicine_info";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_AMOUNT = "amount";
    private static final String KEY_DOSAGE = "dosage";
    private static final String KEY_DAYS = "days";
    private static final String KEY_TIMES = "times";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTbl = "CREATE TABLE " + TABLE_NAME + " ("
                + KEY_ID + " INTEGER PRIMARY KEY, "
                + KEY_NAME + " TEXT, "
                + KEY_AMOUNT + " INTEGER, "
                + KEY_DOSAGE + " INTEGER, "
                + KEY_DAYS + " TEXT, "
                + KEY_TIMES + " TEXT)";
        db.execSQL(createTbl);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addMedicine(MedicineInfo medicine) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, medicine.getName());
        values.put(KEY_AMOUNT, medicine.getAmount());
        values.put(KEY_DOSAGE, medicine.getDosage());
        values.put(KEY_DAYS, medicine.getDays());
        values.put(KEY_TIMES, medicine.getTimes());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public List<MedicineInfo> getMedicine() {
        SQLiteDatabase db = this.getReadableDatabase();

        String queryStm = "SELECT * FROM " + TABLE_NAME;

        Cursor cursor = db.rawQuery(queryStm, null);

        List<MedicineInfo> medList = new ArrayList<>();
        MedicineInfo medicineInfo;

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                int id = cursor.getInt(cursor.getColumnIndex(KEY_ID));
                String name = cursor.getString(cursor.getColumnIndex(KEY_NAME));
                int amount = cursor.getInt(cursor.getColumnIndex(KEY_AMOUNT));
                int dosage = cursor.getInt(cursor.getColumnIndex(KEY_DOSAGE));
                String days = cursor.getString(cursor.getColumnIndex(KEY_DAYS));
                String times = cursor.getString(cursor.getColumnIndex(KEY_TIMES));
                medicineInfo = new MedicineInfo(id, name, amount, dosage, days, times);

                medList.add(medicineInfo);
                cursor.moveToNext();
            }
        }

        cursor.close();
        db.close();
        return medList;
    }

    public MedicineInfo getMedicineByID(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String queryStm = "SELECT * FROM " + TABLE_NAME + " WHERE " + KEY_ID + " = " + id;

        Cursor cursor = db.rawQuery(queryStm, null);

        MedicineInfo medicineInfo = new MedicineInfo();

        if (cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndex(KEY_NAME));
            int amount = cursor.getInt(cursor.getColumnIndex(KEY_AMOUNT));
            int dosage = cursor.getInt(cursor.getColumnIndex(KEY_DOSAGE));
            String days = cursor.getString(cursor.getColumnIndex(KEY_DAYS));
            String times = cursor.getString(cursor.getColumnIndex(KEY_TIMES));
            medicineInfo = new MedicineInfo(name, amount, dosage, days, times);
        }

        cursor.close();
        db.close();
        return medicineInfo;
    }

    public void updateMedicine(MedicineInfo medicine) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, medicine.getName());
        values.put(KEY_AMOUNT, medicine.getAmount());
        values.put(KEY_DOSAGE, medicine.getDosage());
        values.put(KEY_DAYS, medicine.getDays());
        values.put(KEY_TIMES, medicine.getTimes());

        String[] args = new String[]{ String.format("%d", medicine.getId())};
        db.update(TABLE_NAME, values, String.format("%s=?", KEY_ID), args);
        db.close();
    }

    public void deleteMedicine(int medId) {
        SQLiteDatabase db = this.getWritableDatabase();

        String[] args = new String[]{ String.format("%d", medId)};
        db.delete(TABLE_NAME, String.format("%s=?", KEY_ID), args);
        db.close();
    }
}
