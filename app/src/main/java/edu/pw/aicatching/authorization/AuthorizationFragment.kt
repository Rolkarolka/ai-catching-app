package edu.pw.aicatching.authorization

import android.app.Activity
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import edu.pw.aicatching.R
import edu.pw.aicatching.databinding.FragmentAuthorizationBinding
import edu.pw.aicatching.models.Credentials
import edu.pw.aicatching.viewModels.UserViewModel

class AuthorizationFragment : Fragment() {
    private val viewModel: UserViewModel by activityViewModels()

    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest
    private lateinit var signUpRequest: BeginSignInRequest
    private var signUpTryCounter: Int = 0
    private var _binding: FragmentAuthorizationBinding? = null
    private val binding get() = _binding!!
    private val oneTapLoggingResult = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        try {
            if (result.resultCode == Activity.RESULT_OK) { signIn(result) } else hideProgressBar()
        } catch (e: ApiException) { catchLoggingExceptions(e) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthorizationBinding.inflate(inflater, container, false)
        prepareForLoggingUserIn()
        handleUserErrorMessage()
        oneTapClient = Identity.getSignInClient(requireActivity())
        createSignInRequest()
        createSignUpRequest()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.authorizationButton.setOnClickListener {
            showProgressBar()
            sign(signInRequest)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        hideProgressBar()
        _binding = null
    }

    private fun signIn(result: ActivityResult) {
        val credential = oneTapClient.getSignInCredentialFromIntent(result.data)
        if (credential.googleIdToken != null) {
            this.viewModel.logIn(
                Credentials(email = credential.id, token = credential.googleIdToken)
            )
        }
    }

    private fun showProgressBar() {
        activity?.window?.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.INVISIBLE
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    private fun catchLoggingExceptions(e: ApiException) {
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

    private fun prepareForLoggingUserIn() {
        viewModel.user.observe(
            this.viewLifecycleOwner
        ) { user ->
            if (user != null) {
                hideProgressBar()
                view?.let { view ->
                    Navigation.findNavController(view).navigate(R.id.mainFragment)
                }
            }
        }
    }

    private fun handleUserErrorMessage() {
        viewModel.userErrorMessage.observe(
            viewLifecycleOwner
        ) {
            Log.d("AuthorizationFragment:onCreateView:logIn", it)
            hideProgressBar()
            Toast.makeText(
                this.context,
                "Sorry, there was a problem with connecting to server. " +
                    "Please, try again later.",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun createSignUpRequest() {
        signUpRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                createBeginSignInRequest(false)
            )
            .setAutoSelectEnabled(false)
            .build()
    }

    private fun createSignInRequest() {
        signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                createBeginSignInRequest(true)
            )
            .setAutoSelectEnabled(true)
            .build()
    }

    private fun createBeginSignInRequest(filterByAuthorizedAccount: Boolean) =
        BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
            .setSupported(true)
            .setServerClientId(getString(R.string.google_client_id))
            .setFilterByAuthorizedAccounts(filterByAuthorizedAccount)
            .build()

    private fun sign(request: BeginSignInRequest) {
        oneTapClient.beginSignIn(request)
            .addOnSuccessListener(requireActivity()) { result -> onSignSuccess(result) }
            .addOnFailureListener(requireActivity()) { e -> onSignFailure(e) }
    }

    private fun onSignSuccess(result: BeginSignInResult) {
        try {
            val intentSenderRequest = IntentSenderRequest.Builder(result.pendingIntent).build()
            oneTapLoggingResult.launch(intentSenderRequest)
        } catch (e: IntentSender.SendIntentException) {
            e.localizedMessage?.let { Log.d("AuthorizationFragment:Sign:OnSuccessListener", it) }
        }
    }

    private fun onSignFailure(e: Exception) {
        e.localizedMessage?.let { Log.d("AuthorizationFragment:Sign:OnFailureListener", it) }
        if (signUpTryCounter++ <= MAX_LOGGING_TRIES) {
            sign(signUpRequest)
        } else {
            Toast.makeText(
                this.context,
                "Check your Google Account." +
                    " You must be logged in to your account on your phone, before logging into app.",
                Toast.LENGTH_LONG
            ).show()
            signUpTryCounter = 0
            hideProgressBar()
        }
    }

    companion object {
        const val MAX_LOGGING_TRIES = 3
    }
}
