package day_20

import getResourceFile

const val BUTTON_PRESSES = 1000

enum class Pulse {
    HIGH, LOW
}

abstract class Module {
    companion object {
        var pulsesSent = mutableMapOf<Pulse, Long>()

        fun fromString(string: String) = if (string == "broadcaster") Broadcaster() else when (string[0]) {
            '%' -> FlipFlop(string.drop(1))
            '&' -> Conjunction(string.drop(1))
            else -> null
        }
    }

    abstract val id: String;

    val connections: MutableSet<Module> = mutableSetOf()

    fun receivePulse(pulse: Pulse, source: String) {
        pulsesSent[pulse] = (pulsesSent[pulse] ?: 0) + 1
        println("$source -$pulse->$id - H: ${pulsesSent[Pulse.HIGH]} - L: ${pulsesSent[Pulse.LOW]}")
        handleReceivePulse(pulse, source)
    }

    abstract fun handleReceivePulse(pulse: Pulse, source: String)

    abstract fun sendPulses()

    fun addConnection(module: Module) {
        connections += module
    }
}

data class FlipFlop(override val id: String, var isOn: Boolean = false, var incoming: Pulse = Pulse.HIGH) : Module() {
    override fun handleReceivePulse(pulse: Pulse, source: String) {
        incoming = pulse
        if (incoming == Pulse.HIGH) return

        isOn = !isOn
    }

    override fun sendPulses() {
        if (incoming == Pulse.HIGH) return

        val pulse = if (isOn) Pulse.HIGH else Pulse.LOW

        connections.forEach {
            it.receivePulse(pulse, id)
        }
        connections.forEach {
            it.sendPulses()
        }
    }

}

data class Conjunction(
    override val id: String, val inputs: MutableMap<String, Pulse> = mutableMapOf()
) : Module() {
    override fun handleReceivePulse(pulse: Pulse, source: String) {
        inputs[source] = pulse
    }

    override fun sendPulses() {
        val pulse = if (inputs.all{(_, value) -> value == Pulse.HIGH}) Pulse.LOW else Pulse.HIGH

        connections.forEach { it.receivePulse(pulse, id) }
        connections.forEach { it.sendPulses() }
    }
}

data class Broadcaster(override val id: String = "broadcaster", var lastPulse: Pulse = Pulse.LOW) : Module() {
    override fun handleReceivePulse(pulse: Pulse, source: String) {
        lastPulse = pulse
    }

    override fun sendPulses() {
        connections.forEach { it.receivePulse(lastPulse, id) }
        connections.forEach { it.sendPulses() }
    }
}

fun main() {
    val input = getResourceFile("day_20/example.txt").readLines()

    val modules = mutableMapOf<String, Module>()
    val connections = mutableMapOf<String, Collection<String>>()

    for (line in input) {
        val (identifier, connectionList) = line.split(" -> ")

        connections[if (identifier == "broadcaster") identifier else identifier.drop(1)] = connectionList.split(", ")

        modules[if (identifier == "broadcaster") identifier else identifier.drop(1)] =
            Module.fromString(identifier) ?: continue
    }

    connections.forEach { (identifier, conList) -> conList.forEach { modules[it]?.let{module -> modules[identifier]?.addConnection(module)}} }
    val broadcaster = modules["broadcaster"]!!

    for (i in 0..<BUTTON_PRESSES) {
        broadcaster.receivePulse(Pulse.LOW, source = "button")
        broadcaster.sendPulses()
    }

    modules.forEach { (key, conj) ->
        if (conj is Conjunction) {
            modules.filterValues { conj in it.connections }.keys.forEach { conj.inputs += it to Pulse.LOW }
        }
    }

    println(Module.pulsesSent)
    println("Multiplied: ${Module.pulsesSent.values.reduce { acc, cur -> acc * cur }}")
}