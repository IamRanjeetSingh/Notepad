package com.example.notepad.views.activities;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.LongDef;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.notepad.Dal.Repository;
import com.example.notepad.R;
import com.example.notepad.databinding.TestActivityBinding;
import com.example.notepad.models.Note;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java_async.OnCompleteListener;
import java_async.Task;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TestActivityBinding binding = DataBindingUtil.setContentView(this, R.layout.test_activity);

        Log.d("MyTag", "onCreate: deleteDatabase "+deleteDatabase("NotepadDb"));

        RepositoryTests repositoryTests = new RepositoryTests(getApplicationContext());
        repositoryTests.runAllTests();

        binding.data.setText(
                "Total Tests: "+repositoryTests.getTotalTests()+
                "\nPassed Tests: "+repositoryTests.getPassedTests()+
                "\nFailed Tests: "+repositoryTests.getFailedTests()
        );

        if(repositoryTests.getFailedTests() > 0) {
            List<String> failedTests = repositoryTests.getFailedTestsList();
            List<String> failedTestsReasons = repositoryTests.getFailedTestsReasonsList();
            binding.data.append("\nFailed Tests:");
            for(int i = 0; i < failedTests.size() && i < failedTestsReasons.size(); i++) {
                binding.data.append(
                        "\nMethod:\n"+failedTests.get(i)+
                        "\nReason:\n"+failedTestsReasons.get(i)
                );
            }
        }
    }

    public static class RepositoryTests extends TestClass {
        private static final String TAG = "RepoTests";
        private final long ASYNC_WAIT = 2000;
        private Repository repo;

        public RepositoryTests(Context context) {
            repo = new Repository(context);
        }

        @TestMethod
        public boolean insertNote_ValidNote_Successful() {
            Note note = new Note("Note 1", "This is the body of the note.");
            boolean[] result = { true };

            repo.insertNote(note).addOnCompleteListener(tInsert -> {
                if(!tInsert.isSuccessful()) {
                    result[0] = false;
                    Log.d(TAG, "insertNote_ValidNote_Successful: Insert operation failed with reason: "+tInsert.getFailure());
                    return;
                }

                long rowId = tInsert.getResult();
                repo.getNote(rowId).addOnCompleteListener(tRead -> {
                   if(!tRead.isSuccessful()) {
                       result[0] = false;
                       Log.d(TAG, "insertNote_ValidNote_Successful: Read operation failed with reason: "+tRead.getFailure());
                       return;
                   }

                   result[0] = note.equals(tRead.getResult());
                   if(!result[0])
                       Log.d(TAG, "insertNote_ValidNote_Successful: Initial note and Inserted/Read note are not the same\nInitial Note\n"+note+"\nInserted/Read Note\n"+tRead.getResult());
                });
            });

            awaitTaskCompletion();

            return result[0];
        }

        @TestMethod
        public boolean insertNote_MultipleInvalidNotes_Unsuccessful() {
            List<Note> notes = new ArrayList<>();

            notes.add(new Note());
            notes.add(new Note(null, null));
            notes.add(new Note(null, ""));
            notes.add(new Note("", null));
            notes.add(new Note("", ""));
            notes.add(new Note("Title", ""));
            notes.add(new Note(null, "This is the body of the note."));
            notes.add(new Note("Title", null));

            return true;
        }

        @TestMethod
        public boolean insertNote_MultipleValidNotes_Successful() {
            List<Note> notes = new ArrayList<>();
            for(int i = 0; i < 10; i++)
                notes.add(new Note("Note "+(i+1), "This is the body of the note."));

            for(Note note : notes)
                repo.insertNote(note);

            return true;
        }

        @TestMethod
        public boolean getNoteById_ValidId_Successful() {

            final boolean[] result = { true };

            final Note note = new Note("Note 1", "This is the body of the note.");
            Task<Long> insertTask = repo.insertNote(note);
            insertTask.addOnCompleteListener(tInsert -> {
               if(!tInsert.isSuccessful()) {
                   result[0] = false;
                   Log.d("MyTag", "getNoteById_ValidId_Successful: Insert task unsuccessful: "+tInsert.getFailure());
                   return;
               }

               Task<Note> readTask = repo.getNote(tInsert.getResult()).addOnCompleteListener(tRead -> {
                   if(!tRead.isSuccessful()) {
                       result[0] = false;
                       Log.d("MyTag", "getNoteById_ValidId_Successful: Read task unsuccessful: "+tRead.getFailure());
                       return;
                   }

                   result[0] = tRead.getResult().equals(note);
                   if(!result[0])
                       Log.d("MyTag", "getNoteById_ValidId_Successful: Returned Note doesn't match with initial note: \nReturned Note:\n"+tRead.getResult()+"\nInitial Note\n"+note);
               });
            });

            awaitTaskCompletion();

            return result[0];
        }

        @TestMethod
        public boolean getNoteById_InvalidId_Unsuccessful() {
            final boolean[] result = { true };

            Task<Note> readTask = repo.getNote(0).addOnCompleteListener(tRead -> {
                if(!tRead.isSuccessful()) {
                    result[0] = false;
                    Log.d("MyTag", "getNoteById_ValidId_Successful: Read task unsuccessful: "+tRead.getFailure());
                    return;
                }

                result[0] = tRead.getResult() == null;
                if(!result[0])
                    Log.d("MyTag", "getNoteById_ValidId_Successful: Returned Note not equal to null");
            });

            awaitTaskCompletion();

            return result[0];
        }

        private void awaitTaskCompletion() {
            try {
                Thread.sleep(ASYNC_WAIT);
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    public abstract static class TestClass {
        private int passedTests = 0, failedTests = 0;
        private List<String> failedTestsList = new ArrayList<>();
        private List<String> failedTestsReasonsList = new ArrayList<>();

        public final int getTotalTests() {
            return passedTests + failedTests;
        }

        public final int getPassedTests() {
            return passedTests;
        }

        public final int getFailedTests() {
            return failedTests;
        }

        public List<String> getFailedTestsList() {
            return failedTestsList;
        }

        public List<String> getFailedTestsReasonsList() {
            return failedTestsReasonsList;
        }

        public final void runAllTests() {
            Method[] methods = this.getClass().getMethods();
            for(Method method : methods) {
                if(method.getAnnotation(TestMethod.class) != null) {
                    Log.d("Tes", "runAllTests: test method: "+method.getName());
                    try {
                        Object returnedValue = method.invoke(this);
                        if(returnedValue instanceof Boolean) {
                            if((boolean)returnedValue)
                                passedTests++;
                            else
                                failedTests++;
                        }
                        else
                            failedTests++;
                    } catch(IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e.getCause() != null ? e.getCause().toString() : "Unknown Reason");
                    }
                }
            }
        }
    }

    @Target(value = ElementType.METHOD)
    @Retention(value = RetentionPolicy.RUNTIME)
    public @interface TestMethod {}
}
