package com.myproject.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * Created by tat50037 on 21/01/19.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IndexableActivity {
    private String activityName;

    private List<String> implicitTags;
    private List<String> explicitTags;


}
