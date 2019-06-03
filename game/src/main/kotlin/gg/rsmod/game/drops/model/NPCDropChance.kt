package gg.rsmod.game.drops.model

enum class NPCDropChance(dropTableIndex: Int) {
    ALWAYS(0),
    COMMON(1),
    UNCOMMON(2),
    RARE(3),
    VERY_RARE(4);

    private val dropTableIndex: Int = dropTableIndex

    fun getDropTableIndex(): Int {
        return this.dropTableIndex
    }

    fun getById(id: Int): NPCDropChance? {
        for(i in values()) {
            if(i.getDropTableIndex() == id) {
                return i
            }
        }
        return null
    }
}