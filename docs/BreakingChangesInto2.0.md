# Breaking changes from 1.x.y to 2.0.0

* `RegionFile` no longer expose a `RandomAccessFile` but a `DataSource`, which can be heap memory, or a file on disk (via `RandomAccessFile`). A constructor is provided to instantiate from a RandomAccessFile. 
* `ChunkColumn#sections` is no longer an array but a map. Java code will need to use `getSection(byte)`.