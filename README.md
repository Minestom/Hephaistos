# Hephaistos
![](https://github.com/jglrxavpok/Hephaistos/workflows/Gradle%20Build/badge.svg)

![](https://github.com/jglrxavpok/Hephaistos/workflows/Gradle%20Tests/badge.svg)

This library is both a NBT library and a Minecraft Anvil format library.

Made in Kotlin, it is accessible for all languages that run on the JVM.
As it is in Kotlin, this means your project may have to add a new dependency on the Kotlin runtime library.

Hephaistos can read NBT from SNBT, binary format or (with additional dependencies) JSON.
See more in the "parsing" section below for more information.

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

## Parsing
Hephaistos can parse and write NBT data from/to SNBT, NBT binary format or JSON.

### NBT binary format
Use `NBTWriter#writeNamed` and `NBTReader#read()`.

### SNBT format
If you want to read SNBT, use `SNBTParser#parse` with a `Reader` which contains the SNBT string to parse.

If you want to write SNBT, simply use the `toSNBT()` method on any `NBT` instance.

### JSON format
Using JSON requires an additional library. For the moment, only Gson is supported by Hephaistos.

To access the Gson-based reader and writer, you will need to add a new dependency in your `build.gradle` file (example with Jitpack.io names):
```groovy

dependencies {
    api("com.github.jglrxavpok:Hephaistos:${project.hephaistos_version}")
    implementation("com.github.jglrxavpok:Hephaistos:${project.hephaistos_version}:gson")
    implementation("com.github.jglrxavpok:Hephaistos:${project.hephaistos_version}") {
        capabilities {
            requireCapability("org.jglrxavpok.nbt:Hephaistos-gson")
        }
    }
}
```

## Anvil format
Built upon the NBT library part, the `mca` package allows loading and saving MCA (Minecraft Anvil) files.

Contrary to `NBTReader` and `NBTWriter`, `RegionFile` requires a `RandomAccessFile` instead of a InputStream/OutputStream.
The reasoning is that `RandomAccessFile` allows both reads and writes to happen at the same time on the same file.

One must be careful of OutOfMemory errors when reading lots of chunks from a RegionFile. Use `RegionFile#forget` to unload a chunk column from the internal chunk cache to relieve memory.

Finally, Hephaistos allows you to load and save Entities/TileEntities/Lighting but provide very little in the way of support for these features, you will have to make sure your entities are correct by yourself.
In the event that you find that a convenience method would help you in your work, do not hesitate to submit a Pull Request or post an issue on this Github repository.

Here comes the part you are all waiting for, examples!

### Examples

#### Creating a RegionFile/MCAFile from scratch

It is possible to set blocks directly from `RegionFile`:
```java
            // create the region from a given RandomAccessFile. 0,0 is the region coordinates (a region is 32x32 chunks)
            RegionFile region = new RegionFile(file, 0, 0);
            BlockState stone = new BlockState("minecraft:stone");
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    for (int y = 0; y < 256; y++) {
                        // Sets the block inside the 0,0 and 1,0 chunks
                        region.setBlockState(x, y, z, BlockState.Air);
                        region.setBlockState(x + 16, y, z, stone);
                    }
                }
            }
            // save chunks that are in memory (automatically generated via setBlockState) to disk
            // without this line, your chunks will NOT be saved to disk
            region.flushCachedChunks();
```

It is also possible to create chunks on-demand:
```java
            RegionFile region = new RegionFile(file, 0, 0);
            ChunkColumn chunk0 = region.getOrCreateChunk(0, 0);
            ChunkColumn chunk1 = region.getOrCreateChunk(1, 0);
            // just 3 for-loops over the entire chunk to set the blocks via ChunkColumn#setBlockState
            fillChunk(chunk0, BlockState.Air);
            fillChunk(chunk1, new BlockState("minecraft:stone"));
            // write the chunks to disk. It is also possible to use flushCachedChunks() like in 
            // the previous example, but you can select which chunks to save
            region.writeColumn(chunk0);
            region.writeColumn(chunk1);
```

#### Reading from a pre-existing RegionFile/MCAFile
```java
        // Load your region
        RegionFile region = new RegionFile(file, 0, 0);
        // Get your chunk. May return null if the chunk is not present in the file 
        // (ie not generated yet)
        ChunkColumn column0_0 = region.getChunk(0, 0);
        if(column0_0 == null) {
            // throw or whatever
        }
    
        // print all blocks at x,0,z in the ChunkColumn 
        // in an usual Minecraft world, all "minecraft:bedrock" in the Nether or overworld
        // XYZ are chunk local (ie in a 16x256x16 cube)
        // The method WILL throw IllegalArgumentException if XYZ are not valid
        for (int z = 0; z < 16; z++) {
            for (int x = 0; x < 16; x++) {
                System.out.println(column0_0.getBlockState(x, 0, z).getName());
                // a Map<String, String> is available in BlockState#getProperties 
                // to analyse the properties of the block state (like 'lit' or 'facing' for a furnace) 
            }
        }
    
        // both following methods return a NBTList<NBTCompound> with the data from entities/tileEntities
        // it is allowed to modify these lists directly to alter the data for later-saving
        // other methods in the like exist for other features (ticks, heightmaps, etc.) 
        column0_0.getTileEntities()
        column0_0.getEntities()
```