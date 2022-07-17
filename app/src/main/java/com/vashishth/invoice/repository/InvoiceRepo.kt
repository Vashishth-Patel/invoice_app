package com.vashishth.invoice.repository


import com.vashishth.invoice.data.InvoiceDAO
import com.vashishth.invoice.data.entity.*
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

    suspend fun deleteLogo() = invoiceDAO.deleteLogo()

    fun getCustomerList() : Flow<List<Customer>> = invoiceDAO.getCustomerList().flowOn(Dispatchers.IO)
        .conflate()
    fun getBusinessName() = invoiceDAO.getBusinessName()
    fun getBusinessDetail() = invoiceDAO.getBusinessDetail()
    fun getAddressList() = invoiceDAO.getAddressList()

    fun checkBusinessName() = invoiceDAO.checkBusinessName()
    fun checkBusinessDetail() = invoiceDAO.checkBusinessDetail()
    fun checkLogo() = invoiceDAO.checkLogo()
    fun getLogo() = invoiceDAO.getLogo()
    fun getSign() = invoiceDAO.getSign()

    suspend fun deleteCustomer(customer: Customer) = invoiceDAO.deleteCustomer(customer)

}