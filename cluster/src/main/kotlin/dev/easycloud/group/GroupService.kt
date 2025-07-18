package dev.easycloud.group

import dev.easycloud.configuration

class GroupService {
    val groups: ArrayList<Group> = ArrayList()

    fun initialize() {
        configuration.groups.forEach { groupYaml ->
            val group = Group(
                groupYaml.name,
                groupYaml.platform,
                groupYaml.properties.toMutableMap()
            )
            groups.add(group)
        }
    }

}