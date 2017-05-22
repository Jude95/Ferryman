package com.jude.ferryman.compiler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhuchenxi on 2017/1/19.
 */

public class ActivityRelations {
    ArrayList<Relation> mRelations = new ArrayList<>();
    HashMap<String,String> mActivityMap = new HashMap<>();

    public void addRelation(String activityNameRegular, String objectNameRegular){
        mRelations.add(new Relation(activityNameRegular, objectNameRegular));
    }

    public void addActivityMaping(String activityName,String objectName){
        mActivityMap.put(objectName,activityName);
    }

    public boolean isRelated(String activityName,String objectName){
        if (mActivityMap.containsKey(objectName)){
            return mActivityMap.get(objectName).equals(activityName);
        }
        for (Relation relation : mRelations) {
            if (relation.isRelated(activityName, objectName)){
                return true;
            }
        }
        return false;
    }

    public static class Relation{
        Pattern activityNameRegular;
        Pattern objectNameRegular;


        public Relation(String activityNameRegular, String objectNameRegular) {
            this.activityNameRegular = Pattern.compile(activityNameRegular);
            this.objectNameRegular = Pattern.compile(objectNameRegular);
        }

        public boolean isRelated(String activityName,String objectName){
            try {
                Matcher activityMatcher = activityNameRegular.matcher(activityName);
                if (!activityMatcher.find()){
                    return false;
                }
                String symbolActivity = activityMatcher.group();
                if (symbolActivity.length() == 0){
                    return false;
                }

                Matcher objectMatcher = objectNameRegular.matcher(objectName);
                if (!objectMatcher.find()){
                    return false;
                }
                String symbolObject = objectMatcher.group();
                if (symbolObject.length() == 0){
                    return false;
                }

                return symbolActivity.equals(symbolObject);
            }catch (IllegalStateException e){
                return false;
            }
        }
    }


}
