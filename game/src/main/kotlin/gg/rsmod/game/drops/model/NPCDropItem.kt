package gg.rsmod.game.drops.model

class NPCDropItem(itemId: Int, min: Int, max: Int, dropTableChance: NPCDropChance, isRareTable: Boolean) {
    private val itemId: Int = itemId
    private val min: Int = min
    private val max: Int = max
    private val dropTableChance: NPCDropChance = dropTableChance
    private val isRareTable: Boolean = isRareTable

    fun getItemId(): Int { return this.itemId }
    fun getMin(): Int { return this.min }
    fun getMax(): Int { return this.max }
    fun getTableChance(): NPCDropChance { return this.dropTableChance }
    fun isRareTable(): Boolean { return this.isRareTable }
}