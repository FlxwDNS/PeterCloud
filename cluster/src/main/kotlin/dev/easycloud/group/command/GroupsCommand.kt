package dev.easycloud.group.command

import dev.easycloud.group.GroupService
import dev.easycloud.logger
import dev.easycloud.terminal.command.Command

class GroupsCommand(val groupService: GroupService): Command("groups") {

    init {
        setExecutor {
            groupService.groups.forEach { group ->
                logger.info("Group: ${group.name}, Platform: ${group.platform}, Properties: ${group.properties}")
            }
        }
    }
}