package edu.pw.aicatching.authorization

import android.app.Activity
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import edu.pw.aicatching.R
import edu.pw.aicatching.models.Credentials
import edu.pw.aicatching.viewModels.UserViewModel
import kotlinx.android.synthetic.main.fragment_authorization.*

class AuthorizationFragment : Fragment() {
    private val viewModel: UserViewModel by activityViewModels()

    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest
    private lateinit var signUpRequest: BeginSignInRequest
    private var signUpTryCounter: Int = 0
    private val oneTapLoggingResult = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        try {
            if (result.resultCode == Activity.RESULT_OK) {
                val credential = oneTapClient.getSignInCredentialFromIntent(result.data)
                if (credential.googleIdToken != null) {
                    this.viewModel.logIn(
                        Credentials(email = credential.id, token = credential.googleIdToken)
                    )
                }
            }
        } catch (e: ApiException) {
            when (e.statusCode) {
                CommonStatusCodes.NETWORK_ERROR -> {
                    Log.d("Authorization:OneTapLoggingResult:NetworkError", "One-tap encountered a network error.")
                }
                else -> {
                    Log.d(
                        "Authorization:OneTapLoggingResult:OtherError",
                        "Couldn't get credential from result." +
                            " (${e.localizedMessage})"
                    )
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
        viewModel.userLiveData.observe(
            this.viewLifecycleOwner
        ) { user ->
            if (user != null) {
                view?.let { view ->
                    Navigation.findNavController(view).navigate(R.id.mainFragment)
                }
            }
        }

        oneTapClient = Identity.getSignInClient(this.requireActivity())

        signUpRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                createBeginSignInRequest(false)
            )
            .build()

        signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                createBeginSignInRequest(true)
            )
            .setAutoSelectEnabled(true)
            .build()

        return viewBinding
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        authorizationButton.setOnClickListener {
            sign(signInRequest)
        }
    }

    private fun createBeginSignInRequest(filterByAuthorizedAccount: Boolean) =
        BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
            .setSupported(true)
            .setServerClientId(getString(R.string.web_client_id))
            .setFilterByAuthorizedAccounts(filterByAuthorizedAccount)
            .build()

    private fun sign(request: BeginSignInRequest) {
        oneTapClient.beginSignIn(request)
            .addOnSuccessListener(this.requireActivity()) { result ->
                try {
                    val intentSenderRequest = IntentSenderRequest.Builder(result.pendingIntent).build()
                    oneTapLoggingResult.launch(intentSenderRequest)
                } catch (e: IntentSender.SendIntentException) {
                    e.localizedMessage?.let { Log.d("AuthorizationFragment:Sign:OnSuccessListener", it) }
                }
            }
            .addOnFailureListener(this.requireActivity()) { e ->
                e.localizedMessage?.let { Log.d("AuthorizationFragment:Sign:OnFailureListener", it) }
                if (signUpTryCounter <= 3) {
                    signUpTryCounter += 1
                    sign(signUpRequest)

                } else {
                    Toast.makeText(
                        this.context,
                        "Check your Google Account. You must be logged in to your account on your phone, before logging into app.",
                        Toast.LENGTH_LONG
                    ).show()
                    signUpTryCounter = 0
                }
            }
    }
}
