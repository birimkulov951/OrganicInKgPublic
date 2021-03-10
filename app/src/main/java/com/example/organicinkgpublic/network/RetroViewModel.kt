package com.example.organicinkgpublic.network

import androidx.lifecycle.ViewModel


class RetroViewModel : ViewModel() {

   /* var recyclerListData: MutableLiveData<MutableList<NewsListItem>> = MutableLiveData()

    fun getRecyclerListDataObserver(): MutableLiveData<MutableList<NewsListItem>> {
        return recyclerListData
    }

    fun makeApiCallForNewsList() {
        val retroInstance = RetrofitInstance.getRetrofitInstance().create(ApiService::class.java)
        val call = retroInstance.getNewsList()

        call.enqueue(object : Callback<MutableList<NewsListItem>>{
            override fun onResponse(call: Call<MutableList<NewsListItem>>, response: Response<MutableList<NewsListItem>>) {
                if(response.isSuccessful) {
                    //recyclerViewAdapter.setListData(response.body()?.items!!)
                    //recyclerViewAdapter.notifyDataSetChanged()
                    recyclerListData.postValue(response.body())
                } else {
                    recyclerListData.postValue(null)
                }
            }

            override fun onFailure(call: Call<MutableList<NewsListItem>>, t: Throwable) {
                // Toast.makeText(this@RecyclerViewActivity, "Error in getting data from api.", Toast.LENGTH_LONG).show()

                recyclerListData.postValue(null)
            }
        })
    }*/
}