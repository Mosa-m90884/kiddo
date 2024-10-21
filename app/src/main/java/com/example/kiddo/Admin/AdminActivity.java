package com.example.kiddo.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.kiddo.Model.UserInAdminPanel;
import com.example.kiddo.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity implements UserAdapter.OnUserActionListener {

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<UserInAdminPanel> userList;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        userList = new ArrayList<>();
        userAdapter = new UserAdapter(userList, this);
        recyclerView.setAdapter(userAdapter);

        firestore = FirebaseFirestore.getInstance();
        loadUsers();
    }

    private void loadUsers() {
        userList.clear(); // تأكد من مسح القائمة
        firestore.collection("parents")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String id = document.getId();
                            String name = document.getString("username");
                            String email = document.getString("email");
                            UserInAdminPanel user = new UserInAdminPanel(id, name, email);
                            userList.add(user);
                        }
                        userAdapter.notifyDataSetChanged();
                    } else {
                        Log.e("AdminActivity", "Error getting documents: ", task.getException());
                    }
                });

        firestore.collection("children")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String id = document.getId();
                            String name = document.getString("username");
                            String email = document.getString("email");
                            UserInAdminPanel user = new UserInAdminPanel(id, name, email);
                            userList.add(user);
                        }
                        userAdapter.notifyDataSetChanged();
                    } else {
                        Log.e("AdminActivity", "Error getting documents: ", task.getException());
                    }
                });
    }

    @Override
    public void onEdit(UserInAdminPanel user) {
        // ابدأ Activity جديدة لتعديل المستخدم
       /** Intent intent = new Intent(this, EditUserActivity.class);
        intent.putExtra("USER_ID", user.getId());
        intent.putExtra("USERNAME", user.getUsername());
        intent.putExtra("EMAIL", user.getEmail());
        startActivity(intent);**/
    }

    @Override
    public void onDelete(UserInAdminPanel user) {
        firestore.collection("parents").document(user.getId()).delete()
                .addOnSuccessListener(aVoid -> {
                    userList.remove(user);
                    userAdapter.notifyDataSetChanged(); // تحديث الـ RecyclerView
                })
                .addOnFailureListener(e -> {
                    Log.e("AdminActivity", "Error deleting user", e);
                });
        deleteSubCollection(user.getId());
        firestore.collection("children").document(user.getId()).delete()
                .addOnSuccessListener(aVoid -> {
                    userList.remove(user);
                    userAdapter.notifyDataSetChanged(); // تحديث الـ RecyclerView
                })
                .addOnFailureListener(e -> {
                    Log.e("AdminActivity", "Error deleting user", e);
                });
    }

    private void deleteSubCollection(String parentId) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        // الوصول إلى الكوليكشن الفرعي باستخدام معرف الأب
        CollectionReference subCollectionRef = firestore.collection("children")
                .document(parentId)
                .collection("tasks"); // استبدل "children" باسم الكوليكشن الفرعي

        deleteSubCollectionBatch(subCollectionRef);
    }

    private void deleteSubCollectionBatch(CollectionReference subCollectionRef) {
        subCollectionRef.limit(10).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<DocumentSnapshot> documents = task.getResult().getDocuments();
                for (DocumentSnapshot document : documents) {
                    document.getReference().delete();
                }
                // إذا كانت هناك مزيد من الوثائق، كرر العملية
                if (documents.size() == 10) {
                    deleteSubCollectionBatch(subCollectionRef);
                }
            } else {
                Log.e("DeleteSubCollection", "Error getting documents: ", task.getException());
            }
        });
    }
}