package com.mtpv.mobilee_ticket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper{
	
	public static String Pid_code, Pid_pswrd, Pid_Name, Ps_code, Ps_Name, CADRE_CODE, CADRE_NAME,UNIT_CODE,UNIT_NAME,SECURITY_CD;
	
	public static final String USER_TABLE = "USER_TABLE";
	
	public static final String TAG_MAIN = "AUTHENTICATION_DETAILS";
    public static final String PID_CD = "PID_CD";
    public static final String PID_NAME = "PID_NAME";
    public static final String PS_CODE = "PS_CODE";
    public static final String PS_NAME = "PS_NAME";
    public static final String CADRE_CD = "CADRE_CD";
    public static final String CADRE = "CADRE";
    public static final String UNIT_CD = "UNIT_CODE";
    public static final String UNIT_Name = "UNIT_NAME";
    
    public static final String CREATE_USER_TABLE = "CREATE TABLE  IF NOT EXISTS "
            + USER_TABLE + "(" + PID_CD + " VARCHAR," + PID_NAME
            + " VARCHAR," + PS_CODE + " VARCHAR," + PS_NAME + " VARCHAR," + CADRE_CD + " VARCHAR," + CADRE + " VARCHAR ,"
            + UNIT_CODE + " VARCHAR," + UNIT_NAME + " VARCHAR)";
	

	public DatabaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
        db.execSQL(CREATE_USER_TABLE);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		 db.execSQL(CREATE_USER_TABLE);
		 this.onCreate(db);
	}
	
	public  String insertAuthenticateUser(String resp, Context context) {
		// TODO Auto-generated method stub
		// Log.i("insertAuthenticateUser called ::",resp);
		if (resp != null) {
            try {
                JSONObject jsonObj = new JSONObject(resp);
                JSONArray contacts = jsonObj.getJSONArray(TAG_MAIN);
                // looping through All Contacts
                for (int i = 0; i < contacts.length(); i++) {
                    JSONObject c = contacts.getJSONObject(i);
                     
                    String PID_CD1 = c.getString(PID_CD);
                    String PID_NAME1 = c.getString(PID_NAME);
                    String PS_CODE1 = c.getString(PS_CODE);
                    String PS_NAME1 = c.getString(PS_NAME);
                    String CADRE1 = c.getString(CADRE);
                    String CADRE_CD1 = c.getString(CADRE_CD);
                    String UNIT_CODE1 = c.getString(UNIT_CODE);
                    String UNIT_NAME1 = c.getString(UNIT_NAME);

                    Pid_code= PID_CD1;
                    Pid_Name=PID_NAME1;
			        Ps_code=PS_CODE1;
					Ps_Name=PS_NAME1;
					CADRE_CODE=CADRE1;
					CADRE_NAME=CADRE_CD1;
					UNIT_CODE=UNIT_CODE1;
					UNIT_NAME=UNIT_NAME1;
					
        			ContentValues values = new ContentValues();
        			values.put(PID_CD, PID_CD1);
        			values.put(PID_NAME, PID_NAME1);
        			values.put(PS_CODE, PS_CODE1);
        			values.put(PS_NAME, PS_NAME1);
        			values.put(CADRE_CD, CADRE_CD1);
        			values.put(CADRE,CADRE1);
        			values.put(UNIT_CODE,UNIT_CODE1);
        			values.put(UNIT_NAME,UNIT_NAME1);
          			
        			//SharedPreferences pref = context.getSharedPreferences("Login", ); 
        			SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
    			    Editor editor = pref.edit();
    			 
    			/**************** Storing data as KEY/VALUE pair *******************/
    			    editor.putString("pidCode", PID_CD1);  // Saving string
    			    editor.putString("pidName", PID_NAME1);  // Saving string
    			    editor.putString("psCode", PS_CODE1);  // Saving string
    			    editor.putString("psName", PS_NAME1);  // Saving string
    			    editor.putString("cadreCd", CADRE_CD1);  // Saving string
    			    editor.putString("cadreName",CADRE1);
    			    editor.putString("unitCd",UNIT_CODE1);
    			    editor.putString("unitName",UNIT_NAME1);// Saving string
    			    // Save the changes in SharedPreferences
    			    editor.commit(); // commit changes
    			 
    			/**************** Get SharedPreferences data *******************/
    			 
    			// If value for key not exist then return second param value - In this case null
    			 
    			 Log.i("shared response ::::::::::::::", pref.getString("pidName", null))  ;         // getting pidName

    			 /**************** insert  data *******************/
    			 	SQLiteDatabase db = this.getWritableDatabase();
        			db.execSQL(CREATE_USER_TABLE);
        			db.execSQL("delete from " + USER_TABLE);            			
          			db.insert(USER_TABLE, null, values); // Inserting Row
        			
        			 System.out.println("********************* TABLE Insertion Successfully **********************************");
        			 
        			 
        			 String selectQuery = "SELECT  * FROM " + USER_TABLE;//LOGIN_DETAILS
        		       // SQLiteDatabase db = this.getWritableDatabase();
        		        Cursor cursor = db.rawQuery(selectQuery, null);
        		 
        		        // looping through all rows and adding to list
        		        if (cursor.moveToFirst()) {
        		            do {
        		            	 Log.i("1 :",""+ cursor.getString(0));
        		            	 Log.i("2 :",""+cursor.getString(1));
        		            	 Log.i("3 :",""+cursor.getString(2));
        		            	 Log.i("4 :",""+cursor.getString(3));
        		            	 Log.i("5 :",""+cursor.getString(4));
        		            	 Log.i("6 :",""+cursor.getString(5));
        		            	 Log.i("7 :",""+cursor.getString(6));
        		            	 Log.i("8 :",""+cursor.getString(7));
        		                
        		            } while (cursor.moveToNext());
        		        }
        			 db.close();
                  }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("ServiceHandler", "Couldn't get any data from the url");
        }
		
	 return PID_CD;
		
	}

}
