package com.example.storey.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.storey.R
import com.example.storey.data.local.model.LoginModel
import com.example.storey.data.local.statics.StaticData
import com.example.storey.data.remote.response.LoginResponse
import com.example.storey.data.repository.RetrofitRepository
import com.example.storey.databinding.FragmentLoginBinding
import com.example.storey.helper.makeSnackbar
import com.example.storey.helper.setError
import com.example.storey.ui.activity.MainActivity
import com.example.storey.ui.viewmodel.LoginViewModel
import com.example.storey.ui.viewmodel.RetrofitViewModelFactory


class LoginFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val loginViewModel by viewModels<LoginViewModel> {
        RetrofitViewModelFactory.getInstance(RetrofitRepository())
    }
    private lateinit var emailEditText: Editable
    private lateinit var passwordEditText: Editable

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        _binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }


    private fun init() {
        observeData()
        binding.btnLogin.setOnClickListener(this)
    }

    private fun observeData() {
        loginViewModel.loginResponse.observe(this, { response ->
            setUpAction(response)
        })

        loginViewModel.isLoading.observe(this, { isLoading ->
            when {
                isLoading -> {
                    binding.progressBar.isVisible = true
                    binding.btnLogin.isClickable = false
                }
                else -> {
                    binding.progressBar.isVisible = false
                    binding.btnLogin.isClickable = true
                }
            }
        })
    }

    private fun setUpAction(response: LoginResponse) {
        when {
            !response.error -> {
                val intent = Intent(requireActivity(), MainActivity::class.java)
                intent.putExtra(StaticData.KEY_INTENT_PREF, response)
                startActivity(intent)
                requireActivity().finish()
            }
            else -> {
                when (response.message) {
                    StaticData.ERROR_401 -> resources.getString(R.string.login_error_401)
                        .makeSnackbar(binding.root).show()
                    else -> resources.getString(R.string.login_error_400).makeSnackbar(binding.root)
                        .show()
                }
            }
        }
    }

    private fun isFieldValidate(): Boolean {
        var isValidate = true
        val emailField = resources.getString(R.string.email)
        val passwordField = resources.getString(R.string.password)
        emailEditText = binding.etEmail.editText!!.text
        passwordEditText = binding.etPassword.editText!!.text


        if (emailEditText.isEmpty()) {
            binding.etEmail.error = emailField.setError(requireContext())
            isValidate = false
        }
        if (passwordEditText.isEmpty()) {
            binding.etPassword.error = passwordField.setError(requireContext())
            isValidate = false
        }
        if (binding.etEmail.error != null || binding.etPassword.error != null) {
            isValidate = false
        }
        return isValidate
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_login -> {
                if (isFieldValidate()) {
                    val loginModel = LoginModel(
                        emailEditText.toString(), passwordEditText.toString()
                    )
                    loginViewModel.login(loginModel)
                }
            }
        }
    }

}