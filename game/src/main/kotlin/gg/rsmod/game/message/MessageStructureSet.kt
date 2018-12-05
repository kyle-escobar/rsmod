package gg.rsmod.game.message

import gg.rsmod.game.message.impl.IgnoreMessage
import gg.rsmod.net.packet.*
import gg.rsmod.util.ServerProperties
import java.io.File
import java.util.*
import kotlin.collections.LinkedHashMap

/**
 * Stores all the [MessageStructure]s that are used on the
 * [gg.rsmod.game.service.GameService].
 *
 * @author Tom <rspsmods@gmail.com>
 */
class MessageStructureSet {

    /**
     * The [MessageStructure]s stored respectively to their [Class].
     */
    private val structureClasses = hashMapOf<Class<*>, MessageStructure>()

    /**
     * The [MessageStructure]s stored respectively to their opcode.
     */
    private val structureOpcodes = arrayOfNulls<MessageStructure>(255)

    fun get(type: Class<*>): MessageStructure? = structureClasses[type]

    fun get(opcode: Int): MessageStructure? = structureOpcodes[opcode]

    /**
     * Decodes the [packetStructures] [File]. The format is irrelevant as long
     * as the [structureClasses] is populated with correct data.
     */
    @Throws(Exception::class)
    fun load(packetStructures: File): MessageStructureSet {
        val properties = ServerProperties().loadYaml(packetStructures)
        val packets = properties.get<ArrayList<Any>>("packets")!!
        packets.forEach { packet ->
            val values = packet as LinkedHashMap<*, *>
            val className = values["message"] as String
            val packetType = if (values.containsKey("type")) PacketType.valueOf((values["type"] as String).toUpperCase()) else PacketType.IGNORE
            val packetOpcode = values["opcode"] as Int
            val clazz = Class.forName(className)
            val packetLength = if (values.containsKey("length")) values["length"] as Int else 0

            if (clazz::class.java != IgnoreMessage::class.java) {
                val packetStructure = if (values.containsKey("structure")) values["structure"] as ArrayList<*> else null
                val packetValues = LinkedHashMap<String, MessageValue>()
                packetStructure?.forEach { structure ->
                    val structValues = structure as LinkedHashMap<*, *>
                    val name = structValues["name"] as String
                    val order = if (structValues.containsKey("order")) DataOrder.valueOf(structValues["order"] as String) else DataOrder.BIG
                    val transform = if (structValues.containsKey("trans")) DataTransformation.valueOf(structValues["trans"] as String) else DataTransformation.NONE
                    val type = DataType.valueOf(structValues["type"] as String)
                    val signature = if (structValues.containsKey("sign")) DataSignature.valueOf(structValues["sign"] as String) else DataSignature.SIGNED
                    packetValues[name] = MessageValue(id = name, order = order, transformation = transform, type = type,
                            signature = signature)
                }
                val messageStructure = MessageStructure(type = packetType, opcode = packetOpcode, length = packetLength,
                        values = packetValues)
                structureClasses[clazz] = messageStructure
                structureOpcodes[packetOpcode] = messageStructure
            } else {
                val messageStructure = MessageStructure(type = packetType, opcode = packetOpcode,
                        length = packetLength, values = LinkedHashMap())
                structureClasses[clazz] = messageStructure
                structureOpcodes[packetOpcode] = messageStructure
            }
        }
        return this
    }
}