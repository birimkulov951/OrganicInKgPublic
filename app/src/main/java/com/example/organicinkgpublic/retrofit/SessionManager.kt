//session manager
package com.example.organicinkgpublic.retrofit

import android.content.Context
import android.content.SharedPreferences
import com.example.organicinkgpublic.R

/**
 * Session manager to save and fetch data from SharedPreferences
 */
class SessionManager (context: Context) {

    private var tokenPrefs: SharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    private var selectedCategoryPref: SharedPreferences = context.getSharedPreferences(SELECTED_CATEGORY, Context.MODE_PRIVATE)
    private var selectedLocationPref: SharedPreferences = context.getSharedPreferences(SELECTED_LOCATION, Context.MODE_PRIVATE)
    private var priceStartPref: SharedPreferences = context.getSharedPreferences(PRICE_START, Context.MODE_PRIVATE)
    private var priceEndPref: SharedPreferences = context.getSharedPreferences(PRICE_END, Context.MODE_PRIVATE)
    private var sortDirectionPref: SharedPreferences = context.getSharedPreferences(SORT_DIRECTION, Context.MODE_PRIVATE)
    private var filtrateByPref: SharedPreferences = context.getSharedPreferences(FILTRATE_BY, Context.MODE_PRIVATE)
    private var courierPricePref: SharedPreferences = context.getSharedPreferences(COURIER_PRICE, Context.MODE_PRIVATE)
    private var totalPricePref: SharedPreferences = context.getSharedPreferences(TOTAL_PRICE, Context.MODE_PRIVATE)
    private var orderNumberPref: SharedPreferences = context.getSharedPreferences(ORDER_NUMBER, Context.MODE_PRIVATE)

    private var userInfo: SharedPreferences = context.getSharedPreferences(USER_INFO, Context.MODE_PRIVATE)

    private var orderDetails: SharedPreferences = context.getSharedPreferences(DESIRED_DATE_DATA, Context.MODE_PRIVATE)
    private var orderContacts: SharedPreferences = context.getSharedPreferences(CONTACT_NAME_DATA, Context.MODE_PRIVATE)


    //private var productsToBeOrdered: SharedPreferences = context.getSharedPreferences(PRODUCT_AMOUNT_TO_BE_ORDERED, Context.MODE_PRIVATE)

    companion object {

        /** Tokens */
        const val USER_ID = "user_id"
        const val USER_ACCESS_TOKEN = "user_access_token"
        const val USER_ACCESS_TOKEN_EXPIRATION_TIME = "user_access_token_expiration_time"
        const val USER_REFRESH_TOKEN = "user_refresh_token"
        const val USER_REFRESH_TOKEN_EXPIRATION_TIME = "user_refresh_token_expiration_time"
        const val USER_PHONE_NUMBER = "user_phone_number"

        /** Helpers */
        const val SELECTED_CATEGORY = "selected_category"
        const val SELECTED_LOCATION = "selected_location"
        const val PRICE_START = "price_start"
        const val PRICE_END = "price_end"
        const val FILTRATE_BY = "filtrate_by"
        const val SORT_DIRECTION = "sort_direction"
        const val COURIER_PRICE = "courier_price"
        const val TOTAL_PRICE = "total_price"
        const val ORDER_NUMBER = "order_number"

        /** User info */
        const val USER_INFO = "user_info"
        const val USER_NAME = "user_name"
        const val USER_LAST_NAME = "user_last_name"
        const val USER_MIDDLE_NAME = "user_middle_name"
        const val USER_EMAIL = "user_email"
        const val USER_IMAGE_URI = "user_image_uri"


        /** Product Fragment Constants */
        const val PAYMENT_METHOD_DATA = "PAYMENT_METHOD_DATA"
        const val PAYMENT_METHOD_NUMBER_DATA = "PAYMENT_METHOD_NUMBER_DATA"
        const val SHIPMENT_METHOD_DATA = "SHIPMENT_METHOD_DATA"
        const val SHIPMENT_ADDRESS_DATA = "SHIPMENT_ADDRESS_DATA"
        const val DESIRED_DATE_DATA = "DESIRED_DATE_DATA"
        const val CONTACT_NAME_DATA = "CONTACT_NAME_DATA"
        const val CONTACT_SURNAME_DATA = "CONTACT_SURNAME_DATA"
        const val CONTACT_PHONE_NUMBER_DATA = "CONTACT_PHONE_NUMBER_DATA"



    }


