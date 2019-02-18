/**
 * Created by tat50037 on 16/12/18.
 */
package com.myproject.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import java.util.List;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)

public class Activity {
    private String activityId;
    private String activityName;
    private List<String> tags;

}
