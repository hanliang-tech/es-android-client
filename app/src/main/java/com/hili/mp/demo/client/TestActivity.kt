package com.hili.mp.demo.client

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.ScrollView
import android.widget.TextView
import com.hili.sdk.mp.client.IClientControlPoint
import com.hili.sdk.mp.client.MiniClientManager
import com.hili.sdk.mp.client.MiniRemoteDevice
import com.hili.sdk.mp.common.model.MiniAction
import java.lang.Exception
import java.text.SimpleDateFormat


/**
 * Create by WeiPeng on 2021/04/13 19:32
 */
class TestActivity : Activity() {

    private lateinit var component: RefreshDeviceComponent
    private var lastCheckDevice: MiniRemoteDevice? = null

    companion object {
        private const val ES_ID = "113d864d38bc460dfc091b66d509782e"
        private const val ES_PKG = "es.com.hili.mp.demo.client"
        private val FORMAT = SimpleDateFormat("HH:mm:ss:SSS")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.setContentView(R.layout.activity_test)

        component = findViewById(R.id.component)
        component.setOnRefreshListener { startSearch(null) }
        component.setOnItemClickListener {
            lastCheckDevice = it

            MiniClientManager.get().controlPoint.listen(it, object :IClientControlPoint.OnDeviceEventCallback{
                override fun onReceiveEvent(action: MiniAction?) {
                    action?.let {
                        appendMessage("收到消息：${action.name}\n参数：${action.params}")
                    }
                }

                override fun onSuccess() {
                    runOnUiThread {
                        findViewById<View>(R.id.ev_container).visibility = View.VISIBLE
                        findViewById<TextView>(R.id.tv_device_name).text = it?.deviceName
                    }
                    appendMessage("监听成功")
                }

                override fun onError(msg: String?) {
                    appendMessage("监听失败：$msg")
                }
            })
        }
    }

    fun startSearch(v: View?) {
        appendMessage("开始搜索")
        component.startRefreshing()
        MiniClientManager.get().controlPoint.search(8, object : IClientControlPoint.OnDeviceUpdateCallback{
        override fun onDeviceUpdate(devices: MutableList<MiniRemoteDevice>?) {
            runOnUiThread { component.setData(devices) }
        }

        override fun onSearchEnd(e: Exception?) {
            runOnUiThread { component.endRefreshing() }
            appendMessage("搜索完成")
        }
    })}

    fun stopSearch(view: View) { MiniClientManager.get().controlPoint.stopSearch(); appendMessage("停止搜索")}

    private val sendCallback = object: IClientControlPoint.OnSendEventCallback{
        override fun onSuccess() {
            appendMessage("发送成功")
        }

        override fun onError(msg: String?) {
            appendMessage("发送失败：$msg")
        }
    }

    fun startEsApp(view: View) {
        MiniClientManager.get().controlPoint.sendEvent(lastCheckDevice,
            MiniAction.mkEsAppAction()
                .esId(ES_ID)
                .esPackage(ES_PKG)
                .params("{\"token\":\"4073571a49b56914234dd4f79fac51eb\"}")
                .build(), sendCallback)
    }

    fun startNativeApp(view: View) {
        val cmd = "adb shell am start -a com.tencent.qqlivetv.open -d tenvideo2:\\/\\/?action\u003d7\u0026cover_id\u003drjae621myqca41h\u0026stay_flag\u003d0\u0026episode_idx\u003d0\u0026cover_pulltype\u003d1\u0026pullfrom\u003d1024051"
        MiniClientManager.get().controlPoint.sendEvent(lastCheckDevice,
            MiniAction.mkNativeAppAction()
                .params(cmd)
                .build(), sendCallback)
    }

    fun sendEvent(view: View) {
        MiniClientManager.get().controlPoint.sendEvent(lastCheckDevice,
        MiniAction.mkCustomAction("demo_public_event1")
            .params("{\"dddd\":123}")
            .esPackage(ES_PKG)
            .build(), sendCallback)
    }

    @SuppressLint("SetTextI18n")
    private fun appendMessage(msg:String){
        runOnUiThread {
            findViewById<TextView>(R.id.tv_infos)?.let {
                it.text = "${it.text}-------------------------\n${FORMAT.format(System.currentTimeMillis())}\n$msg\n"
                (it.parent as ScrollView).run { post { fullScroll(View.FOCUS_DOWN) } }
            }
        }
    }

    fun destroy(view: View) {
        MiniClientManager.get().destroy()
    }

}