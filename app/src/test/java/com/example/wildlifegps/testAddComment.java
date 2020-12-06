package com.example.wildlifegps;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class testAddComment {

    private addComment addComment = new addComment();
    private String str = "Nice post!";

    Comment comment1 = new Comment();
    Comment comment2 = new Comment();


    @Before
    public void setUp(){
        comment1.setComment(str);
        comment2.setComment("nice");
    }

    @Test
    public void TestThis(){
        Assert.assertEquals(str, comment1.getComment());
        Assert.assertNotSame(str, comment2.getComment());
    }

    @After
    public void tearDown(){

    }
}
