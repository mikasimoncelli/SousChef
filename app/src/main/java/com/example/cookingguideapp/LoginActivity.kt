package com.example.cookingguideapp

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {
    private lateinit var signInLauncher: ActivityResultLauncher<Intent>
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_welcome)
        auth = FirebaseAuth.getInstance()
        setUpGoogleSignIn()
    }

    private fun setUpGoogleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        Log.w(TAG, "wagwan")

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        signInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                Log.w("wagwan", "wagwan")
                handleSignInResult(task)
                updateUsers()
            } else {
                Log.w(TAG, "Google Sign-In failed.")
            }
        }

        findViewById<SignInButton>(R.id.google_sign_in_button).setOnClickListener {
            signIn()
        }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        signInLauncher.launch(signInIntent)
    }

    private fun handleSignInResult(task: Task<GoogleSignInAccount>) {
        try {
            val account = task.getResult(ApiException::class.java)
            firebaseAuthWithGoogle(account.idToken!!)
            updateUsers()
        } catch (e: ApiException) {
            Log.w(TAG, "Google Sign-In failed: ${e.statusCode}")
            Toast.makeText(this, "Google Sign-In failed.", Toast.LENGTH_LONG).show()
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {


                val currentUser = auth.currentUser
                Log.d("Melons", "Current user is: $currentUser")

                if (currentUser != null) {
                    updateUsers()
                    navigateToRecipePage()
                } else {
                    Log.w(TAG, "Firebase Auth didn't return a user.")
                }
            } else {
                Log.w(TAG, "signInWithCredential:failure", task.exception)
                Toast.makeText(this, "Authentication Failed.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun navigateToRecipePage() {
        val recipesFragment = RecipesFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, recipesFragment)
            .commit()
    }





    private fun updateUsers() {
        val currentUser = FirebaseAuth.getInstance().currentUser

        currentUser?.let { user ->

            val userId = user.uid
            val name = user.displayName ?: "No Name"
            val email = user.email ?: "No Email"
            val profilePicture = user.photoUrl?.toString()


            val userData = hashMapOf(
                "uid" to userId,
                "name" to name,
                "email" to email,
                "profilePicture" to profilePicture
            )


            val db = FirebaseFirestore.getInstance()
            val userDocRef = db.collection("users").document(userId)


            userDocRef.set(userData)
                .addOnSuccessListener {
                    Log.d(TAG, "User record successfully added/updated for user ID: $userId")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Failed to add/update user record for user ID: $userId", e)
                }
        } ?: Log.w(TAG, "updateUsers was called, but no current user is logged in.")
    }




}
