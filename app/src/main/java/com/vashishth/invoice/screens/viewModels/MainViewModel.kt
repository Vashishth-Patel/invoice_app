package com.vashishth.invoice.screens.viewModels

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.vashishth.invoice.data.entity.*
import com.vashishth.invoice.data.relations.CartAndProduct
import com.vashishth.invoice.data.relations.InvoiceWithInvoiceItems
import com.vashishth.invoice.repository.InvoiceRepo
import com.vashishth.invoice.screens.AddBusiness.BusinessDetailFormState
import com.vashishth.invoice.screens.AddBusiness.BusinessFormState
import com.vashishth.invoice.screens.AddCustomer.*
import com.vashishth.invoice.screens.AddInvoice.InvoiceFormState
import com.vashishth.invoice.screens.AddInvoice.InvoiceItemState
import com.vashishth.invoice.screens.AddItem.CartItemState
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

    //get cart items
    private val _cartItemsList = MutableStateFlow<List<Cart>>(emptyList())
    val cartItemsList = _cartItemsList.asStateFlow()

    //get cart and product items
    private val _cartAndProductItemsList = MutableStateFlow<List<CartAndProduct>>(emptyList())
    val cartAndProductItemsList = _cartAndProductItemsList.asStateFlow()

    //get products list
    private val _productList = MutableStateFlow<List<Product>>(emptyList())
    val productList = _productList.asStateFlow()

    //get signature
    private val _signList = MutableStateFlow<List<BusinessSign>>(emptyList())
    val signList = _signList.asStateFlow()

    //get last invoice number
    private val _lastInvoiceNum = MutableStateFlow<List<Int>>(emptyList())
    val lastInvoiceNum = _lastInvoiceNum.asStateFlow()

    //get invoiceItems
    private val _invoiceItems = MutableStateFlow<List<InvoiceItem>>(emptyList())
    val invoiceItems = _invoiceItems.asStateFlow()

    //get INvoice
    private val _invoice = MutableStateFlow<List<InvoiceWithInvoiceItems>>(emptyList())
    val invoice = _invoice.asStateFlow()




    //populating customer list
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
        //populating invoice Items
        viewModelScope.launch {
            repository.getInvoiceItems().distinctUntilChanged()
                .collect{invoiceItemsList ->
                    if (invoiceItemsList.isNullOrEmpty()){
                        Log.d("Enpty","Empty List")
                    }else{
                        _invoiceItems.value = invoiceItemsList
                    }
                }
        }



        viewModelScope.launch{
            repository.getInvoice().distinctUntilChanged()
                .collect{
                    if(it.isNullOrEmpty()){
                        Log.d("Empty","Empty list")
                    }else{
                        _invoice.value = it
                    }
                }
        }

        //populating cartItems list
        viewModelScope.launch (Dispatchers.IO){
            repository.getCartItems().distinctUntilChanged()
                .collect{listOfCart ->
                    if (listOfCart.isNullOrEmpty()){
                        Log.d("Empty",": Empty List")
                    }else{
                        _cartItemsList.value = listOfCart
                    }
                }
        }
        //populating catAndproductItems list
        viewModelScope.launch (Dispatchers.IO){
            repository.getCartAndProduct().distinctUntilChanged()
                .collect{listOfCart ->
                    if (listOfCart.isNullOrEmpty()){
                        Log.d("Empty",": Empty List")
                    }else{
                        _cartAndProductItemsList.value = listOfCart
                    }
                }
        }
        //populating lastInvoice
        viewModelScope.launch (Dispatchers.IO){
            repository.getLastInvoiceNumber().distinctUntilChanged()
                .collect{lastNum ->
                    if (lastNum.isNullOrEmpty()){
                        Log.d("Empty",": Empty List")
                    }else{
                        _lastInvoiceNum.value = lastNum
                    }
                }
        }
        //populating products list
        viewModelScope.launch (Dispatchers.IO){
            repository.getProductList().distinctUntilChanged()
                .collect{product ->
                    if (product.isNullOrEmpty()){
                        Log.d("Empty",": EmptyList")
                    }else{
                        _productList.value = product
                    }
                }
        }

        viewModelScope.launch (Dispatchers.IO){
            repository.getSign().distinctUntilChanged()
                .collect{sign ->
                    if (sign.isNullOrEmpty()){
                        Log.d("Empty", ": Empty")
                    }else{
                        _signList.value = sign
                    }
                }
        }
    }

    //CartItem Form

    var cartItemState by mutableStateOf(CartItemState())

    fun onCartItemEvent(event : cartItemFormEvent){
        when(event){
            is cartItemFormEvent.cartItemNameAdd ->{
                cartItemState = cartItemState.copy(itemName = event.itemName)
            }
            is cartItemFormEvent.cartItemCount ->{
                cartItemState = cartItemState.copy(itemCount = event.itemCount)
            }
            is cartItemFormEvent.AddItemToCart ->{
                submitCartItem()
            }
            else ->{

            }
        }
    }
    private fun submitCartItem(){
        viewModelScope.launch {
            repository.insertCartItem(
                Cart(
                    itemName = cartItemState.itemName,
                    quantity = cartItemState.itemCount.toInt()
                )
            )
        }
    }

    fun updateCartItem(name: String,count : Int) = viewModelScope.launch {
        repository.updateCartItem(name =  name,count)
    }

    fun deleteCart() = viewModelScope.launch {
        repository.deleteCart()
    }

    //Customer Form
    var state by mutableStateOf(CustomerFormState())

    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    fun onEvent(event: CustomerFormEvent){
        when(event){
            //Customer
            is CustomerFormEvent.nameChanged -> {
                state = state.copy(name = event.name)
            }
            is CustomerFormEvent.emailChanged -> {
                state = state.copy(email = event.email)
            }
            is CustomerFormEvent.phoneChanged -> {
                state = state.copy(phone = event.phone)
            }
            is CustomerFormEvent.CustomerSubmit -> {
                submitCustomerData()
            }
            else -> {}
        }
    }

    //business name form
    var businessState1 by mutableStateOf(BusinessFormState())
    var businessState2 by mutableStateOf(BusinessDetailFormState())

    fun onBusinessEvent(event : businessFormEvent){
        //Business Name
        when(event) {
            is businessFormEvent.businessNameChanged -> {
                businessState1 = businessState1.copy(name = event.name)
            }
            is businessFormEvent.BusinessNameSubmit -> {
                subimtBusinessname()
            }
            //Business Detail
            is businessFormEvent.legalNameChanged -> {
                businessState2 = businessState2.copy(legalName = event.legalname)
            }
            is businessFormEvent.PANNumberChanged -> {
                businessState2 = businessState2.copy(PANNumber = event.PANNumber)
            }
            is businessFormEvent.GSTINNumberChanged -> {
                businessState2 = businessState2.copy(Gstin = event.GSTINNumber)
            }
            is businessFormEvent.businessPhoneChanged -> {
                businessState2 = businessState2.copy(phoneNumber = event.phoneNumber)
            }
            is businessFormEvent.websiteChanged -> {
                businessState2 = businessState2.copy(website = event.website)
            }
            is businessFormEvent.businessEmailChanged -> {
                businessState2 = businessState2.copy(email = event.businessEmail)
            }
            is businessFormEvent.businessAddressChanged -> {
                businessState2 = businessState2.copy(address = event.address)
            }
            is businessFormEvent.PINCodeChanged -> {
                businessState2 = businessState2.copy(PINCode = event.PIN)
            }
            is businessFormEvent.StateChanged -> {
                businessState2 = businessState2.copy(state = event.state)
            }
            is businessFormEvent.CityChanged -> {
                businessState2 = businessState2.copy(city = event.city)
            }
            is businessFormEvent.BusinessDetailSubmit -> {
                submitBusinessDetail()
            }
            else -> {}
        }
    }

    //item form
    var itemState by mutableStateOf(ItemFormState())

    fun onItemFormEvent(event : itemFormEvent){
        //Add item
        when(event) {
            is itemFormEvent.itemNameChanged -> {
                itemState = itemState.copy(itemName = event.itemName)
            }
            is itemFormEvent.itemPriceChanged -> {
                itemState = itemState.copy(price = event.itemPrice)
            }
            is itemFormEvent.itemUnitChanged -> {
                itemState = itemState.copy(unit = event.itemUnit)
            }
            is itemFormEvent.itemHSNNumberChanged -> {
                itemState = itemState.copy(hsnNumber = event.itemHSN)
            }
            is itemFormEvent.itemStockChanged -> {
                itemState = itemState.copy(stock = event.itemStock)
            }
            is itemFormEvent.itemGSTRateChanged -> {
                itemState = itemState.copy(gstRate = event.itemGST)
            }
            is itemFormEvent.itemStockAddDAteChanged -> {
                itemState = itemState.copy(stockAddDate = event.stockAddDate)
            }
            is itemFormEvent.itemPurchasePriceChanged -> {
                itemState = itemState.copy(purchasePrice = event.itemPurchasePrice)
            }
            is itemFormEvent.itemDescriptionChanged -> {
                itemState = itemState.copy(description = event.itemDescription)
            }
            is itemFormEvent.AddItem -> {
                SubmitItemData()
            }
            else -> {}
        }
    }

    //item form
    var invoiceState by mutableStateOf(InvoiceFormState())

    fun onInvoiceFormEvent(event: invoiceFromEvent){
        when(event){
            is invoiceFromEvent.entryDateChanged ->{
                invoiceState = invoiceState.copy(entryDate = event.entryDate)
            }
            is invoiceFromEvent.customerRegNoChanged ->{
                invoiceState = invoiceState.copy(customerRegNumber = event.customerRegNo)
            }
            is invoiceFromEvent.GenerateInvoice ->{
                generateInvoice()
            }
            else -> {}
        }
    }

