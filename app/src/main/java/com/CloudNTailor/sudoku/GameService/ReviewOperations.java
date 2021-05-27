package com.CloudNTailor.sudoku.GameService;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.CloudNTailor.sudoku.GameEngine.Converter;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.review.model.ReviewErrorCode;
import com.google.android.play.core.tasks.Task;

public class ReviewOperations {

    private static ReviewInfo reviewInfo;
    private static ReviewManager reviewManager;

    public void  ActiveReviewInfo(Context cnt)
    {
        reviewManager = ReviewManagerFactory.create(cnt);

        Task<ReviewInfo> request = reviewManager.requestReviewFlow();
        request.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // We can get the ReviewInfo object
                reviewInfo = task.getResult();
                //Toast.makeText(cnt,"Task  complated",Toast.LENGTH_LONG).show();
                Log.d("TAG", "Task  complated");
            }
            else
            {
              //  Toast.makeText(cnt,"Task CanNot complated",Toast.LENGTH_LONG).show();
                Log.d("TAG", "Task CanNot complated");
            }
        });
    }

    public void StartReviewInfo(Activity act)
    {
        if(reviewInfo!=null) {
            Task<Void> flow = reviewManager.launchReviewFlow(act, reviewInfo);
            flow.addOnCompleteListener(task -> {
                //Operations
                //Toast.makeText(act.getApplicationContext(),"reviewInfo  complated",Toast.LENGTH_LONG).show();
                Log.d("TAG", "reviewInfo  complated");
            });
        }
        else
        {
            //Toast.makeText(act.getApplicationContext(),"reviewInfo  complated",Toast.LENGTH_LONG).show();
            Log.d("TAG", "reviewInfo not complated");
        }
    }
}
