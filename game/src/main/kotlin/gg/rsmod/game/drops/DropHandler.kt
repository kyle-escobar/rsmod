@file:Suppress("CAST_NEVER_SUCCEEDS")

package gg.rsmod.game.drops

import gg.rsmod.game.drops.model.DropTable
import gg.rsmod.game.drops.model.NPCDropChance
import gg.rsmod.game.drops.model.NPCDropItem
import gg.rsmod.game.drops.model.NPCDrops
import gg.rsmod.game.model.attr.KILLER_ATTR
import gg.rsmod.game.model.entity.GroundItem
import gg.rsmod.game.model.entity.Npc
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.model.item.Item
import java.lang.StrictMath.random
import kotlin.math.nextUp

object DropHandler {

    /**
     * Below are the rates in percent chance for each drop table category
     * Numbers must add up to 100.0 or there is a chance the person wont get a drop.
     */
    val common_chance: Double = 10.0 // 1/10
    val uncommon_chance: Double = 3.125 // 1/32
    val rare_chance: Double = 0.78125 // 1/128
    val veryrare_chance: Double = 0.2 // 1/500

    fun calculateDrop(npc: Npc, dropTable: DropTable) {
        // Find if the droptable contains a npc with the ID of [npc]

        var npcDropTable: NPCDrops? = null
        var always_drops: MutableList<NPCDropItem> = mutableListOf()

        val deathTile = npc.tile

        val attacker: Player = (npc.attr[KILLER_ATTR]?.get() as? Player)!!

        /*for(npcDrop: NPCDrops in dropTable.getNPCDrops()) {
            if(npcDrop.getNpcId() == npc.id) {
                npcDropTable = npcDrop
                break
            }
        }*/
        npcDropTable = dropTable.getNPCDrops().get(0)

        // Find always drop items

        npcDropTable!!.getDrops().forEach { dropItem: NPCDropItem? ->
            if(dropItem!!.getTableChance() == NPCDropChance.ALWAYS) {
                always_drops.add(dropItem)
            }
        }



        // Drop the items
        always_drops.forEach { dItem: NPCDropItem ->
            var rand: Int = 1
            if(dItem.getMax() != 1) {
                rand = (1 until dItem.getMax()).random()
            }
            npc.world.spawn(GroundItem(Item(dItem.getItemId(), rand), deathTile, attacker))
        }

        // Find a item frop drop tables to drop.
        val randomItemInt: Double = random().nextUp()*100
        var droppedItem: Item? = null
        var rollTries = 0

        while(droppedItem == null && rollTries <= 5) {
            if(randomItemInt <= veryrare_chance) {
                droppedItem = this.getItemFromTable(npcDropTable, NPCDropChance.VERY_RARE)
            }
            else if(randomItemInt <= rare_chance) {
                droppedItem = this.getItemFromTable(npcDropTable, NPCDropChance.RARE)
            }
            else if(randomItemInt <= uncommon_chance) {
                droppedItem = this.getItemFromTable(npcDropTable, NPCDropChance.UNCOMMON)
            }
            else {
                droppedItem = this.getItemFromTable(npcDropTable, NPCDropChance.COMMON)
            }

            rollTries++
        }

        npc.world.spawn(GroundItem(droppedItem!!, deathTile, attacker))
    }

    private fun getItemFromTable(npcDrops: NPCDrops, table: NPCDropChance): Item? {
        var items: MutableList<NPCDropItem> = mutableListOf()
        for(item: NPCDropItem? in npcDrops.getDrops()) {
            if(item!!.getTableChance() == table) {
                items.add(item)
            }
        }

        if(items.size == 0) {
            return null
        } else {
            var randItem: Int = 0
            if(items.size > 1) {
                randItem = (0 until items.size-1).random()
            }

            val selectedItem: NPCDropItem = items.get(randItem)

            var randQuanity: Int = selectedItem.getMin()
            if(selectedItem.getMax() > selectedItem.getMin()) {
                randQuanity = (selectedItem.getMin() until selectedItem.getMax()).random()
            }

            return Item(selectedItem.getItemId(), randQuanity)
        }
    }
}