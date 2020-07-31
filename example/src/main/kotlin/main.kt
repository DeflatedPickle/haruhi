import bibliothek.gui.dock.common.CControl
import bibliothek.gui.dock.common.CGrid
import com.deflatedpickle.haruhi.api.plugin.DependencyComparator
import com.deflatedpickle.haruhi.api.plugin.Plugin
import com.deflatedpickle.haruhi.api.plugin.PluginType
import com.deflatedpickle.haruhi.component.PluginPanel
import com.deflatedpickle.haruhi.util.ClassGraphUtil
import com.deflatedpickle.haruhi.util.PluginUtil
import javax.swing.JFrame
import javax.swing.SwingUtilities
import javax.swing.UIManager

@Plugin(
    value = "simple_plugin",
    author = "DeflatedPickle",
    version = "1.0.0",
    type = PluginType.COMPONENT,
    component = SimpleComponent::class
)
object SimplePlugin

object SimpleComponent : PluginPanel()

fun main() {
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())

    PluginUtil.isInDev = true

    val frame = JFrame()
    frame.title = "Kotlin Example"

    val control = CControl(frame)
    PluginUtil.control = control

    val grid = CGrid(control)
    frame.add(control.contentArea)

    ClassGraphUtil.refresh()
    PluginUtil.discoverPlugins()
    PluginUtil.discoveredPlugins.sortWith(DependencyComparator)
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

    SwingUtilities.invokeLater {
        frame.setLocationRelativeTo(null)

        control.contentArea.deploy(grid)

        frame.pack()
        frame.isVisible = true
    }
}