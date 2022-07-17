package com.vashishth.invoice.di

import android.content.Context
import androidx.room.Room
import com.vashishth.invoice.data.InvoiceDAO
import com.vashishth.invoice.data.InvoiceDatabase
import com.vashishth.invoice.validation.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    @Singleton
    fun providesDatabase(@ApplicationContext context: Context):InvoiceDatabase {
        return Room.databaseBuilder(context, InvoiceDatabase::class.java, "InvoiceDatabase")
            .allowMainThreadQueries()
            .build()
    }

    @Provides
    @Singleton
    fun providesDao(invoiceDatabase: InvoiceDatabase) : InvoiceDAO {
        return invoiceDatabase.getInvoiceDao()
    }

    @Provides
    fun provideValidateEmail() : ValidateEmail{
        return ValidateEmail()
    }
    @Provides
    fun provideValidateName() : ValidateName{
        return ValidateName()
    }
    @Provides
    fun provideValidatePhone() : ValidatePhone{
        return ValidatePhone()
    }
    @Provides
    fun provideValidateWebsite() : ValidateWebsite{
        return ValidateWebsite()
    }
    @Provides
    fun provideValidatePAN() : ValidatePANNumber{
        return ValidatePANNumber()
    }
    @Provides
    fun provideValidateGSTIN() : ValidateGSTIN{
        return ValidateGSTIN()
    }
    @Provides
    fun provideValidatePIN() : ValidatePIN{
        return ValidatePIN()
    }
    @Provides
    fun provideValidateNumber() : ValidateNumber{
        return ValidateNumber()
    }

}