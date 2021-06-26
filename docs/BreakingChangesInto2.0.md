# Breaking changes from 1.x.y to 2.0.0
This list is probably non-exhaustive, but tries its best.

* `RegionFile` no longer expose a `RandomAccessFile` but a `DataSource`, which can be heap memory, or a file on disk (via `RandomAccessFile`). A constructor is provided to instantiate from a RandomAccessFile. 
* `ChunkColumn#sections` is no longer an array but a map. Java code will need to use `getSection(byte)`.
* `NBTNumber` no longer exist.
* `NBTByte#getValue()`, `NBTDouble#getValue()`, `NBTFloat#getValue`, `NBTInt#getValue()`, `NBTLong#getValue()`, 
  and `NBTShort#getValue()` may need require additional explicit unboxing in Java. It was implicit before, but due to 
  internal changes in Hephaistos and its usage of generics, the Kotlin compiler can no longer automatically unbox for you
  in Java code.
  
  If you want to keep avoiding unboxing, you can use the `getNumberValue()` available in each of the ImmutableNBT or MutableNBT
  versions of these tags.