package gg.rsmod.game.message.impl

import gg.rsmod.game.message.Message

/**
 * @author Tom <rspsmods@gmail.com>
 */
data class ChangeDisplayModeMessage(val mode: Int, val width: Int, val height: Int) : Message