package xin.neoto.redspread

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bridgefy.sdk.client.*
import com.bridgefy.sdk.client.MessageListener
import com.bridgefy.sdk.client.StateListener
import com.bridgefy.sdk.framework.exceptions.MessageException
import com.google.gson.Gson

import xin.neoto.redspread.dummy.DummyContent
import kotlinx.android.synthetic.main.activity_item_list.*
import kotlinx.android.synthetic.main.item_list_content.view.*
import kotlinx.android.synthetic.main.item_list.*
import kotlinx.android.synthetic.main.item_list_content.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


/**
 * An activity representing a list of Pings. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [ItemDetailActivity] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
private const val CREATE_POST_TAG = "CREATE"
private const val CREATE_POST_CODE = 3
private const val DEFAULT_TTD = 5


class ItemListActivity : AppCompatActivity() {

    var messageList = ArrayList<Message>()
    var numDevices = 0


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == CREATE_POST_CODE) {
            val post = data?.getStringExtra("POST")

            if (post != null) {
                val data = HashMap<String, kotlin.Any>()
                data["id"] = UUID.randomUUID()
//                Log.d("KYLE1", data["id"].toString())
                data["title"] = "title"
                data["content"] = post
                data["ttd"] = DEFAULT_TTD

                val message =
                    Message.Builder().setContent(data).build()

                messageList.add(0,message)
                item_list.adapter?.notifyDataSetChanged()

                Bridgefy.sendBroadcastMessage(message)
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_list)
        setSupportActionBar(toolbar)
        toolbar.title = title

        fab.setOnClickListener { view ->
            val intent = Intent(this, CreatePostActivity::class.java)
            startActivityForResult(intent, CREATE_POST_CODE)
        }

        setupRecyclerView(item_list)


        BridgefyUtils.enableBluetooth(applicationContext)
        val b1 = BridgefyUtils.checkBluetoothPermission(applicationContext)
        val b2 = BridgefyUtils.checkLocationPermissions(applicationContext)

        if (!b1 || !b2) {
            Snackbar.make(window.decorView.rootView, "Permission denied", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }


        val builder = Config.Builder()
        builder.setEnergyProfile(BFEnergyProfile.HIGH_PERFORMANCE)
        builder.setEncryption(false)
        Bridgefy.initialize(
            this.applicationContext,
            "f1c2dd65-085a-4fe6-8a3d-dc29aa61dbd6",
            object : RegistrationListener() {
                override fun onRegistrationSuccessful(bridgefyClient: BridgefyClient) {
                    Snackbar.make(window.decorView.rootView, "Reg successful", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()
                    // Bridgefy is ready to start
                    Bridgefy.start(
                        object : MessageListener() {
                            override fun onBroadcastMessageReceived(message: Message?) {
                                if (message != null) {
                                    messageList.add(0,message)
                                    item_list.adapter?.notifyDataSetChanged()
                                }

//                                Snackbar.make(
//                                    window.decorView.rootView,
//                                    "Message !!!",
//                                    Snackbar.LENGTH_LONG
//                                )
//                                    .setAction("Action", null).show()
                            }

                            override fun onMessageSent(messageId: String?) {
                                Snackbar.make(
                                    window.decorView.rootView,
                                    "Sent !!!",
                                    Snackbar.LENGTH_LONG
                                )
                                    .setAction("Action", null).show()
                            }

                            override fun onMessageFailed(message: Message?, e: MessageException?) {
                                super.onMessageFailed(message, e)
                            }

                            override fun onMessageReceived(message: Message?) {
                                Snackbar.make(
                                    window.decorView.rootView,
                                    "received !!!",
                                    Snackbar.LENGTH_LONG
                                )
                                    .setAction("Action", null).show()
                                Log.d("KYLE","message" )
                                if(message !=null){
//                                    Log.d("KYLE", message.content["id"].toString() )
                                    if(message.content["id"] == null){
                                        for((key,value) in message.content){
                                            Log.d("KYLE2","hi" )

                                            var contains = false
                                            for(mess in messageList){
                                                Log.d("KYLE3",mess.content["id"].toString() )
                                                Log.d("KYLE4",key.toString() )


                                                if(mess.content["id"].toString().equals(key.toString())){
                                                    //BOTH DEVICES HAVE MESSAGE
                                                    contains = true
                                                    break;
                                                }
                                            }
                                            if(!contains){
//                                                val data = HashMap<String, kotlin.Any>()
//                                                data["id"] = value["content"]["id"] as
//                                                data["title"] = "title"
//                                                data["content"] = post
//                                                data["ttd"] = DEFAULT_TTD
//                                                var test =
//
//                                                val message =
//                                                    Message.Builder().setContent(data).build()
//                                                var make = HashMap<String, kotlin.Any>()

//                                                messageList.add(0, value as Message)
                                                Log.d("KYLE7", value.toString())
                                                Log.d("KYLE7", value.get)
                                            }
                                        }
                                        item_list.adapter?.notifyDataSetChanged()
                                    }else{
                                        super.onMessageReceived(message)

                                    }
                                }



                            }
                        },
                        object : StateListener() {

                            override fun onStarted() {
                                Snackbar.make(
                                    window.decorView.rootView,
                                    "Started!!! ",
                                    Snackbar.LENGTH_LONG
                                )
                                    .setAction("Action", null).show()
                            }

                            override fun onStartError(message: String?, errorCode: Int) {
                                Snackbar.make(
                                    window.decorView.rootView,
                                    "Start Error!!! " + message,
                                    Snackbar.LENGTH_LONG
                                )
                                    .setAction("Action", null).show()
                            }

                            override fun onDeviceLost(device: Device?) {
                                super.onDeviceLost(device)
                                numDevices--

                            }

                            override fun onDeviceConnected(device: Device?, session: Session?) {
                                numDevices++
                                val data = HashMap<String,kotlin.Any>()
                                for(m:Message in messageList){
                                    data.put(m.content["id"].toString(), m)
                                }
                                val message =
                                    Message.Builder().setContent(data).setReceiverId(device?.userId).build()
                                Bridgefy.sendMessage(message)


//                                Snackbar.make(
//                                    window.decorView.rootView,
//                                    "Device!!!",
//                                    Snackbar.LENGTH_LONG
//                                )
//                                    .setAction("Action", null).show()
                            }
                        }, builder.build()
                    )
                }

                override fun onRegistrationFailed(errorCode: Int, message: String) {
                    Snackbar.make(
                        window.decorView.rootView,
                        "Bluetooth failed, " + message,
                        Snackbar.LENGTH_LONG
                    )
                        .setAction("Action", null).show()
                    // Something went wrong: handle error code, maybe print the message
                }
            }
        )
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.adapter = SimpleItemRecyclerViewAdapter(this, messageList)
    }

    class SimpleItemRecyclerViewAdapter(
        private val parentActivity: ItemListActivity,
        private val values: ArrayList<Message>
//        private val twoPane: Boolean
    ) :
        RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>() {

        private val onClickListener: View.OnClickListener

        init {
            onClickListener = View.OnClickListener { v ->
                val item = v.tag as Message
//                if (twoPane) {
//                    val fragment = ItemDetailFragment().apply {
//                        arguments = Bundle().apply {
//                            putString(ItemDetailFragment.ARG_ITEM_ID, item.id)
//                        }
//                    }
//                    parentActivity.supportFragmentManager
//                        .beginTransaction()
//                        .replace(R.id.item_detail_container, fragment)
//                        .commit()
//                } else {
                val intent = Intent(v.context, ItemDetailActivity::class.java).apply {
                    putExtra(ItemDetailFragment.ARG_ITEM_ID, item.uuid)
                }
                v.context.startActivity(intent)
//                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_list_content, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = values[position]
            val data = item.content
//            val title = data["title"]
            val content = data["content"]
            val ttd = data["ttd"]
//            holder.idView.text = title.toString()
            holder.contentView.text = content.toString()

            with(holder.itemView) {
                tag = item
                setOnClickListener(onClickListener)
            }
        }

        override fun getItemCount() = values.size

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//            val idView: TextView = view.id_text
            val contentView: TextView = view.content
        }
    }
}
