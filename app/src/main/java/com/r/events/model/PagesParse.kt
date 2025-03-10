package com.r.events.model

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.io.IOException
import java.lang.Exception
import java.net.HttpRetryException
import kotlin.concurrent.thread
var list : ArrayList<EventObject> = arrayListOf()
var filters : Filters = Filters("all", arrayListOf(arrayListOf()), "all","all" )

class PagesParse() {

    //это сделать глобальным потом
    var Utils  = Utils()
    var CheckWithFilters = CheckWithFilters()

    suspend fun getDataFromPage(){
        it_events()
        dexigner()
    }
    suspend fun it_events()
    {
       val job =  GlobalScope.launch {
            val doc: Document
            try {
                doc = Jsoup.connect("https://it-events.com/").get()
                val div: Elements = doc.getElementsByClass("section")[0].getElementsByClass("event-list-item")

                //пробегаемся по всем записям
                for(eventer in  0 until  div.size) {
                    try {
                        val SomeElement = div[eventer]

                        //создаем объект мероприятия
                        val eventObject = EventObject()
                        //заполняем его


                        //тип мероприятия
                        eventObject.setType(SomeElement.getElementsByClass("event-list-item__type").text())

                        val Element: Element = SomeElement.getElementsByClass("event-list-item__title")[0]
                        val href = Element.attr("href")
                        //имя мероприятия
                        eventObject.setName(Element.text())
                        //ссылка на страницу с мероприятияем
                        eventObject.setHref(href)

                        val dateStr = SomeElement.getElementsByClass("event-list-item__info")[0].text()
                        val photoh = SomeElement.getElementsByClass("event-list-item__image")[0]
                        val photoH = photoh.attr("style")
                        val idx1 = 22
                        val idx2 = photoH.indexOf('.') + 4

                        //ссылка на фото мероприятия
                        eventObject.setPhotoHref("https://it-events.com${photoH.substring(idx1, idx2)}")

                        //местоположения мероприятия
                        try {
                            eventObject.setLocation(SomeElement.getElementsByClass("event-list-item__info_location").text())
                        }catch (e : Exception){}

                        //онлайн траснляция или нет
                        try {
                            val check = SomeElement.getElementsByClass("event-list-item__info_online").text()
                            if( check != "")
                                eventObject.setOnline(true)
                            else
                                eventObject.setOnline(false)
                        }catch (e : Exception){ }

                        val arr = dateStr.split(' ')

                        //форматируем дату
                        if (dateStr.contains('-')) {
                            val dayMin = arr[0].toInt()
                            val month = Utils.convertMonth(arr[3])
                            val year = arr[4].toInt()

                            val str = arrayListOf(arrayListOf(dayMin, month, year))
                            eventObject.setDate(str)
                            if( CheckWithFilters.check(eventObject))
                            {
                                eventObject.setType("0")
                                list.add(eventObject)
                            }
                        }
                        else {
                            val days = arr[0].toInt()
                            val month = Utils.convertMonth(arr[1])
                            val year = arr[2].toInt()

                            val str = arrayListOf(arrayListOf(days, month, year))
                            eventObject.setDate(str)

                            //ToDo тут фильтр будет
                            list.add(eventObject)
                        }
                    } catch (e: Exception) { }
                }
            } catch (e: IOException) { }
        }
        job.join()
    }
    suspend fun dexigner()
    {

        var opa = GlobalScope.launch {
            val doc: Document
            try {
                doc = Jsoup.connect("https://www.dexigner.com/design-events").get()
                val elemnt : Element = doc.getElementById("agenda")
                val elements : Elements = elemnt.getElementsByClass("event")

                for(SomeElement in 0 until elements.size)
                {
                    val eventObject = EventObject()
                    try{
                        eventObject.setDescription(elements[SomeElement].getElementsByTag("p")[0].text())
                        eventObject.setName(elements[SomeElement].getElementsByTag("h3")[0].text())
                        eventObject.setPhotoHref( "https://www.dexigner.com${ elements[SomeElement].getElementsByTag("img")[0].attr("data-src")}")
                        eventObject.setHref("https://www.dexigner.com${elements[SomeElement].getElementsByTag("a").attr("href")}")
                        eventObject.setLocation( elements[SomeElement].getElementsByClass("location").text())
                        eventObject.setType("1")
                        val date = elements[SomeElement].getElementsByTag("time").text()

                        //Nov 20 - Nov 22, 2019
                        //Nov 22, 2019 (in 14 days)

                        //ToDo добавить двойную дату
                        if(date.contains("ends"))
                        {
                            //ends Dec 14, 2019 (1 month left)
                            val arr = date.split(' ')
                            var month = Utils.convertMonth(arr[1])
                            val day = arr[2].dropLast(1).toInt()
                            var year = arr[3].toInt()
                            val FirstArr = arrayListOf(day, month, year)
                            var day2 = arr[4].drop(1).toInt() + day
                            var month2 = 0
                            if( arr[5].contains("month"))
                            {
                                month2 = month + day2
                                if( month2 > 12)
                                {
                                    month2 -=12
                                    year++
                                }

                                eventObject.setDate(arrayListOf(FirstArr, arrayListOf(day, month2, year)))
                            }
                            else if( arr[5].contains("days"))
                            {
                                if( day2 > 29)
                                {
                                    month++
                                    day2 -= 29
                                    if( month > 12)
                                    {
                                        month -= 12
                                        year++
                                    }
                                }
                                eventObject.setDate(arrayListOf(FirstArr, arrayListOf(day2, month, year)))
                            }

                        }
                        else if(date.contains("-"))
                        {
                            //Nov 20 - Nov 22, 2019
                            val arr = date.split(",")
                            val year = arr[1].drop(1).toInt()
                            val dat = arr[0].split(" - ")
                            val fDate = dat[0].split(' ')
                            val sDate = dat[1].split(' ')
                            val fDay = fDate[1].toInt()
                            val sDay = sDate[1].toInt()
                            val fMonth = Utils.convertMonth(fDate[0])
                            val sMonth = Utils.convertMonth(sDate[0])
                            eventObject.setDate(arrayListOf(arrayListOf(fDay, fMonth, year), arrayListOf(sDay, sMonth, year)))
                        }
                        else
                        {
                            //Dec 14, 2019
                            val arr = date.split(",")
                            val year = arr[1].drop(1).toInt()
                            val dat = arr[0].split(' ')
                            val month = Utils.convertMonth(dat[0])
                            val day = dat[1].toInt()
                            eventObject.setDate(arrayListOf(arrayListOf(day, month, year)))
                        }


                        list.add(eventObject)
                    }catch (e : Exception){}
                }
                }catch (e : Exception) {}
        }
        opa.join()
    }
    fun getHash(eventObject: EventObject) : Long
    {
        val const = 49
        val name = eventObject.getName()
        val photo = eventObject.getPhotoHref()

        return name.length * const + photo[photo.length-1].toLong() * const
    }

}