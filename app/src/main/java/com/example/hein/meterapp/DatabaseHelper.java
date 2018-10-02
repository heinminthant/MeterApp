package com.example.hein.meterapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by user on 5/27/2018.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME ="MeterDB.db";
    private static final int DATABASE_VERSION = 1;
    private final Context context;
    SQLiteDatabase db;

    private static final String DATABASE_PATH = "data/data/com.example.hein.meterapp/databases/";
    private final String  METER_TABLE = "Meter";
    private final String USER_TABLE = "User";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        CreateDb();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void CreateDb()
    {
        boolean dbExist = checkDbExist();
        if(!dbExist)
        {
            this.getReadableDatabase();
            copyDatabase();

        }
    }

    private boolean checkDbExist(){
        SQLiteDatabase sqLiteDatabase = null;
        try
        {
            String path = DATABASE_PATH+DATABASE_NAME;
            sqLiteDatabase = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
        }
        catch (Exception ex)
        {

        }

        if(sqLiteDatabase != null)
        {
            sqLiteDatabase.close();
            return true;

        }
        return false;
    }

    private void copyDatabase()
    {
        try {
            InputStream inputStream = context.getAssets().open(DATABASE_NAME);
            String outFileName = DATABASE_PATH+DATABASE_NAME;
            OutputStream outputStream = new FileOutputStream(outFileName);
            byte[] b = new byte[1024];
            int length;
            while((length = inputStream.read(b))>0)
            {
                outputStream.write(b, 0, length);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private SQLiteDatabase openDatabase()
    {
        String path = DATABASE_PATH+DATABASE_NAME;
        db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
        return db;

    }
    public void close()
    {
        if(db!=null)
        {
            db.close();
        }
    }

    public boolean login(String username, String password)
    {
        db = openDatabase();
        String[] columns = {"username"};
        String selection = "username =? and password = ?";
        String[] selectionArgs = {username, password};
        Cursor cursor = db.query(USER_TABLE, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        close();

        if(cursor.getCount() == 1){
            return true;
        }
        return false;
    }

    public ArrayList<HashMap<String,String>> getMeterData(int binderNo){
        db = openDatabase();
        ArrayList<HashMap<String,String>> meterList = new ArrayList<>();
        String query = "Select MeterNo,LedgerNo,ConsumerName From Meter Where CurrentUnit IS NULL AND BinderNo = " + binderNo + " Order By BinderSerial" ;

        Cursor cursor = db.rawQuery(query,null);
        while(cursor.moveToNext()){
            HashMap<String,String> user = new HashMap<>();
            user.put("MeterNo",cursor.getString(cursor.getColumnIndex("MeterNo")));
            user.put("LedgerNo",cursor.getString(cursor.getColumnIndex("LedgerNo")));
            user.put("ConsumerName",cursor.getString(cursor.getColumnIndex("ConsumerName")));
            meterList.add(user);
        }
        cursor.close();
        db.close();
        return meterList;
    }

    public ArrayList<HashMap<String,String>> searchMeterData(String searchText){

        db = openDatabase();
        String columns[] = {"MeterNo","LedgerNo","ConsumerName"};
        String selection = "MeterNo Like ?";
        ArrayList<HashMap<String,String>> meterList = new ArrayList<>();
        String[] selectionArgs = {"%"+searchText+"%"};
        Cursor cursor = db.query(METER_TABLE,columns,selection,selectionArgs,null,null,null);
        while(cursor.moveToNext()){
            HashMap<String,String> user = new HashMap<>();
            user.put("MeterNo",cursor.getString(cursor.getColumnIndex("MeterNo")));
            user.put("LedgerNo",cursor.getString(cursor.getColumnIndex("LedgerNo")));
            user.put("ConsumerName",cursor.getString(cursor.getColumnIndex("ConsumerName")));
            meterList.add(user);
        }
        cursor.close();
        db.close();
        return meterList;


    }

    public String getLedgerNo(String meterNo){
        db = openDatabase();
        String ledgerNo = "";

        String query = "Select LedgerNo From Meter Where MeterNo = ?";
        Cursor cursor = db.rawQuery(query, new String[]{meterNo});
        while(cursor.moveToNext()){
            ledgerNo = cursor.getString(cursor.getColumnIndex("LedgerNo"));
        }
        cursor.close();
        db.close();
        return ledgerNo;
    }

    public String getImageFileName(String meterNo,String ledgerNo){
        String fileName = meterNo + "-" +ledgerNo + "-19";
        return fileName;
    }

    public Integer[] getBinder(){

        db = openDatabase();

        String query = "Select Distinct BinderNo From " + METER_TABLE;

        Cursor cursor = db.rawQuery(query,null);

        Toast.makeText(context, Integer.toString(cursor.getCount()), Toast.LENGTH_SHORT).show();
        
        Integer[] binder = new Integer[cursor.getCount()];



        while(cursor.moveToNext()){

                binder[cursor.getPosition()] = cursor.getInt(cursor.getColumnIndex("BinderNo"));

        }
        cursor.close();
        db.close();
        return binder;


    }


    public int getRemaining(){
        db = openDatabase();

        String query = "Select * From Meter Where CurrentUnit IS NULL";

        Cursor cursor = db.rawQuery(query,null);

        int count = cursor.getCount();

        cursor.close();
        db.close();

        return count;

    }

    public int getCollecting(){
        db = openDatabase();

        String query = "Select * From Meter Where CurrentUnit IS NOT NULL";

        Cursor cursor = db.rawQuery(query,null);

        int count = cursor.getCount();

        cursor.close();
        db.close();

        return count;
    }

    public void updateCurrentUnit(String meterNo,int currentUnit,String currentDate){
        db = openDatabase();
        db.execSQL("Update Meter Set CurrentUnit = '" + currentUnit + "'Where MeterNo = '" + meterNo + "'");
        db.execSQL("Update Meter Set Reading_Date = '" + currentDate + "'Where MeterNo = '" + meterNo + "'");
        db.close();
    }

    public boolean checkMeterEdit(String meterNo){
        db = openDatabase();

        String query = "Select MeterNo,Reading_Date From Meter Where CurrentUnit IS NOT NULL AND MeterNo = ?";
        Cursor cursor = db.rawQuery(query, new String[]{meterNo});
        int count = cursor.getCount();
        cursor.close();
        db.close();
        if(count==1){
            return true;
        }
        return false;
    }

    public boolean checkMeterRead(String meterNo){
        db = openDatabase();

        String query = "Select MeterNo From Meter Where CurrentUnit IS NULL AND MeterNo = ?";
        Cursor cursor = db.rawQuery(query, new String[]{meterNo});
        int count = cursor.getCount();
        cursor.close();
        db.close();
        if(count==1){
            return true;
        }
        return false;
    }

    public void editCurrent(String meterNo,int currentUnit){
        db = openDatabase();

        db.execSQL("Update Meter Set CurrentUnit = '" + currentUnit + "'Where MeterNo = '" + meterNo + "'");
        db.close();
    }

    public int getBinderSerial(String meterNo){
        db = openDatabase();
        int binderSerial = 0;
        String query = "Select BinderSerial From Meter Where MeterNo = ?";
        Cursor cursor = db.rawQuery(query, new String[]{meterNo});
        while(cursor.moveToNext()){
            binderSerial = cursor.getInt(cursor.getColumnIndex("BinderSerial"));
        }
        return binderSerial;
    }


    public HashMap<String,String> searchMeterBinder(int binderSerial, String binderNo){

        HashMap<String,String> meterData = new HashMap<>();
        db = openDatabase();


        String columns[] = {"MeterNo","LedgerNo","ConsumerName","Address","PreviousUnit","CurrentUnit"};
        String selection = "BinderNo = ? AND BinderSerial = ?";
        String selectionArgs[] = {binderNo, String.valueOf(binderSerial)};


        Cursor cursor = db.query(METER_TABLE,columns,selection,selectionArgs,null,null,null,null);

        while(cursor.moveToNext()) {
            meterData.put("MeterNo", cursor.getString(cursor.getColumnIndex("MeterNo")));
            meterData.put("LedgerNo", cursor.getString(cursor.getColumnIndex("LedgerNo")));
            meterData.put("ConsumerName", cursor.getString(cursor.getColumnIndex("ConsumerName")));
            meterData.put("Address", cursor.getString(cursor.getColumnIndex("Address")));
            meterData.put("PreviousUnit", cursor.getString(cursor.getColumnIndex("PreviousUnit")));
            meterData.put("CurrentUnit",cursor.getString(cursor.getColumnIndex("CurrentUnit")));
        }

        cursor.close();
        db.close();

        return  meterData;
    }

    public HashMap<String,String> searcMeterNo(String meterNo){
        HashMap<String,String> meterData = new HashMap<>();
        db = openDatabase();

        String columns[] = {"MeterNo","LedgerNo","ConsumerName","Address","PreviousUnit","CurrentUnit"};
        String selection = "MeterNo = ?";
        String selectionArgs[] = {meterNo};


        Cursor cursor = db.query(METER_TABLE,columns,selection,selectionArgs,null,null,null,null);

        while(cursor.moveToNext()) {
            meterData.put("MeterNo", cursor.getString(cursor.getColumnIndex("MeterNo")));
            meterData.put("LedgerNo", cursor.getString(cursor.getColumnIndex("LedgerNo")));
            meterData.put("ConsumerName", cursor.getString(cursor.getColumnIndex("ConsumerName")));
            meterData.put("Address", cursor.getString(cursor.getColumnIndex("Address")));
            meterData.put("PreviousUnit", cursor.getString(cursor.getColumnIndex("PreviousUnit")));
            meterData.put("CurrentUnit",cursor.getString(cursor.getColumnIndex("CurrentUnit")));
        }

        cursor.close();
        db.close();


        return meterData;
    }

    public int getPrevious(String meterNo){
        db = openDatabase();
        int previousUnit = 0;
        String query = "Select PreviousUnit From Meter Where MeterNo = ?";
        Cursor cursor = db.rawQuery(query, new String[]{meterNo});
        while(cursor.moveToNext()){
            previousUnit = cursor.getInt(cursor.getColumnIndex("PreviousUnit"));
        }
        cursor.close();
        db.close();
        return previousUnit;
    }

    public String getMeterBinder(int binderSerial,String binderNo){
        String meterNo = "";
        db = openDatabase();
        String query = "Select MeterNo From Meter Where BinderSerial = ? AND BinderNo = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(binderSerial), binderNo});
        while(cursor.moveToNext()){
            meterNo = cursor.getString(cursor.getColumnIndex("MeterNo"));
        }
        cursor.close();
        db.close();
        return meterNo;
    }

    public String getTownship(String username){
        db = openDatabase();
        String township = "";

        String sql = "Select township From user Where username = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{username});
        while(cursor.moveToNext()){
            township = cursor.getString(cursor.getColumnIndex("township"));
        }
        db.close();
        return township;

    }

    public Bitmap getImage(String imgName){
        db = openDatabase();
        String query = "Select ImgLocation From Image Where ImgName = ?";
        Cursor cursor = db.rawQuery(query, new String[]{imgName});
        Bitmap bp = null;
        while(cursor.moveToNext()){
            byte[] img = cursor.getBlob(cursor.getColumnIndex("ImgLocation"));
            bp = BitmapFactory.decodeByteArray(img,0,img.length);
        }
        return bp;
    }

    public void insertImage(Bitmap img,String meterNo,String ledgerNo,String fileName){
        db = openDatabase();
        byte[] data = getBitmapAsByteArray(img);

        ContentValues cv = new ContentValues();
        cv.put("ImgName",fileName);
        cv.put("ImgLocation",data);
        cv.put("MeterNo",meterNo);
        db.insert("Image",null,cv);
//        db.execSQL("Insert into Image (ImgName,ImgLocation,MeterNo) Values( '" + fileName + "','" + data + "','" + meterNo + "'");
        db.close();

    }

    public void downloadMeter(JSONObject meter){
        db = openDatabase();

        ContentValues cv = new ContentValues();
        try {
            cv.put("LedgerNo",meter.getString("LedgerNo"));
            cv.put("MeterNo",meter.getString("MeterNo"));
            cv.put("BinderNo",meter.getInt("BinderNo"));
            cv.put("BinderSerial", meter.getInt("BinderSerial"));
            cv.put("ConsumerName",meter.getString("ConsumerName"));
            cv.put("Address",meter.getString("Address"));
            cv.put("PreviousUnit",meter.getInt("PreviousUnit"));
            db.insert("Meter",null,cv);
            db.close();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static byte[] getBitmapAsByteArray(Bitmap bitmap){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,0,outputStream);
        return outputStream.toByteArray();
    }







}

