package com.crix.getup.util

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.crix.getup.data.model.AppUser
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

val TAG = "Auth"

object Auth {
    private val auth = Firebase.auth

    private val _currentAppUser = MutableLiveData<FirebaseUser?>(auth.currentUser)
    val currentAppUser: LiveData<FirebaseUser?>
        get() = _currentAppUser

    fun register(email: String, password: String): Task<String> {
        val completionSource = TaskCompletionSource<String>()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                Firebase.firestore
                    .collection("user")
                    .document(it.user!!.uid)
                    .set(AppUser.initFromFirebaseUser(it.user!!).toFireBaseObject())
                _currentAppUser.value = it.user
                completionSource.setResult("Register successful")
            }
            .addOnFailureListener {
                Log.e(TAG, "${it.message}")
                completionSource.setException(it)
            }
        return completionSource.task
    }

    fun login(email: String, password: String): Task<String> {
        val completionSource = TaskCompletionSource<String>()
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                _currentAppUser.value = it.user
                completionSource.setResult("Login successful")
            }
            .addOnFailureListener {
                Log.e(TAG, "${it.message}")
                completionSource.setException(it)
            }
        return completionSource.task
    }

    fun logout() {
        auth.signOut()
        _currentAppUser.value = null
    }
}