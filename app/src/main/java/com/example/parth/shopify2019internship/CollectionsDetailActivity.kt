package com.example.parth.shopify2019internship

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_collections_detail.*
import okhttp3.*
import java.io.IOException

/*
Collection Details page: A list of every product for a specific collection. Each row in the list needs to contain, at a minimum:
The name of the product
The total available inventory across all variants of the product
The collection title
The collection image

 */

class CollectionsDetailActivity : AppCompatActivity() {
    companion object {
        private val TAG = "CollectionsDetailActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collections_detail)

        //from recyclerview tutorial
        recyclerView_details.layoutManager = LinearLayoutManager(this@CollectionsDetailActivity)
        recyclerView_details.addItemDecoration(
            DividerItemDecoration(recyclerView_details.context, DividerItemDecoration.VERTICAL))

        // Intent from parent activity passes values to new activity
        val collectionTitle = intent.getStringExtra(CustomViewHolder.collection_title)
        val collectionId = intent.getStringExtra(CustomViewHolder.collection_id)

        supportActionBar?.title = collectionTitle

        fetchJSON(collectionId, collectionTitle)

    }

    private fun fetchJSON(collectionId: String, collectionTitle: String){
        Log.d(CollectionsDetailActivity.TAG, "Attempting to fetch JSON")
        val url = "https://shopicruit.myshopify.com/admin/collects.json?collection_id=$collectionId&page=1&access_token=c32313df0d0ef512ca64d5b336a0d7c6"

        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()
        client.newCall(request).enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body()?.string()
                val gson = GsonBuilder().create()
                val collects = gson.fromJson(body, DisplayModel.Collects::class.java)

                val productIdArray = ArrayList<String>()
                for (collect in collects.collects){
                    productIdArray.add(collect.product_id)
                }
                fetchAllProducts(productIdArray, collects, collectionTitle)
            }

            override fun onFailure(call: Call, e: IOException) {
                Log.d(CollectionsDetailActivity.TAG, "Failed request")
            }
        })
    }

    private fun fetchAllProducts(productIdArray: ArrayList<String>, collects: DisplayModel.Collects, collectionTitle: String){
        var stringProductIds = ""
        for (collect in productIdArray){
            stringProductIds += "$collect,"
        }
        if (stringProductIds.endsWith(","))
        {
            stringProductIds = stringProductIds.substring(0, stringProductIds.length - 1)
        }

        val url = "https://shopicruit.myshopify.com/admin/products.json?ids=$stringProductIds&page=1&access_token=c32313df0d0ef512ca64d5b336a0d7c6"
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body()?.string()
                val gson = GsonBuilder().create()
                val products = gson.fromJson(body, DisplayModel.Products::class.java)

                runOnUiThread {
                    recyclerView_details.adapter = DetailsAdapter(collects, products, collectionTitle)
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                Log.d(CollectionsDetailActivity.TAG, "Failed request for all products")
            }
        })
    }
}