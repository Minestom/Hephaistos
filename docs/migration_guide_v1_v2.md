# Upgrading guide from Hephaistos v1 to v2

This guide was made during all changes made to [Minestom](https://github.com/Minestom/Minestom), while I updated its Hephaistos to version v2.1.2. I hope this guide will be clear enough to provide an easy migration.

The guide will give hints with how to apply the changes via IntelliJ IDEA, but any IDE would work.

* `org.jglrxavpok.hephaistos.nbt.SNBTParser` becomes `org.jglrxavpok.hephaistos.parser.SNBTParser`. 'Replace in Files' (Control+Shift+R) is enough.
* `org.jglrxavpok.hephaistos.nbt.NBTTypes` become `org.jglrxavpok.hephaistos.nbt.NBTType`. 'Replace in Files' (Control+Shift+R) is enough.
* `NBTEnd` is now a Kotlin `object`. This means `NBTEnd` is now a singleton, creating new instances is not allowed. Use `NBTEnd.INSTANCE` instead.
* `NBTWriter` and `NBTReader` constructors no longer take a `boolean` as a second parameter to determine whether the contents are compressed or not.
	If you used no compression (`false` second argument), you can simply drop the second argument and only provide the output.
	If you did use compression, select a `CompressedProcessor`. Three are available by default: `CompressedProcessor.NONE`, `CompressedProcessor.GZIP` and `CompressedProcessor.ZLIB`.

* `NBT#deepClone` no longer exists, because tags are now immutable.

* `NBTCompound` is no longer mutable. `NBT.Compound` is used to build a NBTCompound now.
	If you are writing a compound from scratch, use this example as a base:
	  
Old version:
```java
@Override
public @NotNull Component serializeShowEntity(HoverEvent.@NotNull ShowEntity input, Codec.Encoder<Component, String, ? extends RuntimeException> componentEncoder) throws IOException {
    final NBTCompound tag = new NBTCompound();
    tag.setString(ENTITY_ID, input.id().toString());
    tag.setString(ENTITY_TYPE, input.type().asString());

    final Component name = input.name();
    if (name != null) {
        tag.setString(ENTITY_NAME, componentEncoder.encode(name));
    }

    return Component.text(MinestomAdventure.NBT_CODEC.encode(tag));
}
```

New version:
```java
@Override
public @NotNull Component serializeShowEntity(HoverEvent.@NotNull ShowEntity input, Codec.Encoder<Component, String, ? extends RuntimeException> componentEncoder) throws IOException {
    final NBTCompound tag = NBT.Compound(t -> {
        t.setString(ENTITY_ID, input.id().toString());
        t.setString(ENTITY_TYPE, input.type().asString());

        final Component name = input.name();
        if (name != null) {
            t.setString(ENTITY_NAME, componentEncoder.encode(name));
        }
    });

    return Component.text(MinestomAdventure.NBT_CODEC.encode(tag));
}
```

* `NBTList` is no longer mutable. Use `NBT.List` to create a list:
  
Old:
```java
List<@NotNull Component> lore; // your list
// ...
final NBTList<NBTString> loreNBT = NBT.List<>(NBTType.TAG_String);
for (Component line : lore) {
    loreNBT.add(new NBTString(GsonComponentSerializer.gson().serialize(line)));
}
```

New:
```java
List<@NotNull Component> lore; // your list
// ...
final NBTList<NBTString> loreNBT = NBT.List(NBTType.TAG_String,
        lore.stream()
                .map(line -> new NBTString(GsonComponentSerializer.gson().serialize(line)))
                .collect(Collectors.toList())
);
```

* `NBTCompound#getByteArray`, `NBTCompound#getIntArray`, `NBTCompound#getLongArray`, now respectively return an `ImmutableByteArray`, `ImmutableIntArray`, or an `ImmutableLongArray`.

* `ChunkColumn#getSections` no longer returns an array, but a map due to negative Y now being allowed (1.17+).
This kind of code no longer compiles:
```java
ChunkColumn chunk;
for (var section : chunk.getSections()) {
	// do something
}
```
You will need to use `.values()` to make it work:
```java
ChunkColumn chunk;
for (var section : chunk.getSections().values()) {
	// do something
}
```


## Additionnal notes

* `NBTCompound#removeTag` no longer exists (NBTCompound is now immutable). `MutableNBTCompound` exposes `remove(String)` instead.