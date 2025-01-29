package com.example.cookingguideapp

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore

class WelcomeFragment : Fragment() {

    private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN: Int = 9001

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_welcome, container, false)

        // Configure Google Sign-In
        configureGoogleSignIn()

        // Set up the Google Sign-In button click listener
        val googleSignInButton = view.findViewById<SignInButton>(R.id.google_sign_in_button)
        googleSignInButton.setOnClickListener {
            signIn()
        }

        return view
    }

    private fun configureGoogleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<com.google.android.gms.auth.api.signin.GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            firebaseAuthWithGoogle(account.idToken!!)
        } catch (e: ApiException) {
            // Handle sign in failures here by updating the UI appropriately or logging the error
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(requireActivity()) { task ->
            if (task.isSuccessful) {
                updateUsers()
                // Sign in success, navigate to MainActivity
                navigateToMainApp()
            } else {
            }
        }
    }

    private fun navigateToMainApp() {
        startActivity(Intent(activity, MainActivity::class.java))
        updateUsers()
        activity?.finish()  // Close the WelcomeActivity so the user can't navigate back to it

    }


    private fun updateUsers() {
        val currentUser = FirebaseAuth.getInstance().currentUser

        currentUser?.let { user ->
            // Get the user's information
            val userId = user.uid
            val name = user.displayName ?: "No Name"
            val email = user.email ?: "No Email"
            val profilePicture = user.photoUrl?.toString()

            // Prepare the user data
            val userData = hashMapOf(
                "uid" to userId,
                "name" to name,
                "email" to email,
                "profilePicture" to profilePicture
            )

            // Get a reference to Firestore
            val db = FirebaseFirestore.getInstance()
            val userDocRef = db.collection("users").document(userId)

            // Set or update the user document with the new data
            userDocRef.set(userData)
                .addOnSuccessListener {
                    Log.d(ContentValues.TAG, "User record successfully added/updated for user ID: $userId")
                }
                .addOnFailureListener { e ->
                    Log.w(ContentValues.TAG, "Failed to add/update user record for user ID: $userId", e)
                }
        } ?: Log.w(ContentValues.TAG, "updateUsers was called, but no current user is logged in.")
    }
}