//    invoice item form
    var invoiceItemState by mutableStateOf(InvoiceItemState())

    fun onInvoiveItemFormEvent(event : invoiceItemFormEvent){
        when(event){
            is invoiceItemFormEvent.itemNameChanged ->{
                invoiceItemState = invoiceItemState.copy(itemName = event.itemName)
            }
            is invoiceItemFormEvent.invoiceNumberChanged ->{
                invoiceItemState = invoiceItemState.copy(invoiceNumber = event.invoiceNumber)
            }
            is invoiceItemFormEvent.itemCountChanged ->{
                invoiceItemState = invoiceItemState.copy(quantity = event.itemCount)
            }
            is invoiceItemFormEvent.unitPriceChanged ->{
                invoiceItemState = invoiceItemState.copy(unitPrice = event.unitPrice)
            }
            is invoiceItemFormEvent.addInvoiceItem -> {
                addInvoiceItem()
            }
            else -> {}
        }
    }

    private fun addInvoiceItem(){
        val itemName = invoiceItemState.itemName
        val invoiceNo = invoiceItemState.invoiceNumber
        val quantity = invoiceItemState.quantity
        val unitPrice = invoiceItemState.unitPrice
        viewModelScope.launch{
            repository.insertInvoiceItem(
                InvoiceItem(
                    invoiceNumber = invoiceNo.toInt(),
                    itemName = itemName,
                    quantity = quantity.toInt(),
                    unitPrice = unitPrice.toDouble()
                )
            )
        }
    }

    private fun generateInvoice(){
        var customerRegNo = invoiceState.customerRegNumber
        var entryDate = invoiceState.entryDate

        viewModelScope.launch {
            validationEventChannel.send(ValidationEvent.Success)
            repository.insertInvoice(
                Invoice(customerRegNo = customerRegNo.toInt(),
                    entryDate = DateFormater(entryDate))
            )
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
    fun updateCustomer(name: String,phoneNumber : Long?,email : String?,id : Int) = viewModelScope.launch {
        repository.updateCustomer(name =  name,phoneNumber = phoneNumber,email = email,id = id)
    }

    fun updateItem(itemName:String,price:Double,unit:String,hsnNumber:Int?,stock:Int,GSTRate:String?,purchasePrice:Double,description:String?,itemNumber:Int) = viewModelScope.launch {
        repository.updateItem(itemName,price,unit,hsnNumber,stock,GSTRate,purchasePrice,description,itemNumber)
    }
    fun updateItemCount(stock: Int,itemNumber: Int) = viewModelScope.launch {
        repository.updateItemCount(stock,itemNumber)
    }

    fun updateBusiness(businessName:String, businessId:Int) = viewModelScope.launch {
        repository.updateBusiness(businessName,businessId)
    }

    fun updateBusinessDetails(legalName:String,PANNumber:String?,Gstin:String?,phoneNumber: Long,email: String?,website:String?,businessId: Int)  = viewModelScope.launch {
        repository.updateBusinessDetails(legalName, PANNumber, Gstin, phoneNumber, email, website, businessId)
    }

    fun updateAddress(address: String,PINCode : Int?,state:String?,city:String?,id:Int) = viewModelScope.launch {
        repository.updateAddress(address, PINCode, state, city, id)
    }








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



    private fun SubmitItemData(){
        val itemNameResult = validateName.execute(itemState.itemName)
        val itemPriceResult = validateName.execute(itemState.price)
        val itemUnitResult = validateName.execute(itemState.unit)
        val itemHsnResult = validateNumber.execute(itemState.hsnNumber)
        val itemStockResult = validateName.execute(itemState.stock)
        val itemStockAddDateResult = validateName.execute(itemState.stockAddDate)

        val hasError = listOf(
            itemNameResult,
            itemPriceResult,
            itemUnitResult,
            itemHsnResult,
            itemStockResult,
            itemStockAddDateResult
        ).any { !it.successful }

        if (hasError){
            itemState = itemState.copy(
                itemNameError = itemNameResult.errorMessage,
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
    fun deleteItem(product: Product) = viewModelScope.launch{
        repository.deleteItem(product)
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