# haruhi
A plugin framework for Swing, with a focus on dockable components, configuration and an events system

## Examples
Currently, the most developed example using `haruhi` is the second version of [Quiver](https://github.com/DeflatedPickle/Quiver), which started originally as nothing more than a second project to make sure I didn't make `haruhi` fit for only a single program. The first program being the [Rawky](https://github.com/DeflatedPickle/Rawky) rewrite, which has ironically almost been forgotten, dispite being the project to spawn it. There is also the [sniffle](https://github.com/DeflatedPickle/sniffle) project, providing common and universal plugins. This repository also contains [a barebones example of a few plugins and a launcher](https://github.com/DeflatedPickle/haruhi/blob/master/example/src/main/kotlin/main.kt), though it isn't very exciting

## Documentation
Though `haruhi` doesn't have it's own documentation yet, [the wiki for Rawky](https://github.com/DeflatedPickle/Rawky/wiki/Custom-Plugin) can be followed quite closely, with only the imports changing a bit (mostly to rename references of `rawky` to `haruhi`), and the lauchers [from Quiver](https://github.com/DeflatedPickle/Quiver/blob/rewrite/launcher/src/main/kotlin/com/deflatedpickle/quiver/launcher/main.kt) and [from Rawky](https://github.com/DeflatedPickle/Rawky/blob/rewrite/launcher/src/main/kotlin/com/deflatedpickle/rawky/launcher/main.kt) can be used as reference for starting up everything that `haruhi` needs

## History
`haruhi` started life, not as it's own project but as a part of the rewrite of [Rawky](https://github.com/DeflatedPickle/Rawky). After writing so much for it, I decided that it would be useful for any other Swing programs I, or anyone else, happened to write in future. Mostly because plugins could be shared between programs that used `haruhi` (so long as guidelines where followed), meaning I could end the battle of writing another way to change Swing settings. Though the learning process had a lot more to it, which I consider to be shards of an earlier `haruhi`. The closest being the docking and config system from the original Rawky, though an earlier, rougher plugin and config system existed for [FAOSDance](https://github.com/DeflatedPickle/FAOSDance) that used [JRuby](https://www.jruby.org/) for plugin scripting and [TOML](https://toml.io/en/) for configuration files, but before that was [Wheeze](https://github.com/DeflatedPickle/Wheeze) that used NetRexx to provide scriptable brushes
