package com.example.parth.shopify2019internship

/*
The name of the product
The total available inventory across all variants of the product
The collection title
The collection image

 */

class DisplayModel {
    //Collections to be displayed as:
    class CollectionList(val custom_collections: List<Collection>)

    class Collection(val id: String, val title: String, val image: Image)

    class Collects(val collects: List<Collect>)

    class Collect(val product_id: String, val title: String)

    //-- Models for products --
    class Products(val products: ArrayList<Product>)

    class Product (val title: String, val variants: ArrayList<Variant>, val image: Image)

    class Variant (val title: String)

    class Image (val src: String)
}