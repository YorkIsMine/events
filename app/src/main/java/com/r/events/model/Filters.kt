package com.r.events.model

class Filters ( private var name : String?= null,
                private var date : ArrayList<ArrayList<Int>>?= null,
                private var type : String?= null,
                private var location : String? = null,
                private var online : Boolean?  = null) {

    fun setName(name : String)
    {
        this.name = name
    }
    //day, month, year
    fun setDate( date : ArrayList<ArrayList<Int>>)
    {
        this.date = date
    }
    fun setType( type : String)
    {
        this.type = type
    }
    fun setLocation ( location: String)
    {
        this.location = location
    }
    fun setOnline ( online : Boolean)
    {
        this.online = online
    }
    fun getName() : String
    {
        return this.name!!;
    }
    fun getType() : String
    {
        return this.type!!;
    }
    fun getDate() : ArrayList<ArrayList<Int>>
    {
        return this.date!!;
    }

    fun getOnline() : Boolean
    {
        return this.online!!
    }
}