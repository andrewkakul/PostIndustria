package com.example.postindustriaandroid

import com.example.postindustriaandroid.data.service.NetworkManager
import com.example.postindustriaandroid.data.service.PhotoRepository
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.Before
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.Buffer
import org.junit.After
import org.junit.Assert.*
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection

class ApiSearchTest {

    private var mockWebServer = MockWebServer()
    private lateinit var service: PhotoRepository

    @Before
    fun setup(){
        mockWebServer.start()

        val gson = GsonBuilder()
            .setLenient()
            .create()
        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        service = retrofit.create(PhotoRepository::class.java)
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
     fun getRequestTest(): Unit = runBlocking{
        val reader = this@ApiSearchTest.javaClass.classLoader?.getResourceAsStream("success_response.json")
        val context = Buffer().readFrom(reader!!)
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(context)
        mockWebServer.enqueue(response)
        launch{
            val call = service.getPhoto(NetworkManager.createData("cat"))
            assertNotNull(call)
            val result_test_request = call.awaitResponse().body()!!.photos.photo[0].title
            assertEquals(result_test_request, "test_title_0")
        }
    }
}