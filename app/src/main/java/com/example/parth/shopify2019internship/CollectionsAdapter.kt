package com.example.parth.shopify2019internship

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_row.view.*


class CollectionsAdapter(private val collectionList: DisplayModel.CollectionList): RecyclerView.Adapter<CustomViewHolder>() {

    // inflating cells for the recycler view
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cell = layoutInflater.inflate(R.layout.list_row, parent,false)
        return CustomViewHolder(cell)
    }

    //binding the retrieved attributes to the right vals
    override fun onBindViewHolder(viewHolder: CustomViewHolder, index: Int) {
        val collectionAtIndex = collectionList.custom_collections[index]
        val collectionTitle = collectionAtIndex.title
        val collectionUrl = collectionList.custom_collections[index].image.src
        val thumbnailCollentions = viewHolder.view.imageView_collections

        viewHolder.view.textView_collection_title.text = collectionTitle
        //this gets passed in the constructor of 'CustomViewHolder' Class
        viewHolder.collection = collectionAtIndex
        Picasso.get().load(collectionUrl).into(thumbnailCollentions)

    }

    override fun getItemCount(): Int {
        return collectionList.custom_collections.count()
    }
}

class CustomViewHolder(
    val view : View, var collection: DisplayModel.Collection? = null): RecyclerView.ViewHolder(view){

    companion object {
        private val TAG = "CustomViewHolder"
        const val collection_title = "collection_title"
        const val collection_id = "collection_id"
    }
    init {
        view.setOnClickListener {
            val intent = Intent(view.context, CollectionsDetailActivity::class.java)
            intent.putExtra(collection_title, collection?.title)
            intent.putExtra(collection_id, collection?.id)
            view.context.startActivity(intent)
        }
    }
}