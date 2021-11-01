package com.billbook.lib.router

import com.billbook.lib.router.annotation.Service
import com.billbook.lib.router.annotation.Services
import javax.inject.Singleton

/**
 * @author xluotong@gmail.com
 */
interface LocationService {
    fun getLocation(): Pair<Long, Long>
}

@Service(LocationService::class, name = "location")
class AMapLocationService : LocationService {
    override fun getLocation(): Pair<Long, Long> {
        TODO("Not yet implemented")
    }
}

interface JsonService {
    fun toJson(): String
}

@Singleton
@Services([JsonService::class, LocationService::class])
class CommonService : JsonService, LocationService {
    override fun getLocation(): Pair<Long, Long> {
        TODO("Not yet implemented")
    }

    override fun toJson(): String {
        TODO("Not yet implemented")
    }
}

@Services(
    services = [Service(JsonService::class, name = "Json2"), Service(
        LocationService::class,
        name = "location2"
    )]
)
class CommonService2 : JsonService, LocationService {
    override fun getLocation(): Pair<Long, Long> {
        TODO("Not yet implemented")
    }

    override fun toJson(): String {
        TODO("Not yet implemented")
    }
}