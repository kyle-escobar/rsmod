package gg.rsmod.plugins.api.dsl

import gg.rsmod.game.plugin.KotlinPlugin
import gg.rsmod.plugins.api.NpcDropTableBuilder

fun KotlinPlugin.drop_tables(npc: Int, init: NpcDropTableDsl.Builder.() -> Unit) {
    val builder = NpcDropTableDsl.Builder()
    init(builder)

    drop_tables(npc, builder.build())
}

object NpcDropTableDsl {
    @DslMarker
    annotation class DropTableDslMarker

    @DropTableDslMarker
    class Builder {
        val dropTableBuilder = NpcDropTableBuilder()

        fun build() = dropTableBuilder.build()

        fun config(init: ConfigBuilder.() -> Unit) {
            val builder = ConfigBuilder()
            init(builder)

            dropTableBuilder.setFile(builder.drop_table_file)
        }

    }

    @DropTableDslMarker
    class ConfigBuilder {
        var drop_table_file = ""
    }
}