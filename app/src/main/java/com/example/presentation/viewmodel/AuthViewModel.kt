package com.example.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.AuthRepository
import com.example.domain.model.CountryCode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    val countryCodes = authRepository.countryCodes

    private val _selectedCountry = MutableStateFlow(countryCodes.first())
    val selectedCountry: StateFlow<CountryCode> = _selectedCountry.asStateFlow()

    private val _phoneNumber = MutableStateFlow("")
    val phoneNumber: StateFlow<String> = _phoneNumber.asStateFlow()

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _otpCode = MutableStateFlow("")
    val otpCode: StateFlow<String> = _otpCode.asStateFlow()

    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName.asStateFlow()

    private val _userBio = MutableStateFlow("متصل عبر صلة ✨")
    val userBio: StateFlow<String> = _userBio.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun selectCountry(country: CountryCode) {
        _selectedCountry.value = country
    }

    fun onPhoneChanged(number: String) {
        _phoneNumber.value = number
        _errorMessage.value = null
    }

    fun onEmailChanged(text: String) {
        _email.value = text
        _errorMessage.value = null
    }

    fun onOtpChanged(code: String) {
        _otpCode.value = code
        _errorMessage.value = null
    }

    fun onNameChanged(name: String) {
        _userName.value = name
    }

    fun onBioChanged(bio: String) {
        _userBio.value = bio
    }

    fun requestPhoneOtp(onSuccess: (fullPhone: String) -> Unit) {
        val fullPhone = "${selectedCountry.value.code} ${_phoneNumber.value.trim()}"
        if (_phoneNumber.value.trim().length < 6) {
            _errorMessage.value = "يرجى إدخال رقم هاتف صحيح"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            val success = authRepository.requestPhoneOtp(selectedCountry.value.code, _phoneNumber.value)
            _isLoading.value = false
            if (success) {
                onSuccess(fullPhone)
            } else {
                _errorMessage.value = "تعذر إرسال رمز التحقق. حاول مرة أخرى."
            }
        }
    }

    fun verifyOtp(fullPhoneOrEmail: String, onSuccess: () -> Unit) {
        if (_otpCode.value.trim().length < 4) {
            _errorMessage.value = "رمز التحقق يجب أن يكون مكوناً من 6 أرقام (أو 123456 للتجربة)"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            val success = authRepository.verifyOtp(_otpCode.value)
            _isLoading.value = false
            if (success) {
                onSuccess()
            } else {
                _errorMessage.value = "رمز التحقق غير صحيح"
            }
        }
    }

    fun submitEmailLink(onSuccess: () -> Unit) {
        if (!_email.value.contains("@")) {
            _errorMessage.value = "يرجى إدخال بريد إلكتروني صحيح"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            val success = authRepository.requestEmailLink(_email.value)
            _isLoading.value = false
            if (success) {
                onSuccess()
            } else {
                _errorMessage.value = "تعذر إرسال رابط التفعيل"
            }
        }
    }

    fun finishProfileSetup(onSuccess: () -> Unit) {
        if (_userName.value.trim().isEmpty()) {
            _errorMessage.value = "يرجى إدخال اسمك الكريم"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            authRepository.completeProfileRegistration(
                name = _userName.value.trim(),
                phone = "${selectedCountry.value.code} ${_phoneNumber.value.trim()}",
                email = _email.value.trim(),
                bio = _userBio.value.trim()
            )
            _isLoading.value = false
            onSuccess()
        }
    }
}
