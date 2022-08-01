import imgui.ImGui
import imgui.ImVec2
import imgui.app.Application
import imgui.app.Configuration
import imgui.flag.ImGuiWindowFlags
import org.jglrxavpok.hephaistos.mca.RegionFile
import org.jglrxavpok.hephaistos.nbt.*
import java.awt.Desktop
import java.io.File
import java.io.RandomAccessFile
import javax.swing.JFileChooser

class ViewerApp : Application() {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(ViewerApp())
        }

        private val WhichChunkPopupID = "Which chunk inside region?"
    }

    private var showChunkPopup = false
    private var tryingToOpen = File("")
    private var nbt = NBTCompound()

    override fun configure(config: Configuration) {
        config.title = "Hephaistos NBT Viewer"
    }

    private fun open(f: File) {
        if("mca" == f.extension) {
            tryingToOpen = f
            showChunkPopup = true
            return
        }

        TODO()
    }

    private fun drawPopups() {
        if(showChunkPopup) {
            ImGui.openPopup(WhichChunkPopupID)
        }

        if(ImGui.beginPopupModal(WhichChunkPopupID)) {
            val array by lazy { IntArray(2) }
            ImGui.dragInt2("Chunk coordinates", array)

            ImGui.separator()
            if(ImGui.button("Cancel")) {
                showChunkPopup = false
                ImGui.closeCurrentPopup()
            }
            ImGui.sameLine();
            if(ImGui.button("OK")) {
                val region = RegionFile(RandomAccessFile(tryingToOpen, "r"), 0, 0)
                nbt = region.getChunkData(array[0], array[1]) ?: NBTCompound()

                showChunkPopup = false
                ImGui.closeCurrentPopup()
            }

            ImGui.endPopup()
        }
    }

    private fun drawMainBar() {
        if(ImGui.beginMainMenuBar()) {
            if(ImGui.beginMenu("File")) {
                if(ImGui.menuItem("Open")) {
                    val fileChooser = JFileChooser(File("."))
                    val returnValue = fileChooser.showOpenDialog(null)
                    if(returnValue == JFileChooser.APPROVE_OPTION) {
                        open(fileChooser.selectedFile)
                    }
                }

                ImGui.endMenu();
            }

            ImGui.endMainMenuBar();
        }
    }

    private fun drawEntry(name: String, nbt: NBT) {
        when (nbt.ID) {
            NBTType.TAG_Compound -> {
                if(ImGui.treeNode(name)) {
                    drawTree(nbt as NBTCompound)
                    ImGui.treePop()
                }
            }
            NBTType.TAG_List -> {
                if(ImGui.treeNode(name)) {
                    val asList = nbt as NBTList<out NBT>
                    for ((i, entry) in asList.withIndex()) {
                        drawEntry("[$i]", entry)
                    }
                    ImGui.treePop()
                }
            }
            NBTType.TAG_Byte -> {
                ImGui.bulletText(name)
                val value = IntArray(1)
                value[0] = (nbt as NBTByte).value.toInt()

                ImGui.sameLine()
                ImGui.setNextItemWidth(100f);
                ImGui.dragInt("##value", value, 1.0f, -128f, 128f)
                // TODO: make it modifiable
            }
            NBTType.TAG_Int_Array -> {
                val asArray = nbt as NBTIntArray
                if(ImGui.treeNode(name + " Int[${asArray.size}]")) {
                    for ((i, entry) in asArray.withIndex()) {
                        ImGui.bulletText("[$i] = $entry")
                    }
                    ImGui.treePop()
                }
            }
            NBTType.TAG_Long_Array -> {
                val asArray = nbt as NBTLongArray
                if(ImGui.treeNode(name + " Long[${asArray.size}]")) {
                    for ((i, entry) in asArray.withIndex()) {
                        ImGui.bulletText("[$i] = $entry")
                    }
                    ImGui.treePop()
                }
            }
            NBTType.TAG_Byte_Array -> {
                val asArray = nbt as NBTByteArray
                if(ImGui.treeNode(name + " Byte[${asArray.size}]")) {
                    for ((i, entry) in asArray.withIndex()) {
                        ImGui.bulletText("[$i] = $entry")
                    }
                    ImGui.treePop()
                }
            }
            else -> {
                ImGui.bulletText(name)
                ImGui.sameLine()

                // TODO:
                ImGui.text(nbt.toSNBT())
            }
        }
    }

    private fun drawTree(compound: NBTCompound) {
        ImGui.pushID(compound.toSNBT())
        for((name, entry) in compound) {
            drawEntry(name, entry)
        }
        ImGui.popID()
    }

    override fun process() {
        val size = ImGui.getMainViewport().size

        drawMainBar();
        val mainBarHeight = ImGui.getCursorPos().y
        ImGui.setNextWindowPos(0f, mainBarHeight)
        ImGui.setNextWindowSize(size.x, size.y-mainBarHeight)

        if(ImGui.begin("main window", ImGuiWindowFlags.NoDecoration or ImGuiWindowFlags.NoMove)) {
            drawTree(nbt)
        }

        drawPopups()
        ImGui.end();
    }

}