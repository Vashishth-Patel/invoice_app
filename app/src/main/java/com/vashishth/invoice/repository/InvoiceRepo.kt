package com.vashishth.invoice.repository


import com.vashishth.invoice.data.InvoiceDAO
import com.vashishth.invoice.data.entity.*
import com.vashishth.invoice.data.relations.CartAndProduct
import com.vashishth.invoice.data.relations.InvoiceWithInvoiceItems
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class InvoiceRepo @Inject constructor(
    private val invoiceDAO: InvoiceDAO
) {

    suspend fun insertCustomer(customer: Customer) = withContext(Dispatchers.IO){
        invoiceDAO.insertCustomer(customer)
    }
    suspend fun insertCustomerList(customerList: List<Customer>) = withContext(Dispatchers.IO){
        customerList.forEach{
            invoiceDAO.insertCustomer(it)
        }
    }
    suspend fun insertCartItem(cartItem:Cart) = withContext(Dispatchers.IO){
        invoiceDAO.insertItemToCart(cartItem)
    }
    suspend fun insertInvoiceItem(invoiceItem: InvoiceItem) = withContext(Dispatchers.IO){
        invoiceDAO.insertInvoiceItem(invoiceItem)
    }
    suspend fun insertInvoice(invoice: Invoice) = withContext(Dispatchers.IO){
        invoiceDAO.insertInvoice(invoice)
    }
    suspend fun insertProduct(product: Product) = withContext(Dispatchers.IO){
        invoiceDAO.insertProduct(product)
    }
    suspend fun insertBusinessName(businessDetail: BusinessDetail) = withContext(Dispatchers.IO){
        invoiceDAO.insertBusinessName(businessDetail)
    }
    suspend fun insertAddress(address: Address) = withContext(Dispatchers.IO){
        invoiceDAO.insertAddress(address)
    }
    suspend fun insertBusinessDetail(business: Business) = withContext(Dispatchers.IO){
        invoiceDAO.insertBusiness(business)
    }

    suspend fun insertLogo(businessLogo: BusinessLogo) = withContext(Dispatchers.IO){
        invoiceDAO.insertLogo(businessLogo)
    }

    suspend fun insertSign(businessSign: BusinessSign) = withContext(Dispatchers.IO){
        invoiceDAO.insertSign(businessSign)
    }

    suspend fun updateCustomer(name: String,phoneNumber : Long?,email : String?,id : Int) = withContext(Dispatchers.IO){
        invoiceDAO.updateCustomer(name =  name,phoneNumber = phoneNumber,email = email,id = id)
    }
    suspend fun updateItem(itemName:String,price:Double,unit:String,hsnNumber:Int?,stock:Int,GSTRate:String?,purchasePrice:Double,description:String?,itemNumber:Int) = withContext(Dispatchers.IO){
        invoiceDAO.updateItem(itemName,price,unit,hsnNumber,stock,GSTRate,purchasePrice,description,itemNumber)
    }
    suspend fun updateItemCount(stock: Int,itemNumber: Int) = withContext(Dispatchers.IO){
        invoiceDAO.updateItemCount(stock,itemNumber)
    }

    suspend fun updateBusiness(businessName:String, businessId : Int) = withContext(Dispatchers.IO){
        invoiceDAO.updateBusiness(businessName,businessId)
    }
    suspend fun updateBusinessDetails(legalName:String,PANNumber:String?,Gstin:String?,phoneNumber: Long,email: String?,website:String?,businessId: Int) = withContext(Dispatchers.IO){
        invoiceDAO.updateBusinessDetails(legalName,PANNumber, Gstin, phoneNumber, email, website, businessId)
    }
    suspend fun updateAddress(address: String,PINCode : Int?,state:String?,city:String?,id:Int) = withContext(Dispatchers.IO){
        invoiceDAO.updateAddress(address, PINCode, state, city, id)
    }

    suspend fun updateCartItem(name: String,count:Int) = withContext(Dispatchers.IO){
        invoiceDAO.updateCartItem(name,count)
    }
    suspend fun emptyCart() = withContext(Dispatchers.IO){
        invoiceDAO.emptyCart(0)
    }

    suspend fun deleteLogo() = invoiceDAO.deleteLogo()

    fun getLastInvoiceNumber() : Flow<List<Int>> = invoiceDAO.lastInvoiceNumber().flowOn(Dispatchers.IO)
        .conflate()
    fun getInvoiceItems():Flow<List<InvoiceItem>> = invoiceDAO.getInvoiceItems().flowOn(Dispatchers.IO)
        .conflate()

    fun getInvoice() :Flow<List<InvoiceWithInvoiceItems>> = invoiceDAO.getInvoice().flowOn(Dispatchers.IO)
        .conflate()
    fun getCartItems():Flow<List<Cart>> = invoiceDAO.getCartItems().flowOn(Dispatchers.IO)
        .conflate()
    fun getCartAndProduct():Flow<List<CartAndProduct>> = invoiceDAO.getCartAndProduct().flowOn(Dispatchers.IO)
        .conflate()
    fun getCustomerList() : Flow<List<Customer>> = invoiceDAO.getCustomerList().flowOn(Dispatchers.IO)
        .conflate()
    fun getProductList() : Flow<List<Product>> = invoiceDAO.getProductList().flowOn(Dispatchers.IO)
        .conflate()
    fun getBusinessName() = invoiceDAO.getBusinessName()
    fun getBusinessDetail() = invoiceDAO.getBusinessDetail()
    fun getAddressList() = invoiceDAO.getAddressList()

    fun checkBusinessName() = invoiceDAO.checkBusinessName()
    fun checkBusinessDetail() = invoiceDAO.checkBusinessDetail()
    fun checkLogo() = invoiceDAO.checkLogo()
    fun getLogo() = invoiceDAO.getLogo()
    fun getSign() : Flow<List<BusinessSign>> = invoiceDAO.getSign().flowOn(Dispatchers.IO)
        .conflate()
    suspend fun deleteCart() = invoiceDAO.deleteCart()

    suspend fun deleteCustomer(customer: Customer) = invoiceDAO.deleteCustomer(customer)
    suspend fun deleteItem(product: Product) = invoiceDAO.deleteItem(product)

}