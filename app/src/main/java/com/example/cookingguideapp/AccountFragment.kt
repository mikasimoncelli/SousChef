package com.example.cookingguideapp

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.android.gms.common.api.ApiException
import android.widget.ImageView
import android.widget.Toast
import com.squareup.picasso.Picasso


class AccountFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var statusTextView: TextView
    private lateinit var userPhoneTextView: TextView
    private lateinit var userProfileImageView: ImageView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_account, container, false)

        auth = FirebaseAuth.getInstance()
        statusTextView = view.findViewById(R.id.loggedInMessage)
        userPhoneTextView = view.findViewById<TextView>(R.id.userPhoneTextView)
        userProfileImageView = view.findViewById<ImageView>(R.id.userProfileImageView)
        setupGoogleSignIn(view)
        updateLoginStatus()


        val deleteAccountButton = view.findViewById<Button>(R.id.deleteButton)
        deleteAccountButton?.setOnClickListener {
            showDeleteConfirmationDialog()
        }



        val signOutButton = view.findViewById<Button>(R.id.signOutButton)
        signOutButton.setOnClickListener {
            signOut()
        }

        return view
    }

    private fun setupGoogleSignIn(view: View) {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()

            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)


    }


    private fun signOut() {

        auth.signOut()


        googleSignInClient.signOut().addOnCompleteListener(requireActivity()) {

        }
        googleSignInClient.revokeAccess().addOnCompleteListener(requireActivity()) {

        }


        startActivity(Intent(activity, WelcomeActivity::class.java))
        activity?.finish()  // Close the MainActivity or current activity
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(task: Task<GoogleSignInAccount>) {
        try {
            val account = task.getResult(ApiException::class.java)
            firebaseAuthWithGoogle(account.idToken!!)
        } catch (e: ApiException) {
            updateLoginStatus()
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener(requireActivity()) { task ->
            if (task.isSuccessful) {
                updateLoginStatus()
            } else {

            }
        }
    }

    private fun updateLoginStatus() {
        val user = auth.currentUser
        if (user != null) {


            val name = user.displayName ?: "No Name"
            val phone = user.phoneNumber ?: "Phone not provided" // Provide a default message
            val photoUrl = user.photoUrl

            statusTextView.text = "You are logged in as: $name"
            userPhoneTextView.text = "Phone: $phone"


            if (userProfileImageView != null && photoUrl != null) {
                Picasso.get().load(photoUrl).into(userProfileImageView)
            }

        } else {
            statusTextView.text = "Not logged in"
        }
    }

    private fun deleteAccount() {
        val user = auth.currentUser
        user?.delete()
            ?.addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {

                    signOut() // Sign out after deleting the account

                } else {

                    Toast.makeText(requireContext(), "Failed to delete account.", Toast.LENGTH_SHORT).show()
                }
            }
    }


    private fun showDeleteConfirmationDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.delete_account_msg, null)
        val dialogBuilder = AlertDialog.Builder(requireContext())
            .setView(dialogView)
        val alertDialog = dialogBuilder.create()

        val confirmButton = dialogView.findViewById<Button>(R.id.confirmButton)
        val cancelButton = dialogView.findViewById<Button>(R.id.cancelButton)

        confirmButton.setOnClickListener {

            deleteAccount()
            alertDialog.dismiss()
        }

        cancelButton.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()
    }




    companion object {
        private const val RC_SIGN_IN = 9001
    }
}
