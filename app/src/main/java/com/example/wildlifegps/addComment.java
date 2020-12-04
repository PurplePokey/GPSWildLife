package com.example.wildlifegps;

import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.io.Serializable;
import java.util.ArrayList;

public class addComment extends AppCompatActivity implements Serializable {

    private final addComment activity = addComment.this;

    Button b;
    TextInputEditText box;
    DBHandler dbh;
    Sighting sighting;
    ArrayList<Comment> commentArr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.sighting_view);

        b = (Button)findViewById(R.id.post_comment);
        box = (TextInputEditText)findViewById(R.id.comment_textbox);
        dbh = new DBHandler(activity);

        b.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(!box.getText().toString().matches(""))
                {
                    // not null not empty
                    final String content = box.getText().toString();
                    Comment comment = new Comment();
                    comment.setComment(content);
                    commentArr.add(comment);
                    sighting.setComments(commentArr);

                    dbh.updateSighting(sighting);

                    Toast.makeText(activity, "Comment added!", Toast.LENGTH_LONG).show();
                }
                else {
                    //null or empty
                    Toast.makeText(activity, "Enter text to make a comment", Toast.LENGTH_LONG).show();
                }

                Intent intentAddComment = new Intent(getApplicationContext(), Sighting.class);
                startActivity(intentAddComment);
            }
        });
    }
}
