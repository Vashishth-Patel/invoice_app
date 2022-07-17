package com.vashishth.invoice.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import com.vashishth.invoice.data.entity.*
import kotlinx.coroutines.flow.Flow

@Dao
interface InvoiceDAO {

    //Adds Customer
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomer(customer: Customer)

    //adds invoice
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInvoice(invoice: Invoice)

    //adds invoiceItem
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInvoiceItem(invoiceItem: InvoiceItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBusiness(business: Business)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAddress(address: Address)

    //business name
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBusinessName(businessDetail: BusinessDetail)

    //Adds Item
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: Product)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLogo(businessLogo: BusinessLogo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSign(businessSign: BusinessSign)

    //select item for customerlist item
    @Query("SELECT * FROM Customer")
    fun getCustomerList():Flow<List<Customer>>


    @Query("SELECT * FROM BusinessLogo")
    fun getLogo() : List<BusinessLogo>

    @Query("SELECT COUNT(*) FROM BusinessLogo")
    fun checkLogo() : Int

    @Query("DELETE FROM BusinessLogo")
    suspend fun deleteLogo()

    @Query("SELECT * FROM BusinessSign")
    fun getSign() : List<BusinessSign>

    @Delete
    suspend fun deleteCustomer(customer: Customer)

    @Query("SELECT COUNT(*) FROM businessdetail")
    fun checkBusinessName() : Int

    @Query("SELECT businessName FROM businessdetail")
    fun getBusinessName() : List<String>

    @Query("SELECT COUNT(*) FROM Business")
    fun checkBusinessDetail() : Int

    @Query("SELECT * FROM Business")
    fun getBusinessDetail() : List<Business>

    @Query("SELECT * FROM Address")
    fun getAddressList() : List<Address>


}