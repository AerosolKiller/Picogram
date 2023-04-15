package edu.neu.picogram;

import static android.content.ContentValues.TAG;

import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class GameRepository {
    private FirebaseFirestore db;

    private List<UserNonogram> gameList;

    public GameRepository() {
        db = FirebaseFirestore.getInstance();
        gameList = new ArrayList<>();
    }

    public CompletableFuture<List<UserNonogram>> fetchAllGames() {
        CompletableFuture<List<UserNonogram>> future = new CompletableFuture<>();

        db.collection("games")
                .get()
                .addOnCompleteListener( task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            UserNonogram game = UserNonogram.fromDocument(document);
                            gameList.add(game);
                        }
                        future.complete(gameList);
                    } else {
                        Log.w(TAG, "Error fetching games", task.getException());
                        future.completeExceptionally(task.getException());
                    }
                });

        return future;
    }

}
