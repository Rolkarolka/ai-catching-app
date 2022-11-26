package edu.pw.aicatching.authorization

import android.app.Activity
import android.app.PendingIntent
import android.content.ActivityNotFoundException
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
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.GetSignInIntentRequest
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
    private val REQ_ONE_TAP = 2
    private val oneTapResult = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()){ result ->
//        try {
//            val credential = oneTapClient.getSignInCredentialFromIntent(result.data)
//            val idToken = credential.googleIdToken
//            when {
//                idToken != null -> {
//                    // Got an ID token from Google. Use it to authenticate
//                    // with your backend.
//                    val msg = "idToken: $idToken"
//
//                    Log.d("one tap", msg)
//                }
//                else -> {
//                    // Shouldn't happen.
//                    Log.d("one tap", "No ID token!")
//                }
//            }

        try {
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.let { intent ->
                    oneTapClient.getSignInCredentialFromIntent(intent)
                }?.also { signInCredential ->
                    val googleIdToken =
                        signInCredential.googleIdToken?.also { googleIdToken ->
                            Log.d(TAG, "googleIdToken: $googleIdToken")
                        }
                    val username = signInCredential.id
                    Log.d(TAG, "username: $username")
                    val password = signInCredential.password?.also { password ->
                        Log.d(TAG, "password: $password")
                    }
//                    LoginFragmentDirections.actionLoginFragmentToMaintenanceFragment(
//                        null,
//                        signInCredential
//                    )
//                        .also { navDirections ->
//                        findNavController().navigate(navDirections)
//                    }
                }
            }
        } catch (e: ApiException) {
            when (e.statusCode) {
                CommonStatusCodes.CANCELED -> {
                    Log.d(TAG, "One-tap dialog was closed.: ${e.message.toString()}")
                }
                CommonStatusCodes.NETWORK_ERROR -> {
                    Log.d(TAG, "One-tap encountered a network error.: ${e.message.toString()}")
                }
                else -> {
                    Log.d(TAG, "Couldn't get credential from result.: ${e.localizedMessage}")
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
        signInRequest = BeginSignInRequest.builder()
            .setPasswordRequestOptions(BeginSignInRequest.PasswordRequestOptions.builder()
                .setSupported(true)
                .build())
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
        val request = GetSignInIntentRequest.builder()
            .setServerClientId(getString(R.string.web_client_id))
            .build()
        Identity.getSignInClient(requireActivity())
            .getSignInIntent(request)
            .addOnSuccessListener { result: PendingIntent ->
                try {
                    startIntentSenderForResult(
                        result.intentSender,
                        REQ_ONE_TAP,  /* fillInIntent= */
                        null,  /* flagsMask= */
                        0,  /* flagsValue= */
                        0,  /* extraFlags= */
                        0,  /* options= */
                        null
                    )
                } catch (e: IntentSender.SendIntentException) {
                    Log.e(TAG, "Google Sign-in failed")
                }
            }
            .addOnFailureListener { e: Exception? ->
                Log.e(
                    TAG,
                    "Google Sign-in failed",
                    e
                )
            }
    }

}
