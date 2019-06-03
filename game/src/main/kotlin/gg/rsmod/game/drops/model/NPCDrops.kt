package gg.rsmod.game.drops.model

class NPCDrops(npcId: Int, drops: MutableList<NPCDropItem?>) {
    private val npcId: Int = npcId
    private val drops: MutableList<NPCDropItem?> = drops

    fun getNpcId(): Int { return this.npcId }
    fun getDrops(): MutableList<NPCDropItem?> { return this.drops }

    fun getDrop(id: Int): NPCDropItem? { return this.drops.get(id) }
}