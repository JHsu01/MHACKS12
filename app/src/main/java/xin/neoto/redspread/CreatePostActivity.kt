package xin.neoto.redspread

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_create_post.*

class CreatePostActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)

        postButton.setOnClickListener{
            val intent = Intent()
            intent.putExtra("POST", postText.text.toString())
//            intent.data = Uri.parse(postText.toString())
            setResult(Activity.RESULT_OK, intent)
            finish()

        }
    }
}