    /**
     * Function to save auth token
     */
    fun saveAuthTokens(
        userId: Int,
        accessToken: String,
        accessTokenExpTime: String,
        refreshToken: String,
        refreshTokenExpTime: String) {
        val editor = tokenPrefs.edit()
        editor.putInt(USER_ID, userId)
        editor.putString(USER_ACCESS_TOKEN, accessToken)
        editor.putString(USER_ACCESS_TOKEN_EXPIRATION_TIME, accessTokenExpTime)
        editor.putString(USER_REFRESH_TOKEN, refreshToken)
        editor.putString(USER_REFRESH_TOKEN_EXPIRATION_TIME, refreshTokenExpTime)
        editor.apply()
    }

    /**
     * Function to fetch auth token
     */
    fun fetchUserId(): Int? {
        return tokenPrefs.getInt(USER_ID, 0)
    }

    fun fetchAccessToken(): String? {
        return tokenPrefs.getString(USER_ACCESS_TOKEN, "null")
    }

    fun fetchAccessTokenExpTime(): String? {
        return tokenPrefs.getString(USER_ACCESS_TOKEN_EXPIRATION_TIME, "null")
    }

    fun fetchRefreshToken(): String? {
        return tokenPrefs.getString(USER_REFRESH_TOKEN, "null")
    }

    fun fetchRefreshTokenExpTime(): String? {
        return tokenPrefs.getString(USER_REFRESH_TOKEN_EXPIRATION_TIME, "null")
    }

    /**
     * Function to save phoneNumber which is userName in BackEnd
     */
    fun savePhoneNumber(userPhoneNumber: String) {
        val editor = tokenPrefs.edit()
        editor.putString(USER_PHONE_NUMBER, userPhoneNumber)
        editor.apply()
    }

    fun fetchPhoneNumber(): String? {
        return tokenPrefs.getString(USER_PHONE_NUMBER, "null")
    }

    /**
     * Function to save filter settings
     */
    fun saveSelectedCategory(selectedCategory: String)  {
        val editor = selectedCategoryPref.edit()
        editor.putString(SELECTED_CATEGORY, selectedCategory)
        editor.apply()
    }

    fun saveSelectedLocation(selectedLocation: String) {
        val editor = selectedLocationPref.edit()
        editor.putString(SELECTED_LOCATION, selectedLocation)
        editor.apply()
    }

    fun savePriceStart(priceStart: Int) {
        val editor = priceStartPref.edit()
        editor.putInt(PRICE_START, priceStart)
        editor.apply()
    }

    fun savePriceEnd(priceEnd: Int) {
        val editor = priceEndPref.edit()
        editor.putInt(PRICE_END, priceEnd)
        editor.apply()
    }

    fun saveFiltrateBy(filtrateBy: String) {
        val editor = filtrateByPref.edit()
        editor.putString(FILTRATE_BY, filtrateBy)
        editor.apply()
    }
    fun saveSortDirection(sortDirection: String){
        val editor = sortDirectionPref.edit()
        editor.putString(SORT_DIRECTION, sortDirection)
        editor.apply()
    }

    /**
     * Function to fetch filter Settings
     */
    fun fetchSelectedCategory(): String? {
        return selectedCategoryPref.getString(SELECTED_CATEGORY, "null")
    }

    fun fetchSelectedLocation(): String? {
        return selectedLocationPref.getString(SELECTED_LOCATION, "null")
    }

    fun fetchPriceStart(): Int? {
        return priceStartPref.getInt(PRICE_START, -1)
    }

    fun fetchPriceEnd(): Int? {
        return priceEndPref.getInt(PRICE_END, -1)
    }

    fun fetchFiltrateBy(): String? {
        return filtrateByPref.getString(FILTRATE_BY, "NEW")
    }

    fun fetchSortDirection(): String? {
        return sortDirectionPref.getString(SORT_DIRECTION, "null")
    }
    /**
     * Function to save phoneNumber which is userName in BackEnd
     */
    fun saveCourierPrice(courierPrice: Int) {
        val editor = courierPricePref.edit()
        editor.putInt(COURIER_PRICE, courierPrice)
        editor.apply()
    }

