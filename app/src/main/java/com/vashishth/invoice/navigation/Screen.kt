package com.vashishth.invoice.navigation

sealed class Screen(val route : String){

    object homeScreen : Screen("home_screen")
    object customerListScreen : Screen("customer_list_screen")
    object customerDetailScreen : Screen("customer_detail_screen")
    object itemListScreen : Screen("item_list_screen")
    object itemDetailScreen : Screen("item_detail_screen")
    object salesScreen : Screen("sales_screen")
    object reportsScreen : Screen("report_screen")
    object settingsScreen : Screen("setting_screen")
    object invoicesScreen : Screen("invoices_screen")
    object addCustomerScreen : Screen("add_customer_screen")
    object addBusinessNameScreen : Screen("add_businessname_screen")
    object addBusinessDetailsScreen : Screen("add_businessdetails_screen")
    object updateBusinessDetailsScreen : Screen("update_businessdetails_screen")
    object addItemScreen : Screen("add_item_screen")
    object GenerateInvoiceScreen : Screen("generate_invoice_screen")
    object AddInvoiceItemScreen : Screen("add_invoiceitem_screen")

}
