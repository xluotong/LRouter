package com.billbook.lib.lrouter

import com.billbook.lib.router.CacheIn
import com.billbook.lib.router.ServiceInfo
import com.billbook.lib.router.internel.DefaultServiceCentral
import org.junit.Test

/**
 * @author xluotong@gmail.com
 */
class ServiceTest {

    @Test
    fun testGetServices() {
        val service = DefaultServiceCentral()
        service.register(
            ServiceInfo(
                LocationService::class.java,
                LocationServiceImpl::class.java,
                "locationService1",
                "",
                CacheIn.UNDEFINED
            )
        )
        service.register(
            ServiceInfo(
                LocationService::class.java,
                LocationServiceImpl2::class.java,
                "locationService2",
                "",
                CacheIn.UNDEFINED
            )
        )
//        assert(service.getService(LocationService::class.java) != null)
        assert(service.getService(LocationService::class.java, 122)!!.getLocation() == "122")
        println(service.getService(LocationService::class.java, 122)!!.getLocation())
        assert(
            service.getService(LocationService::class.java, "locationService2")!!
                .getLocation() == "location2"
        )
    }
}

interface LocationService {

    fun getLocation(): String
}

class LocationServiceImpl2 : LocationService {
    override fun getLocation(): String {
        return "location2"
    }
}

class LocationServiceImpl : LocationService {

    private var str: Int = 0

    constructor(str: Int) {
        this.str = str
    }

    override fun getLocation(): String {
        return str.toString()
    }
}