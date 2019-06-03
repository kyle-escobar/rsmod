package gg.rsmod.game.drops.model

class DropTable(npcDrops: MutableList<NPCDrops>?) {
    private var npcDrops: MutableList<NPCDrops> = npcDrops!!

    fun getNPCDrops(): MutableList<NPCDrops> { return this.npcDrops }

}