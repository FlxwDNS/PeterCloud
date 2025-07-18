package dev.easycloud.group

import kotlinx.serialization.Serializable

@Serializable
class Group(
    var name: String,
    var platform: String,
    var properties: MutableMap<String, String>
) {
    fun addProperty(key: String, value: Any) {
        properties[key] = value.toString()
    }

    fun removeProperty(key: String) {
        properties.remove(name)
    }

    fun <T> getProperty(key: String, clazz: Class<T>): T {
        return properties[key]?.let { clazz.cast(it) } as T
    }

    override fun toString(): String {
        return "Group(name='$name', platform='$platform', properties=$properties)"
    }
}
