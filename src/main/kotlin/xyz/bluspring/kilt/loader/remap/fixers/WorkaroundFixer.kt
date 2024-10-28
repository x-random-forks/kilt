package xyz.bluspring.kilt.loader.remap.fixers

import org.objectweb.asm.tree.*
import xyz.bluspring.kilt.loader.remap.KiltRemapper

object WorkaroundFixer {
    private val potionBrewingMapped = KiltRemapper.remapClass("net/minecraft/world/item/alchemy/PotionBrewing\$Mix")

    fun fixClass(classNode: ClassNode) {
        val methodReplace = mutableListOf<MethodNode>()

        for (method in classNode.methods) {
            val newNodeMap = mutableMapOf<AbstractInsnNode, AbstractInsnNode>()

            for (insnNode in method.instructions) {
                if (insnNode is MethodInsnNode && insnNode.owner == "net/minecraftforge/fluids/FluidStack") {
                    if (insnNode.name == "getAmount") {
                        val node = MethodInsnNode(insnNode.opcode, "net/minecraftforge/fluids/FluidStack", "forge\$getAmount", insnNode.desc)
                        newNodeMap[insnNode] = node
                    } else if (insnNode.name == "writeToPacket") {
                        val node = MethodInsnNode(insnNode.opcode, "net/minecraftforge/fluids/FluidStack", "forge\$writeToPacket", insnNode.desc)
                        newNodeMap[insnNode] = node
                    }
                } else if (insnNode is FieldInsnNode && insnNode.owner == potionBrewingMapped) {
                    if (insnNode.name == "field_8962" || insnNode.name == "from") {
                        val node = FieldInsnNode(insnNode.opcode, insnNode.owner, "kilt\$from", insnNode.desc)
                        newNodeMap[insnNode] = node
                    } else if (insnNode.name == "field_8961" || insnNode.name == "to") {
                        val node = FieldInsnNode(insnNode.opcode, insnNode.owner, "kilt\$to", insnNode.desc)
                        newNodeMap[insnNode] = node
                    }
                }
            }

            if (newNodeMap.isNotEmpty()) {
                for ((oldNode, newNode) in newNodeMap) {
                    method.instructions.set(oldNode, newNode)
                }

                methodReplace.add(method)
            }
        }

        classNode.methods.removeIf { methodReplace.any { a -> it.name == a.name && a.desc == it.desc } }
        classNode.methods.addAll(methodReplace)
    }
}