    fun fetchCourierPrice(): Int {
        return courierPricePref.getInt(COURIER_PRICE, -1)
    }

    /**
     * Function to save totalPrice which is userName in BackEnd
     */
    fun saveTotalPrice(totalPrice: Int) {
        val editor = totalPricePref.edit()
        editor.putInt(TOTAL_PRICE, totalPrice)
        editor.apply()
    }

    fun fetchTotalPrice(): Int {
        return totalPricePref.getInt(TOTAL_PRICE, -1)
    }

    /**
     * Function to save orderNumber which is userName in BackEnd
     */
    fun saveOrderNumber(orderNumber: String) {
        val editor = orderNumberPref.edit()
        editor.putString(ORDER_NUMBER, orderNumber)
        editor.apply()
    }

    fun fetchOrderNumber(): String {
        return orderNumberPref.getString(ORDER_NUMBER, "null")!!
    }

    /**
     * Function to save user info
     */
    fun saveUserInfo(email: String?, name: String?, lastName : String?, middleName : String?){
        val editor = userInfo.edit()
        editor.putString(USER_NAME, name)
        editor.putString(USER_LAST_NAME, lastName)
        editor.putString(USER_MIDDLE_NAME, middleName)
        editor.putString(USER_EMAIL, email)
        editor.apply()
    }
    fun deleteInfo(){
        userInfo.edit().clear().apply()
    }
    fun fetchUserName() : String{
        return userInfo.getString(USER_NAME, null).toString()
    }
    fun fetchUserLastName() : String{
        return userInfo.getString(USER_LAST_NAME, null).toString()
    }
    fun fetchUserMiddleName() : String{
        return userInfo.getString(USER_MIDDLE_NAME, null).toString()
    }
    fun fetchUserEmail() : String{
        return userInfo.getString(USER_EMAIL, null).toString()
    }
    fun saveProfileImage(imageUri: String){
        val editor = userInfo.edit()
        editor.putString(USER_IMAGE_URI, imageUri)
        editor.apply()
    }
    fun fetchImageUri(): String{
        return userInfo.getString(USER_IMAGE_URI, null).toString()
    }


    /**
     * Function to save order details
     */
    fun saveOrderDetails(paymentMethod: String, paymentMethodNumber: String, shipmentMethod: String, shipmentMethodAddress: String, desiredDate: String) {
        val editor = orderDetails.edit()
        editor.putString(PAYMENT_METHOD_DATA, paymentMethod)
        editor.putString(PAYMENT_METHOD_NUMBER_DATA, paymentMethodNumber)
        editor.putString(SHIPMENT_METHOD_DATA, shipmentMethod)
        editor.putString(SHIPMENT_ADDRESS_DATA, shipmentMethodAddress)
        editor.putString(DESIRED_DATE_DATA, desiredDate)
        editor.apply()
    }
    fun fetchPaymentMethod(): String? {
        return orderDetails.getString(PAYMENT_METHOD_DATA, "empty")
    }
    fun fetchPaymentMethodNumber(): String? {
        return orderDetails.getString(PAYMENT_METHOD_NUMBER_DATA, "empty")
    }
    fun fetchShipmentMethod(): String? {
        return orderDetails.getString(SHIPMENT_METHOD_DATA, "empty")
    }
    fun fetchShipmentMethodAddress(): String? {
        return orderDetails.getString(SHIPMENT_ADDRESS_DATA, "empty")
    }
    fun fetchDesiredDate(): String? {
        return orderDetails.getString(DESIRED_DATE_DATA, "empty")
    }
    fun saveOrderContacts(contactName: String, contactSurname: String, contactPhoneNumber: String) {
        val editor = orderContacts.edit()
        editor.putString(CONTACT_NAME_DATA, contactName)
        editor.putString(CONTACT_SURNAME_DATA, contactSurname)
        editor.putString(CONTACT_PHONE_NUMBER_DATA, contactPhoneNumber)
        editor.apply()
    }
    fun fetchContactName(): String? {
        return orderContacts.getString(CONTACT_NAME_DATA, "empty")
    }
    fun fetchContactSurname(): String? {
        return orderContacts.getString(CONTACT_SURNAME_DATA, "empty")
    }
    fun fetchContactPhoneNumber(): String? {
        return orderContacts.getString(CONTACT_PHONE_NUMBER_DATA, "empty")
    }
}
