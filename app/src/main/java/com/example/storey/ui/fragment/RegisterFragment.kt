package com.example.storey.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.storey.R
import com.example.storey.data.local.model.RegisterModel
import com.example.storey.data.remote.response.RegisterResponse
import com.example.storey.data.repository.RetrofitRepository
import com.example.storey.databinding.FragmentRegisterBinding
import com.example.storey.helper.makeSnackbar
import com.example.storey.helper.setError
import com.example.storey.ui.viewmodel.RegisterViewModel
import com.example.storey.ui.viewmodel.RetrofitViewModelFactory

class RegisterFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val registerViewModel by viewModels<RegisterViewModel> {
        RetrofitViewModelFactory.getInstance(RetrofitRepository())
    }
    private lateinit var nameEditText: Editable
    private lateinit var emailEditText: Editable
    private lateinit var passwordEditText: Editable

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        observeData()
        binding.btnRegister.setOnClickListener(this)
    }

    private fun observeData() {
        registerViewModel.registerResponse.observe(this, { response ->
            setUpAction(response)
        })

        registerViewModel.isLoading.observe(this, { isLoading ->
            when {
                isLoading -> {
                    binding.progressBar.isVisible = true
                    binding.btnRegister.isClickable = false
                }
                else -> {
                    binding.progressBar.isVisible = false
                    binding.btnRegister.isClickable = true
                }
            }
        })
    }

    private fun setUpAction(response: RegisterResponse) {
        when {
            !response.error -> {
                resources.getString(R.string.register_success).makeSnackbar(binding.root).show()
            }
            else -> {
                resources.getString(R.string.register_error).makeSnackbar(binding.root).show()
            }
        }
    }


    private fun isFieldValidate(): Boolean {
        var isValidate = true
        val nameField = resources.getString(R.string.name)
        val emailField = resources.getString(R.string.email)
        val passwordField = resources.getString(R.string.password)
        nameEditText = binding.etName.editText!!.text
        emailEditText = binding.etEmail.editText!!.text
        passwordEditText = binding.etPassword.editText!!.text

        if (nameEditText.isEmpty()) {
            binding.etName.error = nameField.setError(requireContext())
            isValidate = false
        }
        if (emailEditText.isEmpty()) {
            binding.etEmail.error = emailField.setError(requireContext())
            isValidate = false
        }
        if (passwordEditText.isEmpty()) {
            binding.etPassword.error = passwordField.setError(requireContext())
            isValidate = false
        }
        if (binding.etName.error != null || binding.etEmail.error != null || binding.etPassword.error != null) {
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
            R.id.btn_register -> {
                if (isFieldValidate()) {
                    val registerModel = RegisterModel(
                        name = nameEditText.toString(),
                        email = emailEditText.toString(),
                        password = passwordEditText.toString()
                    )
                    registerViewModel.register(registerModel)
                }
            }
        }
    }


}