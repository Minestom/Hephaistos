# Hephaistos

This library is both a NBT library and a Minecraft Anvil format library.

Made in Kotlin, it is accessible for all languages that run on the JVM.

## NBT
Based on the up-to-date specs present at [Wiki.vg](https://wiki.vg/NBT) 
and on the [Minecraft Wiki](https://minecraft.gamepedia.com/NBT_format#TAG_definition). 

### Examples
While the tests in `src/test/java/nbt` can help comprehension, here are a few more examples to show the philosophy:

#### Reading 'servers.dat'
```java
// false represents the uncompressed status of the file
try (NBTReader reader = new NBTReader(new File("servers.dat"), false)) {
    NBTCompound tag = (NBTCompound) reader.read();
    NBTList<NBTCompound> servers = tag.getList("servers");
    for(NBTCompound server : servers) {
        String id = server.getString("ip");
        String name = server.getString("name");

        if(server.containsKey("acceptTextures")) {
            // ...
        }
        if(server.containsKey("icon")) {
            // ...
        }
        // Do something with the information
    }
} catch (IOException | NBTException e) {
    e.printStackTrace();
}
```


#### Saving to a file
```java
NBTCompound level = new NBTCompound();
NBTCompound data = new NBTCompound();
data.setByte("raining", (byte) 0);
data.setInt("SpawnX", 0);
data.setInt("SpawnZ", 0);
// ...
level.set("Data", data);

try(NBTWriter writer = new NBTWriter(new File("level.dat"), true /*compressed*/)) {
    writer.writeNamed("", level);
} catch (IOException e) {
    e.printStackTrace();
}
```

## Anvil format
Built upon the NBT library part

TODO - Under construction