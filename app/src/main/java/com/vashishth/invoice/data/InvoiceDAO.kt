package com.vashishth.invoice.data

import androidx.room.*
import com.vashishth.invoice.data.entity.*
import com.vashishth.invoice.data.relations.CartAndProduct
import com.vashishth.invoice.data.relations.InvoiceWithInvoiceItems
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

    @Query("SELECT invoiceNumber FROM invoice")
    fun lastInvoiceNumber() : Flow<List<Int>>

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

    //Cart
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItemToCart(cartItem : Cart)

    @Query("UPDATE Cart SET   quantity = :count WHERE itemName =:name")
    suspend fun updateCartItem(name: String,count : Int)

    @Query("UPDATE Cart SET   quantity = :count WHERE quantity >0")
    suspend fun emptyCart(count : Int)

    //select item for customerlist item
    @Query("SELECT * FROM Customer")
    fun getCustomerList():Flow<List<Customer>>

    //get cart items
    @Query("SELECT * FROM Cart")
    fun getCartItems():Flow<List<Cart>>

    @Query("UPDATE Cart SET quantity = 0")
    suspend fun deleteCart()

    //get invoice items
    @Query("SELECT * FROM InvoiceItem")
    fun getInvoiceItems():Flow<List<InvoiceItem>>

    @Query("SELECT * FROM Invoice")
    fun getInvoice():Flow<List<InvoiceWithInvoiceItems>>

    @Transaction
    @Query("SELECT * FROM Product")
    fun getCartAndProduct(): Flow<List<CartAndProduct>>

    @Query("UPDATE Customer SET name = :name, phoneNumber = :phoneNumber, email = :email WHERE customerRegNo =:id")
    suspend fun updateCustomer(name: String,phoneNumber : Long?,email : String?,id : Int)

    @Query("UPDATE Product SET itemName = :itemName, price = :price,unit= :unit,hsnNumber = :hsnNumber,stock = :stock,GSTRate = :GSTRate,purchasePrice = :purchasePrice,description =:description WHERE itemNumber = :itemNumber")
    suspend fun updateItem(itemName:String,price:Double,unit:String,hsnNumber:Int?,stock:Int,GSTRate:String?,purchasePrice:Double,description:String?,itemNumber:Int)

    @Query("UPDATE Product SET stock = :stock WHERE itemNumber = :itemNumber")
    suspend fun updateItemCount(stock: Int,itemNumber: Int)

    @Query("UPDATE BusinessDetail SET businessName = :businessName WHERE businessId = :businessId")
    suspend fun updateBusiness(businessName:String, businessId:Int)

    @Query("UPDATE Business SET legalName = :legalName, PANNumber = :PANNumber,Gstin = :Gstin, phoneNumber = :phoneNumber,email = :email,website = :website WHERE businessId = :businessId")
    suspend fun updateBusinessDetails(legalName:String,PANNumber:String?,Gstin:String?,phoneNumber: Long,email: String?,website:String?,businessId: Int)

    @Query("UPDATE Address SET address = :address, PINCode = :PINCode, state = :state,city = :city WHERE id =:id")
    suspend fun updateAddress(address: String,PINCode : Int?,state:String?,city:String?,id:Int)

    @Query("SELECT * FROM PRODUCT")
    fun getProductList():Flow<List<Product>>

    @Query("SELECT * FROM BusinessLogo")
    fun getLogo() : List<BusinessLogo>

    @Query("SELECT COUNT(*) FROM BusinessLogo")
    fun checkLogo() : Int

    @Query("DELETE FROM BusinessLogo")
    suspend fun deleteLogo()

    @Query("SELECT * FROM BusinessSign")
    fun getSign() : Flow<List<BusinessSign>>

    @Delete
    suspend fun deleteCustomer(customer: Customer)

    @Delete
    suspend fun deleteItem(product: Product)

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