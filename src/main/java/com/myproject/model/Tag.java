/**
 * Created by tat50037 on 16/12/18.
 */
package com.myproject.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Tag {
    private String tagId;
    private String tagName;
    private Tag parentTag;

}
