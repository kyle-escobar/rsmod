package gg.rsmod.game.drops

import gg.rsmod.game.drops.model.DropTable
import gg.rsmod.game.drops.model.NPCDropChance
import gg.rsmod.game.drops.model.NPCDropItem
import gg.rsmod.game.drops.model.NPCDrops
import gg.rsmod.game.model.item.Item
import java.io.File
import java.nio.ByteBuffer
import java.nio.file.Files
import kotlin.experimental.and

object DropTableDecoder {
    fun decodeFile(path: String): DropTable {
        val dropTable = DropTable( mutableListOf())

        val file = File("game/plugins/src/main/kotlin/gg/rsmod/plugins/content" + path)

        if(!file.exists()) {
            println("[ERROR] Drop table file not found at $path.")
        }


        var fileData: ByteArray = Files.readAllBytes(file.toPath())
        var buffer: ByteBuffer = ByteBuffer.wrap(fileData)

        val dropSize = buffer.getShort()

        for(i in (0..(dropSize-1))) {
            val npcId = (buffer.getShort() and '\uffff'.toShort()).toInt()
            val drops: Array<NPCDropItem?> = arrayOfNulls(((buffer.getShort() and '\uffff'.toShort()).toInt()))

            for(j in (0..(drops.size-1))) {
                if(buffer.get() == 0.toByte()) {
                    val itemId = (buffer.getShort() and '\uffff'.toShort()).toInt()
                    val dropTableIndex = buffer.getInt()
                    drops[j] = NPCDropItem(itemId, buffer.getInt(), buffer.getInt(), NPCDropChance.values().get(dropTableIndex), false)
                } else {
                    drops[j] = NPCDropItem(0, 0, 0, NPCDropChance.ALWAYS, true)
                }
            }
            dropTable.getNPCDrops().add(NPCDrops(npcId = npcId, drops = drops.toMutableList()))
        }
        buffer.clear()
        return dropTable
    }
}