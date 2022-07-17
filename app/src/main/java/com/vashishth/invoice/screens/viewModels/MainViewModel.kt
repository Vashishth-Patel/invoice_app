package com.vashishth.invoice.screens.viewModels

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vashishth.invoice.data.entity.*
import com.vashishth.invoice.navigation.Screen
import com.vashishth.invoice.repository.InvoiceRepo
import com.vashishth.invoice.screens.AddBusiness.BusinessDetailFormState
import com.vashishth.invoice.screens.AddBusiness.BusinessFormState
import com.vashishth.invoice.screens.AddCustomer.AllFormEvent
import com.vashishth.invoice.screens.AddCustomer.CustomerFormState
import com.vashishth.invoice.screens.AddItem.ItemFormState
import com.vashishth.invoice.validation.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val validateName: ValidateName,
    private val validatePhone: ValidatePhone,
    private val validateEmail: ValidateEmail,
    private val validateGSTIN: ValidateGSTIN,
    private val validatePANNumber: ValidatePANNumber,
    private val validateWebsite: ValidateWebsite,
    private val validatePIN: ValidatePIN,
    private val validateNumber: ValidateNumber,
    private val repository : InvoiceRepo
) : ViewModel() {

    //get function customer list
    private val _customerList = MutableStateFlow<List<Customer>>(emptyList())
    val customerList = _customerList.asStateFlow()

    init {
        viewModelScope.launch (Dispatchers.IO){
            repository.getCustomerList().distinctUntilChanged()
                .collect{listOfCustomers ->
                    if (listOfCustomers.isNullOrEmpty()){
                        Log.d("Empty",": Empty List")
                    }else{
                        _customerList.value = listOfCustomers
                    }
                }
        }
    }

    //Customer Form
    var state by mutableStateOf(CustomerFormState())
    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    fun onEvent(event: AllFormEvent){
        when(event){
            //Customer
            is AllFormEvent.nameChanged -> {
                state = state.copy(name = event.name)
            }
            is AllFormEvent.emailChanged -> {
                state = state.copy(email = event.email)
            }
            is AllFormEvent.phoneChanged -> {
                state = state.copy(phone = event.phone)
            }
            is AllFormEvent.CustomerSubmit -> {
                submitCustomerData()
            }

            //Business Name
            is AllFormEvent.businessNameChanged -> {
                businessState1 = businessState1.copy(name = event.name)
            }
            is AllFormEvent.BusinessNameSubmit -> {
                subimtBusinessname()
            }

            //Business Detail
            is AllFormEvent.legalNameChanged -> {
                businessState2 = businessState2.copy(legalName = event.legalname)
            }
            is AllFormEvent.PANNumberChanged -> {
                businessState2 = businessState2.copy(PANNumber = event.PANNumber)
            }is AllFormEvent.GSTINNumberChanged -> {
            businessState2 = businessState2.copy(Gstin = event.GSTINNumber)
            }
            is AllFormEvent.businessPhoneChanged -> {
                businessState2 = businessState2.copy(phoneNumber = event.phoneNumber)
            }is AllFormEvent.websiteChanged -> {
            businessState2 = businessState2.copy(website = event.website)
            }
            is AllFormEvent.businessEmailChanged -> {
                businessState2 = businessState2.copy(email = event.businessEmail)
            }
            is AllFormEvent.businessAddressChanged -> {
                businessState2 = businessState2.copy(address = event.address)
            }
            is AllFormEvent.PINCodeChanged -> {
                businessState2 = businessState2.copy(PINCode = event.PIN)
            }
            is AllFormEvent.StateChanged -> {
            businessState2 = businessState2.copy(state = event.state)
            }
            is AllFormEvent.CityChanged -> {
                businessState2 = businessState2.copy(city = event.city)
            }
            is AllFormEvent.BusinessDetailSubmit -> {
                submitBusinessDetail()
            }

            //Add item
            is AllFormEvent.itemNameChanged -> {
                itemState = itemState.copy(itemName = event.itemName)
            }
            is AllFormEvent.itemNumberChanged -> {
                itemState = itemState.copy(itemNumber = event.itemNumber)
            }
            is AllFormEvent.itemPriceChanged -> {
                itemState = itemState.copy(price = event.itemPrice)
            }
            is AllFormEvent.itemUnitChanged -> {
                itemState = itemState.copy(unit = event.itemUnit)
            }
            is AllFormEvent.itemHSNNumberChanged -> {
                itemState = itemState.copy(hsnNumber = event.itemHSN)
            }
            is AllFormEvent.itemStockChanged -> {
                itemState = itemState.copy(stock = event.itemStock)
            }
            is AllFormEvent.itemGSTRateChanged -> {
                itemState = itemState.copy(gstRate = event.itemGST)
            }
            is AllFormEvent.itemStockAddDAteChanged -> {
                itemState = itemState.copy(stockAddDate = event.stockAddDate)
            }
            is AllFormEvent.itemPurchasePriceChanged -> {
                itemState = itemState.copy(purchasePrice = event.itemPurchasePrice)
            }
            is AllFormEvent.itemDescriptionChanged -> {
                itemState = itemState.copy(description = event.itemDescription)
            }
            is AllFormEvent.AddItem -> {
                SubmitItemData()
            }
        }
    }

    private fun submitCustomerData(){
        val nameResult = validateName.execute(state.name)
        val emailResult = validateEmail.execute(state.email)
        val phoneResult = validatePhone.execute(state.phone)
        val hasError = listOf(
            emailResult,
            nameResult,
            phoneResult
        ).any { !it.successful }

        if(hasError){
            state = state.copy(
                emailError = emailResult.errorMessage,
                nameError = nameResult.errorMessage,
                phoneError = phoneResult.errorMessage
            )
            return
        }
        viewModelScope.launch {
            validationEventChannel.send(ValidationEvent.Success)
            repository.insertCustomer(
                Customer(
                    name = state.name,
                    phoneNumber = state.phone?.toLong(),
                    email = state.email
                )
            )
        }
    }

    //business name form
    var businessState1 by mutableStateOf(BusinessFormState())
    var businessState2 by mutableStateOf(BusinessDetailFormState())

    private fun subimtBusinessname(){
        val nameResult = validateName.execute(businessState1.name)
        val hasError = listOf(
            nameResult
        ).any { !it.successful }

        if (hasError){
            businessState1 = businessState1.copy(
                nameError = nameResult.errorMessage
            )
            return
        }
        viewModelScope.launch {
            validationEventChannel.send(ValidationEvent.Success)
            repository.insertBusinessName(
                BusinessDetail(businessName = businessState1.name)
            )

        }
    }

    private fun submitBusinessDetail(){
        val legalNameResult = validateName.execute(businessState2.legalName)
        val PANNumberResult = validatePANNumber.execute(businessState2.PANNumber)
        val GSTINResult = validateGSTIN.execute(businessState2.Gstin)
        val PhoneNumberResult = validatePhone.execute(businessState2.phoneNumber)
        val WebsiteResult = validateWebsite.execute(businessState2.website)
        val emailResult = validateEmail.execute(businessState2.email)
        val PINResult = validatePIN.execute(businessState2.PINCode)
        val addressResult = validateName.execute(businessState2.address)
        val hasError = listOf(
            emailResult,
            legalNameResult,
            PANNumberResult,
            GSTINResult,
            PhoneNumberResult,
            PINResult,
            addressResult,
            WebsiteResult
        ).any { !it.successful }

        if (hasError){
            businessState2 = businessState2.copy(
                legalNameError = legalNameResult.errorMessage,
                PANNumberError = PANNumberResult.errorMessage,
                GstinError = GSTINResult.errorMessage,
                phoneNumberError = PhoneNumberResult.errorMessage,
                websiteError = WebsiteResult.errorMessage,
                emailError = emailResult.errorMessage,
                addressError = addressResult.errorMessage,
                PINCodeError = PINResult.errorMessage
            )
            return
        }
        viewModelScope.launch {
            validationEventChannel.send(ValidationEvent.Success)
            repository.insertBusinessDetail(
                Business(
                    legalName = businessState2.legalName,
                    PANNumber = businessState2.PANNumber,
                    Gstin = businessState2.Gstin,
                    phoneNumber = businessState2.phoneNumber.toLong(),
                    website = businessState2.website,
                    email = businessState2.email
                )
            )
            repository.insertAddress(
                Address(
                    address = businessState2.address,
                    PINCode = businessState2.PINCode?.toInt(),
                    state = businessState2.state,
                    city = businessState2.city
                )
            )
        }
    }

    //item form
    var itemState by mutableStateOf(ItemFormState())

    private fun SubmitItemData(){
        val itemNameResult = validateName.execute(itemState.itemName)
        val itemNumberResult = validateNumber.execute(itemState.itemNumber)
        val itemPriceResult = validateName.execute(itemState.price)
        val itemUnitResult = validateName.execute(itemState.unit)
        val itemHsnResult = validateNumber.execute(itemState.hsnNumber)
        val itemStockResult = validateName.execute(itemState.stock)
        val itemStockAddDateResult = validateName.execute(itemState.stockAddDate)

        val hasError = listOf(
            itemNameResult,
            itemNumberResult,
            itemPriceResult,
            itemUnitResult,
            itemHsnResult,
            itemStockResult,
            itemStockAddDateResult
        ).any { !it.successful }

        if (hasError){
            itemState = itemState.copy(
                itemNameError = itemNameResult.errorMessage,
                itemNumberError = itemNumberResult.errorMessage,
                priceError = itemPriceResult.errorMessage,
                unitError = itemUnitResult.errorMessage,
                hsnNumberError = itemHsnResult.errorMessage,
                stockError = itemStockResult.errorMessage,
                stockAddDateError = itemStockAddDateResult.errorMessage
            )
            return
        }
        viewModelScope.launch {
            validationEventChannel.send(ValidationEvent.Success)
            repository.insertProduct(
                Product(
                    itemName = itemState.itemName,
                    itemNumber = itemState.itemNumber?.toInt(),
                    price = itemState.price.toDouble(),
                    unit = itemState.unit,
                    hsnNumber = itemState.hsnNumber?.toInt(),
                    stock = itemState.stock.toInt(),
                    GSTRate = itemState.gstRate,
                    stockAddDate = DateFormater(itemState.stockAddDate),
                    purchasePrice = itemState.purchasePrice.toDouble(),
                    description = itemState.description
                )
            )

        }

    }



    @SuppressLint("SimpleDateFormat")
    fun DateFormater(str : String) : Date {
        val formatter = SimpleDateFormat("dd-mm-yyyy")
        val date = formatter.parse(str)
        return date
    }

    fun insertCustomerList(customerList: List<Customer>) = viewModelScope.launch {
        repository.insertCustomerList(customerList)
    }

    fun deleteCustomer(customer: Customer) = viewModelScope.launch{
        repository.deleteCustomer(customer)
    }

    //
    fun insertLogo(businessLogo: BusinessLogo) = viewModelScope.launch {
        repository.insertLogo(businessLogo)
    }

    fun insertSign(businessSign: BusinessSign) = viewModelScope.launch {
        repository.insertSign(businessSign)
    }



    //insert businesss

    var businessSign = getSign()

    lateinit var signature: Bitmap


    fun checkBusinessName() = repository.checkBusinessName()
    fun checkBusinessDetail() = repository.checkBusinessDetail()
    fun checkLogo() = repository.checkLogo()
    fun getLogo() = repository.getLogo()
    fun getSign() = repository.getSign()
    fun deleteLogo() = viewModelScope.launch {
        repository.deleteLogo()
    }

    fun getBusinessNameList() = repository.getBusinessName()
    fun getBusinessDetail() = repository.getBusinessDetail()
    fun getAddressList() = repository.getAddressList()

    sealed class ValidationEvent {
        object Success: ValidationEvent()
    }

}