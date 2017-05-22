package com.jude.ferrymandemo;

import com.jude.ferryman.annotations.ActivityRelation;

/**
 * Created by zhuchenxi on 2017/1/20.
 */

public interface ActivityRelations {

    @ActivityRelation(activityNameRegular = "\\b\\w+(?=Activity)", objectNameRegular = "\\b\\w+(?=Presenter)")
    void presenter();

    @ActivityRelation(activityNameRegular = "\\w+", objectNameRegular = "\\b\\w+(?=Data)")
    void data();
}
