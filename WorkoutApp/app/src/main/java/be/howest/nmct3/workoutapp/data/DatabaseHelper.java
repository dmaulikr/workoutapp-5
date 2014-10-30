package be.howest.nmct3.workoutapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by nielslammens on 22/10/14.
 */
public class DatabaseHelper extends SQLiteOpenHelper{

    public static DatabaseHelper INSTANCE;
    public static Object lock = new Object();

    public static final String DB_NAME = "workoutapp.db";
    public static final int DB_VERSION = 1;

    public DatabaseHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static DatabaseHelper getInstance(Context context) {
        if(INSTANCE == null){
            synchronized (lock){
                if(INSTANCE == null){
                    INSTANCE = new DatabaseHelper(context.getApplicationContext());
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + Contract.Workouts.CONTENT_DIRECTORY);
        db.execSQL("DROP TABLE IF EXISTS " + Contract.WorkoutExercises.CONTENT_DIRECTORY);

        createWorkoutsTableV1(db);
        createWorkoutExercisesTableV1(db);
        createDefaultRecords(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Contract.Workouts.CONTENT_DIRECTORY);
        db.execSQL("DROP TABLE IF EXISTS " + Contract.WorkoutExercises.CONTENT_DIRECTORY);

        createWorkoutsTableV1(db);
        createWorkoutExercisesTableV1(db);
        createDefaultRecords(db);
    }

    private void createWorkoutsTableV1(SQLiteDatabase db){
        String SQL = "CREATE TABLE " + Contract.Workouts.CONTENT_DIRECTORY + " ("
                + Contract.WorkoutColumns._ID       + " INTEGER PRIMARY KEY, "
                + Contract.WorkoutColumns.NAME      + " TEXT,"
                + Contract.WorkoutColumns.ISPAID    + " INTEGER"
                + ");";

        Log.d(getClass().getCanonicalName(), "CREATE TABLE VOOR WORKOUTS__________________________________________________");

        db.execSQL(SQL);

    }

    private void createWorkoutExercisesTableV1(SQLiteDatabase db){
        String SQL = "CREATE TABLE " + Contract.WorkoutExercises.CONTENT_DIRECTORY + " ("
                + Contract.WorkoutExerciseColumns._ID           + " INTEGER PRIMARY KEY, "
                + Contract.WorkoutExerciseColumns.WORKOUT_ID    + " INTEGER, "
                + Contract.WorkoutExerciseColumns.EXERCISE_ID   + " INTEGER, "
                + Contract.WorkoutExerciseColumns.REPS          + " TEXT"
                + ");";
        db.execSQL(SQL);

    }

    private void createDefaultRecords(SQLiteDatabase db){

        // Sample workout 1: chest, bench press, free

        ContentValues v1 = new ContentValues();
        v1.put(Contract.Workouts.NAME, "Chest day");
        v1.put(Contract.Workouts.ISPAID, 0);
        Long id = db.insert(Contract.Workouts.CONTENT_DIRECTORY, "", v1);

        ContentValues v1e = new ContentValues();
        v1e.put(Contract.WorkoutExercises.WORKOUT_ID, id);
        v1e.put(Contract.WorkoutExercises.EXERCISE_ID, 2);
        v1e.put(Contract.WorkoutExercises.REPS, "12,10,8");
        id = db.insert(Contract.WorkoutExercises.CONTENT_DIRECTORY, "", v1e);

        // Sample workout 2: legs, squat, free

        ContentValues v2 = new ContentValues();
        v2.put(Contract.Workouts.NAME, "Leg day");
        v2.put(Contract.Workouts.ISPAID, 0);
        id = db.insert(Contract.Workouts.CONTENT_DIRECTORY, "", v2);

        ContentValues v2e = new ContentValues();
        v2e.put(Contract.WorkoutExercises.WORKOUT_ID, id);
        v2e.put(Contract.WorkoutExercises.EXERCISE_ID, 1);
        v2e.put(Contract.WorkoutExercises.REPS, "12,10,8");
        id = db.insert(Contract.WorkoutExercises.CONTENT_DIRECTORY, "", v2e);

        // Sample workout 3: arms, bicep curl, paid

        ContentValues v3 = new ContentValues();
        v3.put(Contract.Workouts.NAME, "Arm day");
        v3.put(Contract.Workouts.ISPAID, 1);
        id = db.insert(Contract.Workouts.CONTENT_DIRECTORY, "", v3);

        ContentValues v3e = new ContentValues();
        v3e.put(Contract.WorkoutExercises.WORKOUT_ID, id);
        v3e.put(Contract.WorkoutExercises.EXERCISE_ID, 5);
        v3e.put(Contract.WorkoutExercises.REPS, "12,10,8");
        id = db.insert(Contract.WorkoutExercises.CONTENT_DIRECTORY, "", v3e);

    }
}
