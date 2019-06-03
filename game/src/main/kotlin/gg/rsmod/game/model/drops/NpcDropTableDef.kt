package gg.rsmod.game.model.drops

import gg.rsmod.game.drops.model.DropTable

data class NpcDropTableDef(
        val dropTable: DropTable?
) {
    companion object {
        val DEFAULT = NpcDropTableDef(null)
    }
}