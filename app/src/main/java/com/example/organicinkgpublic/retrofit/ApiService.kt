package com.example.organicinkgpublic.retrofit

import com.example.organicinkgpublic.model.network.ServerResponse
import com.example.organicinkgpublic.model.network.aboutus.AboutUsResponse
import com.example.organicinkgpublic.model.network.auth.AuthenticationBody
import com.example.organicinkgpublic.model.network.auth.RefreshRequestBody
import com.example.organicinkgpublic.model.network.auth.UserEntityBody
import com.example.organicinkgpublic.model.network.cart.AddProductToBuyListResponse
import com.example.organicinkgpublic.model.network.cart.ProductItemRequestBody
import com.example.organicinkgpublic.model.network.category.CategoryResponse
import com.example.organicinkgpublic.model.network.faq.Faq
import com.example.organicinkgpublic.model.network.orders.OrderRequestBody
import com.example.organicinkgpublic.model.network.orders.closedorders.ClosedOrdersResponse
import com.example.organicinkgpublic.model.network.client.ClientRequestResponse
import com.example.organicinkgpublic.model.network.contacts.ContactsResponse
import com.example.organicinkgpublic.model.network.elsom.UrlRequestBody
import com.example.organicinkgpublic.model.network.orders.com.example.organicinkgandroid.model.network.client.Info
import com.example.organicinkgpublic.model.network.product.ProductResponse
import com.example.organicinkgpublic.model.network.orders.com.example.organicinkgandroid.model.network.product.Search
import com.example.organicinkgpublic.model.network.raiting.RatingCreate
import com.example.organicinkgpublic.model.network.orders.com.example.organicinkgandroid.model.network.raiting.RatingResponse
import com.example.organicinkgpublic.model.network.orders.com.example.organicinkgandroid.model.network.raiting.feedbacks.FeedbackCreate
import com.example.organicinkgpublic.model.network.orders.com.example.organicinkgandroid.model.network.raiting.feedbacks.FeedbackResponse
import com.example.organicinkgpublic.model.network.orders.createorder.OrderResponse2
import com.example.organicinkgpublic.model.network.orders.elsom.GetUrlResponse
import com.example.organicinkgpublic.model.network.ordersettings.OrderSettingsResponse
import com.example.organicinkgpublic.model.network.product.ProductsResponse
import com.example.organicinkgpublic.model.network.refreshtoken.RefreshTokenResponse
import com.example.organicinkgpublic.model.network.supplier.PlaceOfProductionResponse
import com.example.organicinkgpublic.model.network.test.DeletedServerResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    /** AUTHORIZATION */
    @POST("/api/v1/register")
    fun register(@Body userEntityBody: UserEntityBody): Call<ServerResponse>

    @POST("/api/v1/verify/{code}")
    fun verifySMSCode(@Path("code") code: String): Call<ServerResponse>

    @POST("/api/v1/auth")
    fun login(@Body authenticationRequest: AuthenticationBody): Call<ServerResponse>

    @POST("/api/v1/refresh")
    fun refreshTokens(@Body refreshRequest: RefreshRequestBody): Call<RefreshTokenResponse>


    /** BASKET */

    @POST("/api/v1/productItems/arr")
    fun addProductToBuyingList(@Header("Authorization") token: String,@Body productItemRequest: ArrayList<ProductItemRequestBody>): Call<AddProductToBuyListResponse>

    @POST("/api/v1/productItems")
    fun addProductToBuying(@Header("Authorization") token: String,@Body productItemRequest: ProductItemRequestBody): Call<AddProductToBuyListResponse>

    /** ORDERS */

    @POST("/api/v1/orders")
    fun createOrder(@Header("Authorization") token: String,@Body orderRequest: OrderRequestBody): Call<OrderResponse2>

    /*@GET("/api/v1/orders")
    fun getOrderList(@Header("Authorization") token: String): Call<OrderResponse>*/

    @GET("/api/v1/orders/closedOrders")
    fun getUserClosedOrders(@Header("Authorization") token: String): Call<ClosedOrdersResponse>

    @GET("/api/v1/orders/pendingOrders")
    fun getUserPendingOrders(@Header("Authorization") token: String): Call<ClosedOrdersResponse>


    /**    GET   */
    @GET("/api/v1/products")
    fun getProductList(@Query("page") page: Int, @Query("size") size: Int): Call<ProductsResponse>

    @GET("/api/v1/products/like")
    fun getProductsByName(@Query("prodName")productName: String) : Call<Search>

    @GET("/api/v1/products/{id}")
    fun getProductById(@Path("id") id: Int) : Call<ProductResponse>

    @GET("/api/v1/faq/")
    fun getFaqList(): Call<List<Faq>>

    @GET("/api/v1/faq/{id}")
    fun getFaqById(@Path("id") id: Int) : Call<Faq>

    @GET("/api/v1/contacts")
    fun getContactsInfo(): Call<ContactsResponse>

    @GET("/api/v1/about/")
    fun getAboutUsInfo(): Call <ArrayList<AboutUsResponse>>

    @GET("/api/v1/client/{id}")
    fun getClientById(@Path("id") id: Int): Call<ClientRequestResponse>

    @GET("/api/v1/products/filter")
    fun getProductsByFilter(@Query("category") categoryName: Any?,
                            @Query("placeOfProduction") place: Any?,
                            @Query("priceFrom" ) priceFrom: Int?,
                            @Query("priceTo") priceTo: Any?,
                            @Query("size") size: Int,
                            @Query("sortBy") sortBy: Any?,
                            @Query("sortDirection") direction: Any?) : Call<ProductsResponse>

    @GET("/api/v1/products/filter")
    fun getProductsBySubCategory(@Query("category") categoryName: Any?) : Call<ProductsResponse>

    @GET("/api/v1/placeOfProduction")
    fun getPlaceOfProductionList(): Call<PlaceOfProductionResponse>

    @PUT("/api/v1/client/{id}")
    fun updateClientInfo(@Header("Authorization") token: String,
                         @Path("id") id: Int,
                         @Body info: Info
    ) : Call<ClientRequestResponse>

    @GET("/api/v1/orderSetting")
    fun getOrderSettingList(): Call<OrderSettingsResponse>

    /** Rating */

    @POST("/api/v1/raitings")
    fun putRating(@Body rating : RatingCreate) : Call<RatingResponse>

    @POST("/api/v1/comment")
    fun leaveFeedback(@Header("Authorization")token: String,
        @Body feedback: FeedbackCreate): Call<FeedbackResponse>

//    @GET("/api/v1/categories")
//    fun getCategoryList(): Call<CategoryResponseTest>


    @GET("/api/v1/categories")
    fun getCategoryList(@Query("size") size : Int): Call<CategoryResponse>

    @POST("/api/v1/categories")
    fun addCategory(@Query("name ") name: String): Call<CategoryResponse>


    /** DELETE */
    @DELETE("/api/v1/productItems/{id}")
    fun deleteProductFromBuyingList(@Header("Authorization") token: String,@Path("id") id: Int): Call<DeletedServerResponse>

    /** ELSOM */
    @POST("/api/v1/elsom/payment")
    fun getURLForPayment(@Body urlRequestBody: UrlRequestBody): Call<GetUrlResponse>
}
