package xin.neoto.redspread

import java.util.*
import kotlin.collections.HashMap

class Post {
    var title: String = ""
    var content: String = ""
    var ttl = 5
    var uuid: UUID = UUID.randomUUID()

    fun toHashmap() : HashMap<String, Any> {
        val map = HashMap<String, Any>()
        map["title"] = title
        map["content"] = content
        map["uuid"] = uuid.toString()
        map["ttl"] = ttl
        return map
    }

    constructor(title: String, content: String) {
        this.title = title
        this.content = content
        this.uuid = UUID.randomUUID()
        this.ttl = 5
    }
    constructor(map: HashMap<String, Any>) {
        this.title = map["title"] as String
        this.content = map["content"] as String
        this.uuid = UUID.fromString(map["uuid"] as String)
        this.ttl = map["ttl"] as Int

    }


}