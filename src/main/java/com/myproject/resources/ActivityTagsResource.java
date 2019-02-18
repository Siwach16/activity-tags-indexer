package com.myproject.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.myproject.model.Activity;
import com.myproject.model.IndexableActivity;
import com.myproject.model.Tag;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by tat50037 on 21/01/19.
 */
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/")
public class ActivityTagsResource {
    @POST
    @Path("/index")
    public void indexActivity(Activity activity) throws IOException {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        StringBuilder sb=new StringBuilder("?");
        for(String id:activity.getTags()){
            sb.append("id=").append(id).append("&");
        }


        HttpGet getRequest = new HttpGet(
                "http://localhost:8082/getAllParentTags/"+sb.toString());
        HttpGet getRequest1 = new HttpGet(
                "http://localhost:8082/getAllTagsById/"+sb.toString());

        HttpResponse response = httpClient.execute(getRequest);

        IndexableActivity activity1=new IndexableActivity();
        activity1.setActivityName(activity.getActivityName());
        Tag[] tags=new ObjectMapper().readValue(EntityUtils.toString(response.getEntity()), Tag[].class);

        activity1.setImplicitTags(new ArrayList<String>());

        activity1.setExplicitTags(new ArrayList<String>());
        for(Tag tag: tags){
            activity1.getImplicitTags().add(tag.getTagName());
        }
        HttpResponse response1 = httpClient.execute(getRequest1);
        Tag[] tags1=new ObjectMapper().readValue(EntityUtils.toString(response1.getEntity()), Tag[].class);

        for(Tag tag: tags1){
            activity1.getExplicitTags().add(tag.getTagName());
        }
        HttpPost postRequest = new HttpPost(
                "http://localhost:9200/activity/_doc/"+activity.getActivityId()+"?refresh=true");
        postRequest.setHeader("Content-Type","application/json");
        postRequest.setEntity(new StringEntity(new ObjectMapper().writeValueAsString(activity1)));
        response=httpClient.execute(postRequest);
        HttpPost refreshRequest = new HttpPost(
                "http://localhost:9200/activity/_refresh");
        httpClient.execute(refreshRequest);
        System.out.println(EntityUtils.toString(response.getEntity()));

    }

    @DELETE
    @Path("/deleteActivity/{id}")
    public void indexActivity(@PathParam("id") String id) throws IOException {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpDelete deleteRequest = new HttpDelete(
                "http://localhost:9200/activity/_doc/"+id+"?refresh=true");
        HttpResponse response=httpClient.execute(deleteRequest);
        System.out.println(EntityUtils.toString(response.getEntity()));

    }
}
