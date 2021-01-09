import bibliothek.gui.dock.common.CControl
import bibliothek.gui.dock.common.CGrid
import com.deflatedpickle.haruhi.api.Registry
import com.deflatedpickle.haruhi.api.constants.MenuCategory
import com.deflatedpickle.haruhi.api.plugin.AnnotationPluginDependencyComparator
import com.deflatedpickle.haruhi.api.plugin.Plugin
import com.deflatedpickle.haruhi.api.plugin.PluginType
import com.deflatedpickle.haruhi.api.util.ComponentPosition
import com.deflatedpickle.haruhi.component.PluginPanel
import com.deflatedpickle.haruhi.event.EventProgramFinishSetup
import com.deflatedpickle.haruhi.util.ClassGraphUtil
import com.deflatedpickle.haruhi.util.PluginUtil
import com.deflatedpickle.haruhi.util.RegistryUtil
import javax.swing.*

@Suppress("unused")
@Plugin(
    value = "simple_plugin",
    author = "DeflatedPickle",
    version = "1.0.0",
    type = PluginType.COMPONENT,
    component = SimpleComponent::class
)
object SimplePlugin {
    init {
        EventProgramFinishSetup.addListener {
            (RegistryUtil.get(MenuCategory.MENU.name)
                ?.get(MenuCategory.FILE.name) as JMenu).add("New")
        }
    }
}
object SimpleComponent : PluginPanel()

@Suppress("unused")
@Plugin(
    value = "other_simple_plugin",
    author = "DeflatedPickle",
    version = "1.0.0",
    type = PluginType.COMPONENT,
    component = OtherSimpleComponent::class,
    componentMinimizedPosition = ComponentPosition.EAST
)
object OtherSimplePlugin {
    init {
        EventProgramFinishSetup.addListener {
            (RegistryUtil.get(MenuCategory.MENU.name)
                ?.get(MenuCategory.EDIT.name) as JMenu).add("Undo")
        }
    }
}
object OtherSimpleComponent : PluginPanel()

fun main() {
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())

    PluginUtil.isInDev = true

    val menuBar = JMenuBar()
    val menuRegistry = object : Registry<String, JMenu>() {
        init {
            register(MenuCategory.FILE.name, JMenu("File"))
            register(MenuCategory.EDIT.name, JMenu("Edit"))
        }

        override fun register(key: String, value: JMenu) {
            super.register(key, value)
            menuBar.add(value)
        }
    } as Registry<String, Any>

    RegistryUtil.register(MenuCategory.MENU.name, menuRegistry)

    val tempRegistry = RegistryUtil.get(MenuCategory.MENU.name)
    tempRegistry?.register(MenuCategory.TOOLS.name, JMenu("Tools"))

    val frame = JFrame()
    frame.title = "Kotlin Example"
    frame.jMenuBar = menuBar

    val control = CControl(frame)
    PluginUtil.control = control

    val grid = CGrid(control)
    PluginUtil.grid = grid
    frame.add(control.contentArea)

    ClassGraphUtil.refresh()
    PluginUtil.discoverAnnotationPlugins()
    PluginUtil.discoveredPlugins.sortWith(AnnotationPluginDependencyComparator)
    PluginUtil.loadPlugins {
        PluginUtil.validateVersion(it) &&
                PluginUtil.validateDescription(it) &&
                PluginUtil.validateType(it) &&
                PluginUtil.validateDependencySlug(it) &&
                PluginUtil.validateDependencyExistence(it)
    }

    for (plugin in PluginUtil.discoveredPlugins) {
        if (plugin.component != Nothing::class) {
            with(plugin.component.objectInstance!!) {
                PluginUtil.createComponent(plugin, this)
            }
        }
    }

    EventProgramFinishSetup.trigger(true)

    SwingUtilities.invokeLater {
        frame.setLocationRelativeTo(null)

        control.contentArea.deploy(grid)

        frame.pack()
        frame.isVisible = true
    }
}