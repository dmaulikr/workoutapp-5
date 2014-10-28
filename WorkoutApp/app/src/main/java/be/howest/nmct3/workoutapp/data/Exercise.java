package be.howest.nmct3.workoutapp.data;

import java.util.List;

/**
 * Created by nielslammens on 22/10/14.
 */
public class Exercise {
    public Integer Id;
    public String Name;
    public Integer MuscleGroupId;
    public List<Integer> Reps;

    public final String[] MuscleGroups = {"ARMS", "BACK", "CHEST", "SHOULDERS", "ABS", "LEGS"};
    public enum MuscleGroup{
        ARMS, BACK, CHEST, SHOULDERS, ABS, LEGS;
    }
}
