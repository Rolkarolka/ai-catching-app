package edu.pw.aicatching.authorization

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import edu.pw.aicatching.R
import kotlinx.android.synthetic.main.fragment_authorization.*

class AuthorizationFragment: Fragment() {
    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest
    private lateinit var signUpRequest: BeginSignInRequest
    private var showOneTapUI = true


    private val oneTapResult = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()){ result ->
        try {
            if (result.resultCode == Activity.RESULT_OK) {
                val credential = oneTapClient.getSignInCredentialFromIntent(result.data)
                val idToken = credential.googleIdToken
                when {
                    idToken != null -> {
                        // Got an ID token from Google. Use it to authenticate
                        // with your backend.
                        Log.d(TAG, "Got ID token.")
                    }
                    else -> {
                        // Shouldn't happen.
                        Log.d(TAG, "No ID token or password!")
                    }
                }

            }
        } catch (e: ApiException) {
            when (e.statusCode) {
                CommonStatusCodes.CANCELED -> {
                    Log.d(TAG, "One-tap dialog was closed.")
                    // Don't re-prompt the user.
                    showOneTapUI = false
                }
                CommonStatusCodes.NETWORK_ERROR -> {
                    Log.d(TAG, "One-tap encountered a network error.")
                    // Try again or just ignore.
                }
                else -> {
                    Log.d(TAG, "Couldn't get credential from result." +
                        " (${e.localizedMessage})")
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewBinding = inflater.inflate(R.layout.fragment_authorization, container, false)

        oneTapClient = Identity.getSignInClient(this.requireActivity())
        signUpRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(getString(R.string.web_client_id))
                    .setFilterByAuthorizedAccounts(false)
                    .build())
            .build()

        signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(getString(R.string.web_client_id))
                    .setFilterByAuthorizedAccounts(true)
                    .build())
            .setAutoSelectEnabled(true)
            .build()
        return viewBinding
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        authorizationButton.setOnClickListener {
            signIn()
        }
    }

    private fun signIn() {
        oneTapClient.beginSignIn(signInRequest)
            .addOnSuccessListener(this.requireActivity()) { result ->
                try {
                    val intentSenderRequest = IntentSenderRequest.Builder(result.pendingIntent).build()
                    oneTapResult.launch(intentSenderRequest)
                } catch (e: IntentSender.SendIntentException) {
                    Log.e(TAG, "Couldn't start One Tap UI: ${e.localizedMessage}")
                }
            }
            .addOnFailureListener(this.requireActivity()) { e ->
                e.localizedMessage?.let { Log.d(TAG, it) }
                signUp()
            }

    }

    private fun signUp() {
        oneTapClient.beginSignIn(signUpRequest)
            .addOnSuccessListener(this.requireActivity()) { result ->
                try {
                    val intentSenderRequest = IntentSenderRequest.Builder(result.pendingIntent).build()
                    oneTapResult.launch(intentSenderRequest)
                } catch (e: IntentSender.SendIntentException) {
                    Log.e(TAG, "Couldn't start One Tap UI: ${e.localizedMessage}")
                }
            }
            .addOnFailureListener(this.requireActivity()) { e ->
                e.localizedMessage?.let { Log.d(TAG, it) }
            }

    }

}
